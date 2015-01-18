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
		if (MetalRegistry.instance == null)
			MetalRegistry.instance = new MetalRegistry();
		return MetalRegistry.instance;
	}

	public static List<Metal> getMetals() {
		return MetalRegistry.instance.metals;
	}

	public static boolean isCommon(String name) {
		return MetalRegistry.instance.metalMap.containsKey(MetalRegistry.cleanName(name));
	}

	public static String cleanName(String name) {
		name = name.replaceAll("^(.+?)\\.", "");
		name = name.replaceAll("^ore|ore$", "");
		name = name.replaceAll("^dust|dust$", "");
		name = name.replaceAll("^pulv|pulv$", "");
		name = name.replaceAll("^ingot|ingot$", "");
		name = name.replaceAll("^nugget|nugget$", "");
		name = name.replaceAll("^storage|storage$", "");
		return name.toLowerCase();
	}

	public static Metal getCommon(String name) {
		return MetalRegistry.instance.metalMap.get(MetalRegistry.cleanName(name));
	}

	private List<Metal> metals = new ArrayList<Metal>();
	private Map<String, Metal> metalMap = new HashMap<String, Metal>();

	public void preInitialize() {
		registerOre("aluminium", 1F, 1F, 6, 6, 0.2F, 3F, 2F);
		registerOre("zinc", 1F, 0.9375F, 6, 6, 0.2F, 2.5F, 2F);
		registerOre("copper", 1F, 0.8125F, 6, 6, 0.2F, 3F, 2F);
		registerOre("tin", 0.8F, 0.6875F, 6, 6, 0.2F, 1.5F, 2F);
		registerOre("lead", 0.8F, 0.625F, 6, 6, 0.2F, 1.5F, 2F);
		registerOre("iron", 0.8F, 0.5F, 6, 6, 0.2F, 4F, 2F);
		registerOre("nickel", 0.6F, 0.375F, 6, 6, 0.2F, 4F, 2F);
		registerOre("tungsten", 0.6F, 0.3125F, 6, 6, 0.2F, 7.5F, 2F);
		registerOre("silver", 0.6F, 0.25F, 6, 6, 0.2F, 2.5F, 2F);
		registerOre("gold", 0.4F, 0.125F, 6, 6, 0.2F, 2.75F, 2F);
		registerOre("titanium", 0.4F, 0.0625F, 6, 6, 0.2F, 6F, 2F);
		registerOre("platinum", 0.4F, 0F, 6, 6, 0.2F, 3.5F, 2F);
		registerAlloy("brass", "copper", "zinc", 1F, 0.875F, 6, 6, 0.2F, 3.5F, 2F);
		registerAlloy("bronze", "copper", "tin", 0.8F, 0.75F, 6, 6, 0.2F, 3F, 2F);
		registerAlloy("steel", "coal", "iron", 0.8F, 0.5625F, 6, 6, 0.2F, 7.25F, 2F);
		registerAlloy("invar", "nickel", "iron", 0.6F, 0.4375F, 6, 6, 0.2F, 4F, 2F);
		registerAlloy("electrum", "gold", "silver", 0.4F, 0.1875F, 6, 6, 0.2F, 2.5F, 2F);
		ImmutableList<IMCMessage> list = FMLInterModComms.fetchRuntimeMessages(CommonOres.instance);
		List<NBTTagCompound> metals = new ArrayList<NBTTagCompound>();
		List<NBTTagCompound> alloys = new ArrayList<NBTTagCompound>();
		for (IMCMessage imc : list) {
			if (imc.key.equalsIgnoreCase("commonMetal"))
				metals.add(imc.getNBTValue());
			if (imc.key.equalsIgnoreCase("commonAlloy"))
				alloys.add(imc.getNBTValue());
		}
		for (NBTTagCompound tag : metals)
			try {
				registerOre(tag.getString("name"), tag.getFloat("rarity"), tag.getFloat("depth"), tag.getInteger("nodes"), tag.getInteger("size"), tag.getFloat("spread"), tag.getFloat("hardness"), tag.getFloat("resistance"));
			}
			catch (Exception e) {
				System.err.println("CommonOre has detected a badly formatted metal registration.");
			}
		for (NBTTagCompound tag : alloys)
			try {
				registerAlloy(tag.getString("name"), tag.getString("primary"), tag.getString("secondary"), tag.getFloat("rarity"), tag.getFloat("depth"), tag.getInteger("nodes"), tag.getInteger("size"), tag.getFloat("spread"), tag.getFloat("hardness"), tag.getFloat("resistance"));
			}
			catch (Exception e) {
				System.err.println("CommonOre has detected a badly formatted alloy registration.");
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

	public void registerAlloy(String name, String primary, String secondary, float chunkRarity, float depth, int nodesPerChunk, int nodeSize, float spread, float hardness, float resistance) {
		Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metal.setComponents(primary, secondary);
		metals.add(metal);
		metalMap.put(name, metal);
	}

	public void registerOre(String name, float chunkRarity, float depth, int nodesPerChunk, int nodeSize, float spread, float hardness, float resistance) {
		Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metals.add(metal);
		metalMap.put(metal.name, metal);
	}
}
