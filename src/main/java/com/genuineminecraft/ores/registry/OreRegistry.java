package com.genuineminecraft.ores.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineminecraft.ores.metals.Metal;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class OreRegistry {

	private static OreRegistry instance;

	public static OreRegistry getInstance() {
		if (instance == null)
			instance = new OreRegistry();
		return instance;
	}

	public List<Metal> metals = new ArrayList<Metal>();
	public Map<String, Metal> metalMap = new HashMap<String, Metal>();

	public void preInitialize() {
		registerOre("aluminium", 1.0F, 1, 6, 4, 0.1F);
		registerOre("zinc", 1.0F, 1, 6, 4, 0.1F);
		registerOre("copper", 1.0F, 1, 6, 4, 0.1F);
		registerOre("tin", 0.8F, 1, 6, 4, 0.1F);
		registerOre("lead", 0.8F, 1, 6, 4, 0.1F);
		registerOre("iron", 0.8F, 1, 6, 4, 0.1F);
		registerOre("nickel", 0.6F, 1, 6, 4, 0.1F);
		registerOre("tungsten", 0.6F, 1, 6, 4, 0.1F);
		registerOre("silver", 0.6F, 1, 6, 4, 0.1F);
		registerOre("gold", 0.4F, 1, 6, 4, 0.1F);
		registerOre("titanium", 0.4F, 1, 6, 4, 0.1F);
		registerOre("platinum", 0.4F, 1, 6, 4, 0.1F);
		registerAlloy("brass", "copper", "zinc", 1.0F, 1, 6, 4, 0.1F);
		registerAlloy("bronze", "copper", "tin", 0.8F, 1, 6, 4, 0.1F);
		registerAlloy("steel", "coal", "iron", 0.8F, 1, 6, 4, 0.1F);
		registerAlloy("invar", "nickel", "iron", 0.6F, 1, 6, 4, 0.1F);
		registerAlloy("electrum", "gold", "silver", 0.4F, 1, 6, 4, 0.1F);
	}

	public void initialize() {
		for (Metal metal : metals) {
			GameRegistry.registerBlock(metal.ore, metal.ore.getName());
			GameRegistry.registerBlock(metal.storage, metal.storage.getName());
			GameRegistry.registerItem(metal.dust, metal.dust.getName());
			GameRegistry.registerItem(metal.ingot, metal.ingot.getName());
			GameRegistry.registerItem(metal.nugget, metal.nugget.getName());
			OreDictionary.registerOre("ore" + metal.name, metal.ore);
			OreDictionary.registerOre("storage" + metal.name, metal.storage);
			OreDictionary.registerOre("dust" + metal.name, metal.dust);
			OreDictionary.registerOre("pulv" + metal.name, metal.dust);
			OreDictionary.registerOre("ingot" + metal.name, metal.ingot);
			OreDictionary.registerOre("nugget" + metal.name, metal.nugget);
			GameRegistry.addSmelting(metal.ore, new ItemStack(metal.ingot), 10);
			GameRegistry.addSmelting(metal.dust, new ItemStack(metal.ingot), 10);
			GameRegistry.addShapelessRecipe(new ItemStack(metal.nugget, 9), metal.ingot);
			GameRegistry.addShapelessRecipe(new ItemStack(metal.storage), metal.ingot, metal.ingot, metal.ingot, metal.ingot, metal.ingot, metal.ingot, metal.ingot, metal.ingot, metal.ingot);
			GameRegistry.addShapelessRecipe(new ItemStack(metal.ingot), metal.nugget, metal.nugget, metal.nugget, metal.nugget, metal.nugget, metal.nugget, metal.nugget, metal.nugget, metal.nugget);
			GameRegistry.addShapelessRecipe(new ItemStack(metal.ingot, 9), metal.storage);
			ItemStack stack;
			stack = new ItemStack(metal.ore);
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", stack);
			stack = new ItemStack(metal.storage);
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", stack);
		}
	}

	public void postInitialize() {}

	public void registerOre(String name, float chunkRarity, float depth, int nodesPerChunk, int nodeSize, float spread) {
		Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread);
		metals.add(metal);
		metalMap.put(name, metal);
	}

	public void registerAlloy(String name, String first, String second, float chunkRarity, float depth, int nodesPerChunk, int nodeSize, float spread) {
		Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread);
		Metal primary = metalMap.get(first);
		Metal secondary = metalMap.get(second);
		metal.setComponents(primary, secondary);
		metals.add(metal);
		metalMap.put(name, metal);
	}
}
