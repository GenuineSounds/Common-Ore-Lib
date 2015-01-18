package com.genuineminecraft.ores.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.genuineminecraft.ores.metals.Metal;

public class Ingot extends Item {

	public final Metal metal;

	public Ingot(Metal metal) {
		this.metal = metal;
		setUnlocalizedName("ingot" + metal.nameFixed);
		setCreativeTab(CreativeTabs.tabMaterials);
		setTextureName("CommonOres:ingots/" + metal.nameFixed);
	}
}
