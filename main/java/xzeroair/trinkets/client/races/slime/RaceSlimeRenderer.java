package xzeroair.trinkets.client.races.slime;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.RaceDefaultRenderer;
import xzeroair.trinkets.races.slime.RaceSlime;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.ColorHelper;

@SideOnly(Side.CLIENT)
public class RaceSlimeRenderer extends RaceDefaultRenderer<RaceSlimeRenderer, RaceSlime> {

    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(Reference.MODID, "textures/races/slime/overlay.png");

    public RaceSlimeRenderer(EntityLivingBase entity, RaceSlime raceSlime) {
        super(entity, raceSlime);
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!this.getHandler().showTraits()) {
            return;
        }
        final float[] color = ColorHelper.getRGBColor(this.getHandler().getPrimaryTraitColor());
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);
        GlStateManager.color(color[0], color[1], color[2], 0.35F);
        GlStateManager.scale(1.05F, 1.05F, 1.05F);
        Minecraft.getMinecraft().renderEngine.bindTexture(OVERLAY_TEXTURE);
        renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    @Override
    public void doRenderHand(EnumHand hand, ItemStack itemStack, float swingProgress, float interpolatedPitch, float equipProgress, float partialTicks) {
        final EntityLivingBase entity = Minecraft.getMinecraft().player;
        if ((hand != EnumHand.MAIN_HAND) || !itemStack.isEmpty() || entity.isInvisible() || !this.getHandler().showTraits()) {
            return;
        }

        final Object entityRenderer = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entity);
        if (!(entityRenderer instanceof RenderPlayer)) {
            return;
        }

        final EnumHandSide handSide = entity instanceof EntityPlayer ? ((EntityPlayer) entity).getPrimaryHand() : EnumHandSide.RIGHT;
        this.renderHandOverlay(entity, (RenderPlayer) entityRenderer, handSide, swingProgress, equipProgress);
    }

    private void renderHandOverlay(EntityLivingBase entity, RenderPlayer renderer, EnumHandSide handSide, float swingProgress, float equipProgress) {
        final float side = handSide == EnumHandSide.RIGHT ? 1F : -1F;
        final float swingRoot = MathHelper.sqrt(swingProgress);
        final float swingX = -0.3F * MathHelper.sin(swingRoot * (float) Math.PI);
        final float swingY = 0.4F * MathHelper.sin(swingRoot * ((float) Math.PI * 2F));
        final float swingZ = -0.4F * MathHelper.sin(swingProgress * (float) Math.PI);
        final float swingSquare = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        final float swingAngle = MathHelper.sin(swingRoot * (float) Math.PI);
        final float[] color = ColorHelper.getRGBColor(this.getHandler().getPrimaryTraitColor());
        final ModelPlayer model = renderer.getMainModel();
        final ModelRenderer arm = handSide == EnumHandSide.RIGHT ? model.bipedRightArm : model.bipedLeftArm;
        final ModelRenderer armWear = handSide == EnumHandSide.RIGHT ? model.bipedRightArmwear : model.bipedLeftArmwear;

        GlStateManager.pushMatrix();
        GlStateManager.translate(side * (swingX + 0.64000005F), swingY - 0.6F - (equipProgress * 0.6F), swingZ - 0.71999997F);
        GlStateManager.rotate(side * 45F, 0F, 1F, 0F);
        GlStateManager.rotate(side * swingAngle * 70F, 0F, 1F, 0F);
        GlStateManager.rotate(side * swingSquare * -20F, 0F, 0F, 1F);
        GlStateManager.translate(side * -1F, 3.6F, 3.5F);
        GlStateManager.rotate(side * 120F, 0F, 0F, 1F);
        GlStateManager.rotate(200F, 1F, 0F, 0F);
        GlStateManager.rotate(side * -135F, 0F, 1F, 0F);
        GlStateManager.translate(side * 5.6F, 0F, 0F);
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);
        GlStateManager.color(color[0], color[1], color[2], 0.35F);
        GlStateManager.translate(arm.rotationPointX * 0.0625F, arm.rotationPointY * 0.0625F, arm.rotationPointZ * 0.0625F);
        GlStateManager.scale(1.05F, 1.05F, 1.05F);
        GlStateManager.translate(-arm.rotationPointX * 0.0625F, -arm.rotationPointY * 0.0625F, -arm.rotationPointZ * 0.0625F);
        Minecraft.getMinecraft().renderEngine.bindTexture(OVERLAY_TEXTURE);
        model.swingProgress = 0F;
        model.isSneak = false;
        model.setRotationAngles(0F, 0F, 0F, 0F, 0F, 0.0625F, entity);
        arm.rotateAngleX = 0F;
        arm.render(0.0625F);
        armWear.rotateAngleX = 0F;
        armWear.render(0.0625F);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
