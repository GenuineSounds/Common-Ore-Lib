package com.genuineminecraft.ores.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.creativetab.CreativeTabs;

public class Ore extends BlockOre {

	public String name;

	public Ore(String name) {
		super();
		name = "ore" + name;
		this.name = name;
		this.setBlockName(name);
		this.setHardness(5F);
		this.setResistance(1F);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setStepSound(Block.soundTypeStone);
		this.setBlockTextureName("CommonOres:ores/" + name.substring(3));
	}

	public Ore setHarvest(int harvestLevel) {
		this.setHarvestLevel("pickaxe", harvestLevel);
		return this;
	}

	public String getName() {
		return this.name;
	}
}
