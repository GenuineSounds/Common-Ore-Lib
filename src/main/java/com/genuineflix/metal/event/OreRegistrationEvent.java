package com.genuineflix.metal.event;

import com.genuineflix.metal.registry.MetalRegistry;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

public class OreRegistrationEvent {

	@SubscribeEvent
	public void registerOreEvent(final OreRegisterEvent event) {
		if (!MetalRegistry.isMetal(event.Name) || !(event.Ore.getItem() instanceof ItemBlock))
			return;
		MetalRegistry.registrationEvent(event.Name, ((ItemBlock) event.Ore.getItem()).block, event.Ore.getItemDamage());
	}
}
