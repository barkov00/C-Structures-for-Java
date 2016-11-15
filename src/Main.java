

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import c_like_structs.CStruct;
import c_like_structs.CStructUtils;
import c_like_structs.CType;

public class Main {

	public static class MyCStruct extends CStruct {
		@CType("int8")		public byte field1;
		@CType("uint8") 	public int field2;
		@CType("uint16") 	public int uint16_field;
		@CType("uint32") 	public long uint32_field;
		@CType("double") 	public double double_field;
		@CType("float") 	public float float_field;
	}
	
	public static class MyStruct {
		@CType("int8")		public byte field1;
		@CType("uint8") 	public int field2;
		@CType("uint16") 	public int uint16_field;
		@CType("uint32") 	public long uint32_field;
		@CType("double") 	public double double_field;
		@CType("float") 	public float float_field;
	}
	
	public static void main(String... args){
		
		MyCStruct st_big_endian = new MyCStruct();
		MyCStruct st_little_endian = new MyCStruct();
		MyStruct st_le = new MyStruct();
		
		byte[] test_big_endian = createTestData(false);
		byte[] test_little_endian = createTestData(true);
		
		byte[] ser_big_endian = null;
		byte[] ser_little_endian = null;
		
		try{
			
			CStructUtils.fill(st_le, test_little_endian, ByteOrder.LITTLE_ENDIAN);
			
			st_big_endian.fill(test_big_endian, ByteOrder.BIG_ENDIAN);
			st_little_endian.fill(test_little_endian, ByteOrder.LITTLE_ENDIAN);
			
			ser_big_endian = st_big_endian.getBytes(ByteOrder.BIG_ENDIAN);
			ser_little_endian = st_little_endian.getBytes(ByteOrder.LITTLE_ENDIAN);
			
			st_big_endian.fill(ser_big_endian,  ByteOrder.BIG_ENDIAN);
			st_little_endian.fill(ser_little_endian, ByteOrder.LITTLE_ENDIAN);
			
		} catch(Exception exc){
			System.out.println(exc.getMessage());
		}
		
		System.out.println("\nser_big_endian size = " + ser_big_endian.length);
		System.out.println("\nser_little_endian size = " + ser_little_endian.length);
		
		System.out.println("\r\n" + CStructUtils.printStruct(st_le) );
		
		System.out.println("\r\nbig_endian:\r\n"    + st_big_endian.toString() + "\r\n");
		System.out.println("little_endian:\r\n" + st_little_endian.toString());
		
	}
	
	public static byte[] createTestData(boolean little_endian){
		byte[] arr = {0x01, (byte)0xFF, (byte) 0x0f, (byte) 0xff,	 	(byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff,			0,0,0,0,0,0,0,0, 	0,0,0,0	};
		
		byte[] dbl = new byte[8];
		ByteBuffer.wrap(dbl).putDouble(256.76d);
		
		byte[] flt = new byte[4];
		ByteBuffer.wrap(flt).putFloat(125.56f);
		
		
		if(little_endian){
			flt = reverse(flt, 0, 4);
			dbl = reverse(dbl, 0, 8);
			//uint16
			arr[2] = (byte) 0xFF;
			arr[3] = (byte) 0x0F;
			//uint32
			arr[4] = (byte) 0xFF;
			arr[5] = (byte) 0xFF; 
			arr[6] = (byte) 0x00;
			arr[7] = (byte) 0x00; 
		} 
		
		System.arraycopy(dbl,  0, arr, 8, 8);
		System.arraycopy(flt,  0, arr, 16, 4);

		
		return arr;
	}
	
	public static byte[] reverse(byte[] array, int offset, int count) {
        if (array == null) {
            return null;
        }
        byte tmp = 0;
        for(int i = 0; i < count / 2; i++){
        	tmp = array[offset + i];
        	array[offset + i] = array[offset + count - i - 1];
        	array[offset + count - i - 1] = tmp;
        }
        return array;
    }
	
}
