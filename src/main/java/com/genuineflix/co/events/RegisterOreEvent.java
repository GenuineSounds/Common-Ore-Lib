package com.genuineflix.co.events;

import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.genuineflix.co.registry.MetalRegistry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RegisterOreEvent {

	@SubscribeEvent
	public void registerOreEvent(final OreRegisterEvent event) {
		MetalRegistry.getInstance().registerGeneration(event.Name, event.Ore);
	}
}
