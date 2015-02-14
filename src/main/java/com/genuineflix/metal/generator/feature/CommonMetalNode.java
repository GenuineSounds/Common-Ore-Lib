package com.genuineflix.metal.generator.feature;

import java.util.Random;

import javax.vecmath.Tuple3i;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.genuineflix.metal.registry.Metal;
import com.genuineflix.metal.util.GenerationHelper;
import com.google.common.base.Predicate;

public class CommonMetalNode extends WorldGenMinable {

	public static class NodePos extends Tuple3i {

		private static final long serialVersionUID = -7754326638558155495L;
		public final String ore;
		public final double radius;

		public NodePos(final String ore, final int x, final int y, final int z) {
			this(ore, x, y, z, 0);
		}

		public NodePos(final String ore, final int x, final int y, final int z, final double radius) {
			super(x, y, z);
			this.ore = ore;
			this.radius = radius;
		}

		public boolean areIntersecting(final NodePos pos) {
			return this.distanceTo(pos) - (radius + pos.radius) <= 0;
		}

		public boolean areIntersecting(final NodePos pos, final double extra) {
			return this.distanceTo(pos) - (radius / 2.0 + pos.radius / 2.0 + extra) <= 0;
		}

		public double distanceTo(final NodePos pos) {
			return this.distanceTo(pos.x, pos.y, pos.z);
		}

		public double distanceTo(final double x, final double y, final double z) {
			final double distanceX = this.x - x;
			final double distanceY = this.y - y;
			final double distanceZ = this.z - z;
			return Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
		}
	}

	public static enum BiomeType {
		STONE(0), NETHER(1), ENDER(2);

		public final int meta;

		private BiomeType(final int meta) {
			this.meta = meta;
		}
	}

	public final Metal metal;
	public final BiomeType type;
	public final int size;
	public final Predicate<Block> isBlockReplaceable;

	public CommonMetalNode(final Metal metal, final BiomeType type, final int size) {
		this(metal, type, size, GenerationHelper.IS_REPLACEABLE_BLOCK);
	}

	public CommonMetalNode(final Metal metal, final BiomeType type, final int size, final Predicate<Block> isBlockReplaceable) {
		super(metal.getOre(), type.meta, size, Blocks.stone);
		this.metal = metal;
		this.type = type;
		this.size = size;
		this.isBlockReplaceable = isBlockReplaceable;
	}

	@Override
	public boolean generate(final World world, final Random random, final int x, final int y, final int z) {
		boolean generated = false;
		if (!isBlockReplaceable.apply(world.getBlock(x, y, z)))
			return generated;
		final float angle = random.nextFloat() * (float) Math.PI;
		final double d0 = x + 8 + MathHelper.sin(angle) * size / 8.0F;
		final double d1 = x + 8 - MathHelper.sin(angle) * size / 8.0F;
		final double d2 = z + 8 + MathHelper.cos(angle) * size / 8.0F;
		final double d3 = z + 8 - MathHelper.cos(angle) * size / 8.0F;
		final double d4 = y + random.nextInt(3) - 2;
		final double d5 = y + random.nextInt(3) - 2;
		for (int count = 0; count <= size; ++count) {
			final double d6 = d0 + (d1 - d0) * count / size;
			final double d7 = d4 + (d5 - d4) * count / size;
			final double d8 = d2 + (d3 - d2) * count / size;
			final double d9 = random.nextDouble() * size / 16.0D;
			final double d10 = (MathHelper.sin(count * (float) Math.PI / size) + 1.0F) * d9 + 1.0D;
			final double d11 = (MathHelper.sin(count * (float) Math.PI / size) + 1.0F) * d9 + 1.0D;
			final int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
			final int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
			final int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
			final int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
			final int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
			final int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);
			for (int genX = i1; genX <= l1; ++genX) {
				final double d12 = (genX + 0.5D - d6) / (d10 / 2.0D);
				if (d12 * d12 < 1.0D)
					for (int genY = j1; genY <= i2; ++genY) {
						final double d13 = (genY + 0.5D - d7) / (d11 / 2.0D);
						if (d12 * d12 + d13 * d13 < 1.0D)
							for (int genZ = k1; genZ <= j2; ++genZ) {
								final double d14 = (genZ + 0.5D - d8) / (d10 / 2.0D);
								if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && isBlockReplaceable.apply(world.getBlock(genX, genY, genZ))) {
									world.setBlock(genX, genY, genZ, metal.getOre(), type.meta, 2);
									generated = true;
								}
							}
					}
			}
		}
		if (generated) {
			final NodePos pos = new NodePos(metal.getName(), x, y, z);
			GenerationHelper.cacheNodeGen(world.getChunkFromBlockCoords(x, z), pos);
		}
		return generated;
	}
}
