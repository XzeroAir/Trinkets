package xzeroair.trinkets.util.compat.morph;

import me.ichun.mods.morph.api.event.MorphEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class MorphEventHandler {

	@SubscribeEvent
	public void event(MorphEvent event) {
		if(TrinketsConfig.compat.morph && (event.getEntityPlayer() != null)) {
			if(TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketFairyRing) || TrinketHelper.AccessoryCheck(event.getEntityPlayer(), ModItems.trinkets.TrinketDwarfRing)) {
				event.setCanceled(true);
			}
		}
	}

}
