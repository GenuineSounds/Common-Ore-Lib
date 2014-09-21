package com.genuineminecraft.ores.generator;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.genuineminecraft.ores.metals.Metal;
import com.genuineminecraft.ores.registry.OreRegistry;

import cpw.mods.fml.common.IWorldGenerator;

public class AlloyOreWorldGenerator implements IWorldGenerator {

	private final boolean rareAlloys;
	private final int radius;

	public AlloyOreWorldGenerator(boolean rareAlloys, int radius) {
		this.rareAlloys = rareAlloys;
		this.radius = radius;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId) {
			case -1:
				genNether(world, random, chunkX, chunkZ);
				break;
			case 0:
				genOverworld(world, random, chunkX, chunkZ);
				break;
			case 1:
				genEnd(world, random, chunkX, chunkZ);
				break;
			default:
				break;
		}
	}

	private void genNether(World world, Random random, int chunkX, int chunkZ) {}

	private void genOverworld(World world, Random random, int chunkX, int chunkZ) {
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		WorldGenMinable gen = null;
		for (Metal metal : OreRegistry.getInstance().metals) {
			if (!metal.isAlloy())
				continue;
			gen = new WorldGenMinable(metal.ore, metal.getNodeSize());
			int nodes = metal.getNodesPerChunk() / 2;
			if (nodes < 1)
				nodes = 1;
			for (int i = 0; i < nodes; i++) {
				if (metal.getChunkRarity() < random.nextDouble())
					continue;
				int x = chunkX * 16 + random.nextInt(16);
				int z = chunkZ * 16 + random.nextInt(16);
				int yMax = 255;
				for (int yCheck = 255; yCheck > 0; yCheck--) {
					if (chunk.getBlock(x - chunkX * 16, yCheck, z - chunkZ * 16).equals(Blocks.air))
						yMax--;
					else
						break;
				}
				int y = (int) (((random.nextGaussian() - 0.5) * metal.getSpread() * yMax) + metal.getDepth() * yMax);
				if (y < 0)
					continue;
				if (!rareAlloys) {
					gen.generate(world, random, x, y, z);
					continue;
				}
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
					gen.generate(world, random, x, y, z);
			}
		}
	}

	private void genEnd(World world, Random random, int chunkX, int chunkZ) {}
}
