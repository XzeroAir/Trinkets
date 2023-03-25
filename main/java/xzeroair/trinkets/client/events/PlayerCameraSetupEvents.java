package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.trinkets.TrinketDragonsEye;
import xzeroair.trinkets.items.trinkets.TrinketSeaStone;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.util.TrinketsConfig;

public class PlayerCameraSetupEvents {

	private static Minecraft mc = Minecraft.getMinecraft();

	private CameraTranslation getVanillaTranslation(EntityRenderer renderer, double partialTicks) {
		final Entity entity = mc.getRenderViewEntity();
		float f = entity.getEyeHeight();
		final double d0 = entity.prevPosX + ((entity.posX - entity.prevPosX) * partialTicks);
		final double d1 = entity.prevPosY + ((entity.posY - entity.prevPosY) * partialTicks) + (f);
		final double d2 = entity.prevPosZ + ((entity.posZ - entity.prevPosZ) * partialTicks);
		final int perspective = mc.gameSettings.thirdPersonView;
		float x = 0;
		float y = 0;
		float z = 0;

		if (perspective > 0) {
			double d3 = 4F + ((4F - 4F) * partialTicks);

			final float f1 = entity.rotationYaw;
			float f2 = entity.rotationPitch;

			if (perspective == 2) {
				f2 += 180.0F;
			}
			final float sq = 0.017453292F;
			final double d4 = -MathHelper.sin(f1 * sq) * MathHelper.cos(f2 * sq) * d3;
			final double d5 = MathHelper.cos(f1 * sq) * MathHelper.cos(f2 * sq) * d3;
			final double d6 = -MathHelper.sin(f2 * sq) * d3;
			for (int i = 0; i < 8; ++i) {
				float f3 = ((i & 1) * 2) - 1;
				float f4 = (((i >> 1) & 1) * 2) - 1;
				float f5 = (((i >> 2) & 1) * 2) - 1;
				f3 = f3 * 0.1F;
				f4 = f4 * 0.1F;
				f5 = f5 * 0.1F;
				final double startX = d0 + f3;
				final double startY = d1 + f4;
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
			z = ((float) d3);
		} else {
			z = 0.05F;
		}
		return new CameraTranslation(x, y, perspective == 2 ? -z : z);
	}

	public CameraTranslation updatedOffset(EntityRenderer renderer, double multiplier, double partialTicks) {
		final Entity entity = mc.getRenderViewEntity();
		float f = entity.getEyeHeight();
		final double d0 = entity.prevPosX + ((entity.posX - entity.prevPosX) * partialTicks);
		final double d1 = entity.prevPosY + ((entity.posY - entity.prevPosY) * partialTicks) + f;
		final double d2 = entity.prevPosZ + ((entity.posZ - entity.prevPosZ) * partialTicks);
		final int perspective = mc.gameSettings.thirdPersonView;
		double d = 4F * multiplier;
		float x = 0;
		float y = 0;
		float z = 0;
		if (perspective > 0) {
			//			double d3 = multiplier >= 1 ?
			double d3 = d + ((d - d) * partialTicks);
			//					: (4F + ((4F - 4F) * partialTicks));
			//			double d3 = (4F + ((4F - 4F) * partialTicks));

			final float f1 = entity.rotationYaw;
			float f2 = entity.rotationPitch;

			if (perspective == 2) {
				f2 += 180.0F;
			}
			final float sq = 0.017453292F;
			final double d4 = -MathHelper.sin(f1 * sq) * MathHelper.cos(f2 * sq) * d3;
			final double d5 = MathHelper.cos(f1 * sq) * MathHelper.cos(f2 * sq) * d3;
			final double d6 = -MathHelper.sin(f2 * sq) * d3;
			for (int i = 0; i < 8; ++i) {
				float f3 = ((i & 1) * 2) - 1;
				float f4 = (((i >> 1) & 1) * 2) - 1;
				float f5 = (((i >> 2) & 1) * 2) - 1;
				f3 = f3 * 0.1F;
				f4 = f4 * 0.1F;
				f5 = f5 * 0.1F;
				final double startX = d0 + f3;
				final double startY = d1 + f4;
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
						if (multiplier <= 1) {
							d3 = d7 * multiplier;
						} else {
							d3 = d7;
						}
						//								* multiplier;
						// Works when Fairy, Doesn't Work when Titan
					}
					//						d3 *= multiplier;
				}
			}
			if (perspective == 2) {
			}

			z = (float) (d3);
		} else {
			z = 0.05F;
		}
		return new CameraTranslation(x, y, perspective == 2 ? z : -z);
	}

	private void applyBobbing(float partialTicks) {
		if (mc.getRenderViewEntity() instanceof EntityPlayer) {
			final EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
			final float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
			final float f1 = -(entityplayer.distanceWalkedModified + (f * partialTicks));
			final float f2 = entityplayer.prevCameraYaw
					+ ((entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks);
			final float f3 = entityplayer.prevCameraPitch
					+ ((entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks);
			GlStateManager.translate(
					MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.15F,
					-Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2) * 0.0F, 0.0F
			);
			GlStateManager.rotate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(
					Math.abs(MathHelper.cos((f1 * (float) Math.PI) - 0.2F) * f2) * 5.0F, 1.0F, 0.0F,
					0.0F
			);
			GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
		}
	}

	@SubscribeEvent
	public void CameraSetup(CameraSetup event) {
		if (!TrinketsConfig.CLIENT.cameraHeight) {
			return;
		}
		final Entity entity = event.getEntity();
		if (entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;
			Capabilities.getEntityProperties(player, prop -> {
				final EntityRacePropertiesHandler handler = prop.getRaceHandler();
				if (!prop.isNormalSize() || handler.isTransforming()) {
					final float defaultHeight = prop.getDefaultHeight();
					final float height = handler.getHeight();
					final int perspective = mc.gameSettings.thirdPersonView;
					if (perspective > 0) {
						final CameraTranslation vanilla = this.getVanillaTranslation(event.getRenderer(), event.getRenderPartialTicks());
						GlStateManager.translate(0.0F, 0.0F, vanilla.getZ());
						final CameraTranslation offset = this.updatedOffset(event.getRenderer(), (height / defaultHeight), event.getRenderPartialTicks());
						GlStateManager.translate(0.0F, 0.0, offset.getZ());
					}
				}
			});
		}
	}

	//	@SubscribeEvent
	//	public void FOVUpdate(FOVUpdateEvent event) {
	//	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void renderFogDensityEvent(FogDensity event) {
		final EntityPlayer player = Minecraft.getMinecraft().player;
		if (player.isInWater()) {
			final boolean hasSeaStone = !TrinketHelper.getAccessory(player, acc -> acc.getItem() instanceof TrinketSeaStone).isEmpty();
			boolean hasAffinity = TrinketHelper.entityHasAbility(Abilities.waterAffinity.toString(), player);
			if (hasSeaStone || hasAffinity) {
				if (event.isCancelable() && !event.isCanceled()) {
					event.setCanceled(true);
				}
				event.setDensity(0);
			}
		}
	}

	//	@SubscribeEvent
	//	public void renderFogEvent(RenderFogEvent event) {
	//	}

	@SubscribeEvent
	public void renderBlockOverlay(RenderBlockOverlayEvent event) {
		final OverlayType overlay = event.getOverlayType();
		if ((overlay == OverlayType.FIRE) || (overlay == OverlayType.WATER)) {
			final boolean cancel = !TrinketHelper.getAccessory(event.getPlayer(), acc -> {
				if ((overlay == OverlayType.FIRE) &&
						((acc.getItem() instanceof TrinketDragonsEye) ||
								acc.getItem().getRegistryName().toString().equalsIgnoreCase(ModItems.RaceTrinkets.TrinketDragonRing.getRegistryName().toString()))) {
					if (((TrinketDragonsEye) acc.getItem()).getPrimaryElement() == Elements.FIRE) {
						return true;
					} else {
						return false;
					}
				} else if ((overlay == OverlayType.WATER) && (acc.getItem() instanceof TrinketSeaStone)) {
					return true;
				} else {
					return false;
				}
			}).isEmpty();
			boolean hasFireAffinity = (overlay == OverlayType.FIRE) && TrinketHelper.entityHasAbility(Abilities.fireImmunity.toString(), event.getPlayer());
			boolean hasWaterAffinity = (overlay == OverlayType.WATER) && TrinketHelper.entityHasAbility(Abilities.waterAffinity.toString(), event.getPlayer());
			if (cancel || hasFireAffinity || hasWaterAffinity) {
				if (event.isCancelable() && !event.isCanceled()) {
					event.setCanceled(true);
				}
			}
		}
	}

	private static class CameraTranslation {
		private float x;
		private float y;
		private float z;

		public CameraTranslation(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public final float getX() {
			return x;
		}

		public final float getY() {
			return y;
		}

		public final float getZ() {
			return z;
		}
	}
}
