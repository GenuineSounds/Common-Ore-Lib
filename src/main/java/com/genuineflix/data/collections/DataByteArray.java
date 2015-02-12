package com.genuineflix.data.collections;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

import net.minecraft.nbt.NBTTagByteArray;

import com.genuineflix.data.AbstractData;
import com.genuineflix.data.IData;
import com.genuineflix.data.IDataPrimitiveArray;
import com.genuineflix.data.SizeLimit;
import com.genuineflix.data.primitives.DataByte;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DataByteArray extends AbstractData<byte[]> implements IDataPrimitiveArray {

	public static final String NAME = "BYTE[]";
	public static final long SIZE = DataByte.SIZE;
	public static final byte TYPE = 7;
	private byte[] value;

	public DataByteArray() {}

	public DataByteArray(final byte[] value) {
		this.value = value;
	}

	@Override
	public byte[] value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(value.length);
		out.write(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		final int size = in.readInt();
		limit.assertLimit(DataByteArray.SIZE * size);
		value = new byte[size];
		in.readFully(value);
	}

	@Override
	public byte getTypeByte() {
		return DataByteArray.TYPE;
	}

	@Override
	public String getTypeName() {
		return DataByteArray.NAME;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(value.length * (int) SIZE);
		sb.append('[');
		for (int i = 0; i < value.length; i++)
			sb.append(String.format("%02X ", value[i]));
		if (sb.indexOf(" ") > 0)
			sb.deleteCharAt(sb.length() - 1);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public DataByteArray copy() {
		final byte[] value = new byte[this.value.length];
		System.arraycopy(this.value, 0, value, 0, this.value.length);
		return new DataByteArray(value);
	}

	@Override
	public NBTTagByteArray toNBT() {
		return new NBTTagByteArray(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof IDataPrimitiveArray)
			return Arrays.equals(value(), ((IDataPrimitiveArray) obj).toByteArray());
		return obj instanceof byte[] && Arrays.equals(value(), (byte[]) obj);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}

	@Override
	public JsonArray serialize(final IData<byte[]> src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonArray array = new JsonArray();
		for (final byte b : src.value())
			array.add(new JsonPrimitive(b));
		return array;
	}

	@Override
	public DataByteArray deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		try {
			final JsonArray array = json.getAsJsonArray();
			final byte[] bytes = new byte[array.size()];
			for (int i = 0; i < bytes.length; i++)
				bytes[i] = array.get(i).getAsByte();
			return new DataByteArray(bytes);
		}
		catch (final Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public byte[] toByteArray() {
		return value;
	}

	@Override
	public int[] toIntArray() {
		final int[] array = new int[value.length];
		for (int i = 0; i < array.length; i++)
			array[i] = value[i];
		return array;
	}
}