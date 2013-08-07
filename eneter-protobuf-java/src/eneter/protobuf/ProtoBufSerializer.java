package eneter.protobuf;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

import eneter.messaging.dataprocessing.serializing.ISerializer;
import eneter.messaging.endpoints.typedmessages.VoidMessage;
import eneter.messaging.endpoints.typedmessages.internal.ReliableMessage;
import eneter.messaging.messagingsystems.composites.monitoredmessagingcomposit.*;
import eneter.messaging.nodes.broker.*;
import eneter.messaging.nodes.channelwrapper.WrappedData;
import eneter.protobuf.EneterProtoBufDeclarations.*;

public class ProtoBufSerializer implements ISerializer
{

    @Override
    public <T> Object serialize(T dataToSerialize, Class<T> clazz)
            throws Exception
    {
        Object aSerializedData;
        
        // If it is an internal Eneter type adapt it for ProtoBuf
        if (clazz == WrappedData.class)
        {
            aSerializedData = serializeWrappedData((WrappedData)dataToSerialize);
        }
        else if (clazz == BrokerMessage.class)
        {
            aSerializedData = serializeBrokerMessage((BrokerMessage)dataToSerialize);
        }
        else if (clazz == ReliableMessage.class)
        {
            aSerializedData = serializeReliableMessage((ReliableMessage)dataToSerialize);
        }
        else if (clazz == MonitorChannelMessage.class)
        {
            aSerializedData = serializeMonitorChannelMessage((MonitorChannelMessage)dataToSerialize);
        }
        else if (clazz == VoidMessage.class)
        {
            aSerializedData = serializeVoidMessage((VoidMessage)dataToSerialize);
        }
        else
        {
            // If it is not internal Eneter message use directly ProtoBuf serializer.
            GeneratedMessage aDataToSerialize = (GeneratedMessage)dataToSerialize;
            aSerializedData = aDataToSerialize.toByteArray();
        }
        
        return aSerializedData;
    }

    @Override
    public <T> T deserialize(Object serializedData, Class<T> clazz)
            throws Exception
    {
        T aDeserializedObject;

        // If it is an internal Eneter type, adapt it from the proto type.
        if (clazz == WrappedData.class)
        {
            aDeserializedObject = clazz.cast(deserializeWrappedData((byte[])serializedData));
        }
        else if (clazz == BrokerMessage.class)
        {
            aDeserializedObject = clazz.cast(deserializeBrokerMessage((byte[])serializedData)); 
        }
        else if (clazz == ReliableMessage.class)
        {
            aDeserializedObject = clazz.cast(deserializeReliableMessage((byte[])serializedData)); 
        }
        else if (clazz == MonitorChannelMessage.class)
        {
            aDeserializedObject = clazz.cast(deserializeMonitorChannelMessage((byte[])serializedData)); 
        }
        else if (clazz == VoidMessage.class)
        {
            aDeserializedObject = clazz.cast(deserializeVoidMessage((byte[])serializedData));
        }
        else
        {
            // If it is not an Eneter internal type then use directly protobuf.
            Method aParsingMethod = clazz.getMethod("parseFrom", byte[].class);
            aDeserializedObject = clazz.cast(aParsingMethod.invoke(null, (byte[])serializedData));
        }
        
        return aDeserializedObject;
    }
    
    
    private byte[] serializeWrappedData(WrappedData data)
    {
        WrappedDataProto.Builder aBuilder = WrappedDataProto.newBuilder()
                .setAddedData((String)data.AddedData);
        if (data.OriginalData != null)
        {
            if (data.OriginalData instanceof String)
            {
                aBuilder.setOriginalDataStr((String)data.OriginalData);
            }
            else
            {
                aBuilder.setOriginalDataBin(ByteString.copyFrom((byte[]) data.OriginalData));
            }
        }
        WrappedDataProto aWrappedDataProto = aBuilder.build();

        return aWrappedDataProto.toByteArray();
    }

    private WrappedData deserializeWrappedData(byte[] data) throws InvalidProtocolBufferException
    {
        WrappedDataProto aWrappedDataProto = WrappedDataProto.parseFrom(data);
        WrappedData aWrappedData = new WrappedData();
        aWrappedData.AddedData = aWrappedDataProto.getAddedData();
        if (aWrappedDataProto.hasOriginalDataStr())
        {
            aWrappedData.OriginalData = aWrappedDataProto.getOriginalDataStr();
        }
        else if (aWrappedDataProto.hasOriginalDataBin())
        {
            aWrappedData.OriginalData = aWrappedDataProto.getOriginalDataBin().toByteArray();
        }

        return aWrappedData;
    }


