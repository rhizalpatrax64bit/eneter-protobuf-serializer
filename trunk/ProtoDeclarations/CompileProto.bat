\Ondrej\Projects\Externals\ProtoBufNet\ProtoGen\protogen.exe -i:EneterProtoBufDeclarations.proto -o:EneterProtoBufDeclarations.cs -p:detectMissing -p:lightFramework -ns:Eneter.ProtoBuf

\Ondrej\Projects\Externals\protobuf-2.6.1\protoc.exe -I=./ --java_out=./ ./EneterProtoBufDeclarations.proto
\Ondrej\Projects\Externals\protobuf-2.6.1\protoc.exe -I=./ --java_out=./ ./EneterProtoBufPrimitivesDeclarations.proto