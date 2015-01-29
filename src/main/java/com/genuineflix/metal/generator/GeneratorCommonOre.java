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

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
		if (world.provider.dimensionId != 0)
			return;
		final Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		final int yMax = Utility.findHighestBlock(world, chunk);
		for (final Metal metal : MetalRegistry.instance.getGeneratedMetals()) {
			if (metal.isAlloy())
				continue;
			final int nodes = metal.getNodesPerChunk() + 1;
			final CommonGenMinable gen = new CommonGenMinable(metal.ore, metal.getNodeSize());
			for (int i = 0; i < yMax / 64F * nodes; i++) {
				if (metal.getChunkRarity() < random.nextDouble())
					continue;
				final int y = (int) (((float) random.nextGaussian() - 0.5F) * metal.getSpread() * yMax + metal.getDepth() * yMax);
				if (y < 0)
					continue;
				Utility.cacheGeneration(metal, chunk);
				gen.generate(world, random, chunkX * 16 + random.nextInt(16), y, chunkZ * 16 + random.nextInt(16));
			}
		}
	}
}
