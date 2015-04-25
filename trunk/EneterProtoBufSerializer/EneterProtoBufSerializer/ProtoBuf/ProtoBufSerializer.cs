/*
 * Project: Eneter.ProtoBuf.Serializer
 * Author:  Ondrej Uzovic
 * 
 * Copyright © Ondrej Uzovic 2013
*/

using System;
using System.IO;
using Eneter.Messaging.DataProcessing.Serializing;
using Eneter.Messaging.EndPoints.Rpc;
using Eneter.Messaging.EndPoints.TypedMessages;
using Eneter.Messaging.MessagingSystems.Composites.MonitoredMessagingComposit;
using Eneter.Messaging.Nodes.Broker;
using Eneter.Messaging.Nodes.ChannelWrapper;
using ProtoBuf;
using Eneter.Messaging.MessagingSystems.Composites.MessageBus;

namespace Eneter.ProtoBuf
{
    /// <summary>
    /// Implements protocol buffer serialization for Eneter.Messaging.Framework.
    /// </summary>
    public class ProtoBufSerializer : ISerializer
    {
        /// <summary>
        /// Serializes data using Protocol Buffer binary format.
        /// </summary>
        /// <typeparam name="T">data type of serialized data</typeparam>
        /// <param name="dataToSerialize">protocol buffer generated data class to be serialized.</param>
        /// <returns>array containing serialized data</returns>
        public object Serialize<T>(T dataToSerialize)
        {
            object aSerializedData;

            // If it is an internal Eneter type adapt it for ProtoBuf
            if (dataToSerialize is MultiTypedMessage)
            {
                aSerializedData = SerializeMultiTypedMessage(dataToSerialize as MultiTypedMessage);
            }
            else if (dataToSerialize is MessageBusMessage)
            {
                aSerializedData = SerializeMessageBusMessage(dataToSerialize as MessageBusMessage);
            }
            else if (dataToSerialize is RpcMessage)
            {
                aSerializedData = SerializeRpcMessage(dataToSerialize as RpcMessage);
            }
            else if (dataToSerialize is EventArgs)
            {
                aSerializedData = SerializeEventArgs(dataToSerialize as EventArgs);
            }
            else if (dataToSerialize is WrappedData)
            {
                aSerializedData = SerializeWrappedData(dataToSerialize as WrappedData);
            }
            else if (dataToSerialize is BrokerMessage)
            {
                aSerializedData = SerializeBrokerMessage(dataToSerialize as BrokerMessage);
            }
            else if (dataToSerialize is MonitorChannelMessage)
            {
                aSerializedData = SerializeMonitorChannelMessage(dataToSerialize as MonitorChannelMessage);
            }
            else if (dataToSerialize is VoidMessage)
            {
                aSerializedData = SerializeVoidMessage(dataToSerialize as VoidMessage);
            }
            else
            {
                // If it is not internal Eneter message use directly ProtoBuf serializer.
                aSerializedData = SerializeProtoBuf<T>(dataToSerialize);
            }

            return aSerializedData;
        }

        /// <summary>
        /// Deserializes data from the Protocol Buffer binary format.
        /// </summary>
        /// <typeparam name="T">data type of deserialized data.</typeparam>
        /// <param name="serializedData">data (byte[]) serialized by protocol buffer to be deserialized</param>
        /// <returns>instance of deserialized data type</returns>
        public T Deserialize<T>(object serializedData)
        {
            using (MemoryStream aBuf = new MemoryStream((byte[])serializedData))
            {
                T aDeserializedObject;

                // If it is an internal Eneter type, adapt it from the proto type.
                if (typeof(T) == typeof(MultiTypedMessage))
                {
                    aDeserializedObject = (T)((object)DeserializeMultiTypedMessage(aBuf));
                }
                else if (typeof(T) == typeof(MessageBusMessage))
                {
                    aDeserializedObject = (T)((object)DeserializeMessageBusMessage(aBuf));
                }
                else if (typeof(T) == typeof(RpcMessage))
                {
                    aDeserializedObject = (T)((object)DeserializeRpcMessage(aBuf));
                }
                else if (typeof(T) == typeof(EventArgs))
                {
                    aDeserializedObject = (T)((object)DeserializeEventArgs(aBuf));
                }
                else if (typeof(T) == typeof(WrappedData))
                {
                    aDeserializedObject = (T)((object)DeserializeWrappedData(aBuf));
                }
                else if (typeof(T) == typeof(BrokerMessage))
                {
                    aDeserializedObject = (T)((object)DeserializeBrokerMessage(aBuf));
                }
                else if (typeof(T) == typeof(MonitorChannelMessage))
                {
                    aDeserializedObject = (T)((object)DeserializeMonitorChannelMessage(aBuf));
                }
                else if (typeof(T) == typeof(VoidMessage))
                {
                    aDeserializedObject = (T)((object)DeserializeVoidMessage(aBuf));
                }
                else
                {
                    // If it is not internal Eneter message use directly ProtoBuf serializer.
                    aDeserializedObject = Serializer.Deserialize<T>(aBuf);
                }

                return aDeserializedObject;
            }
        }

