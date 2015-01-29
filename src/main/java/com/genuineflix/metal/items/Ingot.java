package com.genuineflix.metal.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.metals.Metal;

public class Ingot extends Item {

	public final Metal metal;

	public Ingot(final Metal metal) {
		this.metal = metal;
		setUnlocalizedName("ingot" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabMaterials);
		setTextureName(CommonOre.MODID + ":ingots/" + metal.nameFixed);
	}
}
