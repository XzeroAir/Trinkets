package xzeroair.trinkets.util.compat.morph;

import me.ichun.mods.morph.api.event.MorphEvent;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TrinketHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper.TrinketType;

public class MorphEventHandler {

	@SubscribeEvent
	public void event(MorphEvent event) {
		if(event.getEntityPlayer() != null) {
			Item ringCheck = TrinketHelper.getBaubleType(event.getEntityPlayer(), TrinketType.rings);
			if((ringCheck == ModItems.small_ring) || (ringCheck == ModItems.dwarf_ring)) {
				event.setCanceled(true);
			}
		}
	}

}
