package com.genuineflix.metal.api;

import com.genuineflix.data.collections.DataCompound;

public interface LoadableData<T> {

	public T load(DataCompound nbt);
}
