package com.genuineflix.co.utils;

import gnu.trove.map.hash.TIntIntHashMap;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.genuineflix.co.metals.Metal;
import com.genuineflix.co.registry.MetalRegistry;

public class Utility {

	public static TIntIntHashMap cachedHeight = new TIntIntHashMap();
	public static ChunkMap<List<Metal>> metalsGenerated = new ChunkMap<List<Metal>>();

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

	public static boolean presentInChunk(final Metal metal, final Chunk chunk) {
		if (!Utility.metalsGenerated.contains(chunk))
			return false;
		if (Utility.metalsGenerated.get(chunk).contains(metal))
			return true;
		return false;
	}

	public static int hashChunk(final Chunk chunk) {
		return (chunk.xPosition >> 8 ^ chunk.xPosition) & 0xFFFF | ((chunk.zPosition >> 8 ^ chunk.zPosition) & 0xFFFF) << 16;
	}

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
					if (block == MetalRegistry.instance.getCommon(metal.getPrimaryComponent()).ore)
						foundPrimary = true;
					if (block == MetalRegistry.instance.getCommon(metal.getSecondaryComponent()).ore)
						foundSecondary = true;
					if (foundPrimary && foundSecondary)
						return true;
				}
		return false;
	}
}
