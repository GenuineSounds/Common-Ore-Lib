package com.genuineflix.metal.generator;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import com.genuineflix.metal.generator.feature.CommonGenMinable;
import com.genuineflix.metal.metals.Metal;
import com.genuineflix.metal.registry.MetalRegistry;
import com.genuineflix.metal.utils.Utility;

import cpw.mods.fml.common.IWorldGenerator;

public class GeneratorCommonOre implements IWorldGenerator {

	int count = 0;
	long total = 0;
	int min = 1000;
	int max = 0;

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
		if (world.provider.dimensionId != 0)
			return;
		final Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		final int yMax = Utility.findHighestBlock(world, chunk);
		for (final Metal metal : MetalRegistry.instance.getGeneratedMetals()) {
			if (metal.isAlloy())
				continue;
			final int nodes = metal.getProperties().nodes;
			final CommonGenMinable gen = new CommonGenMinable(metal.ore, metal.getProperties().size);
			for (int i = 0; i < nodes; i++) {
				if (metal.getProperties().rarity < random.nextDouble())
					continue;
				double y = (random.nextGaussian() - 0.5) * (metal.getProperties().spread * yMax);
				y += metal.getProperties().depth * yMax;
				if (y <= 1)
					continue;
				gen.generate(world, random, chunkX * 16 + random.nextInt(16), (int) y, chunkZ * 16 + random.nextInt(16));
			}
		}
	}
}
