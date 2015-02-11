package com.genuineflix.metal.api.data.collections;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

import net.minecraft.nbt.NBTTagByteArray;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.SizeLimit;
import com.genuineflix.metal.api.data.primitives.DataByte;

public class DataArrayByte extends Data<byte[]> {

	public static final String NAME = "BYTE[]";
	public static final long SIZE = DataByte.SIZE;
	public static final byte TYPE = 7;
	private byte[] value;

	public DataArrayByte() {}

	public DataArrayByte(final byte[] value) {
		this.value = value;
	}

	@Override
	public byte[] value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(value.length);
		out.write(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		final int size = in.readInt();
		limit.assertLimit(DataArrayByte.SIZE * size);
		value = new byte[size];
		in.readFully(value);
	}

	@Override
	public byte getTypeByte() {
		return DataArrayByte.TYPE;
	}

	@Override
	public String getTypeName() {
		return DataArrayByte.NAME;
	}

	@Override
	public String toString() {
		return "[" + value.length + " bytes]";
	}

	@Override
	public Data<byte[]> copy() {
		final byte[] value = new byte[this.value.length];
		System.arraycopy(this.value, 0, value, 0, this.value.length);
		return new DataArrayByte(value);
	}

	@Override
	public NBTTagByteArray toNBT() {
		return new NBTTagByteArray(value);
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && Arrays.equals(value, ((DataArrayByte) obj).value);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(value);
	}
}