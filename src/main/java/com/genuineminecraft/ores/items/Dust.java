package com.genuineminecraft.ores.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineminecraft.ores.metals.Metal;

public class Dust extends Item {

	public final Metal metal;

	public Dust(Metal metal) {
		this.metal = metal;
		this.setUnlocalizedName("dust" + metal.nameFixed);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setTextureName("CommonOres:dusts/" + metal.nameFixed);
	}
}
