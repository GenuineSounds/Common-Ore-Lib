package com.genuineflix.co.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineflix.co.metals.Metal;

public class Nugget extends Item {

	public final Metal metal;

	public Nugget(final Metal metal) {
		this.metal = metal;
		setUnlocalizedName("nugget" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabMaterials);
		setTextureName("CommonOres:nuggets/" + metal.nameFixed);
	}
}
