package com.genuineminecraft.ores.utils;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.genuineminecraft.ores.metals.Metal;
import com.genuineminecraft.ores.registry.MetalRegistry;

public class Utility {

	public static TIntIntHashMap cachedHeight = new TIntIntHashMap();
	public static TIntObjectHashMap<List<Metal>> metalsGenerated = new TIntObjectHashMap<List<Metal>>();

	public static int findHighestBlock(IBlockAccess ba, Chunk chunk) {
		int hash = hashChunk(chunk);
		if (cachedHeight.contains(hash))
			return cachedHeight.get(hash);
		for (int y = chunk.getTopFilledSegment() + 16; y > 0; y--) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					if (ba.getBlock(chunk.xPosition * 16 + x, y, chunk.zPosition * 16 + z) != Blocks.air) {
						cachedHeight.put(hash, y);
						return y;
					}
				}
			}
		}
		return chunk.getTopFilledSegment() + 16;
	}

	public static void cacheGeneration(Metal metal, Chunk chunk) {
		int hash = hashChunk(chunk);
		if (!metalsGenerated.contains(hash)) {
			List<Metal> list = new ArrayList<Metal>();
			list.add(metal);
			metalsGenerated.put(hash, list);
		} else {
			List<Metal> list = metalsGenerated.get(hash);
			if (!list.contains(metal)) {
				list.add(metal);
				metalsGenerated.put(hash, list);
			}
		}
	}

	public static boolean presentInChunk(Metal metal, Chunk chunk) {
		int hash = hashChunk(chunk);
		if (!metalsGenerated.contains(hash))
			return false;
		if (metalsGenerated.get(hash).contains(metal))
			return true;
		return false;
	}

	public static int hashChunk(Chunk chunk) {
		return (((chunk.xPosition >> 8) ^ chunk.xPosition) & 0xFFFF) | ((((chunk.zPosition >> 8) ^ chunk.zPosition) & 0xFFFF) << 16);
	}

	public static boolean areComponentsFound(Metal metal, World world, int x, int y, int z, int radius) {
		if (!metal.isAlloy())
			return false;
		boolean foundPrimary = false;
		boolean foundSecondary = false;
		int count = 0;
		for (int xd = -radius; xd <= radius; xd++)
			for (int yd = -radius; yd <= radius; yd++)
				for (int zd = -radius; zd <= radius; zd++) {
					if (!world.blockExists(x + xd, y + yd, z + zd))
						continue;
					Block block = world.getBlock(x + xd, y + yd, z + zd);
					if (MetalRegistry.getCommon(metal.getPrimaryComponent()).ore == block)
						foundPrimary = true;
					if (MetalRegistry.getCommon(metal.getSecondaryComponent()).ore == block)
						foundSecondary = true;
					if (foundPrimary && foundSecondary)
						return true;
				}
		return false;
	}
}
