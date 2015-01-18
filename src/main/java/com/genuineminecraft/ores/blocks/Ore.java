package com.genuineminecraft.ores.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.creativetab.CreativeTabs;

import com.genuineminecraft.ores.metals.Metal;

public class Ore extends BlockOre {

	public final Metal metal;

	public Ore(Metal metal) {
		super();
		this.metal = metal;
		setBlockName("ore" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabBlock);
		setStepSound(Block.soundTypeStone);
		setBlockTextureName("CommonOres:ores/" + metal.nameFixed);
	}

	public void setup() {
		setHardness(metal.getHardness());
		setResistance(metal.getResistance());
	}
}
