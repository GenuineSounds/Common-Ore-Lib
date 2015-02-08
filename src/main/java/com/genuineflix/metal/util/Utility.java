package com.genuineflix.metal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.interfaces.IAlloy.Component;
import com.genuineflix.metal.registry.Metal;
import com.genuineflix.metal.registry.MetalRegistry;
import com.google.common.base.Predicate;

public class Utility {

	public static boolean areComponentsFound(final World world, final int posX, final int posY, final int posZ, final Metal metal, final int radius) {
		if (!metal.isAlloy())
			return false;
		final boolean[] componentWasFound = new boolean[metal.getComponents().length];
		for (int x = -radius; x <= radius; x++)
			for (int y = -radius; y <= radius; y++)
				for (int z = -radius; z <= radius; z++) {
					if (!world.blockExists(posX + x, posY + y, posZ + z))
						continue;
					final String tag = world.getBlock(posX + x, posY + y, posZ + z).getUnlocalizedName() + ":" + world.getBlockMetadata(posX + x, posY + y, posZ + z);
					for (int i = 0; i < metal.getComponents().length; i++) {
						final Component component = metal.getComponents()[i];
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
		CommonOre.log.warn("Caching common: " + oreDict + " = " + block.getUnlocalizedName() + ":" + meta);
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
		return name.replace("aluminum", "aluminium").toLowerCase();
	}

	public static int findGroundLevel(final Chunk chunk, final Predicate<Block> isGroundBlock) {
		for (int y = chunk.getTopFilledSegment() + 16; y > 1; y--)
			for (int x = 0; x < 16; x++)
				for (int z = 0; z < 16; z++)
					if (isGroundBlock.apply(chunk.getBlock(x, y, z)))
						return y;
		return chunk.getTopFilledSegment() + 16;
	}

	public static int findGroundLevel(final Chunk chunk, final int x, final int z, final Predicate<Block> isGroundBlock) {
		for (int y = chunk.getTopFilledSegment() + 16; y > 1; y--)
			if (isGroundBlock.apply(chunk.getBlock(x, y, z)))
				return y;
		return chunk.getTopFilledSegment() + 16;
	}

	public static double generativeScaling(final int yMax, final boolean inverse, final double number) {
		return (inverse ? yConst / yMax : yMax / yConst) * number;
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

	public static final Predicate<Block> IS_GROUND_BLOCK = new Predicate<Block>() {

		@Override
		public boolean apply(final Block input) {
			for (final Block block : Utility.GROUND_BLOCKS)
				if (Block.isEqualTo(input, block))
					return true;
			return false;
		}
	};
	public static final Predicate<Block> IS_REPLACEABLE_BLOCK = new Predicate<Block>() {

		@Override
		public boolean apply(final Block input) {
			for (final Block block : Utility.REPLACED_BLOCKS)
				if (Block.isEqualTo(input, block))
					return true;
			return false;
		}
	};
	private static final double yConst = 128D;
	private static final List<String> commonList = Arrays.asList(new String[] {
			"coal", "aluminium", "zinc", "copper", "tin", "lead", "iron", "nickel", "tungsten", "silver", "gold", "titanium", "platinum", "brass", "bronze", "steel", "invar", "electrum"
	});
	private static Map<String, List<String>> commonCache = new HashMap<String, List<String>>();
	private static final String[] fixes = {
			"ore", "dust", "pulv(erized*)*", "block", "ingot", "nugget", "storage", "compress(ed)*"
	};
	public static final Block[] REPLACED_BLOCKS = new Block[] {
			Blocks.dirt, Blocks.stone, Blocks.sand, Blocks.gravel, Blocks.netherrack, Blocks.soul_sand
	};
	public static final Block[] GROUND_BLOCKS = new Block[] {
			Blocks.grass, Blocks.dirt, Blocks.stone, Blocks.sand, Blocks.gravel, Blocks.clay, Blocks.snow, Blocks.snow_layer, Blocks.hardened_clay, Blocks.mycelium, Blocks.netherrack, Blocks.soul_sand
	};
	public static final CreativeTabs COMMON_TAB = new CreativeTabs(CommonOre.NAME) {

		@Override
		public Item getTabIconItem() {
			return MetalRegistry.instance.getMetal("tungsten").ingot;
		}
	};
}
