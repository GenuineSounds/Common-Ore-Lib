package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;

import com.genuineflix.metal.interfaces.IAlloy.Component;
import com.genuineflix.metal.interfaces.IOre.Properties;
import com.genuineflix.metal.util.StringHelper;
import com.google.common.collect.ImmutableList;

public final class MetalRegistry {

	public static final String[] COMMON_NAMES = new String[] {
			"aluminium", "zinc", "copper", "tin", "lead", "iron", "nickel", "tungsten", "silver", "gold", "titanium", "platinum", "brass", "bronze", "steel", "invar", "electrum"
	};
	private static Map<String, List<String>> commonCache = new HashMap<String, List<String>>();
	private static List<Metal> metalList = new ArrayList<Metal>();
	private static boolean closed = false;

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

	public static Metal getMetal(String name) {
		name = StringHelper.cleanName(name);
		for (final Metal metal : metalList)
			if (metal.name.equals(name))
				return metal;
		return null;
	}

	public static List<Metal> getMetals() {
		return metalList;
	}

	public static void init() {
		for (final Metal metal : metalList)
			metal.registerRecipes();
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
		for (final String cName : COMMON_NAMES)
			if (cName.equals(StringHelper.cleanName(name)))
				return true;
		return false;
	}

	public static boolean isRegistryClosed() {
		return closed;
	}

	public static void post() {
		setClosed(true);
		metalList = ImmutableList.copyOf(metalList);
	}

	public static void pre() {}

	public static void registerAlloy(final String name, final Properties properties, final String... components) {
		final Metal metal = new Metal(name);
		metal.setup(properties);
		metal.setGeneration(properties.generate);
		final Component[] compounds = new Component[components.length];
		for (int i = 0; i < components.length; i++)
			compounds[i] = new Component(components[i]);
		metal.setComponents(compounds);
		metalList.add(metal);
		metal.registerOre();
	}

	public static void registerOre(final String name, final Properties properties) {
		final Metal metal = new Metal(name);
		metal.setup(properties);
		metal.setGeneration(properties.generate);
		metalList.add(metal);
		metal.registerOre();
	}

	private static void setClosed(final boolean blockRegistration) {
		MetalRegistry.closed = blockRegistration;
	}

	static {
		setClosed(true);
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
		setClosed(false);
	}
}
