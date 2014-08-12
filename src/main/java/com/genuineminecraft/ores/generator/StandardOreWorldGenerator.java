package com.genuineminecraft.ores.generator;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.genuineminecraft.ores.CommonOres;
import com.genuineminecraft.ores.blocks.Ore;

import cpw.mods.fml.common.IWorldGenerator;

public class StandardOreWorldGenerator implements IWorldGenerator {

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
			// genOverworld(world, random, chunkX, chunkZ, isHellBiome);
			break;
		}
	}

	private void genNether(World world, Random random, int chunkX, int chunkZ) {
	}

	private void genOverworld(World world, Random random, int chunkX, int chunkZ) {
		for (Ore ore : CommonOres.oreMap.values()) {
			if (ore.isAlloy())
				continue;
			for (int i = 0; i < ore.getNodesPerChunk(); i++) {
				if (ore.getChunkRarity() < random.nextInt(100))
					continue;
				int y = (int) ((random.nextGaussian() - 0.5) * ore.getSpread()) + ore.getDepth();
				if (y < 0)
					continue;
				int x = chunkX * 16 + random.nextInt(16);
				int z = chunkZ * 16 + random.nextInt(16);
				WorldGenMinable gen = new WorldGenMinable(ore, ore.getNodeSize());
				gen.generate(world, random, x, y, z);
			}
		}
	}

	private void genEnd(World world, Random random, int chunkX, int chunkZ) {
	}
}
