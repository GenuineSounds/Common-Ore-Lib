package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.metals.Metal;
import com.genuineflix.metal.utils.Utility;
import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MetalRegistry {

	public static final MetalRegistry instance = new MetalRegistry();
	private String[] metalNames;
	private final List<Metal> completeMetalList = new ArrayList<Metal>();
	private final List<Metal> generatedMetalList = new ArrayList<Metal>();
	private final Map<String, Metal> nameToMetal = new HashMap<String, Metal>();
	private final List<String> oresRegistered = new ArrayList<String>();
	private boolean available = true;

	public List<Metal> getAllMetals() {
		return completeMetalList;
	}

	private Metal getCommonMetal(final String name) {
		return nameToMetal.get(name);
	}

	public List<Metal> getGeneratedMetals() {
		return generatedMetalList;
	}

	public String[] getMetalNames() {
		return metalNames;
	}

	public void init() {
		metalNames = new String[completeMetalList.size()];
		for (int i = 0; i < completeMetalList.size(); i++)
			metalNames[i] = completeMetalList.get(i).name;
		Arrays.sort(metalNames);
		for (final Metal metal : completeMetalList)
			metal.registerRecipes();
	}

	private boolean isCommonMetal(final String name) {
		return nameToMetal.containsKey(name);
	}

	public void post() {
		for (final String name : oresRegistered) {
			final Metal metal = getCommonMetal(name);
			if (metal == null)
				continue;
			metal.setGeneration(true);
			if (!generatedMetalList.contains(metal))
				generatedMetalList.add(metal);
		}
	}

	public void pre() {
		available = false;
		registerOre("aluminium", 1F, 1F, 6, 6, 0.2F, 3F);
		registerOre("zinc", 1F, 0.9375F, 6, 6, 0.2F, 2.5F);
		registerOre("copper", 1F, 0.8125F, 6, 6, 0.2F, 3F);
		registerOre("tin", 0.8F, 0.6875F, 6, 6, 0.2F, 1.5F);
		registerOre("lead", 0.8F, 0.625F, 6, 6, 0.2F, 1.5F);
		registerOre("iron", 0.8F, 0.5F, 6, 6, 0.2F, 4F, 4F, true);
		registerOre("nickel", 0.6F, 0.375F, 6, 6, 0.2F, 4F);
		registerOre("tungsten", 0.6F, 0.3125F, 6, 6, 0.2F, 7.5F);
		registerOre("silver", 0.6F, 0.25F, 6, 6, 0.2F, 2.5F);
		registerOre("gold", 0.4F, 0.125F, 6, 6, 0.2F, 2.75F, 2.75F, true);
		registerOre("titanium", 0.4F, 0.0625F, 6, 6, 0.2F, 6F);
		registerOre("platinum", 0.4F, 0F, 6, 6, 0.2F, 3.5F);
		registerAlloy("brass", "copper", 1, "zinc", 1, 1F, 0.875F, 6, 6, 0.2F, 3.5F);
		registerAlloy("bronze", "copper", 3, "tin", 1, 0.8F, 0.75F, 6, 6, 0.2F, 3F);
		registerAlloy("steel", "iron", 1, "coal", 1, 0.8F, 0.5625F, 6, 6, 0.2F, 7.25F);
		registerAlloy("invar", "iron", 2, "nickel", 1, 0.6F, 0.4375F, 6, 6, 0.2F, 4F);
		registerAlloy("electrum", "gold", 1, "silver", 1, 0.4F, 0.1875F, 6, 6, 0.2F, 2.5F);
		final ImmutableList<IMCMessage> list = FMLInterModComms.fetchRuntimeMessages(CommonOre.instance);
		final List<NBTTagCompound> metals = new ArrayList<NBTTagCompound>();
		final List<NBTTagCompound> alloys = new ArrayList<NBTTagCompound>();
		for (final IMCMessage imc : list) {
			if (imc.key.equalsIgnoreCase("metal"))
				metals.add(imc.getNBTValue());
			if (imc.key.equalsIgnoreCase("alloy"))
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
				registerAlloy(tag.getString("name"), tag.getString("primary"), tag.getInteger("primaryNumber"), tag.getString("secondary"), tag.getInteger("secondaryNumber"), tag.getFloat("rarity"), tag.getFloat("depth"), tag.getInteger("nodes"), tag.getInteger("size"), tag.getFloat("spread"), tag.getFloat("hardness"), tag.getFloat("resistance"), generate);
			}
			catch (final Exception e) {
				System.err.println("CommonOre has detected a badly formatted alloy registration.");
			}
		available = true;
	}

	public void registerAlloy(final String name, final String primary, final int primaryRecipeComponents, final String secondary, final int secondaryRecipeComponents, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness) {
		registerAlloy(name, primary, primaryRecipeComponents, secondary, secondaryRecipeComponents, chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, hardness, false);
	}

	public void registerAlloy(final String name, final String primary, final int primaryRecipeComponents, final String secondary, final int secondaryRecipeComponents, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance, final boolean generate) {
		final Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metal.setGeneration(generate);
		metal.setComponents(primary, primaryRecipeComponents, secondary, secondaryRecipeComponents);
		completeMetalList.add(metal);
		nameToMetal.put(name, metal);
		metal.registerOre();
	}

	public void registerOre(final String name, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness) {
		registerOre(name, chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, hardness, false);
	}

	public void registerOre(final String name, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance, final boolean generate) {
		final Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metal.setGeneration(generate);
		completeMetalList.add(metal);
		nameToMetal.put(metal.name, metal);
		metal.registerOre();
	}

	@SubscribeEvent
	public void registerOreEvent(final OreRegisterEvent event) {
		if (Utility.isCommonName(event.Name)) {
			Utility.cacheCommon(event.Ore);
			if (!available)
				return;
			oresRegistered.add(Utility.cleanName(event.Name));
		}
	}
}
