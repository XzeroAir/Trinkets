package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.hud.mana.ManaHud;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.util.TrinketsConfig;

public class RenderEntitiesEvent {

	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		final EntityPlayer player = event.getEntityPlayer();
		final EntityProperties cap = Capabilities.getEntityRace(player);
		//		System.out.println(Minecraft.getMinecraft().currentScreen);
		if (cap != null) {
			final int size = cap.getSize();
			final float scale = size * 0.01F;
			GlStateManager.pushMatrix();
			final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
			if ((player == Minecraft.getMinecraft().player) && (screen != null) && !((screen instanceof GuiChat) || (screen instanceof GuiEntityProperties) || (screen instanceof ManaHud))) {
				return;
			}
			final float W = 1F;
			if (cap.isTransforming()) {
				//				if ((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
				//					scale = (size * 0.01F) * 0.5F;
				//					System.out.println(scale);
				//					GlStateManager.scale(scale, scale, scale);
				//				} else {
				GlStateManager.scale(scale, scale, scale);
				GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
				//				}
			}

			if (cap.isTransformed()) {
				if ((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
					//					if (cap.getSize() == 25) {
					//						W = ((cap.getSize() * 2) * 0.01f);
					//						GlStateManager.scale(W, 1, W);
					//					}
					//					if (player.isSneaking()) {
					//						GlStateManager.translate(0, 0.08D, 0);
					//					}
				} else {
					GlStateManager.scale(scale, scale, scale);
					GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
				}
			}
			cap.getRaceProperties().doRenderPlayerPre(player, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
		}
		//		boolean moving = ((player.getHorizontalFacing().getDirectionVec().getX() * player.motionX) > 0) || ((player.getHorizontalFacing().getDirectionVec().getZ() * player.motionZ) > 0);
		//		if (moving && (player.isInWater() || player.isInLava())) {
		//			GlStateManager.translate(0, -(cap.getHeight() / 2), 0);
		//		}
	}

	@SubscribeEvent
	public void renderPlayerPost(RenderPlayerEvent.Post event) {
		final EntityPlayer player = event.getEntityPlayer();
		final EntityProperties cap = Capabilities.getEntityRace(player);
		if (cap != null) {
			cap.getRaceProperties().doRenderPlayerPost(player, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
			GlStateManager.popMatrix();
		}
	}

	@SubscribeEvent
	public void onRenderSpecialPre(RenderLivingEvent.Specials.Pre event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final EntityProperties cap = Capabilities.getEntityRace(player);
			if (cap != null) {
				if (!cap.getCurrentRace().equals(EntityRaces.none)) {
					GlStateManager.pushMatrix();
					final float scale = (float) cap.getTargetSize() / 100;
					GlStateManager.translate(0, 0.3 / scale, 0);
				}
				cap.getRaceProperties().doRenderLivingSpecialsPre(event);
			}
		}
	}

	@SubscribeEvent
	public void onRenderSpecialPost(RenderLivingEvent.Specials.Post event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final EntityProperties cap = Capabilities.getEntityRace(player);
			if (cap != null) {
				cap.getRaceProperties().doRenderLivingSpecialsPost(event);
				if (!cap.getCurrentRace().equals(EntityRaces.none)) {
					GlStateManager.popMatrix();
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderLivingPre(RenderLivingEvent.Pre event) {
		if ((event.getEntity() != null) && !(event.getEntity() instanceof EntityPlayer)) {
			final EntityLivingBase entity = event.getEntity();
			final EntityProperties cap = Capabilities.getEntityRace(entity);
			if (cap != null) {
				final int size = cap.getSize();
				final float scale = size * 0.01F;
				if (!cap.getCurrentRace().equals(EntityRaces.none)) {
					GlStateManager.pushMatrix();
					if ((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
						float W = 1F;
						if (cap.getSize() < 50) {
							W = ((cap.getSize() * 2) * 0.01f);
						}
						if (cap.getSize() != cap.getTargetSize()) {
							GlStateManager.scale(scale, scale, scale);
							GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
						} else {
							if (cap.getSize() == 25) {
								GlStateManager.scale(W, 1, W);
							}
						}
						cap.getRaceProperties().doRenderLivingPre(event);
					} else {
						GlStateManager.scale(scale, scale, scale);
						GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
						cap.getRaceProperties().doRenderLivingPre(event);
					}
				} else {
					if (cap.isTransforming()) {
						GlStateManager.pushMatrix();
						GlStateManager.scale(scale, scale, scale);
						GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
					}
				}
				cap.getRaceProperties().doRenderLivingPre(event);
			}
		}
	}

	@SubscribeEvent
	public void onRenderLivingPost(RenderLivingEvent.Post event) {
		if ((event.getEntity() != null) && !(event.getEntity() instanceof EntityPlayer)) {
			final EntityLivingBase entity = event.getEntity();
			final EntityProperties cap = Capabilities.getEntityRace(entity);
			if (cap != null) {
				cap.getRaceProperties().doRenderLivingPost(event);
				if (!cap.getCurrentRace().equals(EntityRaces.none)) {
					GlStateManager.popMatrix();
				} else {
					if (cap.isTransforming()) {
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderHand(RenderHandEvent event) {

	}

	@SubscribeEvent
	public void onRenderSpecificHand(RenderSpecificHandEvent event) {

	}

}
