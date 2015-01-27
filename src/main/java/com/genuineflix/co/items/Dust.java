package com.genuineflix.co.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineflix.co.metals.Metal;

public class Dust extends Item {

	public final Metal metal;

	public Dust(final Metal metal) {
		this.metal = metal;
		setUnlocalizedName("dust" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabMaterials);
		setTextureName("CommonOres:dusts/" + metal.nameFixed);
	}
}
