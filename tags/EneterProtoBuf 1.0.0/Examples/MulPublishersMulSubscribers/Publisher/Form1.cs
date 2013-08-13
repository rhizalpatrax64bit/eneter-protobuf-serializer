using System;
using System.Windows.Forms;
using Eneter.Messaging.DataProcessing.Serializing;
using Eneter.Messaging.MessagingSystems.MessagingSystemBase;
using Eneter.Messaging.MessagingSystems.TcpMessagingSystem;
using Eneter.Messaging.Nodes.Broker;
using Eneter.ProtoBuf;
using ProtoBuf;

namespace Publisher
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

            // Create broker client responsible for sending messages to the broker.
            IDuplexBrokerFactory aBrokerFactory = new DuplexBrokerFactory(mySerializer);
            myBrokerClient = aBrokerFactory.CreateBrokerClient();

            // Create output channel to send messages via Tcp.
            IMessagingSystemFactory aMessaging = new TcpMessagingSystemFactory();
            myOutputChannel = aMessaging.CreateDuplexOutputChannel("tcp://127.0.0.1:7091/");

            // Attach the output channel to the broker client to be able to send messages.
            myBrokerClient.AttachDuplexOutputChannel(myOutputChannel);
        }

        // Correctly close the output channel.
        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            // Note: The duplex output channel can receive response messages too.
            //       Therefore we must close it to stop the thread receiving response messages.
            //       If the thred is not closed then the application could not be correctly closed.
            myBrokerClient.DetachDuplexOutputChannel();
            myOutputChannel.CloseConnection();
        }

        // Send NotifyMsg1
        private void Notify1Btn_Click(object sender, EventArgs e)
        {
            NotifyMsg1 aMsg = new NotifyMsg1();
            aMsg.CurrentTime = DateTime.Now;

            object aSerializedMsg = mySerializer.Serialize<NotifyMsg1>(aMsg);

            myBrokerClient.SendMessage("MyNotifyMsg1", aSerializedMsg);
        }

        // Send NotifyMsg2
        private void Notify2Btn_Click(object sender, EventArgs e)
        {
            NotifyMsg2 aMsg = new NotifyMsg2();
            aMsg.Number = 12345;

            object aSerializedMsg = mySerializer.Serialize<NotifyMsg2>(aMsg);

            myBrokerClient.SendMessage("MyNotifyMsg2", aSerializedMsg);
        }

        // Send NotifyMsg3
        private void Notify3Btn_Click(object sender, EventArgs e)
        {
            NotifyMsg3 aMsg = new NotifyMsg3();
            aMsg.TextMessage = "My notifying text message.";

            object aSerializedMsg = mySerializer.Serialize<NotifyMsg3>(aMsg);

            myBrokerClient.SendMessage("MyNotifyMsg3", aSerializedMsg);
        }


        // Broker client is used to send messages to the broker,
        // that forwards messages to subscribers.
        private IDuplexBrokerClient myBrokerClient;

        // The output channel used by the broker client to send messages to the broker.
        private IDuplexOutputChannel myOutputChannel;

        // Serializer used to serialize notification messages.
        private ISerializer mySerializer;
    }
}
