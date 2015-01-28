package com.genuineflix.co.events;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.OreGenEvent;

import com.genuineflix.co.generator.feature.CommonGenMinable;
import com.genuineflix.co.utils.Utility;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreGenerationEvent {

	public static Field blockWorldGenMinableField;
	public static Field metaWorldGenMinableField;
	public static Boolean enabledWorldGenMinable = true;
//	public static Field blockWorldGenMinableMetaField;
//	public static Field metaWorldGenMinableMetaField;
//	public static Boolean enabledWorldGenMinableMeta = true;

	@SubscribeEvent
	public void minable(final OreGenEvent.GenerateMinable event) {
		if (event.generator == null || event.generator instanceof CommonGenMinable)
			return;
//		if (event.generator instanceof WorldGenMinableMeta) {
//			if (OreGenerationEvent.enabledWorldGenMinableMeta)
//				try {
//					final Block block = (Block) OreGenerationEvent.blockWorldGenMinableMetaField.get(event.generator);
//					if (block != null) {
//						Integer meta = (Integer) OreGenerationEvent.metaWorldGenMinableMetaField.get(event.generator);
//						if (meta == null)
//							meta = 0;
//						final ItemStack stack = new ItemStack(block, 1, meta);
//						if (Utility.isCached(stack))
//							event.setResult(Result.DENY);
//					}
//				}
//				catch (final Exception e) {}
//		} else
		if (event.generator instanceof WorldGenMinable)
			if (OreGenerationEvent.enabledWorldGenMinable)
				try {
					final Block block = (Block) OreGenerationEvent.blockWorldGenMinableField.get(event.generator);
					if (block != null) {
						Integer meta = (Integer) OreGenerationEvent.metaWorldGenMinableField.get(event.generator);
						if (meta == null)
							meta = 0;
						final ItemStack stack = new ItemStack(block, 1, meta);
						if (Utility.isCached(stack))
							event.setResult(Result.DENY);
					}
				}
				catch (final Exception e) {}
	}

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
//		try {
//			OreGenerationEvent.blockWorldGenMinableMetaField = micdoodle8.mods.galacticraft.core.world.gen.WorldGenMinableMeta.class.getDeclaredFields()[0];
//			OreGenerationEvent.blockWorldGenMinableMetaField.setAccessible(true);
//		}
//		catch (final Exception e) {
//			OreGenerationEvent.enabledWorldGenMinableMeta = false;
//		}
//		try {
//			OreGenerationEvent.metaWorldGenMinableMetaField = micdoodle8.mods.galacticraft.core.world.gen.WorldGenMinableMeta.class.getDeclaredFields()[2];
//			OreGenerationEvent.metaWorldGenMinableMetaField.setAccessible(true);
//		}
//		catch (final Exception e) {}
	}
}
