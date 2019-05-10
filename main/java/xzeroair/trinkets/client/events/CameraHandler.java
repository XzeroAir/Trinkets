package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class CameraHandler {

	private static Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void FOVUpdate(FOVUpdateEvent event){
		if(event.getEntity() != null) {
			final EntityPlayer player = event.getEntity();
			if(TrinketHelper.baubleCheck(player, ModItems.fairy_ring)) {
				if(mc.gameSettings.thirdPersonView > 0) {
					event.setNewfov((event.getFov() / 90.0f) * 60.0f);
				}
			}
		}
	}
}
