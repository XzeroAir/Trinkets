package xzeroair.trinkets.client.races.dragon;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.races.IRenderModelInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import javax.annotation.Nonnull;

public class RaceDragonWings implements IRenderModelInterface {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/races/dragon/dragon_wings.png");
    public static final ResourceLocation TEXTURE_ARMS = new ResourceLocation(Reference.MODID + ":" + "textures/races/dragon/dragon_wings_arms.png");
    public static final ResourceLocation TEXTURE_LEATHER = new ResourceLocation(Reference.MODID + ":" + "textures/races/dragon/dragon_wings_leather.png");
    public static final ResourceLocation TEXTURE_COMBINED = new ResourceLocation(Reference.MODID + ":" + "textures/races/dragon/dragon_wings_combined.png");

    public static RaceDragonWings INSTANCE = new RaceDragonWings();

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale) {
        int variant = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitVariant());
        int colorPrimary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getColorOption() == 1 ? prop.getRaceHandler().getSecondaryTraitColor() : prop.getRaceHandler().getPrimaryTraitColor());
        int colorSecondary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getColorOption() > 0 ? prop.getRaceHandler().getPrimaryTraitColor() : prop.getRaceHandler().getSecondaryTraitColor());
        int auxColor = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitAuxColor());
        this.render(entity, renderer, isFake, isSlim, partialTicks, scale, variant, colorPrimary, colorSecondary, auxColor);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(@Nonnull EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale, int variant, int colorPrimary, int colorSecondary, int auxColor) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (entity.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }

        ModelBase model = renderer.getMainModel();
        if (model instanceof ModelBiped) {
            ((ModelBiped) model).bipedBody.postRender(scale);
        }

        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, -2.0F, 0.0F);

        if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            GlStateManager.translate(-0.4F, -1.0F, 0.0F);
        }

        final double rootX = -12.0D;
        final double rootY = isSlim ? -19.0D : -20.0D;
        final double rootZ = 0.0D;

        final int rootWidth = 8;
        final int rootHeight = 32;
        final int rootUWidth = 16;
        final int rootVHeight = 64;
        final int rootTexWidth = 64;
        final int rootTexHeight = 64;

        final int midWidth = 16;
        final int midHeight = 32;
        final int midUWidth = 32;
        final int midVHeight = 64;
        final int midTexWidth = 64;
        final int midTexHeight = 64;

        final int tipWidth = 8;
        final int tipHeight = 32;
        final int tipUWidth = 16;
        final int tipVHeight = 64;
        final int tipTexWidth = 64;
        final int tipTexHeight = 64;

        final double midX = rootX - midWidth;
        final double tipX = midX - tipWidth;

        final float[] primaryRgb = ColorHelper.getRGBColor(colorPrimary);
        final float[] secondaryRgb = ColorHelper.getRGBColor(colorSecondary);
        final float alpha = 1.0F;

        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        final boolean onGround = entity.onGround;

        final float flapSpeed = 0.04F;
        final float cycle = onGround ? 0.0F : (entity.ticksExisted + partialTicks) * flapSpeed;
        final float forwardRatio = 0.45F;
        final float phase = cycle - (float) Math.floor(cycle);

        final float flap;
        if (phase < forwardRatio) {
            final float t = phase / forwardRatio;
            flap = -MathHelper.cos(t * (float) Math.PI);
        } else {
            final float t = (phase - forwardRatio) / (1.0F - forwardRatio);
            flap = MathHelper.cos(t * (float) Math.PI);
        }

        final float forwardFlap = Math.max(0.0F, flap);
        final float backwardFlap = Math.max(0.0F, -flap);
        final float downFlap = 0.5F + 0.5F * flap;

        final float anchorX = (isSlim ? 0 : 0.2F) + ((onGround ? 1.0F : -1.0F) - downFlap * 1.5F);
        final float anchorY = onGround ? -2.0F : -3.0F;

        final float wingSeparation = (onGround ? 0.0F : 2.0F) + backwardFlap * 0.15F;

        final float rootYaw = onGround ? -40.0F : 60.0F + flap * 40.0F;
        final float rootPitch = onGround ? 0.0F : 10.0F;
        final float rootRoll = onGround ? 0.0F : 15.0F + flap * 17.0F;

        final float midYaw = -flap * (12.0F + forwardFlap * 24.0F + backwardFlap * 12.0F);

        final float tipFollow = midYaw * 1.05F;
        final float tipLag = flap * Math.abs(flap) * 32.0F;
        final float tipYaw = onGround ? 12.0F : tipFollow + tipLag;

        // Left wing
        GlStateManager.pushMatrix();
        GlStateManager.translate(anchorX, anchorY, wingSeparation);
        GlStateManager.scale(0.9F, 0.9F, 0.9F);
        GlStateManager.rotate(rootYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-rootPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-rootRoll, 0.0F, 0.0F, 1.0F);

        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, rootX, rootY, rootZ, 48, 0, rootUWidth, rootVHeight, rootWidth, rootHeight, rootTexWidth, rootTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, rootX, rootY, rootZ, 48, 0, rootUWidth, rootVHeight, rootWidth, rootHeight, rootTexWidth, rootTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, rootX, rootY, rootZ, 48, 0, rootUWidth, rootVHeight, rootWidth, rootHeight, rootTexWidth, rootTexHeight, secondaryRgb[0], secondaryRgb[1], secondaryRgb[2], alpha);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(rootX, rootY, rootZ);
        GlStateManager.rotate(-midYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-rootX, -rootY, -rootZ);

        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, midX, rootY, rootZ, 16, 0, midUWidth, midVHeight, midWidth, midHeight, midTexWidth, midTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, midX, rootY, rootZ, 16, 0, midUWidth, midVHeight, midWidth, midHeight, midTexWidth, midTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, midX, rootY, rootZ, 16, 0, midUWidth, midVHeight, midWidth, midHeight, midTexWidth, midTexHeight, secondaryRgb[0], secondaryRgb[1], secondaryRgb[2], alpha);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(midX, rootY, rootZ);
        GlStateManager.rotate(-tipYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-midX, -rootY, -rootZ);

        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, tipX, rootY, rootZ, 0, 0, tipUWidth, tipVHeight, tipWidth, tipHeight, tipTexWidth, tipTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, tipX, rootY, rootZ, 0, 0, tipUWidth, tipVHeight, tipWidth, tipHeight, tipTexWidth, tipTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, tipX, rootY, rootZ, 0, 0, tipUWidth, tipVHeight, tipWidth, tipHeight, tipTexWidth, tipTexHeight, secondaryRgb[0], secondaryRgb[1], secondaryRgb[2], alpha);
        }

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();

        // Right wing
        GlStateManager.pushMatrix();
        GlStateManager.translate(anchorX, anchorY, -wingSeparation);
        GlStateManager.scale(0.9F, 0.9F, 0.9F);
        GlStateManager.rotate(-rootYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(rootPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-rootRoll, 0.0F, 0.0F, 1.0F);

        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, rootX, rootY, rootZ, 48, 0, rootUWidth, rootVHeight, rootWidth, rootHeight, rootTexWidth, rootTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, rootX, rootY, rootZ, 48, 0, rootUWidth, rootVHeight, rootWidth, rootHeight, rootTexWidth, rootTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, rootX, rootY, rootZ, 48, 0, rootUWidth, rootVHeight, rootWidth, rootHeight, rootTexWidth, rootTexHeight, secondaryRgb[0], secondaryRgb[1], secondaryRgb[2], alpha);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(rootX, rootY, rootZ);
        GlStateManager.rotate(midYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-rootX, -rootY, -rootZ);

        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, midX, rootY, rootZ, 16, 0, midUWidth, midVHeight, midWidth, midHeight, midTexWidth, midTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, midX, rootY, rootZ, 16, 0, midUWidth, midVHeight, midWidth, midHeight, midTexWidth, midTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, midX, rootY, rootZ, 16, 0, midUWidth, midVHeight, midWidth, midHeight, midTexWidth, midTexHeight, secondaryRgb[0], secondaryRgb[1], secondaryRgb[2], alpha);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(midX, rootY, rootZ);
        GlStateManager.rotate(tipYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-midX, -rootY, -rootZ);

        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, tipX, rootY, rootZ, 0, 0, tipUWidth, tipVHeight, tipWidth, tipHeight, tipTexWidth, tipTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, tipX, rootY, rootZ, 0, 0, tipUWidth, tipVHeight, tipWidth, tipHeight, tipTexWidth, tipTexHeight, primaryRgb[0], primaryRgb[1], primaryRgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, tipX, rootY, rootZ, 0, 0, tipUWidth, tipVHeight, tipWidth, tipHeight, tipTexWidth, tipTexHeight, secondaryRgb[0], secondaryRgb[1], secondaryRgb[2], alpha);
        }

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

}
