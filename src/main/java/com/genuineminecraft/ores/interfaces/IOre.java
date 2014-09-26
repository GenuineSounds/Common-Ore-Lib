package com.genuineminecraft.ores.interfaces;

public interface IOre {

	public String getName();

	public int getNodesPerChunk();

	public float getChunkRarity();

	public float getDepth();

	public float getSpread();

	public int getNodeSize();

	public float getHardness();

	public float getResistance();
}
