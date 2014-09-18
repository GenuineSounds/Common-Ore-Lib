package com.genuineminecraft.ores.registry;

public enum Ore {
	aluminium(1.0F, 60, 6, 4, 10),
	     zinc(1.0F, 56, 6, 4, 10),
	    brass(1.0F, 52, 6, 4, 10),
	   copper(1.0F, 48, 6, 4, 10),
	   bronze(0.8F, 45, 6, 4, 10),
	      tin(0.8F, 41, 6, 4, 10),
	     lead(0.8F, 37, 6, 4, 10),
	    steel(0.8F, 33, 6, 4, 10),
	     iron(0.8F, 30, 6, 4, 10),
	    invar(0.6F, 26, 6, 4, 10),
	   nickel(0.6F, 22, 6, 4, 10),
	 tungsten(0.6F, 18, 6, 4, 10),
	   silver(0.6F, 15, 6, 4, 10),
	 electrum(0.4F, 11, 6, 4, 10),
	     gold(0.4F,  7, 6, 4, 10),
	 titanium(0.4F,  3, 6, 4, 10),
	 platinum(0.4F,  0, 6, 4, 10);

	Ore(float chunkRarity, float depth, int nodeSize, int nodesPerChunk, int spread) {
		this.chunkRarity = chunkRarity;
		this.depth = depth;
		this.nodeSize = nodeSize;
		this.nodesPerChunk = nodesPerChunk;
		this.spread = spread;
	}

	public float depth;
	public float spread;
	public int nodeSize;
	public int nodesPerChunk;
	public float chunkRarity;
}
