package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;

public class CameraHandler {

	private static Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void FOVUpdate(FOVUpdateEvent event){
		if(event.getEntity() != null) {
			final EntityPlayer player = event.getEntity();
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			if((cap != null)) {
				if((TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing) || cap.getFood().contentEquals("fairy_dew")) && !TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
					if(mc.gameSettings.thirdPersonView > 0) {
						event.setNewfov((event.getFov() / 90.0f) * 60.0f);
					}
				}
			}
		}
	}
}
