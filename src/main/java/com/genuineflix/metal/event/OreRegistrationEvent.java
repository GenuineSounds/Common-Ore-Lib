package com.genuineflix.metal.event;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.genuineflix.metal.registry.Metal;
import com.genuineflix.metal.registry.MetalRegistry;
import com.genuineflix.metal.util.StringHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreRegistrationEvent {

	@SubscribeEvent
	public void registerOreEvent(final OreRegisterEvent event) {
		if (!MetalRegistry.isCommonName(event.Name) || !(event.Ore.getItem() instanceof ItemBlock))
			return;
		final Block block = ((ItemBlock) event.Ore.getItem()).field_150939_a;
		final int meta = event.Ore.getItemDamage();
		MetalRegistry.cacheCommonBlock(event.Name, block, meta);
		if (MetalRegistry.isRegistryClosed())
			return;
		final Metal metal = MetalRegistry.getMetal(StringHelper.cleanName(event.Name));
		if (metal == null)
			return;
		metal.setGeneration(true);
	}
}
