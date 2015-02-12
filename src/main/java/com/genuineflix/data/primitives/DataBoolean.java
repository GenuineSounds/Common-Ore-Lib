package com.genuineflix.data.primitives;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import net.minecraft.nbt.NBTTagByte;

import com.genuineflix.data.AbstractData;
import com.genuineflix.data.IData;
import com.genuineflix.data.IDataPrimitive;
import com.genuineflix.data.SizeLimit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DataBoolean extends AbstractData<Boolean> implements IDataPrimitive {

	public static final String NAME = "BOOLEAN";
	public static final long SIZE = 1;
	public static final byte TYPE = 12;
	private boolean value;

	public DataBoolean() {}

	public DataBoolean(final boolean value) {
		this.value = value;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public byte getTypeByte() {
		return TYPE;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeBoolean(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		limit.assertLimit(SIZE);
		value = in.readBoolean();
	}

	@Override
	public DataBoolean copy() {
		return new DataBoolean(value);
	}

	@Override
	public NBTTagByte toNBT() {
		return new NBTTagByte(value ? (byte) 1 : (byte) 0);
	}

	@Override
	public Boolean value() {
		return value;
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && value == ((DataBoolean) obj).value;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ toByte();
	}

	@Override
	public boolean toBoolean() {
		return value;
	}

	@Override
	public long toLong() {
		return value ? 1 : 0;
	}

	@Override
	public int toInt() {
		return value ? 1 : 0;
	}

	@Override
	public short toShort() {
		return (short) (value ? 1 : 0);
	}

	@Override
	public byte toByte() {
		return (byte) (value ? 1 : 0);
	}

	@Override
	public double toDouble() {
		return value ? 1 : 0;
	}

	@Override
	public float toFloat() {
		return value ? 1 : 0;
	}

	@Override
	public JsonPrimitive serialize(final IData<Boolean> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataBoolean deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return new DataBoolean(json.getAsBoolean());
	}
}
