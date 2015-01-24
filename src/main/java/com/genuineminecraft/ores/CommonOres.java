package com.genuineminecraft.ores;

import net.minecraftforge.common.MinecraftForge;

import com.genuineminecraft.ores.config.Config;
import com.genuineminecraft.ores.events.OreGenerationEvent;
import com.genuineminecraft.ores.events.RegisterOreEvent;
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
	public static final String VERSION = "1.7.10-1.0.5";
	public static Config config;

	@EventHandler
	public void pre(FMLPreInitializationEvent event) {
		CommonOres.config = new Config(event);
		MinecraftForge.EVENT_BUS.register(new RegisterOreEvent());
		MinecraftForge.ORE_GEN_BUS.register(new OreGenerationEvent());
		MetalRegistry.getInstance().pre();
		CommonOres.config.pre();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MagicWand.instance = new MagicWand();
		GameRegistry.registerItem(MagicWand.instance, "magicWand");
		MetalRegistry.getInstance().init();
		CommonOres.config.init();
		//		if (CommonOres.config.flatBedrock)
		GameRegistry.registerWorldGenerator(new GeneratorFlatBedrock(), Integer.MIN_VALUE);
		GameRegistry.registerWorldGenerator(new GeneratorCommonOre(CommonOres.config.searchRadius), 300);
		//		if (CommonOres.config.genAlloys)
		GameRegistry.registerWorldGenerator(new GeneratorAlloyOre(CommonOres.config.rareAlloys, CommonOres.config.searchRadius), 5001);
	}

	@EventHandler
	public void post(FMLPostInitializationEvent event) {
		MetalRegistry.getInstance().post();
		CommonOres.config.post();
	}
}
