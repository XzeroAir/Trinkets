package xzeroair.trinkets.client.races.dragon;

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

public class RaceDragonHorns implements IRenderModelInterface {
    public static final BipedJsonModel HORNS = new BipedJsonModel(new ResourceLocation(Reference.MODID, "race/dragon/dragon_horns"));

    public static final RaceDragonHorns INSTANCE = new RaceDragonHorns();

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
        ModelBase model = renderer.getMainModel();
        if (model instanceof ModelBiped) {
            ((ModelBiped) model).bipedHead.postRender(scale);
        }
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
//        float bodyYaw = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;
        float swing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
//        float netHeadYaw = yaw - bodyYaw;
        double offsetX = 0.27;
        double offsetY = -0.48;
        double offsetZ = -0.3;
        float cScale = 0.54F;
//        GlStateManager.rotate(netHeadYaw, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(offsetX, offsetY, offsetZ);
        GlStateManager.rotate(180F, 0, 0, 1);
        GlStateManager.scale(cScale, cScale, cScale);
        GlStateManager.color(1, 1, 1, 1);
        this.getModel().render(entity, swing, Math.min(entity.limbSwingAmount, 1.0F), (float) entity.ticksExisted + partialTicks, yaw, pitch, scale, colorPrimary);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }


    public BipedJsonModel getModel() {
        return HORNS;
    }
}
