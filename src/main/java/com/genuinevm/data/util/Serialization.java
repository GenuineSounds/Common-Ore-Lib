package com.genuinevm.data.util;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.genuinevm.data.AbstractData;
import com.genuinevm.data.DataNull;
import com.genuinevm.data.array.DataByteArray;
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
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class Serialization {

	public static AbstractData serializedElement(final JsonElement element, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		AbstractData out = DataNull.INSTANCE;
		if (element.isJsonObject())
			// Push down object as DataCompound for deserialization
			out = new DataCompound();
		else if (element.isJsonArray())
			// Push down array as DataList for deserialization
			out = new DataList();
		// Must be a primitive.
		else {
			final JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isBoolean())
				// Push Booleans for deserialization
				out = new DataBoolean();
			else
				try {
					final Number number = convertToSmallestNumber(primitive.getAsNumber().toString());
					// Push down primitives for deserialization
					out = determineNumber(number);
					// If no valid number was found and pushed then check for special case.
					// These return since the deserialization strategy is not in that Data class.
					if (out == DataNull.INSTANCE) {
						if (number instanceof BigInteger)
							return new DataByteArray(primitive.getAsBigInteger().toByteArray());
						else if (number instanceof BigDecimal)
							return new DataString(primitive.getAsBigDecimal().toPlainString());
						throw new JsonParseException("Enable to determine Number value." + number.getClass());
					}
				}
				catch (final NumberFormatException e) {}
			if (out == DataNull.INSTANCE && primitive.isString())
				// Push Strings for deserialization
				out = new DataString();
		}
		return (AbstractData) out.deserialize(element, out.getClass(), context);
	}

	public static Number convertToSmallestNumber(String input) {
		// Is a decimal number.
		if (input.contains(".")) {
			// Fix no leading zero
			if (input.startsWith("."))
				input = "0" + input;
			// Fix no trailing zero
			if (input.endsWith("."))
				input = input + "0";
			try {
				final Float fValue = Float.parseFloat(input);
				// Check for loss of precision with floats, if none return it.
				if (fValue.toString().equals(input))
					return fValue;
				final Double dValue = Double.parseDouble(input);
				// Check for loss of precision with floats, if none return it.
				if (dValue.toString().equals(input))
					return dValue;
			}
			catch (final NumberFormatException e) {}
			// If it's not a BigDecimal then just throw the error because the string was broken as fuck.
			return new BigDecimal(input);
		}
		try {
			final Long value = Long.decode(input);
			// Check Byte first since it's the smallest
			if (value.longValue() >= Byte.MIN_VALUE && value.longValue() <= Byte.MAX_VALUE)
				return Byte.decode(input);
			// Next in line
			else if (value.longValue() >= Short.MIN_VALUE && value.longValue() <= Short.MAX_VALUE)
				return Short.decode(input);
			// Next in line
			else if (value.longValue() >= Integer.MIN_VALUE && value.longValue() <= Integer.MAX_VALUE)
				return Integer.decode(input);
			// It must be a Long then
			return value;
		}
		catch (final NumberFormatException e) {
			// If it's not a BigInteger then just throw the error because the string was broken as fuck.
			return new BigInteger(input);
		}
	}

	public static AbstractData determineNumber(final Number number) {
		AbstractData out = DataNull.INSTANCE;
		if (number instanceof Byte)
			out = new DataByte();
		else if (number instanceof Short)
			out = new DataShort();
		else if (number instanceof Integer)
			out = new DataInteger();
		else if (number instanceof Float)
			out = new DataFloat();
		else if (number instanceof Double)
			out = new DataDouble();
		else if (number instanceof Long)
			out = new DataLong();
		return out;
	}
}
