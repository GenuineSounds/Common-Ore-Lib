package com.genuineflix.metal.api.data.primitives;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.MathHelper;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.IDataPrimitive;
import com.genuineflix.metal.api.data.SizeLimit;

public class DataDouble extends Data<Double> implements IDataPrimitive {

	public static final String NAME = "DOUBLE";
	public static final long SIZE = 64;
	public static final byte TYPE = 6;
	private double value;

	public DataDouble() {}

	public DataDouble(final double value) {
		this.value = value;
	}

	@Override
	public Double value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeDouble(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		limit.assertLimit(DataDouble.SIZE);
		value = in.readDouble();
	}

	@Override
	public byte getTypeByte() {
		return DataDouble.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		return "" + value + "d";
	}

	@Override
	public Data<Double> copy() {
		return new DataDouble(value);
	}

	@Override
	public NBTTagDouble toNBT() {
		return new NBTTagDouble(value);
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && value == ((DataDouble) obj).value;
	}

	@Override
	public int hashCode() {
		final long i = Double.doubleToLongBits(value);
		return super.hashCode() ^ (int) (i ^ i >>> 32);
	}

	@Override
	public long toLong() {
		return (long) Math.floor(value);
	}

	@Override
	public int toInt() {
		return MathHelper.floor_double(value);
	}

	@Override
	public short toShort() {
		return (short) (MathHelper.floor_double(value) & 65535);
	}

	@Override
	public byte toByte() {
		return (byte) (MathHelper.floor_double(value) & 255);
	}

	@Override
	public double toDouble() {
		return value;
	}

	@Override
	public float toFloat() {
		return (float) value;
	}
}