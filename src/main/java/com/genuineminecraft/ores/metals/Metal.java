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
	private String primary;
	private String secondary;
	private boolean alloy;

	public Metal(String name) {
		this.name = name;
		nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		ore = new Ore(this);
		dust = new Dust(this);
		ingot = new Ingot(this);
		nugget = new Nugget(this);
		storage = new Storage(this);
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
	public float getHardness() {
		return hardness;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getNodeSize() {
		return nodeSize;
	}

	@Override
	public int getNodesPerChunk() {
		return nodesPerChunk;
	}

	@Override
	public String getPrimaryComponent() {
		return primary;
	}

	@Override
	public float getResistance() {
		return resistance;
	}

	@Override
	public String getSecondaryComponent() {
		return secondary;
	}

	@Override
	public float getSpread() {
		return spread;
	}

	@Override
	public boolean isAlloy() {
		return alloy;
	}

	@Override
	public void setComponents(String primary, String secondary) {
		this.primary = primary;
		this.secondary = secondary;
		alloy = true;
	}

	private void setComponents(Metal primary, Metal secondary) {
		this.primary = primary.name;
		this.secondary = secondary.name;
		alloy = true;
	}

	public Metal setup(float chunkRarity, float depth, int nodesPerChunk, int nodeSize, float spread, float hardness, float resistance) {
		this.chunkRarity = chunkRarity;
		this.depth = depth;
		this.nodesPerChunk = nodesPerChunk;
		this.nodeSize = nodeSize;
		this.spread = spread;
		this.hardness = hardness;
		this.resistance = resistance;
		ore.setup();
		storage.setup();
		return this;
	}
}
