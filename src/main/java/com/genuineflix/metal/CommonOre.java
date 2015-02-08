package com.genuineflix.metal;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.genuineflix.metal.config.Config;
import com.genuineflix.metal.event.OreGenerationEvent;
import com.genuineflix.metal.generator.GeneratorAlloyOre;
import com.genuineflix.metal.generator.GeneratorFlatBedrock;
import com.genuineflix.metal.generator.GeneratorStandardOre;
import com.genuineflix.metal.item.MagicWand;
import com.genuineflix.metal.registry.MetalRegistry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = CommonOre.MODID, name = CommonOre.NAME, version = CommonOre.VERSION)
public class CommonOre {

	@Instance(CommonOre.MODID)
	public static CommonOre instance;
	public static final String MODID = "CommonOre";
	public static final String NAME = "CommonOre";
	public static final String VERSION = "1.0.12";
	public static final Logger log = LogManager.getLogger(MODID);
	public static Config config;

	public CommonOre() {
		MinecraftForge.EVENT_BUS.register(MetalRegistry.instance);
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		MagicWand.wand = new MagicWand();
		GameRegistry.registerItem(MagicWand.wand, "magicWand");
		MetalRegistry.instance.init();
		CommonOre.config.init();
		if (CommonOre.config.flatBedrock)
			GameRegistry.registerWorldGenerator(new GeneratorFlatBedrock(), Integer.MIN_VALUE);
		GameRegistry.registerWorldGenerator(new GeneratorStandardOre(), 5000);
		if (CommonOre.config.genAlloys)
			GameRegistry.registerWorldGenerator(new GeneratorAlloyOre(CommonOre.config.rareAlloys, CommonOre.config.searchRadius), 5001);
	}

	@EventHandler
	public void post(final FMLPostInitializationEvent event) {
		MetalRegistry.instance.post();
		CommonOre.config.post();
	}

	@EventHandler
	public void pre(final FMLPreInitializationEvent event) {
		CommonOre.config = new Config(event);
		MinecraftForge.ORE_GEN_BUS.register(new OreGenerationEvent());
		MetalRegistry.instance.pre();
		CommonOre.config.pre();
	}
}
