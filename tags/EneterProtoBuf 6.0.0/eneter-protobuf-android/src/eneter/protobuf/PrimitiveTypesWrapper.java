package eneter.protobuf;

import java.util.Arrays;
import java.util.List;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import eneter.protobuf.EneterProtoBufPrimitivesDeclarations.*;


class PrimitiveTypesWrapper
{
    public static byte[] serializeString(String data)
    {
        return StringWrapper.newBuilder().setValue(data).build().toByteArray();
    }
    
    public static String deserializeString(byte[] data) throws InvalidProtocolBufferException
    {
        return StringWrapper.parseFrom(data).getValue();
    }
    
    public static byte[] serializeStringArray(String[] data)
    {
        StringArrayWrapper.Builder aBuilder = StringArrayWrapper.newBuilder();
        if (data != null)
        {
            aBuilder.addAllValue(Arrays.asList(data));
        }
        else
        {
            aBuilder.addAllValue(Arrays.asList(myEmptyStringArray));
        }
        return aBuilder.build().toByteArray();
    }
    
    public static String[] deserializeStringArray(byte[] data) throws InvalidProtocolBufferException
    {
        StringArrayWrapper aWrapper = StringArrayWrapper.parseFrom(data);
        List<String> aList = aWrapper.getValueList();
        String[] anArray = aList.toArray(new String[aList.size()]);
        return anArray;
    }
    
    
    
    public static byte[] serializeBool(boolean data)
    {
        return BooleanWrapper.newBuilder().setValue(data).build().toByteArray();
    }
    
    public static boolean deserializeBool(byte[] data) throws InvalidProtocolBufferException
    {
        return BooleanWrapper.parseFrom(data).getValue();
    }
    
    public static byte[] serializeBoolArray(boolean[] data)
    {
        BooleanArrayWrapper.Builder aBuilder = BooleanArrayWrapper.newBuilder();
        if (data != null)
        {
            for (boolean i : data)
            {
                aBuilder.addValue(i);
            }
        }
        else
        {
            aBuilder.addAllValue(Arrays.asList(myEmptyBoolArray));
        }
        return aBuilder.build().toByteArray();
    }
    
    public static boolean[] deserializeBoolArray(byte[] data) throws InvalidProtocolBufferException
    {
        BooleanArrayWrapper aWrapper = BooleanArrayWrapper.parseFrom(data);
        List<Boolean> aList = aWrapper.getValueList();
        boolean[] anArray = new boolean[aList.size()];
        for (int i = 0; i < anArray.length; ++i)
        {
            anArray[i] = aList.get(i);
        }
        return anArray;
    }
    
    
    
    
    public static byte[] serializeByte(byte data)
    {
        ByteString aBytes = ByteString.copyFrom(new byte[] { data } );
        return ByteWrapper.newBuilder().setValue(aBytes).build().toByteArray();
    }
    
    public static byte deserializeByte(byte[] data) throws InvalidProtocolBufferException
    {
        ByteWrapper aByteWrapper = ByteWrapper.parseFrom(data);
        byte aValue = aByteWrapper.getValue().byteAt(0);
        return aValue;
    }
    
    public static byte[] serializeByteArray(byte[] data)
    {
        ByteWrapper.Builder aBuilder = ByteWrapper.newBuilder();
        if (data != null)
        {
            ByteString aBytes = ByteString.copyFrom(data);
            aBuilder.setValue(aBytes);
        }
        else
        {
            aBuilder.setValue(ByteString.EMPTY);
        }
        return aBuilder.build().toByteArray();
    }
    
    public static byte[] deserializeByteArray(byte[] data) throws InvalidProtocolBufferException
    {
        return ByteWrapper.parseFrom(data).getValue().toByteArray();
    }
    
   
    
    
    public static byte[] serializeShort(short data)
    {
        return serializeInt(data);
    }
    
    public static short deserializeShort(byte[] data) throws InvalidProtocolBufferException
    {
        short aValue = (short) deserializeInt(data);
        return aValue;
    }
    
    public static byte[] serializeShortArray(short[] data)
    {
        int aLength = (data != null) ? data.length : 0;
        int[] anInts = new int[aLength];
        for (int i = 0; i < anInts.length; ++i)
        {
            anInts[i] = data[i];
        }
        return serializeIntArray(anInts);
    }
    
    public static short[] deserializeShortArray(byte[] data) throws InvalidProtocolBufferException
    {
        int[] anInts = deserializeIntArray(data);
        short[] aShorts = new short[anInts.length];
        for (int i = 0; i < aShorts.length; ++i)
        {
            aShorts[i] = (short) anInts[i];
        }
        return aShorts;
    }
    
    
    
    public static byte[] serializeChar(char data)
    {
        int aValue = (int) data;
        return serializeInt(aValue);
    }
    
    public static char deserializeChar(byte[] data) throws InvalidProtocolBufferException
    {
        int aValue = deserializeInt(data);
        return (char) aValue;
    }
    
    public static byte[] serializeCharArray(char[] data)
    {
        int aLength = (data != null) ? data.length : 0;
        int[] anInts = new int[aLength];
        for (int i = 0; i < anInts.length; ++i)
        {
            anInts[i] = data[i];
        }
        return serializeIntArray(anInts);
    }
    
