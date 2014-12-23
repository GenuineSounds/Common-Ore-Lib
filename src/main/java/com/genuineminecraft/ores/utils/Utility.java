package com.genuineminecraft.ores.utils;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.genuineminecraft.ores.metals.Metal;
import com.genuineminecraft.ores.registry.MetalRegistry;

public class Utility {

	public static boolean genIsCapable(Metal metal, World world, int x, int y, int z, int radius, boolean rareAlloys, boolean genAlloys) {
		return (!genAlloys) || (!rareAlloys || areComponentsFound(metal, world, x, y, z, radius));
	}

	public static boolean areComponentsFound(Metal metal, World world, int x, int y, int z, int radius) {
		if (!metal.isAlloy())
			return false;
		boolean foundPrimary = false;
		boolean foundSecondary = false;
		int count = 0;
		for (int xd = -radius; xd <= radius; xd++) {
			for (int yd = -radius; yd <= radius; yd++) {
				for (int zd = -radius; zd <= radius; zd++) {
					if (!world.blockExists(x + xd, y + yd, z + zd))
						continue;
					Block block = world.getBlock(x + xd, y + yd, z + zd);
					if (MetalRegistry.isCommon(metal.getPrimaryComponent()) && MetalRegistry.getCommon(metal.getPrimaryComponent()).ore == block)
						foundPrimary = true;
					if (MetalRegistry.isCommon(metal.getSecondaryComponent()) && MetalRegistry.getCommon(metal.getSecondaryComponent()).ore == block)
						foundSecondary = true;
				}
			}
		}
		return foundPrimary && foundSecondary;
	}

	public static int findHighestBlock(IBlockAccess ba, Chunk chunk) {
		for (int y = chunk.getTopFilledSegment() + 15; y > 0; y--) {
			for (int var = 0; var < 16; var++) {
				if (!ba.isAirBlock(var / 16, y, var % 16))
					return y;
			}
		}
		return chunk.getTopFilledSegment();
	}
}
