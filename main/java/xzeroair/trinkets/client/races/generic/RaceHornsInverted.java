package xzeroair.trinkets.client.races.generic;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.model.BipedJsonModel;
import xzeroair.trinkets.client.races.IRenderModelInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceHornsInverted implements IRenderModelInterface {

    public static final BipedJsonModel HORNS = new BipedJsonModel(new ResourceLocation(Reference.MODID, "race/generic/horns_inverted"));

    public static final RaceHornsInverted INSTANCE = new RaceHornsInverted();

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale) {
        int variant = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitAuxVariant());
        int colorPrimary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitAuxColor());
        this.render(entity, renderer, isFake, isSlim, partialTicks, scale, variant, colorPrimary);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale, int variant, int colorPrimary) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        if (entity.isSneaking()) {
            GlStateManager.translate(0F, 0.2F, 0F);
        }
        if (Trinkets.MOD_COMPAT.MoBends && TrinketsConfig.compat.MO_BENDS) {
            ModelBase model = renderer.getMainModel();
            if (model instanceof ModelBiped) {
                ((ModelBiped) model).bipedHead.postRender(scale);
            }
        }
        GlStateManager.rotate(180F, 0F, 0F, 1F);
        GlStateManager.translate(0.0, -0.0, -0.7);
        GlStateManager.rotate(-20F + 0F, 1F, 0F, 0F);
        float cScale = 1.0F;
        GlStateManager.scale(cScale, cScale, cScale);
        GlStateManager.translate(-8F * scale, -8F * scale, 8F * scale);
        GlStateManager.color(1, 1, 1, 1);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float swing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
        this.getModel().render(entity, swing, Math.min(entity.limbSwingAmount, 1.0F), (float) entity.ticksExisted + partialTicks, yaw, pitch, scale, colorPrimary);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }


    public BipedJsonModel getModel() {
        return HORNS;
    }
}
