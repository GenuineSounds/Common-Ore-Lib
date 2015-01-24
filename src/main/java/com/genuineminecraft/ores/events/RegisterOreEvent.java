package com.genuineminecraft.ores.events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.genuineminecraft.ores.registry.MetalRegistry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RegisterOreEvent {

	@SubscribeEvent
	public void registerOreEvent(OreRegisterEvent event) {
		MetalRegistry.getInstance().registerGeneration(event.Name, event.Ore);
	}
}
