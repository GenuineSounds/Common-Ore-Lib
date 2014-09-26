package com.genuineminecraft.ores.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;

import com.genuineminecraft.ores.metals.Metal;

public class Storage extends BlockCompressed {

	public final Metal metal;

	public Storage(Metal metal) {
		super(MapColor.ironColor);
		this.metal = metal;
		this.setBlockName("block" + metal.nameFixed);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockTextureName("CommonOres:blocks/" + metal.nameFixed);
	}

	public void setup() {
		this.setHardness(metal.getHardness());
		this.setResistance(metal.getResistance());
	}
}
