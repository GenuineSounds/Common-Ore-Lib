package com.genuineminecraft.ores.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.genuineminecraft.ores.metals.Metal;

public class Utility {

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
					if (block == metal.getPrimaryComponent().ore)
						foundPrimary = true;
					if (block == metal.getSecondaryComponent().ore)
						foundSecondary = true;
				}
			}
		}
		return foundPrimary && foundSecondary;
	}

	public static int findHighestBlock(Chunk chunk) {
		for (int y = chunk.getTopFilledSegment() + 15; y > 0; y--) {
			for (int var = 0; var < 16; var++) {
				if (!chunk.getBlock(var / 16, y, var % 16).equals(Blocks.air))
					return y;
			}
		}
		return chunk.getTopFilledSegment();
	}

	public static boolean genIsCapable(Metal metal, World world, int x, int y, int z, int radius, boolean rareAlloys, boolean genAlloys) {
		return (!genAlloys) || (!rareAlloys || areComponentsFound(metal, world, x, y, z, radius));
	}
}
