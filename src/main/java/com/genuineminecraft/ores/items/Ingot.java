package com.genuineminecraft.ores.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Ingot extends Item {

	private String name;

	public Ingot(String name) {
		name = "ingot" + name;
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setTextureName("CommonOres:ingots/" + name.substring(5));
	}

	public String getName() {
		return name;
	}
}