        private byte[] SerializeMultiTypedMessage(MultiTypedMessage data)
        {
            MultiTypedMessageProto aMultiTypedMessageProto = new MultiTypedMessageProto();
            aMultiTypedMessageProto.TypeName = data.TypeName;
            if (data.MessageData != null)
            {
                if (data.MessageData is string)
                {
                    aMultiTypedMessageProto.MessageDataStr = (string)data.MessageData;
                }
                else
                {
                    aMultiTypedMessageProto.MessageDataBin = (byte[])data.MessageData;
                }
            }
            return SerializeProtoBuf<MultiTypedMessageProto>(aMultiTypedMessageProto);
        }

        private MultiTypedMessage DeserializeMultiTypedMessage(MemoryStream data)
        {
            MultiTypedMessageProto aMultiTypedMessageProto = Serializer.Deserialize<MultiTypedMessageProto>(data);
            MultiTypedMessage aMultiTypedMessage = new MultiTypedMessage();
            aMultiTypedMessage.TypeName = aMultiTypedMessageProto.TypeName;
            if (aMultiTypedMessageProto.MessageDataStrSpecified)
            {
                aMultiTypedMessage.MessageData = aMultiTypedMessageProto.MessageDataStr;
            }
            else if (aMultiTypedMessageProto.MessageDataBinSpecified)
            {
                aMultiTypedMessage.MessageData = aMultiTypedMessageProto.MessageDataBin;
            }

            return aMultiTypedMessage;
        }

        private byte[] SerializeMessageBusMessage(MessageBusMessage data)
        {
            MessageBusMessageProto aMessageBusMessageProto = new MessageBusMessageProto();
            aMessageBusMessageProto.Request = (int)data.Request;
            aMessageBusMessageProto.Id = data.Id;
            if (data.MessageData != null)
            {
                if (data.MessageData is string)
                {
                    aMessageBusMessageProto.MessageDataStr = (string)data.MessageData;
                }
                else
                {
                    aMessageBusMessageProto.MessageDataBin = (byte[])data.MessageData;
                }
            }
            return SerializeProtoBuf<MessageBusMessageProto>(aMessageBusMessageProto);
        }

        private MessageBusMessage DeserializeMessageBusMessage(MemoryStream data)
        {
            MessageBusMessageProto aMessageBusMessageProto = Serializer.Deserialize<MessageBusMessageProto>(data);
            MessageBusMessage aMessageBusMessage = new MessageBusMessage();
            aMessageBusMessage.Request = (EMessageBusRequest)aMessageBusMessageProto.Request;
            aMessageBusMessage.Id = aMessageBusMessageProto.Id;
            if (aMessageBusMessageProto.MessageDataStrSpecified)
            {
                aMessageBusMessage.MessageData = aMessageBusMessageProto.MessageDataStr;
            }
            else if (aMessageBusMessageProto.MessageDataBinSpecified)
            {
                aMessageBusMessage.MessageData = aMessageBusMessageProto.MessageDataBin;
            }
            return aMessageBusMessage;
        }

        private byte[] SerializeRpcMessage(RpcMessage data)
        {
            RpcMessageProto anRpcMessageProto = new RpcMessageProto();
            anRpcMessageProto.Id = data.Id;
            anRpcMessageProto.Flag = data.Flag;
            anRpcMessageProto.OperationName = data.OperationName;
            if (data.SerializedData != null)
            {
                foreach (byte[] aMethodParameter in data.SerializedData)
                {
                    anRpcMessageProto.SerializedData.Add(aMethodParameter);
                }
            }
            anRpcMessageProto.ErrorType = data.ErrorType;
            anRpcMessageProto.ErrorMessage = data.ErrorMessage;
            anRpcMessageProto.ErrorDetails = data.ErrorDetails;

            return SerializeProtoBuf<RpcMessageProto>(anRpcMessageProto);
        }

        private RpcMessage DeserializeRpcMessage(MemoryStream data)
        {
            RpcMessageProto anRpcMessageProto = Serializer.Deserialize<RpcMessageProto>(data);
            RpcMessage anRpcMessage = new RpcMessage();
            anRpcMessage.Id = anRpcMessageProto.Id;
            anRpcMessage.Flag = anRpcMessageProto.Flag;
            anRpcMessage.OperationName = anRpcMessageProto.OperationName;
            anRpcMessage.ErrorType = anRpcMessageProto.ErrorType;
            anRpcMessage.ErrorMessage = anRpcMessageProto.ErrorMessage;
            anRpcMessage.ErrorDetails = anRpcMessageProto.ErrorDetails;
            
            if (anRpcMessageProto.SerializedData != null)
            {
                anRpcMessage.SerializedData = new object[anRpcMessageProto.SerializedData.Count];
                for (int i = 0; i < anRpcMessageProto.SerializedData.Count; ++i)
                {
                    anRpcMessage.SerializedData[i] = anRpcMessageProto.SerializedData[i];
                }
            }

            return anRpcMessage;
        }

        private byte[] SerializeEventArgs(EventArgs data)
        {
            return myEmptyMessage;
        }

        private EventArgs DeserializeEventArgs(MemoryStream data)
        {
            Serializer.Deserialize<EventArgsProto>(data);
            return myEventArgs;
        }

