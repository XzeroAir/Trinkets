package xzeroair.trinkets.client.playerCamera;

import org.lwjgl.util.glu.Project;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;

@SideOnly(Side.CLIENT)
public class PlayerRaceCameraRendererOptifine extends EntityRenderer {
	private final Minecraft mc;
	private final IResourceManager resourceManager;
	private float farPlaneDistance;
	private int rendererUpdateCount;
	private float fovModifierHand;
	private float fovModifierHandPrev;
	private float bossColorModifier;
	private float bossColorModifierPrev;
	private boolean cloudFog;
	private boolean renderHand = true;
	private boolean drawBlockOutline = true;
	private final DynamicTexture lightmapTexture;
	private final int[] lightmapColors;
	private boolean lightmapUpdateNeeded;
	private float torchFlickerX;
	private float torchFlickerDX;
	private int rainSoundCounter;
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;
	private float fogColor2;
	private float fogColor1;
	private int debugViewDirection;
	private boolean debugView;
	private double cameraZoom = 1.0D;
	private double cameraYaw;
	private double cameraPitch;
	private int frameCount;

	public PlayerRaceCameraRendererOptifine(Minecraft mcIn, IResourceManager resourceManagerIn) {
		super(mcIn, resourceManagerIn);
		mc = mcIn;
		resourceManager = resourceManagerIn;
		lightmapTexture = new DynamicTexture(16, 16);
		lightmapColors = lightmapTexture.getTextureData();
	}

	@Override
	public void updateRenderer() {
		super.updateRenderer();
		//		if (OpenGlHelper.shadersSupported && (ShaderLinkHelper.getStaticShaderLinkHelper() == null)) {
		//			ShaderLinkHelper.setNewStaticShaderLinkHelper();
		//		}
		//
		//		this.updateFovModifierHand();
		//		this.updateTorchFlicker();
		//		fogColor2 = fogColor1;
		//		thirdPersonDistancePrev = 4.0F;
		//
		//		if (mc.gameSettings.smoothCamera) {
		//			float f = (mc.gameSettings.mouseSensitivity * 0.6F) + 0.2F;
		//			float f1 = f * f * f * 8.0F;
		//			smoothCamFilterX = mouseFilterXAxis.smooth(smoothCamYaw, 0.05F * f1);
		//			smoothCamFilterY = mouseFilterYAxis.smooth(smoothCamPitch, 0.05F * f1);
		//			smoothCamPartialTicks = 0.0F;
		//			smoothCamYaw = 0.0F;
		//			smoothCamPitch = 0.0F;
		//		} else {
		//			smoothCamFilterX = 0.0F;
		//			smoothCamFilterY = 0.0F;
		//			mouseFilterXAxis.reset();
		//			mouseFilterYAxis.reset();
		//		}
		//
		//		if (mc.getRenderViewEntity() == null) {
		//			mc.setRenderViewEntity(mc.player);
		//		}
		//
		//		float f3 = mc.world.getLightBrightness(new BlockPos(mc.getRenderViewEntity().getPositionEyes(1F))); // Forge: fix MC-51150
		//		float f4 = mc.gameSettings.renderDistanceChunks / 32.0F;
		//		float f2 = (f3 * (1.0F - f4)) + f4;
		//		fogColor1 += (f2 - fogColor1) * 0.1F;
		//		++rendererUpdateCount;
		//		itemRenderer.updateEquippedItem();
		//		this.addRainParticles();
		//		bossColorModifierPrev = bossColorModifier;
		//
		//		if (mc.ingameGUI.getBossOverlay().shouldDarkenSky()) {
		//			bossColorModifier += 0.05F;
		//
		//			if (bossColorModifier > 1.0F) {
		//				bossColorModifier = 1.0F;
		//			}
		//		} else if (bossColorModifier > 0.0F) {
		//			bossColorModifier -= 0.0125F;
		//		}
		//
		//		if (itemActivationTicks > 0) {
		//			--itemActivationTicks;
		//
		//			if (itemActivationTicks == 0) {
		//				itemActivationItem = null;
		//			}
		//		}
	}

	/**
	 * Update FOV modifier hand
	 */
	private void updateFovModifierHand() {
		float f = 1.0F;

		if (mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
			AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer) mc.getRenderViewEntity();
			f = abstractclientplayer.getFovModifier();
		}

		fovModifierHandPrev = fovModifierHand;
		fovModifierHand += (f - fovModifierHand) * 0.5F;

