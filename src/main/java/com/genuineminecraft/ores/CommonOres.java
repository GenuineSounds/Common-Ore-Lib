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
	public static final String VERSION = "1.6";
	@Instance("CommonOres")
	public static CommonOres instance;
	public static Config config;
	public static List<String> commonList = Arrays.asList(
	// Tier 5
			"platinum", "titanium", "gold",
			// Tier 4
			"electrum", "silver", "tungsten",
			// Tier 3
			"nickel", "invar", "iron",
			// Tier 2
			"steel", "lead", "tin", "bronze",
			// Tier 1
			"copper", "brass", "zinc", "aluminium");
	public static List<String> alloyList = Arrays.asList("electrum", "invar", "steel", "bronze", "brass");
	public static Map<String, Dust> dustMap = new HashMap<String, Dust>();
	public static Map<String, Ingot> ingotMap = new HashMap<String, Ingot>();
	public static Map<String, Nugget> nuggetMap = new HashMap<String, Nugget>();
	public static Map<String, Storage> storageMap = new HashMap<String, Storage>();
	public static Map<String, Ore> oreMap = new HashMap<String, Ore>();
	public static boolean flatBedrock;
	public static boolean genAlloys;
	public static boolean rareAlloys;
	public static int searchRadius;

	public static Block getOreByName(String name) {
		Block out = oreMap.get(cleanName(name));
		if (out == null)
			out = GameRegistry.findBlock("Minecraft", name);
		return out;
	}

	public static Item getDustByName(String name) {
		Item out = dustMap.get(cleanName(name));
		if (out == null)
			out = GameRegistry.findItem("Minecraft", name);
		return out;
	}

	public static Item getIngotByName(String name) {
		Item out = ingotMap.get(cleanName(name));
		if (out == null)
			out = GameRegistry.findItem("Minecraft", name);
		return out;
	}

	public static Item getNuggetByName(String name) {
		Item out = nuggetMap.get(cleanName(name));
		if (out == null)
			out = GameRegistry.findItem("Minecraft", name);
		return out;
	}

	public static Block getBlockByName(String name) {
		Block out = storageMap.get(cleanName(name));
		if (out == null)
			out = GameRegistry.findBlock("Minecraft", name);
		return out;
	}

	public static String cleanName(String name) {
		if (name.startsWith("ore"))
			name = name.substring(3);
		else if (name.startsWith("dust"))
			name = name.substring(4);
		else if (name.startsWith("pulv"))
			name = name.substring(4);
		else if (name.startsWith("ingot"))
			name = name.substring(5);
		else if (name.startsWith("block"))
			name = name.substring(5);
		else if (name.startsWith("nugget"))
			name = name.substring(6);
		return name;
	}

	public static void createLang() {
		System.out.println();
		for (String name : commonList) {
			String nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			System.out.println("tile.ore" + nameFixed + ".name=" + nameFixed + " Ore");
		}
		System.out.println();
		for (String name : commonList) {
			String nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			System.out.println("item.dust" + nameFixed + ".name=Pulverized " + nameFixed);
		}
		System.out.println();
		for (String name : commonList) {
			String nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			System.out.println("item.ingot" + nameFixed + ".name=" + nameFixed + " Ingot");
		}
		System.out.println();
		for (String name : commonList) {
			String nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			System.out.println("item.nugget" + nameFixed + ".name=" + nameFixed + " Nugget");
		}
		System.out.println();
		for (String name : commonList) {
			String nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			System.out.println("tile.block" + nameFixed + ".name=" + nameFixed + " Block");
		}
		System.out.println();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.instance = this;
		File folder = new File(event.getModConfigurationDirectory() + "/" + MODID);
		folder.mkdirs();
		Configuration oreGenCfg = new Configuration(new File(folder, MODID + " - Ore Generation.cfg"));
		Configuration mainCfg = new Configuration(new File(folder, MODID + " - Main.cfg"));
		config = new Config();
		for (int i = 0; i < commonList.size(); i++) {
			String name = commonList.get(i);
			int depth = (int) (((1D / (double) commonList.size()) * 64D) * i);
			config.nodesPerChunkMap.put(name, oreGenCfg.getInt("nodesPerChunk", name, 4, 0, 8, "How many nodeshave a chance to generate in a chunk"));
			config.chunkRarityMap.put(name, oreGenCfg.getInt("chunkRarity", name, 100, 0, 100, "Chance percent of generating in any chunk"));
			config.nodeSizeMap.put(name, oreGenCfg.getInt("nodeSize", name, 6, 0, 16, "Node size"));
			config.depthMap.put(name, oreGenCfg.getInt("depth", name, depth, 0, 256, "Depth at which the ore is most common"));
			config.spreadMap.put(name, oreGenCfg.getInt("spread", name, 6, 0, 256, "How far can the ore deviate from its depth"));
		}
		// Main Config
		flatBedrock = mainCfg.getBoolean("flatBedrock", "Options", true, "Generate flat bedrock in the Overworld and nether.");
		genAlloys = mainCfg.getBoolean("genAlloys", "Options", true, "Generate alloy ores in world. Might break balance if rare alloys is not enabled. (recommended)");
		rareAlloys = mainCfg.getBoolean("rareAlloys", "Options", true, "Generation of alloy ores only occur when the two major component of the ore appear close together. (recommended)");
		searchRadius = mainCfg.getInt("searchRadius", "Options", 5, 0, 16, "Radius for rare alloys to search in each direction to find their component ores");
		oreGenCfg.save();
		mainCfg.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		for (String name : commonList) {
			String nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			Ore ore = new Ore(nameFixed);
			Dust dust = new Dust(nameFixed);
			Ingot ingot = new Ingot(nameFixed);
			Nugget nugget = new Nugget(nameFixed);
			Storage storage = new Storage(nameFixed);
			ore.setup(config.chunkRarityMap.get(name), config.nodesPerChunkMap.get(name), config.nodeSizeMap.get(name), config.depthMap.get(name), config.spreadMap.get(name));
			oreMap.put(name, ore);
			dustMap.put(name, dust);
			ingotMap.put(name, ingot);
			nuggetMap.put(name, nugget);
			storageMap.put(name, storage);
			GameRegistry.registerBlock(ore, ore.getName());
			GameRegistry.registerItem(dust, dust.getName());
			GameRegistry.registerItem(ingot, ingot.getName());
			GameRegistry.registerItem(nugget, nugget.getName());
			GameRegistry.registerBlock(storage, storage.getName());
			OreDictionary.registerOre(ore.getName(), ore);
			OreDictionary.registerOre(dust.getName(), dust);
			OreDictionary.registerOre("pulv" + nameFixed, dust);
			OreDictionary.registerOre(ingot.getName(), ingot);
			OreDictionary.registerOre(nugget.getName(), nugget);
			OreDictionary.registerOre(storage.getName(), storage);
			GameRegistry.addSmelting(ore, new ItemStack(ingot), 10);
			GameRegistry.addSmelting(dust, new ItemStack(ingot), 10);
			GameRegistry.addShapelessRecipe(new ItemStack(nugget, 9), ingot);
			GameRegistry.addShapelessRecipe(new ItemStack(storage), ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot);
			GameRegistry.addShapelessRecipe(new ItemStack(ingot), nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget);
			GameRegistry.addShapelessRecipe(new ItemStack(ingot, 9), storage);
		}
		for (int i = 0; i < commonList.size(); i++) {
			String name = commonList.get(i);
			if (alloyList.contains(name))
				oreMap.get(name).setComponents(oreMap.get(commonList.get(i - 1)), oreMap.get(commonList.get(i + 1)), null);
		}
		// World Generator
		if (flatBedrock)
			GameRegistry.registerWorldGenerator(new FlatBedrockWorldGenerator(), 0);
		GameRegistry.registerWorldGenerator(new StandardOreWorldGenerator(), 30);
		if (genAlloys)
			GameRegistry.registerWorldGenerator(new AlloyOreWorldGenerator(rareAlloys, searchRadius), 50);
		for (Entry<String, Storage> entry : storageMap.entrySet()) {
			ItemStack stack = new ItemStack(entry.getValue());
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", stack);
		}
		for (Entry<String, Ore> entry : oreMap.entrySet()) {
			ItemStack stack = new ItemStack(entry.getValue());
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", stack);
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
}
