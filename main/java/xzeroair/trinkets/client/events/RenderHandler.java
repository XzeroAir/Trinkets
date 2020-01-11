package xzeroair.trinkets.client.events;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.effects.EffectsDragonsEye;
import xzeroair.trinkets.util.TrinketsConfig;

public class RenderHandler {

	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if((mc.player != null) && (mc.player.world != null)) {

		}
	}

	@SubscribeEvent
	public void renderFogColor(EntityViewRenderEvent.FogColors event) {

	}

	@SubscribeEvent
	public void renderFog(EntityViewRenderEvent.FogDensity event) {
		if(event.getEntity() instanceof EntityPlayer) {
			if(TrinketHelper.AccessoryCheck((EntityPlayer) event.getEntity(), ModItems.trinkets.TrinketDragonsEye)) {
				if(event.getEntity().isInLava() && (event.getState().getMaterial() == Material.LAVA)) {
					if((event.getDensity() >= 0.1f) && !event.isCanceled()) {
						event.setCanceled(true);
						event.setDensity(0.1f);
					}
				}
			}
			if(TrinketHelper.AccessoryCheck((EntityPlayer) event.getEntity(), ModItems.trinkets.TrinketSea)) {
				if(event.getEntity().isInWater() && (event.getState().getMaterial() == Material.WATER)) {
					if((event.getDensity() >= 0.1f) && !event.isCanceled()) {
						event.setCanceled(true);
						event.setDensity(0.02f);
					}
				}
			}
		}

	}

	@SubscribeEvent
	public void renderGameOverlay(RenderGameOverlayEvent event) {
	}

	@SubscribeEvent
	public void renderBlockOverlay(RenderBlockOverlayEvent event) {
		if(event.getOverlayType() == OverlayType.FIRE) {
			if(TrinketHelper.AccessoryCheck(event.getPlayer(), ModItems.trinkets.TrinketDragonsEye)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		final EntityPlayer player = event.getEntityPlayer();
		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if(cap != null) {
			final int size = cap.getSize();
			final float scale = (float) size/100;
			if((cap.getTrans() == true)) {
				GlStateManager.pushMatrix();
				if((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
					if(cap.getSize() != cap.getTarget()) {
						GlStateManager.scale(scale, scale, scale);
						GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
					}
					if(player.isSneaking()) {
						GlStateManager.translate(0F, 0.125F, 0F);
					}
					if (player.isRiding()) {
						if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing) || cap.getFood().contentEquals("fairy_dew")) {
							GlStateManager.translate(0F, 0.45F, 0F);
						}
						if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing) || cap.getFood().contentEquals("dwarf_stout")) {
							GlStateManager.translate(0, 0.125F, 0);
						}
					}
				} else {
					GlStateManager.scale(scale, scale, scale);
					GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());

					if (player.isRiding()) {
						if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing) || cap.getFood().contentEquals("fairy_dew")) {
							GlStateManager.translate(0F, 1.8F, 0F);
						}
						if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing) || cap.getFood().contentEquals("dwarf_stout")) {
							GlStateManager.translate(0, 0.125F, 0);
						}
					}
				}
			} else {
				if(cap.getSize() != cap.getTarget()) {
					GlStateManager.pushMatrix();
					GlStateManager.scale(scale, scale, scale);
					GlStateManager.translate((event.getX() / scale) - event.getX(), (event.getY() / scale) - event.getY(), (event.getZ() / scale) - event.getZ());
				}
			}
		}
	}

	@SubscribeEvent
	public void renderPlayerPost(RenderPlayerEvent.Post event) {
		final EntityPlayer player = event.getEntityPlayer();
		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if(cap != null) {
			if(cap.getTrans() == true) {
				GlStateManager.popMatrix();
			} else {
				if(cap.getSize() != cap.getTarget()) {
					GlStateManager.popMatrix();
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderSpecialPre(RenderLivingEvent.Specials.Pre event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			if(cap != null) {
				if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing) || TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing) || cap.getFood().contentEquals("fairy_dew") || cap.getFood().contentEquals("dwarf_stout")) {
					GlStateManager.pushMatrix();
					final float scale = (float)cap.getTarget()/100;
					GlStateManager.translate(0, 0.3/scale, 0);
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderSpecialPost(RenderLivingEvent.Specials.Post event) {
		if (event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			if(cap != null) {
				if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing) || TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing) || cap.getFood().contentEquals("fairy_dew") || cap.getFood().contentEquals("dwarf_stout")) {
					GlStateManager.popMatrix();
				}
			}
		}
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		if(Minecraft.getMinecraft().player != null) {
			final EntityPlayer player = Minecraft.getMinecraft().player;
			if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye)) {
				final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDragonsEye);
				EffectsDragonsEye.playerTicks(stack, player);
			}
		}
	}

}
