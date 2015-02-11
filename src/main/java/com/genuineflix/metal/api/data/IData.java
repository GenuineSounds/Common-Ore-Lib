package com.genuineflix.metal.api.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTBase;

interface IData<T> {

	public String getTypeName();

	public byte getTypeByte();

	void write(DataOutput out) throws IOException;

	void read(DataInput in, int depth, SizeLimit tracker) throws IOException;

	public Data<T> copy();

	public NBTBase toNBT();

	@Override
	public String toString();

	public T value();
}
