package ninja.genuine.metal.event;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import ninja.genuine.metal.registry.MetalRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreRegistrationEvent {

	@SubscribeEvent
	public void registerOreEvent(final OreRegisterEvent event) {
		if (!MetalRegistry.isMetal(event.Name) || !(event.Ore.getItem() instanceof ItemBlock))
			return;
		MetalRegistry.registrationEvent(event.Name, ((ItemBlock) event.Ore.getItem()).field_150939_a, event.Ore.getItemDamage());
	}
}
