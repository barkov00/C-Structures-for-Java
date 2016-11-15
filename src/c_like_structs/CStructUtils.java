package c_like_structs;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class CStructUtils {
		
	public static void fill(Object struct, byte[] array, ByteOrder byteOrder) throws IllegalArgumentException, IllegalAccessException{
		if(byteOrder == null) return;
		if(array == null) return;
		
		int index = 0;
		int length = array.length;
		
		@SuppressWarnings("rawtypes")
		Class clazz = struct.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		byte[] temp = {0, 0, 0, 0, 0, 0, 0, 0}; //8 bytes
		
		for(int i = 0; i < fields.length; i++){
			Field f = fields[i];
			
			CType type = f.getAnnotation(CType.class);
			if(type == null) continue;
			String t = type.value();
			
			set(temp, (byte)0);
			
			if("int8".equals(t)) {
				if(index + 1 > length) break;
				f.setByte(struct, array[index++]);
			}
			
			if("uint8".equals(t)) {
				if(index + 1 > length) break;
				f.setInt(struct, (int)(array[index++] & 0xFF));
			}
			
			if("uint16".equals(t) || "int16".equals(t)){
				if(index + 2 > length) break;
				System.arraycopy(array, index, temp, 6, 2);
				index += 2;
				if(byteOrder == ByteOrder.LITTLE_ENDIAN) reverse(temp, 6, 2);
				f.setInt(struct, ByteBuffer.wrap(temp, 4, 4).getInt());
			}
			
			if("uint32".equals(t) || "int32".equals(t)){
				if(index + 4 > length) break;
				System.arraycopy(array, index, temp, 4, 4);
				index += 4;
				if(byteOrder == ByteOrder.LITTLE_ENDIAN) reverse(temp, 4, 4);
				f.setLong(struct, ByteBuffer.wrap(temp, 0, 8).getLong());
			}
			
			if("float".equals(t)){
				if(index + 4 > length) break;
				System.arraycopy(array, index, temp, 4, 4);
				index += 4;
				if(byteOrder == ByteOrder.LITTLE_ENDIAN) reverse(temp, 4, 4);
				f.setFloat(struct, ByteBuffer.wrap(temp, 4, 4).getFloat());
			}
			
			if("double".equals(t)){
				if(index + 8 > length) break;
				System.arraycopy(array, index, temp, 0, 8);
				index += 8;
				if(byteOrder == ByteOrder.LITTLE_ENDIAN) reverse(temp, 0, 8);
				f.setDouble(struct, ByteBuffer.wrap(temp, 0, 8).getDouble());
			}
		}
	}
	
	public static String printStruct(Object struct){
		@SuppressWarnings("rawtypes")
		Class clazz = struct.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(clazz.getSimpleName() + ":\n");
		
		try{
			for(int i = 0; i < fields.length; i++){
				Field f = fields[i];
				CType type = f.getAnnotation(CType.class);
				if(type == null) continue;
				String t = type.value();
				if("int8".equals(t)){
					sb.append(t + " " + f.getName() + " = " + f.getByte(struct));
				}
				
				if("uint8".equals(t)){
					sb.append(t + " " + f.getName() + " = " + f.getInt(struct));
				}
				
				if("uint16".equals(t) || "int16".equals(t)){
					sb.append(t + " " + f.getName() + " = " + f.getInt(struct));
				}
				
				if("uint32".equals(t) || "int32".equals(t)){
					sb.append(t + " " + f.getName() + " = " + f.getLong(struct));
				}
				
				if("float".equals(t)){
					sb.append(t + " " + f.getName() + " = " + f.getFloat(struct));
				}
				
				if("double".equals(t)){
					sb.append(t + " " + f.getName() + " = " + f.getDouble(struct));
				}
				
				sb.append("\n");
			}
		} catch (Exception exc){
			
		}
		
		return sb.toString();
	}
	
	public static byte[] getBytes(Object struct, ByteOrder byteOrder) throws IllegalArgumentException, IllegalAccessException{
		List<Byte> bytes = new ArrayList<Byte>();
		
		@SuppressWarnings("rawtypes")
		Class clazz = struct.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		for(int i = 0; i < fields.length; i++){
			Field f = fields[i];
			CType type = f.getAnnotation(CType.class);
			if(type == null) continue;
			String t = type.value();
			
			if("int8".equals(t)) {
				bytes.add(f.getByte(struct));
			}
			
			if("uint8".equals(t)){
				bytes.add( (byte)(f.getInt(struct) & 0xFF) );
			}
			
			if("uint16".equals(t) || "int16".equals(t)){
				byte[] arr = ByteBuffer.allocate(4).putInt(f.getInt(struct)).array();
				if(byteOrder == ByteOrder.LITTLE_ENDIAN){
					bytes.add(arr[3]);
					bytes.add(arr[2]);
				} else {
					bytes.add(arr[2]);
					bytes.add(arr[3]);
				}
			}
			
			if("uint32".equals(t) || "int32".equals(t)){
				byte[] arr = ByteBuffer.allocate(4).order(byteOrder).putInt((int)f.getLong(struct)).array();
				for(byte b : arr){
					bytes.add(b);
				}
			}
			
			if("float".equals(t)){
				byte[] arr = ByteBuffer.allocate(4).order(byteOrder).putFloat(f.getFloat(struct)).array();
				for(byte b : arr){
					bytes.add(b);
				}
			}
			
			if("double".equals(t)){
				byte[] arr = ByteBuffer.allocate(8).order(byteOrder).putDouble(f.getDouble(struct)).array();
				for(byte b : arr){
					bytes.add(b);
				}
			}
		}
		
		byte[] arr = new byte[bytes.size()];
		for(int i = 0; i < arr.length; i++) arr[i] = bytes.get(i);
		return arr;
	}
	
	static private byte[] set(byte[] array, byte value){
		for(int i = 0; i < array.length; i++)
			array[i] = value;
		return array;
	}
	
	static private byte[] reverse(byte[] array, int offset, int count) {
        if (array == null) return null;
        byte tmp = 0;
        for(int i = 0; i < count / 2; i++){
        	tmp = array[offset + i];
        	array[offset + i] = array[offset + count - i - 1];
        	array[offset + count - i - 1] = tmp;
        }
        return array;
    }

}
