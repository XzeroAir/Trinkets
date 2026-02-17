package xzeroair.trinkets.client.races.goblin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.RaceDefaultRenderer;
import xzeroair.trinkets.races.goblin.RaceGoblin;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class RaceGoblinRenderer extends RaceDefaultRenderer<RaceGoblinRenderer, RaceGoblin> {


    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/ears.png");
    public static final ResourceLocation TEXTURE_INNER = new ResourceLocation(Reference.MODID + ":" + "textures/inner_ears.png");
    public static final ResourceLocation TEXTURE_OUTER = new ResourceLocation(Reference.MODID + ":" + "textures/outer_ears.png");

    public RaceGoblinRenderer(EntityLivingBase entity, RaceGoblin raceGoblin) {
        super(entity, raceGoblin);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderLayer(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!TrinketsConfig.CLIENT.rendering || !getHandler().showTraits()) {
            return;
        }
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
        final float[] rgb = ColorHelper.getRGBColor(getHandler().getTraitVariant() == 1 ? getHandler().getSecondaryTraitColor() : getHandler().getPrimaryTraitColor());
        final float[] rgb2 = ColorHelper.getRGBColor(getHandler().getTraitVariant() == 1 ? getHandler().getPrimaryTraitColor() : getHandler().getSecondaryTraitColor());
        final float fscale = 0.34F;
        GlStateManager.scale(fscale, fscale, fscale);
        final double x = 0.0;
        final double y = -1.2;
        final double z = -0.6;
        final double height = 1;
        final double width = 1;
        final float u = 0;
        final float v = 0;
        final int uWidth = 16;
        final int vHeight = 16;
        final float tileWidth = 64;
        final float tileHeight = 32;
        final double xR = 0.50;
        final double xL = -xR;
        float rot = 30F;
        final int solidVariant = 2;
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.pushMatrix();
        GlStateManager.rotate(-rot, 0, 1, 0);
        GlStateManager.rotate(-10, 1, 0, 0);
        //			GlStateManager.rotate(20, 0, 0, 1);
        if (getHandler().getTraitVariant() == solidVariant) {
            DrawingHelper.Draw(TEXTURE, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xR, y, z + 0.0001, u, v + 16, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
        } else {
            DrawingHelper.Draw(TEXTURE_INNER, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE_OUTER, x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
            DrawingHelper.Draw(TEXTURE_OUTER, x + xR, y, z + 0.0001, u, v + 16, uWidth, vHeight, width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.rotate(rot, 0, 1, 0);
        GlStateManager.rotate(-10, 1, 0, 0);
        //			GlStateManager.rotate(-20, 0, 0, 1);
        if (getHandler().getTraitVariant() == solidVariant) {
            DrawingHelper.Draw(TEXTURE, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE, x + xL, y, z + 0.0001, u, v + 16, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
        } else {
            DrawingHelper.Draw(TEXTURE_INNER, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb[0], rgb[1], rgb[2], 1F);
            DrawingHelper.Draw(TEXTURE_OUTER, x + xL, y, z, u, v, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
            DrawingHelper.Draw(TEXTURE_OUTER, x + xL, y, z + 0.0001, u, v + 16, uWidth, vHeight, -width, height, tileWidth, tileHeight, rgb2[0], rgb2[1], rgb2[2], 1F);
        }
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }
}
