package com.genuinevm.data.collection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

import org.apache.commons.lang3.StringEscapeUtils;

import com.genuinevm.data.AbstractData;
import com.genuinevm.data.Data;
import com.genuinevm.data.Primitive;
import com.genuinevm.data.PrimitiveArray;
import com.genuinevm.data.array.DataByteArray;
import com.genuinevm.data.array.DataIntegerArray;
import com.genuinevm.data.primitive.DataBoolean;
import com.genuinevm.data.primitive.DataByte;
import com.genuinevm.data.primitive.DataDouble;
import com.genuinevm.data.primitive.DataFloat;
import com.genuinevm.data.primitive.DataInteger;
import com.genuinevm.data.primitive.DataLong;
import com.genuinevm.data.primitive.DataShort;
import com.genuinevm.data.primitive.DataString;
import com.genuinevm.data.util.Serialization;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class DataCompound extends AbstractData<Map<String, AbstractData>> {

	public static final String NAME = "COMPOUND";
	public static final long SIZE = 16;
	public static final byte TYPE = 10;
	private Map<String, AbstractData> values = new HashMap<String, AbstractData>();

	public DataCompound() {}

	public DataCompound(final Map<String, AbstractData> values) {
		this.values = values;
	}

	@Override
	public Map<String, AbstractData> value() {
		return values;
	}

	@Override
	public void write(final DataOutput output) throws IOException {
		for (final String name : values.keySet()) {
			final AbstractData data = values.get(name);
			output.writeByte(data.getTypeByte());
			if (data.getTypeByte() != 0) {
				output.writeUTF(name);
				data.write(output);
			}
		}
		output.writeByte(0);
	}

	@Override
	public void read(final DataInput input) throws IOException {
		values.clear();
		byte type;
		while ((type = input.readByte()) != 0) {
			final String name = input.readUTF();
			final AbstractData data = AbstractData.create(type);
			data.read(input);
			values.put(name, data);
		}
	}

	public Set<String> getEntryNames() {
		return values.keySet();
	}

	@Override
	public byte getTypeByte() {
		return DataCompound.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	public void set(final String name, final AbstractData value) {
		if (this != value)
			values.put(name, value);
	}

	public void set(final String name, final boolean value) {
		values.put(name, new DataBoolean(value));
	}

	public void set(final String name, final byte value) {
		values.put(name, new DataByte(value));
	}

	public void set(final String name, final short value) {
		values.put(name, new DataShort(value));
	}

	public void set(final String name, final int value) {
		values.put(name, new DataInteger(value));
	}

	public void set(final String name, final long value) {
		values.put(name, new DataLong(value));
	}

	public void set(final String name, final float value) {
		values.put(name, new DataFloat(value));
	}

	public void set(final String name, final double value) {
		values.put(name, new DataDouble(value));
	}

	public void set(final String name, final String value) {
		values.put(name, new DataString(value));
	}

	public void set(final String name, final byte[] value) {
		values.put(name, new DataByteArray(value));
	}

	public void set(final String name, final int[] value) {
		values.put(name, new DataIntegerArray(value));
	}

	public void set(final String name, final BigInteger value) {
		values.put(name, new DataByteArray(value.toByteArray()));
	}

	public void set(final String name, final BigDecimal value) {
		values.put(name, new DataString(value.toPlainString()));
	}

	public byte getType(final String name) {
		final AbstractData nbtbase = values.get(name);
		return nbtbase != null ? nbtbase.getTypeByte() : 0;
	}

	public boolean hasKey(final String name) {
		return values.containsKey(name);
	}

	public boolean hasKey(final String name, final int withType) {
		final byte type = getType(name);
		return type == withType ? true : withType != 99 ? false : type == 1 || type == 2 || type == 3 || type == 4 || type == 5 || type == 6;
	}

	public AbstractData getData(final String name) {
		return values.get(name);
	}

	public boolean getBoolean(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toBoolean() : false;
		}
		catch (final ClassCastException e) {
			return false;
		}
	}

	public byte getByte(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toByte() : 0;
		}
		catch (final ClassCastException e) {
			return (byte) 0;
		}
	}

	public short getShort(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toShort() : 0;
		}
		catch (final ClassCastException e) {
			return (short) 0;
		}
	}

	public int getInteger(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toInt() : 0;
		}
		catch (final ClassCastException classcastexception) {
			return 0;
		}
	}

	public long getLong(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toLong() : 0;
		}
		catch (final ClassCastException e) {
			return 0;
		}
	}

	public float getFloat(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toFloat() : 0;
		}
		catch (final ClassCastException classcastexception) {
			return 0;
		}
	}

	public double getDouble(final String name) {
		try {
			return values.containsKey(name) ? ((Primitive) values.get(name)).toDouble() : 0;
		}
		catch (final ClassCastException classcastexception) {
			return 0;
		}
	}

	public byte[] getByteArray(final String name) {
		try {
			return values.containsKey(name) ? ((PrimitiveArray) values.get(name)).toByteArray() : new byte[0];
		}
		catch (final ClassCastException e) {
			return new byte[0];
		}
	}

	public int[] getIntArray(final String name) {
		try {
			return values.containsKey(name) ? ((PrimitiveArray) values.get(name)).toIntArray() : new int[0];
		}
		catch (final ClassCastException e) {
			return new int[0];
		}
	}

	public String getString(final String name) {
		try {
			return values.containsKey(name) ? ((DataString) values.get(name)).value() : "";
		}
		catch (final ClassCastException classcastexception) {
			return "";
		}
	}

	public BigInteger getBigInteger(final String name) {
		try {
			return values.containsKey(name) ? new BigInteger(((DataByteArray) values.get(name)).value()) : new BigInteger("0");
		}
		catch (final ClassCastException e) {
			return new BigInteger("0");
		}
	}

	public BigDecimal getBigDecimal(final String name) {
		try {
			return values.containsKey(name) ? new BigDecimal(((DataString) values.get(name)).value()) : new BigDecimal("0");
		}
		catch (final ClassCastException e) {
			return new BigDecimal("0");
		}
	}

	public DataCompound getCompound(final String name) {
		try {
			return values.containsKey(name) ? (DataCompound) values.get(name) : new DataCompound();
		}
		catch (final ClassCastException e) {
			return new DataCompound();
		}
	}

	public DataList getList(final String name, final int ofType) {
		try {
			if (getType(name) != DataList.TYPE)
				return new DataList();
			else {
				final DataList list = (DataList) values.get(name);
				return list.size() > 0 && list.getListType() == ofType ? list : new DataList();
			}
		}
		catch (final ClassCastException e) {
			return new DataList();
		}
	}

	public void remove(final String name) {
		values.remove(name);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		for (final String str : values.keySet()) {
			sb.append('"');
			sb.append(StringEscapeUtils.escapeJson(str));
			sb.append("\": ");
			sb.append(values.get(str));
			sb.append(", ");
		}
		if (sb.indexOf(", ") > 0) {
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(" }");
		if (sb.indexOf("  ") > 0) {
			sb.deleteCharAt(sb.length() - 2);
			sb.deleteCharAt(sb.length() - 2);
		}
		return sb.toString();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public DataCompound copy() {
		final DataCompound compound = new DataCompound();
		for (final String key : values.keySet())
			compound.set(key, (AbstractData) values.get(key).copy());
		return compound;
	}

	@Override
	public NBTTagCompound toNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
		for (final String key : values.keySet())
			compound.setTag(key, values.get(key).toNBT());
		return compound;
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Data)
			return value().equals(((Data) obj).value());
		return obj instanceof Map && value().entrySet().equals(((Map) obj).entrySet());
	}

	@Override
	public int hashCode() {
		return values.hashCode();
	}

	@Override
	public JsonObject serialize(final AbstractData<Map<String, AbstractData>> src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonObject object = new JsonObject();
		for (final Entry<String, AbstractData> entry : src.value().entrySet())
			object.add(entry.getKey(), entry.getValue().serialize(entry.getValue(), entry.getValue().getClass(), context));
		return object;
	}

	@Override
	public DataCompound deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		final Map<String, AbstractData> map = new HashMap<String, AbstractData>();
		try {
			for (final Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
				map.put(entry.getKey(), Serialization.serializedElement(entry.getValue(), entry.getValue().getClass(), context));
			return new DataCompound(map);
		}
		catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}
}