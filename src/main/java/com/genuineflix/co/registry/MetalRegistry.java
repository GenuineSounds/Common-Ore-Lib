package com.genuineflix.co.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.genuineflix.co.CommonOre;
import com.genuineflix.co.metals.Metal;
import com.genuineflix.co.utils.Utility;
import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MetalRegistry {

	public static final MetalRegistry instance = new MetalRegistry();
	private final List<Metal> completeMetalList = new ArrayList<Metal>();
	private final List<Metal> generatedMetalList = new ArrayList<Metal>();
	private final Map<String, Metal> nameToMetal = new HashMap<String, Metal>();
	private final List<String> oresRegistered = new ArrayList<String>();
	private boolean available = true;

	public List<Metal> getGeneratedMetals() {
		return generatedMetalList;
	}

	public List<Metal> getAllMetals() {
		return completeMetalList;
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

	public void pre() {
		available = false;
		registerOre("aluminium", 1F, 1F, 6, 6, 0.2F, 3F, 2F, false);
		registerOre("zinc", 1F, 0.9375F, 6, 6, 0.2F, 2.5F, 2F, false);
		registerOre("copper", 1F, 0.8125F, 6, 6, 0.2F, 3F, 2F, false);
		registerOre("tin", 0.8F, 0.6875F, 6, 6, 0.2F, 1.5F, 2F, false);
		registerOre("lead", 0.8F, 0.625F, 6, 6, 0.2F, 1.5F, 2F, false);
		registerOre("iron", 0.8F, 0.5F, 6, 6, 0.2F, 4F, 2F, true);
		registerOre("nickel", 0.6F, 0.375F, 6, 6, 0.2F, 4F, 2F, false);
		registerOre("tungsten", 0.6F, 0.3125F, 6, 6, 0.2F, 7.5F, 2F, false);
		registerOre("silver", 0.6F, 0.25F, 6, 6, 0.2F, 2.5F, 2F, false);
		registerOre("gold", 0.4F, 0.125F, 6, 6, 0.2F, 2.75F, 2F, true);
		registerOre("titanium", 0.4F, 0.0625F, 6, 6, 0.2F, 6F, 2F, false);
		registerOre("platinum", 0.4F, 0F, 6, 6, 0.2F, 3.5F, 2F, false);
		registerAlloy("brass", "copper", "zinc", 1F, 0.875F, 6, 6, 0.2F, 3.5F, 2F, false);
		registerAlloy("bronze", "copper", "tin", 0.8F, 0.75F, 6, 6, 0.2F, 3F, 2F, false);
		registerAlloy("steel", "coal", "iron", 0.8F, 0.5625F, 6, 6, 0.2F, 7.25F, 2F, false);
		registerAlloy("invar", "nickel", "iron", 0.6F, 0.4375F, 6, 6, 0.2F, 4F, 2F, false);
		registerAlloy("electrum", "gold", "silver", 0.4F, 0.1875F, 6, 6, 0.2F, 2.5F, 2F, false);
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
		available = true;
	}

	public void init() {
		for (final Metal metal : completeMetalList)
			metal.registerRecipes();
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

	public void registerAlloy(final String name, final String primary, final String secondary, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance) {
		registerAlloy(name, primary, secondary, chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance, false);
	}

	public void registerAlloy(final String name, final String primary, final String secondary, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance, final boolean generate) {
		final Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metal.setGeneration(generate);
		if (!isCommonMetal(primary) || !isCommonMetal(secondary))
			metal.setComponents(primary, secondary);
		else
			metal.setComponents(getCommonMetal(primary), getCommonMetal(secondary));
		completeMetalList.add(metal);
		nameToMetal.put(name, metal);
		metal.registerOre();
	}

	public void registerOre(final String name, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance) {
		registerOre(name, chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance, false);
	}

	public void registerOre(final String name, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance, final boolean generate) {
		final Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metal.setGeneration(generate);
		completeMetalList.add(metal);
		nameToMetal.put(metal.name, metal);
		metal.registerOre();
	}

	private boolean isCommonMetal(final String name) {
		return nameToMetal.containsKey(name);
	}

	private Metal getCommonMetal(final String name) {
		return nameToMetal.get(name);
	}
}