    private byte[] serializeBrokerMessage(BrokerMessage data)
    {
        BrokerMessageProto.Builder aBuilder = BrokerMessageProto.newBuilder()
                .setRequest(data.Request.toString())
                .addAllMessageTypes(Arrays.asList(data.MessageTypes));
        if (data.Message != null)
        {
            if (data.Message instanceof String)
            {
                aBuilder.setMessageStr((String)data.Message);
            }
            else
            {
                aBuilder.setMessageBin(ByteString.copyFrom((byte[])data.Message));
            }
        }
        BrokerMessageProto aBrokerMessageProto = aBuilder.build();
        
        return aBrokerMessageProto.toByteArray();
    }

    private BrokerMessage deserializeBrokerMessage(byte[] data) throws InvalidProtocolBufferException
    {
        BrokerMessageProto aBrokerMessageProto = BrokerMessageProto.parseFrom(data);
        
        List<String> aMessageTypes = aBrokerMessageProto.getMessageTypesList();
        
        BrokerMessage aBrokerMessage = new BrokerMessage();
        aBrokerMessage.Request = EBrokerRequest.valueOf(aBrokerMessageProto.getRequest());
        aBrokerMessage.MessageTypes = aMessageTypes.toArray(new String[aMessageTypes.size()]);
        if (aBrokerMessageProto.hasMessageStr())
        {
            aBrokerMessage.Message = aBrokerMessageProto.getMessageStr();
        }
        else if (aBrokerMessageProto.hasMessageBin())
        {
            aBrokerMessage.Message = aBrokerMessageProto.getMessageBin().toByteArray();
        }
        
        return aBrokerMessage;
    }


    private byte[] serializeReliableMessage(ReliableMessage data)
    {
        ReliableMessageProto.Builder aBuilder = ReliableMessageProto.newBuilder()
                .setMessageId(data.MessageId)
                .setMessageType(data.MessageType.toString());
        if (data.Message != null)
        {
            if (data.Message instanceof String)
            {
                aBuilder.setMessageStr((String)data.Message);
            }
            else
            {
                aBuilder.setMessageBin(ByteString.copyFrom((byte[])data.Message));
            }
        }
        ReliableMessageProto aReliableMessageProto = aBuilder.build();
        
        return aReliableMessageProto.toByteArray();
    }

    private ReliableMessage deserializeReliableMessage(byte[] data) throws InvalidProtocolBufferException
    {
        ReliableMessageProto aReliableMessageProto = ReliableMessageProto.parseFrom(data);
        ReliableMessage aReliableMessage = new ReliableMessage();
        aReliableMessage.MessageId = aReliableMessageProto.getMessageId();
        aReliableMessage.MessageType = ReliableMessage.EMessageType.valueOf(aReliableMessageProto.getMessageType());
        if (aReliableMessageProto.hasMessageStr())
        {
            aReliableMessage.Message = aReliableMessageProto.getMessageStr();
        }
        else if (aReliableMessageProto.hasMessageBin())
        {
            aReliableMessage.Message = aReliableMessageProto.getMessageBin().toByteArray();
        }
        
        return aReliableMessage;
    }


    private byte[] serializeMonitorChannelMessage(MonitorChannelMessage data)
    {
        MonitorChannelMessageProto.Builder aBuilder = MonitorChannelMessageProto.newBuilder()
                .setMessageType(data.MessageType.toString());
        if (data.MessageContent != null)
        {
            if (data.MessageContent instanceof String)
            {
                aBuilder.setMessageContentStr((String)data.MessageContent);
            }
            else
            {
                aBuilder.setMessageContentBin(ByteString.copyFrom((byte[])data.MessageContent));
            }
        }
        MonitorChannelMessageProto aMonitorChannelMessageProto = aBuilder.build();
        
        return aMonitorChannelMessageProto.toByteArray();
    }

    private MonitorChannelMessage deserializeMonitorChannelMessage(byte[] data) throws InvalidProtocolBufferException
    {
        MonitorChannelMessageProto aMonitorChannelMessageProto = MonitorChannelMessageProto.parseFrom(data); 
        MonitorChannelMessage aMonitorChannelMessage = new MonitorChannelMessage();
        aMonitorChannelMessage.MessageType = MonitorChannelMessageType.valueOf(aMonitorChannelMessageProto.getMessageType());
        if (aMonitorChannelMessageProto.hasMessageContentStr())
        {
            aMonitorChannelMessage.MessageContent = aMonitorChannelMessageProto.getMessageContentStr();
        }
        else if (aMonitorChannelMessageProto.hasMessageContentBin())
        {
            aMonitorChannelMessage.MessageContent = aMonitorChannelMessageProto.getMessageContentBin().toByteArray();
        }
        
        return aMonitorChannelMessage;
    }
    
    
    private byte[] serializeVoidMessage(VoidMessage data)
    {
        VoidMessageProto aVoidMessageProto = VoidMessageProto.newBuilder()
                .build();
        return aVoidMessageProto.toByteArray();
    }
    
    private VoidMessage deserializeVoidMessage(byte[] data) throws InvalidProtocolBufferException
    {
        VoidMessageProto.parseFrom(data);
        VoidMessage aVoidMessage = new VoidMessage();
        return aVoidMessage;
    }
}
