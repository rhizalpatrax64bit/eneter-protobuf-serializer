using System;
using Eneter.Messaging.DataProcessing.Serializing;
using Eneter.Messaging.MessagingSystems.MessagingSystemBase;
using Eneter.Messaging.MessagingSystems.TcpMessagingSystem;
using Eneter.Messaging.Nodes.Broker;
using Eneter.ProtoBuf;

namespace BrokerApplication
{
    class Program
    {
        static void Main(string[] args)
        {
            // Create ProtoBuf serializer.
            ISerializer aSerializer = new ProtoBufSerializer();

            // Create the broker.
            IDuplexBrokerFactory aBrokerFactory = new DuplexBrokerFactory(aSerializer);
            IDuplexBroker aBroker = aBrokerFactory.CreateBroker();

            // Create the Input channel receiving messages via Tcp.
            // Note: You can also choose NamedPipes or Http.
            //       (if you choose http, do not forget to execute it with sufficient user rights)
            IMessagingSystemFactory aMessaging = new TcpMessagingSystemFactory();
            IDuplexInputChannel aBrokerInputChannel = aMessaging.CreateDuplexInputChannel("tcp://127.0.0.1:7091/");

            aBroker.AttachDuplexInputChannel(aBrokerInputChannel);

            // Attach the input channel to the broker and start listening.
            Console.WriteLine("The broker application is running. Press ENTER to stop.");
            Console.ReadLine();

            aBroker.DetachDuplexInputChannel();
        }
    }
}
