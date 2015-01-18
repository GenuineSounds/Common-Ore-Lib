package com.genuineminecraft.ores.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineminecraft.ores.metals.Metal;

public class Dust extends Item {

	public final Metal metal;

	public Dust(Metal metal) {
		this.metal = metal;
		setUnlocalizedName("dust" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabMaterials);
		setTextureName("CommonOres:dusts/" + metal.nameFixed);
	}
}
