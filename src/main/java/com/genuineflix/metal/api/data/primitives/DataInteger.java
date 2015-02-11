package com.genuineflix.metal.api.data.primitives;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagInt;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.IDataPrimitive;
import com.genuineflix.metal.api.data.SizeLimit;

public class DataInteger extends Data<Integer> implements IDataPrimitive {

	public static final String NAME = "INT";
	public static final long SIZE = 32;
	public static final byte TYPE = 3;
	private int value;

	public DataInteger() {}

	public DataInteger(final int value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		limit.assertLimit(DataInteger.SIZE);
		value = in.readInt();
	}

	@Override
	public byte getTypeByte() {
		return DataInteger.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		return Integer.toString(value, 10);
	}

	@Override
	public Data<Integer> copy() {
		return new DataInteger(value);
	}

	@Override
	public NBTTagInt toNBT() {
		return new NBTTagInt(value);
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && value == ((DataInteger) obj).value;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ value;
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
		return (short) (value & 65535);
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
}