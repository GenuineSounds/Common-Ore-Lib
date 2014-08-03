package com.genuineminecraft.ores.generator;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class TemplateWorldGenerator implements IWorldGenerator {

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
		}
	}

	private void genNether(World world, Random random, int chunkX, int chunkZ) {}

	private void genOverworld(World world, Random random, int chunkX, int chunkZ) {}

	private void genEnd(World world, Random random, int chunkX, int chunkZ) {}
}
