package com.genuineflix.metal.api.data.primitives;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagByte;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.IDataPrimitive;
import com.genuineflix.metal.api.data.SizeLimit;

public class DataByte extends Data<Byte> implements IDataPrimitive {

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
		return Byte.toString(value) + "b";
	}

	@Override
	public Data<Byte> copy() {
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
}