package com.genuineminecraft.ores.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineminecraft.ores.metals.Metal;

public class Ingot extends Item {

	public final Metal metal;

	public Ingot(Metal metal) {
		this.metal = metal;
		this.setUnlocalizedName("ingot" + metal.nameFixed);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setTextureName("CommonOres:ingots/" + metal.nameFixed);
	}
}
