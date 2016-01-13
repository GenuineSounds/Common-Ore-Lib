package ninja.genuine.metal.generator;

import java.util.Random;

import net.minecraft.world.World;
import ninja.genuine.metal.api.IMetal;
import ninja.genuine.metal.util.GenerationHelper;

public class GeneratorAlloyOre implements CommonGenerator {

	private final boolean rare;
	private final int radius;

	public GeneratorAlloyOre(final boolean rare, final int radius) {
		this.rare = rare;
		this.radius = radius;
	}

	@Override
	public boolean isValidMetal(final IMetal metal) {
		return metal.isComposite();
	}

	@Override
	public boolean isValidAction(final World world, final Random random, final int x, final int y, final int z, final IMetal metal) {
		return !rare || GenerationHelper.areComponentsFound(world, x, y, z, metal, radius);
	}
}
