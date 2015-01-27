package com.genuineflix.co.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineflix.co.CommonOre;
import com.genuineflix.co.metals.Metal;
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
		return MetalRegistry.instance.completeMetalList;
	}

	public static boolean isCommon(final Block block) {
		return MetalRegistry.isCommon(block.getUnlocalizedName());
	}

	public static boolean isCommon(final String name) {
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

	public static Metal getCommon(final String name) {
		return MetalRegistry.instance.metalMap.get(MetalRegistry.cleanName(name));
	}

	private final List<Metal> completeMetalList = new ArrayList<Metal>();
	private final List<Metal> generatedMetalList = new ArrayList<Metal>();
	private final Map<String, Metal> metalMap = new HashMap<String, Metal>();
	private final List<ItemStack> oresRegistered = new ArrayList<ItemStack>();
	private final List<String> namesRegistered = new ArrayList<String>();
	private boolean available = true;

	public void pre() {
		registerOre("aluminium", 1F, 1F, 6, 6, 0.2F, 3F, 2F, true);
		registerOre("zinc", 1F, 0.9375F, 6, 6, 0.2F, 2.5F, 2F, true);
		registerOre("copper", 1F, 0.8125F, 6, 6, 0.2F, 3F, 2F, true);
		registerOre("tin", 0.8F, 0.6875F, 6, 6, 0.2F, 1.5F, 2F, true);
		registerOre("lead", 0.8F, 0.625F, 6, 6, 0.2F, 1.5F, 2F, true);
		registerOre("iron", 0.8F, 0.5F, 6, 6, 0.2F, 4F, 2F, true);
		registerOre("nickel", 0.6F, 0.375F, 6, 6, 0.2F, 4F, 2F, true);
		registerOre("tungsten", 0.6F, 0.3125F, 6, 6, 0.2F, 7.5F, 2F, true);
		registerOre("silver", 0.6F, 0.25F, 6, 6, 0.2F, 2.5F, 2F, true);
		registerOre("gold", 0.4F, 0.125F, 6, 6, 0.2F, 2.75F, 2F, true);
		registerOre("titanium", 0.4F, 0.0625F, 6, 6, 0.2F, 6F, 2F, true);
		registerOre("platinum", 0.4F, 0F, 6, 6, 0.2F, 3.5F, 2F, true);
		registerAlloy("brass", "copper", "zinc", 1F, 0.875F, 6, 6, 0.2F, 3.5F, 2F, true);
		registerAlloy("bronze", "copper", "tin", 0.8F, 0.75F, 6, 6, 0.2F, 3F, 2F, true);
		registerAlloy("steel", "coal", "iron", 0.8F, 0.5625F, 6, 6, 0.2F, 7.25F, 2F, true);
		registerAlloy("invar", "nickel", "iron", 0.6F, 0.4375F, 6, 6, 0.2F, 4F, 2F, true);
		registerAlloy("electrum", "gold", "silver", 0.4F, 0.1875F, 6, 6, 0.2F, 2.5F, 2F, true);
		final ImmutableList<IMCMessage> list = FMLInterModComms.fetchRuntimeMessages(CommonOre.instance);
		final List<NBTTagCompound> metals = new ArrayList<NBTTagCompound>();
		final List<NBTTagCompound> alloys = new ArrayList<NBTTagCompound>();
		for (final IMCMessage imc : list) {
			if (imc.key.equalsIgnoreCase("commonMetal"))
				metals.add(imc.getNBTValue());
			if (imc.key.equalsIgnoreCase("commonAlloy"))
				alloys.add(imc.getNBTValue());
		}
		for (final NBTTagCompound tag : metals)
			try {
				boolean generate = false;
				if (tag.hasKey("generate"))
					generate = tag.getBoolean("generate");
				registerOre(tag.getString("name"), tag.getFloat("rarity"), tag.getFloat("depth"), tag.getInteger("nodes"), tag.getInteger("size"), tag.getFloat("spread"), tag.getFloat("hardness"), tag.getFloat("resistance"), generate);
			}
			catch (final Exception e) {
				System.err.println("CommonOre has detected a badly formatted metal registration.");
			}
		for (final NBTTagCompound tag : alloys)
			try {
				boolean generate = false;
				if (tag.hasKey("generate"))
					generate = tag.getBoolean("generate");
				registerAlloy(tag.getString("name"), tag.getString("primary"), tag.getString("secondary"), tag.getFloat("rarity"), tag.getFloat("depth"), tag.getInteger("nodes"), tag.getInteger("size"), tag.getFloat("spread"), tag.getFloat("hardness"), tag.getFloat("resistance"), generate);
			}
			catch (final Exception e) {
				System.err.println("CommonOre has detected a badly formatted alloy registration.");
			}
	}

	public void init() {
		available = false;
		for (final Metal metal : completeMetalList) {
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
		available = true;
	}

	public void post() {
		for (final String name : namesRegistered) {
			final Metal metal = MetalRegistry.getCommon(name);
			if (metal == null) {
				System.out.println(name + " was not found to be a suitable CommoneOre");
				continue;
			}
			System.out.println(name + " was found to be a suitable CommoneOre");
			metal.setGeneration(true);
			generatedMetalList.add(metal);
		}
	}

	public void registerGeneration(final String name, final ItemStack ore) {
		if (!available)
			return;
		namesRegistered.add(name);
		oresRegistered.add(ore);
	}

	public void registerAlloy(final String name, final String primary, final String secondary, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance) {
		registerAlloy(name, primary, secondary, chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance, false);
	}

	public void registerAlloy(final String name, final String primary, final String secondary, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance, final boolean generate) {
		final Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metal.setGeneration(generate);
		metal.setComponents(primary, secondary);
		completeMetalList.add(metal);
		metalMap.put(name, metal);
	}

	public void registerOre(final String name, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance) {
		registerOre(name, chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance, false);
	}

	public void registerOre(final String name, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance, final boolean generate) {
		final Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metal.setGeneration(generate);
		completeMetalList.add(metal);
		metalMap.put(metal.name, metal);
	}
}
