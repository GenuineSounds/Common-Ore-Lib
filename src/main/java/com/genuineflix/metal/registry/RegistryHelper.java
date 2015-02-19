package com.genuineflix.metal.registry;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.api.IMetal;
import com.genuineflix.metal.block.BlockOreCommon;
import com.genuineflix.metal.block.ItemOreCommon;
import com.genuineflix.metal.generator.feature.CommonMetalNode.BiomeType;
import com.genuineflix.metal.util.StringHelper;

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
		GameRegistry.registerBlock(metal.getOre(), ItemOreCommon.class, "ore" + nameFixed, new Object[] {
			nameFixed
		});
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
		final String nameFixed = metal.getDisplayName();
		GameRegistry.addSmelting(new ItemStack(metal.getOre(), 1, 0), new ItemStack(metal.getIngot(), 1, 0), 10);
		GameRegistry.addSmelting(new ItemStack(metal.getOre(), 1, 1), new ItemStack(metal.getIngot(), 2, 0), 10);
		GameRegistry.addSmelting(new ItemStack(metal.getOre(), 1, 2), new ItemStack(metal.getIngot(), 4, 0), 10);
		GameRegistry.addSmelting(metal.getDust(), new ItemStack(metal.getIngot()), 10);
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getNugget(), 9), "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getBlock()), "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getIngot()), "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getIngot(), 9), "block" + nameFixed));
		RegistryHelper.sendStackTo("ThermalExpansion", "PulverizerRecipe", 4000, new ItemStack(metal.getOre(), 1, 0), new ItemStack(metal.getDust(), 2, 0));
		RegistryHelper.sendStackTo("ThermalExpansion", "PulverizerRecipe", 4800, new ItemStack(metal.getOre(), 1, 1), new ItemStack(metal.getDust(), 4, 0));
		RegistryHelper.sendStackTo("ThermalExpansion", "PulverizerRecipe", 6400, new ItemStack(metal.getOre(), 1, 2), new ItemStack(metal.getDust(), 8, 0));
		if (metal.isComposite())
			RegistryHelper.registerAlloyRecipes(metal);
	}

	private static void registerAlloyRecipes(final IMetal metal) {
		Object[] dustNames = new Object[0];
		for (final IMetal.Compound comp : metal.getCompounds())
			for (int i = 0; i < comp.factor; i++) {
				dustNames = Arrays.copyOf(dustNames, dustNames.length + 1);
				dustNames[dustNames.length - 1] = StringHelper.camelCase("dust", comp.metal.getName());
			}
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.getDust(), dustNames.length, 0), dustNames));
		final IMetal component1 = metal.getCompounds().get(0).metal;
		final IMetal component2 = metal.getCompounds().get(1).metal;
		final Item dust1 = component1.getDust();
		final Item dust2 = component2.getDust();
		if (dust1 != null && dust2 != null) {
			final ItemStack dustStack1 = new ItemStack(dust1, metal.getCompounds().get(0).factor, 0);
			final ItemStack dustStack2 = new ItemStack(dust2, metal.getCompounds().get(1).factor, 0);
			final ItemStack dustOutput = new ItemStack(metal.getDust(), dustNames.length, 0);
			RegistryHelper.sendStackTo("ThermalExpansion", "SmelterRecipe", 1600, dustStack1, dustStack2, dustOutput);
		}
		final Item ingot1 = component1.getIngot();
		final Item ingot2 = component2.getIngot();
		if (ingot1 != null && ingot2 != null) {
			final ItemStack ingotStack1 = new ItemStack(ingot1, metal.getCompounds().get(0).factor, 0);
			final ItemStack ingotStack2 = new ItemStack(ingot2, metal.getCompounds().get(1).factor, 0);
			final ItemStack ingotOutput = new ItemStack(metal.getIngot(), dustNames.length, 0);
			RegistryHelper.sendStackTo("ThermalExpansion", "SmelterRecipe", 2400, ingotStack1, ingotStack2, ingotOutput);
		}
	}

	private static void sendStackTo(final String mod, final String key, final int energy, final ItemStack in, final ItemStack out) {
		final NBTTagCompound message = new NBTTagCompound();
		final NBTTagCompound input = new NBTTagCompound();
		final NBTTagCompound primaryOutput = new NBTTagCompound();
		in.writeToNBT(input);
		out.writeToNBT(primaryOutput);
		message.setInteger("energy", energy);
		message.setTag("input", input);
		message.setTag("primaryOutput", primaryOutput);
		FMLInterModComms.sendMessage(mod, key, message);
	}

	private static void sendStackTo(final String mod, final String key, final int energy, final ItemStack input1, final ItemStack input2, final ItemStack out) {
		final NBTTagCompound message = new NBTTagCompound();
		final NBTTagCompound primaryInput = new NBTTagCompound();
		final NBTTagCompound secondaryInput = new NBTTagCompound();
		final NBTTagCompound primaryOutput = new NBTTagCompound();
		input1.writeToNBT(primaryInput);
		input2.writeToNBT(secondaryInput);
		out.writeToNBT(primaryOutput);
		message.setInteger("energy", energy);
		message.setTag("primaryInput", primaryInput);
		message.setTag("secondaryInput", secondaryInput);
		message.setTag("primaryOutput", primaryOutput);
		FMLInterModComms.sendMessage(mod, key, message);
	}
}
