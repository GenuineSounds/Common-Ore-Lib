package com.genuineminecraft.ores.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.genuineminecraft.ores.metals.Metal;
import com.genuineminecraft.ores.registry.MetalRegistry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Config {

	public boolean flatBedrock;
	public boolean genAlloys;
	public boolean rareAlloys;
	public int searchRadius;
	public Configuration oreGenCfg;
	public Configuration mainCfg;

	public Config(FMLPreInitializationEvent event) {
		File folder = new File(event.getModConfigurationDirectory(), "CommonOre");
		folder.mkdirs();
		this.mainCfg = new Configuration(new File(folder, "Main.cfg"));
		this.mainCfg.save();
		this.oreGenCfg = new Configuration(new File(folder, "Ore Generation.cfg"));
		this.oreGenCfg.save();
	}

	public void init() {}

	public void postInit() {
		for (int i = 0; i < MetalRegistry.getMetals().size(); i++) {
			Metal metal = MetalRegistry.getMetals().get(i);
			float rarity = this.oreGenCfg.getFloat("chunkRarity", metal.name, metal.getChunkRarity(), 0, 1F, "Chance of generating in any chunk (Percentage)");
			float depth = this.oreGenCfg.getFloat("depth", metal.name, metal.getDepth(), 0, 1F, "Depth at which the ore is most common (Percentage)");
			int nodes = this.oreGenCfg.getInt("nodesPerChunk", metal.name, metal.getNodesPerChunk(), 0, 8, "How many nodes have a chance to generate in a chunk");
			int size = this.oreGenCfg.getInt("nodeSize", metal.name, metal.getNodeSize(), 0, 16, "How many ore can generate in each node");
			float spread = this.oreGenCfg.getFloat("spread", metal.name, metal.getSpread(), 0, 1F, "How far can the ore deviate from its depth (Percentage)");
			float hardness = this.oreGenCfg.getFloat("hardness", metal.name, metal.getHardness(), 0, 100F, "How easy is this metal to harvest");
			float resistance = this.oreGenCfg.getFloat("resistance", metal.name, metal.getResistance(), 0, 100F, "How resistant are the blocks to explosions");
			metal.setup(rarity, depth, nodes, size, spread, hardness, resistance);
		}
		this.oreGenCfg.save();
	}

	public void preInit() {
		this.genAlloys = this.mainCfg.getBoolean("genAlloys", "Options", true, "Generate alloy ores in world. Will break balance of other mods if rareAlloys is disabled (recommended)");
		this.rareAlloys = this.mainCfg.getBoolean("rareAlloys", "Options", true, "Generation of alloy ores only occur when the two components of the ore appear close together (recommended)");
		this.flatBedrock = this.mainCfg.getBoolean("flatBedrock", "Options", false, "Generate flat bedrock in the Over-world and Nether");
		this.searchRadius = this.mainCfg.getInt("searchRadius", "Options", 4, 0, 16, "Radius for rare alloys to search in each direction to find their component ores. Higher numbers will take longer to generate");
		this.mainCfg.save();
	}
}
