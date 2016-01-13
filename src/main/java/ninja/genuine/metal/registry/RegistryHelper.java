package ninja.genuine.metal.registry;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import ninja.genuine.metal.CommonOre;
import ninja.genuine.metal.api.IMetal;
import ninja.genuine.metal.block.BlockOreCommon;
import ninja.genuine.metal.block.ItemOreCommon;
import ninja.genuine.metal.generator.feature.CommonMetalNode.BiomeType;
import ninja.genuine.metal.util.StringHelper;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegistryHelper {

	private RegistryHelper() {}

	static void createItems(final Metal metal) {
		final String nameFixed = metal.getDisplayName();
		metal.setOre(new BlockOreCommon(nameFixed).setBlockName("ore" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setStepSound(Block.soundTypeStone).setBlockTextureName(CommonOre.MODID + ":ores/" + nameFixed));
		metal.setDust(new Item().setUnlocalizedName("dust" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setTextureName(CommonOre.MODID + ":dusts/" + nameFixed));
		metal.setIngot(new Item().setUnlocalizedName("ingot" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setTextureName(CommonOre.MODID + ":ingots/" + nameFixed));
		metal.setNugget(new Item().setUnlocalizedName("nugget" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setTextureName(CommonOre.MODID + ":nuggets/" + nameFixed));
		metal.setBlock(new BlockCompressed(MapColor.ironColor).setBlockName("block" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setStepSound(Block.soundTypeMetal).setBlockTextureName(CommonOre.MODID + ":blocks/" + nameFixed));
	}

	static void registerItems(final IMetal metal) {
		final String nameFixed = metal.getDisplayName();
		GameRegistry.registerBlock(metal.getOre(), ItemOreCommon.class, "ore" + nameFixed, new Object[] { nameFixed });
		GameRegistry.registerItem(metal.getDust(), "dust" + nameFixed);
		GameRegistry.registerItem(metal.getIngot(), "ingot" + nameFixed);
		GameRegistry.registerItem(metal.getNugget(), "nugget" + nameFixed);
		GameRegistry.registerBlock(metal.getBlock(), "storage" + nameFixed);
		for (int i = 0; i < BiomeType.values().length; i++)
			OreDictionary.registerOre("ore" + BlockOreCommon.NAMES[i] + nameFixed, new ItemStack(metal.getOre(), 1, i));
		OreDictionary.registerOre("dust" + nameFixed, metal.getDust());
		OreDictionary.registerOre("pulv" + nameFixed, metal.getDust());
		OreDictionary.registerOre("ingot" + nameFixed, metal.getIngot());
		OreDictionary.registerOre("nugget" + nameFixed, metal.getNugget());
		OreDictionary.registerOre("storage" + nameFixed, metal.getBlock());
		OreDictionary.registerOre("block" + nameFixed, metal.getBlock());
		for (int i = 0; i < BiomeType.values().length; i++)
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(metal.getOre(), 1, i));
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(metal.getBlock(), 1, 0));
	}

	static void registerRecipes(final IMetal metal) {
		if (metal instanceof FakeMetal)
			return;
		final String name = metal.getDisplayName();
		ItemStack oreRegular = new ItemStack(metal.getOre(), 1, 0);
		ItemStack oreNether = new ItemStack(metal.getOre(), 1, 1);
		ItemStack oreEnder = new ItemStack(metal.getOre(), 1, 2);
		ItemStack ingot1 = new ItemStack(metal.getIngot(), 1, 0);
		ItemStack ingot2 = new ItemStack(metal.getIngot(), 2, 0);
		ItemStack ingot3 = new ItemStack(metal.getIngot(), 4, 0);
		GameRegistry.addSmelting(oreRegular.copy(), ingot1.copy(), 10);
		GameRegistry.addSmelting(oreNether.copy(), ingot2.copy(), 10);
		GameRegistry.addSmelting(oreEnder.copy(), ingot3.copy(), 10);
		ThermalExpansionHelper.addFurnaceRecipe(800 * 2, oreRegular.copy(), ingot1.copy());
		ThermalExpansionHelper.addFurnaceRecipe(800 * 4, oreNether.copy(), ingot2.copy());
		ThermalExpansionHelper.addFurnaceRecipe(800 * 8, oreEnder.copy(), ingot3.copy());
		GameRegistry.addSmelting(metal.getDust(), new ItemStack(metal.getIngot()), 10);
		ThermalExpansionHelper.addFurnaceRecipe(800 * 1, new ItemStack(metal.getDust(), 1, 0), ingot1.copy());
		String ingot = "ingot" + name;
		String nugget = "nugget" + name;
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getNugget(), 9), ingot));
		Object[] o1 = { ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot };
		Object[] o2 = { nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget };
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getBlock()), o1));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getIngot()), o2));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getIngot(), 9), "block" + name));
		ThermalExpansionHelper.addPulverizerRecipe(4000, oreRegular.copy(), new ItemStack(metal.getDust(), 2, 0));
		ThermalExpansionHelper.addPulverizerRecipe(4800, oreNether.copy(), new ItemStack(metal.getDust(), 4, 0));
		ThermalExpansionHelper.addPulverizerRecipe(6400, oreEnder.copy(), new ItemStack(metal.getDust(), 8, 0));
		if (metal.isComposite())
			RegistryHelper.registerAlloyRecipes(metal);
	}

	private static void registerAlloyRecipes(final IMetal metal) {
		Object[] names = new String[0];
		for (final IMetal.Compound comp : metal.getCompounds())
			for (int i = 0; i < comp.factor; i++)
				names = appendToArray(names, StringHelper.camelCase("dust", comp.metal.getName()));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getDust(), names.length, 0), names));
		final IMetal component1 = metal.getCompounds().get(0).metal;
		final IMetal component2 = metal.getCompounds().get(1).metal;
		final Item dust1 = component1.getDust();
		final Item dust2 = component2.getDust();
		if (dust1 != null && dust2 != null) {
			final ItemStack dustStack1 = new ItemStack(dust1, metal.getCompounds().get(0).factor, 0);
			final ItemStack dustStack2 = new ItemStack(dust2, metal.getCompounds().get(1).factor, 0);
			final ItemStack dustOutput = new ItemStack(metal.getDust(), names.length, 0);
			ThermalExpansionHelper.addSmelterRecipe(1600, dustStack1, dustStack2, dustOutput);
		}
		final Item ingot1 = component1.getIngot();
		final Item ingot2 = component2.getIngot();
		if (ingot1 != null && ingot2 != null) {
			final ItemStack ingotStack1 = new ItemStack(ingot1, metal.getCompounds().get(0).factor, 0);
			final ItemStack ingotStack2 = new ItemStack(ingot2, metal.getCompounds().get(1).factor, 0);
			final ItemStack ingotOutput = new ItemStack(metal.getIngot(), names.length, 0);
			ThermalExpansionHelper.addSmelterRecipe(2400, ingotStack1, ingotStack2, ingotOutput);
		}
	}

	private static <T> T[] appendToArray(T[] ts, T t) {
		ts = Arrays.copyOf(ts, ts.length + 1);
		ts[ts.length - 1] = t;
		return ts;
	}
}
