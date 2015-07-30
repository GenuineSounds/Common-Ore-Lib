package com.genuineflix.metal.generator;

import java.util.Random;

import com.genuineflix.metal.api.IMetal;
import com.genuineflix.metal.generator.feature.CommonMetalNode;
import com.genuineflix.metal.generator.feature.CommonMetalNode.BiomeType;
import com.genuineflix.metal.registry.MetalRegistry;
import com.genuineflix.metal.util.GenerationHelper;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public abstract class AbstractMetalGenerator implements IWorldGenerator {

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world,
			final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
		final Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		final int chunkLevel = GenerationHelper.findGroundLevel(chunk, GenerationHelper.IS_GROUND_BLOCK);
		for (final IMetal metal : MetalRegistry.getMetals()) {
			if (metal.getGeneration() == null || !metal.getGeneration().canGenerate() || !isValidMetal(metal))
				continue;
			final int nodes = (int) GenerationHelper.generativeScaling(chunkLevel, true, metal.getGeneration().nodes);
			generateMetal(world, chunk, random, metal, nodes);
		}
	}

	private void generateMetal(final World world, final Chunk chunk, final Random random, final IMetal metal,
			final int nodes) {
		for (int i = 0; i < nodes; i++) {
			if (metal.getGeneration().rarity < random.nextDouble())
				continue;
			final int rndX = random.nextInt(16);
			final int rndZ = random.nextInt(16);
			final int posX = chunk.xPosition * 16 + rndX;
			final int posZ = chunk.zPosition * 16 + rndZ;
			final BiomeGenBase biome = chunk.getBiome(new BlockPos(rndX, 64, rndZ), world.getWorldChunkManager());
			final BiomeType type;
			if (biome.fillerBlock == Blocks.netherrack || biome == BiomeGenBase.hell
					|| !world.provider.isSurfaceWorld())
				type = BiomeType.NETHER;
			else if (biome.fillerBlock == Blocks.end_stone || biome == BiomeGenBase.sky)
				type = BiomeType.ENDER;
			else
				type = BiomeType.STONE;
			final int columnLevel = GenerationHelper.findGroundLevel(chunk, rndX, rndZ, biome.topBlock);
			int posY;
			switch (type) {
			case NETHER:
				posY = (int) (random.nextGaussian() * metal.getGeneration().spread * 64
						+ metal.getGeneration().depth * 64);
				if (random.nextBoolean())
					posY = 128 - posY;
				break;
			default:
				posY = (int) (random.nextGaussian() * metal.getGeneration().spread * columnLevel
						+ metal.getGeneration().depth * columnLevel);
				break;
			}
			final CommonMetalNode node = new CommonMetalNode(metal, type,
					(int) GenerationHelper.generativeScaling(columnLevel, false, metal.getGeneration().size));
			if (isValidAction(world, random, posX, posY, posZ, metal))
				node.generate(world, random, posX, posY, posZ);
		}
	}

	public abstract boolean isValidMetal(final IMetal metal);

	public abstract boolean isValidAction(final World world, final Random random, final int x, final int y, final int z,
			final IMetal metal);
}
