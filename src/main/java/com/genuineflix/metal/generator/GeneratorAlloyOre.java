package com.genuineflix.metal.generator;

import java.util.Random;

import net.minecraft.world.World;

import com.genuineflix.metal.registry.Metal;
import com.genuineflix.metal.util.GenerationHelper;

public class GeneratorAlloyOre extends AbstractMetalGenerator {

	private final boolean rare;
	private final int radius;

	public GeneratorAlloyOre(final boolean rare, final int radius) {
		this.rare = rare;
		this.radius = radius;
	}

	@Override
	public boolean isValidMetal(final Metal metal) {
		return metal.isComposite();
	}

	@Override
	public boolean isValidAction(final World world, final Random random, final int x, final int y, final int z, final Metal metal) {
		return !rare || GenerationHelper.areComponentsFound(world, x, y, z, metal, radius);
	}
}
