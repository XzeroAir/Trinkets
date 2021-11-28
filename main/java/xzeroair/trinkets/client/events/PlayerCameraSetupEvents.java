package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.ModItems;

public class PlayerCameraSetupEvents {

	private static Minecraft mc = Minecraft.getMinecraft();

	private double getVanillaTranslation(double partialTicks, EntityPlayer player) {

		final float f = player.getEyeHeight();
		final double d0 = player.prevPosX + ((player.posX - player.prevPosX) * partialTicks);
		final double d1 = player.prevPosY + ((player.posY - player.prevPosY) * partialTicks) + (f);
		final double d2 = player.prevPosZ + ((player.posZ - player.prevPosZ) * partialTicks);

		double d3 = 4F + ((4F - 4F) * partialTicks);

		final float f1 = player.rotationYaw;
		float f2 = player.rotationPitch;

		if (mc.gameSettings.thirdPersonView == 2) {
			f2 += 180.0F;
		}
		final float sq = 0.017453292F;
		final double d4 = -MathHelper.sin(f1 * sq) * MathHelper.cos(f2 * sq) * d3;
		final double d5 = MathHelper.cos(f1 * sq) * MathHelper.cos(f2 * sq) * d3;
		final double d6 = (-MathHelper.sin(f2 * sq)) * d3;
		for (int i = 0; i < 8; ++i) {
			float f3 = ((i & 1) * 2) - 1;
			float f4 = (((i >> 1) & 1) * 2) - 1;
			float f5 = (((i >> 2) & 1) * 2) - 1;
			f3 = f3 * 0.1F;
			f4 = f4 * 0.1F;
			f5 = f5 * 0.1F;
			final double startX = d0 + f3;
			final double startY = (d1 + f4);
			final double startZ = d2 + f5;
			final Vec3d start = new Vec3d(startX, startY, startZ);
			final double endX = ((d0 - d4) + f3 + f5);
			final double endY = ((d1 - d6) + f4);
			final double endZ = ((d2 - d5) + f5);
			final Vec3d end = new Vec3d(endX, endY, endZ);
			final RayTraceResult raytraceresult = mc.world.rayTraceBlocks(start, end);
			if (raytraceresult != null) {
				final double d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2));
				if (d7 < d3) {
					d3 = d7;
				}
			}
		}
		return d3;
	}

	private double getAdjustedTranslation(double partialTicks, EntityPlayer player) {

		final float f = player.getEyeHeight();
		final double d0 = player.prevPosX + ((player.posX - player.prevPosX) * partialTicks);
		final double d1 = player.prevPosY + ((player.posY - player.prevPosY) * partialTicks) + (f);
		final double d2 = player.prevPosZ + ((player.posZ - player.prevPosZ) * partialTicks);

		double d3 = 8 + (0 * partialTicks);

		final float f1 = player.rotationYaw;
		float f2 = player.rotationPitch;

		if (mc.gameSettings.thirdPersonView == 2) {
			f2 += 180.0F;
		}
		final float sq = 0.017453292F * 1F;
		//		final double d4 = -MathHelper.sin(f1 * sq) * MathHelper.cos(f2 * sq) * d3;
		//		final double d5 = MathHelper.cos(f1 * sq) * MathHelper.cos(f2 * sq) * d3;
		//		final double d6 = (-MathHelper.sin(f2 * sq)) * d3;
		final double d4 = -MathHelper.sin(f1 * (sq * 1F)) * MathHelper.cos(f2 * (sq * 1F)) * d3;
		final double d5 = MathHelper.cos(f1 * (sq * 1F)) * MathHelper.cos(f2 * (sq * 1F)) * d3;
		final double d6 = (-MathHelper.sin(f2 * (sq * 1F))) * d3;
		for (int i = 0; i < 8; ++i) {
			float f3 = ((i & 1) * 2) - 1;
			float f4 = (((i >> 1) & 1) * 2) - 1;
			float f5 = (((i >> 2) & 1) * 2) - 1;
			f3 = f3 * 0.1F;
			f4 = f4 * 0.1F;
			f5 = f5 * 0.1F;
			final double startX = (d0 + f3);
			final double startY = (d1 + f4);
			final double startZ = (d2 + f5);
			final Vec3d start = new Vec3d(startX, startY, startZ);
			final double endX = ((d0 - d4) + f3 + f5);
			final double endY = ((d1 - d6) + f4);
			final double endZ = ((d2 - (d5)) + f5);
			final Vec3d end = new Vec3d(endX, endY, endZ);
			final RayTraceResult raytraceresult = mc.world.rayTraceBlocks(start, end);
			if (raytraceresult != null) {
				//				if (d6 < 0) {
				//					hitBlock = MathHelper.clamp(-d6, 0, 0.111F);
				//				}
				final double d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2));
				if (d7 < d3) {
					d3 = d7;
				}
			}
		}
		return d3;
	}

	@SubscribeEvent
	public void CameraSetup(CameraSetup event) {
		//				event.setYaw(320);
		//		event.setYaw(140);
		//		if (mc.gameSettings.thirdPersonView == 2) {
		//		event.setYaw(event.getYaw() - 80);
		//		event.setYaw(event.getYaw() + 60);
		//		event.setYaw(event.getYaw() + 90);
		//		event.setPitch(event.getPitch() - 40);
		//		} else {
		//			event.setPitch(event.getPitch() + 40);
		//		}

		//		final EntityPlayer player = Minecraft.getMinecraft().player;
		//		RaceProperties cap = Capabilities.getEntityRace(player);
		//		if (cap != null) {
		//			final float f1 = event.getYaw();
		//			float f2 = event.getPitch();
		//
		//			if (mc.gameSettings.thirdPersonView == 2) {
		//				f2 += 180.0F;
		//			}
		//			final int currentView = Minecraft.getMinecraft().gameSettings.thirdPersonView;
		//			final double mcTL = this.getVanillaTranslation(event.getRenderPartialTicks(), player);
		//			final double adTL = this.getAdjustedTranslation(event.getRenderPartialTicks(), player);
		//			System.out.println(mcTL);
		//			GlStateManager.translate(0, 0, (mcTL * 1));
		//		}
	}

	@SubscribeEvent
	public void FOVUpdate(FOVUpdateEvent event) {
		//		if(event.getEntity() != null) {
		//			final EntityPlayer player = event.getEntity();
		//			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		//			if((cap != null)) {
		//				if((TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing) || cap.getFood().contentEquals("fairy_dew")) && !TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
		//					if(mc.gameSettings.thirdPersonView > 0) {
		//						event.setNewfov((event.getFov() / 90.0f) * 60.0f);
		//					}
		//				}
		//				if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketTitanRing)) {
		//					System.out.println((event.getFov() / 60.0f) * 180.0f);
		//					event.setNewfov((event.getFov() / 90.0f) * 180.0f);
		//				}
		//			}
		//		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderFogDensityEvent(FogDensity event) {
		final EntityPlayer player = Minecraft.getMinecraft().player;
		//		final float d = event.getDensity();
		if (player.isInWater()) {
			if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketSea)) {
				if (event.isCancelable() && !event.isCanceled()) {
					event.setCanceled(true);
				}
				event.setDensity(0);
				//			} else {
				//				event.setCanceled(false);
			}
			//		} else {
			//			event.setCanceled(false);
			//			event.setDensity(d);
		}
	}

	@SubscribeEvent
	public void renderFogEvent(RenderFogEvent event) {
	}

	@SubscribeEvent
	public void renderBlockOverlay(RenderBlockOverlayEvent event) {
		if (event.getOverlayType() == OverlayType.FIRE) {
			if (TrinketHelper.AccessoryCheck(event.getPlayer(), ModItems.trinkets.TrinketDragonsEye) || TrinketHelper.AccessoryCheck(event.getPlayer(), ModItems.RaceTrinkets.TrinketDragonRing)) {
				event.setCanceled(true);
			}
		}
		if (event.getOverlayType() == OverlayType.WATER) {
			if (TrinketHelper.AccessoryCheck(event.getPlayer(), ModItems.trinkets.TrinketSea)) {
				event.setCanceled(true);
			}
		}
	}
}
