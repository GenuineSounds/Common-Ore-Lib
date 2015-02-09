package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;

import com.genuineflix.metal.interfaces.IOre;
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

	public static void pre() {
		for (final Metal metal : metalList) {
			RegistryHelper.createItems(metal);
			RegistryHelper.registerItems(metal);
		}
	}

	public static void init() {
		for (final Metal metal : metalList)
			RegistryHelper.registerRecipes(metal);
	}

	public static void post() {
		for (final Metal metal : metalList)
			metal.finallize();
		setClosed(true);
		metalList = ImmutableList.copyOf(metalList);
	}

	public static void registerMetal(final String name, final IOre.Property properties, final IOre.Component... components) {
		final Metal metal = new Metal(name);
		metal.setup(properties);
		metal.setComponents(components);
		metalList.add(metal);
	}

	private static void setClosed(final boolean blockRegistration) {
		MetalRegistry.closed = blockRegistration;
	}

	static {
		final IOre.Property propAl = new IOre.Property(1.0F, 1.00F, 10, 20, 0.05F, 3.0F, 3.0F);
		final IOre.Property propFe = new IOre.Property(1.0F, 0.50F, 10, 24, 0.05F, 4.0F, 4.0F, true);
		final IOre.Property propTi = new IOre.Property(0.8F, 0.06F, 5, 12, 0.05F, 6.0F, 6.0F);
		final IOre.Property propW = new IOre.Property(0.7F, 0.31F, 8, 16, 0.05F, 7.5F, 7.5F);
		final IOre.Property propNi = new IOre.Property(0.6F, 0.38F, 8, 12, 0.05F, 4.0F, 4.0F);
		final IOre.Property propZn = new IOre.Property(0.6F, 0.94F, 10, 12, 0.05F, 2.5F, 2.5F);
		final IOre.Property propCu = new IOre.Property(0.5F, 0.81F, 10, 18, 0.05F, 3.0F, 3.0F);
		final IOre.Property propPb = new IOre.Property(0.4F, 0.63F, 8, 12, 0.05F, 1.5F, 1.5F);
		final IOre.Property propSn = new IOre.Property(0.4F, 0.69F, 10, 18, 0.05F, 1.5F, 1.5F);
		final IOre.Property propAg = new IOre.Property(0.3F, 0.25F, 8, 10, 0.05F, 2.5F, 2.5F);
		final IOre.Property propPt = new IOre.Property(0.3F, 0.00F, 8, 10, 0.05F, 3.5F, 3.5F);
		final IOre.Property propAu = new IOre.Property(0.3F, 0.13F, 8, 10, 0.05F, 2.75F, 2.75F, true);
		final IOre.Property propCuZn = new IOre.Property(0.4F, 0.88F, 10, 10, 0.05F, 3.5F, 3.5F);
		final IOre.Property propCuSn = new IOre.Property(0.4F, 0.75F, 10, 10, 0.05F, 3.0F, 3.0F);
		final IOre.Property propFeC = new IOre.Property(0.4F, 0.56F, 9, 10, 0.05F, 7.25F, 7.25F);
		final IOre.Property propFeNi = new IOre.Property(0.5F, 0.44F, 8, 10, 0.05F, 4.0F, 4.0F);
		final IOre.Property propAuAg = new IOre.Property(0.3F, 0.19F, 8, 10, 0.05F, 2.5F, 2.5F);
		setClosed(true);
		registerMetal("aluminium", propAl);
		registerMetal("iron", propFe);
		registerMetal("titanium", propTi);
		registerMetal("tungsten", propW);
		registerMetal("nickel", propNi);
		registerMetal("zinc", propZn);
		registerMetal("copper", propCu);
		registerMetal("lead", propPb);
		registerMetal("tin", propSn);
		registerMetal("silver", propAg);
		registerMetal("platinum", propPt);
		registerMetal("gold", propAu);
		registerMetal("brass", propCuZn, new IOre.Component("copper", 1), new IOre.Component("zinc", 1));
		registerMetal("bronze", propCuSn, new IOre.Component("copper", 3), new IOre.Component("tin", 1));
		registerMetal("steel", propFeC, new IOre.Component("iron", 1), new IOre.Component("coal", 1));
		registerMetal("invar", propFeNi, new IOre.Component("iron", 2), new IOre.Component("nickel", 1));
		registerMetal("electrum", propAuAg, new IOre.Component("gold", 1), new IOre.Component("silver", 1));
		setClosed(false);
	}
}
