package com.genuineminecraft.ores.metals;

import com.genuineminecraft.ores.blocks.Ore;
import com.genuineminecraft.ores.blocks.Storage;
import com.genuineminecraft.ores.interfaces.IAlloy;
import com.genuineminecraft.ores.interfaces.IOre;
import com.genuineminecraft.ores.items.Dust;
import com.genuineminecraft.ores.items.Ingot;
import com.genuineminecraft.ores.items.Nugget;

public class Metal implements IOre, IAlloy {

	public final String name;
	public final Ore ore;
	public final Storage storage;
	public final Dust dust;
	public final Ingot ingot;
	public final Nugget nugget;
	private float chunkRarity;
	private float depth;
	private int nodesPerChunk;
	private int nodeSize;
	private float spread;
	private Metal primary;
	private Metal secondary;

	public Metal(String name) {
		this.name = name;
		String nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		ore = new Ore(nameFixed);
		dust = new Dust(nameFixed);
		ingot = new Ingot(nameFixed);
		nugget = new Nugget(nameFixed);
		storage = new Storage(nameFixed);
	}

	public void setup(float chunkRarity, float depth, int nodesPerChunk, int nodeSize, float spread) {
		this.chunkRarity = chunkRarity;
		this.depth = depth;
		this.nodesPerChunk = nodesPerChunk;
		this.nodeSize = nodeSize;
		this.spread = spread;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setComponents(Metal primary, Metal secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}

	@Override
	public int getNodesPerChunk() {
		return nodesPerChunk;
	}

	@Override
	public float getChunkRarity() {
		return chunkRarity;
	}

	@Override
	public float getDepth() {
		return depth;
	}

	@Override
	public float getSpread() {
		return spread;
	}

	@Override
	public int getNodeSize() {
		return nodeSize;
	}

	@Override
	public Metal getPrimaryComponent() {
		return primary;
	}

	@Override
	public Metal getSecondaryComponent() {
		return secondary;
	}

	@Override
	public boolean isAlloy() {
		return primary != null && secondary != null;
	}
}
