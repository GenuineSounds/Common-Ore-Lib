package com.genuineflix.metal.api.data.primitives;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagLong;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.IDataPrimitive;
import com.genuineflix.metal.api.data.SizeLimit;

public class DataLong extends Data<Long> implements IDataPrimitive {

	public static final String NAME = "LONG";
	public static final long SIZE = 64;
	public static final byte TYPE = 4;
	private long value;

	public DataLong() {}

	@Override
	public Long value() {
		return value;
	}

	public DataLong(final long value) {
		this.value = value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeLong(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		limit.assertLimit(DataLong.SIZE);
		value = in.readLong();
	}

	@Override
	public byte getTypeByte() {
		return DataLong.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		return "" + value + "L";
	}

	@Override
	public Data<Long> copy() {
		return new DataLong(value);
	}

	@Override
	public NBTTagLong toNBT() {
		return new NBTTagLong(value);
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && value == ((DataLong) obj).value;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ (int) (value ^ value >>> 32);
	}

	@Override
	public long toLong() {
		return value;
	}

	@Override
	public int toInt() {
		return (int) (value & -1L);
	}

	@Override
	public short toShort() {
		return (short) (int) (value & 65535L);
	}

	@Override
	public byte toByte() {
		return (byte) (int) (value & 255L);
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