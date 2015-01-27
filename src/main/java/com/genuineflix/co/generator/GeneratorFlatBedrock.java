package com.genuineflix.co.generator;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class GeneratorFlatBedrock implements IWorldGenerator {

	private void genEnd(final World world, final Random random, final int chunkX, final int chunkZ, final boolean isHellBiome) {}

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
		final boolean isHellBiome = world.getBiomeGenForCoords(chunkX, chunkZ) == BiomeGenBase.hell;
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
		}
	}

	private void genNether(final World world, final Random random, final int chunkX, final int chunkZ, final boolean isHellBiome) {
		for (int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++) {
				for (int y = 121; y < 125; y++)
					if (world.getBlock(chunkX * 16 + x, y, chunkZ * 16 + z) == Blocks.bedrock)
						world.setBlock(chunkX * 16 + x, y, chunkZ * 16 + z, Blocks.netherrack, 0, 2);
				for (int y = 1; y < 6; y++)
					if (world.getBlock(chunkX * 16 + x, y, chunkZ * 16 + z) == Blocks.bedrock)
						world.setBlock(chunkX * 16 + x, y, chunkZ * 16 + z, Blocks.netherrack, 0, 2);
			}
	}

	private void genOverworld(final World world, final Random random, final int chunkX, final int chunkZ, final boolean isHellBiome) {
		for (int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++)
				for (int y = 1; y < 6; y++)
					if (world.getBlock(chunkX * 16 + x, y, chunkZ * 16 + z) == Blocks.bedrock)
						world.setBlock(chunkX * 16 + x, y, chunkZ * 16 + z, isHellBiome ? Blocks.netherrack : Blocks.stone, 0, 2);
	}
}
