package com.genuineminecraft.ores.config;

import java.util.HashMap;
import java.util.Map;

public class Config {

	public Map<String, Integer> chunkRarityMap = new HashMap<String, Integer>();
	public Map<String, Integer> nodesPerChunkMap = new HashMap<String, Integer>();
	public Map<String, Integer> nodeSizeMap = new HashMap<String, Integer>();
	public Map<String, Integer> depthMap = new HashMap<String, Integer>();
	public Map<String, Integer> spreadMap = new HashMap<String, Integer>();
	public Map<String, String> pairMap = new HashMap<String, String>();

	public Config() {}
}
