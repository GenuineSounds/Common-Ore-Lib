package com.genuinevm.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import net.minecraft.nbt.NBTTagEnd;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class DataNull extends AbstractData<Void> {

	public static final DataNull INSTANCE = new DataNull();
	public static final String NAME = "NULL";
	public static final byte TYPE = 0;
	public static final long SIZE = 0;

	private DataNull() {}

	@Override
	public void read(final DataInput in) throws IOException {}

	@Override
	public void write(final DataOutput out) throws IOException {}

	@Override
	public byte getTypeByte() {
		return DataNull.TYPE;
	}

	@Override
	public String getTypeName() {
		return NAME;
	}

	@Override
	public String toString() {
		return "NULL";
	}

	@Override
	public AbstractData<Void> copy() {
		return DataNull.INSTANCE;
	}

	@Override
	public NBTTagEnd toNBT() {
		return new NBTTagEnd();
	}

	@Override
	public boolean equals(final Object obj) {
		return obj == INSTANCE;
	}

	@Override
	public Void value() {
		return null;
	}

	@Override
	public JsonNull serialize(final AbstractData<Void> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return JsonNull.INSTANCE;
	}

	@Override
	public DataNull deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return DataNull.INSTANCE;
	}
}