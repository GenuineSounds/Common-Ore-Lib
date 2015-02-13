package com.genuineflix.metal.api;

import com.genuinevm.data.collection.DataCompound;

public interface LoadableData<T> {

	public T load(DataCompound compound);
}
