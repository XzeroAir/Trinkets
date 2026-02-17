package xzeroair.trinkets.client.races.fairy;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.model.Wings;
import xzeroair.trinkets.client.races.RaceDefaultRenderer;
import xzeroair.trinkets.races.fairy.RaceFairy;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class RaceFairyRenderer extends RaceDefaultRenderer<RaceFairyRenderer, RaceFairy> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings.png");

    float armSwing = 0;

    @SideOnly(Side.CLIENT)
    private final ModelBase wings = new Wings();

    public RaceFairyRenderer(EntityLivingBase entity, RaceFairy raceFairy) {
        super(entity, raceFairy);
    }

    @Override
    public void whileTransformed(EntityLivingBase entity) {
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void doRenderLayer(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!TrinketsConfig.CLIENT.rendering || !getHandler().showTraits()) {
            return;
        }
        GlStateManager.pushMatrix();
        if (isFake || (getHandler().getTraitVariant() == 2)) {
            GlStateManager.pushMatrix();
            if (renderer instanceof RenderPlayer) {
                final RenderPlayer r = (RenderPlayer) renderer;
                r.getMainModel().bipedBody.postRender(scale);
            }
            if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
                GlStateManager.translate(0F, -0.1F, 0.06F);
                GlStateManager.scale(1.1F, 1.1F, 1.1F);
            }
            if (entity.isSneaking()) {
                GlStateManager.translate(0F, 0F, -0.1F);
            }
            GlStateManager.scale(scale, scale, scale);
            wings.render(entity, entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted, entity.rotationYaw, entity.rotationPitch, 1);
            GlStateManager.popMatrix();
        } else {
            final float flap = MathHelper.cos((limbSwing * 0.6662F) + (float) Math.PI);
            final int angle = 54;
            if (!entity.onGround || (flap != armSwing)) {
                tick += 1 * 6;
            }
            if (((tick > angle) || ((flap == armSwing) && entity.onGround))) {
                tick = 0;
            }

            if (entity.isSneaking()) {
                GlStateManager.translate(0F, 0.2F, 0F);
            }
            if (renderer instanceof RenderPlayer) {
                final RenderPlayer rend = (RenderPlayer) renderer;
                rend.getMainModel().bipedBody.postRender(scale);
            }
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.translate(0, -2F, 0);
            if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
                GlStateManager.translate(-0.4F, -1F, 0F);
            }

            final double x = 18;
            final double y = 0;
            final double z = -1;

            final int barWidth = 16;
            final int barHeight = 16;
            final int barCutoffWidth = 36;
            final int barCutoffHeight = 42;
            final int texWidth = 36;
            final int texHeight = 42;

            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(angle - tick, 0, 1, 0);
            GlStateManager.translate(-1, 0, 0);
            final float[] rgb = ColorHelper.getRGBColor(getHandler().getTraitVariant() == 1 ? getHandler().getSecondaryTraitColor() : getHandler().getPrimaryTraitColor());
            DrawingHelper.Draw(TEXTURE, -x, y, z, 0, 0, barCutoffWidth, barCutoffHeight, barWidth, barHeight, texWidth, texHeight, rgb[0], rgb[1], rgb[2], 1F);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.rotate(-angle + tick, 0, 1, 0);
            GlStateManager.translate(-1, 0, 0);
            DrawingHelper.Draw(TEXTURE, -x, y, -z, 0, 0, barCutoffWidth, barCutoffHeight, barWidth, barHeight, texWidth, texHeight, rgb[0], rgb[1], rgb[2], 1F);
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            armSwing = flap;
        }
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }
}
