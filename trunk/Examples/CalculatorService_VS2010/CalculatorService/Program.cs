using System;
using Eneter.Messaging.DataProcessing.Serializing;
using Eneter.Messaging.EndPoints.TypedMessages;
using Eneter.Messaging.MessagingSystems.MessagingSystemBase;
using Eneter.Messaging.MessagingSystems.TcpMessagingSystem;
using Eneter.ProtoBuf;
using ProtoBuf;

namespace CalculatorService
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

    class Program
    {
        static void Main(string[] args)
        {
            // Create ProtoBuf serializer.
            ISerializer aSerializer = new ProtoBufSerializer();

            // Create message receiver.
            IDuplexTypedMessagesFactory aReceiverFactory = new DuplexTypedMessagesFactory(aSerializer);
            IDuplexTypedMessageReceiver<ResponseMessage, RequestMessage> aReceiver =
                aReceiverFactory.CreateDuplexTypedMessageReceiver<ResponseMessage, RequestMessage>();

            // Subscribe to process request messages.
            aReceiver.MessageReceived += OnMessageReceived;

            // Use TCP for the communication.
            IMessagingSystemFactory aMessaging = new TcpMessagingSystemFactory();
            IDuplexInputChannel anInputChannel =
                aMessaging.CreateDuplexInputChannel("tcp://127.0.0.1:4502/");

            // Attach the input channel to the receiver and start listening.
            aReceiver.AttachDuplexInputChannel(anInputChannel);

            Console.WriteLine("The calculator service is running. Press ENTER to stop.");
            Console.ReadLine();

            // Detach the input channel to stop listening.
            aReceiver.DetachDuplexInputChannel();
        }

        private static void OnMessageReceived(object sender, TypedRequestReceivedEventArgs<RequestMessage> e)
        {
            // Calculate numbers.
            ResponseMessage aResponseMessage = new ResponseMessage();
            aResponseMessage.Result = e.RequestMessage.Number1 + e.RequestMessage.Number2;

            Console.WriteLine("{0} + {1} = {2}", e.RequestMessage.Number1, e.RequestMessage.Number2, aResponseMessage.Result);

            // Send back the response message.
            var aReceiver = (IDuplexTypedMessageReceiver<ResponseMessage, RequestMessage>)sender;
            aReceiver.SendResponseMessage(e.ResponseReceiverId, aResponseMessage);
        }
    }
}
