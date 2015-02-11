package com.genuineflix.metal.api;

import com.genuineflix.metal.api.data.collections.DataCompound;

public interface LoadableData<T> {

	public T load(DataCompound nbt);
}
