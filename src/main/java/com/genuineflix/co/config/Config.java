package com.genuineflix.co.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.genuineflix.co.metals.Metal;
import com.genuineflix.co.registry.MetalRegistry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Config {

	public boolean flatBedrock;
	public boolean genAlloys;
	public boolean rareAlloys;
	public int searchRadius;
	public Configuration oreGenCfg;
	public Configuration mainCfg;

	public Config(final FMLPreInitializationEvent event) {
		final File folder = new File(event.getModConfigurationDirectory(), "CommonOre");
		folder.mkdirs();
		mainCfg = new Configuration(new File(folder, "Main.cfg"));
		mainCfg.save();
		oreGenCfg = new Configuration(new File(folder, "Ore Generation.cfg"));
		oreGenCfg.save();
	}

	public void pre() {
		genAlloys = mainCfg.getBoolean("genAlloys", "Options", true, "Generate alloy ores in world. Will break balance of other mods if rareAlloys is disabled (recommended)");
		rareAlloys = mainCfg.getBoolean("rareAlloys", "Options", true, "Generation of alloy ores only occur when the two components of the ore appear close together (recommended)");
		flatBedrock = mainCfg.getBoolean("flatBedrock", "Options", false, "Generate flat bedrock in the Over-world and Nether. Off by default since you probably already have a mod that does this.");
		searchRadius = mainCfg.getInt("searchRadius", "Options", 4, 0, 16, "Radius for rare alloys to search in each direction to find their component ores. Higher numbers will take longer to generate");
		mainCfg.save();
	}

	public void init() {}

	public void post() {
		for (int i = 0; i < MetalRegistry.instance.getAllMetals().size(); i++) {
			final Metal metal = MetalRegistry.instance.getAllMetals().get(i);
			final float rarity = oreGenCfg.getFloat("chunkRarity", metal.nameFixed, metal.getChunkRarity(), 0, 1F, "Chance of generating in any chunk (Percentage)");
			final float depth = oreGenCfg.getFloat("depth", metal.nameFixed, metal.getDepth(), 0, 1F, "Depth at which the ore is most common (Percentage)");
			final int nodes = oreGenCfg.getInt("nodesPerChunk", metal.nameFixed, metal.getNodesPerChunk(), 0, 8, "How many nodes have a chance to generate in a chunk");
			final int size = oreGenCfg.getInt("nodeSize", metal.nameFixed, metal.getNodeSize(), 0, 16, "How many ore can generate in each node");
			final float spread = oreGenCfg.getFloat("spread", metal.nameFixed, metal.getSpread(), 0, 1F, "How far can the ore deviate from its depth (Percentage)");
			final float hardness = oreGenCfg.getFloat("hardness", metal.nameFixed, metal.getHardness(), 0, 100F, "How easy is this metal to harvest");
			final float resistance = oreGenCfg.getFloat("resistance", metal.nameFixed, metal.getResistance(), 0, 100F, "How resistant are the blocks to explosions");
			metal.setup(rarity, depth, nodes, size, spread, hardness, resistance);
			final boolean generate = oreGenCfg.getBoolean("resistance", metal.nameFixed, metal.willGenerate(), "Whether this ore will generate");
			metal.setGeneration(generate);
		}
		oreGenCfg.save();
	}
}
