package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.interfaces.IAlloy.Component;
import com.genuineflix.metal.interfaces.IOre.Properties;
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
		registerOre("aluminium", new Properties(1F, 1F, 6, 6, 0.2F, 3F, 3F));
		registerOre("zinc", new Properties(1F, 0.9375F, 6, 6, 0.2F, 2.5F, 2.5F));
		registerOre("copper", new Properties(1F, 0.8125F, 6, 6, 0.2F, 3F, 3F));
		registerOre("tin", new Properties(0.8F, 0.6875F, 6, 6, 0.2F, 1.5F, 1.5F));
		registerOre("lead", new Properties(0.8F, 0.625F, 6, 6, 0.2F, 1.5F, 1.5F));
		registerOre("iron", new Properties(0.8F, 0.5F, 6, 6, 0.2F, 4F, 4F, true));
		registerOre("nickel", new Properties(0.6F, 0.375F, 6, 6, 0.2F, 4F, 4F));
		registerOre("tungsten", new Properties(0.6F, 0.3125F, 6, 6, 0.2F, 7.5F, 7.5F));
		registerOre("silver", new Properties(0.6F, 0.25F, 6, 6, 0.2F, 2.5F, 2.5F));
		registerOre("gold", new Properties(0.4F, 0.125F, 6, 6, 0.2F, 2.75F, 2.75F, true));
		registerOre("titanium", new Properties(0.4F, 0.0625F, 6, 6, 0.2F, 6F, 6F));
		registerOre("platinum", new Properties(0.4F, 0F, 6, 6, 0.2F, 3.5F, 3.5F));
		registerAlloy("brass", new Properties(1F, 0.875F, 6, 6, 0.2F, 3.5F, 3.5F), new Component("copper"), new Component("zinc"));
		registerAlloy("bronze", new Properties(0.8F, 0.75F, 6, 6, 0.2F, 3F, 3F), new Component("copper"), new Component("copper"), new Component("copper"), new Component("tin"));
		registerAlloy("steel", new Properties(0.8F, 0.5625F, 6, 6, 0.2F, 7.25F, 7.25F), new Component("iron"), new Component("coal"));
		registerAlloy("invar", new Properties(0.6F, 0.4375F, 6, 6, 0.2F, 4F, 4F), new Component("iron"), new Component("iron"), new Component("nickel"));
		registerAlloy("electrum", new Properties(0.4F, 0.1875F, 6, 6, 0.2F, 2.5F, 2.5F), new Component("gold"), new Component("silver"));
		final ImmutableList<IMCMessage> list = FMLInterModComms.fetchRuntimeMessages(CommonOre.instance);
		final List<NBTTagCompound> metals = new ArrayList<NBTTagCompound>();
		final List<NBTTagCompound> alloys = new ArrayList<NBTTagCompound>();
		available = true;
	}

	public void registerAlloy(final String name, final Properties properties, final Component... components) {
		final Metal metal = new Metal(name);
		metal.setup(properties);
		metal.setGeneration(properties.generate);
		metal.setComponents(components);
		completeMetalList.add(metal);
		nameToMetal.put(name, metal);
		metal.registerOre();
	}

	public void registerOre(final String name, final Properties properties) {
		final Metal metal = new Metal(name);
		metal.setup(properties);
		metal.setGeneration(properties.generate);
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
