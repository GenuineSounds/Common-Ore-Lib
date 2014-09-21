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

	public Map<String, Float> chunkRarityMap = new HashMap<String, Float>();
	public Map<String, Integer> nodesPerChunkMap = new HashMap<String, Integer>();
	public Map<String, Integer> nodeSizeMap = new HashMap<String, Integer>();
	public Map<String, Float> depthMap = new HashMap<String, Float>();
	public Map<String, Float> spreadMap = new HashMap<String, Float>();
	public Map<String, String> pairMap = new HashMap<String, String>();
	public boolean flatBedrock;
	public boolean genAlloys;
	public boolean rareAlloys;
	public int searchRadius;
	Configuration oreGenCfg;
	Configuration mainCfg;

	public Config(FMLPreInitializationEvent event) {
		File folder = new File(event.getModConfigurationDirectory() + "/" + CommonOres.MODID);
		folder.mkdirs();
		oreGenCfg = new Configuration(new File(folder, CommonOres.MODID + " - Ore Generation.cfg"));
		mainCfg = new Configuration(new File(folder, CommonOres.MODID + " - Main.cfg"));
		// Main Config
		flatBedrock = mainCfg.getBoolean("flatBedrock", "Options", true, "Generate flat bedrock in the Overworld and nether.");
		genAlloys = mainCfg.getBoolean("genAlloys", "Options", true, "Generate alloy ores in world. Might break balance if rare alloys is not enabled. (recommended)");
		rareAlloys = mainCfg.getBoolean("rareAlloys", "Options", true, "Generation of alloy ores only occur when the two major component of the ore appear close together. (recommended)");
		searchRadius = mainCfg.getInt("searchRadius", "Options", 5, 0, 16, "Radius for rare alloys to search in each direction to find their component ores");
		mainCfg.save();
	}

	public void load(FMLInitializationEvent event) {
		for (int i = 0; i < OreRegistry.getInstance().metals.size(); i++) {
			Metal metal = OreRegistry.getInstance().metals.get(i);
			float depth = (float) i / (float) OreRegistry.getInstance().metals.size();
			nodesPerChunkMap.put(metal.name, oreGenCfg.getInt("nodesPerChunk", metal.name, 4, 0, 8, "How many nodeshave a chance to generate in a chunk"));
			chunkRarityMap.put(metal.name, oreGenCfg.getFloat("chunkRarity", metal.name, 1, 0, 1, "Chance percent of generating in any chunk"));
			nodeSizeMap.put(metal.name, oreGenCfg.getInt("nodeSize", metal.name, 6, 0, 16, "Node size"));
			depthMap.put(metal.name, oreGenCfg.getFloat("depth", metal.name, depth, 0, 1F, "Depth at which the ore is most common"));
			spreadMap.put(metal.name, oreGenCfg.getFloat("spread", metal.name, 0.1F, 0, 1F, "How far can the ore deviate from its depth"));
		}
		oreGenCfg.save();
	}
}
