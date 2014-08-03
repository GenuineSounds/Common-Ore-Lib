package com.genuineminecraft.ores.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Nugget extends Item {

	private String name;

	public Nugget(String name) {
		name = "nugget" + name;
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setTextureName("CommonOres:nuggets/" + name.substring(6));
	}

	public String getName() {
		return name;
	}
}
