/**
 * Project: Eneter.ProtoBuf.Serializer
 * Author: Ondrej Uzovic
 * 
 * Copyright © 2013 Ondrej Uzovic
 * 
 */

package eneter.protobuf;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.Arrays;

import org.junit.Test;

import eneter.messaging.dataprocessing.serializing.ISerializer;
import eneter.messaging.dataprocessing.serializing.JavaBinarySerializer;
import eneter.messaging.dataprocessing.serializing.XmlStringSerializer;
import eneter.messaging.endpoints.typedmessages.VoidMessage;
import eneter.messaging.endpoints.typedmessages.internal.ReliableMessage;
import eneter.messaging.endpoints.typedmessages.internal.ReliableMessage.EMessageType;
import eneter.messaging.messagingsystems.composites.monitoredmessagingcomposit.*;
import eneter.messaging.nodes.broker.*;
import eneter.messaging.nodes.channelwrapper.WrappedData;
import eneter.protobuf.EneterProtoBufUnitTestDeclarations.TestMessage;

public class Test_ProtoBufSerializer
{
    public static class TestMessage2 implements Serializable
    {
        private static final long serialVersionUID = -7462355711199438895L;
        public String Name;
        public int Value;
    }

	@Test
	public void serializeDeserializeTestMessage() throws Exception
	{
	    TestMessage aTestMessage = TestMessage.newBuilder()
	            .setName("Hello")
	            .setValue(123)
	            .build();
	    
	    ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
	    
	    Object aSerializedData = aProtoBufSerializer.serialize(aTestMessage, TestMessage.class);
	    
	    TestMessage aResult = aProtoBufSerializer.deserialize(aSerializedData, TestMessage.class);
	    
	    assertEquals(aResult.getName(), aTestMessage.getName());
	    assertEquals(aResult.getValue(), aTestMessage.getValue());
	}
	
	@Test
	public void serializeDeserializePerformanceTest() throws Exception
	{
	    TestMessage aTestMessage = TestMessage.newBuilder()
                .setName("Hello")
                .setValue(123)
                .build();
        ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
        serializerPerformanceTest(aProtoBufSerializer, aTestMessage, TestMessage.class);

        TestMessage2 aTestMessage2 = new TestMessage2();
        aTestMessage2.Name = "Hello";
        aTestMessage2.Value = 123;
        JavaBinarySerializer aNetBinSerializer = new JavaBinarySerializer();
        serializerPerformanceTest(aNetBinSerializer, aTestMessage2, TestMessage2.class);

        XmlStringSerializer anXmlSerializer = new XmlStringSerializer();
        serializerPerformanceTest(anXmlSerializer, aTestMessage2, TestMessage2.class);
	}
	
	@Test
    public void serializeDeserializeWrappedData() throws Exception
    {
        ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
	    
	    WrappedData aSrc = new WrappedData();
	    aSrc.AddedData = "Hello";
	    aSrc.OriginalData = new byte[]{10, 20, 30, 40};
        Object aSerializedData = aProtoBufSerializer.serialize(aSrc, WrappedData.class);
        WrappedData aResult = aProtoBufSerializer.deserialize(aSerializedData, WrappedData.class);
        assertEquals(aResult.AddedData, aSrc.AddedData);
        assertTrue(Arrays.equals((byte[])aResult.OriginalData, (byte[])aSrc.OriginalData));
        
        
        aSrc.OriginalData = "Hello2";
        aSerializedData = aProtoBufSerializer.serialize(aSrc, WrappedData.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, WrappedData.class);
        assertEquals(aResult.AddedData, aSrc.AddedData);
        assertEquals(aResult.OriginalData, aSrc.OriginalData);
        
        
        aSrc.OriginalData = null;
        aSerializedData = aProtoBufSerializer.serialize(aSrc, WrappedData.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, WrappedData.class);
        assertEquals(aResult.AddedData, aSrc.AddedData);
        assertNull(aResult.OriginalData);
    }
	
	@Test
    public void serializeDeserializeBrokerMessage() throws Exception
    {
	    ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
	    
        BrokerMessage aSrc = new BrokerMessage();
        aSrc.Request = EBrokerRequest.Subscribe;
        aSrc.MessageTypes = new String[]{"Hello1", "Hello2"};
        aSrc.Message = null;
        Object aSerializedData = aProtoBufSerializer.serialize(aSrc, BrokerMessage.class);
        BrokerMessage aResult = aProtoBufSerializer.deserialize(aSerializedData, BrokerMessage.class);
        assertEquals(aResult.Request, aSrc.Request);
        assertTrue(Arrays.equals(aResult.MessageTypes, aSrc.MessageTypes));
        assertNull(aResult.Message);
        
        
        aSrc.Message = "Hello";
        aSerializedData = aProtoBufSerializer.serialize(aSrc, BrokerMessage.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, BrokerMessage.class);
        assertEquals(aResult.Request, aSrc.Request);
        assertTrue(Arrays.equals(aResult.MessageTypes, aSrc.MessageTypes));
        assertEquals(aResult.Message, aSrc.Message);
       
        
        aSrc.Message = new byte[]{1, 2, 3};
        aSerializedData = aProtoBufSerializer.serialize(aSrc, BrokerMessage.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, BrokerMessage.class);
        assertEquals(aResult.Request, aSrc.Request);
        assertTrue(Arrays.equals(aResult.MessageTypes, aSrc.MessageTypes));
        assertTrue(Arrays.equals((byte[])aResult.Message, (byte[])aSrc.Message));
    }

