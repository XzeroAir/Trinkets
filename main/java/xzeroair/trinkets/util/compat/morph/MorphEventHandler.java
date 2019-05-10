package xzeroair.trinkets.util.compat.morph;

import me.ichun.mods.morph.api.event.MorphEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class MorphEventHandler {

	@SubscribeEvent
	public void event(MorphEvent event) {
		if(event.getEntityPlayer() != null) {
			if(TrinketHelper.baubleCheck(event.getEntityPlayer(), ModItems.fairy_ring) || TrinketHelper.baubleCheck(event.getEntityPlayer(), ModItems.dwarf_ring)) {
				event.setCanceled(true);
			}
		}
	}

}
