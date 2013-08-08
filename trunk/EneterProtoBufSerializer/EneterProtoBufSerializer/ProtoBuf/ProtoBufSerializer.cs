/*
 * Project: Eneter.ProtoBuf.Serializer
 * Author:  Ondrej Uzovic
 * 
 * Copyright © Ondrej Uzovic 2013
*/

using System;
using System.IO;
using Eneter.Messaging.DataProcessing.Serializing;
using Eneter.Messaging.EndPoints.TypedMessages;
using Eneter.Messaging.MessagingSystems.Composites.MonitoredMessagingComposit;
using Eneter.Messaging.Nodes.Broker;
using Eneter.Messaging.Nodes.ChannelWrapper;
using ProtoBuf;

namespace Eneter.ProtoBuf
{
    public class ProtoBufSerializer : ISerializer
    {
        public object Serialize<_T>(_T dataToSerialize)
        {
            object aSerializedData;

            // If it is an internal Eneter type adapt it for ProtoBuf
            if (dataToSerialize is WrappedData)
            {
                aSerializedData = SerializeWrappedData(dataToSerialize as WrappedData);
            }
            else if (dataToSerialize is BrokerMessage)
            {
                aSerializedData = SerializeBrokerMessage(dataToSerialize as BrokerMessage);
            }
            else if (dataToSerialize is ReliableMessage)
            {
                aSerializedData = SerializeReliableMessage(dataToSerialize as ReliableMessage);
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
                aSerializedData = SerializeProtoBuf<_T>(dataToSerialize);
            }

            return aSerializedData;
        }

        public _T Deserialize<_T>(object serializedData)
        {
            using (MemoryStream aBuf = new MemoryStream((byte[])serializedData))
            {
                _T aDeserializedObject;

                // If it is an internal Eneter type, adapt it from the proto type.
                if (typeof(_T) == typeof(WrappedData))
                {
                    aDeserializedObject = (_T)((object)DeserializeWrappedData(aBuf));
                }
                else if (typeof(_T) == typeof(BrokerMessage))
                {
                    aDeserializedObject = (_T)((object)DeserializeBrokerMessage(aBuf));
                }
                else if (typeof(_T) == typeof(ReliableMessage))
                {
                    aDeserializedObject = (_T)((object)DeserializeReliableMessage(aBuf));
                }
                else if (typeof(_T) == typeof(MonitorChannelMessage))
                {
                    aDeserializedObject = (_T)((object)DeserializeMonitorChannelMessage(aBuf));
                }
                else if (typeof(_T) == typeof(VoidMessage))
                {
                    aDeserializedObject = (_T)((object)DeserializeVoidMessage(aBuf));
                }
                else
                {
                    // If it is not internal Eneter message use directly ProtoBuf serializer.
                    aDeserializedObject = Serializer.Deserialize<_T>(aBuf);
                }

                return aDeserializedObject;
            }
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
            aBrokerMessageProto.Request = data.Request.ToString();
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
            aBrokerMessage.Request = (EBrokerRequest) Enum.Parse(typeof(EBrokerRequest), aBrokerMessageProto.Request, true);
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


        private byte[] SerializeReliableMessage(ReliableMessage data)
        {
            ReliableMessageProto aReliableMessageProto = new ReliableMessageProto();
            aReliableMessageProto.MessageId = data.MessageId;
            aReliableMessageProto.MessageType = data.MessageType.ToString();
            if (data.Message is string)
            {
                aReliableMessageProto.MessageStr = (string)data.Message;
            }
            else
            {
                aReliableMessageProto.MessageBin = (byte[])data.Message;
            }
            return SerializeProtoBuf<ReliableMessageProto>(aReliableMessageProto);
        }

        private ReliableMessage DeserializeReliableMessage(MemoryStream data)
        {
            ReliableMessageProto aReliableMessageProto = Serializer.Deserialize<ReliableMessageProto>(data);
            ReliableMessage aReliableMessage = new ReliableMessage();
            aReliableMessage.MessageId = aReliableMessageProto.MessageId;
            aReliableMessage.MessageType = (ReliableMessage.EMessageType)Enum.Parse(typeof(ReliableMessage.EMessageType), aReliableMessageProto.MessageType, true);
            if (aReliableMessageProto.MessageStrSpecified)
            {
                aReliableMessage.Message = aReliableMessageProto.MessageStr;
            }
            else if (aReliableMessageProto.MessageBinSpecified)
            {
                aReliableMessage.Message = aReliableMessageProto.MessageBin;
            }
            return aReliableMessage;
        }


        private byte[] SerializeMonitorChannelMessage(MonitorChannelMessage data)
        {
            MonitorChannelMessageProto aMonitorChannelMessageProto = new MonitorChannelMessageProto();
            aMonitorChannelMessageProto.MessageType = data.MessageType.ToString();
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
            aMonitorChannelMessage.MessageType = (MonitorChannelMessageType)Enum.Parse(typeof(MonitorChannelMessageType), aMonitorChannelMessageProto.MessageType, true);
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
            VoidMessageProto aVoidMessageProto = new VoidMessageProto();
            return SerializeProtoBuf<VoidMessageProto>(aVoidMessageProto);
        }

        private VoidMessage DeserializeVoidMessage(MemoryStream data)
        {
            Serializer.Deserialize<VoidMessageProto>(data);
            VoidMessage aVoidMessage = new VoidMessage();
            return aVoidMessage;
        }

        private byte[] SerializeProtoBuf<_T>(_T dataToSerialize)
        {
            using (MemoryStream aBuf = new MemoryStream())
            {
                Serializer.Serialize<_T>(aBuf, dataToSerialize);
                return aBuf.ToArray();
            }
        }
    }
}
