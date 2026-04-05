package xzeroair.trinkets.client.races.faelis;

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
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import javax.annotation.Nonnull;

public class RaceFaelisEars implements IRenderModelInterface {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/races/faelis/ears.png");
//    public static final ResourceLocation TEXTURE_INNER = new ResourceLocation(Reference.MODID + ":" + "textures/inner_ears.png");
//    public static final ResourceLocation TEXTURE_OUTER = new ResourceLocation(Reference.MODID + ":" + "textures/outer_ears.png");

    public static RaceFaelisEars INSTANCE = new RaceFaelisEars();

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale) {
        int variant = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitVariant());
        int colorPrimary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getColorOption() == 1 ? prop.getRaceHandler().getSecondaryTraitColor() : prop.getRaceHandler().getPrimaryTraitColor());
        int colorSecondary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getColorOption() > 0 ? prop.getRaceHandler().getPrimaryTraitColor() : prop.getRaceHandler().getSecondaryTraitColor());
        this.render(entity, renderer, isFake, isSlim, partialTicks, scale, variant, colorPrimary, colorSecondary);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(@Nonnull EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale, int variant, int colorPrimary, int colorSecondary) {
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            GlStateManager.translate(0, 0.2, 0);
        }
        if (renderer instanceof RenderPlayer) {
            final RenderPlayer rend = (RenderPlayer) renderer;
            rend.getMainModel().bipedHead.postRender(scale);
        }
        if (entity.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
            GlStateManager.translate(0.0F, -0.02F, -0.045F);
            GlStateManager.scale(1.1F, 1.1F, 1.1F);
        }
        final float[] rgb = ColorHelper.getRGBColor(colorPrimary);
        final float[] rgb2 = ColorHelper.getRGBColor(colorSecondary);
        final float fscale = 0.30F;
        GlStateManager.scale(fscale, fscale, fscale);
        final double x = 0.0;
        final double y = -2.4;
        final double z = -0.72;
        final double height = 1;
        final double width = 1;
        final float u = 0;
        final float v = 0;
        final int uWidth = 16;
        final int vHeight = 16;
        final float tileWidth = 16;
        final float tileHeight = 64;
        final double xR = -0.3;
        final double xL = -xR;
        float rot = 26F;
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.rotate(-rot, 0, 1, 0);
        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xR, y, z + 0.0001, u, v + 48, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
        } else {
            DrawingHelper.Draw(TEXTURE, x + xR, y, z, u, v + 16, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xR, y, z, u, v + 32, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xR, y, z + 0.0001, u, v + 48, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
        }
        GlStateManager.rotate(rot * 2, 0, 1, 0);
        if (colorPrimary == colorSecondary) {
            DrawingHelper.Draw(TEXTURE, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xL, y, z + 0.0001, u, v + 48, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
        } else {
            DrawingHelper.Draw(TEXTURE, x + xL, y, z, u, v + 16, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xL, y, z, u, v + 32, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xL, y, z + 0.0001, u, v + 48, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
        }
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }
}