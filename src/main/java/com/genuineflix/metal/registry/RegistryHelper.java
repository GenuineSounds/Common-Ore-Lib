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
import com.genuineflix.metal.block.BlockOreCommon;
import com.genuineflix.metal.block.ItemOreCommon;
import com.genuineflix.metal.generator.feature.CommonMetalNode.BiomeType;
import com.genuineflix.metal.interfaces.IOre;
import com.genuineflix.metal.util.StringHelper;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegistryHelper {

	private RegistryHelper() {}

	static void createItems(final Metal metal) {
		final String nameFixed = metal.nameFixed;
		metal.ore = new BlockOreCommon(nameFixed).setBlockName("ore" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setStepSound(Block.soundTypeStone).setBlockTextureName(CommonOre.MODID + ":ores/" + nameFixed);
		metal.dust = new Item().setUnlocalizedName("dust" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setTextureName(CommonOre.MODID + ":dusts/" + nameFixed);
		metal.ingot = new Item().setUnlocalizedName("ingot" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setTextureName(CommonOre.MODID + ":ingots/" + nameFixed);
		metal.nugget = new Item().setUnlocalizedName("nugget" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setTextureName(CommonOre.MODID + ":nuggets/" + nameFixed);
		metal.block = new BlockCompressed(MapColor.ironColor).setBlockName("block" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setStepSound(Block.soundTypeMetal).setBlockTextureName(CommonOre.MODID + ":blocks/" + nameFixed);
	}

	static void registerItems(final Metal metal) {
		final String nameFixed = metal.nameFixed;
		GameRegistry.registerBlock(metal.ore, ItemOreCommon.class, "ore" + nameFixed, new Object[] {
			nameFixed
		});
		GameRegistry.registerItem(metal.dust, "dust" + nameFixed);
		GameRegistry.registerItem(metal.ingot, "ingot" + nameFixed);
		GameRegistry.registerItem(metal.nugget, "nugget" + nameFixed);
		GameRegistry.registerBlock(metal.block, "storage" + nameFixed);
		for (int i = 0; i < BiomeType.values().length; i++)
			OreDictionary.registerOre("ore" + BlockOreCommon.NAMES[i] + nameFixed, new ItemStack(metal.ore, 1, i));
		OreDictionary.registerOre("dust" + nameFixed, metal.dust);
		OreDictionary.registerOre("pulv" + nameFixed, metal.dust);
		OreDictionary.registerOre("ingot" + nameFixed, metal.ingot);
		OreDictionary.registerOre("nugget" + nameFixed, metal.nugget);
		OreDictionary.registerOre("storage" + nameFixed, metal.block);
		OreDictionary.registerOre("block" + nameFixed, metal.block);
		for (int i = 0; i < BiomeType.values().length; i++)
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(metal.ore, 1, i));
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(metal.block, 1, 0));
	}

	static void registerRecipes(final Metal metal) {
		final String nameFixed = metal.nameFixed;
		GameRegistry.addSmelting(new ItemStack(metal.ore, 1, 0), new ItemStack(metal.ingot, 1, 0), 10);
		GameRegistry.addSmelting(new ItemStack(metal.ore, 1, 1), new ItemStack(metal.ingot, 2, 0), 10);
		GameRegistry.addSmelting(new ItemStack(metal.ore, 1, 2), new ItemStack(metal.ingot, 4, 0), 10);
		GameRegistry.addSmelting(metal.dust, new ItemStack(metal.ingot), 10);
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.nugget, 9), "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.block), "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.ingot), "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.ingot, 9), "block" + nameFixed));
		RegistryHelper.sendStackTo("ThermalExpansion", "PulverizerRecipe", 4000, new ItemStack(metal.ore, 1, 0), new ItemStack(metal.dust, 2, 0));
		RegistryHelper.sendStackTo("ThermalExpansion", "PulverizerRecipe", 4800, new ItemStack(metal.ore, 1, 1), new ItemStack(metal.dust, 4, 0));
		RegistryHelper.sendStackTo("ThermalExpansion", "PulverizerRecipe", 6400, new ItemStack(metal.ore, 1, 2), new ItemStack(metal.dust, 8, 0));
		if (metal.isAlloy())
			RegistryHelper.registerAlloyRecipes(metal);
	}

	private static void registerAlloyRecipes(final Metal metal) {
		Object[] names = new Object[0];
		for (final IOre.Component comp : metal.getComponents())
			for (int i = 0; i < comp.factor; i++) {
				names = Arrays.copyOf(names, names.length + 1);
				names[names.length - 1] = StringHelper.camelCase("dust", comp.name);
			}
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(metal.dust, names.length, 0), names));
		if (!MetalRegistry.isCommonName(metal.getComponents().get(0).name) || !MetalRegistry.isCommonName(metal.getComponents().get(1).name))
			return;
		final Metal component1 = MetalRegistry.getMetal(metal.getComponents().get(0).name);
		final Item dust1 = component1.dust;
		final Item ingot1 = component1.ingot;
		final ItemStack dustStack1 = new ItemStack(dust1, metal.getComponents().get(0).factor, 0);
		final ItemStack ingotStack1 = new ItemStack(ingot1, metal.getComponents().get(0).factor, 0);
		final Metal component2 = MetalRegistry.getMetal(metal.getComponents().get(1).name);
		final Item dust2 = component2.dust;
		final Item ingot2 = component2.ingot;
		final ItemStack dustStack2 = new ItemStack(dust2, metal.getComponents().get(1).factor, 0);
		final ItemStack ingotStack2 = new ItemStack(ingot2, metal.getComponents().get(1).factor, 0);
		CommonOre.log.error(metal.nameFixed + " " + names.length);
		final ItemStack dustOutput = new ItemStack(metal.dust, names.length, 0);
		final ItemStack ingotOutput = new ItemStack(metal.ingot, names.length, 0);
		RegistryHelper.sendStackTo("ThermalExpansion", "SmelterRecipe", 1600, dustStack1, dustStack2, dustOutput);
		RegistryHelper.sendStackTo("ThermalExpansion", "SmelterRecipe", 2400, ingotStack1, ingotStack2, ingotOutput);
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
