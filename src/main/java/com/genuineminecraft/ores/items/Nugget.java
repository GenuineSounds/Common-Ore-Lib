package com.genuineminecraft.ores.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineminecraft.ores.metals.Metal;

public class Nugget extends Item {

	public final Metal metal;

	public Nugget(Metal metal) {
		this.metal = metal;
		this.setUnlocalizedName("nugget" + metal.nameFixed);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setTextureName("CommonOres:nuggets/" + metal.nameFixed);
	}
}
