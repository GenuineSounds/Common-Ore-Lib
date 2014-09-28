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
		this.setBlockName("ore" + metal.nameFixed);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setStepSound(Block.soundTypeStone);
		this.setBlockTextureName("CommonOres:ores/" + metal.nameFixed);
	}

	public void setup() {
		this.setHardness(this.metal.getHardness());
		this.setResistance(this.metal.getResistance());
	}
}
