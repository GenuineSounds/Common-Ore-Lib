package com.genuineflix.metal.api.data.primitives;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagString;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.SizeLimit;

public class DataString extends Data<String> {

	public static final String NAME = "STRING";
	public static final long SIZE = 8;
	public static final byte TYPE = 8;
	private String value = "";

	public DataString() {}

	public DataString(final String value) {
		this.value = value;
		if (value == null)
			throw new IllegalArgumentException("Null string not allowed");
	}

	@Override
	public String value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeUTF(value);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		value = in.readUTF();
		limit.assertLimit(DataString.SIZE * value.length());
	}

	@Override
	public byte getTypeByte() {
		return DataString.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		return "\"" + value + "\"";
	}

	@Override
	public Data<String> copy() {
		return new DataString(value);
	}

	@Override
	public NBTTagString toNBT() {
		return new NBTTagString(value);
	}

	@Override
	public boolean equals(final Object p_equals_1_) {
		if (!super.equals(p_equals_1_))
			return false;
		else {
			final DataString nbttagstring = (DataString) p_equals_1_;
			return value == null && nbttagstring.value == null || value != null && value.equals(nbttagstring.value);
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ value.hashCode();
	}

	@Override
	public String directString() {
		return value;
	}
}