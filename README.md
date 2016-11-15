# C-Structures-for-Java
**Serialize/deserialize structures on Java in C style. Just for fun.**

**Usage:**

**Deserialization**

1) Create structure class:
```
public static class MyCStruct extends CStruct {
		@CType("int8")		public byte field1;
		@CType("uint8") 	public int field2;
		@CType("uint16") 	public int uint16_field;
		@CType("uint32") 	public long uint32_field;
		@CType("double") 	public double double_field;
		@CType("float") 	public float float_field;
}
```
2) Fill it from byte array:

```
byte[] array = receiveData();
MyCStruct st_big_endian = new MyCStruct();
st_big_endian.fill(array, ByteOrder.BIG_ENDIAN);
```

3) Print it to console:
```
System.out.println(st_big_endian.toString());
```

**Serialization**

```byte[] array = st_big_endian.getBytes(ByteOrder.BIG_ENDIAN);```

**Follow simple rules:**
```
C type                         Java type

int8                  ===>       byte
uint8, uint16, int16  ===>       int
int32, uint32         ===>       long
double                ===>       double
float                 ===>       float

```
