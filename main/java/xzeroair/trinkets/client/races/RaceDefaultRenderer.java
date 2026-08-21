package xzeroair.trinkets.client.races;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import xzeroair.trinkets.client.gui.ITrinketGuiInterface;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.traits.abilities.AbilityElytraFlight;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.mobends.MoBendsCompat;

public abstract class RaceDefaultRenderer<T extends RaceDefaultRenderer, H extends EntityRacePropertiesHandler> implements IRenderRaceHandler<T> {

    protected EntityLivingBase entity;
    protected int tick, lastTick = 0;
    protected H handler;
    private float previousLimbSwingAmount;
    private float limbSwingAmount;
    private boolean limbSwingAmountModified;

    public RaceDefaultRenderer(EntityLivingBase entity, H raceHandler) {
        this.entity = entity;
        this.handler = raceHandler;
    }

    protected H getHandler() {
        return this.handler;
    }

    /*
     * Player Entities Only
     */
    @Override
    public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
        final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if ((screen != null) && !((screen instanceof GuiChat) || screen instanceof GuiIngameMenu || (screen instanceof ITrinketGuiInterface))) {
            return;
        }
        boolean customGlide = false;
        final IAbilityInterface ability = this.getHandler().getAbility(TrinketsRegistryNames.MODID + ":" + TrinketsRegistryNames.ModAbilities.ELYTRA_FLIGHT);
        if (ability instanceof AbilityElytraFlight && ((AbilityElytraFlight) ability).isGliding()) {
            customGlide = true;
            this.previousLimbSwingAmount = entity.prevLimbSwingAmount;
            this.limbSwingAmount = entity.limbSwingAmount;
            this.limbSwingAmountModified = true;
            entity.prevLimbSwingAmount = 0F;
            entity.limbSwingAmount = 0F;
        }
        if ((this.getHandler().isTransforming() || this.getHandler().isTransformed()) && !this.getHandler().getProperties().isNormalSize()) {
            final double hScale = this.getHandler().getProperties().getHeightValue() * 0.01D;
            final double wScale = this.getHandler().getProperties().getWidthValue() * 0.01D;
            final double xLoc = (x / wScale) - x;
            final double yLoc = (y / hScale) - y;
            final double zLoc = (z / wScale) - z;

            final double yOffset = entity.getYOffset();
            final Entity mount = entity.getRidingEntity();
            //			double vanillaOffset = mount.posY + mount.getMountedYOffset() + entity.getYOffset();// + 0.15 * prevRearingAmount
            final double mountedOffset = entity.isRiding() && (mount != null) ? (mount.getMountedYOffset()) : 0;
            //			final double offsetDifference = entity.isRiding() && (mount != null) ? (mount.height - mountedOffset) : 0;
            //						final double retMountedOffset = mountedOffset - ((offsetDifference) * 0.66D);
            final double retMountedOffset = -(mountedOffset + yOffset) - 0.1D;
            if (entity.isRiding() && !(mount instanceof AlphaWolf)) {
                GlStateManager.translate(0, mountedOffset, 0);
                GlStateManager.translate(0, -yOffset, 0);
                GlStateManager.translate(0, retMountedOffset, 0);
            }
            GlStateManager.scale(wScale, hScale, wScale);
            if (entity.isRiding() && !(mount instanceof AlphaWolf)) {
                GlStateManager.translate(0, -retMountedOffset, 0);
                GlStateManager.translate(0, yOffset, 0);
                GlStateManager.translate(0, -mountedOffset, 0);
            }
            GlStateManager.translate(xLoc, yLoc, zLoc);
        }
        if (customGlide) {
            this.applyCustomGlidePose(entity, partialTick);
        }
    }

    /*
     * Player Entities Only
     */
    @Override
    public void doRenderPlayerPost(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
        if (this.limbSwingAmountModified) {
            entity.prevLimbSwingAmount = this.previousLimbSwingAmount;
            entity.limbSwingAmount = this.limbSwingAmount;
            this.limbSwingAmountModified = false;
        }
    }

    private void applyCustomGlidePose(EntityPlayer entity, float partialTick) {
        final float bodyYaw = this.getRenderBodyYaw(entity, partialTick);
        final float vanillaRotation = 180F - bodyYaw;
        final float glideTicks = Math.max(entity.getTicksElytraFlying(), 10) + partialTick;
        final float glideProgress = MathHelper.clamp((glideTicks * glideTicks) / 100F, 0F, 1F);

        GlStateManager.rotate(vanillaRotation, 0F, 1F, 0F);
        GlStateManager.rotate(glideProgress * (-90F - entity.rotationPitch), 1F, 0F, 0F);

        final Vec3d look = entity.getLook(partialTick);
        final double horizontalSpeedSquared = (entity.motionX * entity.motionX) + (entity.motionZ * entity.motionZ);
        final double horizontalLookSquared = (look.x * look.x) + (look.z * look.z);
        if (horizontalSpeedSquared > 0D && horizontalLookSquared > 0D) {
            final double facingMotion = ((entity.motionX * look.x) + (entity.motionZ * look.z)) / (Math.sqrt(horizontalSpeedSquared) * Math.sqrt(horizontalLookSquared));
            final double strafeMotion = (entity.motionX * look.z) - (entity.motionZ * look.x);
            GlStateManager.rotate((float) (Math.signum(strafeMotion) * Math.acos(facingMotion) * (180D / Math.PI)), 0F, 1F, 0F);
        }
        GlStateManager.rotate(-vanillaRotation, 0F, 1F, 0F);
    }

    private float getRenderBodyYaw(EntityPlayer entity, float partialTick) {
        float bodyYaw = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTick);
        final float headYaw = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTick);
        if (entity.isRiding() && entity.getRidingEntity() instanceof EntityLivingBase) {
            final EntityLivingBase mount = (EntityLivingBase) entity.getRidingEntity();
            bodyYaw = this.interpolateRotation(mount.prevRenderYawOffset, mount.renderYawOffset, partialTick);
            float headOffset = MathHelper.clamp(MathHelper.wrapDegrees(headYaw - bodyYaw), -85F, 85F);
            bodyYaw = headYaw - headOffset;
            if (headOffset * headOffset > 2500F) {
                bodyYaw += headOffset * 0.2F;
            }
        }
        return bodyYaw;
    }

    private float interpolateRotation(float previous, float current, float partialTick) {
        return previous + (partialTick * MathHelper.wrapDegrees(current - previous));
    }

    /*
     * Both Player and Non Player Entities
     */
    @Override
    public <E extends EntityLivingBase> void doRenderLivingSpecialsPre(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<E> renderer, float partialTick) {
        if (entity instanceof EntityPlayer) {
            if ((this.getHandler().isTransforming() || this.getHandler().isTransformed()) && !this.getHandler().getProperties().isNormalSize()) {
                GlStateManager.pushMatrix();
                final float t2 = this.getHandler().getProperties().getDefaultHeight() - (entity.height);
                GlStateManager.translate(0, t2, 0);
            }
        }
    }

    /*
     * Both Player and Non Player Entities
     */
    @Override
    public <E extends EntityLivingBase> void doRenderLivingSpecialsPost(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<E> renderer, float partialTick) {
        if (entity instanceof EntityPlayer) {
            if ((this.getHandler().isTransforming() || this.getHandler().isTransformed()) && !this.getHandler().getProperties().isNormalSize()) {
                GlStateManager.popMatrix();
            }
        }
    }

    /*
     * Non Player Entities Only
     */
    @Override
    public <E extends EntityLivingBase> void doRenderLivingPre(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<E> renderer, float partialTick) {
        if ((this.getHandler().isTransforming() || this.getHandler().isTransformed()) && !this.getHandler().getProperties().isNormalSize()) {
            GlStateManager.pushMatrix();
            final double hScale = this.getHandler().getProperties().getHeightValue() * 0.01D;
            final double wScale = this.getHandler().getProperties().getWidthValue() * 0.01D;
            final double xLoc = (x / wScale) - x;
            final double yLoc = (y / hScale) - y;
            final double zLoc = (z / wScale) - z;

            final double yOffset = entity.getYOffset();
            final Entity mount = entity.getRidingEntity();
            //			double vanillaOffset = mount.posY + mount.getMountedYOffset() + entity.getYOffset();// + 0.15 * prevRearingAmount
            final double mountedOffset = entity.isRiding() && (mount != null) ? (mount.getMountedYOffset()) : 0;
            //			final double offsetDifference = entity.isRiding() && (mount != null) ? (mount.height - mountedOffset) : 0;
            //						final double retMountedOffset = mountedOffset - ((offsetDifference) * 0.66D);
            final double retMountedOffset = -(mountedOffset + yOffset) - 0.1D;
            //			GlStateManager.translate(-xLoc, -yLoc, -zLoc);
            if (entity.isRiding()) {
                GlStateManager.translate(0, mountedOffset, 0);
                GlStateManager.translate(0, -yOffset, 0);
                GlStateManager.translate(0, retMountedOffset, 0);
            }
            GlStateManager.scale(wScale, hScale, wScale);
            if (entity.isRiding()) {
                GlStateManager.translate(0, -retMountedOffset, 0);
                GlStateManager.translate(0, yOffset, 0);
                GlStateManager.translate(0, -mountedOffset, 0);
            }
            GlStateManager.translate(xLoc, yLoc, zLoc);
            //			System.out.println(hScale + "|" + wScale + "| X:" + xLoc + "| Y:" + yLoc + "| Z:" + zLoc);
        }
    }

    /*
     * Non Player Entities Only
     */
    @Override
    public <E extends EntityLivingBase> void doRenderLivingPost(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<E> renderer, float partialTick) {
        if ((this.getHandler().isTransforming() || this.getHandler().isTransformed()) && !this.getHandler().getProperties().isNormalSize()) {
            GlStateManager.popMatrix();
        }
    }

    @Override
    public IRenderRaceHandler<T> getRaceRenderer() {
        return this;
    }
}
