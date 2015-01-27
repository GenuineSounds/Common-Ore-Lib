package com.genuineflix.co.events;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.OreGenEvent;

import com.genuineflix.co.generator.feature.CommonGenMinable;
import com.genuineflix.co.registry.MetalRegistry;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreGenerationEvent {

	@SubscribeEvent
	public void minable(final OreGenEvent.GenerateMinable event) {
		if (event.generator != null && event.generator instanceof WorldGenMinable && !(event.generator instanceof CommonGenMinable))
			try {
				final WorldGenMinable gen = (WorldGenMinable) event.generator;
				final Field blockField = WorldGenMinable.class.getFields()[0];
				blockField.setAccessible(true);
				final Block block = (Block) blockField.get(event.generator);
				if (MetalRegistry.isCommon(block))
					event.setResult(Result.DENY);
			}
			catch (final Exception e) {
				e.printStackTrace();
			}
	}
}
