/**
 * Project: Eneter.ProtoBuf.Serializer
 * Author: Ondrej Uzovic
 * 
 * Copyright © 2013 Ondrej Uzovic
 * 
 */

package eneter.protobuf;

import java.lang.reflect.Method;

import com.google.protobuf.GeneratedMessage;

import eneter.messaging.dataprocessing.serializing.ISerializer;
import eneter.messaging.endpoints.rpc.RpcMessage;
import eneter.messaging.endpoints.typedmessages.VoidMessage;
import eneter.messaging.endpoints.typedmessages.internal.ReliableMessage;
import eneter.messaging.messagingsystems.composites.monitoredmessagingcomposit.*;
import eneter.messaging.nodes.broker.*;
import eneter.messaging.nodes.channelwrapper.WrappedData;


/**
 * Implements protocol buffer serialization for Eneter.Messaging.Framework.
 * 
 *
 */
public class ProtoBufSerializer implements ISerializer
{

    /**
     * Serializes data using Protocol Buffer binary format.
     * @param dataToSerialize protocol buffer generated data class to be serialized.
     * @param clazz data type of serialized data.
     * @return byte[] array containing serialized data. 
     */
    @Override
    public <T> Object serialize(T dataToSerialize, Class<T> clazz)
            throws Exception
    {
        boolean aUseDirectlyProtobBuf = false;
        Object aSerializedData = null;
        
        if (clazz == String.class)
        {
            aSerializedData = PrimitiveTypesWrapper.serializeString((String)dataToSerialize);
        }
        // If it is a primitive type
        else if (clazz.isPrimitive())
        {
            if (clazz == Boolean.class || clazz == Byte.class || clazz == Character.class ||
                clazz == Short.class || clazz == Integer.class || clazz == Long.class ||
                clazz == Float.class || clazz == Double.class)
            {
                throw new IllegalStateException("ProtoBufSerializer does not support primitive types as references (e.g. int is supported but not Integer).");
            }
            
            if (clazz == boolean.class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeBool((Boolean)dataToSerialize);
            }
            else if (clazz == byte.class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeByte((Byte)dataToSerialize);
            }
            else if (clazz == char.class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeChar((Character)dataToSerialize);
            }
            else if (clazz == short.class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeShort((Short)dataToSerialize);
            }
            else if (clazz == int.class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeInt((Integer)dataToSerialize);
            }
            else if (clazz == long.class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeLong((Long)dataToSerialize);
            }
            else if (clazz == float.class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeFloat((Float)dataToSerialize);
            }
            else if (clazz == double.class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeDouble((Double)dataToSerialize);
            }
            else
            {
                aUseDirectlyProtobBuf = true;
            }
        }
        // if it is an array of string or a primitive type.
        else if (clazz.isArray())
        {
            if (clazz == Boolean[].class || clazz == Byte[].class || clazz == Character[].class ||
                clazz == Short[].class || clazz == Integer[].class || clazz == Long[].class ||
                clazz == Float[].class || clazz == Double[].class)
            {
                throw new IllegalStateException("ProtoBufSerializer does not support arrays of primitive types references (e.g. int[] is supported but not Integer[]).");
            }
            
            if (clazz == String[].class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeStringArray((String[])dataToSerialize);
            }
            else if (clazz == boolean[].class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeBoolArray((boolean[])dataToSerialize);
            }
            else if (clazz == byte[].class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeByteArray((byte[])dataToSerialize);
            }
            else if (clazz == char[].class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeCharArray((char[])dataToSerialize);
            }
            else if (clazz == short[].class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeShortArray((short[])dataToSerialize);
            }
            else if (clazz == int[].class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeIntArray((int[])dataToSerialize);
            }
            else if (clazz == long[].class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeLongArray((long[])dataToSerialize);
            }
            else if (clazz == float[].class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeFloatArray((float[])dataToSerialize);
            }
            else if (clazz == double[].class)
            {
                aSerializedData = PrimitiveTypesWrapper.serializeDoubleArray((double[])dataToSerialize);
            }
            else
            {
                aUseDirectlyProtobBuf = true;
            }
        }
        // If it is an internal Eneter type adapt it for ProtoBuf
        else if (clazz == RpcMessage.class)
        {
            aSerializedData = EneterTypesWrapper.serializeRpcMessage((RpcMessage)dataToSerialize);
        }
        else if (clazz == WrappedData.class)
        {
            aSerializedData = EneterTypesWrapper.serializeWrappedData((WrappedData)dataToSerialize);
        }
        else if (clazz == BrokerMessage.class)
        {
            aSerializedData = EneterTypesWrapper.serializeBrokerMessage((BrokerMessage)dataToSerialize);
        }
        else if (clazz == ReliableMessage.class)
        {
            aSerializedData = EneterTypesWrapper.serializeReliableMessage((ReliableMessage)dataToSerialize);
        }
        else if (clazz == MonitorChannelMessage.class)
        {
            aSerializedData = EneterTypesWrapper.serializeMonitorChannelMessage((MonitorChannelMessage)dataToSerialize);
        }
        else if (clazz == VoidMessage.class)
        {
            aSerializedData = EneterTypesWrapper.serializeVoidMessage((VoidMessage)dataToSerialize);
        }
        else
        {
            aUseDirectlyProtobBuf = true;
        }

        // If it is not string, primitive type or array of an primitive type then
        // try to use directly ProtoBuf serializer.
        if (aUseDirectlyProtobBuf)
        {
            GeneratedMessage aDataToSerialize = (GeneratedMessage)dataToSerialize;
            aSerializedData = aDataToSerialize.toByteArray();
        }
        
        return aSerializedData;
    }

