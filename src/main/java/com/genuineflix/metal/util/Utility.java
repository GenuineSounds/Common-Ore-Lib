package com.genuineflix.metal.util;

import java.util.ArrayList;
import java.util.List;

import com.genuineflix.metal.api.IMetal;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Utility {

	public static List<ItemStack> getOreDictStacks(final IMetal metal) {
		final List<ItemStack> list = new ArrayList<ItemStack>();
		for (final IMetal.Compound component : metal.getCompounds())
			list.addAll(OreDictionary.getOres("ore" + component.metal.getDisplayName()));
		return list;
	}
}
