package com.genuineminecraft.ores.events;

import net.minecraftforge.event.terraingen.OreGenEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreGenerationEvent {

	@SubscribeEvent
	public void minable(OreGenEvent.GenerateMinable event) {
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
}
