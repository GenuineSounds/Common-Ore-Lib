package com.genuineminecraft.ores;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineminecraft.ores.blocks.Ore;
import com.genuineminecraft.ores.blocks.Storage;
import com.genuineminecraft.ores.config.Config;
import com.genuineminecraft.ores.generator.AlloyOreWorldGenerator;
import com.genuineminecraft.ores.generator.FlatBedrockWorldGenerator;
import com.genuineminecraft.ores.generator.StandardOreWorldGenerator;
import com.genuineminecraft.ores.items.Dust;
import com.genuineminecraft.ores.items.Ingot;
import com.genuineminecraft.ores.items.Nugget;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = CommonOres.MODID, name = CommonOres.NAME, version = CommonOres.VERSION)
public class CommonOres {

	public static final String MODID = "CommonOres";
	public static final String NAME = "[Genuine] Common Ores";
	public static final String VERSION = "1.7.10-r1";
	@Instance("CommonOres")
	public static CommonOres instance;
	public static Config config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Config(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// World Generator
		if (config.flatBedrock)
			GameRegistry.registerWorldGenerator(new FlatBedrockWorldGenerator(), 0);
		GameRegistry.registerWorldGenerator(new StandardOreWorldGenerator(), 30);
		if (config.genAlloys)
			GameRegistry.registerWorldGenerator(new AlloyOreWorldGenerator(config.rareAlloys, config.searchRadius), 50);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
}
