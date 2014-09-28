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
	public final String nameFixed;
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
	private float hardness;
	private float resistance;
	private Metal primary;
	private Metal secondary;

	public Metal(String name) {
		this.name = name;
		this.nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		this.ore = new Ore(this);
		this.dust = new Dust(this);
		this.ingot = new Ingot(this);
		this.nugget = new Nugget(this);
		this.storage = new Storage(this);
	}

	@Override
	public float getChunkRarity() {
		return this.chunkRarity;
	}

	@Override
	public float getDepth() {
		return this.depth;
	}

	@Override
	public float getHardness() {
		return this.hardness;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getNodeSize() {
		return this.nodeSize;
	}

	@Override
	public int getNodesPerChunk() {
		return this.nodesPerChunk;
	}

	@Override
	public Metal getPrimaryComponent() {
		return this.primary;
	}

	@Override
	public float getResistance() {
		return this.resistance;
	}

	@Override
	public Metal getSecondaryComponent() {
		return this.secondary;
	}

	@Override
	public float getSpread() {
		return this.spread;
	}

	@Override
	public boolean isAlloy() {
		return this.primary != null && this.secondary != null;
	}

	@Override
	public void setComponents(Metal primary, Metal secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}

	public Metal setup(float chunkRarity, float depth, int nodesPerChunk, int nodeSize, float spread, float hardness, float resistance) {
		this.chunkRarity = chunkRarity;
		this.depth = depth;
		this.nodesPerChunk = nodesPerChunk;
		this.nodeSize = nodeSize;
		this.spread = spread;
		this.hardness = hardness;
		this.resistance = resistance;
		this.ore.setup();
		this.storage.setup();
		return this;
	}
}
