/*
 * Project: Eneter.ProtoBuf.Serializer
 * Author:  Ondrej Uzovic
 * 
 * Copyright © Ondrej Uzovic 2013
*/

using System;
using System.Linq;
using Eneter.Messaging.EndPoints.TypedMessages;
using Eneter.Messaging.MessagingSystems.Composites.MonitoredMessagingComposit;
using Eneter.Messaging.Nodes.Broker;
using Eneter.Messaging.Nodes.ChannelWrapper;
using Eneter.ProtoBuf;
using NUnit.Framework;
using Eneter.Messaging.Diagnostic;
using System.Diagnostics;
using Eneter.Messaging.DataProcessing.Serializing;
using System.Threading;

namespace EneterProtoBufSerializer_UTests
{
    [TestFixture]
    public class Test_ProtoBufSerializer
    {
        [Serializable]
        public class TestMessage2
        {
            public string Name;
            public int Value;
        }

        [Test]
        public void SerializeDeserializeTestMessage()
        {
            TestMessage aTestMessage = new TestMessage();
            aTestMessage.Name = "Hello";
            aTestMessage.Value = 123;

            ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
            object aSerializedData = aProtoBufSerializer.Serialize<TestMessage>(aTestMessage);
            TestMessage aResult = aProtoBufSerializer.Deserialize<TestMessage>(aSerializedData);

            Assert.AreEqual(aTestMessage.Name, aResult.Name);
            Assert.AreEqual(aTestMessage.Value, aResult.Value);
        }

        [Test]
        public void SerializeDeserializePerformanceTest()
        {
            TestMessage aTestMessage = new TestMessage();
            aTestMessage.Name = "Hello";
            aTestMessage.Value = 123;
            ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
            SerializerPerformanceTest<TestMessage>(aProtoBufSerializer, aTestMessage);

            TestMessage2 aTestMessage2 = new TestMessage2();
            aTestMessage2.Name = "Hello";
            aTestMessage2.Value = 123;
            BinarySerializer aNetBinSerializer = new BinarySerializer();
            SerializerPerformanceTest<TestMessage2>(aNetBinSerializer, aTestMessage2);

            XmlStringSerializer anXmlSerializer = new XmlStringSerializer();
            SerializerPerformanceTest<TestMessage2>(anXmlSerializer, aTestMessage2);

            DataContractJsonStringSerializer aJsonSerializer = new DataContractJsonStringSerializer();
            SerializerPerformanceTest<TestMessage2>(aJsonSerializer, aTestMessage2);
        }

        [Test]
        public void ThreadSafetyTest()
        {
            TestMessage aTestMessage = new TestMessage();
            aTestMessage.Name = "Hello";
            aTestMessage.Value = 123;

            ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();

            AutoResetEvent anAllThreadDone = new AutoResetEvent(false);
            int aCount = 0;

            Stopwatch aStopWatch = new Stopwatch();
            aStopWatch.Start();

            for (int i = 0; i < 10; ++i)
            {
                ThreadPool.QueueUserWorkItem(x =>
                    {
                        SerializerPerformanceTest<TestMessage>(aProtoBufSerializer, aTestMessage);
                        ++aCount;

                        if (aCount == 10)
                        {
                            anAllThreadDone.Set();
                        }
                    });
            }

            anAllThreadDone.WaitOne();

            aStopWatch.Stop();
            Console.WriteLine("Elapsed time: " + aStopWatch.Elapsed);
        }

        [Test]
        public void SerializeDeserializeWrappedData()
        {
            ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
	    
	        WrappedData aSrc = new WrappedData();
	        aSrc.AddedData = "Hello";
	        aSrc.OriginalData = new byte[]{10, 20, 30, 40};

            object aSerializedData = aProtoBufSerializer.Serialize<WrappedData>(aSrc);
            WrappedData aResult = aProtoBufSerializer.Deserialize<WrappedData>(aSerializedData);
            Assert.AreEqual(aResult.AddedData, aSrc.AddedData);
            Assert.IsTrue(Enumerable.SequenceEqual((byte[])aResult.OriginalData, (byte[])aSrc.OriginalData));
        
        
            aSrc.OriginalData = "Hello2";
            aSerializedData = aProtoBufSerializer.Serialize<WrappedData>(aSrc);
            aResult = aProtoBufSerializer.Deserialize<WrappedData>(aSerializedData);
            Assert.AreEqual(aResult.AddedData, aSrc.AddedData);
            Assert.AreEqual(aResult.OriginalData, aSrc.OriginalData);
        
        
            aSrc.OriginalData = null;
            aSerializedData = aProtoBufSerializer.Serialize<WrappedData>(aSrc);
            aResult = aProtoBufSerializer.Deserialize<WrappedData>(aSerializedData);
            Assert.AreEqual(aResult.AddedData, aSrc.AddedData);
            Assert.IsNull(aResult.OriginalData);
        }

