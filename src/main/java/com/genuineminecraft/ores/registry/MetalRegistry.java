package com.genuineminecraft.ores.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineminecraft.ores.CommonOres;
import com.genuineminecraft.ores.metals.Metal;
import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.registry.GameRegistry;

public class MetalRegistry {

	private static MetalRegistry instance;

	public static MetalRegistry getInstance() {
		if (instance == null)
			instance = new MetalRegistry();
		return instance;
	}

	public List<Metal> metals = new ArrayList<Metal>();
	public Map<String, Metal> metalMap = new HashMap<String, Metal>();

	public void preInitialize() {
		registerOre("aluminium", 1.0F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("zinc", 1.0F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("copper", 1.0F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("tin", 0.8F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("lead", 0.8F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("iron", 0.8F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("nickel", 0.6F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("tungsten", 0.6F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("silver", 0.6F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("gold", 0.4F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("titanium", 0.4F, 1, 6, 4, 0.1F, 5F, 1F);
		registerOre("platinum", 0.4F, 1, 6, 4, 0.1F, 5F, 1F);
		registerAlloy("brass", "copper", "zinc", 1.0F, 1, 6, 4, 0.1F, 5F, 1F);
		registerAlloy("bronze", "copper", "tin", 0.8F, 1, 6, 4, 0.1F, 5F, 1F);
		registerAlloy("steel", "coal", "iron", 0.8F, 1, 6, 4, 0.1F, 5F, 1F);
		registerAlloy("invar", "nickel", "iron", 0.6F, 1, 6, 4, 0.1F, 5F, 1F);
		registerAlloy("electrum", "gold", "silver", 0.4F, 1, 6, 4, 0.1F, 5F, 1F);
		ImmutableList<IMCMessage> list = FMLInterModComms.fetchRuntimeMessages(CommonOres.instance);
		List<NBTTagCompound> metals = new ArrayList<NBTTagCompound>();
		List<NBTTagCompound> alloys = new ArrayList<NBTTagCompound>();
		for (IMCMessage imc : list) {
			if (imc.key.equalsIgnoreCase("commonMetal"))
				metals.add(imc.getNBTValue());
			if (imc.key.equalsIgnoreCase("commonAlloy"))
				alloys.add(imc.getNBTValue());
		}
		for (NBTTagCompound tag : metals) {
			try {
				registerOre(tag.getString("name"), tag.getFloat("rarity"), tag.getFloat("depth"), tag.getInteger("nodes"), tag.getInteger("size"), tag.getFloat("spread"), tag.getFloat("hardness"), tag.getFloat("resistance"));
			} catch (Exception e) {}
		}
		for (NBTTagCompound tag : alloys) {
			try {
				registerAlloy(tag.getString("name"), tag.getString("primary"), tag.getString("secondary"), tag.getFloat("rarity"), tag.getFloat("depth"), tag.getInteger("nodes"), tag.getInteger("size"), tag.getFloat("spread"), tag.getFloat("hardness"), tag.getFloat("resistance"));
			} catch (Exception e) {}
		}
	}

	public void initialize() {
		for (Metal metal : metals) {
			GameRegistry.registerBlock(metal.ore, "ore" + metal.name);
			GameRegistry.registerItem(metal.dust, "dust" + metal.name);
			GameRegistry.registerItem(metal.ingot, "ingot" + metal.name);
			GameRegistry.registerItem(metal.nugget, "nugget" + metal.name);
			GameRegistry.registerBlock(metal.storage, "storage" + metal.name);
			OreDictionary.registerOre("ore" + metal.name, metal.ore);
			OreDictionary.registerOre("dust" + metal.name, metal.dust);
			OreDictionary.registerOre("pulv" + metal.name, metal.dust);
			OreDictionary.registerOre("ingot" + metal.name, metal.ingot);
			OreDictionary.registerOre("nugget" + metal.name, metal.nugget);
			OreDictionary.registerOre("storage" + metal.name, metal.storage);
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

	public void registerOre(String name, float chunkRarity, float depth, int nodesPerChunk, int nodeSize, float spread, float hardness, float resistance) {
		Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metals.add(metal);
		metalMap.put(metal.name, metal);
	}

	public void registerAlloy(String name, String first, String second, float chunkRarity, float depth, int nodesPerChunk, int nodeSize, float spread, float hardness, float resistance) {
		Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		Metal primary = metalMap.get(first);
		Metal secondary = metalMap.get(second);
		metal.setComponents(primary, secondary);
		metals.add(metal);
		metalMap.put(name, metal);
	}
}
