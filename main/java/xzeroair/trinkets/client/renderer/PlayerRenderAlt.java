package xzeroair.trinkets.client.renderer;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import xzeroair.trinkets.client.model.ModelPlayerAlt;

public class PlayerRenderAlt extends RenderPlayer {
	/** this field is used to indicate the 3-pixel wide arms */
	private static boolean smallArms;

	public PlayerRenderAlt(RenderManager renderManager)
	{
		this(renderManager, false);
	}

	public PlayerRenderAlt(RenderManager renderManager, boolean useSmallArms)
	{
		super(renderManager, useSmallArms);
		this.smallArms = useSmallArms;
		this.mainModel = new ModelPlayerAlt(0.0F, useSmallArms);
	}

	@Override
	public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		//		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Pre(entity, this, partialTicks, x, y, z))) {
		//			return;
		//		}
		if (!entity.isUser() || (this.renderManager.renderViewEntity == entity))
		{
			double d0 = y;

			if (entity.isSneaking())
			{
				d0 = y - 0.125D;
			}

			this.setModelVisibilities(entity);
			GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
			doRenderModel(entity, x, d0, z, entityYaw, partialTicks);
			GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Post(entity, this, partialTicks, x, y, z));
	}

	private void doRenderModel(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
		boolean shouldSit = (entity.isRiding() && !(entity.getRidingEntity() instanceof EntityMinecart) && ((entity.getRidingEntity() != null) && entity.getRidingEntity().shouldRiderSit()));
		this.mainModel.isRiding = shouldSit;
		this.mainModel.isChild = entity.isChild();

		try
		{
			float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
			float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
			float f2 = f1 - f;

			if (shouldSit && (entity.getRidingEntity() instanceof EntityLivingBase))
			{
				EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
				f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
				f2 = f1 - f;
				float f3 = MathHelper.wrapDegrees(f2);

				if (f3 < -85.0F)
				{
					f3 = -85.0F;
				}

				if (f3 >= 85.0F)
				{
					f3 = 85.0F;
				}

				f = f1 - f3;

				if ((f3 * f3) > 2500.0F)
				{
					f += f3 * 0.2F;
				}

				f2 = f1 - f;
			}

			float f7 = entity.prevRotationPitch + ((entity.rotationPitch - entity.prevRotationPitch) * partialTicks);
			this.renderLivingAt(entity, x, y, z);
			float f8 = this.handleRotationFloat(entity, partialTicks);
			this.applyRotations(entity, f8, f, partialTicks);
			float f4 = this.prepareScale(entity, partialTicks);
			float f5 = 0.0F;
			float f6 = 0.0F;

			if (!entity.isRiding())
			{
				f5 = entity.prevLimbSwingAmount + ((entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks);
				f6 = entity.limbSwing - (entity.limbSwingAmount * (1.0F - partialTicks));

				if (entity.isChild())
				{
					f6 *= 3.0F;
				}

				if (f5 > 1.0F)
				{
					f5 = 1.0F;
				}
				f2 = f1 - f; // Forge: Fix MC-1207
			}

			GlStateManager.enableAlpha();
			this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
			this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, f4, entity);

			if (this.renderOutlines)
			{
				boolean flag1 = this.setScoreTeamColor(entity);
				GlStateManager.enableColorMaterial();
				GlStateManager.enableOutlineMode(this.getTeamColor(entity));

				if (!this.renderMarker)
				{
					this.renderModel(entity, f6, f5, f8, f2, f7, f4);
				}

				if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator())
				{
					this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
				}

				GlStateManager.disableOutlineMode();
				GlStateManager.disableColorMaterial();

				if (flag1)
				{
					this.unsetScoreTeamColor();
				}
			}
			else
			{
				boolean flag = this.setDoRenderBrightness(entity, partialTicks);
				this.renderModel(entity, f6, f5, f8, f2, f7, f4);

				if (flag)
				{
					this.unsetBrightness();
				}

				GlStateManager.depthMask(true);

				if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator())
				{
					this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
				}
			}

			GlStateManager.disableRescaleNormal();
		}
		catch (Exception exception)
		{
			//            LOGGER.error("Couldn't render entity", (Throwable)exception);
		}

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	private void setModelVisibilities(AbstractClientPlayer clientPlayer)
	{
		ModelPlayer modelplayer = this.getMainModel();

		if (clientPlayer.isSpectator())
		{
			modelplayer.setVisible(false);
			modelplayer.bipedHead.showModel = true;
			modelplayer.bipedHeadwear.showModel = true;
		}
		else
		{
			ItemStack itemstack = clientPlayer.getHeldItemMainhand();
			ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
			modelplayer.setVisible(true);
			modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
			modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
			modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
			modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
			modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
			modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
			modelplayer.isSneak = clientPlayer.isSneaking();
			ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
			ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

			if (!itemstack.isEmpty())
			{
				modelbiped$armpose = ModelBiped.ArmPose.ITEM;

				if (clientPlayer.getItemInUseCount() > 0)
				{
					EnumAction enumaction = itemstack.getItemUseAction();

					if (enumaction == EnumAction.BLOCK)
					{
						modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
					}
					else if (enumaction == EnumAction.BOW)
					{
						modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}

			if (!itemstack1.isEmpty())
			{
				modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

				if (clientPlayer.getItemInUseCount() > 0)
				{
					EnumAction enumaction1 = itemstack1.getItemUseAction();

					if (enumaction1 == EnumAction.BLOCK)
					{
						modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
					}
					// FORGE: fix MC-88356 allow offhand to use bow and arrow animation
					else if (enumaction1 == EnumAction.BOW)
					{
						modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}

			if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT)
			{
				modelplayer.rightArmPose = modelbiped$armpose;
				modelplayer.leftArmPose = modelbiped$armpose1;
			}
			else
			{
				modelplayer.rightArmPose = modelbiped$armpose1;
				modelplayer.leftArmPose = modelbiped$armpose;
			}
		}
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayer entity)
	{
		return entity.getLocationSkin();
	}

	@Override
	public void transformHeldFull3DItemLayer()
	{
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	/**
	 * Allows the render to do state modifications necessary before the model is rendered.
	 */
	@Override
	protected void preRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime)
	{
		float f = 0.9375F;
		GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
	}

	@Override
	protected void renderEntityName(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq)
	{
		if (distanceSq < 100.0D)
		{
			Scoreboard scoreboard = entityIn.getWorldScoreboard();
			ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);

			if (scoreobjective != null)
			{
				Score score = scoreboard.getOrCreateScore(entityIn.getName(), scoreobjective);
				this.renderLivingLabel(entityIn, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y, z, 64);
				y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.025F);
			}
		}

		super.renderEntityName(entityIn, x, y, z, name, distanceSq);
	}

	@Override
	public void renderRightArm(AbstractClientPlayer clientPlayer)
	{
		float f = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelPlayer modelplayer = this.getMainModel();
		this.setModelVisibilities(clientPlayer);
		GlStateManager.enableBlend();
		modelplayer.swingProgress = 0.0F;
		modelplayer.isSneak = false;
		modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
		modelplayer.bipedRightArm.rotateAngleX = 0.0F;
		modelplayer.bipedRightArm.render(0.0625F);
		modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
		modelplayer.bipedRightArmwear.render(0.0625F);
		GlStateManager.disableBlend();
	}

	@Override
	public void renderLeftArm(AbstractClientPlayer clientPlayer)
	{
		float f = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelPlayer modelplayer = this.getMainModel();
		this.setModelVisibilities(clientPlayer);
		GlStateManager.enableBlend();
		modelplayer.isSneak = false;
		modelplayer.swingProgress = 0.0F;
		modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
		modelplayer.bipedLeftArm.rotateAngleX = 0.0F;
		modelplayer.bipedLeftArm.render(0.0625F);
		modelplayer.bipedLeftArmwear.rotateAngleX = 0.0F;
		modelplayer.bipedLeftArmwear.render(0.0625F);
		GlStateManager.disableBlend();
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	@Override
	protected void renderLivingAt(AbstractClientPlayer entityLivingBaseIn, double x, double y, double z)
	{
		if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping())
		{
			super.renderLivingAt(entityLivingBaseIn, x + (double)entityLivingBaseIn.renderOffsetX, y + (double)entityLivingBaseIn.renderOffsetY, z + (double)entityLivingBaseIn.renderOffsetZ);
		}
		else
		{
			super.renderLivingAt(entityLivingBaseIn, x, y, z);
		}
	}

	@Override
	protected void applyRotations(AbstractClientPlayer entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		if (entityLiving.isEntityAlive() && entityLiving.isPlayerSleeping())
		{
			GlStateManager.rotate(entityLiving.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
		}
		else if (entityLiving.isElytraFlying())
		{
			super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
			float f = (float)entityLiving.getTicksElytraFlying() + partialTicks;
			float f1 = MathHelper.clamp((f * f) / 100.0F, 0.0F, 1.0F);
			GlStateManager.rotate(f1 * (-90.0F - entityLiving.rotationPitch), 1.0F, 0.0F, 0.0F);
			Vec3d vec3d = entityLiving.getLook(partialTicks);
			double d0 = (entityLiving.motionX * entityLiving.motionX) + (entityLiving.motionZ * entityLiving.motionZ);
			double d1 = (vec3d.x * vec3d.x) + (vec3d.z * vec3d.z);

			if ((d0 > 0.0D) && (d1 > 0.0D))
			{
				double d2 = ((entityLiving.motionX * vec3d.x) + (entityLiving.motionZ * vec3d.z)) / (Math.sqrt(d0) * Math.sqrt(d1));
				double d3 = (entityLiving.motionX * vec3d.z) - (entityLiving.motionZ * vec3d.x);
				GlStateManager.rotate(((float)(Math.signum(d3) * Math.acos(d2)) * 180.0F) / (float)Math.PI, 0.0F, 1.0F, 0.0F);
			}
		}
		else
		{
			super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
		}
	}
}