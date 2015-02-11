package com.genuineflix.metal.api.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagEnd;

import com.genuineflix.metal.api.data.collections.DataArrayByte;
import com.genuineflix.metal.api.data.collections.DataArrayInteger;
import com.genuineflix.metal.api.data.collections.DataCompound;
import com.genuineflix.metal.api.data.collections.DataList;
import com.genuineflix.metal.api.data.primitives.DataByte;
import com.genuineflix.metal.api.data.primitives.DataDouble;
import com.genuineflix.metal.api.data.primitives.DataFloat;
import com.genuineflix.metal.api.data.primitives.DataInteger;
import com.genuineflix.metal.api.data.primitives.DataLong;
import com.genuineflix.metal.api.data.primitives.DataShort;
import com.genuineflix.metal.api.data.primitives.DataString;

public abstract class Data<T> implements IData<T> {

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof Data && getTypeByte() == ((Data) obj).getTypeByte();
	}

	@Override
	public int hashCode() {
		return getTypeByte();
	}

	protected String directString() {
		return toString();
	}

	public static final Data[] TYPES = new Data[] {
			new DataEnd(), new DataByte(), new DataShort(), new DataInteger(),
			//
			new DataLong(), new DataFloat(), new DataDouble(), new DataArrayByte(),
			//
			new DataString(), new DataList(), new DataCompound(), new DataArrayInteger()
	};

	protected static Data<?> create(final byte type) {
		if (type < TYPES.length)
			return TYPES[type].copy();
		return null;
	}

	public static class DataEnd extends Data<Void> {

		public static final String NAME = "END";
		public static final byte TYPE = 0;
		public static final long SIZE = 0;

		@Override
		public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {}

		@Override
		public void write(final DataOutput out) throws IOException {}

		@Override
		public byte getTypeByte() {
			return DataEnd.TYPE;
		}

		@Override
		public String getTypeName() {
			return NAME;
		}

		@Override
		public String toString() {
			return NAME;
		}

		@Override
		public Data<Void> copy() {
			return new DataEnd();
		}

		@Override
		public NBTTagEnd toNBT() {
			return new NBTTagEnd();
		}

		@Override
		public Void value() {
			return null;
		}
	}
}
