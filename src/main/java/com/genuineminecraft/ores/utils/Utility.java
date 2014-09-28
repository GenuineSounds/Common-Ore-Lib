package com.genuineminecraft.ores.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.genuineminecraft.ores.metals.Metal;

public class Utility {

	public static boolean areComponentsFound(Metal metal, World world, int x, int y, int z, int radius) {
		boolean foundPrimary = false;
		boolean foundSecondary = false;
		int count = 0;
		for (int xd = -radius; xd <= radius; xd++) {
			for (int yd = -radius; yd <= radius; yd++) {
				for (int zd = -radius; zd <= radius; zd++) {
					if (!world.blockExists(x + xd, y + yd, z + zd))
						continue;
					Block block = world.getBlock(x + xd, y + yd, z + zd);
					if (block == metal.getPrimaryComponent().ore)
						foundPrimary = true;
					if (block == metal.getSecondaryComponent().ore)
						foundSecondary = true;
				}
			}
		}
		if (foundPrimary && foundSecondary)
			return true;
		return false;
	}

	public static int findHighestBlock(Chunk chunk) {
		int yMax = chunk.getTopFilledSegment() + 15;
		for (int y = yMax; y > 0; y--) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					if (chunk.getBlock(x, y, z).equals(Blocks.air))
						yMax = y;
					else
						return yMax;
				}
			}
		}
		return yMax;
	}

	public static boolean genIsCapable(Metal metal, World world, int x, int y, int z, int radius, boolean rareAlloys, boolean genAlloys) {
		if (genAlloys)
			return !rareAlloys || areComponentsFound(metal, world, x, y, z, radius);
		return true;
	}
}
