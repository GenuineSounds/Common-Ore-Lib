package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.genuineflix.metal.interfaces.IAlloy.Component;
import com.genuineflix.metal.interfaces.IOre.Properties;
import com.genuineflix.metal.metals.Metal;
import com.genuineflix.metal.utils.Utility;

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
		registerOre("aluminium", new Properties(1.0F, 1.00F, 5, 12, 0.1F, 3F, 3F));
		registerOre("iron", new Properties(1.0F, 0.50F, 5, 12, 0.1F, 4F, 4F, true));
		registerOre("titanium", new Properties(0.9F, 0.06F, 2, 6, 0.1F, 6F, 6F));
		registerOre("tungsten", new Properties(0.8F, 0.31F, 4, 8, 0.1F, 7.5F, 7.5F));
		registerOre("nickel", new Properties(0.6F, 0.38F, 4, 6, 0.1F, 4F, 4F));
		registerOre("zinc", new Properties(0.4F, 0.94F, 5, 6, 0.1F, 2.5F, 2.5F));
		registerOre("copper", new Properties(0.4F, 0.81F, 5, 8, 0.1F, 3F, 3F));
		registerOre("lead", new Properties(0.4F, 0.63F, 4, 6, 0.1F, 1.5F, 1.5F));
		registerOre("tin", new Properties(0.4F, 0.69F, 5, 8, 0.1F, 1.5F, 1.5F));
		registerOre("silver", new Properties(0.2F, 0.25F, 4, 5, 0.1F, 2.5F, 2.5F));
		registerOre("platinum", new Properties(0.2F, 0.00F, 4, 5, 0.1F, 3.5F, 3.5F));
		registerOre("gold", new Properties(0.2F, 0.13F, 4, 5, 0.1F, 2.75F, 2.75F, true));
		registerAlloy("brass", new Properties(0.4F, 0.88F, 10, 4, 0.1F, 3.5F, 3.5F), "copper", "zinc");
		registerAlloy("bronze", new Properties(0.4F, 0.75F, 10, 4, 0.1F, 3F, 3F), "copper", "copper", "copper", "tin");
		registerAlloy("steel", new Properties(0.7F, 0.56F, 10, 4, 0.1F, 7.25F, 7.25F), "iron", "coal");
		registerAlloy("invar", new Properties(0.8F, 0.44F, 10, 4, 0.1F, 4F, 4F), "iron", "iron", "nickel");
		registerAlloy("electrum", new Properties(0.2F, 0.19F, 10, 4, 0.1F, 2.5F, 2.5F), "gold", "silver");
		available = true;
	}

	public void registerAlloy(final String name, final Properties properties, final String... components) {
		final Metal metal = new Metal(name);
		metal.setup(properties);
		metal.setGeneration(properties.generate);
		if (properties.generate)
			oresRegistered.add(metal.name);
		final Component[] compounds = new Component[components.length];
		for (int i = 0; i < components.length; i++)
			compounds[i] = new Component(components[i]);
		metal.setComponents(compounds);
		completeMetalList.add(metal);
		nameToMetal.put(metal.name, metal);
		metal.registerOre();
	}

	public void registerOre(final String name, final Properties properties) {
		final Metal metal = new Metal(name);
		metal.setup(properties);
		metal.setGeneration(properties.generate);
		if (properties.generate)
			oresRegistered.add(metal.name);
		completeMetalList.add(metal);
		nameToMetal.put(metal.name, metal);
		metal.registerOre();
	}

	@SubscribeEvent
	public void registerOreEvent(final OreRegisterEvent event) {
		if (event.Ore.getItem() instanceof ItemBlock) {
			final Block block = ((ItemBlock) event.Ore.getItem()).field_150939_a;
			final int meta = event.Ore.getItemDamage();
			if (Utility.isCommonName(event.Name)) {
				Utility.cacheCommonBlock(event.Name, block, meta);
				if (available)
					oresRegistered.add(Utility.cleanName(event.Name));
			}
		}
	}
}
