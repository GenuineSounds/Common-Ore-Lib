package com.genuineminecraft.ores.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.world.World;

import com.genuineminecraft.ores.CommonOres;
import com.genuineminecraft.ores.interfaces.IAlloy;
import com.genuineminecraft.ores.interfaces.IMinable;

public class Ore extends BlockOre implements IMinable, IAlloy {

	private String name;
	private boolean isAlloy;
	private int chunkRarity;
	private int nodesPerChunk;
	private int nodeSize;
	private int depth;
	private int spread;
	private Block primary;
	private Block secondary;
	private Block tertiary;

	public Ore(String name) {
		super();
		name = "ore" + name;
		this.name = name;
		this.setBlockName(name);
		this.setHardness(5F);
		this.setResistance(1F);
		this.setStepSound(Block.soundTypeStone);
		this.setBlockTextureName("CommonOres:ores/" + name.substring(3));
	}

	public Ore setup(int chunkRarity, int nodesPerChunk, int nodeSize, int depth, int spread) {
		this.chunkRarity = chunkRarity;
		this.nodesPerChunk = nodesPerChunk;
		this.nodeSize = nodeSize;
		this.depth = depth;
		this.spread = spread;
		return this;
	}

	public Ore setHarvest(int harvestLevel) {
		this.setHarvestLevel("pickaxe", harvestLevel);
		return this;
	}

	@Override
	public boolean isReplaceableOreGen(World world, int x, int y, int z, Block target) {
		return CommonOres.oreMap.containsValue(target);
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int getChunkRarity() {
		return chunkRarity;
	}

	@Override
	public int getNodesPerChunk() {
		return nodesPerChunk;
	}

	public int getNodeSize() {
		return nodeSize;
	}

	@Override
	public int getDepth() {
		return depth;
	}

	@Override
	public int getSpread() {
		return spread;
	}

	@Override
	public void setComponents(Block primary, Block secondary, Block tertiary) {
		this.primary = primary;
		this.secondary = secondary;
		this.tertiary = tertiary;
	}

	@Override
	public Block getPrimaryComponent() {
		return primary;
	}

	@Override
	public Block getSecondaryComponent() {
		return secondary;
	}

	@Override
	public Block getTertiaryComponent() {
		return tertiary;
	}

	@Override
	public boolean isAlloy() {
		return getPrimaryComponent() != null && getSecondaryComponent() != null;
	}
}
