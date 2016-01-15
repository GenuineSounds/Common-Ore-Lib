package ninja.genuine.metal.generator;

import java.util.Random;

import net.minecraft.world.World;
import ninja.genuine.metal.api.IMetal;

public class GeneratorStandardOre extends CommonGenerator {

	@Override
	public boolean isValidMetal(final IMetal metal) {
		return !metal.isComposite();
	}

	@Override
	public boolean isValidAction(final World world, final Random random, final int x, final int y, final int z, final IMetal metal) {
		return true;
	}
}