	@Test
    public void serializeDeserializeReliableMessage() throws Exception
    {
	    ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
	    
	    ReliableMessage aSrc = new ReliableMessage();
        aSrc.MessageType = EMessageType.Acknowledge;
        aSrc.MessageId = "123";
        aSrc.Message = null;
        Object aSerializedData = aProtoBufSerializer.serialize(aSrc, ReliableMessage.class);
        ReliableMessage aResult = aProtoBufSerializer.deserialize(aSerializedData, ReliableMessage.class);
        assertEquals(aResult.MessageType, aSrc.MessageType);
        assertEquals(aResult.MessageId, aSrc.MessageId);
        assertNull(aResult.Message);
                
        
        aSrc.Message = "Hello2";
        aSerializedData = aProtoBufSerializer.serialize(aSrc, ReliableMessage.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, ReliableMessage.class);
        assertEquals(aResult.MessageType, aSrc.MessageType);
        assertEquals(aResult.MessageId, aSrc.MessageId);
        assertEquals(aResult.Message, aSrc.Message);
        
        
        aSrc.Message = new byte[]{10, 20, 30};
        aSerializedData = aProtoBufSerializer.serialize(aSrc, ReliableMessage.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, ReliableMessage.class);
        assertEquals(aResult.MessageType, aSrc.MessageType);
        assertEquals(aResult.MessageId, aSrc.MessageId);
        assertTrue(Arrays.equals((byte[])aResult.Message, (byte[])aSrc.Message));
    }
	
	@Test
    public void serializeDeserializeMonitorChannelMessage() throws Exception
    {
        ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
        
        MonitorChannelMessage aSrc = new MonitorChannelMessage();
        aSrc.MessageType = MonitorChannelMessageType.Message;
        aSrc.MessageContent = null;
        Object aSerializedData = aProtoBufSerializer.serialize(aSrc, MonitorChannelMessage.class);
        MonitorChannelMessage aResult = aProtoBufSerializer.deserialize(aSerializedData, MonitorChannelMessage.class);
        assertEquals(aResult.MessageType, aSrc.MessageType);
        assertNull(aResult.MessageContent);
                
        
        aSrc.MessageContent = "Hello2";
        aSerializedData = aProtoBufSerializer.serialize(aSrc, MonitorChannelMessage.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, MonitorChannelMessage.class);
        assertEquals(aResult.MessageType, aSrc.MessageType);
        assertEquals(aResult.MessageContent, aSrc.MessageContent);
        
        
        aSrc.MessageContent = new byte[]{10, 20, 30};
        aSerializedData = aProtoBufSerializer.serialize(aSrc, MonitorChannelMessage.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, MonitorChannelMessage.class);
        assertEquals(aResult.MessageType, aSrc.MessageType);
        assertTrue(Arrays.equals((byte[])aResult.MessageContent, (byte[])aSrc.MessageContent));
    }
	
	@Test
    public void serializeDeserializeVoidMessage() throws Exception
    {
        ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
        
        VoidMessage aSrc = new VoidMessage();
        Object aSerializedData = aProtoBufSerializer.serialize(aSrc, VoidMessage.class);
        VoidMessage aResult = aProtoBufSerializer.deserialize(aSerializedData, VoidMessage.class);
        assertNotNull(aResult);
    }
	
	
	private <T> void serializerPerformanceTest(ISerializer serializer, T dataToSerialize, Class<T> clazz) throws Exception
	{
	    long aStartingTime = System.nanoTime();

        for (int i = 0; i < 100000; ++i)
        {
            Object aSerializedData = serializer.serialize(dataToSerialize, clazz);
            T aResult2 = serializer.deserialize(aSerializedData, clazz);
        }

        long anElapsedTime = System.nanoTime() - aStartingTime;
        
        System.out.printf("%s: %s\n", serializer.getClass().getSimpleName(), nanoToTime(anElapsedTime));
	}
	
	private String nanoToTime(long elapsedTime)
	{
	    long aHours = (long) (elapsedTime / (60.0 * 60.0 * 1000000000.0));
        elapsedTime -= aHours * 60 * 60 * 1000000000;
        
        long aMinutes = (long) (elapsedTime / (60.0 * 1000000000.0));
        elapsedTime -= aMinutes * 60 * 1000000000;
        
        long aSeconds = elapsedTime / 1000000000;
        elapsedTime -= aSeconds * 1000000000;
        
        long aMiliseconds = elapsedTime / 1000000;
        elapsedTime -= aMiliseconds * 1000000;
        
        double aMicroseconds = elapsedTime / 1000.0;

        return String.format("[%d:%d:%d %dms %.1fus]",
            aHours,
            aMinutes,
            aSeconds,
            aMiliseconds,
            aMicroseconds);
	}
	
}
