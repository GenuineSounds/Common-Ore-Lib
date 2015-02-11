package com.genuineflix.metal.api.data.primitives;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagShort;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.IDataPrimitive;
import com.genuineflix.metal.api.data.SizeLimit;

public class DataShort extends Data<Short> implements IDataPrimitive {

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
		return "" + value + "s";
	}

	@Override
	public Data<Short> copy() {
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
}