package com.genuineflix.metal.event;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.genuineflix.metal.registry.MetalRegistry;
import com.genuineflix.metal.registry.RegistryHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreRegistrationEvent {

	@SubscribeEvent
	public void registerOreEvent(final OreRegisterEvent event) {
		if (!RegistryHelper.isCommonName(event.Name) || !(event.Ore.getItem() instanceof ItemBlock))
			return;
		MetalRegistry.registrationEvent(event.Name, ((ItemBlock) event.Ore.getItem()).field_150939_a, event.Ore.getItemDamage());
	}
}
