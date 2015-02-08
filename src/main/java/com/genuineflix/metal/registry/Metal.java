package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.block.ItemBlockOre;
import com.genuineflix.metal.block.Ore;
import com.genuineflix.metal.generator.feature.CommonMetalNode.BiomeType;
import com.genuineflix.metal.interfaces.IAlloy;
import com.genuineflix.metal.util.StringHelper;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class Metal implements IAlloy {

	public final String name;
	public final String nameFixed;
	public final Block ore;
	public final Block block;
	public final Item dust;
	public final Item ingot;
	public final Item nugget;
	private Properties properties;
	private final List<Component> components = new ArrayList<Component>();
	private boolean generate;

	public Metal(final String name) {
		this.name = name;
		nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		ore = new Ore(nameFixed).setBlockName("ore" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setStepSound(Block.soundTypeStone).setBlockTextureName(CommonOre.MODID + ":ores/" + nameFixed);
		dust = new Item().setUnlocalizedName("dust" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setTextureName(CommonOre.MODID + ":dusts/" + nameFixed);
		ingot = new Item().setUnlocalizedName("ingot" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setTextureName(CommonOre.MODID + ":ingots/" + nameFixed);
		nugget = new Item().setUnlocalizedName("nugget" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setTextureName(CommonOre.MODID + ":nuggets/" + nameFixed);
		block = new BlockCompressed(MapColor.ironColor).setBlockName("block" + nameFixed).setCreativeTab(CommonOre.COMMON_TAB).setStepSound(Block.soundTypeMetal).setBlockTextureName(CommonOre.MODID + ":blocks/" + nameFixed);
	}

	public boolean generate() {
		return generate;
	}

	@Override
	public List<Component> getComponents() {
		return components;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public boolean isAlloy() {
		return components != null && components.size() > 0;
	}

	public void registerOre() {
		GameRegistry.registerBlock(ore, ItemBlockOre.class, "ore" + nameFixed, new Object[] {
			nameFixed
		});
		GameRegistry.registerItem(dust, "dust" + nameFixed);
		GameRegistry.registerItem(ingot, "ingot" + nameFixed);
		GameRegistry.registerItem(nugget, "nugget" + nameFixed);
		GameRegistry.registerBlock(block, "storage" + nameFixed);
		for (int i = 0; i < BiomeType.values().length; i++)
			OreDictionary.registerOre("ore" + Ore.NAMES[i] + nameFixed, new ItemStack(ore, 1, i));
		OreDictionary.registerOre("dust" + nameFixed, dust);
		OreDictionary.registerOre("pulv" + nameFixed, dust);
		OreDictionary.registerOre("ingot" + nameFixed, ingot);
		OreDictionary.registerOre("nugget" + nameFixed, nugget);
		OreDictionary.registerOre("storage" + nameFixed, block);
		OreDictionary.registerOre("block" + nameFixed, block);
		for (int i = 0; i < BiomeType.values().length; i++)
			FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(ore, 1, i));
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(block, 1, 0));
	}

	public void registerRecipes() {
		GameRegistry.addSmelting(new ItemStack(ore, 1, 0), new ItemStack(ingot, 1, 0), 10);
		GameRegistry.addSmelting(new ItemStack(ore, 1, 1), new ItemStack(ingot, 2, 0), 10);
		GameRegistry.addSmelting(new ItemStack(ore, 1, 2), new ItemStack(ingot, 4, 0), 10);
		GameRegistry.addSmelting(dust, new ItemStack(ingot), 10);
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(nugget, 9), "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(block), "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot), "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot, 9), "block" + nameFixed));
		sendStackTo("ThermalExpansion", "PulverizerRecipe", 4000, new ItemStack(ore, 1, 0), new ItemStack(dust, 2, 0));
		sendStackTo("ThermalExpansion", "PulverizerRecipe", 4800, new ItemStack(ore, 1, 1), new ItemStack(dust, 4, 0));
		sendStackTo("ThermalExpansion", "PulverizerRecipe", 6400, new ItemStack(ore, 1, 2), new ItemStack(dust, 8, 0));
		if (isAlloy())
			registerAlloyRecipes();
	}

	private void registerAlloyRecipes() {
		Object[] names = new Object[0];
		for (int i = 0; i < components.get(i).factor; i++) {
			names = Arrays.copyOf(names, names.length + 1);
			names[names.length - 1] = StringHelper.camelCase("dust", components.get(i).name);
		}
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(dust, names.length, 0), names));
		if (!MetalRegistry.isCommonName(components.get(0).name) || !MetalRegistry.isCommonName(components.get(1).name))
			return;
		final Metal component1 = MetalRegistry.instance.getMetal(components.get(0).name);
		final Item dust1 = component1.dust;
		final Item ingot1 = component1.ingot;
		final ItemStack dustStack1 = new ItemStack(dust1, components.get(0).factor, 0);
		final ItemStack ingotStack1 = new ItemStack(ingot1, components.get(0).factor, 0);
		final Metal component2 = MetalRegistry.instance.getMetal(components.get(1).name);
		final Item dust2 = component2.dust;
		final Item ingot2 = component2.ingot;
		final ItemStack dustStack2 = new ItemStack(dust2, components.get(1).factor, 0);
		final ItemStack ingotStack2 = new ItemStack(ingot2, components.get(1).factor, 0);
		final ItemStack dustOutput = new ItemStack(dust, names.length, 0);
		final ItemStack ingotOutput = new ItemStack(ingot, names.length, 0);
		sendStackTo("ThermalExpansion", "SmelterRecipe", 1600, dustStack1, dustStack2, dustOutput);
		sendStackTo("ThermalExpansion", "SmelterRecipe", 2400, ingotStack1, ingotStack2, ingotOutput);
	}

	private void sendStackTo(final String mod, final String key, final int energy, final ItemStack in, final ItemStack out) {
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

	private void sendStackTo(final String mod, final String key, final int energy, final ItemStack input1, final ItemStack input2, final ItemStack out) {
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

	@Override
	public void setComponents(final Component... addComps) {
		components.clear();
		for (int i = 0; i < addComps.length; i++) {
			final Component component = addComps[i];
			if (components.contains(component))
				components.get(components.indexOf(component)).factor++;
			else
				components.add(component);
		}
	}

	public void setGeneration(final boolean generate) {
		if (!this.generate && generate)
			CommonOre.log.info("[CommonOre] Setting " + nameFixed + " implicitly to generate");
		if (this.generate && !generate)
			CommonOre.log.info("[CommonOre] Setting " + nameFixed + " explicitly to not generate");
		this.generate = generate;
	}

	@Override
	public void setOreProperties(final Properties properties) {
		this.properties = properties;
	}

	public Metal setup(final Properties properties) {
		setOreProperties(properties);
		ore.setHardness(properties.hardness);
		ore.setResistance(properties.resistance);
		block.setHardness(properties.hardness);
		block.setResistance(properties.resistance);
		return this;
	}
}
