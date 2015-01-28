package com.genuineflix.co.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.genuineflix.co.CommonOre;
import com.genuineflix.co.metals.Metal;
import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MetalRegistry {

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

	public static final MetalRegistry instance = new MetalRegistry();

	public static List<Metal> getAllMetals() {
		return MetalRegistry.instance.completeMetalList;
	}

	public List<Metal> getGeneratedMetals() {
		return generatedMetalList;
	}

	public boolean isCommon(final Block block) {
		return block != null && isCommon(block.getUnlocalizedName());
	}

	public boolean isCommon(final Item item) {
		return item != null && isCommon(item.getUnlocalizedName());
	}

	public boolean isCommon(final ItemStack stack) {
		return stack != null && isCommon(stack.getUnlocalizedName());
	}

	public boolean isCommon(final String name) {
		return metalMap.containsKey(MetalRegistry.cleanName(name));
	}

	public Metal getCommon(final Block block) {
		if (block == null)
			return null;
		return getCommon(block.getUnlocalizedName());
	}

	public Metal getCommon(final Item item) {
		if (item == null)
			return null;
		return getCommon(item.getUnlocalizedName());
	}

	public Metal getCommon(final ItemStack stack) {
		if (stack == null)
			return null;
		return getCommon(stack.getUnlocalizedName());
	}

	public Metal getCommon(final String name) {
		return metalMap.get(MetalRegistry.cleanName(name));
	}

	private final List<Metal> completeMetalList = new ArrayList<Metal>();
	private final List<Metal> generatedMetalList = new ArrayList<Metal>();
	private final Map<String, Metal> metalMap = new HashMap<String, Metal>();
	private final List<ItemStack> oresRegistered = new ArrayList<ItemStack>();
	private boolean available = true;

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
		for (final ItemStack stack : oresRegistered) {
			final Metal metal = getCommon(stack);
			if (metal == null)
				continue;
			System.out.println("[CommoneOre]: " + stack + " was found to be a suitable.");
			metal.setGeneration(true);
			generatedMetalList.add(metal);
		}
	}

	@SubscribeEvent
	public void registerOreEvent(final OreRegisterEvent event) {
		System.out.println("[CommonOre]: " + available + ": " + event.Name);
		if (!available)
			return;
		oresRegistered.add(event.Ore);
	}

	public void registerAlloy(final String name, final String primary, final String secondary, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance) {
		registerAlloy(name, primary, secondary, chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance, false);
	}

	public void registerAlloy(final String name, final String primary, final String secondary, final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance, final boolean generate) {
		final Metal metal = new Metal(name);
		metal.setup(chunkRarity, depth, nodesPerChunk, nodeSize, spread, hardness, resistance);
		metal.setGeneration(generate);
		metal.setComponents(primary, secondary);
		metal.registerOre();
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
		metal.registerOre();
		completeMetalList.add(metal);
		metalMap.put(metal.name, metal);
	}
}
