using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;
using Eneter.ProtoBuf;
using Eneter.Messaging.Nodes.ChannelWrapper;

namespace EneterProtoBufSerializer_UTests
{
    [TestFixture]
    public class Test_ProtoBufSerializer
    {
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
        public void serializeDeserializeWrappedData()
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
    }
}
