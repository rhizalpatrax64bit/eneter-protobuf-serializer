//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

// Option: missing-value detection (*Specified/ShouldSerialize*/Reset*) enabled
    
// Option: light framework (CF/Silverlight) enabled
    
// Generated from: EneterProtoBufDeclarations.proto
namespace Eneter.ProtoBuf
{
  [global::ProtoBuf.ProtoContract(Name=@"WrappedDataProto")]
  public partial class WrappedDataProto : global::ProtoBuf.IExtensible
  {
    public WrappedDataProto() {}
    
    private string _AddedData;
    [global::ProtoBuf.ProtoMember(1, IsRequired = true, Name=@"AddedData", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public string AddedData
    {
      get { return _AddedData; }
      set { _AddedData = value; }
    }

    private byte[] _OriginalDataBin;
    [global::ProtoBuf.ProtoMember(2, IsRequired = false, Name=@"OriginalDataBin", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public byte[] OriginalDataBin
    {
      get { return _OriginalDataBin?? null; }
      set { _OriginalDataBin = value; }
    }
    [global::System.Xml.Serialization.XmlIgnore]
    
    public bool OriginalDataBinSpecified
    {
      get { return _OriginalDataBin != null; }
      set { if (value == (_OriginalDataBin== null)) _OriginalDataBin = value ? OriginalDataBin : (byte[])null; }
    }
    private bool ShouldSerializeOriginalDataBin() { return OriginalDataBinSpecified; }
    private void ResetOriginalDataBin() { OriginalDataBinSpecified = false; }
    

    private string _OriginalDataStr;
    [global::ProtoBuf.ProtoMember(3, IsRequired = false, Name=@"OriginalDataStr", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public string OriginalDataStr
    {
      get { return _OriginalDataStr?? ""; }
      set { _OriginalDataStr = value; }
    }
    [global::System.Xml.Serialization.XmlIgnore]
    
    public bool OriginalDataStrSpecified
    {
      get { return _OriginalDataStr != null; }
      set { if (value == (_OriginalDataStr== null)) _OriginalDataStr = value ? OriginalDataStr : (string)null; }
    }
    private bool ShouldSerializeOriginalDataStr() { return OriginalDataStrSpecified; }
    private void ResetOriginalDataStr() { OriginalDataStrSpecified = false; }
    
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
  [global::ProtoBuf.ProtoContract(Name=@"BrokerMessageProto")]
  public partial class BrokerMessageProto : global::ProtoBuf.IExtensible
  {
    public BrokerMessageProto() {}
    
    private string _Request;
    [global::ProtoBuf.ProtoMember(1, IsRequired = true, Name=@"Request", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public string Request
    {
      get { return _Request; }
      set { _Request = value; }
    }
    private readonly global::System.Collections.Generic.List<string> _MessageTypes = new global::System.Collections.Generic.List<string>();
    [global::ProtoBuf.ProtoMember(2, Name=@"MessageTypes", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public global::System.Collections.Generic.List<string> MessageTypes
    {
      get { return _MessageTypes; }
    }
  

    private byte[] _MessageBin;
    [global::ProtoBuf.ProtoMember(3, IsRequired = false, Name=@"MessageBin", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public byte[] MessageBin
    {
      get { return _MessageBin?? null; }
      set { _MessageBin = value; }
    }
    [global::System.Xml.Serialization.XmlIgnore]
    
    public bool MessageBinSpecified
    {
      get { return _MessageBin != null; }
      set { if (value == (_MessageBin== null)) _MessageBin = value ? MessageBin : (byte[])null; }
    }
    private bool ShouldSerializeMessageBin() { return MessageBinSpecified; }
    private void ResetMessageBin() { MessageBinSpecified = false; }
    

    private string _MessageStr;
    [global::ProtoBuf.ProtoMember(4, IsRequired = false, Name=@"MessageStr", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public string MessageStr
    {
      get { return _MessageStr?? ""; }
      set { _MessageStr = value; }
    }
    [global::System.Xml.Serialization.XmlIgnore]
    
    public bool MessageStrSpecified
    {
      get { return _MessageStr != null; }
      set { if (value == (_MessageStr== null)) _MessageStr = value ? MessageStr : (string)null; }
    }
    private bool ShouldSerializeMessageStr() { return MessageStrSpecified; }
    private void ResetMessageStr() { MessageStrSpecified = false; }
    
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
  [global::ProtoBuf.ProtoContract(Name=@"ReliableMessageProto")]
  public partial class ReliableMessageProto : global::ProtoBuf.IExtensible
  {
    public ReliableMessageProto() {}
    
    private string _MessageType;
    [global::ProtoBuf.ProtoMember(1, IsRequired = true, Name=@"MessageType", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public string MessageType
    {
      get { return _MessageType; }
      set { _MessageType = value; }
    }
    private string _MessageId;
    [global::ProtoBuf.ProtoMember(2, IsRequired = true, Name=@"MessageId", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public string MessageId
    {
      get { return _MessageId; }
      set { _MessageId = value; }
    }

    private byte[] _MessageBin;
    [global::ProtoBuf.ProtoMember(3, IsRequired = false, Name=@"MessageBin", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public byte[] MessageBin
    {
      get { return _MessageBin?? null; }
      set { _MessageBin = value; }
    }
    [global::System.Xml.Serialization.XmlIgnore]
    
    public bool MessageBinSpecified
    {
      get { return _MessageBin != null; }
      set { if (value == (_MessageBin== null)) _MessageBin = value ? MessageBin : (byte[])null; }
    }
    private bool ShouldSerializeMessageBin() { return MessageBinSpecified; }
    private void ResetMessageBin() { MessageBinSpecified = false; }
    

    private string _MessageStr;
    [global::ProtoBuf.ProtoMember(4, IsRequired = false, Name=@"MessageStr", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public string MessageStr
    {
      get { return _MessageStr?? ""; }
      set { _MessageStr = value; }
    }
    [global::System.Xml.Serialization.XmlIgnore]
    
    public bool MessageStrSpecified
    {
      get { return _MessageStr != null; }
      set { if (value == (_MessageStr== null)) _MessageStr = value ? MessageStr : (string)null; }
    }
    private bool ShouldSerializeMessageStr() { return MessageStrSpecified; }
    private void ResetMessageStr() { MessageStrSpecified = false; }
    
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
  [global::ProtoBuf.ProtoContract(Name=@"MonitorChannelMessageProto")]
  public partial class MonitorChannelMessageProto : global::ProtoBuf.IExtensible
  {
    public MonitorChannelMessageProto() {}
    
    private string _MessageType;
    [global::ProtoBuf.ProtoMember(1, IsRequired = true, Name=@"MessageType", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public string MessageType
    {
      get { return _MessageType; }
      set { _MessageType = value; }
    }

    private byte[] _MessageContentBin;
    [global::ProtoBuf.ProtoMember(2, IsRequired = false, Name=@"MessageContentBin", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public byte[] MessageContentBin
    {
      get { return _MessageContentBin?? null; }
      set { _MessageContentBin = value; }
    }
    [global::System.Xml.Serialization.XmlIgnore]
    
    public bool MessageContentBinSpecified
    {
      get { return _MessageContentBin != null; }
      set { if (value == (_MessageContentBin== null)) _MessageContentBin = value ? MessageContentBin : (byte[])null; }
    }
    private bool ShouldSerializeMessageContentBin() { return MessageContentBinSpecified; }
    private void ResetMessageContentBin() { MessageContentBinSpecified = false; }
    

    private string _MessageContentStr;
    [global::ProtoBuf.ProtoMember(3, IsRequired = false, Name=@"MessageContentStr", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public string MessageContentStr
    {
      get { return _MessageContentStr?? ""; }
      set { _MessageContentStr = value; }
    }
    [global::System.Xml.Serialization.XmlIgnore]
    
    public bool MessageContentStrSpecified
    {
      get { return _MessageContentStr != null; }
      set { if (value == (_MessageContentStr== null)) _MessageContentStr = value ? MessageContentStr : (string)null; }
    }
    private bool ShouldSerializeMessageContentStr() { return MessageContentStrSpecified; }
    private void ResetMessageContentStr() { MessageContentStrSpecified = false; }
    
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
  [global::ProtoBuf.ProtoContract(Name=@"VoidMessageProto")]
  public partial class VoidMessageProto : global::ProtoBuf.IExtensible
  {
    public VoidMessageProto() {}
    
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
}