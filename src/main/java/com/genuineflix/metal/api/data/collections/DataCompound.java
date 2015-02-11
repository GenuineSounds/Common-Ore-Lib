package com.genuineflix.metal.api.data.collections;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.genuineflix.metal.api.data.Data;
import com.genuineflix.metal.api.data.IDataPrimitive;
import com.genuineflix.metal.api.data.SizeLimit;
import com.genuineflix.metal.api.data.primitives.DataByte;
import com.genuineflix.metal.api.data.primitives.DataDouble;
import com.genuineflix.metal.api.data.primitives.DataFloat;
import com.genuineflix.metal.api.data.primitives.DataInteger;
import com.genuineflix.metal.api.data.primitives.DataLong;
import com.genuineflix.metal.api.data.primitives.DataShort;
import com.genuineflix.metal.api.data.primitives.DataString;

public class DataCompound extends Data<Map<String, Data>> {

	public static final String NAME = "COMPOUND";
	public static final long SIZE = 16;
	public static final byte TYPE = 10;
	private static final Logger logger = LogManager.getLogger();
	private final Map<String, Data> values = new HashMap<String, Data>();

	@Override
	public Map<String, Data> value() {
		return values;
	}

	@Override
	public void write(final DataOutput output) throws IOException {
		for (final String name : values.keySet()) {
			final Data data = values.get(name);
			output.writeByte(data.getTypeByte());
			if (data.getTypeByte() != 0) {
				output.writeUTF(name);
				data.write(output);
			}
		}
		output.writeByte(0);
	}

	@Override
	public void read(final DataInput input, final int depth, final SizeLimit limit) throws IOException {
		if (depth > 512)
			throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
		else {
			values.clear();
			byte type;
			while ((type = input.readByte()) != 0) {
				final String name = input.readUTF();
				limit.assertLimit(DataCompound.SIZE * name.length());
				final Data data = Data.create(type);
				try {
					data.read(input, depth + 1, limit);
				}
				catch (final IOException e) {
					final CrashReport report = CrashReport.makeCrashReport(e, "Loading NBT data");
					final CrashReportCategory category = report.makeCategory("NBT Tag");
					category.addCrashSection("Tag name", name);
					category.addCrashSection("Tag type", Byte.valueOf(type));
					throw new ReportedException(report);
				}
				values.put(name, data);
			}
		}
	}

	public Set<String> getEntryNames() {
		return values.keySet();
	}

	@Override
	public byte getTypeByte() {
		return DataCompound.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	public void setData(final String name, final Data value) {
		values.put(name, value);
	}

	public void setByte(final String name, final byte value) {
		values.put(name, new DataByte(value));
	}

	public void setShort(final String name, final short value) {
		values.put(name, new DataShort(value));
	}

	public void setInteger(final String name, final int value) {
		values.put(name, new DataInteger(value));
	}

	public void setLong(final String name, final long value) {
		values.put(name, new DataLong(value));
	}

	public void setFloat(final String name, final float value) {
		values.put(name, new DataFloat(value));
	}

	public void setDouble(final String name, final double value) {
		values.put(name, new DataDouble(value));
	}

	public void setString(final String name, final String value) {
		values.put(name, new DataString(value));
	}

	public void setByteArray(final String name, final byte[] value) {
		values.put(name, new DataArrayByte(value));
	}

	public void setIntArray(final String name, final int[] value) {
		values.put(name, new DataArrayInteger(value));
	}

	public void setBoolean(final String name, final boolean value) {
		setByte(name, (byte) (value ? 1 : 0));
	}

	public Data getTag(final String name) {
		return values.get(name);
	}

	public byte getType(final String name) {
		final Data nbtbase = values.get(name);
		return nbtbase != null ? nbtbase.getTypeByte() : 0;
	}

	public boolean hasKey(final String name) {
		return values.containsKey(name);
	}

	public boolean hasKey(final String name, final int withType) {
		final byte type = getType(name);
		return type == withType ? true : withType != 99 ? false : type == 1 || type == 2 || type == 3 || type == 4 || type == 5 || type == 6;
	}

	public byte getByte(final String name) {
		try {
			return values.containsKey(name) ? ((IDataPrimitive) values.get(name)).toByte() : 0;
		}
		catch (final ClassCastException e) {
			return (byte) 0;
		}
	}

	public short getShort(final String name) {
		try {
			return values.containsKey(name) ? ((IDataPrimitive) values.get(name)).toShort() : 0;
		}
		catch (final ClassCastException e) {
			return (short) 0;
		}
	}

	public int getInteger(final String name) {
		try {
			return values.containsKey(name) ? ((IDataPrimitive) values.get(name)).toInt() : 0;
		}
		catch (final ClassCastException classcastexception) {
			return 0;
		}
	}

	public long getLong(final String name) {
		try {
			return values.containsKey(name) ? ((IDataPrimitive) values.get(name)).toLong() : 0;
		}
		catch (final ClassCastException e) {
			return 0;
		}
	}

	public float getFloat(final String name) {
		try {
			return values.containsKey(name) ? ((IDataPrimitive) values.get(name)).toFloat() : 0;
		}
		catch (final ClassCastException classcastexception) {
			return 0;
		}
	}

	public double getDouble(final String name) {
		try {
			return values.containsKey(name) ? ((IDataPrimitive) values.get(name)).toDouble() : 0;
		}
		catch (final ClassCastException classcastexception) {
			return 0;
		}
	}

	public String getString(final String name) {
		try {
			return values.containsKey(name) ? ((DataString) values.get(name)).value() : "";
		}
		catch (final ClassCastException classcastexception) {
			return "";
		}
	}

	public byte[] getByteArray(final String name) {
		try {
			return values.containsKey(name) ? ((DataArrayByte) values.get(name)).value() : new byte[0];
		}
		catch (final ClassCastException e) {
			return new byte[0];
		}
	}

	public int[] getIntArray(final String name) {
		try {
			return values.containsKey(name) ? ((DataArrayInteger) values.get(name)).value() : new int[0];
		}
		catch (final ClassCastException e) {
			return new int[0];
		}
	}

	public DataCompound getCompoundTag(final String name) {
		try {
			return values.containsKey(name) ? (DataCompound) values.get(name) : new DataCompound();
		}
		catch (final ClassCastException e) {
			return new DataCompound();
		}
	}

	public DataList getTagList(final String name, final int ofType) {
		try {
			if (getType(name) != DataList.TYPE)
				return new DataList();
			else {
				final DataList list = (DataList) values.get(name);
				return list.size() > 0 && list.getListType() == ofType ? list : new DataList();
			}
		}
		catch (final ClassCastException e) {
			return new DataList();
		}
	}

	public boolean getBoolean(final String name) {
		return getByte(name) != 0;
	}

	public void remove(final String name) {
		values.remove(name);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (final String str : values.keySet()) {
			sb.append(str);
			sb.append(':');
			sb.append(values.get(str));
			sb.append(',');
		}
		sb.append('}');
		return sb.toString();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Data<Map<String, Data>> copy() {
		final DataCompound compound = new DataCompound();
		final Iterator iterator = values.keySet().iterator();
		while (iterator.hasNext()) {
			final String s = (String) iterator.next();
			compound.setData(s, values.get(s).copy());
		}
		return compound;
	}

	@Override
	public NBTTagCompound toNBT() {
		final NBTTagCompound compound = new NBTTagCompound();
		for (final String key : values.keySet())
			compound.setTag(key, values.get(key).toNBT());
		return compound;
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj) && values.entrySet().equals(((DataCompound) obj).values.entrySet());
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ values.hashCode();
	}
}