        private byte[] SerializeWrappedData(WrappedData data)
        {
            WrappedDataProto aWrappedDataProto = new WrappedDataProto();
            aWrappedDataProto.AddedData = (string)data.AddedData;
            if (data.OriginalData != null)
            {
                if (data.OriginalData is string)
                {
                    aWrappedDataProto.OriginalDataStr = (string)data.OriginalData;
                }
                else
                {
                    aWrappedDataProto.OriginalDataBin = (byte[])data.OriginalData;
                }
            }
            return SerializeProtoBuf<WrappedDataProto>(aWrappedDataProto);
        }

        private WrappedData DeserializeWrappedData(MemoryStream data)
        {
            WrappedDataProto aWrappedDataProto = Serializer.Deserialize<WrappedDataProto>(data);
            WrappedData aWrappedData = new WrappedData();
            aWrappedData.AddedData = aWrappedDataProto.AddedData;
            if (aWrappedDataProto.OriginalDataStrSpecified)
            {
                aWrappedData.OriginalData = aWrappedDataProto.OriginalDataStr;
            }
            else if (aWrappedDataProto.OriginalDataBinSpecified)
            {
                aWrappedData.OriginalData = aWrappedDataProto.OriginalDataBin;
            }

            return aWrappedData;
        }


        private byte[] SerializeBrokerMessage(BrokerMessage data)
        {
            BrokerMessageProto aBrokerMessageProto = new BrokerMessageProto();
            aBrokerMessageProto.Request = (int)data.Request;
            aBrokerMessageProto.MessageTypes.AddRange(data.MessageTypes);
            if (data.Message is string)
            {
                aBrokerMessageProto.MessageStr = (string)data.Message;
            }
            else
            {
                aBrokerMessageProto.MessageBin = (byte[])data.Message;
            }
            return SerializeProtoBuf<BrokerMessageProto>(aBrokerMessageProto);
        }

        private BrokerMessage DeserializeBrokerMessage(MemoryStream data)
        {
            BrokerMessageProto aBrokerMessageProto = Serializer.Deserialize<BrokerMessageProto>(data);
            BrokerMessage aBrokerMessage = new BrokerMessage();
            aBrokerMessage.Request = (EBrokerRequest)aBrokerMessageProto.Request;
            aBrokerMessage.MessageTypes = aBrokerMessageProto.MessageTypes.ToArray();
            if (aBrokerMessageProto.MessageStrSpecified)
            {
                aBrokerMessage.Message = aBrokerMessageProto.MessageStr;
            }
            else if (aBrokerMessageProto.MessageBinSpecified)
            {
                aBrokerMessage.Message = aBrokerMessageProto.MessageBin;
            }
            return aBrokerMessage;
        }


        private byte[] SerializeMonitorChannelMessage(MonitorChannelMessage data)
        {
            MonitorChannelMessageProto aMonitorChannelMessageProto = new MonitorChannelMessageProto();
            aMonitorChannelMessageProto.MessageType = (int)data.MessageType;
            if (data.MessageContent is string)
            {
                aMonitorChannelMessageProto.MessageContentStr = (string)data.MessageContent;
            }
            else
            {
                aMonitorChannelMessageProto.MessageContentBin = (byte[])data.MessageContent;
            }
            return SerializeProtoBuf<MonitorChannelMessageProto>(aMonitorChannelMessageProto);
        }

        private MonitorChannelMessage DeserializeMonitorChannelMessage(MemoryStream data)
        {
            MonitorChannelMessageProto aMonitorChannelMessageProto = Serializer.Deserialize<MonitorChannelMessageProto>(data);
            MonitorChannelMessage aMonitorChannelMessage = new MonitorChannelMessage();
            aMonitorChannelMessage.MessageType = (MonitorChannelMessageType)aMonitorChannelMessageProto.MessageType;
            if (aMonitorChannelMessageProto.MessageContentStrSpecified)
            {
                aMonitorChannelMessage.MessageContent = aMonitorChannelMessageProto.MessageContentStr;
            }
            else if (aMonitorChannelMessageProto.MessageContentBinSpecified)
            {
                aMonitorChannelMessage.MessageContent = aMonitorChannelMessageProto.MessageContentBin;
            }
            return aMonitorChannelMessage;
        }

        private byte[] SerializeVoidMessage(VoidMessage data)
        {
            return myEmptyMessage;
        }

        private VoidMessage DeserializeVoidMessage(MemoryStream data)
        {
            // Read bytes from the stream.
            Serializer.Deserialize<VoidMessageProto>(data);
            return myVoidMessage;
        }

        private byte[] SerializeProtoBuf<_T>(_T dataToSerialize)
        {
            using (MemoryStream aBuf = new MemoryStream())
            {
                Serializer.Serialize<_T>(aBuf, dataToSerialize);
                return aBuf.ToArray();
            }
        }

        private readonly EventArgs myEventArgs = new EventArgs();
        private readonly VoidMessage myVoidMessage = new VoidMessage();
        private readonly byte[] myEmptyMessage = { 8, 0 };
    }
}
