package com.genuineflix.metal.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.genuineflix.metal.api.IMetal;
import com.genuineflix.metal.generator.feature.CommonMetalNode.NodePos;
import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class GenerationHelper {

	public static int findGroundLevel(final Chunk chunk, final int x, final int z, final IBlockState groundBlock) {
		for (int y = chunk.getTopFilledSegment() + 16; y > 1; y--)
			if (Block.isEqualTo(groundBlock.getBlock(), chunk.getBlock(x, y, z)))
				return y;
		return chunk.getTopFilledSegment() + 16;
	}

	public static double generativeScaling(final int yMax, final boolean inverse, final double number) {
		return (inverse ? GenerationHelper.yConst / yMax : yMax / GenerationHelper.yConst) * number;
	}

	public static int findGroundLevel(final Chunk chunk, final int x, final int z,
			final Predicate<Block> isGroundBlock) {
		for (int y = chunk.getTopFilledSegment() + 16; y > 1; y--)
			if (isGroundBlock.apply(chunk.getBlock(x, y, z)))
				return y;
		return chunk.getTopFilledSegment() + 16;
	}

	public static int findGroundLevel(final Chunk chunk, final Predicate<Block> isGroundBlock) {
		for (int y = chunk.getTopFilledSegment() + 16; y > 1; y--)
			for (int x = 0; x < 16; x++)
				for (int z = 0; z < 16; z++)
					if (isGroundBlock.apply(chunk.getBlock(x, y, z)))
						return y;
		return chunk.getTopFilledSegment() + 16;
	}

	public static boolean areComponentsFound(final World world, final int posX, final int posY, final int posZ,
			final IMetal metal, final int radius) {
		final List<NodePos> cachedNodes = GenerationHelper
				.getNodesInChunk(world.getChunkFromBlockCoords(new BlockPos(posX, 64, posZ)));
		if (cachedNodes == null || cachedNodes.isEmpty())
			return false;
		final NodePos node = new NodePos(metal.getName(), posX, posY, posZ);
		final boolean[] foundComponents = new boolean[metal.getCompounds().size()];
		for (int ci = 0; ci < metal.getCompounds().size(); ci++) {
			final IMetal.Compound component = metal.getCompounds().get(ci);
			for (int ni = 0; ni < cachedNodes.size(); ni++) {
				final NodePos componentNode = cachedNodes.get(ni);
				if (!componentNode.ore.equals(component.metal))
					continue;
				foundComponents[ci] |= componentNode.areIntersecting(node, radius);
			}
		}
		boolean foundAll = true;
		for (int i = 0; i < foundComponents.length; i++)
			foundAll &= foundComponents[i];
		return foundAll;
	}

	static final double yConst = 128D;
	public static final Predicate<Block> IS_REPLACEABLE_BLOCK = new Predicate<Block>() {

		@Override
		public boolean apply(final Block input) {
			for (final Block block : GenerationHelper.REPLACED_BLOCKS)
				if (Block.isEqualTo(input, block))
					return true;
			return false;
		}
	};
	public static final Predicate<Block> IS_GROUND_BLOCK = new Predicate<Block>() {

		@Override
		public boolean apply(final Block input) {
			for (final Block block : GenerationHelper.GROUND_BLOCKS)
				if (Block.isEqualTo(input, block))
					return true;
			return false;
		}
	};
	static Map<Integer, List<NodePos>> genCache = new HashMap<Integer, List<NodePos>>();
	public static final Block[] REPLACED_BLOCKS = new Block[] {
			//
			Blocks.stone, Blocks.dirt, Blocks.gravel, Blocks.sandstone, Blocks.hardened_clay,
			//
			Blocks.netherrack, Blocks.soul_sand, Blocks.end_stone };
	public static final Block[] GROUND_BLOCKS = new Block[] {
			// Normal biomes.
			Blocks.stone, Blocks.grass, Blocks.dirt, Blocks.gravel,
			// Hot biomes.
			Blocks.sand,
			// Tall Biomes.
			Blocks.hardened_clay,
			// Cold biomes.
			Blocks.ice, Blocks.snow, Blocks.snow_layer,
			// Mushroom biomes.
			Blocks.mycelium,
			// Hell biomes.
			Blocks.netherrack, Blocks.soul_sand };

	public static void cacheNodeGen(final Chunk chunk, final NodePos pos) {
		final int hash = GenerationHelper.chunkHash(chunk);
		List<NodePos> list = GenerationHelper.genCache.get(hash);
		if (list == null)
			list = new ArrayList<NodePos>();
		list.add(pos);
		GenerationHelper.genCache.put(hash, list);
	}

	public static List<NodePos> getNodesInChunk(final Chunk chunk) {
		final int hash = GenerationHelper.chunkHash(chunk);
		return GenerationHelper.genCache.get(hash);
	}

	public static int chunkHash(final Chunk chunk) {
		return (chunk.xPosition >> 8 ^ chunk.xPosition) & 0xFFFF
				| ((chunk.zPosition >> 8 ^ chunk.zPosition) & 0xFFFF) << 16;
	}
}
