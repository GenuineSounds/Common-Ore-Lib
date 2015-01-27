package com.genuineflix.co.events;

import java.lang.reflect.Field;

import micdoodle8.mods.galacticraft.core.world.gen.WorldGenMinableMeta;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.OreGenEvent;

import com.genuineflix.co.generator.feature.CommonGenMinable;
import com.genuineflix.co.registry.MetalRegistry;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreGenerationEvent {

	public static Field blockWorldGenMinableField;
	public static Field blockWorldGenMinableMetaField;
	public static Field metaWorldGenMinableField;
	public static Field metaWorldGenMinableMetaField;
	public static Boolean enabledWorldGenMinable = true;
	public static Boolean enabledWorldGenMinableMeta = true;

	@SubscribeEvent
	public void minable(final OreGenEvent.GenerateMinable event) {
		if (event.generator == null || event.generator instanceof CommonGenMinable)
			return;
		if (enabledWorldGenMinable && event.generator instanceof WorldGenMinable) {
			try {
				final Block block = (Block) blockWorldGenMinableField.get(event.generator);
				if (block != null && MetalRegistry.isCommon(block))
					event.setResult(Result.DENY);
			}
			catch (final Exception e) {}
		} else if (enabledWorldGenMinableMeta && event.generator instanceof WorldGenMinableMeta) {
			try {
				final Block block = (Block) blockWorldGenMinableField.get(event.generator);
				if (block != null && MetalRegistry.isCommon(block))
					event.setResult(Result.DENY);
			}
			catch (final Exception e) {}
		}
	}

	static {
		try {
			blockWorldGenMinableField = WorldGenMinable.class.getDeclaredFields()[0];
			blockWorldGenMinableField.setAccessible(true);
		}
		catch (Exception e) {
			enabledWorldGenMinable = false;
		}
		try {
			metaWorldGenMinableField = WorldGenMinable.class.getDeclaredFields()[4];
			metaWorldGenMinableField.setAccessible(true);
		}
		catch (Exception e) {}
		try {
			blockWorldGenMinableMetaField = WorldGenMinableMeta.class.getDeclaredFields()[0];
			blockWorldGenMinableMetaField.setAccessible(true);
		}
		catch (Exception e) {
			enabledWorldGenMinableMeta = false;
		}
		try {
			metaWorldGenMinableMetaField = WorldGenMinableMeta.class.getDeclaredFields()[3];
			metaWorldGenMinableMetaField.setAccessible(true);
		}
		catch (Exception e) {}
	}
}
