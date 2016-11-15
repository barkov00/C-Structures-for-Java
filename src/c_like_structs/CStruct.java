package c_like_structs;

import java.nio.ByteOrder;

public class CStruct {
		
	public CStruct(){
		
	}
	
	public CStruct(byte[] array, ByteOrder byteOrder) throws IllegalArgumentException, IllegalAccessException{
		fill(array, byteOrder);
	}
	
	public void fill(byte[] array, ByteOrder byteOrder) throws IllegalArgumentException, IllegalAccessException{
		CStructUtils.fill(this, array, byteOrder);
	}
	
	@Override
	public String toString(){
		return CStructUtils.printStruct(this);
	}
	
	public byte[] getBytes(ByteOrder byteOrder) throws IllegalArgumentException, IllegalAccessException{
		return CStructUtils.getBytes(this, byteOrder);
	}
	
}
