package com.genuineminecraft.ores.generator;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.genuineminecraft.ores.metals.Metal;
import com.genuineminecraft.ores.registry.MetalRegistry;
import com.genuineminecraft.ores.utils.Utility;

import cpw.mods.fml.common.IWorldGenerator;

public class GeneratorAlloyOre implements IWorldGenerator {

	private final boolean rareAlloys;
	private final int radius;

	public GeneratorAlloyOre(boolean rareAlloys, int radius) {
		this.rareAlloys = rareAlloys;
		this.radius = radius;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.dimensionId != 0)
			return;
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		// TODO Improve speeds if possible
		int yMax = Utility.findHighestBlock(world, chunk);
		for (Metal metal : MetalRegistry.getMetals()) {
			if (!metal.isAlloy())
				continue;
			int nodes = (int) (metal.getNodesPerChunk() / 2F) + 1;
			WorldGenMinable gen = new WorldGenMinable(metal.ore, metal.getNodeSize());
			for (int i = 0; i < yMax / 64F * nodes; i++) {
				if (metal.getChunkRarity() < random.nextDouble())
					continue;
				int x = chunkX * 16 + random.nextInt(16);
				int z = chunkZ * 16 + random.nextInt(16);
				int y = (int) (((float) random.nextGaussian() - 0.5F) * metal.getSpread() * yMax + metal.getDepth() * yMax);
				if (y < 0)
					continue;
				// TODO Improve speeds if possible
				if (Utility.genIsCapable(metal, world, x, y, z, i, rareAlloys, true))
					gen.generate(world, random, x, y, z);
			}
		}
	}
}
