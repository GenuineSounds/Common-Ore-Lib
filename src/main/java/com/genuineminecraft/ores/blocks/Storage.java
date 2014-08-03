package com.genuineminecraft.ores.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;

public class Storage extends BlockCompressed {

	private String name;

	public Storage(String name) {
		super(MapColor.ironColor);
		name = "block" + name;
		this.name = name;
		this.setBlockName(name);
		this.setHardness(5F);
		this.setResistance(1F);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockTextureName("CommonOres:blocks/" + name.substring(5));
	}

	public Storage setHarvest(int harvestLevel) {
		this.setHarvestLevel("pickaxe", harvestLevel);
		return this;
	}

	public String getName() {
		return this.name;
	}
}
