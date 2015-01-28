package com.genuineflix.co.metals;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.genuineflix.co.blocks.Ore;
import com.genuineflix.co.blocks.Storage;
import com.genuineflix.co.interfaces.IAlloy;
import com.genuineflix.co.interfaces.IOre;
import com.genuineflix.co.items.Dust;
import com.genuineflix.co.items.Ingot;
import com.genuineflix.co.items.Nugget;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class Metal implements IOre, IAlloy {

	public final String name;
	public final String nameFixed;
	public final Ore ore;
	public final Storage storage;
	public final Dust dust;
	public final Ingot ingot;
	public final Nugget nugget;
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
	private boolean generate;

	public Metal(final String name) {
		this.name = name;
		nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		ore = new Ore(this);
		dust = new Dust(this);
		ingot = new Ingot(this);
		nugget = new Nugget(this);
		storage = new Storage(this);
	}

	public void setGeneration(final boolean generate) {
		this.generate = generate;
	}

	public boolean willGenerate() {
		return generate;
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

	@Override
	public String getPrimaryComponent() {
		return primary;
	}

	@Override
	public float getResistance() {
		return resistance;
	}

	@Override
	public String getSecondaryComponent() {
		return secondary;
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
	public void setComponents(final String primary, final String secondary) {
		this.primary = primary;
		this.secondary = secondary;
		alloy = true;
	}

	private void setComponents(final Metal primary, final Metal secondary) {
		this.primary = primary.name;
		this.secondary = secondary.name;
		alloy = true;
	}

	public Metal setup(final float chunkRarity, final float depth, final int nodesPerChunk, final int nodeSize, final float spread, final float hardness, final float resistance) {
		this.chunkRarity = chunkRarity;
		this.depth = depth;
		this.nodesPerChunk = nodesPerChunk;
		this.nodeSize = nodeSize;
		this.spread = spread;
		this.hardness = hardness;
		this.resistance = resistance;
		ore.setup();
		storage.setup();
		return this;
	}

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
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(ore));
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(storage));
	}

	public void registerRecipes() {
		GameRegistry.addSmelting(ore, new ItemStack(ingot), 10);
		GameRegistry.addSmelting(dust, new ItemStack(ingot), 10);
		GameRegistry.addShapelessRecipe(new ItemStack(nugget, 9), ingot);
		GameRegistry.addShapelessRecipe(new ItemStack(storage), ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot, ingot);
		GameRegistry.addShapelessRecipe(new ItemStack(ingot), nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget, nugget);
		GameRegistry.addShapelessRecipe(new ItemStack(ingot, 9), storage);
	}
}
