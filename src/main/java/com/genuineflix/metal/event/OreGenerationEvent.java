package com.genuineflix.metal.event;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.OreGenEvent;

import com.genuineflix.metal.generator.feature.CommonMetalNode;
import com.genuineflix.metal.util.Utility;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreGenerationEvent {

	public static Field blockWorldGenMinableField;
	public static Field metaWorldGenMinableField;
	public static Boolean enabledWorldGenMinable = true;
	static {
		try {
			OreGenerationEvent.blockWorldGenMinableField = WorldGenMinable.class.getDeclaredFields()[0];
			OreGenerationEvent.blockWorldGenMinableField.setAccessible(true);
		}
		catch (final Exception e) {
			OreGenerationEvent.enabledWorldGenMinable = false;
		}
		try {
			OreGenerationEvent.metaWorldGenMinableField = WorldGenMinable.class.getDeclaredFields()[4];
			OreGenerationEvent.metaWorldGenMinableField.setAccessible(true);
		}
		catch (final Exception e) {}
	}

	@SubscribeEvent
	public void minable(final OreGenEvent.GenerateMinable event) {
		if (event.generator == null || event.generator instanceof CommonMetalNode)
			return;
		if (event.generator instanceof WorldGenMinable)
			if (OreGenerationEvent.enabledWorldGenMinable)
				try {
					final Block block = (Block) OreGenerationEvent.blockWorldGenMinableField.get(event.generator);
					if (block != null) {
						Integer meta = (Integer) OreGenerationEvent.metaWorldGenMinableField.get(event.generator);
						if (meta == null)
							meta = 0;
						if (Utility.isCommonBlock(block, meta))
							event.setResult(Result.DENY);
					}
				}
				catch (final Exception e) {}
	}
}
