package com.genuineminecraft.ores.events;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.OreGenEvent;
import cpw.mods.fml.common.Mod.EventHandler;

public class OreEvents {

	private Map<ChunkCoordIntPair, Chunk> map = new HashMap<ChunkCoordIntPair, Chunk>();

	@EventHandler
	public void minable(OreGenEvent.GenerateMinable event) {
		switch (event.type) {
			case IRON:
			case GOLD:
			case DIRT:
			case GRAVEL:
				event.setCanceled(true);
				break;
			case COAL:
			case DIAMOND:
			case LAPIS:
			case QUARTZ:
			case REDSTONE:
			case CUSTOM:
			default:
				break;
		}
	}

	@EventHandler
	public void postGen(OreGenEvent.Post event) {
		World world = event.world;
		ChunkCoordIntPair pair = new ChunkCoordIntPair(event.worldX, event.worldZ);
		Chunk chunk = this.map.get(pair);
		if (chunk != null) {
			this.map.remove(pair);
			System.out.println("Removed cached chunk");
		}
	}

	@EventHandler
	public void preGen(OreGenEvent.Pre event) {
		this.map.put(new ChunkCoordIntPair(event.worldX, event.worldZ), event.world.getChunkFromChunkCoords(event.worldX, event.worldZ));
	}
}
