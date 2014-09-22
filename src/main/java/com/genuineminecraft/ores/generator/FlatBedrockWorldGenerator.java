package com.genuineminecraft.ores.generator;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class FlatBedrockWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		long time = System.currentTimeMillis();
		boolean isHellBiome = world.getBiomeGenForCoords(chunkX, chunkZ) == BiomeGenBase.hell;
		switch (world.provider.dimensionId) {
			case -1:
				genNether(world, random, chunkX, chunkZ, isHellBiome);
				break;
			case 0:
				genOverworld(world, random, chunkX, chunkZ, isHellBiome);
				break;
			case 1:
				genEnd(world, random, chunkX, chunkZ, isHellBiome);
				break;
			default:
				break;
		}
		long newTime = System.currentTimeMillis() - time;
		if (newTime > 1)
			System.out.println("Bedrock generation: " + newTime + " milliseconds");
	}

	private void genNether(World world, Random random, int chunkX, int chunkZ, boolean isHellBiome) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 121; y < 125; y++)
					if (world.getBlock(chunkX * 16 + x, y, chunkZ * 16 + z) == Blocks.bedrock)
						world.setBlock(chunkX * 16 + x, y, chunkZ * 16 + z, Blocks.netherrack, 0, 2);
				for (int y = 1; y < 6; y++)
					if (world.getBlock(chunkX * 16 + x, y, chunkZ * 16 + z) == Blocks.bedrock)
						world.setBlock(chunkX * 16 + x, y, chunkZ * 16 + z, Blocks.netherrack, 0, 2);
			}
		}
	}

	private void genOverworld(World world, Random random, int chunkX, int chunkZ, boolean isHellBiome) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				if (isHellBiome)
					for (int y = 121; y < 125; y++)
						if (world.getBlock(chunkX * 16 + x, y, chunkZ * 16 + z) == Blocks.bedrock)
							world.setBlock(chunkX * 16 + x, y, chunkZ * 16 + z, Blocks.netherrack, 0, 2);
				for (int y = 1; y < 6; y++)
					if (world.getBlock(chunkX * 16 + x, y, chunkZ * 16 + z) == Blocks.bedrock)
						world.setBlock(chunkX * 16 + x, y, chunkZ * 16 + z, isHellBiome ? Blocks.netherrack : Blocks.stone, 0, 2);
				if (world.getBlock(chunkX * 16 + x, 0, chunkZ * 16 + z) != Blocks.bedrock)
					world.setBlock(chunkX * 16 + x, 0, chunkZ * 16 + z, Blocks.bedrock, 0, 2);
			}
		}
	}

	private void genEnd(World world, Random random, int chunkX, int chunkZ, boolean isHellBiome) {}
}
