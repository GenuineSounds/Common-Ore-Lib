package com.genuinevm.data;

import net.minecraft.nbt.NBTBase;

import com.genuinevm.data.array.DataByteArray;
import com.genuinevm.data.array.DataIntegerArray;
import com.genuinevm.data.collection.DataCompound;
import com.genuinevm.data.collection.DataList;
import com.genuinevm.data.primitive.DataBoolean;
import com.genuinevm.data.primitive.DataByte;
import com.genuinevm.data.primitive.DataDouble;
import com.genuinevm.data.primitive.DataFloat;
import com.genuinevm.data.primitive.DataInteger;
import com.genuinevm.data.primitive.DataLong;
import com.genuinevm.data.primitive.DataShort;
import com.genuinevm.data.primitive.DataString;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public abstract class AbstractData<T> implements Data<T>, JsonSerializer<AbstractData<T>>, JsonDeserializer<AbstractData<T>> {

	/**
	 * Name of this data structure. Not required to be non empty.
	 * @return Name corresponding to this data type.
	 */
	public abstract String getTypeName();

	/**
	 * The byte corresponding with this IData. Used for reading and writing GDF to a stream.
	 * Only one byte can be assigned to each IData.
	 * @return unique byte specific to this IData class.
	 */
	public abstract byte getTypeByte();

	/**
	 * Convenience method to create an NBT out of the GDF.
	 * @return
	 */
	public abstract NBTBase toNBT();

	public static final AbstractData[] TYPES = new AbstractData[] {
			//
			DataNull.INSTANCE, new DataByte(), new DataShort(), new DataInteger(),
			//
			new DataLong(), new DataFloat(), new DataDouble(), new DataByteArray(),
			//
			new DataString(), new DataList(), new DataCompound(), new DataIntegerArray(),
			//
			new DataBoolean() };

	public static AbstractData create(final byte type) {
		if (type > 0 && type < TYPES.length)
			return (AbstractData) TYPES[type].copy();
		return DataNull.INSTANCE;
	}

	public static class BadDataException extends Exception {

		public BadDataException(final String message, final Exception context) {
			super(message, context);
		}
	}
}
