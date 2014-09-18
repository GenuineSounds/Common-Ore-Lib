package com.genuineminecraft.ores.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.genuineminecraft.ores.CommonOres;
import com.genuineminecraft.ores.registry.OreRegistry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {

	public Map<String, Integer> chunkRarityMap = new HashMap<String, Integer>();
	public Map<String, Integer> nodesPerChunkMap = new HashMap<String, Integer>();
	public Map<String, Integer> nodeSizeMap = new HashMap<String, Integer>();
	public Map<String, Integer> depthMap = new HashMap<String, Integer>();
	public Map<String, Integer> spreadMap = new HashMap<String, Integer>();
	public Map<String, String> pairMap = new HashMap<String, String>();
	public boolean flatBedrock;
	public boolean genAlloys;
	public boolean rareAlloys;
	public int searchRadius;

	public Config(FMLPreInitializationEvent event) {
		File folder = new File(event.getModConfigurationDirectory() + "/" + CommonOres.MODID);
		folder.mkdirs();
		Configuration oreGenCfg = new Configuration(new File(folder, CommonOres.MODID + " - Ore Generation.cfg"));
		Configuration mainCfg = new Configuration(new File(folder, CommonOres.MODID + " - Main.cfg"));
		for (int i = 0; i < OreRegistry.commonList.size(); i++) {
			String name = OreRegistry.commonList.get(i);
			int depth = (int) (((1D / (double) OreRegistry.commonList.size()) * 64D) * i);
			nodesPerChunkMap.put(name, oreGenCfg.getInt("nodesPerChunk", name, 4, 0, 8, "How many nodeshave a chance to generate in a chunk"));
			chunkRarityMap.put(name, oreGenCfg.getInt("chunkRarity", name, 100, 0, 100, "Chance percent of generating in any chunk"));
			nodeSizeMap.put(name, oreGenCfg.getInt("nodeSize", name, 6, 0, 16, "Node size"));
			depthMap.put(name, oreGenCfg.getInt("depth", name, depth, 0, 256, "Depth at which the ore is most common"));
			spreadMap.put(name, oreGenCfg.getInt("spread", name, 6, 0, 256, "How far can the ore deviate from its depth"));
		}
		// Main Config
		flatBedrock = mainCfg.getBoolean("flatBedrock", "Options", true, "Generate flat bedrock in the Overworld and nether.");
		genAlloys = mainCfg.getBoolean("genAlloys", "Options", true, "Generate alloy ores in world. Might break balance if rare alloys is not enabled. (recommended)");
		rareAlloys = mainCfg.getBoolean("rareAlloys", "Options", true, "Generation of alloy ores only occur when the two major component of the ore appear close together. (recommended)");
		searchRadius = mainCfg.getInt("searchRadius", "Options", 5, 0, 16, "Radius for rare alloys to search in each direction to find their component ores");
		oreGenCfg.save();
		mainCfg.save();
	}
}
