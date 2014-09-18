package com.genuineminecraft.ores.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineminecraft.ores.CommonOres;
import com.genuineminecraft.ores.blocks.Ore;
import com.genuineminecraft.ores.blocks.Storage;
import com.genuineminecraft.ores.items.Dust;
import com.genuineminecraft.ores.items.Ingot;
import com.genuineminecraft.ores.items.Nugget;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class OreRegistry {

	private static OreRegistry instance;

	public static OreRegistry getInstance() {
		if (instance == null)
			instance = new OreRegistry();
		return instance;
	}

	public static List<String> commonList = new ArrayList<String>();
	public static List<String> alloyList = Arrays.asList("electrum", "invar", "steel", "bronze", "brass");
	public Map<String, Dust> dustMap = new HashMap<String, Dust>();
	public Map<String, Ingot> ingotMap = new HashMap<String, Ingot>();
	public Map<String, Nugget> nuggetMap = new HashMap<String, Nugget>();
	public Map<String, Storage> storageMap = new HashMap<String, Storage>();
	public Map<String, Ore> oreMap = new HashMap<String, Ore>();

	public static void registerOre(String... oreNames) {
		for (String oreName : oreNames)
			commonList.add(oreName);
	}

	public void preinitialize() {
		registerOre("platinum", "titanium", "gold", "electrum", "silver", "tungsten", "nickel", "invar", "iron", "steel", "lead", "tin", "bronze", "copper", "brass", "zinc", "aluminium");
	}

	public void initialize() {
		for (String name : commonList) {
			String nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			Ore ore = new Ore(nameFixed);
			Dust dust = new Dust(nameFixed);
			Ingot ingot = new Ingot(nameFixed);
			Nugget nugget = new Nugget(nameFixed);
			Storage storage = new Storage(nameFixed);
			ore.setup(CommonOres.config.chunkRarityMap.get(name), CommonOres.config.nodesPerChunkMap.get(name), CommonOres.config.nodeSizeMap.get(name), CommonOres.config.depthMap.get(name), CommonOres.config.spreadMap.get(name));
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
		for (Entry<String, Storage> entry : storageMap.entrySet()) {
			ItemStack stack = new ItemStack(entry.getValue());
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", stack);
		}
		for (Entry<String, Ore> entry : oreMap.entrySet()) {
			ItemStack stack = new ItemStack(entry.getValue());
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", stack);
		}
	}

	public static Block getOreByName(String name) {
		Block out = instance.oreMap.get(cleanName(name));
		if (out == null)
			out = GameRegistry.findBlock("Minecraft", name);
		return out;
	}

	public static Item getDustByName(String name) {
		Item out = instance.dustMap.get(cleanName(name));
		if (out == null)
			out = GameRegistry.findItem("Minecraft", name);
		return out;
	}

	public static Item getIngotByName(String name) {
		Item out = instance.ingotMap.get(cleanName(name));
		if (out == null)
			out = GameRegistry.findItem("Minecraft", name);
		return out;
	}

	public static Item getNuggetByName(String name) {
		Item out = instance.nuggetMap.get(cleanName(name));
		if (out == null)
			out = GameRegistry.findItem("Minecraft", name);
		return out;
	}

	public static Block getBlockByName(String name) {
		Block out = instance.storageMap.get(cleanName(name));
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
}
