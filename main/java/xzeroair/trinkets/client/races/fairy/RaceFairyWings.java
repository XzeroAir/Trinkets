package xzeroair.trinkets.client.races.fairy;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.model.Wings;
import xzeroair.trinkets.client.races.IRenderModelInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class RaceFairyWings implements IRenderModelInterface {

    public static RaceFairyWings INSTANCE = new RaceFairyWings();

    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/races/fairy/fairy_wings.png");

    @SideOnly(Side.CLIENT)
    private final ModelBase wings = new Wings();

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
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale, int variant, int colorPrimary, int colorSecondary) {
        GlStateManager.pushMatrix();
        if (isFake ? variant == 0 : (variant == 1)) {
            GlStateManager.pushMatrix();
            if (entity.isSneaking()) {
                GlStateManager.translate(0F, 0.2F, 0F);
            }
//            if (entity.isSneaking()) {
//                GlStateManager.translate(0F, 0F, -0.1F);
//            }
            if (Trinkets.MOD_COMPAT.MoBends && TrinketsConfig.compat.MO_BENDS) {
                ModelBase model = renderer.getMainModel();
                if (model instanceof ModelBiped) {
                    ((ModelBiped) model).bipedBody.postRender(scale);
                }
            }
            if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
                GlStateManager.translate(0F, -0.1F, 0.06F);
                GlStateManager.scale(1.1F, 1.1F, 1.1F);
            }
//            GlStateManager.scale(scale, scale, scale);
            float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
            float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            float swing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
            this.wings.render(entity, swing, Math.min(entity.limbSwingAmount, 1.0F), (float) entity.ticksExisted + partialTicks, yaw, pitch, scale);
            GlStateManager.popMatrix();
        } else {
            int frames = 60;
            int angleTick = 0;
            if (!entity.onGround) {
                angleTick = (int) (((entity.ticksExisted + partialTicks) * 24) % frames);
            }
            if (angleTick >= (frames)) {
                angleTick = frames - 1;
            }
            float angle = Math.max(50F - angleTick, 0F);

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
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(angle, 0, 1, 0);
            GlStateManager.translate(-1, 0, 0);
            final float[] rgb = ColorHelper.getRGBColor(colorPrimary);
            DrawingHelper.Draw(TEXTURE, -x, y, z, 0, 0, barCutoffWidth, barCutoffHeight, barWidth, barHeight, texWidth, texHeight, rgb[0], rgb[1], rgb[2], 1F);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.rotate(-angle, 0, 1, 0);
            GlStateManager.translate(-1, 0, 0);
            DrawingHelper.Draw(TEXTURE, -x, y, -z, 0, 0, barCutoffWidth, barCutoffHeight, barWidth, barHeight, texWidth, texHeight, rgb[0], rgb[1], rgb[2], 1F);
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
        }
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }
}