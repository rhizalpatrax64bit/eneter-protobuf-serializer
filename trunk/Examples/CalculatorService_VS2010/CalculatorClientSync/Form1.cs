using System;
using System.Windows.Forms;
using Eneter.Messaging.DataProcessing.Serializing;
using Eneter.Messaging.EndPoints.TypedMessages;
using Eneter.Messaging.MessagingSystems.MessagingSystemBase;
using Eneter.Messaging.MessagingSystems.TcpMessagingSystem;
using Eneter.ProtoBuf;
using ProtoBuf;

namespace CalculatorClientSync
{
    public partial class Form1 : Form
    {
        // Request message.
        [ProtoContract]
        public class RequestMessage
        {
            [ProtoMember(1)]
            public int Number1 { get; set; }

            [ProtoMember(2)]
            public int Number2 { get; set; }
        }

        // Response message.
        [ProtoContract]
        public class ResponseMessage
        {
            [ProtoMember(1)]
            public int Result { get; set; }
        }

        private ISyncDuplexTypedMessageSender<ResponseMessage, RequestMessage> mySender;

        public Form1()
        {
            InitializeComponent();

            OpenConnection();
        }

        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            CloseConnection();
        }

        private void OpenConnection()
        {
            // Create Protocol Buffers serializer.
            ISerializer aSerializer = new ProtoBufSerializer();

            // Create the synchronous message sender.
            // It will wait max 5 seconds for the response.
            // To wait infinite time use TimeSpan.FromMiliseconds(-1) or
            // default constructor new DuplexTypedMessagesFactory()
            IDuplexTypedMessagesFactory aSenderFactory =
                new DuplexTypedMessagesFactory(TimeSpan.FromSeconds(5), aSerializer);
            mySender = aSenderFactory.CreateSyncDuplexTypedMessageSender<ResponseMessage, RequestMessage>();

            // Use TCP for the communication.
            IMessagingSystemFactory aMessaging = new TcpMessagingSystemFactory();
            IDuplexOutputChannel anOutputChannel =
                aMessaging.CreateDuplexOutputChannel("tcp://127.0.0.1:4502/");

            // Attach the output channel and be able to send messages
            // and receive response messages.
            mySender.AttachDuplexOutputChannel(anOutputChannel);
        }

        private void CloseConnection()
        {
            // Detach input channel and stop listening to response messages.
            mySender.DetachDuplexOutputChannel();
        }

        private void CalculateBtn_Click(object sender, EventArgs e)
        {
            // Create the request message.
            RequestMessage aRequest = new RequestMessage();
            aRequest.Number1 = int.Parse(Number1TextBox.Text);
            aRequest.Number2 = int.Parse(Number2TextBox.Text);

            // Send request to the service to calculate 2 numbers.
            // It waits until the response is received.
            ResponseMessage aResponse = mySender.SendRequestMessage(aRequest);

            // Display the result.
            ResultTextBox.Text = aResponse.Result.ToString();
        }
    }
}
