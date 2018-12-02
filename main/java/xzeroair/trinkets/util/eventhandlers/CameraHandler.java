package xzeroair.trinkets.util.eventhandlers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class CameraHandler {

	private static Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void FOVUpdate(FOVUpdateEvent event){
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = event.getEntity();
			if(TrinketHelper.baubleCheck(player, ModItems.small_ring)) {
				if(mc.gameSettings.thirdPersonView > 0) {
					event.setNewfov((event.getFov() / 90.0f) * 60.0f);
				}
				//										event.setNewfov(event.getFov()*0.9f);
				//					event.getNewfov();
			}
		}
	}

	@SubscribeEvent
	public void EntityViewRender(EntityViewRenderEvent.CameraSetup event) {
		//		if(org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_T)) {
		//					event.setYaw(0.1f);
		//					event.setRoll(event.getEntity().ticksExisted);
		//					event.setPitch(0.1f);
		//		System.out.println(event.getResult());
	}

}
