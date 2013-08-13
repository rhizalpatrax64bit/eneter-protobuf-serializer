using System;
using Eneter.Messaging.DataProcessing.Serializing;
using Eneter.Messaging.EndPoints.TypedMessages;
using Eneter.Messaging.MessagingSystems.MessagingSystemBase;
using Eneter.Messaging.MessagingSystems.TcpMessagingSystem;
using Eneter.ProtoBuf;
using message.declarations;

namespace ServiceExample
{
    class Program
    {
        private static IDuplexTypedMessageReceiver<MyResponse, MyRequest> myReceiver;

        static void Main(string[] args)
        {
            // Instantiate Protocol Buffer based serializer.
            ISerializer aSerializer = new ProtoBufSerializer();

            // Create message receiver receiving 'MyRequest' and receiving 'MyResponse'.
            // The receiver will use Protocol Buffers to serialize/deserialize messages. 
            IDuplexTypedMessagesFactory aReceiverFactory = new DuplexTypedMessagesFactory(aSerializer);
            myReceiver = aReceiverFactory.CreateDuplexTypedMessageReceiver<MyResponse, MyRequest>();

            // Subscribe to handle messages.
            myReceiver.MessageReceived += OnMessageReceived;

            // Create TCP messaging.
            IMessagingSystemFactory aMessaging = new TcpMessagingSystemFactory();
            
            IDuplexInputChannel anInputChannel
                = aMessaging.CreateDuplexInputChannel("tcp://127.0.0.1:8060/");

            // Attach the input channel and start to listen to messages.
            myReceiver.AttachDuplexInputChannel(anInputChannel);

            Console.WriteLine("The service is running. To stop press enter.");
            Console.ReadLine();

            // Detach the input channel and stop listening.
            // It releases the thread listening to messages.
            myReceiver.DetachDuplexInputChannel();
        }

        // It is called when a message is received.
        private static void OnMessageReceived(object sender, TypedRequestReceivedEventArgs<MyRequest> e)
        {
            Console.WriteLine("Received: " + e.RequestMessage.Text);

            // Create the response message.
            MyResponse aResponse = new MyResponse();
            aResponse.Length = e.RequestMessage.Text.Length;

            // Send the response message back to the client.
            myReceiver.SendResponseMessage(e.ResponseReceiverId, aResponse);
        }
    }
}
