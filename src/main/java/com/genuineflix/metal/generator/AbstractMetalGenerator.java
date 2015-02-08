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

public abstract class AbstractMetalGenerator implements IWorldGenerator {

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
		final Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		final int chunkLevel = Utility.findGroundLevel(chunk, Utility.IS_GROUND_BLOCK);
		for (final Metal metal : MetalRegistry.instance.getGeneratedMetals()) {
			if (!isValidMetal(metal))
				continue;
			final int nodes = (int) Utility.generativeScaling(chunkLevel, true, metal.getProperties().nodes);
			generateMetal(world, chunk, random, metal, nodes);
		}
	}

	private void generateMetal(final World world, final Chunk chunk, final Random random, final Metal metal, final int nodes) {
		for (int i = 0; i < nodes; i++) {
			if (metal.getProperties().rarity < random.nextDouble())
				continue;
			final int rndX = random.nextInt(16);
			final int rndZ = random.nextInt(16);
			final int columnLevel = Utility.findGroundLevel(chunk, rndX, rndZ, Utility.IS_GROUND_BLOCK);
			final int posX = chunk.xPosition * 16 + rndX;
			final int posY = (int) (random.nextGaussian() * metal.getProperties().spread * columnLevel + metal.getProperties().depth * columnLevel);
			final int posZ = chunk.zPosition * 16 + rndZ;
			final CommonGenMinable node = new CommonGenMinable(metal.ore, (int) Utility.generativeScaling(columnLevel, false, metal.getProperties().size));
			if (isActionValid(world, random, posX, posY, posZ, metal))
				node.generate(world, random, posX, posY, posZ);
		}
	}

	public abstract boolean isValidMetal(final Metal metal);

	public abstract boolean isActionValid(final World world, final Random random, final int x, final int y, final int z, final Metal metal);
}