    /**
     * Deserializes data from the Protocol Buffer binary format.
     * @param serializedData data (byte[]) serialized by protocol buffer to be deserialized
     * @param clazz data type of deserialized data.
     * @return instance of deserialized data type 
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(Object serializedData, Class<T> clazz)
            throws Exception
    {
        boolean aUseDirectlyProtoBuf = false;
        T aDeserializedObject = null;

        if (clazz == String.class)
        {
            aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeString((byte[])serializedData));
        }
        // If it is a primitive type
        else if (clazz.isPrimitive())
        {
            if (clazz == Boolean.class || clazz == Byte.class || clazz == Character.class ||
                clazz == Short.class || clazz == Integer.class || clazz == Long.class ||
                clazz == Float.class || clazz == Double.class)
            {
                throw new IllegalStateException("ProtoBufSerializer does not support primitive types as references (e.g. int is supported but not Integer).");
            }
            
            if (clazz == boolean.class)
            {
                Boolean aResult = PrimitiveTypesWrapper.deserializeBool((byte[])serializedData);
                aDeserializedObject = (T) aResult;
            }
            else if (clazz == byte.class)
            {
                Byte aResult = PrimitiveTypesWrapper.deserializeByte((byte[])serializedData);
                aDeserializedObject = (T) aResult;
            }
            else if (clazz == char.class)
            {
                Character aResult = PrimitiveTypesWrapper.deserializeChar((byte[])serializedData);
                aDeserializedObject = (T) aResult;
            }
            else if (clazz == short.class)
            {
                Short aResult = PrimitiveTypesWrapper.deserializeShort((byte[])serializedData);
                aDeserializedObject = (T) aResult;
            }
            else if (clazz == int.class)
            {
                Integer aResult = PrimitiveTypesWrapper.deserializeInt((byte[])serializedData);
                aDeserializedObject = (T) aResult;
            }
            else if (clazz == long.class)
            {
                Long aResult = PrimitiveTypesWrapper.deserializeLong((byte[])serializedData);
                aDeserializedObject = (T) aResult;
            }
            else if (clazz == float.class)
            {
                Float aResult = PrimitiveTypesWrapper.deserializeFloat((byte[])serializedData);
                aDeserializedObject = (T) aResult;
            }
            else if (clazz == double.class)
            {
                Double aResult = PrimitiveTypesWrapper.deserializeDouble((byte[])serializedData);
                aDeserializedObject = (T) aResult;
            }
            else
            {
                aUseDirectlyProtoBuf = true;
            }
        }
        // If is an array of string or a primitive type.
        else if (clazz.isArray())
        {
            if (clazz == Boolean[].class || clazz == Byte[].class || clazz == Character[].class ||
                clazz == Short[].class || clazz == Integer[].class || clazz == Long[].class ||
                clazz == Float[].class || clazz == Double[].class)
            {
                throw new IllegalStateException("ProtoBufSerializer does not support arrays of primitive types references (e.g. int[] is supported but not Integer[]).");
            }
            
            if (clazz == String[].class)
            {
                aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeStringArray((byte[])serializedData));
            }
            else if (clazz == boolean[].class)
            {
                aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeBoolArray((byte[])serializedData));
            }
            else if (clazz == byte[].class)
            {
                aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeByteArray((byte[])serializedData));
            }
            else if (clazz == char[].class)
            {
                aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeCharArray((byte[])serializedData));
            }
            else if (clazz == short[].class)
            {
                aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeShortArray((byte[])serializedData));
            }
            else if (clazz == int[].class)
            {
                aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeIntArray((byte[])serializedData));
            }
            else if (clazz == long[].class)
            {
                aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeLongArray((byte[])serializedData));
            }
            else if (clazz == float[].class)
            {
                aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeFloatArray((byte[])serializedData));
            }
            else if (clazz == double[].class)
            {
                aDeserializedObject = clazz.cast(PrimitiveTypesWrapper.deserializeDoubleArray((byte[])serializedData));
            }
            else
            {
                aUseDirectlyProtoBuf = true;
            }
        }
        // If it is an internal Eneter type, adapt it from the proto type.
        else if (clazz == RpcMessage.class)
        {
            aDeserializedObject = clazz.cast(EneterTypesWrapper.deserializeRpcMessage((byte[])serializedData));
        }
        else if (clazz == WrappedData.class)
        {
            aDeserializedObject = clazz.cast(EneterTypesWrapper.deserializeWrappedData((byte[])serializedData));
        }
        else if (clazz == BrokerMessage.class)
        {
            aDeserializedObject = clazz.cast(EneterTypesWrapper.deserializeBrokerMessage((byte[])serializedData)); 
        }
        else if (clazz == ReliableMessage.class)
        {
            aDeserializedObject = clazz.cast(EneterTypesWrapper.deserializeReliableMessage((byte[])serializedData)); 
        }
        else if (clazz == MonitorChannelMessage.class)
        {
            aDeserializedObject = clazz.cast(EneterTypesWrapper.deserializeMonitorChannelMessage((byte[])serializedData)); 
        }
        else if (clazz == VoidMessage.class)
        {
            aDeserializedObject = clazz.cast(EneterTypesWrapper.deserializeVoidMessage((byte[])serializedData));
        }
        else
        {
            aUseDirectlyProtoBuf = true;
        }
        
        if (aUseDirectlyProtoBuf)
        {
            // If it is not an Eneter internal type then use directly protobuf.
            Method aParsingMethod = clazz.getMethod("parseFrom", byte[].class);
            aDeserializedObject = clazz.cast(aParsingMethod.invoke(null, (byte[])serializedData));
        }
        
        return aDeserializedObject;
    }
    
    
}
