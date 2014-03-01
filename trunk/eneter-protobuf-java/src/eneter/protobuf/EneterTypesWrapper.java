package eneter.protobuf;

import java.util.Arrays;
import java.util.List;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import eneter.messaging.endpoints.rpc.RpcMessage;
import eneter.messaging.endpoints.typedmessages.VoidMessage;
import eneter.messaging.endpoints.typedmessages.internal.ReliableMessage;
import eneter.messaging.messagingsystems.composites.monitoredmessagingcomposit.*;
import eneter.messaging.nodes.broker.*;
import eneter.messaging.nodes.channelwrapper.WrappedData;
import eneter.net.system.EventArgs;
import eneter.protobuf.EneterProtoBufDeclarations.*;

class EneterTypesWrapper
{
    public static byte[] serializeRpcMessage(RpcMessage data)
    {
        RpcMessageProto.Builder aBuilder = RpcMessageProto.newBuilder()
                .setId(data.Id)
                .setFlag(data.Flag)
                .setOperationName(data.OperationName)
                .setError(data.Error);
        if (data.SerializedData != null)
        {
            for (int i = 0; i < data.SerializedData.length; ++i)
            {
                aBuilder.addSerializedData(ByteString.copyFrom((byte[]) data.SerializedData[i]));
            }
        }
        
        RpcMessageProto anRpcMessageProto = aBuilder.build();
        return anRpcMessageProto.toByteArray();
    }
    
    public static RpcMessage deserializeRpcMessage(byte[] data) throws InvalidProtocolBufferException
    {
        RpcMessageProto anRpcMessageProto = RpcMessageProto.parseFrom(data);
        RpcMessage anRpcMessage = new RpcMessage();
        anRpcMessage.Id = anRpcMessageProto.getId();
        anRpcMessage.Flag = anRpcMessageProto.getFlag();
        anRpcMessage.OperationName = anRpcMessageProto.getOperationName();
        anRpcMessage.Error = anRpcMessageProto.getError();
        
        List<ByteString> aMethodParams = anRpcMessageProto.getSerializedDataList();
        if (aMethodParams != null)
        {
            anRpcMessage.SerializedData = new Object[aMethodParams.size()];
            for (int i = 0; i < aMethodParams.size(); ++i)
            {
                anRpcMessage.SerializedData[i] = aMethodParams.get(i).toByteArray();
            }
        }
        
        return anRpcMessage;
    }
    
    public static byte[] serializeEventArgs(EventArgs data)
    {
        EventArgsProto aVoidMessageProto = EventArgsProto.newBuilder().build();
        return aVoidMessageProto.toByteArray();
    }
    
    public static EventArgs deserializeEventArgs(byte[] data) throws InvalidProtocolBufferException
    {
        EventArgsProto.parseFrom(data);
        EventArgs anEventArgs = new EventArgs();
        return anEventArgs;
    }
    
    public static byte[] serializeWrappedData(WrappedData data)
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

    public static WrappedData deserializeWrappedData(byte[] data) throws InvalidProtocolBufferException
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


    public static byte[] serializeBrokerMessage(BrokerMessage data)
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

    public static BrokerMessage deserializeBrokerMessage(byte[] data) throws InvalidProtocolBufferException
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


    public static byte[] serializeReliableMessage(ReliableMessage data)
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

    public static ReliableMessage deserializeReliableMessage(byte[] data) throws InvalidProtocolBufferException
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


    public static byte[] serializeMonitorChannelMessage(MonitorChannelMessage data)
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

    public static MonitorChannelMessage deserializeMonitorChannelMessage(byte[] data) throws InvalidProtocolBufferException
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
    
    
    public static byte[] serializeVoidMessage(VoidMessage data)
    {
        VoidMessageProto aVoidMessageProto = VoidMessageProto.newBuilder()
                .build();
        return aVoidMessageProto.toByteArray();
    }
    
    public static VoidMessage deserializeVoidMessage(byte[] data) throws InvalidProtocolBufferException
    {
        VoidMessageProto.parseFrom(data);
        VoidMessage aVoidMessage = new VoidMessage();
        return aVoidMessage;
    }
}
