package com.genuinevm.data.primitive;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;

import net.minecraft.nbt.NBTTagString;

import org.apache.commons.lang3.StringEscapeUtils;

import com.genuinevm.data.AbstractData;
import com.genuinevm.data.Data;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class DataString extends AbstractData<String> {

	public static final String NAME = "STRING";
	public static final long SIZE = 8;
	public static final byte TYPE = 8;
	private String value = "";

	public DataString() {}

	public DataString(final String value) {
		this.value = value;
		if (value == null)
			throw new IllegalArgumentException("Null value not allowed");
	}

	@Override
	public String value() {
		return value;
	}

	@Override
	public void read(final DataInput in) throws IOException {
		value = in.readUTF();
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeUTF(value);
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
		return StringEscapeUtils.escapeJson(value);
	}

	@Override
	public DataString copy() {
		return new DataString(value);
	}

	@Override
	public NBTTagString toNBT() {
		return new NBTTagString(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (super.equals(obj))
			return true;
		if (obj instanceof Data)
			return value().equals(((Data) obj).value());
		return value().equals(obj);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public JsonPrimitive serialize(final AbstractData<String> src, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(src.value());
	}

	@Override
	public DataString deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		return new DataString(json.getAsString());
	}
}
