package com.genuineflix.metal.utils;

import gnu.trove.map.hash.TIntIntHashMap;

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

import com.genuineflix.metal.metals.Metal;

public class Utility {

	public static boolean areComponentsFound(final Metal metal, final World world, final int x, final int y, final int z, final int radius) {
		if (!metal.isAlloy())
			return false;
		boolean foundPrimary = false;
		boolean foundSecondary = false;
		final int count = 0;
		for (int xd = -radius; xd <= radius; xd++)
			for (int yd = -radius; yd <= radius; yd++)
				for (int zd = -radius; zd <= radius; zd++) {
					if (!world.blockExists(x + xd, y + yd, z + zd))
						continue;
					final Block block = world.getBlock(x + xd, y + yd, z + zd);
					final int meta = world.getBlockMetadata(x + xd, y + yd, z + zd);
					final ItemStack stack = new ItemStack(block, 1, meta);
					if (Utility.componentCache.containsKey(stack.getUnlocalizedName())) {
						if (Utility.componentCache.get(stack.getUnlocalizedName()).equals(metal.getPrimaryOreDict()))
							foundPrimary = true;
						if (Utility.componentCache.get(stack.getUnlocalizedName()).equals(metal.getSecondaryOreDict()))
							foundSecondary = true;
					} else {
						final ArrayList<ItemStack> priStacks = metal.getOreListFromPrimaryComponent();
						final ArrayList<ItemStack> secStacks = metal.getOreListFromSecondaryComponent();
						for (final ItemStack testStack : priStacks) {
							if (!testStack.isItemEqual(stack))
								continue;
							foundPrimary = true;
							Utility.componentCache.put(stack.getUnlocalizedName(), metal.getPrimaryOreDict());
						}
						for (final ItemStack testStack : secStacks) {
							if (!testStack.isItemEqual(stack))
								continue;
							foundSecondary = true;
							Utility.componentCache.put(stack.getUnlocalizedName(), metal.getSecondaryOreDict());
						}
					}
					if (foundPrimary && foundSecondary)
						return true;
				}
		return false;
	}

	public static void cacheCommon(final ItemStack stack) {
		Utility.commonCache.add(stack.getUnlocalizedName());
	}

	public static void cacheGeneration(final Metal metal, final Chunk chunk) {
		if (!Utility.metalsGenerated.contains(chunk)) {
			final List<Metal> list = new ArrayList<Metal>();
			list.add(metal);
			Utility.metalsGenerated.put(chunk, list);
		} else {
			final List<Metal> list = Utility.metalsGenerated.get(chunk);
			if (!list.contains(metal)) {
				list.add(metal);
				Utility.metalsGenerated.put(chunk, list);
			}
		}
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
		final int hash = Utility.hashChunk(chunk);
		if (Utility.cachedHeight.contains(hash))
			return Utility.cachedHeight.get(hash);
		for (int y = chunk.getTopFilledSegment() + 16; y > 0; y--)
			for (int x = 0; x < 16; x++)
				for (int z = 0; z < 16; z++)
					if (ba.getBlock(chunk.xPosition * 16 + x, y, chunk.zPosition * 16 + z) != Blocks.air) {
						Utility.cachedHeight.put(hash, y);
						return y;
					}
		return chunk.getTopFilledSegment() + 16;
	}

	public static String fixCamelCase(final String prefix, final String str) {
		return prefix.toLowerCase() + str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	public static int hashChunk(final Chunk chunk) {
		return (chunk.xPosition >> 8 ^ chunk.xPosition) & 0xFFFF | ((chunk.zPosition >> 8 ^ chunk.zPosition) & 0xFFFF) << 16;
	}

	public static boolean isCached(final ItemStack stack) {
		return stack != null && Utility.commonCache.contains(stack.getUnlocalizedName());
	}

	public static boolean isCommonName(final String name) {
		return Utility.commonList.contains(Utility.cleanName(name));
	}

	public static boolean presentInChunk(final Metal metal, final Chunk chunk) {
		if (!Utility.metalsGenerated.contains(chunk))
			return false;
		if (Utility.metalsGenerated.get(chunk).contains(metal))
			return true;
		return false;
	}

	public static final List<String> commonList = Arrays.asList(new String[] {
			"coal", "aluminium", "zinc", "copper", "tin", "lead", "iron", "nickel", "tungsten", "silver", "gold", "titanium", "platinum", "brass", "bronze", "steel", "invar", "electrum"
	});
	private static TIntIntHashMap cachedHeight = new TIntIntHashMap();
	private static ChunkMap<List<Metal>> metalsGenerated = new ChunkMap<List<Metal>>();
	private static Map<String, String> componentCache = new HashMap<String, String>();
	private static List<String> commonCache = new ArrayList<String>();
	public static final String[] fixes = {
			"ore", "dust", "pulv(erized*)*", "ingot", "nugget", "storage", "compress(ed)*"
	};
}
