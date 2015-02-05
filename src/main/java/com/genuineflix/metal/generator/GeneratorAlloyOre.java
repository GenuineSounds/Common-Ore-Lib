package com.genuineflix.metal.generator;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import com.genuineflix.metal.generator.feature.CommonGenMinable;
import com.genuineflix.metal.registry.Metal;
import com.genuineflix.metal.registry.MetalRegistry;
import com.genuineflix.metal.util.Utility;

import cpw.mods.fml.common.IWorldGenerator;

public class GeneratorAlloyOre implements IWorldGenerator {

	private final boolean rare;
	private final int radius;

	public GeneratorAlloyOre(final boolean rareAlloys, final int radius) {
		rare = rareAlloys;
		this.radius = radius;
	}

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
		final Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		final int yMax = Utility.findHighestBlock(world, chunk);
		for (final Metal metal : MetalRegistry.instance.getGeneratedMetals()) {
			if (!metal.isAlloy())
				continue;
			final CommonGenMinable gen = new CommonGenMinable(metal.ore, (int) Utility.generativeScaling(yMax, false, metal.getProperties().size));
			for (int i = 0; i < Utility.generativeScaling(yMax, true, metal.getProperties().nodes); i++) {
				if (metal.getProperties().rarity < random.nextDouble())
					continue;
				final int x = chunkX * 16 + random.nextInt(16);
				final int z = chunkZ * 16 + random.nextInt(16);
				int y = (int) ((random.nextGaussian() - 0.5) * (metal.getProperties().spread * yMax) + metal.getProperties().depth * yMax);
				if (y < 1)
					y = 1;
				if (!rare || Utility.areComponentsFound(metal, world, x, y, z, radius))
					gen.generate(world, random, x, y, z);
			}
		}
	}
}
