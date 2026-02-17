package xzeroair.trinkets.client.races;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.hud.mana.ManaHud;
import xzeroair.trinkets.entity.AlphaWolf;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;

public abstract class RaceDefaultRenderer<T extends RaceDefaultRenderer, H extends EntityRacePropertiesHandler> implements IRenderRaceHandler<T> {

    protected EntityLivingBase entity;
    protected int tick, lastTick = 0;
    protected H handler;

    public RaceDefaultRenderer(EntityLivingBase entity, H raceHandler) {
        this.entity = entity;
        this.handler = raceHandler;
    }

    public void whileTransformed(EntityLivingBase entity) {
        if (entity != null && entity.world != null && entity.world.isRemote) {
            if (!entity.onGround) {
                lastTick = tick;
                tick += 48;
            }
            if ((tick >= (1210)) || (entity.onGround)) {
                tick = 0;
                lastTick = 0;
            }
        }
    }

    protected H getHandler() {
        return handler;
    }

    /*
     * Player Entities Only
     */
    @Override
    public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
        final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if ((entity == Minecraft.getMinecraft().player) && (screen != null) && !((screen instanceof GuiChat) || (screen instanceof GuiEntityProperties) || (screen instanceof ManaHud))) {
            return;
        }
        if ((getHandler().isTransforming() || getHandler().isTransformed()) && !getHandler().getEntityProperties().isNormalSize()) {
            final double hScale = getHandler().getEntityProperties().getHeightValue() * 0.01D;
            final double wScale = getHandler().getEntityProperties().getWidthValue() * 0.01D;
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
    }

    /*
     * Player Entities Only
     */
    @Override
    public void doRenderPlayerPost(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
    }

    /*
     * Both Player and Non Player Entities
     */
    @Override
    public <T extends EntityLivingBase> void doRenderLivingSpecialsPre(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
        if (entity instanceof EntityPlayer) {
            if ((getHandler().isTransforming() || getHandler().isTransformed()) && !getHandler().getEntityProperties().isNormalSize()) {
                GlStateManager.pushMatrix();
                final float t2 = getHandler().getEntityProperties().getDefaultHeight() - (entity.height);
                GlStateManager.translate(0, t2, 0);
            }
        }
    }

    /*
     * Both Player and Non Player Entities
     */
    @Override
    public <T extends EntityLivingBase> void doRenderLivingSpecialsPost(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
        if (entity instanceof EntityPlayer) {
            if ((getHandler().isTransforming() || getHandler().isTransformed()) && !getHandler().getEntityProperties().isNormalSize()) {
                GlStateManager.popMatrix();
            }
        }
    }

    /*
     * Non Player Entities Only
     */
    @Override
    public <T extends EntityLivingBase> void doRenderLivingPre(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
        if ((getHandler().isTransforming() || getHandler().isTransformed()) && !getHandler().getEntityProperties().isNormalSize()) {
            GlStateManager.pushMatrix();
            final double hScale = getHandler().getEntityProperties().getHeightValue() * 0.01D;
            final double wScale = getHandler().getEntityProperties().getWidthValue() * 0.01D;
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
    public <T extends EntityLivingBase> void doRenderLivingPost(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
        if ((getHandler().isTransforming() || getHandler().isTransformed()) && !getHandler().getEntityProperties().isNormalSize()) {
            GlStateManager.popMatrix();
        }
    }
}
