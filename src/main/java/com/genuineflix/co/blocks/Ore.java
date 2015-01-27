package com.genuineflix.co.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.creativetab.CreativeTabs;

import com.genuineflix.co.metals.Metal;

public class Ore extends BlockOre {

	public final Metal metal;

	public Ore(final Metal metal) {
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
