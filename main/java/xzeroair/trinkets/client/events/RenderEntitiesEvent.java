package xzeroair.trinkets.client.events;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.Capabilities;

public class RenderEntitiesEvent {

	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		final EntityPlayer player = event.getEntityPlayer();
		Capabilities.getEntityProperties(
				player, cap -> {
					GlStateManager.pushMatrix();
					cap.getRaceHandler().doRenderPlayerPre(player, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
				}
		);

	}

	@SubscribeEvent
	public void renderPlayerPost(RenderPlayerEvent.Post event) {
		final EntityPlayer player = event.getEntityPlayer();
		Capabilities.getEntityProperties(
				player, cap -> {
					cap.getRaceHandler().doRenderPlayerPost(player, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
					GlStateManager.popMatrix();
				}
		);
	}

	@SubscribeEvent
	public void onRenderSpecialPre(RenderLivingEvent.Specials.Pre event) {
		final EntityLivingBase entity = event.getEntity();
		Capabilities.getEntityProperties(
				entity, cap -> {
					cap.getRaceHandler().doRenderLivingSpecialsPre(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
				}
		);
	}

	@SubscribeEvent
	public void onRenderSpecialPost(RenderLivingEvent.Specials.Post event) {
		final EntityLivingBase entity = event.getEntity();
		Capabilities.getEntityProperties(
				entity, cap -> {
					cap.getRaceHandler().doRenderLivingSpecialsPost(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
				}
		);
	}

	@SubscribeEvent
	public void onRenderLivingPre(RenderLivingEvent.Pre event) {
		final EntityLivingBase entity = event.getEntity();
		if (!(entity instanceof EntityPlayer)) {
			Capabilities.getEntityProperties(
					entity, cap -> {
						cap.getRaceHandler().doRenderLivingPre(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
					}
			);
		}
	}

	@SubscribeEvent
	public void onRenderLivingPost(RenderLivingEvent.Post event) {
		final EntityLivingBase entity = event.getEntity();
		if (!(entity instanceof EntityPlayer)) {
			Capabilities.getEntityProperties(
					entity, cap -> {
						cap.getRaceHandler().doRenderLivingPost(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
					}
			);
		}
	}

	//	@SubscribeEvent
	//	public void onRenderHand(RenderHandEvent event) {
	//	}

	//	@SubscribeEvent
	//	public void onRenderSpecificHand(RenderSpecificHandEvent event) {
	//		final EntityPlayerSP player = Minecraft.getMinecraft().player;
	//		Capabilities.getEntityRace(
	//				player, cap -> {
	//					cap.getRaceHandler().doRenderHand(event.getHand(), event.getItemStack(), event.getSwingProgress(), event.getInterpolatedPitch(), event.getEquipProgress(), event.getPartialTicks());
	//				}
	//		);
	//	}

}
