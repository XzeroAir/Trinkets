package xzeroair.trinkets.client.races.dragon;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.races.IRenderModelInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.compat.mobends.MoBendsCompat;
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
        GlStateManager.color(1, 1, 1, 1);
        if (entity.isSneaking()) {
            if (!MoBendsCompat.isModEnabled()) {
                GlStateManager.rotate(35, 1, 0, 0);
                GlStateManager.translate(0F, 0.0F, -0.2F);
            }
            GlStateManager.translate(0F, 0.2F, 0F);
        }
        if (MoBendsCompat.isModEnabled()) {
            if (renderer instanceof RenderPlayer) {
                final RenderPlayer rend = (RenderPlayer) renderer;
                rend.getMainModel().bipedBody.postRender(scale);
            }
        }
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.translate(0, -2F, 0);
        if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            GlStateManager.translate(-0.4F, -1F, 0F);
        }

        final double x = -12;
        final double y = isSlim ? -19 : -20;
        final double z = 0;
        final int width = 8;
        final int height = 32;
        final int uWidth = 16;
        final int vHeight = 64;
        final int texWidth = 64;
        final int texHeight = 64;

        final int innerWidth = 16;
        final int innerHeight = 32;
        final int innerUWidth = 32;
        final int innerVHeight = 64;
        final int innerTexWidth = 64;
        final int innerTexHeight = 64;

        final int outerwidth = 8;
        final int outHeight = 32;
        final int outerUWidth = 16;
        final int outerVHeight = 64;
        final int outerTexWidth = 64;
        final int outerTexHeight = 64;

        GlStateManager.disableLighting();
        GlStateManager.disableCull();

        final float[] rgb = ColorHelper.getRGBColor(colorPrimary);
        final float[] rgb2 = ColorHelper.getRGBColor(colorSecondary);
        final float alpha = 1F;
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        double motion = Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
        double amplitude = 0;
        amplitude += motion * 4.0;
        amplitude = Math.min(amplitude, 1.0);
        double time = entity.onGround ? 0 : entity.ticksExisted + partialTicks;
        double flapSpeed = 0.25;
        double flapAngleX = 20;   // Twist
        double sweepAngleY = 20; // Up Down
        double rollAngleZ = 30; // Flap
        float baseWingX = (float) (entity.onGround ? 2F : -0F);
        float baseWingY = (float) (entity.onGround ? 0 : -1F);
        float baseWingZ = (float) (entity.onGround ? 2F : 4F);
        double flap = Math.sin(time * flapSpeed);
        float angleX = (float) (flap * flapAngleX);
        angleX += entity.onGround ? 0 : (20F);
        float angleY = (float) (flap * sweepAngleY);
        angleY += -0F;
        float angleZ = (float) (flap * rollAngleZ);
        angleZ += 45F;
        float angleInnerX = (float) (angleZ);
        float angleMiddle = (float) ((flap * rollAngleZ) + (20F));
        float angleOuter = (float) (flap * rollAngleZ);
        float angleInnerZ = (float) (angleY);
        float angleInnerY = (float) (-angleX);
        double distance = 3.0;
        double coneAngle = Math.toRadians(45);
        double phase = time * flapSpeed;
        double pitch = Math.sin(phase) * coneAngle;
        double yaw = Math.cos(phase) * coneAngle;
        float offsetX = baseWingX - (float) (distance * Math.sin(yaw));
        float offsetY = baseWingY + (float) (distance * Math.sin(pitch));
        float offsetZ = baseWingZ - (float) (distance * Math.cos(yaw) * Math.cos(pitch));
        // Inner Section
        GlStateManager.translate((offsetX), (offsetY), (offsetZ));
        GlStateManager.scale(0.9, 0.9, 0.9);
        GlStateManager.rotate(angleInnerX, 0, 1, 0);
        GlStateManager.rotate(angleInnerY, 1, 0, 0);
        GlStateManager.rotate(angleInnerZ, 0, 0, 1);
        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb[0], rgb[1], rgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb[0], rgb[1], rgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
        }

        // Middle Sections
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angleMiddle, 0, 1, 0);
        GlStateManager.translate(-x, -y, -z);
        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, (x - innerWidth), y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, (x - innerWidth), y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
        }

        // Outer Section
        GlStateManager.translate(x - innerWidth, y, z);
        GlStateManager.rotate(angleOuter, 0, 1, 0);
        GlStateManager.translate(-(x - innerWidth), -y, -z);
        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, ((x - innerWidth) - outerwidth), y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
        }
        GlStateManager.popMatrix();

        // 2nd Wing
        GlStateManager.pushMatrix();
        // Inner
        GlStateManager.translate((offsetX), (offsetY), (-offsetZ));
        GlStateManager.scale(0.9, 0.9, 0.9);
        GlStateManager.rotate(-angleInnerX, 0, 1, 0);
        GlStateManager.rotate(-angleInnerY, 1, 0, 0);
        GlStateManager.rotate(angleInnerZ, 0, 0, 1);
        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb[0], rgb[1], rgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb[0], rgb[1], rgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
        }
        // Middle
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-angleMiddle, 0, 1, 0);
        GlStateManager.translate(-x, -y, -z);
        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
        }
        // Outer
        GlStateManager.translate(x - innerWidth, y, z);
        GlStateManager.rotate(-(angleOuter), 0, 1, 0);
        GlStateManager.translate(-(x - innerWidth), -y, -z);
        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
        } else {
            DrawingHelper.Draw(TEXTURE_ARMS, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb[0], rgb[1], rgb[2], alpha);
            DrawingHelper.Draw(TEXTURE_LEATHER, (x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight, rgb2[0], rgb2[1], rgb2[2], alpha);
        }
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

}