		if (fovModifierHand > 1.5F) {
			fovModifierHand = 1.5F;
		}

		if (fovModifierHand < 0.1F) {
			fovModifierHand = 0.1F;
		}
	}

	/**
	 * Changes the field of view of the player depending on if they are underwater
	 * or not
	 */
	private float getFOVModifier(float partialTicks, boolean useFOVSetting) {
		if (debugView) {
			return 90.0F;
		} else {
			Entity entity = mc.getRenderViewEntity();
			float f = 70.0F;

			if (useFOVSetting) {
				f = mc.gameSettings.fovSetting;
				f = f * (fovModifierHandPrev + ((fovModifierHand - fovModifierHandPrev) * partialTicks));
			}

			if ((entity instanceof EntityLivingBase) && (((EntityLivingBase) entity).getHealth() <= 0.0F)) {
				float f1 = ((EntityLivingBase) entity).deathTime + partialTicks;
				f /= ((1.0F - (500.0F / (f1 + 500.0F))) * 2.0F) + 1.0F;
			}

			IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(mc.world, entity, partialTicks);

			if (iblockstate.getMaterial() == Material.WATER) {
				f = (f * 60.0F) / 70.0F;
			}

			return net.minecraftforge.client.ForgeHooksClient.getFOVModifier(this, entity, iblockstate, partialTicks, f);
		}
	}

	private void hurtCameraEffect(float partialTicks) {
		if (mc.getRenderViewEntity() instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) mc.getRenderViewEntity();
			float f = entitylivingbase.hurtTime - partialTicks;

			if (entitylivingbase.getHealth() <= 0.0F) {
				float f1 = entitylivingbase.deathTime + partialTicks;
				GlStateManager.rotate(40.0F - (8000.0F / (f1 + 200.0F)), 0.0F, 0.0F, 1.0F);
			}

			if (f < 0.0F) {
				return;
			}

			f = f / entitylivingbase.maxHurtTime;
			f = MathHelper.sin(f * f * f * f * (float) Math.PI);
			float f2 = entitylivingbase.attackedAtYaw;
			GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
		}
	}

	/**
	 * Updates the bobbing render effect of the player.
	 */
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

	/**
	 * sets up player's eye (or camera in third person mode)
	 */
	float cameraDistance = 4F;

	private void orientCamera(float partialTicks) {
		final Entity entity = mc.getRenderViewEntity();
		float f = entity.getEyeHeight();
		double d0 = entity.prevPosX + ((entity.posX - entity.prevPosX) * partialTicks);
		double d1 = entity.prevPosY + ((entity.posY - entity.prevPosY) * partialTicks) + f;
		double d2 = entity.prevPosZ + ((entity.posZ - entity.prevPosZ) * partialTicks);

		double hitBlock = 0;

		if ((entity instanceof EntityLivingBase) && ((EntityLivingBase) entity).isPlayerSleeping()) {
			f = (float) (f + 1.0D);
			GlStateManager.translate(0.0F, 0.3F, 0.0F);

			if (!mc.gameSettings.debugCamEnable) {
				final BlockPos blockpos = new BlockPos(entity);
				final IBlockState iblockstate = mc.world.getBlockState(blockpos);
				net.minecraftforge.client.ForgeHooksClient.orientBedCamera(
						mc.world, blockpos, iblockstate,
						entity
				);

				GlStateManager.rotate(
						entity.prevRotationYaw
								+ ((entity.rotationYaw - entity.prevRotationYaw) * partialTicks) + 180.0F,
						0.0F, -1.0F, 0.0F
				);
				GlStateManager.rotate(
						entity.prevRotationPitch + ((entity.rotationPitch - entity.prevRotationPitch) * partialTicks),
						-1.0F, 0.0F, 0.0F
				);
			}
		} else if (mc.gameSettings.thirdPersonView > 0) {
			EntityPlayer player = mc.player;
			EntityProperties prop = Capabilities.getEntityRace(player);
			//TODO Fix this or get rid of the Camera Renderer
			if ((prop != null) && (prop.getTargetSize() != 1D)) {
				float cameraScale = (((prop.getSize() * 100)) / prop.getTargetSize()) * 0.01f;
				cameraDistance = 4F * (prop.getTargetSize() * 0.01F) * cameraScale;
			}

			double d3 = cameraDistance + ((cameraDistance - cameraDistance) * partialTicks);

			if (mc.gameSettings.debugCamEnable) {
				GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
			} else {
				final float f1 = entity.rotationYaw;
				float f2 = entity.rotationPitch;

				if (mc.gameSettings.thirdPersonView == 2) {
					f2 += 180.0F;
				}

				final float sq = 0.017453292F * 1F;
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
					final RayTraceResult raytraceresult = mc.world.rayTraceBlocks(
							new Vec3d(d0 + f3, d1 + f4, d2 + f5),
							new Vec3d((d0 - d4) + f3 + f5, (d1 - d6) + f4, (d2 - d5) + f5)
					);

					// System.out.println(d6);
					if (raytraceresult != null) {
						// System.out.println("Hit");
						if (d6 < 0) {
							hitBlock = MathHelper.clamp(-d6, 0, 0.111F);
						}
						final double d7 = raytraceresult.hitVec.distanceTo(new Vec3d(d0, d1, d2));

						if (d7 < d3) {
							d3 = d7;
						}
					} else {
						// System.out.println("No Block");
					}
				}

				if (mc.gameSettings.thirdPersonView == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.0F, 0.0F, (float) (-d3));
				GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			GlStateManager.translate(0.0F, 0.0F, 0.05F);
		}

		if (!mc.gameSettings.debugCamEnable) {
			float yaw = entity.prevRotationYaw + ((entity.rotationYaw - entity.prevRotationYaw) * partialTicks)
					+ 180.0F;
			final float pitch = entity.prevRotationPitch
					+ ((entity.rotationPitch - entity.prevRotationPitch) * partialTicks);
			final float roll = 0.0F;
			if (entity instanceof EntityAnimal) {
				final EntityAnimal entityanimal = (EntityAnimal) entity;
				yaw = entityanimal.prevRotationYawHead
						+ ((entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks) + 180.0F;
			}
			final IBlockState state = ActiveRenderInfo.getBlockStateAtEntityViewpoint(
					mc.world, entity,
					partialTicks
			);
			final net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup event = new net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup(
					this, entity, state, partialTicks, yaw, pitch, roll
			);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
			GlStateManager.rotate(event.getRoll(), 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(event.getPitch(), 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(event.getYaw(), 0.0F, 1.0F, 0.0F);
		}

		GlStateManager.translate(0.0F, -(f - hitBlock), 0.0F);
		d0 = entity.prevPosX + ((entity.posX - entity.prevPosX) * partialTicks);
		d1 = entity.prevPosY + ((entity.posY - entity.prevPosY) * partialTicks) + f;
		d2 = entity.prevPosZ + ((entity.posZ - entity.prevPosZ) * partialTicks);
		cloudFog = mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
	}

	/**
	 * sets up projection, view effects, camera position/rotation
	 */
	private void setupCameraTransform(float partialTicks, int pass) {
		farPlaneDistance = mc.gameSettings.renderDistanceChunks * 16;
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		float f = 0.07F;

		if (mc.gameSettings.anaglyph) {
			GlStateManager.translate((-((pass * 2) - 1)) * 0.07F, 0.0F, 0.0F);
		}

		if (cameraZoom != 1.0D) {
			GlStateManager.translate((float) cameraYaw, (float) (-cameraPitch), 0.0F);
			GlStateManager.scale(cameraZoom, cameraZoom, 1.0D);
		}

		Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) mc.displayWidth / (float) mc.displayHeight, 0.05F, farPlaneDistance * MathHelper.SQRT_2);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();

		if (mc.gameSettings.anaglyph) {
			GlStateManager.translate(((pass * 2) - 1) * 0.1F, 0.0F, 0.0F);
		}

		this.hurtCameraEffect(partialTicks);

		if (mc.gameSettings.viewBobbing) {
			this.applyBobbing(partialTicks);
		}

		float f1 = mc.player.prevTimeInPortal + ((mc.player.timeInPortal - mc.player.prevTimeInPortal) * partialTicks);

		if (f1 > 0.0F) {
			int i = 20;

			if (mc.player.isPotionActive(MobEffects.NAUSEA)) {
				i = 7;
			}

			float f2 = (5.0F / ((f1 * f1) + 5.0F)) - (f1 * 0.04F);
			f2 = f2 * f2;
			GlStateManager.rotate((rendererUpdateCount + partialTicks) * i, 0.0F, 1.0F, 1.0F);
			GlStateManager.scale(1.0F / f2, 1.0F, 1.0F);
			GlStateManager.rotate(-(rendererUpdateCount + partialTicks) * i, 0.0F, 1.0F, 1.0F);
		}

		this.orientCamera(partialTicks);

		if (debugView) {
			switch (debugViewDirection) {
			case 0:
				GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				break;
			case 1:
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				break;
			case 2:
				GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				break;
			case 3:
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				break;
			case 4:
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			}
		}
	}

	/**
	 * Render player hand
	 */
	private void renderHand(float partialTicks, int pass) {
		if (!debugView) {
			GlStateManager.matrixMode(5889);
			GlStateManager.loadIdentity();
			float f = 0.07F;

			if (mc.gameSettings.anaglyph) {
				GlStateManager.translate((-((pass * 2) - 1)) * 0.07F, 0.0F, 0.0F);
			}

			Project.gluPerspective(this.getFOVModifier(partialTicks, false), (float) mc.displayWidth / (float) mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);
			GlStateManager.matrixMode(5888);
			GlStateManager.loadIdentity();

			if (mc.gameSettings.anaglyph) {
				GlStateManager.translate(((pass * 2) - 1) * 0.1F, 0.0F, 0.0F);
			}

			GlStateManager.pushMatrix();
			this.hurtCameraEffect(partialTicks);

			if (mc.gameSettings.viewBobbing) {
				this.applyBobbing(partialTicks);
			}

			boolean flag = (mc.getRenderViewEntity() instanceof EntityLivingBase) && ((EntityLivingBase) mc.getRenderViewEntity()).isPlayerSleeping();

			if (!net.minecraftforge.client.ForgeHooksClient.renderFirstPersonHand(mc.renderGlobal, partialTicks, pass)) {
				if ((mc.gameSettings.thirdPersonView == 0) && !flag && !mc.gameSettings.hideGUI && !mc.playerController.isSpectator()) {
					this.enableLightmap();
					itemRenderer.renderItemInFirstPerson(partialTicks);
					this.disableLightmap();
				}
			}

			GlStateManager.popMatrix();

			if ((mc.gameSettings.thirdPersonView == 0) && !flag) {
				itemRenderer.renderOverlays(partialTicks);
				this.hurtCameraEffect(partialTicks);
			}

			if (mc.gameSettings.viewBobbing) {
				this.applyBobbing(partialTicks);
			}
		}
	}

	/**
	 * Recompute a random value that is applied to block color in updateLightmap()
	 */
	private void updateTorchFlicker() {
		torchFlickerDX = (float) (torchFlickerDX + ((Math.random() - Math.random()) * Math.random() * Math.random()));
		torchFlickerDX = (float) (torchFlickerDX * 0.9D);
		torchFlickerX += torchFlickerDX - torchFlickerX;
		lightmapUpdateNeeded = true;
	}

	private void updateLightmap(float partialTicks) {
		if (lightmapUpdateNeeded) {
			mc.profiler.startSection("lightTex");
			World world = mc.world;

			if (world != null) {
				float f = world.getSunBrightness(1.0F);
				float f1 = (f * 0.95F) + 0.05F;

				for (int i = 0; i < 256; ++i) {
					float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
					float f3 = world.provider.getLightBrightnessTable()[i % 16] * ((torchFlickerX * 0.1F) + 1.5F);

					if (world.getLastLightningBolt() > 0) {
						f2 = world.provider.getLightBrightnessTable()[i / 16];
					}

					float f4 = f2 * ((f * 0.65F) + 0.35F);
					float f5 = f2 * ((f * 0.65F) + 0.35F);
					float f6 = f3 * ((((f3 * 0.6F) + 0.4F) * 0.6F) + 0.4F);
					float f7 = f3 * ((f3 * f3 * 0.6F) + 0.4F);
					float f8 = f4 + f3;
					float f9 = f5 + f6;
					float f10 = f2 + f7;
					f8 = (f8 * 0.96F) + 0.03F;
					f9 = (f9 * 0.96F) + 0.03F;
					f10 = (f10 * 0.96F) + 0.03F;

					if (bossColorModifier > 0.0F) {
						float f11 = bossColorModifierPrev + ((bossColorModifier - bossColorModifierPrev) * partialTicks);
						f8 = (f8 * (1.0F - f11)) + (f8 * 0.7F * f11);
						f9 = (f9 * (1.0F - f11)) + (f9 * 0.6F * f11);
						f10 = (f10 * (1.0F - f11)) + (f10 * 0.6F * f11);
					}

					if (world.provider.getDimensionType().getId() == 1) {
						f8 = 0.22F + (f3 * 0.75F);
						f9 = 0.28F + (f6 * 0.75F);
						f10 = 0.25F + (f7 * 0.75F);
					}

					float[] colors = { f8, f9, f10 };
					world.provider.getLightmapColors(partialTicks, f, f2, f3, colors);
					f8 = colors[0];
					f9 = colors[1];
					f10 = colors[2];

					// Forge: fix MC-58177
					f8 = MathHelper.clamp(f8, 0f, 1f);
					f9 = MathHelper.clamp(f9, 0f, 1f);
					f10 = MathHelper.clamp(f10, 0f, 1f);

					if (mc.player.isPotionActive(MobEffects.NIGHT_VISION)) {
						float f15 = this.getNightVisionBrightness(mc.player, partialTicks);
						float f12 = 1.0F / f8;

						if (f12 > (1.0F / f9)) {
							f12 = 1.0F / f9;
						}

						if (f12 > (1.0F / f10)) {
							f12 = 1.0F / f10;
						}

						f8 = (f8 * (1.0F - f15)) + (f8 * f12 * f15);
						f9 = (f9 * (1.0F - f15)) + (f9 * f12 * f15);
						f10 = (f10 * (1.0F - f15)) + (f10 * f12 * f15);
					}

					if (f8 > 1.0F) {
						f8 = 1.0F;
					}

					if (f9 > 1.0F) {
						f9 = 1.0F;
					}

					if (f10 > 1.0F) {
						f10 = 1.0F;
					}

					float f16 = mc.gameSettings.gammaSetting;
					float f17 = 1.0F - f8;
					float f13 = 1.0F - f9;
					float f14 = 1.0F - f10;
					f17 = 1.0F - (f17 * f17 * f17 * f17);
					f13 = 1.0F - (f13 * f13 * f13 * f13);
					f14 = 1.0F - (f14 * f14 * f14 * f14);
					f8 = (f8 * (1.0F - f16)) + (f17 * f16);
					f9 = (f9 * (1.0F - f16)) + (f13 * f16);
					f10 = (f10 * (1.0F - f16)) + (f14 * f16);
					f8 = (f8 * 0.96F) + 0.03F;
					f9 = (f9 * 0.96F) + 0.03F;
					f10 = (f10 * 0.96F) + 0.03F;

					if (f8 > 1.0F) {
						f8 = 1.0F;
					}

					if (f9 > 1.0F) {
						f9 = 1.0F;
					}

					if (f10 > 1.0F) {
						f10 = 1.0F;
					}

					if (f8 < 0.0F) {
						f8 = 0.0F;
					}

					if (f9 < 0.0F) {
						f9 = 0.0F;
					}

					if (f10 < 0.0F) {
						f10 = 0.0F;
					}

					int j = 255;
					int k = (int) (f8 * 255.0F);
					int l = (int) (f9 * 255.0F);
					int i1 = (int) (f10 * 255.0F);
					lightmapColors[i] = -16777216 | (k << 16) | (l << 8) | i1;
				}

				lightmapTexture.updateDynamicTexture();
				lightmapUpdateNeeded = false;
				mc.profiler.endSection();
			}
		}
	}

	private float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks) {
		int i = entitylivingbaseIn.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
		return i > 200 ? 1.0F : 0.7F + (MathHelper.sin((i - partialTicks) * (float) Math.PI * 0.2F) * 0.3F);
	}

	private boolean isDrawBlockOutline() {
		if (!drawBlockOutline) {
			return false;
		} else {
			Entity entity = mc.getRenderViewEntity();
			boolean flag = (entity instanceof EntityPlayer) && !mc.gameSettings.hideGUI;

			if (flag && !((EntityPlayer) entity).capabilities.allowEdit) {
				ItemStack itemstack = ((EntityPlayer) entity).getHeldItemMainhand();

				if ((mc.objectMouseOver != null) && (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)) {
					BlockPos blockpos = mc.objectMouseOver.getBlockPos();
					Block block = mc.world.getBlockState(blockpos).getBlock();

					if (mc.playerController.getCurrentGameType() == GameType.SPECTATOR) {
						flag = block.hasTileEntity(mc.world.getBlockState(blockpos)) && (mc.world.getTileEntity(blockpos) instanceof IInventory);
					} else {
						flag = !itemstack.isEmpty() && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block));
					}
				}
			}

			return flag;
		}
	}

	@Override
	public void renderWorld(float partialTicks, long finishTimeNano) {
		this.updateLightmap(partialTicks);

		if (mc.getRenderViewEntity() == null) {
			mc.setRenderViewEntity(mc.player);
		}

		this.getMouseOver(partialTicks);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.5F);
		mc.profiler.startSection("center");

		if (mc.gameSettings.anaglyph) {
			anaglyphField = 0;
			GlStateManager.colorMask(false, true, true, false);
			this.renderWorldPass(0, partialTicks, finishTimeNano);
			anaglyphField = 1;
			GlStateManager.colorMask(true, false, false, false);
			this.renderWorldPass(1, partialTicks, finishTimeNano);
			GlStateManager.colorMask(true, true, true, false);
		} else {
			this.renderWorldPass(2, partialTicks, finishTimeNano);
		}

		mc.profiler.endSection();
	}

	private void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
		RenderGlobal renderglobal = mc.renderGlobal;
		ParticleManager particlemanager = mc.effectRenderer;
		boolean flag = this.isDrawBlockOutline();
		GlStateManager.enableCull();
		mc.profiler.endStartSection("clear");
		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		this.updateFogColor(partialTicks);
		GlStateManager.clear(16640);
		mc.profiler.endStartSection("camera");
		this.setupCameraTransform(partialTicks, pass);
		ActiveRenderInfo.updateRenderInfo(mc.getRenderViewEntity(), mc.gameSettings.thirdPersonView == 2); //Forge: MC-46445 Spectator mode particles and sounds computed from where you have been before
		mc.profiler.endStartSection("frustum");
		ClippingHelperImpl.getInstance();
		mc.profiler.endStartSection("culling");
		ICamera icamera = new Frustum();
		Entity entity = mc.getRenderViewEntity();
		double d0 = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
		double d1 = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
		double d2 = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);
		icamera.setPosition(d0, d1, d2);

		if (mc.gameSettings.renderDistanceChunks >= 4) {
			mc.profiler.endStartSection("sky");
			GlStateManager.matrixMode(5889);
			GlStateManager.loadIdentity();
			Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) mc.displayWidth / (float) mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);
			GlStateManager.matrixMode(5888);
			renderglobal.renderSky(partialTicks, pass);
			GlStateManager.matrixMode(5889);
			GlStateManager.loadIdentity();
			Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float) mc.displayWidth / (float) mc.displayHeight, 0.05F, farPlaneDistance * MathHelper.SQRT_2);
			GlStateManager.matrixMode(5888);
		}

		mc.profiler.endStartSection("prepareterrain");
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();
		mc.profiler.endStartSection("terrain_setup");
		renderglobal.setupTerrain(entity, partialTicks, icamera, frameCount++, mc.player.isSpectator());

		if ((pass == 0) || (pass == 2)) {
			mc.profiler.endStartSection("updatechunks");
			mc.renderGlobal.updateChunks(finishTimeNano);
		}

		mc.profiler.endStartSection("terrain");
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.disableAlpha();
		renderglobal.renderBlockLayer(BlockRenderLayer.SOLID, partialTicks, pass, entity);
		GlStateManager.enableAlpha();
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, mc.gameSettings.mipmapLevels > 0); // FORGE: fix flickering leaves when mods mess up the blurMipmap settings
		renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		renderglobal.renderBlockLayer(BlockRenderLayer.CUTOUT, partialTicks, pass, entity);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		GlStateManager.shadeModel(7424);
		GlStateManager.alphaFunc(516, 0.1F);

		if (!debugView) {
			GlStateManager.matrixMode(5888);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			RenderHelper.enableStandardItemLighting();
			mc.profiler.endStartSection("entities");
			net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
			renderglobal.renderEntities(entity, icamera, partialTicks);
			net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
			RenderHelper.disableStandardItemLighting();
			this.disableLightmap();
		}

		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();

		if (flag && (mc.objectMouseOver != null) && !entity.isInsideOfMaterial(Material.WATER)) {
			EntityPlayer entityplayer = (EntityPlayer) entity;
			GlStateManager.disableAlpha();
			mc.profiler.endStartSection("outline");
			if (!net.minecraftforge.client.ForgeHooksClient.onDrawBlockHighlight(renderglobal, entityplayer, mc.objectMouseOver, 0, partialTicks)) {
				renderglobal.drawSelectionBox(entityplayer, mc.objectMouseOver, 0, partialTicks);
			}
			GlStateManager.enableAlpha();
		}

		if (mc.debugRenderer.shouldRender()) {
			mc.debugRenderer.renderDebug(partialTicks, finishTimeNano);
		}

		mc.profiler.endStartSection("destroyProgress");
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), entity, partialTicks);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		GlStateManager.disableBlend();

		if (!debugView) {
			this.enableLightmap();
			mc.profiler.endStartSection("litParticles");
			particlemanager.renderLitParticles(entity, partialTicks);
			RenderHelper.disableStandardItemLighting();
			mc.profiler.endStartSection("particles");
			particlemanager.renderParticles(entity, partialTicks);
			this.disableLightmap();
		}

		GlStateManager.depthMask(false);
		GlStateManager.enableCull();
		mc.profiler.endStartSection("weather");
		this.renderRainSnow(partialTicks);
		GlStateManager.depthMask(true);
		renderglobal.renderWorldBorder(entity, partialTicks);
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.shadeModel(7425);
		mc.profiler.endStartSection("translucent");
		renderglobal.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, partialTicks, pass, entity);
		if (!debugView) //Only render if render pass 0 happens as well.
		{
			RenderHelper.enableStandardItemLighting();
			mc.profiler.endStartSection("entities");
			net.minecraftforge.client.ForgeHooksClient.setRenderPass(1);
			renderglobal.renderEntities(entity, icamera, partialTicks);
			// restore blending function changed by RenderGlobal.preRenderDamagedBlocks
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			net.minecraftforge.client.ForgeHooksClient.setRenderPass(-1);
			RenderHelper.disableStandardItemLighting();
		}
		GlStateManager.shadeModel(7424);
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
		GlStateManager.disableFog();

		if ((entity.posY + entity.getEyeHeight()) >= 128.0D) {
			mc.profiler.endStartSection("aboveClouds");
		}

		mc.profiler.endStartSection("forge_render_last");
		net.minecraftforge.client.ForgeHooksClient.dispatchRenderLast(renderglobal, partialTicks);

		mc.profiler.endStartSection("hand");

		if (renderHand) {
			GlStateManager.clear(256);
			this.renderHand(partialTicks, pass);
		}
	}

	/**
	 * calculates fog and calls glClearColor
	 */
	private void updateFogColor(float partialTicks) {
		World world = mc.world;
		Entity entity = mc.getRenderViewEntity();
		float f = 0.25F + ((0.75F * mc.gameSettings.renderDistanceChunks) / 32.0F);
		f = 1.0F - (float) Math.pow(f, 0.25D);
		Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float f1 = (float) vec3d.x;
		float f2 = (float) vec3d.y;
		float f3 = (float) vec3d.z;
		Vec3d vec3d1 = world.getFogColor(partialTicks);
		fogColorRed = (float) vec3d1.x;
		fogColorGreen = (float) vec3d1.y;
		fogColorBlue = (float) vec3d1.z;

		if (mc.gameSettings.renderDistanceChunks >= 4) {
			double d0 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) > 0.0F ? -1.0D : 1.0D;
			Vec3d vec3d2 = new Vec3d(d0, 0.0D, 0.0D);
			float f5 = (float) entity.getLook(partialTicks).dotProduct(vec3d2);

			if (f5 < 0.0F) {
				f5 = 0.0F;
			}

			if (f5 > 0.0F) {
				float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

				if (afloat != null) {
					f5 = f5 * afloat[3];
					fogColorRed = (fogColorRed * (1.0F - f5)) + (afloat[0] * f5);
					fogColorGreen = (fogColorGreen * (1.0F - f5)) + (afloat[1] * f5);
					fogColorBlue = (fogColorBlue * (1.0F - f5)) + (afloat[2] * f5);
				}
			}
		}

		fogColorRed += (f1 - fogColorRed) * f;
		fogColorGreen += (f2 - fogColorGreen) * f;
		fogColorBlue += (f3 - fogColorBlue) * f;
		float f8 = world.getRainStrength(partialTicks);

		if (f8 > 0.0F) {
			float f4 = 1.0F - (f8 * 0.5F);
			float f10 = 1.0F - (f8 * 0.4F);
			fogColorRed *= f4;
			fogColorGreen *= f4;
			fogColorBlue *= f10;
		}

		float f9 = world.getThunderStrength(partialTicks);

		if (f9 > 0.0F) {
			float f11 = 1.0F - (f9 * 0.5F);
			fogColorRed *= f11;
			fogColorGreen *= f11;
			fogColorBlue *= f11;
		}

		IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(mc.world, entity, partialTicks);

		if (cloudFog) {
			Vec3d vec3d3 = world.getCloudColour(partialTicks);
			fogColorRed = (float) vec3d3.x;
			fogColorGreen = (float) vec3d3.y;
			fogColorBlue = (float) vec3d3.z;
		} else {
			//Forge Moved to Block.
			Vec3d viewport = ActiveRenderInfo.projectViewFromEntity(entity, partialTicks);
			BlockPos viewportPos = new BlockPos(viewport);
			IBlockState viewportState = mc.world.getBlockState(viewportPos);
			Vec3d inMaterialColor = viewportState.getBlock().getFogColor(mc.world, viewportPos, viewportState, entity, new Vec3d(fogColorRed, fogColorGreen, fogColorBlue), partialTicks);
			fogColorRed = (float) inMaterialColor.x;
			fogColorGreen = (float) inMaterialColor.y;
			fogColorBlue = (float) inMaterialColor.z;
		}

		float f13 = fogColor2 + ((fogColor1 - fogColor2) * partialTicks);
		fogColorRed *= f13;
		fogColorGreen *= f13;
		fogColorBlue *= f13;
		double d1 = (entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks)) * world.provider.getVoidFogYFactor();

		if ((entity instanceof EntityLivingBase) && ((EntityLivingBase) entity).isPotionActive(MobEffects.BLINDNESS)) {
			int i = ((EntityLivingBase) entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

			if (i < 20) {
				d1 *= 1.0F - (i / 20.0F);
			} else {
				d1 = 0.0D;
			}
		}

		if (d1 < 1.0D) {
			if (d1 < 0.0D) {
				d1 = 0.0D;
			}

			d1 = d1 * d1;
			fogColorRed = (float) (fogColorRed * d1);
			fogColorGreen = (float) (fogColorGreen * d1);
			fogColorBlue = (float) (fogColorBlue * d1);
		}

		if (bossColorModifier > 0.0F) {
			float f14 = bossColorModifierPrev + ((bossColorModifier - bossColorModifierPrev) * partialTicks);
			fogColorRed = (fogColorRed * (1.0F - f14)) + (fogColorRed * 0.7F * f14);
			fogColorGreen = (fogColorGreen * (1.0F - f14)) + (fogColorGreen * 0.6F * f14);
			fogColorBlue = (fogColorBlue * (1.0F - f14)) + (fogColorBlue * 0.6F * f14);
		}

		if ((entity instanceof EntityLivingBase) && ((EntityLivingBase) entity).isPotionActive(MobEffects.NIGHT_VISION)) {
			float f15 = this.getNightVisionBrightness((EntityLivingBase) entity, partialTicks);
			float f6 = 1.0F / fogColorRed;

			if (f6 > (1.0F / fogColorGreen)) {
				f6 = 1.0F / fogColorGreen;
			}

			if (f6 > (1.0F / fogColorBlue)) {
				f6 = 1.0F / fogColorBlue;
			}

			// Forge: fix MC-4647 and MC-10480
			if (Float.isInfinite(f6)) {
				f6 = Math.nextAfter(f6, 0.0);
			}

			fogColorRed = (fogColorRed * (1.0F - f15)) + (fogColorRed * f6 * f15);
			fogColorGreen = (fogColorGreen * (1.0F - f15)) + (fogColorGreen * f6 * f15);
			fogColorBlue = (fogColorBlue * (1.0F - f15)) + (fogColorBlue * f6 * f15);
		}

		if (mc.gameSettings.anaglyph) {
			float f16 = ((fogColorRed * 30.0F) + (fogColorGreen * 59.0F) + (fogColorBlue * 11.0F)) / 100.0F;
			float f17 = ((fogColorRed * 30.0F) + (fogColorGreen * 70.0F)) / 100.0F;
			float f7 = ((fogColorRed * 30.0F) + (fogColorBlue * 70.0F)) / 100.0F;
			fogColorRed = f16;
			fogColorGreen = f17;
			fogColorBlue = f7;
		}

		net.minecraftforge.client.event.EntityViewRenderEvent.FogColors event = new net.minecraftforge.client.event.EntityViewRenderEvent.FogColors(this, entity, iblockstate, partialTicks, fogColorRed, fogColorGreen, fogColorBlue);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);

		fogColorRed = event.getRed();
		fogColorGreen = event.getGreen();
		fogColorBlue = event.getBlue();

		GlStateManager.clearColor(fogColorRed, fogColorGreen, fogColorBlue, 0.0F);
	}
}