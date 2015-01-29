package com.genuineflix.metal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.metals.Metal;

public class Storage extends BlockCompressed {

	public final Metal metal;

	public Storage(final Metal metal) {
		super(MapColor.ironColor);
		this.metal = metal;
		setBlockName("block" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabBlock);
		setStepSound(Block.soundTypeMetal);
		setBlockTextureName(CommonOre.MODID + ":blocks/" + metal.nameFixed);
	}

	public void setup() {
		setHardness(metal.getHardness());
		setResistance(metal.getResistance());
	}
}
