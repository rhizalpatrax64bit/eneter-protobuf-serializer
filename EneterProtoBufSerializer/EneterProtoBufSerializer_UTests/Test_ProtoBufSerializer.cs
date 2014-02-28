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
using Eneter.Messaging.EndPoints.Rpc;

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

        [Test]
        public void SerializeDeserializeRpcMessage()
        {
            ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
            byte[] aParam1 = aProtoBufSerializer.Serialize<string>("hello") as byte[];
            byte[] aParam2 = aProtoBufSerializer.Serialize<bool>(true) as byte[];
            byte[] aParam3 = aProtoBufSerializer.Serialize<bool>(false) as byte[];
            byte[] aParam4 = aProtoBufSerializer.Serialize<byte>(255) as byte[];
            byte[] aParam5 = aProtoBufSerializer.Serialize<char>('ž') as byte[];
            byte[] aParam6 = aProtoBufSerializer.Serialize<short>(-1) as byte[];
            byte[] aParam7 = aProtoBufSerializer.Serialize<int>(100) as byte[];
            byte[] aParam8 = aProtoBufSerializer.Serialize<long>((long)-1236987) as byte[];
            byte[] aParam9 = aProtoBufSerializer.Serialize<float>(1.2345f) as byte[];
            byte[] aParam10 = aProtoBufSerializer.Serialize<double>(13.2345) as byte[];
            
            string[] st = {"hello 1", "hello 2"};
            bool[] bo = {true, false};
            byte[] by = {123, 1};
            char[] ch = {'ž', 'A'};
            short[] sh = {-1, 6000};
            int[] i = {-1, 60000};
            long[] lo = {-1, 60000000};
            float[] fl = {-1.0f, 1.234f};
            double[] dou = {-1.0, 100.4353};

            byte[] aParam11 = aProtoBufSerializer.Serialize<string[]>(st) as byte[];
            byte[] aParam12 = aProtoBufSerializer.Serialize<bool[]>(bo) as byte[];
            byte[] aParam13 = aProtoBufSerializer.Serialize<byte[]>(by) as byte[];
            byte[] aParam14 = aProtoBufSerializer.Serialize<char[]>(ch) as byte[];
            byte[] aParam15 = aProtoBufSerializer.Serialize<short[]>(sh) as byte[];
            byte[] aParam16 = aProtoBufSerializer.Serialize<int[]>(i) as byte[];
            byte[] aParam17 = aProtoBufSerializer.Serialize<long[]>(lo) as byte[];
            byte[] aParam18 = aProtoBufSerializer.Serialize<float[]>(fl) as byte[];
            byte[] aParam19 = aProtoBufSerializer.Serialize<double[]>(dou) as byte[];

            
            RpcMessage anRpcMessage = new RpcMessage();
            anRpcMessage.Id = 102;
            anRpcMessage.Flag = 20;
            anRpcMessage.OperationName = "DummyOperation";
            anRpcMessage.Error = "DummyError";
            anRpcMessage.SerializedData = new object[]
                { aParam1, aParam2, aParam3, aParam4, aParam5, aParam6, aParam7, aParam8, aParam9, aParam10,
                  aParam11, aParam12, aParam13, aParam14, aParam15, aParam16, aParam17, aParam18, aParam19};

            object aSerialized = aProtoBufSerializer.Serialize<RpcMessage>(anRpcMessage);

            RpcMessage aDeserialized = aProtoBufSerializer.Deserialize<RpcMessage>(aSerialized);

            Assert.AreEqual(anRpcMessage.Id, aDeserialized.Id);
            Assert.AreEqual(anRpcMessage.Flag, aDeserialized.Flag);
            Assert.AreEqual(anRpcMessage.OperationName, aDeserialized.OperationName);
            Assert.AreEqual(anRpcMessage.Error, aDeserialized.Error);

            Assert.AreEqual(19, aDeserialized.SerializedData.Length);

            string aD1 = aProtoBufSerializer.Deserialize<string>(aDeserialized.SerializedData[0]);
            bool aD2 = aProtoBufSerializer.Deserialize<bool>(aDeserialized.SerializedData[1]);
            bool aD3 = aProtoBufSerializer.Deserialize<bool>(aDeserialized.SerializedData[2]);
            byte aD4 = aProtoBufSerializer.Deserialize<byte>(aDeserialized.SerializedData[3]);
            char aD5 = aProtoBufSerializer.Deserialize<char>(aDeserialized.SerializedData[4]);
            short aD6 = aProtoBufSerializer.Deserialize<short>(aDeserialized.SerializedData[5]);
            int aD7 = aProtoBufSerializer.Deserialize<int>(aDeserialized.SerializedData[6]);
            long aD8 = aProtoBufSerializer.Deserialize<long>(aDeserialized.SerializedData[7]);
            float aD9 = aProtoBufSerializer.Deserialize<float>(aDeserialized.SerializedData[8]);
            double aD10 = aProtoBufSerializer.Deserialize<double>(aDeserialized.SerializedData[9]);
        
            string[] aD11 = aProtoBufSerializer.Deserialize<string[]>(aDeserialized.SerializedData[10]);
            bool[] aD12 = aProtoBufSerializer.Deserialize<bool[]>(aDeserialized.SerializedData[11]);
            byte[] aD13 = aProtoBufSerializer.Deserialize<byte[]>(aDeserialized.SerializedData[12]);
            char[] aD14 = aProtoBufSerializer.Deserialize<char[]>(aDeserialized.SerializedData[13]);
            short[] aD15 = aProtoBufSerializer.Deserialize<short[]>(aDeserialized.SerializedData[14]);
            int[] aD16 = aProtoBufSerializer.Deserialize<int[]>(aDeserialized.SerializedData[15]);
            long[] aD17 = aProtoBufSerializer.Deserialize<long[]>(aDeserialized.SerializedData[16]);
            float[] aD18 = aProtoBufSerializer.Deserialize<float[]>(aDeserialized.SerializedData[17]);
            double[] aD19 = aProtoBufSerializer.Deserialize<double[]>(aDeserialized.SerializedData[18]);

            Assert.AreEqual("hello", aD1);
            Assert.AreEqual(true, aD2);
            Assert.AreEqual(false, aD3);
            Assert.AreEqual((byte)255, aD4);
            Assert.AreEqual('ž', aD5);
            Assert.AreEqual(-1, aD6);
            Assert.AreEqual(100, aD7);
            Assert.AreEqual((long)-1236987, aD8);
            Assert.True(Math.Abs((float)1.2345 - aD9) < 0.00001);
            Assert.True(Math.Abs((double)13.2345 - aD10) < 0.00001);


            Assert.True(st.SequenceEqual(aD11));
        
            Assert.True(bo.SequenceEqual(aD12));
            Assert.True(by.SequenceEqual(aD13));
            Assert.True(ch.SequenceEqual(aD14));
            Assert.True(sh.SequenceEqual(aD15));
            Assert.True(i.SequenceEqual(aD16));
            Assert.True(lo.SequenceEqual(aD17));
            Assert.True(fl.SequenceEqual(aD18));
            Assert.True(dou.SequenceEqual(aD19));
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
