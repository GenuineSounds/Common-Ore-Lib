package com.genuineflix.co.utils;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.world.chunk.Chunk;

public class ChunkMap<T> extends TIntObjectHashMap<T> {

	public boolean contains(final Chunk chunk) {
		return super.contains(Utility.hashChunk(chunk));
	}

	public boolean containsKey(final Chunk chunk) {
		return super.containsKey(Utility.hashChunk(chunk));
	}

	public T get(final Chunk chunk) {
		return super.get(Utility.hashChunk(chunk));
	}

	public T put(final Chunk chunk, final T value) {
		return super.put(Utility.hashChunk(chunk), value);
	}
}
