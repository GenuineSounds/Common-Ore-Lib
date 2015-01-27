package com.genuineflix.co.generator.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class CommonGenMinable extends WorldGenMinable {

	public CommonGenMinable(final Block block, final int number) {
		super(block, number, Blocks.stone);
	}

	public CommonGenMinable(final Block block, final int number, final Block target) {
		super(block, number, target);
	}

	public CommonGenMinable(final Block block, final int meta, final int number, final Block target) {
		super(block, meta, number, target);
	}
}
