package com.genuineflix.data.collections;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagList;

import com.genuineflix.data.AbstractData;
import com.genuineflix.data.IData;
import com.genuineflix.data.IDataPrimitive;
import com.genuineflix.data.SizeLimit;
import com.genuineflix.data.primitives.DataByte;
import com.genuineflix.data.primitives.DataInteger;
import com.genuineflix.data.primitives.DataNull;
import com.genuineflix.data.primitives.DataString;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class DataList extends AbstractData<List<AbstractData>> {

	public static final String NAME = "LIST";
	public static final long SIZE = 8;
	public static final byte TYPE = 9;
	private byte type = 0;
	private List<AbstractData> value = new ArrayList<AbstractData>();

	@Override
	public List<AbstractData> value() {
		return value;
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		if (!value.isEmpty())
			type = value.get(0).getTypeByte();
		else
			type = DataNull.TYPE;
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
			final AbstractData data = AbstractData.create(type);
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
			sb.append(value.get(i).directString());
			sb.append(',');
		}
		sb.append(']');
		return sb.toString();
	}

	public void add(final AbstractData data) {
		if (type == 0)
			type = data.getTypeByte();
		else if (type != data.getTypeByte()) {
			System.err.println("WARNING: Adding mismatching tag types to tag list");
			return;
		}
		value.add(data);
	}

	private AbstractData replace(final int index, final AbstractData data) {
		if (index >= 0 && index < value.size()) {
			if (type == 0)
				type = data.getTypeByte();
			else if (type != data.getTypeByte()) {
				System.err.println("WARNING: Adding mismatching tag types to tag list");
				return DataNull.INSTANCE;
			}
			return value.set(index, data);
		}
		System.err.println("WARNING: index out of bounds to set tag in tag list");
		return DataNull.INSTANCE;
	}

	public AbstractData remove(final int index) {
		return value.remove(index);
	}

	public DataCompound getCompound(final int index) {
		if (index >= 0 && index < value.size()) {
			final AbstractData data = value.get(index);
			return data.getTypeByte() == DataCompound.TYPE ? (DataCompound) data : new DataCompound();
		} else
			return new DataCompound();
	}

	public int[] getIntegerArray(final int index) {
		if (index >= 0 && index < value.size()) {
			final AbstractData data = value.get(index);
			return data.getTypeByte() == DataIntegerArray.TYPE ? ((DataIntegerArray) data).value() : new int[0];
		} else
			return new int[0];
	}

	public double getDouble(final int index) {
		if (index >= 0 && index < value.size()) {
			final AbstractData data = value.get(index);
			return data instanceof IDataPrimitive ? ((IDataPrimitive) data).toDouble() : 0;
		} else
			return 0;
	}

	public float getFloat(final int index) {
		if (index >= 0 && index < value.size()) {
			final AbstractData data = value.get(index);
			return data instanceof IDataPrimitive ? ((IDataPrimitive) data).toFloat() : 0;
		} else
			return 0;
	}

	public String getString(final int index) {
		if (index >= 0 && index < value.size()) {
			final AbstractData data = value.get(index);
			return data instanceof DataString ? ((DataString) data).value() : data.toString();
		} else
			return "";
	}

	public int size() {
		return value.size();
	}

	@Override
	public AbstractData<List<AbstractData>> copy() {
		final DataList list = new DataList();
		list.type = type;
		for (final AbstractData data : value)
			list.value.add((AbstractData) data.copy());
		return list;
	}

	@Override
	public NBTTagList toNBT() {
		final NBTTagList list = new NBTTagList();
		for (final IData data : value)
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

	@Override
	public JsonArray serialize(final IData<List<AbstractData>> src, final Type typeOfSrc, final JsonSerializationContext context) {
		final JsonArray array = new JsonArray();
		for (final AbstractData data : src.value())
			array.add(data.serialize(data, data.getClass(), context));
		return array;
	}

	@Override
	public AbstractData deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		final DataList out = new DataList();
		final JsonArray jsonArray = json.getAsJsonArray();
		final AbstractData[] array = new AbstractData[jsonArray.size()];
		boolean allDataByte = true;
		boolean allDataInteger = true;
		for (int i = 0; i < array.length; i++) {
			final JsonElement element = jsonArray.get(i);
			final AbstractData data = serializedElement(element, element.getClass(), context);
			if (data instanceof DataInteger || data instanceof DataByte)
				allDataByte &= data instanceof DataByte;
			else
				allDataByte = allDataInteger = false;
			array[i] = data;
		}
		if (allDataByte) {
			final byte[] bytesOut = new byte[array.length];
			for (int i = 0; i < array.length; i++)
				bytesOut[i] = ((DataByte) array[i]).value();
			return new DataByteArray(bytesOut);
		} else if (allDataInteger) {
			final int[] intsOut = new int[array.length];
			for (int i = 0; i < array.length; i++)
				intsOut[i] = ((DataInteger) array[i]).value();
			return new DataIntegerArray(intsOut);
		}
		for (final AbstractData data : array)
			out.add(data);
		return out;
	}
}
