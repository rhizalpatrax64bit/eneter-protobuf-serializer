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
import eneter.messaging.endpoints.rpc.RpcMessage;
import eneter.messaging.endpoints.typedmessages.MultiTypedMessage;
import eneter.messaging.endpoints.typedmessages.VoidMessage;
import eneter.messaging.messagingsystems.composites.messagebus.EMessageBusRequest;
import eneter.messaging.messagingsystems.composites.messagebus.MessageBusMessage;
import eneter.messaging.messagingsystems.composites.monitoredmessagingcomposit.*;
import eneter.messaging.nodes.broker.*;
import eneter.messaging.nodes.channelwrapper.WrappedData;
import eneter.net.system.EventArgs;
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
    public void serializeDeserializeMultiTypedMessage() throws Exception
    {
        ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();

        MultiTypedMessage aSrc = new MultiTypedMessage();
        aSrc.TypeName = "String";
        aSrc.MessageData = "Hello";

        Object aSerializedData = aProtoBufSerializer.serialize(aSrc, MultiTypedMessage.class);

        MultiTypedMessage aResult = aProtoBufSerializer.deserialize(aSerializedData, MultiTypedMessage.class);
        assertEquals(aSrc.TypeName, aResult.TypeName);
        assertEquals(aSrc.MessageData, aResult.MessageData);

        aSrc.TypeName = "Byte[]";
        aSrc.MessageData = new byte[] { 1, 2, 3 };
        aSerializedData = aProtoBufSerializer.serialize(aSrc, MultiTypedMessage.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, MultiTypedMessage.class);
        assertEquals(aSrc.TypeName, aResult.TypeName);
        assertTrue(Arrays.equals((byte[])aSrc.MessageData, (byte[])aResult.MessageData));
    }

	@Test
    public void serializeDeserializeMessageBusMessage() throws Exception
    {
        ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();

        MessageBusMessage aSrc = new MessageBusMessage();
        aSrc.Request = EMessageBusRequest.DisconnectClient;
        aSrc.Id = "1234";
        aSrc.MessageData = "Hello";
        Object aSerializedData = aProtoBufSerializer.serialize(aSrc, MessageBusMessage.class);
        MessageBusMessage aResult = aProtoBufSerializer.deserialize(aSerializedData, MessageBusMessage.class);
        assertEquals(aSrc.Request, aResult.Request);
        assertEquals(aSrc.Id, aResult.Id);
        assertEquals(aSrc.MessageData, aResult.MessageData);

        aSrc.Request = EMessageBusRequest.SendResponseMessage;
        aSrc.Id = "1234";
        aSrc.MessageData = new byte[] { 1, 2, 3 };
        aSerializedData = aProtoBufSerializer.serialize(aSrc, MessageBusMessage.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, MessageBusMessage.class);
        assertEquals(aSrc.Request, aResult.Request);
        assertEquals(aSrc.Id, aResult.Id);
        assertTrue(Arrays.equals((byte[])aSrc.MessageData, (byte[])aResult.MessageData));

        aSrc.Request = EMessageBusRequest.SendResponseMessage;
        aSrc.Id = "";
        aSrc.MessageData = null;
        aSerializedData = aProtoBufSerializer.serialize(aSrc, MessageBusMessage.class);
        aResult = aProtoBufSerializer.deserialize(aSerializedData, MessageBusMessage.class);
        assertEquals(aSrc.Request, aResult.Request);
        assertEquals(aSrc.Id, aResult.Id);
        assertNull(aResult.MessageData);
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
	
	@Test
    public void serializeDeserializeEventArgs() throws Exception
    {
        ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
        
        EventArgs aSrc = new EventArgs();
        Object aSerializedData = aProtoBufSerializer.serialize(aSrc, EventArgs.class);
        EventArgs aResult = aProtoBufSerializer.deserialize(aSerializedData, EventArgs.class);
        assertNotNull(aResult);
    }
	
	@Test
	public void SerializeDeserializeRpcMessage() throws Exception
    {
	    ProtoBufSerializer aProtoBufSerializer = new ProtoBufSerializer();
	    
	    byte[] aParam1 = (byte[]) aProtoBufSerializer.serialize("hello", String.class);
	    byte[] aParam2 = (byte[]) aProtoBufSerializer.serialize(true, boolean.class);
	    byte[] aParam3 = (byte[]) aProtoBufSerializer.serialize(false, boolean.class);
	    byte[] aParam4 = (byte[]) aProtoBufSerializer.serialize((byte)255, byte.class);
	    byte[] aParam5 = (byte[]) aProtoBufSerializer.serialize('ž', char.class);
	    byte[] aParam6 = (byte[]) aProtoBufSerializer.serialize((short)-1, short.class);
	    byte[] aParam7 = (byte[]) aProtoBufSerializer.serialize(100, int.class);
	    byte[] aParam8 = (byte[]) aProtoBufSerializer.serialize((long)-1236987, long.class);
        byte[] aParam9 = (byte[]) aProtoBufSerializer.serialize((float)1.2345, float.class);
        byte[] aParam10 = (byte[]) aProtoBufSerializer.serialize((double)13.2345, double.class);
        
        String[] st = {"hello 1", "hello 2"};
        boolean[] bo = {true, false};
        byte[] by = {123, 1};
        char[] ch = {'ž', 'A'};
        short[] sh = {-1, 6000};
        int[] in = {-1, 60000};
        long[] lo = {-1, 60000000};
        float[] fl = {-1.0f, 1.234f};
        double[] dou = {-1.0, 100.4353};
        
        
        byte[] aParam11 = (byte[]) aProtoBufSerializer.serialize(st, String[].class);
        byte[] aParam12 = (byte[]) aProtoBufSerializer.serialize(bo, boolean[].class);
        byte[] aParam13 = (byte[]) aProtoBufSerializer.serialize(by, byte[].class);
        byte[] aParam14 = (byte[]) aProtoBufSerializer.serialize(ch, char[].class);
        byte[] aParam15 = (byte[]) aProtoBufSerializer.serialize(sh, short[].class);
        byte[] aParam16 = (byte[]) aProtoBufSerializer.serialize(in, int[].class);
        byte[] aParam17 = (byte[]) aProtoBufSerializer.serialize(lo, long[].class);
        byte[] aParam18 = (byte[]) aProtoBufSerializer.serialize(fl, float[].class);
        byte[] aParam19 = (byte[]) aProtoBufSerializer.serialize(dou, double[].class);
	    
        RpcMessage anRpcMessage = new RpcMessage();
        anRpcMessage.Id = 102;
        anRpcMessage.Flag = 20;
        anRpcMessage.OperationName = "DummyOperation";
        anRpcMessage.ErrorType = "DummyErrorType";
        anRpcMessage.ErrorMessage = "DummyError";
        anRpcMessage.ErrorDetails = "DummyErrorDetails";
        anRpcMessage.SerializedData = new Object[]
                { aParam1, aParam2, aParam3, aParam4, aParam5, aParam6, aParam7, aParam8, aParam9, aParam10,
                  aParam11, aParam12, aParam13, aParam14, aParam15, aParam16, aParam17, aParam18, aParam19};
        
        Object aSerialized = aProtoBufSerializer.serialize(anRpcMessage, RpcMessage.class);

        RpcMessage aDeserialized = aProtoBufSerializer.deserialize(aSerialized, RpcMessage.class);
        
        assertEquals(anRpcMessage.Id, aDeserialized.Id);
        assertEquals(anRpcMessage.Flag, aDeserialized.Flag);
        assertEquals(anRpcMessage.OperationName, aDeserialized.OperationName);
        assertEquals(anRpcMessage.ErrorType, aDeserialized.ErrorType);
        assertEquals(anRpcMessage.ErrorMessage, aDeserialized.ErrorMessage);
        assertEquals(anRpcMessage.ErrorDetails, aDeserialized.ErrorDetails);

        assertEquals(19, aDeserialized.SerializedData.length);

        String aD1 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[0], String.class);
        boolean aD2 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[1], boolean.class);
        boolean aD3 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[2], boolean.class);
        byte aD4 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[3], byte.class);
        char aD5 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[4], char.class);
        short aD6 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[5], short.class);
        int aD7 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[6], int.class);
        long aD8 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[7], long.class);
        float aD9 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[8], float.class);
        double aD10 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[9], double.class);
        
        String[] aD11 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[10], String[].class);
        boolean[] aD12 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[11], boolean[].class);
        byte[] aD13 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[12], byte[].class);
        char[] aD14 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[13], char[].class);
        short[] aD15 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[14], short[].class);
        int[] aD16 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[15], int[].class);
        long[] aD17 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[16], long[].class);
        float[] aD18 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[17], float[].class);
        double[] aD19 = aProtoBufSerializer.deserialize(aDeserialized.SerializedData[18], double[].class);

        assertEquals("hello", aD1);
        assertEquals(true, aD2);
        assertEquals(false, aD3);
        assertEquals((byte)255, aD4);
        assertEquals('ž', aD5);
        assertEquals(-1, aD6);
        assertEquals(100, aD7);
        assertEquals((long)-1236987, aD8);
        assertTrue(Math.abs((float)1.2345 - aD9) < 0.00001);
        assertTrue(Math.abs((double)13.2345 - aD10) < 0.00001);
        
        for (int i = 0; i < st.length; ++i)
        {
            assertEquals(st[i], aD11[i]);
        }
        
        assertTrue(Arrays.equals(aD12, bo));
        assertTrue(Arrays.equals(aD13, by));
        assertTrue(Arrays.equals(aD14, ch));
        assertTrue(Arrays.equals(aD15, sh));
        assertTrue(Arrays.equals(aD16, in));
        assertTrue(Arrays.equals(aD17, lo));
        assertTrue(Arrays.equals(aD18, fl));
        assertTrue(Arrays.equals(aD19, dou));
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
