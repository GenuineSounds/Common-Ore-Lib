package com.genuineflix.metal.generator;

import java.util.Random;

import net.minecraft.world.World;

import com.genuineflix.metal.registry.Metal;

public class GeneratorStandardOre extends AbstractMetalGenerator {

	@Override
	public boolean isValidMetal(final Metal metal) {
		return !metal.isAlloy();
	}

	@Override
	public boolean isValidAction(final World world, final Random random, final int x, final int y, final int z, final Metal metal) {
		return true;
	}
}
