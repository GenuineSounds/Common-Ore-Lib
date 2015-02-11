package com.genuineflix.metal.api.data.collections;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

import net.minecraft.nbt.NBTTagIntArray;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.SizeLimit;
import com.genuineflix.metal.api.data.primitives.DataInteger;

public class DataArrayInteger extends Data<int[]> {

	public static final String NAME = "INT[]";
	public static final long SIZE = DataInteger.SIZE;
	public static final byte TYPE = 11;
	private int[] value;

	public DataArrayInteger() {}

	public DataArrayInteger(final int[] value) {
		this.value = value;
	}

	@Override
	public int[] value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(value.length);
		for (int i = 0; i < value.length; ++i)
			out.writeInt(value[i]);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		final int size = in.readInt();
		limit.assertLimit(DataArrayInteger.SIZE * size);
		value = new int[size];
		for (int i = 0; i < size; ++i)
			value[i] = in.readInt();
	}

	@Override
	public byte getTypeByte() {
		return DataArrayInteger.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(value.length * (int) DataArrayInteger.SIZE);
		sb.append('[');
		for (int i = 0; i < value.length; i++) {
			sb.append(value[i]);
			sb.append(',');
		}
		sb.append(']');
		return sb.toString();
	}

	@Override
	public Data<int[]> copy() {
		final int[] value = new int[this.value.length];
		System.arraycopy(this.value, 0, value, 0, this.value.length);
		return new DataArrayInteger(value);
	}

	@Override
	public NBTTagIntArray toNBT() {
		return new NBTTagIntArray(value);
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && Arrays.equals(value, ((DataArrayInteger) obj).value);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(value);
	}
}