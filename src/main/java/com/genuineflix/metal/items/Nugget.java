package com.genuineflix.metal.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.metals.Metal;

public class Nugget extends Item {

	public final Metal metal;

	public Nugget(final Metal metal) {
		this.metal = metal;
		setUnlocalizedName("nugget" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabMaterials);
		setTextureName(CommonOre.MODID + ":nuggets/" + metal.nameFixed);
	}
}
