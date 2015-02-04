package com.genuineflix.metal.registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import scala.actors.threadpool.Arrays;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.interfaces.IAlloy;
import com.genuineflix.metal.util.Utility;

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
	private Component[] components = new Component[0];
	private boolean generate;

	public Metal(final String name) {
		this.name = name;
		nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		ore = new BlockOre().setBlockName("ore" + nameFixed).setCreativeTab(Utility.COMMON_TAB).setStepSound(Block.soundTypeStone).setBlockTextureName(CommonOre.MODID + ":ores/" + nameFixed);
		dust = new Item().setUnlocalizedName("dust" + nameFixed).setCreativeTab(Utility.COMMON_TAB).setTextureName(CommonOre.MODID + ":dusts/" + nameFixed);
		ingot = new Item().setUnlocalizedName("ingot" + nameFixed).setCreativeTab(Utility.COMMON_TAB).setTextureName(CommonOre.MODID + ":ingots/" + nameFixed);
		nugget = new Item().setUnlocalizedName("nugget" + nameFixed).setCreativeTab(Utility.COMMON_TAB).setTextureName(CommonOre.MODID + ":nuggets/" + nameFixed);
		block = new BlockCompressed(MapColor.ironColor).setBlockName("block" + nameFixed).setCreativeTab(Utility.COMMON_TAB).setStepSound(Block.soundTypeMetal).setBlockTextureName(CommonOre.MODID + ":blocks/" + nameFixed);
	}

	public boolean generate() {
		return generate;
	}

	@Override
	public Component[] getComponents() {
		return components;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public boolean isAlloy() {
		return components != null && components.length > 0;
	}

	public void registerAlloyRecipes() {
		Object[] items = new Object[0];
		for (final Component component : components)
			for (int i = 0; i < component.factor; i++) {
				items = Arrays.copyOf(items, items.length + 1, Object[].class);
				items[items.length - 1] = component.name;
			}
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(dust, items.length), items));
	}

	public void registerOre() {
		GameRegistry.registerBlock(ore, "ore" + nameFixed);
		GameRegistry.registerItem(dust, "dust" + nameFixed);
		GameRegistry.registerItem(ingot, "ingot" + nameFixed);
		GameRegistry.registerItem(nugget, "nugget" + nameFixed);
		GameRegistry.registerBlock(block, "storage" + nameFixed);
		OreDictionary.registerOre("ore" + nameFixed, ore);
		OreDictionary.registerOre("dust" + nameFixed, dust);
		OreDictionary.registerOre("pulv" + nameFixed, dust);
		OreDictionary.registerOre("ingot" + nameFixed, ingot);
		OreDictionary.registerOre("nugget" + nameFixed, nugget);
		OreDictionary.registerOre("storage" + nameFixed, block);
		OreDictionary.registerOre("block" + nameFixed, block);
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(ore));
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(block));
	}

	public void registerRecipes() {
		GameRegistry.addSmelting(ore, new ItemStack(ingot), 10);
		GameRegistry.addSmelting(dust, new ItemStack(ingot), 10);
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(nugget, 9), "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(block), "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot), "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot, 9), "block" + nameFixed));
		if (isAlloy())
			registerAlloyRecipes();
	}

	@Override
	public void setComponents(final Component... components) {
		this.components = components;
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
