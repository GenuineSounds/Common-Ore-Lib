package com.genuineminecraft.ores.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

import com.genuineminecraft.ores.CommonOres;
import com.genuineminecraft.ores.metals.Metal;
import com.genuineminecraft.ores.registry.OreRegistry;

import cpw.mods.fml.common.event.FMLInitializationEvent;
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
		mainCfg = new Configuration(new File(folder, "Main.cfg"));
		oreGenCfg = new Configuration(new File(folder, "Ore Generation.cfg"));
		genAlloys = mainCfg.getBoolean("genAlloys", "Options", true, "Generate alloy ores in world. Might break balance if rare alloys is not enabled. (recommended)");
		rareAlloys = mainCfg.getBoolean("rareAlloys", "Options", true, "Generation of alloy ores only occur when the two major component of the ore appear close together. (recommended)");
		flatBedrock = mainCfg.getBoolean("flatBedrock", "Options", true, "Generate flat bedrock in the Overworld and nether.");
		searchRadius = mainCfg.getInt("searchRadius", "Options", 5, 0, 16, "Radius for rare alloys to search in each direction to find their component ores");
		mainCfg.save();
	}

	public void load() {
		for (int i = 0; i < OreRegistry.getInstance().metals.size(); i++) {
			Metal metal = OreRegistry.getInstance().metals.get(i);
			float rarity = oreGenCfg.getFloat("chunkRarity", metal.name, 1, 0, 1, "Chance percent of generating in any chunk");
			float depth = oreGenCfg.getFloat("depth", metal.name, (float) i / (float) OreRegistry.getInstance().metals.size(), 0, 1F, "Depth at which the ore is most common");
			int nodes = oreGenCfg.getInt("nodesPerChunk", metal.name, 4, 0, 8, "How many nodeshave a chance to generate in a chunk");
			int size = oreGenCfg.getInt("nodeSize", metal.name, 6, 0, 16, "Node size");
			float spread = oreGenCfg.getFloat("spread", metal.name, 0.1F, 0, 1F, "How far can the ore deviate from its depth");
			metal.setup(rarity, depth, nodes, size, spread);
		}
		oreGenCfg.save();
	}

	public void post() {
		for (Metal metal : OreRegistry.getInstance().metals) {}
	}
}
