package com.genuineflix.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTBase;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface IData<T> extends JsonSerializer<IData<T>>, JsonDeserializer<IData<T>> {

	/**
	 * Name of this data structure. Not required to be non empty.
	 * @return Name corresponding to this data type.
	 */
	public String getTypeName();

	/**
	 * The byte corresponding with this IData. Used for reading and writing GDF to a stream.
	 * Only one byte can be assigned to each IData.
	 * @return unique byte specific to this IData class.
	 */
	public byte getTypeByte();

	/**
	 * The value help by the instance of this object.
	 * @return T: value of this object.
	 */
	public T value();

	public IData<T> copy();

	public NBTBase toNBT();

	void read(DataInput in, int depth, SizeLimit limit) throws IOException;

	void write(DataOutput out) throws IOException;

	public static class BadDataException extends Exception {

		public BadDataException(final String message, final Exception context) {
			super(message, context);
		}
	}
}
