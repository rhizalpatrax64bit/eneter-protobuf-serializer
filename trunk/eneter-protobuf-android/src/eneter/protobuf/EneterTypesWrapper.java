/**
 * Project: Eneter.ProtoBuf.Serializer
 * Author: Ondrej Uzovic
 * 
 * Copyright © 2013 Ondrej Uzovic
 * 
 */

package eneter.protobuf;

import java.util.Arrays;
import java.util.List;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import eneter.messaging.endpoints.rpc.RpcMessage;
import eneter.messaging.endpoints.typedmessages.MultiTypedMessage;
import eneter.messaging.endpoints.typedmessages.VoidMessage;
import eneter.messaging.messagingsystems.composites.messagebus.EMessageBusRequest;
import eneter.messaging.messagingsystems.composites.messagebus.MessageBusMessage;
import eneter.messaging.messagingsystems.composites.monitoredmessagingcomposit.*;
import eneter.messaging.nodes.broker.*;
import eneter.messaging.nodes.channelwrapper.WrappedData;
import eneter.net.system.EventArgs;
import eneter.protobuf.EneterProtoBufDeclarations.*;

class EneterTypesWrapper
{
    public static byte[] serializeMultiTypedMessage(MultiTypedMessage data)
    {
        MultiTypedMessageProto.Builder aBuilder = MultiTypedMessageProto.newBuilder()
                .setTypeName(data.TypeName);
        if (data.MessageData != null)
        {
            if (data.MessageData instanceof String)
            {
                aBuilder.setMessageDataStr((String)data.MessageData);
            }
            else
            {
                aBuilder.setMessageDataBin(ByteString.copyFrom((byte[]) data.MessageData));
            }
        }
        MultiTypedMessageProto aMessage = aBuilder.build();
        return aMessage.toByteArray();
    }

    public static MultiTypedMessage deserializeMultiTypedMessage(byte[] data) throws InvalidProtocolBufferException
    {
        MultiTypedMessageProto aMultiTyMessageProto = MultiTypedMessageProto.parseFrom(data);
        MultiTypedMessage aMessage = new MultiTypedMessage();
        aMessage.TypeName = aMultiTyMessageProto.getTypeName();
        if (aMultiTyMessageProto.hasMessageDataStr())
        {
            aMessage.MessageData = aMultiTyMessageProto.getMessageDataStr();
        }
        else if (aMultiTyMessageProto.hasMessageDataBin())
        {
            aMessage.MessageData = aMultiTyMessageProto.getMessageDataBin().toByteArray();
        }

        return aMessage;
    }


    public static byte[] serializeMessageBusMessage(MessageBusMessage data)
    {
        MessageBusMessageProto.Builder aBuilder = MessageBusMessageProto.newBuilder()
                .setRequest(data.Request.geValue())
                .setId(data.Id);
        if (data.MessageData != null)
        {
            if (data.MessageData instanceof String)
            {
                aBuilder.setMessageDataStr((String)data.MessageData);
            }
            else
            {
                aBuilder.setMessageDataBin(ByteString.copyFrom((byte[]) data.MessageData));
            }
        }
        MessageBusMessageProto aMessage = aBuilder.build();

        return aMessage.toByteArray();
    }

    public static MessageBusMessage deserializeMessageBusMessage(byte[] data) throws InvalidProtocolBufferException
    {
        MessageBusMessageProto aMessageBusMessageProto = MessageBusMessageProto.parseFrom(data);
        MessageBusMessage aMessage = new MessageBusMessage();
        aMessage.Request = EMessageBusRequest.fromInt(aMessageBusMessageProto.getRequest());
        aMessage.Id = aMessageBusMessageProto.getId();
        if (aMessageBusMessageProto.hasMessageDataStr())
        {
            aMessage.MessageData = aMessageBusMessageProto.getMessageDataStr();
        }
        else if (aMessageBusMessageProto.hasMessageDataBin())
        {
            aMessage.MessageData = aMessageBusMessageProto.getMessageDataBin().toByteArray();
        }

        return aMessage;
    }


    public static byte[] serializeRpcMessage(RpcMessage data)
    {
        RpcMessageProto.Builder aBuilder = RpcMessageProto.newBuilder()
                .setId(data.Id)
                .setFlag(data.Flag)
                .setOperationName(data.OperationName)
                .setErrorType(data.ErrorType)
                .setErrorMessage(data.ErrorMessage)
                .setErrorDetails(data.ErrorDetails);
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
        anRpcMessage.ErrorType = anRpcMessageProto.getErrorType();
        anRpcMessage.ErrorMessage = anRpcMessageProto.getErrorMessage();
        anRpcMessage.ErrorDetails = anRpcMessageProto.getErrorDetails();
        
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
        return myEmptyMessage;
    }
    
    public static EventArgs deserializeEventArgs(byte[] data) throws InvalidProtocolBufferException
    {
        EventArgsProto.parseFrom(data);
        return myEventArgs;
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
                .setRequest(data.Request.geValue())
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
        aBrokerMessage.Request = EBrokerRequest.fromInt(aBrokerMessageProto.getRequest());
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

    
    public static byte[] serializeMonitorChannelMessage(MonitorChannelMessage data)
    {
        MonitorChannelMessageProto.Builder aBuilder = MonitorChannelMessageProto.newBuilder()
                .setMessageType(data.MessageType.geValue());
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
        aMonitorChannelMessage.MessageType = MonitorChannelMessageType.fromInt(aMonitorChannelMessageProto.getMessageType());
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
        return myEmptyMessage;
    }
    
    public static VoidMessage deserializeVoidMessage(byte[] data) throws InvalidProtocolBufferException
    {
        VoidMessageProto.parseFrom(data);
        return myVoidMessage;
    }
    
    
    private static final EventArgs myEventArgs = new EventArgs();
    private static final VoidMessage myVoidMessage = new VoidMessage();
    private static final byte[] myEmptyMessage = { 8, 0 };
}
