package com.genuineflix.data.primitives;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import net.minecraft.nbt.NBTTagShort;

import com.genuineflix.data.AbstractData;
import com.genuineflix.data.IData;
import com.genuineflix.data.IDataPrimitive;
import com.genuineflix.data.SizeLimit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DataShort extends AbstractData<Short> implements IDataPrimitive {

	public static final String NAME = "SHORT";
	public static final long SIZE = 16;
	public static final byte TYPE = 2;
	private short value;

	public DataShort() {}

	public DataShort(final short value) {
		this.value = value;
	}

	@Override
	public Short value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeShort(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		limit.assertLimit(DataShort.SIZE);
		value = in.readShort();
	}

	@Override
	public byte getTypeByte() {
		return DataShort.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		return Short.toString(value);
	}

	@Override
	public DataShort copy() {
		return new DataShort(value);
	}

	@Override
	public NBTTagShort toNBT() {
		return new NBTTagShort(value);
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && value == ((DataShort) obj).value;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ value;
	}

	@Override
	public boolean toBoolean() {
		return toByte() != 0;
	}

	@Override
	public long toLong() {
		return value;
	}

	@Override
	public int toInt() {
		return value;
	}

	@Override
	public short toShort() {
		return value;
	}

	@Override
	public byte toByte() {
		return (byte) (value & 255);
	}

	@Override
	public double toDouble() {
		return value;
	}

	@Override
	public float toFloat() {
		return value;
	}

	@Override
	public JsonPrimitive serialize(final IData<Short> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataShort deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return new DataShort(json.getAsShort());
	}
}