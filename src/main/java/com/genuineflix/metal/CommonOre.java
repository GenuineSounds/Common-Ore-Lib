package com.genuineflix.metal;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import com.genuineflix.metal.config.Config;
import com.genuineflix.metal.event.OreGenerationEvent;
import com.genuineflix.metal.event.OreRegistrationEvent;
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
	public static final String VERSION = "1.0.16";
	public static final CreativeTabs COMMON_TAB = new CreativeTabs(NAME) {

		@Override
		public Item getTabIconItem() {
			return MetalRegistry.TUNGSTEN.getIngot();
		}
	};
	public static Config config;

	public CommonOre() {
		MinecraftForge.EVENT_BUS.register(new OreRegistrationEvent());
		MinecraftForge.ORE_GEN_BUS.register(new OreGenerationEvent());
	}

	@EventHandler
	public void pre(final FMLPreInitializationEvent event) {
		CommonOre.config = new Config(event);
		MetalRegistry.pre();
		CommonOre.config.pre();
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		MetalRegistry.init();
		CommonOre.config.init();
		if (CommonOre.config.flatBedrock)
			GameRegistry.registerWorldGenerator(new GeneratorFlatBedrock(), Integer.MIN_VALUE);
		GameRegistry.registerWorldGenerator(new GeneratorStandardOre(), 5000);
		if (CommonOre.config.genAlloys)
			GameRegistry.registerWorldGenerator(new GeneratorAlloyOre(CommonOre.config.rareAlloys, CommonOre.config.searchRadius), 5001);
		MagicWand.wand = new MagicWand();
		GameRegistry.registerItem(MagicWand.wand, "magicWand");
	}

	@EventHandler
	public void post(final FMLPostInitializationEvent event) {
		MetalRegistry.post();
		CommonOre.config.post();
	}
}