    public static char[] deserializeCharArray(byte[] data) throws InvalidProtocolBufferException
    {
        int[] anInts = deserializeIntArray(data);
        char[] aChars = new char[anInts.length];
        for (int i = 0; i < aChars.length; ++i)
        {
            aChars[i] = (char) anInts[i];
        }
        return aChars;
    }
    
    
    public static byte[] serializeInt(int data)
    {
        return IntWrapper.newBuilder().setValue(data).build().toByteArray();
    }
    
    public static int deserializeInt(byte[] data) throws InvalidProtocolBufferException
    {
        return IntWrapper.parseFrom(data).getValue();
    }
    
    public static byte[] serializeIntArray(int[] data)
    {
        IntArrayWrapper.Builder aBuilder = IntArrayWrapper.newBuilder();
        if (data != null)
        {
            for (int i : data)
            {
                aBuilder.addValue(i);
            }
        }
        else
        {
            aBuilder.addAllValue(Arrays.asList(myEmptyIntArray));
        }
        return aBuilder.build().toByteArray();
    }
    
    public static int[] deserializeIntArray(byte[] data) throws InvalidProtocolBufferException
    {
        IntArrayWrapper aWrapper = IntArrayWrapper.parseFrom(data);
        List<Integer> aList = aWrapper.getValueList();
        int[] anArray = new int[aList.size()];
        for (int i = 0; i < anArray.length; ++i)
        {
            anArray[i] = aList.get(i);
        }
        return anArray;
    }
    
    
    
    
    public static byte[] serializeLong(long data)
    {
        return LongWrapper.newBuilder().setValue(data).build().toByteArray();
    }
    
    public static long deserializeLong(byte[] data) throws InvalidProtocolBufferException
    {
        return LongWrapper.parseFrom(data).getValue();
    }
    
    public static byte[] serializeLongArray(long[] data)
    {
        LongArrayWrapper.Builder aBuilder = LongArrayWrapper.newBuilder();
        if (data != null)
        {
            for (long i : data)
            {
                aBuilder.addValue(i);
            }
        }
        else
        {
            aBuilder.addAllValue(Arrays.asList(myEmptyLongArray));
        }
        return aBuilder.build().toByteArray();
    }
    
    public static long[] deserializeLongArray(byte[] data) throws InvalidProtocolBufferException
    {
        LongArrayWrapper aWrapper = LongArrayWrapper.parseFrom(data);
        List<Long> aList = aWrapper.getValueList();
        long[] anArray = new long[aList.size()];
        for (int i = 0; i < anArray.length; ++i)
        {
            anArray[i] = aList.get(i);
        }
        return anArray;
    }
    
    
    public static byte[] serializeFloat(float data)
    {
        return FloatWrapper.newBuilder().setValue(data).build().toByteArray();
    }
    
    public static float deserializeFloat(byte[] data) throws InvalidProtocolBufferException
    {
        return FloatWrapper.parseFrom(data).getValue();
    }
    
    public static byte[] serializeFloatArray(float[] data)
    {
        FloatArrayWrapper.Builder aBuilder = FloatArrayWrapper.newBuilder();
        if (data != null)
        {
            for (float i : data)
            {
                aBuilder.addValue(i);
            }
        }
        else
        {
            aBuilder.addAllValue(Arrays.asList(myEmptyFloatArray));
        }
        return aBuilder.build().toByteArray();
    }
    
    public static float[] deserializeFloatArray(byte[] data) throws InvalidProtocolBufferException
    {
        FloatArrayWrapper aWrapper = FloatArrayWrapper.parseFrom(data);
        List<Float> aList = aWrapper.getValueList();
        float[] anArray = new float[aList.size()];
        for (int i = 0; i < anArray.length; ++i)
        {
            anArray[i] = aList.get(i);
        }
        return anArray;
    }
    
    
    
    public static byte[] serializeDouble(double data)
    {
        return DoubleWrapper.newBuilder().setValue(data).build().toByteArray();
    }
    
    public static double deserializeDouble(byte[] data) throws InvalidProtocolBufferException
    {
        return DoubleWrapper.parseFrom(data).getValue();
    }
    
    public static byte[] serializeDoubleArray(double[] data)
    {
        DoubleArrayWrapper.Builder aBuilder = DoubleArrayWrapper.newBuilder();
        if (data != null)
        {
            for (double i : data)
            {
                aBuilder.addValue(i);
            }
        }
        else
        {
            aBuilder.addAllValue(Arrays.asList(myEmptyDoubleArray));
        }
        return aBuilder.build().toByteArray();
    }
    
    public static double[] deserializeDoubleArray(byte[] data) throws InvalidProtocolBufferException
    {
        DoubleArrayWrapper aWrapper = DoubleArrayWrapper.parseFrom(data);
        List<Double> aList = aWrapper.getValueList();
        double[] anArray = new double[aList.size()];
        for (int i = 0; i < anArray.length; ++i)
        {
            anArray[i] = aList.get(i);
        }
        return anArray;
    }
    
    
    private static final String[] myEmptyStringArray = {};
    private static final Boolean[] myEmptyBoolArray = {};
    private static final Integer[] myEmptyIntArray = {};
    private static final Long[] myEmptyLongArray = {};
    private static final Float[] myEmptyFloatArray = {};
    private static final Double[] myEmptyDoubleArray = {};
}
