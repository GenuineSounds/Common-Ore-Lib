package com.genuineflix.metal.api.data.collections;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagList;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.IDataPrimitive;
import com.genuineflix.metal.api.data.SizeLimit;
import com.genuineflix.metal.api.data.primitives.DataString;

public class DataList extends Data<List<Data>> {

	public static final String NAME = "LIST";
	public static final long SIZE = 8;
	public static final byte TYPE = 9;
	private byte type = 0;
	private List<Data> value = new ArrayList<Data>();

	@Override
	public List<Data> value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		if (!value.isEmpty())
			type = value.get(0).getTypeByte();
		else
			type = DataEnd.TYPE;
		out.writeByte(type);
		out.writeInt(value.size());
		for (int i = 0; i < value.size(); i++)
			value.get(i).write(out);
	}

	@Override
	public void read(final DataInput in, final int depth, final SizeLimit limit) throws IOException {
		if (depth > 512)
			throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
		limit.assertLimit(DataList.SIZE);
		type = in.readByte();
		final int size = in.readInt();
		value = new ArrayList();
		for (int i = 0; i < size; i++) {
			final Data data = Data.create(type);
			data.read(in, depth + 1, limit);
			value.add(data);
		}
	}

	@Override
	public byte getTypeByte() {
		return DataList.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(value.size() * (int) DataList.SIZE);
		sb.append('[');
		for (int i = 0; i < value.size(); i++) {
			sb.append(i);
			sb.append(':');
			sb.append(value.get(i));
			sb.append(',');
		}
		sb.append(']');
		return sb.toString();
	}

	public void addData(final Data data) {
		if (type == 0)
			type = data.getTypeByte();
		else if (type != data.getTypeByte()) {
			System.err.println("WARNING: Adding mismatching tag types to tag list");
			return;
		}
		value.add(data);
	}

	public void addData(final int index, final Data data) {
		if (index >= 0 && index < value.size()) {
			if (type == 0)
				type = data.getTypeByte();
			else if (type != data.getTypeByte()) {
				System.err.println("WARNING: Adding mismatching tag types to tag list");
				return;
			}
			value.set(index, data);
		} else
			System.err.println("WARNING: index out of bounds to set tag in tag list");
	}

	public Data removeTag(final int index) {
		return value.remove(index);
	}

	public DataCompound getCompound(final int index) {
		if (index >= 0 && index < value.size()) {
			final Data data = value.get(index);
			return data.getTypeByte() == DataCompound.TYPE ? (DataCompound) data : new DataCompound();
		} else
			return new DataCompound();
	}

	public int[] getArrayInteger(final int index) {
		if (index >= 0 && index < value.size()) {
			final Data data = value.get(index);
			return data.getTypeByte() == DataArrayInteger.TYPE ? ((DataArrayInteger) data).value() : new int[0];
		} else
			return new int[0];
	}

	public double getDouble(final int index) {
		if (index >= 0 && index < value.size()) {
			final Data data = value.get(index);
			return data instanceof IDataPrimitive ? ((IDataPrimitive) data).toDouble() : 0.0D;
		} else
			return 0.0D;
	}

	public float getFloat(final int index) {
		if (index >= 0 && index < value.size()) {
			final Data data = value.get(index);
			return data instanceof IDataPrimitive ? ((IDataPrimitive) data).toFloat() : 0.0F;
		} else
			return 0.0F;
	}

	public String getString(final int index) {
		if (index >= 0 && index < value.size()) {
			final Data data = value.get(index);
			return data instanceof DataString ? ((DataString) data).value() : data.toString();
		} else
			return "";
	}

	public int size() {
		return value.size();
	}

	@Override
	public Data<List<Data>> copy() {
		final DataList list = new DataList();
		list.type = type;
		for (final Data data : value)
			list.value.add(data.copy());
		return list;
	}

	@Override
	public NBTTagList toNBT() {
		final NBTTagList list = new NBTTagList();
		for (final Data data : value)
			list.appendTag(data.toNBT());
		return list;
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj)) {
			final DataList list = (DataList) obj;
			return type == list.type && value.equals(list.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ value.hashCode();
	}

	public byte getListType() {
		return type;
	}
}