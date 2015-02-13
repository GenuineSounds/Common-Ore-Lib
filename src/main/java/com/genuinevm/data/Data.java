package com.genuinevm.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Data<T> {

	/**
	 * The value held by the instance of this object.
	 * @return T: value of this object.
	 */
	public T value();

	public Data<T> copy();

	public void read(DataInput in) throws IOException;

	public void write(DataOutput out) throws IOException;
}
