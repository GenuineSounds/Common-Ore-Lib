package com.genuineminecraft.ores.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Dust extends Item {

	private String name;

	public Dust(String name) {
		name = "dust" + name;
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setTextureName("CommonOres:dusts/" + name.substring(4));
	}

	public String getName() {
		return name;
	}
}
