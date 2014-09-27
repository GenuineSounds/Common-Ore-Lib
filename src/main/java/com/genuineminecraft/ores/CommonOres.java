package com.genuineminecraft.ores;

import net.minecraftforge.common.MinecraftForge;

import com.genuineminecraft.ores.config.Config;
import com.genuineminecraft.ores.events.OreEvents;
import com.genuineminecraft.ores.generator.GeneratorAlloyOre;
import com.genuineminecraft.ores.generator.GeneratorCommonOre;
import com.genuineminecraft.ores.generator.GeneratorFlatBedrock;
import com.genuineminecraft.ores.registry.MetalRegistry;

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
	public static final String VERSION = "1.7.10-r3";
	public static Config config;

	@EventHandler
	public void preInitialize(FMLPreInitializationEvent event) {
		config = new Config(event);
		MetalRegistry.getInstance().preInitialize();
		config.preInit();
	}

	@EventHandler
	public void initialize(FMLInitializationEvent event) {
		MetalRegistry.getInstance().initialize();
		config.init();
		if (config.flatBedrock)
			GameRegistry.registerWorldGenerator(new GeneratorFlatBedrock(), 0);
		GameRegistry.registerWorldGenerator(new GeneratorCommonOre(), 30);
		if (config.genAlloys)
			GameRegistry.registerWorldGenerator(new GeneratorAlloyOre(config.rareAlloys, config.searchRadius), 50);
	}

	@EventHandler
	public void postInitialize(FMLPostInitializationEvent event) {
		MetalRegistry.getInstance().postInitialize();
		config.postInit();
		MinecraftForge.ORE_GEN_BUS.register(new OreEvents());
	}
}
