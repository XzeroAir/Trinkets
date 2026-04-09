package xzeroair.trinkets.client.races.taurus;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.model.BipedJsonModel;
import xzeroair.trinkets.client.races.IRenderModelInterface;
import xzeroair.trinkets.util.Reference;

import javax.annotation.Nonnull;

public class RaceTaurusFemaleHorns implements IRenderModelInterface {

    public static final BipedJsonModel TUARIAN_HORNS_F = new BipedJsonModel(new ResourceLocation(Reference.MODID, "race/taurus/tuarian_horns_f"));

    public static RaceTaurusFemaleHorns INSTANCE = new RaceTaurusFemaleHorns();

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
            GlStateManager.translate(0F, 0.2F, 0F);
        }
        ModelBase model = renderer.getMainModel();
        if (model instanceof ModelBiped) {
            ((ModelBiped) model).bipedHead.postRender(scale);
        }
        GlStateManager.rotate(180F, 0F, 0F, 1F);
        float cScale = 1.5F;
        GlStateManager.scale(cScale, cScale, cScale);
        GlStateManager.translate(-8F * scale, -8F * scale, 8F * scale);
        GlStateManager.color(1, 1, 1, 1);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float swing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
        GlStateManager.translate(0, 0.2, -1);
        this.getModel().render(entity, swing, Math.min(entity.limbSwingAmount, 1.0F), (float) entity.ticksExisted + partialTicks, yaw, pitch, scale, colorPrimary, colorSecondary, auxColor, false);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public BipedJsonModel getModel() {
        return TUARIAN_HORNS_F;
    }
}
