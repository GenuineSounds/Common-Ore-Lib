package com.genuineflix.co;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.genuineflix.co.config.Config;
import com.genuineflix.co.events.OreGenerationEvent;
import com.genuineflix.co.generator.GeneratorAlloyOre;
import com.genuineflix.co.generator.GeneratorCommonOre;
import com.genuineflix.co.generator.GeneratorFlatBedrock;
import com.genuineflix.co.items.MagicWand;
import com.genuineflix.co.registry.MetalRegistry;

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
	public static final String NAME = "Common Ore";
	public static final String VERSION = "1.0.8";
	public static Logger log = LogManager.getLogger(MODID);
	public static Config config;

	public CommonOre() {
		MinecraftForge.EVENT_BUS.register(MetalRegistry.instance);
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		MagicWand.instance = new MagicWand();
		GameRegistry.registerItem(MagicWand.instance, "magicWand");
		MetalRegistry.instance.init();
		CommonOre.config.init();
		if (CommonOre.config.flatBedrock)
			GameRegistry.registerWorldGenerator(new GeneratorFlatBedrock(), Integer.MIN_VALUE);
		GameRegistry.registerWorldGenerator(new GeneratorCommonOre(), 5000);
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
