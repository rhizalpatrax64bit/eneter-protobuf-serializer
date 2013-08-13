using System;
using System.Windows.Forms;
using Eneter.Messaging.DataProcessing.Serializing;
using Eneter.Messaging.MessagingSystems.MessagingSystemBase;
using Eneter.Messaging.MessagingSystems.TcpMessagingSystem;
using Eneter.Messaging.Nodes.Broker;
using ProtoBuf;
using Eneter.ProtoBuf;

namespace Subscriber
{
    public partial class Form1 : Form
    {
        // Notification message 1
        [ProtoContract]
        public class NotifyMsg1
        {
            [ProtoMember(1)]
            public DateTime CurrentTime { get; set; }
        }

        // Notification message 2
        [ProtoContract]
        public class NotifyMsg2
        {
            [ProtoMember(1)]
            public int Number { get; set; }
        }

        // Notification message 3
        [ProtoContract]
        public class NotifyMsg3
        {
            [ProtoMember(1)]
            public string TextMessage { get; set; }
        }

        public Form1()
        {
            InitializeComponent();

            // Create ProtoBuf serializer.
            mySerializer = new ProtoBufSerializer();

            // Create the broker client that will receive notification messages.
            IDuplexBrokerFactory aBrokerFactory = new DuplexBrokerFactory(mySerializer);
            myBrokerClient = aBrokerFactory.CreateBrokerClient();
            myBrokerClient.BrokerMessageReceived += OnNotificationMessageReceived;

            // Create the Tcp messaging for the communication with the publisher.
            // Note: For the interprocess communication you can use: Tcp, NamedPipes and Http.
            IMessagingSystemFactory aMessagingFactory = new TcpMessagingSystemFactory();

            // Create duplex output channel for the communication with the publisher.
            // Note: The duplex output channel can send requests and receive responses.
            //       In our case, the broker client will send requests to subscribe/unsubscribe
            //       and receive notifications as response messages.
            myOutputChannel = aMessagingFactory.CreateDuplexOutputChannel("tcp://127.0.0.1:7091/");

            // Attach the output channel to the broker client
            myBrokerClient.AttachDuplexOutputChannel(myOutputChannel);
        }

        // Correctly close the communication.
        // Note: If the communication is not correctly closed, the thread listening to
        //       response messages will not be closed.
        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            myBrokerClient.DetachDuplexOutputChannel();
            myOutputChannel.CloseConnection();
        }

        // Method processing notification messages from the publisher.
        private void OnNotificationMessageReceived(object sender, BrokerMessageReceivedEventArgs e)
        {
            // The notification event does not come in UI thread.
            // Therefore, if we want to work with UI controls we must execute it in the UI thread.
            InvokeInUIThread(() =>
                {
                    if (e.ReceivingError == null)
                    {
                        if (e.MessageTypeId == "MyNotifyMsg1")
                        {
                            NotifyMsg1 aDeserializedMsg = mySerializer.Deserialize<NotifyMsg1>(e.Message);
                            Received1TextBox.Text = aDeserializedMsg.CurrentTime.ToString();
                        }
                        else if (e.MessageTypeId == "MyNotifyMsg2")
                        {
                            NotifyMsg2 aDeserializedMsg = mySerializer.Deserialize<NotifyMsg2>(e.Message);
                            Received2TextBox.Text = aDeserializedMsg.Number.ToString();
                        }
                        else if (e.MessageTypeId == "MyNotifyMsg3")
                        {
                            NotifyMsg3 aDeserializedMsg = mySerializer.Deserialize<NotifyMsg3>(e.Message);
                            Received3TextBox.Text = aDeserializedMsg.TextMessage;
                        }
                    }
                });
        }

        // Subscribe to notification message 1
        private void Subscribe1Btn_Click(object sender, EventArgs e)
        {
            myBrokerClient.Subscribe("MyNotifyMsg1");
        }

        // Unsubscribe from notification message 1
        private void Unsubscribe1Btn_Click(object sender, EventArgs e)
        {
            Received1TextBox.Text = "";
            myBrokerClient.Unsubscribe("MyNotifyMsg1");
        }

        // Subscribe to notification message 2
        private void Subscribe2Btn_Click(object sender, EventArgs e)
        {
            myBrokerClient.Subscribe("MyNotifyMsg2");
        }

        // Unsubscribe from notification message 2
        private void Unsubscribe2Btn_Click(object sender, EventArgs e)
        {
            Received2TextBox.Text = "";
            myBrokerClient.Unsubscribe("MyNotifyMsg2");
        }

        // Subscribe to notification message 3
        private void Subscribe3Btn_Click(object sender, EventArgs e)
        {
            myBrokerClient.Subscribe("MyNotifyMsg3");
        }

        // Unsubscribe from notification message 3
        private void Unsubscribe3Btn_Click(object sender, EventArgs e)
        {
            Received3TextBox.Text = "";
            myBrokerClient.Unsubscribe("MyNotifyMsg3");
        }

        // Helper method to invoke some functionality in UI thread.
        private void InvokeInUIThread(Action uiMethod)
        {
            // If we are not in the UI thread then we must synchronize via the invoke mechanism.
            if (InvokeRequired)
            {
                Invoke(uiMethod);
            }
            else
            {
                uiMethod();
            }
        }


        // BrokerClient provides the communication with the broker.
        private IDuplexBrokerClient myBrokerClient;

        // The output channel used by the broker client to send messages to the broker.
        private IDuplexOutputChannel myOutputChannel;

        // Serializer used to sdeerialize notification messages.
        private ISerializer mySerializer;
    }
}
