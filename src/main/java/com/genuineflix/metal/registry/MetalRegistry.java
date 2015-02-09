package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineflix.metal.interfaces.IMetal.Compound;
import com.genuineflix.metal.interfaces.IMetal.Settings;
import com.genuineflix.metal.util.StringHelper;
import com.google.common.collect.ImmutableList;

public final class MetalRegistry {

	public static final Metal COAL;
	public static final Metal ALUMINIUM;
	public static final Metal IRON;
	public static final Metal TITANIUM;
	public static final Metal TUNGSTEN;
	public static final Metal NICKEL;
	public static final Metal ZINC;
	public static final Metal COPPER;
	public static final Metal LEAD;
	public static final Metal TIN;
	public static final Metal SILVER;
	public static final Metal PLATINUM;
	public static final Metal GOLD;
	public static final Metal BRASS;
	public static final Metal BRONZE;
	public static final Metal STEEL;
	public static final Metal INVAR;
	public static final Metal ELECTRUM;

	public static List<Metal> getMetals() {
		return metals;
	}

	public static Metal getMetal(final String name) {
		for (final Metal m : metals)
			if (m.name.equals(StringHelper.cleanName(name)))
				return m;
		return null;
	}

	public static void registrationEvent(final String name, final Block block, final int meta) {
		if (!name.startsWith("ore"))
			return;
		List<String> list;
		if (!commonCache.containsKey(name))
			list = new ArrayList<String>();
		else
			list = commonCache.get(name);
		list.add(block.getUnlocalizedName() + ":" + meta);
		commonCache.put(name, list);
		if (closed)
			return;
		for (final Metal m : metals)
			if (m.name.equals(StringHelper.cleanName(name))) {
				m.getSettings().setGenerate(true);
				break;
			}
	}

	public static boolean isCommonBlock(final Block block) {
		return isCommonBlock(block, 0);
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

	public static void pre() {
		for (final Metal metal : metals) {
			if (metal.manuallyInitiated())
				continue;
			RegistryHelper.createItems(metal);
			RegistryHelper.registerItems(metal);
		}
	}

	public static void init() {
		COAL.setOre(Blocks.coal_ore);
		COAL.setBlock(Blocks.coal_block);
		COAL.setIngot(Items.coal);
		final List<ItemStack> coalDusts = OreDictionary.getOres("dustCoal");
		if (!coalDusts.isEmpty())
			COAL.setDust(coalDusts.get(0).getItem());
		for (final Metal metal : metals)
			RegistryHelper.registerRecipes(metal);
	}

	public static void post() {
		for (final Metal metal : metals)
			metal.finallize();
		metals = ImmutableList.copyOf(metals);
	}

	private static Metal registerMetal(final String name) {
		final Metal metal = new Metal(name);
		metals.add(metal);
		return metal;
	}

	private static Metal registerMetal(final String name, final Settings properties, final Compound... components) {
		final Metal metal = new Metal(name, properties, components);
		metals.add(metal);
		return metal;
	}

	private static Map<String, List<String>> commonCache = new HashMap<String, List<String>>();
	private static List<Metal> metals = new ArrayList<Metal>();
	private static boolean closed = false;
	static {
		// Default properties for metals
		final Settings propAl = new Settings(1.0F, 1.00F, 10, 20, 0.05F, 3.0F, 3.0F);
		final Settings propFe = new Settings(1.0F, 0.50F, 10, 24, 0.05F, 4.0F, 4.0F, true);
		final Settings propTi = new Settings(0.8F, 0.06F, 5, 12, 0.05F, 6.0F, 6.0F);
		final Settings propW = new Settings(0.7F, 0.31F, 8, 16, 0.05F, 7.5F, 7.5F);
		final Settings propNi = new Settings(0.6F, 0.38F, 8, 12, 0.05F, 4.0F, 4.0F);
		final Settings propZn = new Settings(0.6F, 0.94F, 10, 12, 0.05F, 2.5F, 2.5F);
		final Settings propCu = new Settings(0.5F, 0.81F, 10, 18, 0.05F, 3.0F, 3.0F);
		final Settings propPb = new Settings(0.4F, 0.63F, 8, 12, 0.05F, 1.5F, 1.5F);
		final Settings propSn = new Settings(0.4F, 0.69F, 10, 18, 0.05F, 1.5F, 1.5F);
		final Settings propAg = new Settings(0.3F, 0.25F, 8, 10, 0.05F, 2.5F, 2.5F);
		final Settings propPt = new Settings(0.3F, 0.00F, 8, 10, 0.05F, 3.5F, 3.5F);
		final Settings propAu = new Settings(0.3F, 0.13F, 8, 10, 0.05F, 2.75F, 2.75F, true);
		final Settings propCuZn = new Settings(0.4F, 0.88F, 10, 10, 0.05F, 3.5F, 3.5F);
		final Settings propCuSn = new Settings(0.4F, 0.75F, 10, 10, 0.05F, 3.0F, 3.0F);
		final Settings propFeC = new Settings(0.4F, 0.56F, 9, 10, 0.05F, 7.25F, 7.25F);
		final Settings propFeNi = new Settings(0.5F, 0.44F, 8, 10, 0.05F, 4.0F, 4.0F);
		final Settings propAuAg = new Settings(0.3F, 0.19F, 8, 10, 0.05F, 2.5F, 2.5F);
		// Coal is a dummy metal
		COAL = registerMetal("coal").flagManualInitiation();
		// Default metals
		ALUMINIUM = registerMetal("aluminium", propAl);
		IRON = registerMetal("iron", propFe);
		TITANIUM = registerMetal("titanium", propTi);
		TUNGSTEN = registerMetal("tungsten", propW);
		NICKEL = registerMetal("nickel", propNi);
		ZINC = registerMetal("zinc", propZn);
		COPPER = registerMetal("copper", propCu);
		LEAD = registerMetal("lead", propPb);
		TIN = registerMetal("tin", propSn);
		SILVER = registerMetal("silver", propAg);
		PLATINUM = registerMetal("platinum", propPt);
		GOLD = registerMetal("gold", propAu);
		// Default composite metals
		BRASS = registerMetal("brass", propCuZn, new Compound(COPPER, 1), new Compound(ZINC, 1));
		BRONZE = registerMetal("bronze", propCuSn, new Compound(COPPER, 3), new Compound(TIN, 1));
		STEEL = registerMetal("steel", propFeC, new Compound(IRON, 1), new Compound(COAL, 1));
		INVAR = registerMetal("invar", propFeNi, new Compound(IRON, 2), new Compound(NICKEL, 1));
		ELECTRUM = registerMetal("electrum", propAuAg, new Compound(GOLD, 1), new Compound(SILVER, 1));
	}
}
