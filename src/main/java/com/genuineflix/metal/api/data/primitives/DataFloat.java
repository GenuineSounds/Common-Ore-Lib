package com.genuineflix.metal.api.data.primitives;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.MathHelper;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.IDataPrimitive;
import com.genuineflix.metal.api.data.SizeLimit;

public class DataFloat extends Data<Float> implements IDataPrimitive {

	public static final String NAME = "FLOAT";
	public static final long SIZE = 32;
	public static final byte TYPE = 5;
	private float value;

	public DataFloat() {}

	public DataFloat(final float value) {
		this.value = value;
	}

	@Override
	public Float value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeFloat(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		limit.assertLimit(DataFloat.SIZE);
		value = in.readFloat();
	}

	@Override
	public byte getTypeByte() {
		return DataFloat.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		return "" + value + "f";
	}

	@Override
	public Data<Float> copy() {
		return new DataFloat(value);
	}

	@Override
	public NBTTagFloat toNBT() {
		return new NBTTagFloat(value);
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && value == ((DataFloat) obj).value;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Float.floatToIntBits(value);
	}

	@Override
	public long toLong() {
		return (long) value;
	}

	@Override
	public int toInt() {
		return MathHelper.floor_float(value);
	}

	@Override
	public short toShort() {
		return (short) (MathHelper.floor_float(value) & 65535);
	}

	@Override
	public byte toByte() {
		return (byte) (MathHelper.floor_float(value) & 255);
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