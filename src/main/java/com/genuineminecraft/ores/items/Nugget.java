package com.genuineminecraft.ores.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineminecraft.ores.metals.Metal;

public class Nugget extends Item {

	public final Metal metal;

	public Nugget(Metal metal) {
		this.metal = metal;
		setUnlocalizedName("nugget" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabMaterials);
		setTextureName("CommonOres:nuggets/" + metal.nameFixed);
	}
}
