package com.genuineflix.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTBase;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface IData<T> extends JsonSerializer<IData<T>>, JsonDeserializer<IData<T>> {

	public String getTypeName();

	public byte getTypeByte();

	void write(DataOutput out) throws IOException;

	void read(DataInput in, int depth, SizeLimit limit) throws IOException;

	public IData<T> copy();

	public NBTBase toNBT();

	@Override
	public String toString();

	public T value();

	public static class BadDataException extends Exception {

		public BadDataException(final String message) {
			super(message);
		}
	}
}
