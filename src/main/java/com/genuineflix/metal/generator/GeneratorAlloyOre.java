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

public class GeneratorAlloyOre implements IWorldGenerator {

	private final boolean rareAlloys;
	private final int radius;

	public GeneratorAlloyOre(final boolean rareAlloys, final int radius) {
		this.rareAlloys = rareAlloys;
		this.radius = radius;
	}

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
		if (world.provider.dimensionId != 0)
			return;
		final Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		final int yMax = Utility.findHighestBlock(world, chunk);
		for (final Metal metal : MetalRegistry.instance.getGeneratedMetals()) {
			if (!metal.isAlloy())
				continue;
			final int nodes = (int) (metal.getProperties().nodes / 2F) + 1;
			final CommonGenMinable gen = new CommonGenMinable(metal.ore, metal.getProperties().size);
			for (int i = 0; i < nodes; i++) {
				if (metal.getProperties().rarity < random.nextDouble())
					continue;
				double y = (random.nextGaussian() - 0.5) * (metal.getProperties().spread * yMax);
				y += metal.getProperties().depth * yMax;
				if (y <= 1)
					continue;
				final int x = chunkX * 16 + random.nextInt(16);
				final int z = chunkZ * 16 + random.nextInt(16);
				if (!rareAlloys || Utility.areComponentsFound(metal, world, x, (int) y, z, radius))
					gen.generate(world, random, x, (int) y, z);
			}
		}
	}
}
