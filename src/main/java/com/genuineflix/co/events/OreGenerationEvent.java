package com.genuineflix.co.events;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.OreGenEvent;

import com.genuineflix.co.utils.ChunkMap;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreGenerationEvent {

	public static ChunkMap<Chunk> chunkCache = new ChunkMap<Chunk>();

	@SubscribeEvent
	public void minable(final OreGenEvent.GenerateMinable event) {
		switch (event.type) {
			case IRON:
			case GOLD:
			case DIRT:
			case GRAVEL:
				event.setResult(Result.DENY);
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

	@SubscribeEvent
	public void cancelShit(final OreGenEvent.Pre event) {
		final World world = event.world;
		final Chunk chunk = world.getChunkFromBlockCoords(event.worldX, event.worldZ);
		OreGenerationEvent.chunkCache.put(chunk, chunk);
	}
}
