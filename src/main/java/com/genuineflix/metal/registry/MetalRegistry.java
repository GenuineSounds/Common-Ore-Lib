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

import com.genuineflix.metal.api.IMetal.Compound;
import com.genuineflix.metal.api.IMetal.Settings;
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
		return MetalRegistry.metals;
	}

	public static Metal getMetal(final String name) {
		for (final Metal m : MetalRegistry.metals)
			if (m.getName().equals(StringHelper.cleanName(name)))
				return m;
		return null;
	}

	public static boolean isMetal(final String name) {
		return MetalRegistry.getMetal(name) != null;
	}

	public static void registrationEvent(final String name, final Block block, final int meta) {
		if (!name.startsWith("ore"))
			return;
		List<String> list;
		if (!MetalRegistry.commonCache.containsKey(name))
			list = new ArrayList<String>();
		else
			list = MetalRegistry.commonCache.get(name);
		list.add(block.getUnlocalizedName() + ":" + meta);
		MetalRegistry.commonCache.put(name, list);
		if (MetalRegistry.closed)
			return;
		for (final Metal metal : MetalRegistry.metals) {
			if (!metal.getName().equals(StringHelper.cleanName(name)))
				continue;
			metal.getSettings().setGenerate(true);
			break;
		}
	}

	public static boolean isCommonBlock(final Block block) {
		return MetalRegistry.isCommonBlock(block, 0);
	}

	public static boolean isCommonBlock(final Block block, final int meta) {
		if (block == null)
			return false;
		for (final List<String> entry : MetalRegistry.commonCache.values()) {
			if (entry == null || entry.isEmpty())
				continue;
			if (entry.contains(block.getUnlocalizedName() + ":" + meta))
				return true;
		}
		return false;
	}

	public static void pre() {
		for (final Metal metal : MetalRegistry.metals) {
			if (metal.isManual())
				continue;
			MetalRegistry.closed = true;
			RegistryHelper.createItems(metal);
			RegistryHelper.registerItems(metal);
			MetalRegistry.closed = false;
		}
	}

	public static void init() {
		MetalRegistry.COAL.setOre(Blocks.coal_ore);
		MetalRegistry.COAL.setBlock(Blocks.coal_block);
		MetalRegistry.COAL.setIngot(Items.coal);
		final List<ItemStack> coalDusts = OreDictionary.getOres("dustCoal");
		if (!coalDusts.isEmpty())
			MetalRegistry.COAL.setDust(coalDusts.get(0).getItem());
		for (final Metal metal : MetalRegistry.metals)
			RegistryHelper.registerRecipes(metal);
	}

	public static void post() {
		for (final Metal metal : MetalRegistry.metals)
			metal.finallize();
		MetalRegistry.metals = ImmutableList.copyOf(MetalRegistry.metals);
	}

	private static Metal registerMetal(final String name) {
		final Metal metal = new Metal(name);
		MetalRegistry.metals.add(metal);
		return metal;
	}

	private static Metal registerMetal(final String name, final Settings properties, final Compound... components) {
		final Metal metal = new Metal(name, properties, components);
		MetalRegistry.metals.add(metal);
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
		COAL = MetalRegistry.registerMetal("coal").manual();
		// Default metals
		ALUMINIUM = MetalRegistry.registerMetal("aluminium", propAl);
		IRON = MetalRegistry.registerMetal("iron", propFe);
		TITANIUM = MetalRegistry.registerMetal("titanium", propTi);
		TUNGSTEN = MetalRegistry.registerMetal("tungsten", propW);
		NICKEL = MetalRegistry.registerMetal("nickel", propNi);
		ZINC = MetalRegistry.registerMetal("zinc", propZn);
		COPPER = MetalRegistry.registerMetal("copper", propCu);
		LEAD = MetalRegistry.registerMetal("lead", propPb);
		TIN = MetalRegistry.registerMetal("tin", propSn);
		SILVER = MetalRegistry.registerMetal("silver", propAg);
		PLATINUM = MetalRegistry.registerMetal("platinum", propPt);
		GOLD = MetalRegistry.registerMetal("gold", propAu);
		// Default composite metals
		BRASS = MetalRegistry.registerMetal("brass", propCuZn, new Compound(MetalRegistry.COPPER, 1), new Compound(MetalRegistry.ZINC, 1));
		BRONZE = MetalRegistry.registerMetal("bronze", propCuSn, new Compound(MetalRegistry.COPPER, 3), new Compound(MetalRegistry.TIN, 1));
		STEEL = MetalRegistry.registerMetal("steel", propFeC, new Compound(MetalRegistry.IRON, 1), new Compound(MetalRegistry.COAL, 1));
		INVAR = MetalRegistry.registerMetal("invar", propFeNi, new Compound(MetalRegistry.IRON, 2), new Compound(MetalRegistry.NICKEL, 1));
		ELECTRUM = MetalRegistry.registerMetal("electrum", propAuAg, new Compound(MetalRegistry.GOLD, 1), new Compound(MetalRegistry.SILVER, 1));
	}
}
