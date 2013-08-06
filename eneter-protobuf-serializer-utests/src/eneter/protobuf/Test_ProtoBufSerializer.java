package eneter.protobuf;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.Test;

import eneter.messaging.endpoints.typedmessages.VoidMessage;
import eneter.messaging.endpoints.typedmessages.internal.ReliableMessage;
import eneter.messaging.endpoints.typedmessages.internal.ReliableMessage.EMessageType;
import eneter.messaging.messagingsystems.composites.monitoredmessagingcomposit.MonitorChannelMessage;
import eneter.messaging.messagingsystems.composites.monitoredmessagingcomposit.MonitorChannelMessageType;
import eneter.messaging.nodes.broker.BrokerMessage;
import eneter.messaging.nodes.broker.EBrokerRequest;
import eneter.messaging.nodes.channelwrapper.WrappedData;
import eneter.protobuf.EneterProtoBufDeclarations.WrappedDataProto;
import eneter.protobuf.EneterProtoBufUnitTestDeclarations.TestMessage;

public class Test_ProtoBufSerializer
{

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
	
}
