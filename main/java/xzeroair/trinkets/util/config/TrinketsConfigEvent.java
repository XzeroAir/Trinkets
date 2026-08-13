package xzeroair.trinkets.util.config;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketsConfigEvent {

	public static TrinketsConfigEvent instance = new TrinketsConfigEvent();

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MODID)) {
			ConfigHelper.refreshAttributeCacheMap();
			TrinketsConfig.Save();
			ConfigHelper.TrinketConfigStorage.init();
		}
	}

}