        [Test]
        public void SerializeDeserializeBrokerMessage()
        {
	        ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
	    
            BrokerMessage aSrc = new BrokerMessage();
            aSrc.Request = EBrokerRequest.Subscribe;
            aSrc.MessageTypes = new String[]{"Hello1", "Hello2"};
            aSrc.Message = null;
            object aSerializedData = aProtoBufSerializer.Serialize<BrokerMessage>(aSrc);
            BrokerMessage aResult = aProtoBufSerializer.Deserialize<BrokerMessage>(aSerializedData);
            Assert.AreEqual(aResult.Request, aSrc.Request);
            Assert.IsTrue(Enumerable.SequenceEqual(aResult.MessageTypes, aSrc.MessageTypes));
            Assert.IsNull(aResult.Message);
        
        
            aSrc.Message = "Hello";
            aSerializedData = aProtoBufSerializer.Serialize<BrokerMessage>(aSrc);
            aResult = aProtoBufSerializer.Deserialize<BrokerMessage>(aSerializedData);
            Assert.AreEqual(aResult.Request, aSrc.Request);
            Assert.IsTrue(Enumerable.SequenceEqual(aResult.MessageTypes, aSrc.MessageTypes));
            Assert.AreEqual(aResult.Message, aSrc.Message);
       
        
            aSrc.Message = new byte[]{1, 2, 3};
            aSerializedData = aProtoBufSerializer.Serialize<BrokerMessage>(aSrc);
            aResult = aProtoBufSerializer.Deserialize<BrokerMessage>(aSerializedData);
            Assert.AreEqual(aResult.Request, aSrc.Request);
            Assert.IsTrue(Enumerable.SequenceEqual(aResult.MessageTypes, aSrc.MessageTypes));
            Assert.IsTrue(Enumerable.SequenceEqual((byte[])aResult.Message, (byte[])aSrc.Message));
        }

        [Test]
        public void SerializeDeserializeReliableMessage()
        {
	        ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
	    
	        ReliableMessage aSrc = new ReliableMessage();
            aSrc.MessageType = ReliableMessage.EMessageType.Acknowledge;
            aSrc.MessageId = "123";
            aSrc.Message = null;
            object aSerializedData = aProtoBufSerializer.Serialize<ReliableMessage>(aSrc);
            ReliableMessage aResult = aProtoBufSerializer.Deserialize<ReliableMessage>(aSerializedData);
            Assert.AreEqual(aResult.MessageType, aSrc.MessageType);
            Assert.AreEqual(aResult.MessageId, aSrc.MessageId);
            Assert.IsNull(aResult.Message);
                
        
            aSrc.Message = "Hello2";
            aSerializedData = aProtoBufSerializer.Serialize<ReliableMessage>(aSrc);
            aResult = aProtoBufSerializer.Deserialize<ReliableMessage>(aSerializedData);
            Assert.AreEqual(aResult.MessageType, aSrc.MessageType);
            Assert.AreEqual(aResult.MessageId, aSrc.MessageId);
            Assert.AreEqual(aResult.Message, aSrc.Message);
        
        
            aSrc.Message = new byte[]{10, 20, 30};
            aSerializedData = aProtoBufSerializer.Serialize<ReliableMessage>(aSrc);
            aResult = aProtoBufSerializer.Deserialize<ReliableMessage>(aSerializedData);
            Assert.AreEqual(aResult.MessageType, aSrc.MessageType);
            Assert.AreEqual(aResult.MessageId, aSrc.MessageId);
            Assert.IsTrue(Enumerable.SequenceEqual((byte[])aResult.Message, (byte[])aSrc.Message));
        }

        [Test]
        public void SerializeDeserializeMonitorChannelMessage()
        {
            ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
        
            MonitorChannelMessage aSrc = new MonitorChannelMessage();
            aSrc.MessageType = MonitorChannelMessageType.Message;
            aSrc.MessageContent = null;
            object aSerializedData = aProtoBufSerializer.Serialize<MonitorChannelMessage>(aSrc);
            MonitorChannelMessage aResult = aProtoBufSerializer.Deserialize<MonitorChannelMessage>(aSerializedData);
            Assert.AreEqual(aResult.MessageType, aSrc.MessageType);
            Assert.IsNull(aResult.MessageContent);
                
        
            aSrc.MessageContent = "Hello2";
            aSerializedData = aProtoBufSerializer.Serialize<MonitorChannelMessage>(aSrc);
            aResult = aProtoBufSerializer.Deserialize<MonitorChannelMessage>(aSerializedData);
            Assert.AreEqual(aResult.MessageType, aSrc.MessageType);
            Assert.AreEqual(aResult.MessageContent, aSrc.MessageContent);
        
        
            aSrc.MessageContent = new byte[]{10, 20, 30};
            aSerializedData = aProtoBufSerializer.Serialize<MonitorChannelMessage>(aSrc);
            aResult = aProtoBufSerializer.Deserialize<MonitorChannelMessage>(aSerializedData);
            Assert.AreEqual(aResult.MessageType, aSrc.MessageType);
            Assert.IsTrue(Enumerable.SequenceEqual((byte[])aResult.MessageContent, (byte[])aSrc.MessageContent));
        }

        [Test]
        public void serializeDeserializeVoidMessage()
        {
            ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
        
            VoidMessage aSrc = new VoidMessage();
            object aSerializedData = aProtoBufSerializer.Serialize<VoidMessage>(aSrc);
            VoidMessage aResult = aProtoBufSerializer.Deserialize<VoidMessage>(aSerializedData);
            Assert.IsNotNull(aResult);
        }



        private void SerializerPerformanceTest<T>(ISerializer serializer, T dataToSerialize)
        {
            int aMessageSize = 0;

            Stopwatch aStopWatch = new Stopwatch();
            aStopWatch.Start();

            for (int i = 0; i < 100000; ++i)
            {
                object aSerializedData = serializer.Serialize<T>(dataToSerialize);
                if (aSerializedData is byte[])
                {
                    aMessageSize = ((byte[])aSerializedData).Length;
                }
                else if (aSerializedData is string)
                {
                    aMessageSize = ((string)aSerializedData).Length;
                }
                T aResult2 = serializer.Deserialize<T>(aSerializedData);
            }

            aStopWatch.Stop();
            Console.WriteLine(serializer.GetType().Name + ": " + aStopWatch.Elapsed + " Message Size: " + aMessageSize);
        }
    }
}
