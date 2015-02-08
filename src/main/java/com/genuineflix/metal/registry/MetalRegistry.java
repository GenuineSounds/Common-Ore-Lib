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
import com.genuineflix.metal.util.StringHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MetalRegistry {

	public static void cacheCommonBlock(final String oreDict, final Block block, final int meta) {
		if (!oreDict.startsWith("ore"))
			return;
		List<String> list;
		if (!commonCache.containsKey(oreDict))
			list = new ArrayList<String>();
		else
			list = commonCache.get(oreDict);
		list.add(block.getUnlocalizedName() + ":" + meta);
		commonCache.put(oreDict, list);
	}

	public static boolean isCommonBlock(final Block block, final int meta) {
		if (block == null)
			return false;
		for (final List<String> entry : commonCache.values()) {
			if (entry == null || entry.isEmpty())
				continue;
			if (entry.contains(block.getUnlocalizedName() + ":" + meta))
				return true;
		}
		return false;
	}

	public static boolean isCommonName(final String name) {
		return commonList.contains(StringHelper.cleanName(name));
	}

	private static final List<String> commonList = Arrays.asList(new String[] {
			"aluminium", "zinc", "copper", "tin", "lead", "iron", "nickel", "tungsten", "silver", "gold", "titanium", "platinum", "brass", "bronze", "steel", "invar", "electrum"
	});
	private static Map<String, List<String>> commonCache = new HashMap<String, List<String>>();
	public static final MetalRegistry instance = new MetalRegistry();
	private String[] metalNames;
	private final List<Metal> completeMetalList = new ArrayList<Metal>();
	private final List<Metal> generatedMetalList = new ArrayList<Metal>();
	private final List<String> oresRegistered = new ArrayList<String>();
	private final Map<String, Metal> nameToMetal = new HashMap<String, Metal>();
	private boolean available = true;

	private MetalRegistry() {}

	@SubscribeEvent
	public void registerOreEvent(final OreRegisterEvent event) {
		if (event.Ore.getItem() instanceof ItemBlock) {
			final Block block = ((ItemBlock) event.Ore.getItem()).field_150939_a;
			final int meta = event.Ore.getItemDamage();
			if (MetalRegistry.isCommonName(event.Name)) {
				MetalRegistry.cacheCommonBlock(event.Name, block, meta);
				if (available)
					oresRegistered.add(StringHelper.cleanName(event.Name));
			}
		}
	}

	public void pre() {
		available = false;
		registerOre("aluminium", new Properties(1.0F, 1.00F, 10, 20, 0.05F, 3.0F, 3.0F));
		registerOre("iron", new Properties(1.0F, 0.50F, 10, 24, 0.05F, 4.0F, 4.0F, true));
		registerOre("titanium", new Properties(0.8F, 0.06F, 5, 12, 0.05F, 6.0F, 6.0F));
		registerOre("tungsten", new Properties(0.7F, 0.31F, 8, 16, 0.05F, 7.5F, 7.5F));
		registerOre("nickel", new Properties(0.6F, 0.38F, 8, 12, 0.05F, 4.0F, 4.0F));
		registerOre("zinc", new Properties(0.6F, 0.94F, 10, 12, 0.05F, 2.5F, 2.5F));
		registerOre("copper", new Properties(0.5F, 0.81F, 10, 18, 0.05F, 3.0F, 3.0F));
		registerOre("lead", new Properties(0.4F, 0.63F, 8, 12, 0.05F, 1.5F, 1.5F));
		registerOre("tin", new Properties(0.4F, 0.69F, 10, 18, 0.05F, 1.5F, 1.5F));
		registerOre("silver", new Properties(0.3F, 0.25F, 8, 10, 0.05F, 2.5F, 2.5F));
		registerOre("platinum", new Properties(0.3F, 0.00F, 8, 10, 0.05F, 3.5F, 3.5F));
		registerOre("gold", new Properties(0.3F, 0.13F, 8, 10, 0.05F, 2.75F, 2.75F, true));
		registerAlloy("brass", new Properties(0.4F, 0.88F, 10, 10, 0.05F, 3.5F, 3.5F), "copper", "zinc");
		registerAlloy("bronze", new Properties(0.4F, 0.75F, 10, 10, 0.05F, 3.0F, 3.0F), "copper", "copper", "copper", "tin");
		registerAlloy("steel", new Properties(0.4F, 0.56F, 9, 10, 0.05F, 7.25F, 7.25F), "iron", "coal");
		registerAlloy("invar", new Properties(0.5F, 0.44F, 8, 10, 0.05F, 4.0F, 4.0F), "iron", "iron", "nickel");
		registerAlloy("electrum", new Properties(0.3F, 0.19F, 8, 10, 0.05F, 2.5F, 2.5F), "gold", "silver");
		available = true;
	}

	public void init() {
		metalNames = new String[completeMetalList.size()];
		for (int i = 0; i < completeMetalList.size(); i++)
			metalNames[i] = completeMetalList.get(i).name;
		Arrays.sort(metalNames);
		for (final Metal metal : completeMetalList)
			metal.registerRecipes();
	}

	public void post() {
		for (final String name : oresRegistered) {
			final Metal metal = getMetal(name);
			if (metal == null)
				continue;
			metal.setGeneration(true);
			if (!generatedMetalList.contains(metal))
				generatedMetalList.add(metal);
		}
	}

	private boolean isCommonMetal(final String name) {
		return nameToMetal.containsKey(name);
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

	public List<Metal> getAllMetals() {
		return completeMetalList;
	}

	public List<Metal> getGeneratedMetals() {
		return generatedMetalList;
	}

	public Metal getMetal(final String name) {
		return nameToMetal.get(name);
	}

	public String[] getMetalNames() {
		return metalNames;
	}
}
