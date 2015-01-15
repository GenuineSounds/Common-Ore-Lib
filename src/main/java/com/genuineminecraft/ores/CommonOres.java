package com.genuineminecraft.ores;

import com.genuineminecraft.ores.config.Config;
import com.genuineminecraft.ores.generator.GeneratorAlloyOre;
import com.genuineminecraft.ores.generator.GeneratorCommonOre;
import com.genuineminecraft.ores.generator.GeneratorFlatBedrock;
import com.genuineminecraft.ores.items.MagicWand;
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
	public void pre(FMLPreInitializationEvent event) {
		config = new Config(event);
		MetalRegistry.getInstance().preInitialize();
		config.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MagicWand.instance = new MagicWand();
		GameRegistry.registerItem(MagicWand.instance, "magicWand");
		MetalRegistry.getInstance().initialize();
		config.init();
		if (config.flatBedrock)
			GameRegistry.registerWorldGenerator(new GeneratorFlatBedrock(), 0);
		GameRegistry.registerWorldGenerator(new GeneratorCommonOre(config.rareAlloys, config.searchRadius), 5000);
		if (config.genAlloys)
			GameRegistry.registerWorldGenerator(new GeneratorAlloyOre(config.rareAlloys, config.searchRadius), 5001);
	}

	@EventHandler
	public void post(FMLPostInitializationEvent event) {
		MetalRegistry.getInstance().postInitialize();
		config.postInit();
	}
}
