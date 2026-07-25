package xzeroair.trinkets.client.races.faelis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.races.IRenderModelInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import javax.annotation.Nonnull;

public class RaceFaelisClaws implements IRenderModelInterface {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/races/faelis/claws.png");

    public static RaceFaelisClaws INSTANCE = new RaceFaelisClaws();

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale) {
        int variant = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitVariant());
        int colorPrimary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitAuxColor());
        int colorSecondary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getColorOption() > 0 ? prop.getRaceHandler().getPrimaryTraitColor() : prop.getRaceHandler().getSecondaryTraitColor());
        this.render(entity, renderer, isFake, isSlim, partialTicks, scale, variant, colorPrimary, colorSecondary);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(@Nonnull EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale, int variant, int colorPrimary, int colorSecondary) {
        this.render(entity, renderer, isFake, isSlim, partialTicks, scale, variant, colorPrimary, colorSecondary, true, true);
    }

    @SideOnly(Side.CLIENT)
    public void render(@Nonnull EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale, int variant, int colorPrimary, int colorSecondary, boolean renderLeft, boolean renderRight) {
        if (!renderLeft && !renderRight) {
            return;
        }
        final float offsetX = isSlim ? -12.4F : -18.6F;
        final float offsetY = 61F;
        final float offsetZ = -21F;
        final float bS = 0.16f;
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableCull();
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        if (renderLeft) {
            GlStateManager.pushMatrix();
            if (entity.isSneaking()) {
                GlStateManager.translate(0F, 0.2F, 0F);
            }
            ModelBase model = renderer.getMainModel();
            if (model instanceof ModelBiped) {
                ((ModelBiped) model).bipedLeftArm.postRender(scale);
            }
            GlStateManager.scale(scale * bS, scale * bS, scale * bS);
            GlStateManager.translate(-offsetX, offsetY, offsetZ);
            GlStateManager.rotate(-90F, 0F, 1F, 0F);
            DrawingHelper.Draw(0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32);
            GlStateManager.popMatrix();
        }
        if (renderRight) {
            GlStateManager.pushMatrix();
            if (entity.isSneaking()) {
                GlStateManager.translate(0F, 0.2F, 0F);
            }
            if (renderer instanceof RenderPlayer) {
                final RenderPlayer rend = (RenderPlayer) renderer;
                rend.getMainModel().bipedRightArm.postRender(scale);
            }
            GlStateManager.scale(scale * bS, scale * bS, scale * bS);
            GlStateManager.translate(offsetX, offsetY, offsetZ);
            GlStateManager.rotate(-90F, 0F, 1F, 0F);
            DrawingHelper.Draw(0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32);
            GlStateManager.popMatrix();
        }
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
