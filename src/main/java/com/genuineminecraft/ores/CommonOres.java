package com.genuineminecraft.ores;

import com.genuineminecraft.ores.config.Config;
import com.genuineminecraft.ores.generator.AlloyOreWorldGenerator;
import com.genuineminecraft.ores.generator.FlatBedrockWorldGenerator;
import com.genuineminecraft.ores.generator.StandardOreWorldGenerator;
import com.genuineminecraft.ores.registry.OreRegistry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = CommonOres.MODID, name = CommonOres.NAME, version = CommonOres.VERSION)
public class CommonOres {

	@Instance(CommonOres.MODID)
	public static CommonOres instance;
	public static final String MODID = "CommonOres";
	public static final String NAME = "Common Ores";
	public static final String VERSION = "1.7.10-r2";
	public static Config config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Config(event);
		OreRegistry.getInstance().preInitialize();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		OreRegistry.getInstance().initialize();
		config.load();
		if (config.flatBedrock)
			GameRegistry.registerWorldGenerator(new FlatBedrockWorldGenerator(), 0);
		GameRegistry.registerWorldGenerator(new StandardOreWorldGenerator(), 30);
		if (config.genAlloys)
			GameRegistry.registerWorldGenerator(new AlloyOreWorldGenerator(config.rareAlloys, config.searchRadius), 50);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		OreRegistry.getInstance().postInitialize();
		config.post();
	}
}
