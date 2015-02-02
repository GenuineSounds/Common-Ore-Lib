package com.genuineflix.metal.metals;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.interfaces.IAlloy;
import com.genuineflix.metal.interfaces.IOre;
import com.genuineflix.metal.utils.Utility;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class Metal implements IOre, IAlloy {

	public final String name;
	public final String nameFixed;
	public final Block ore;
	public final Block storage;
	public final Item dust;
	public final Item ingot;
	public final Item nugget;
	private float chunkRarity;
	private float depth;
	private int nodesPerChunk;
	private int nodeSize;
	private float spread;
	private float hardness;
	private float resistance;
	private String primary;
	private String secondary;
	private boolean alloy;
	private boolean nonCommonAlloy;
	private boolean generate;

	public Metal(final String name) {
		this.name = name;
		nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		ore = new BlockOre().setBlockName("ore" + nameFixed).setCreativeTab(CreativeTabs.tabBlock).setStepSound(Block.soundTypeStone).setBlockTextureName(CommonOre.MODID + ":ores/" + nameFixed);
		dust = new Item().setUnlocalizedName("dust" + nameFixed).setCreativeTab(CreativeTabs.tabMaterials).setTextureName(CommonOre.MODID + ":dusts/" + nameFixed);
		ingot = new Item().setUnlocalizedName("ingot" + nameFixed).setCreativeTab(CreativeTabs.tabMaterials).setTextureName(CommonOre.MODID + ":ingots/" + nameFixed);
		nugget = new Item().setUnlocalizedName("nugget" + nameFixed).setCreativeTab(CreativeTabs.tabMaterials).setTextureName(CommonOre.MODID + ":nuggets/" + nameFixed);
		storage = new BlockCompressed(MapColor.ironColor).setBlockName("block" + nameFixed).setCreativeTab(CreativeTabs.tabBlock).setStepSound(Block.soundTypeMetal).setBlockTextureName(CommonOre.MODID + ":blocks/" + nameFixed);
	}

	@Override
	public float getChunkRarity() {
		return chunkRarity;
	}

	@Override
	public float getDepth() {
		return depth;
	}

	@Override
	public float getHardness() {
		return hardness;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getNodeSize() {
		return nodeSize;
	}

	@Override
	public int getNodesPerChunk() {
		return nodesPerChunk;
	}

	public ArrayList<ItemStack> getOreListFromPrimaryComponent() {
		return OreDictionary.getOres(Utility.fixCamelCase("ore", getPrimaryComponent()));
	}

	public ArrayList<ItemStack> getOreListFromSecondaryComponent() {
		return OreDictionary.getOres(Utility.fixCamelCase("ore", getSecondaryComponent()));
	}

	@Override
	public String getPrimaryComponent() {
		return primary;
	}

	public String getPrimaryOreDict() {
		return Utility.fixCamelCase("ore", getPrimaryComponent());
	}

	@Override
	public float getResistance() {
		return resistance;
	}

	@Override
	public String getSecondaryComponent() {
		return secondary;
	}

	public String getSecondaryOreDict() {
		return Utility.fixCamelCase("ore", getSecondaryComponent());
	}

	@Override
	public float getSpread() {
		return spread;
	}

	@Override
	public boolean isAlloy() {
		return alloy;
	}

	@Override
	public boolean isNonCommon() {
		return nonCommonAlloy;
	};

	public void registerOre() {
		GameRegistry.registerBlock(ore, "ore" + nameFixed);
		GameRegistry.registerItem(dust, "dust" + nameFixed);
		GameRegistry.registerItem(ingot, "ingot" + nameFixed);
		GameRegistry.registerItem(nugget, "nugget" + nameFixed);
		GameRegistry.registerBlock(storage, "storage" + nameFixed);
		OreDictionary.registerOre("ore" + nameFixed, ore);
		OreDictionary.registerOre("dust" + nameFixed, dust);
		OreDictionary.registerOre("pulv" + nameFixed, dust);
		OreDictionary.registerOre("ingot" + nameFixed, ingot);
		OreDictionary.registerOre("nugget" + nameFixed, nugget);
		OreDictionary.registerOre("storage" + nameFixed, storage);
		OreDictionary.registerOre("block" + nameFixed, storage);
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(ore));
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(storage));
	}

	public void registerRecipes() {
		GameRegistry.addSmelting(ore, new ItemStack(ingot), 10);
		GameRegistry.addSmelting(dust, new ItemStack(ingot), 10);
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(nugget, 9), "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(storage), "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed, "ingot" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot), "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed, "nugget" + nameFixed));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ingot, 9), "block" + nameFixed));
	}

	public void registerAlloyRecipes() {
		// TODO: Add alloy recipes.
	}

	public void setComponents(final Metal primary, final Metal secondary) {
		setComponents(primary.name, secondary.name, false);
	}

	public void setComponents(final String primary, final String secondary) {
		setComponents(primary, secondary, true);
	}

	private void setComponents(final String primary, final String secondary, final boolean nonCommon) {
		this.primary = primary;
		this.secondary = secondary;
		alloy = true;
		nonCommonAlloy = nonCommon;
	}

	public void setGeneration(final boolean generate) {
		if (!this.generate && generate)
			CommonOre.log.info("[CommonOre] Setting " + nameFixed + " to generate");
		this.generate = generate;
	}

	public Metal setup(final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance) {
		this.chunkRarity = chunkRarity;
		this.depth = depth;
		this.nodesPerChunk = nodesPerChunk;
		this.nodeSize = nodeSize;
		this.spread = spread;
		this.hardness = hardness;
		this.resistance = resistance;
		ore.setHardness(hardness);
		ore.setResistance(resistance);
		storage.setHardness(hardness);
		storage.setResistance(resistance);
		return this;
	}

	public boolean willGenerate() {
		return generate;
	}
}
