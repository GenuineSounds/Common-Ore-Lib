package ninja.genuine.metal.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import ninja.genuine.metal.api.IMetal;
import ninja.genuine.metal.api.IMetal.Compound;
import ninja.genuine.metal.api.IMetal.Generation;
import ninja.genuine.metal.util.StringHelper;

import com.google.common.collect.ImmutableList;

public final class MetalRegistry {

	public static final IMetal COAL;
	public static final IMetal ALUMINUM;
	public static final IMetal IRON;
	public static final IMetal TITANIUM;
	public static final IMetal TUNGSTEN;
	public static final IMetal NICKEL;
	public static final IMetal ZINC;
	public static final IMetal COPPER;
	public static final IMetal LEAD;
	public static final IMetal TIN;
	public static final IMetal SILVER;
	public static final IMetal PLATINUM;
	public static final IMetal GOLD;
	public static final IMetal BRASS;
	public static final IMetal BRONZE;
	public static final IMetal STEEL;
	public static final IMetal INVAR;
	public static final IMetal ELECTRUM;

	public static List<IMetal> getMetals() {
		return MetalRegistry.metals;
	}

	public static IMetal getMetal(final String name) {
		for (final IMetal m : MetalRegistry.metals)
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
		for (final IMetal metal : MetalRegistry.metals) {
			if (!metal.getName().equals(StringHelper.cleanName(name)))
				continue;
			metal.getGeneration().setGenerate(true);
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
		for (final IMetal metal : MetalRegistry.metals) {
			if (metal.getGeneration() == null)
				continue;
			MetalRegistry.closed = true;
			RegistryHelper.createItems((Metal) metal);
			RegistryHelper.registerItems(metal);
			MetalRegistry.closed = false;
		}
	}

	public static void init() {
		((Metal) MetalRegistry.COAL).setOre(Blocks.coal_ore);
		((Metal) MetalRegistry.COAL).setBlock(Blocks.coal_block);
		((Metal) MetalRegistry.COAL).setIngot(Items.coal);
		final List<ItemStack> coalDusts = OreDictionary.getOres("dustCoal");
		if (!coalDusts.isEmpty())
			((Metal) MetalRegistry.COAL).setDust(coalDusts.get(0).getItem());
		for (final IMetal metal : MetalRegistry.metals)
			RegistryHelper.registerRecipes(metal);
	}

	public static void post() {
		for (final IMetal metal : MetalRegistry.metals)
			if (metal instanceof Metal)
				((Metal) metal).finallize();
		MetalRegistry.metals = ImmutableList.copyOf(MetalRegistry.metals);
	}

	private static Metal createFakeMetal(final String name) {
		final Metal metal = new FakeMetal(name);
		MetalRegistry.metals.add(metal);
		return metal;
	}

	private static Metal createMetal(final String name, final Generation properties, final Compound... components) {
		final Metal metal = new Metal(name, properties, components);
		MetalRegistry.metals.add(metal);
		return metal;
	}

	private static Map<String, List<String>> commonCache = new HashMap<String, List<String>>();
	private static List<IMetal> metals = new ArrayList<IMetal>();
	private static boolean closed = false;

	static {
		// Default properties for metals
		final Generation propAl = new Generation(1.0F, 1.00F, 10, 20, 0.05F, 3.0F, 3.0F);
		final Generation propFe = new Generation(1.0F, 0.50F, 10, 24, 0.05F, 4.0F, 4.0F, true);
		final Generation propTi = new Generation(0.8F, 0.06F, 5, 12, 0.05F, 6.0F, 6.0F);
		final Generation propW = new Generation(0.7F, 0.31F, 8, 16, 0.05F, 7.5F, 7.5F);
		final Generation propNi = new Generation(0.6F, 0.38F, 8, 12, 0.05F, 4.0F, 4.0F);
		final Generation propZn = new Generation(0.6F, 0.94F, 10, 12, 0.05F, 2.5F, 2.5F);
		final Generation propCu = new Generation(0.5F, 0.81F, 10, 18, 0.05F, 3.0F, 3.0F);
		final Generation propPb = new Generation(0.4F, 0.63F, 8, 12, 0.05F, 1.5F, 1.5F);
		final Generation propSn = new Generation(0.4F, 0.69F, 10, 18, 0.05F, 1.5F, 1.5F);
		final Generation propAg = new Generation(0.3F, 0.25F, 8, 10, 0.05F, 2.5F, 2.5F);
		final Generation propPt = new Generation(0.3F, 0.00F, 8, 10, 0.05F, 3.5F, 3.5F);
		final Generation propAu = new Generation(0.3F, 0.13F, 8, 10, 0.05F, 2.75F, 2.75F, true);
		final Generation propCuZn = new Generation(0.4F, 0.88F, 10, 10, 0.05F, 3.5F, 3.5F);
		final Generation propCuSn = new Generation(0.4F, 0.75F, 10, 10, 0.05F, 3.0F, 3.0F);
		final Generation propFeC = new Generation(0.4F, 0.56F, 9, 10, 0.05F, 7.25F, 7.25F);
		final Generation propFeNi = new Generation(0.5F, 0.44F, 8, 10, 0.05F, 4.0F, 4.0F);
		final Generation propAuAg = new Generation(0.3F, 0.19F, 8, 10, 0.05F, 2.5F, 2.5F);
		// Coal is a dummy metal
		COAL = MetalRegistry.createFakeMetal("coal");
		// Default metals
		ALUMINUM = MetalRegistry.createMetal("aluminum", propAl);
		IRON = MetalRegistry.createMetal("iron", propFe);
		TITANIUM = MetalRegistry.createMetal("titanium", propTi);
		TUNGSTEN = MetalRegistry.createMetal("tungsten", propW);
		NICKEL = MetalRegistry.createMetal("nickel", propNi);
		ZINC = MetalRegistry.createMetal("zinc", propZn);
		COPPER = MetalRegistry.createMetal("copper", propCu);
		LEAD = MetalRegistry.createMetal("lead", propPb);
		TIN = MetalRegistry.createMetal("tin", propSn);
		SILVER = MetalRegistry.createMetal("silver", propAg);
		PLATINUM = MetalRegistry.createMetal("platinum", propPt);
		GOLD = MetalRegistry.createMetal("gold", propAu);
		// Default composite metals
		BRASS = MetalRegistry.createMetal("brass", propCuZn, new Compound(MetalRegistry.COPPER, 1), new Compound(MetalRegistry.ZINC, 1));
		BRONZE = MetalRegistry.createMetal("bronze", propCuSn, new Compound(MetalRegistry.COPPER, 3), new Compound(MetalRegistry.TIN, 1));
		STEEL = MetalRegistry.createMetal("steel", propFeC, new Compound(MetalRegistry.IRON, 1), new Compound(MetalRegistry.COAL, 1));
		INVAR = MetalRegistry.createMetal("invar", propFeNi, new Compound(MetalRegistry.IRON, 2), new Compound(MetalRegistry.NICKEL, 1));
		ELECTRUM = MetalRegistry.createMetal("electrum", propAuAg, new Compound(MetalRegistry.GOLD, 1), new Compound(MetalRegistry.SILVER, 1));
	}
}
