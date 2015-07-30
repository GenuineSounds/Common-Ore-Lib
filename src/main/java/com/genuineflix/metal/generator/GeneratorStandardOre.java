package com.genuineflix.metal.generator;

import java.util.Random;

import com.genuineflix.metal.api.IMetal;

import net.minecraft.world.World;

public class GeneratorStandardOre extends AbstractMetalGenerator {

	@Override
	public boolean isValidMetal(final IMetal metal) {
		return !metal.isComposite();
	}

	@Override
	public boolean isValidAction(final World world, final Random random, final int x, final int y, final int z,
			final IMetal metal) {
		return true;
	}
}
