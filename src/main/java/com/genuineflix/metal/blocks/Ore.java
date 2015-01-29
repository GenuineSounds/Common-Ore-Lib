package com.genuineflix.metal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.creativetab.CreativeTabs;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.metals.Metal;

public class Ore extends BlockOre {

	public final Metal metal;

	public Ore(final Metal metal) {
		super();
		this.metal = metal;
		setBlockName("ore" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabBlock);
		setStepSound(Block.soundTypeStone);
		setBlockTextureName(CommonOre.MODID + ":ores/" + metal.nameFixed);
	}

	public void setup() {
		setHardness(metal.getHardness());
		setResistance(metal.getResistance());
	}
}
