package com.genuineflix.metal.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineflix.metal.interfaces.IMetal;
import com.genuineflix.metal.registry.Metal;

public class Utility {

	public static List<ItemStack> getOreDictStacks(final Metal metal) {
		final List<ItemStack> list = new ArrayList<ItemStack>();
		for (final IMetal.Compound component : metal.getCompounds())
			list.addAll(OreDictionary.getOres("ore" + component.metal.nameFixed));
		return list;
	}
}
