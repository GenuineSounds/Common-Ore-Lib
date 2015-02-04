package com.genuineflix.metal.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.interfaces.IAlloy.Component;
import com.genuineflix.metal.metals.Metal;

public class Utility {

	public static boolean areComponentsFound(final Metal metal, final World world, final int x, final int y, final int z, final int radius) {
		if (!metal.isAlloy())
			return false;
		final Component[] components = metal.getComponents();
		final boolean[] componentWasFound = new boolean[metal.getComponents().length];
		for (int xd = -radius; xd <= radius; xd++)
			for (int yd = -radius; yd <= radius; yd++)
				for (int zd = -radius; zd <= radius; zd++) {
					if (!world.blockExists(x + xd, y + yd, z + zd))
						continue;
					final String tag = world.getBlock(x + xd, y + yd, z + zd).getUnlocalizedName() + ":" + world.getBlockMetadata(x + xd, y + yd, z + zd);
					for (int i = 0; i < components.length; i++) {
						final Component component = components[i];
						final String oreName = camelCase("ore", component.name);
						if (!Utility.commonCache.containsKey(oreName))
							continue;
						if (Utility.commonCache.get(oreName).contains(tag))
							componentWasFound[i] = true;
					}
				}
		boolean missedAny = false;
		for (int i = 0; i < componentWasFound.length; i++)
			missedAny |= !componentWasFound[i];
		return !missedAny;
	}

	public static void cacheCommonBlock(final String oreDict, final Block block, final int meta) {
		if (!oreDict.startsWith("ore"))
			return;
		CommonOre.log.warn("Caching common: " + oreDict + ".add(" + block.getUnlocalizedName() + ":" + meta + ")");
		List<String> list;
		if (!commonCache.containsKey(oreDict))
			list = new ArrayList<String>();
		else
			list = commonCache.get(oreDict);
		list.add(block.getUnlocalizedName() + ":" + meta);
		commonCache.put(oreDict, list);
	}

	public static String camelCase(final String prefix, final String str) {
		return prefix.toLowerCase() + str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	public static String cleanName(String name) {
		final String old = name;
		name = name.replaceAll("^(.+?)\\.", "");
		for (final String string : Utility.fixes) {
			name = name.replaceAll("^" + string, "");
			name = name.replaceAll(string + "$", "");
		}
		if ("aluminum".equalsIgnoreCase(name))
			name = "aluminium";
		name = name.toLowerCase();
		return name;
	}

	public static int findHighestBlock(final IBlockAccess ba, final Chunk chunk) {
		for (int y = chunk.getTopFilledSegment() + 16; y > 0; y--)
			for (int x = 0; x < 16; x++)
				for (int z = 0; z < 16; z++)
					if (ba.getBlock(chunk.xPosition * 16 + x, y, chunk.zPosition * 16 + z) != Blocks.air)
						return y;
		return chunk.getTopFilledSegment() + 16;
	}

	public static List<ItemStack> getOreDictStacks(final Metal metal) {
		final List<ItemStack> list = new ArrayList<ItemStack>();
		for (final Component component : metal.getComponents())
			list.addAll(OreDictionary.getOres(camelCase("ore", component.name)));
		return list;
	}

	public static int hashChunk(final Chunk chunk) {
		return (chunk.xPosition >> 8 ^ chunk.xPosition) & 0xFFFF | ((chunk.zPosition >> 8 ^ chunk.zPosition) & 0xFFFF) << 16;
	}

	public static boolean isCommonBlock(final Block block, final int meta) {
		if (block == null)
			return false;
		for (final List<String> entry : commonCache.values()) {
			if (entry == null || entry.isEmpty())
				continue;
			if (entry.contains(block.getUnlocalizedName() + ":" + meta))
				return true;
		}
		return false;
	}

	public static boolean isCommonName(final String name) {
		return Utility.commonList.contains(Utility.cleanName(name));
	}

	public static final List<String> commonList = Arrays.asList(new String[] {
			"coal", "aluminium", "zinc", "copper", "tin", "lead", "iron", "nickel", "tungsten", "silver", "gold", "titanium", "platinum", "brass", "bronze", "steel", "invar", "electrum"
	});
	private static Map<String, List<String>> commonCache = new HashMap<String, List<String>>();
	public static final String[] fixes = {
			"ore", "dust", "pulv(erized*)*", "block", "ingot", "nugget", "storage", "compress(ed)*"
	};
}
