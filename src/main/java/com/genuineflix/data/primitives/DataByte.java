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

public class DataByte extends AbstractData<Byte> implements IDataPrimitive {

	public static final String NAME = "BYTE";
	public static final long SIZE = 8;
	public static final byte TYPE = 1;
	private byte value;

	public DataByte() {}

	public DataByte(final byte value) {
		this.value = value;
	}

	@Override
	public Byte value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeByte(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		limit.assertLimit(DataByte.SIZE);
		value = in.readByte();
	}

	@Override
	public byte getTypeByte() {
		return DataByte.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		return Byte.toString(value);
	}

	@Override
	public DataByte copy() {
		return new DataByte(value);
	}

	@Override
	public NBTTagByte toNBT() {
		return new NBTTagByte(value);
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && value == ((DataByte) obj).value;
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
		return value;
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
	public JsonPrimitive serialize(final IData<Byte> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataByte deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return new DataByte(json.getAsByte());
	}
}