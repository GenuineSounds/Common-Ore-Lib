package ninja.genuine.metal;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import ninja.genuine.metal.config.Config;
import ninja.genuine.metal.event.OreGenerationEvent;
import ninja.genuine.metal.event.OreRegistrationEvent;
import ninja.genuine.metal.generator.GeneratorAlloyOre;
import ninja.genuine.metal.generator.GeneratorFlatBedrock;
import ninja.genuine.metal.generator.GeneratorStandardOre;
import ninja.genuine.metal.item.MagicWand;
import ninja.genuine.metal.registry.MetalRegistry;
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
	public static final String VERSION = "1.0.21";
	public static final CreativeTabs COMMON_TAB = new CreativeTabs(CommonOre.NAME) {

		@Override
		public Item getTabIconItem() {
			return MetalRegistry.TUNGSTEN.getIngot();
		}
	};
	public static Config config;

	public CommonOre() {
		MinecraftForge.EVENT_BUS.register(new OreRegistrationEvent());
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
		MinecraftForge.ORE_GEN_BUS.register(new OreGenerationEvent());
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
