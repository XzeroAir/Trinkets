package xzeroair.trinkets.client.races.faelis;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.BipedJsonModel;
import xzeroair.trinkets.client.races.IRenderModelInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceFaelisTail implements IRenderModelInterface {

    public static final BipedJsonModel TAIL = new BipedJsonModel(new ResourceLocation(Reference.MODID, "race/faelis/tail"));

    public static final RaceFaelisTail INSTANCE = new RaceFaelisTail();

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale) {
        int variant = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitAuxVariant());
        int colorPrimary = Capabilities.getEntityProperties(entity, 0, (prop, rtn) -> prop.getRaceHandler().getTraitAuxColor());
        this.render(entity, renderer, isFake, isSlim, partialTicks, scale, variant, colorPrimary, colorPrimary);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float partialTicks, float scale, int variant, int colorPrimary, int colorSecondary) {
        EntityProperties.ClientInfo clientInfo = Capabilities.getEntityProperties(entity, new EntityProperties.ClientInfo(), (prop, rtn) -> prop.getClientInfo());
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        if (entity.isSneaking()) {
            GlStateManager.translate(0F, 0.2F, 0F);
        }
        if (Trinkets.MOD_COMPAT.MoBends && TrinketsConfig.compat.MO_BENDS) {
            ModelBase model = renderer.getMainModel();
            if (model instanceof ModelBiped) {
                ((ModelBiped) model).bipedBody.postRender(scale);
            }
        }
        boolean TEST_MODE = false;   // true = shake using time, false = shake using movement
        double maxAngleZ = 20;    // main bell swing (side-to-side)
        double maxAngleX = 0.8;     // movement drag tilt
        double maxAngleY = 0.0;    // optional twist
        double speed = 0.35;        // shake speed
        double followStrength = 6.0;
        double damping = 0.9;
        double phase = (entity.ticksExisted + partialTicks) * speed;
        double forwardMotion = clientInfo.getForwardMotion();
        double amplitude = clientInfo.getAmplitude();

        if (TEST_MODE) {
            amplitude = 1.0;
        } else {
            amplitude += clientInfo.getMotion() * 1.5;
            amplitude = Math.min(amplitude, 1.0);
            amplitude *= 0.95;
        }

        clientInfo.setAmplitude(amplitude);
        double targetTilt = -forwardMotion * followStrength * amplitude;
        double tiltX = clientInfo.getTiltX();
        tiltX += (targetTilt - clientInfo.getTiltX()) * 0.08; // follow strength
        tiltX *= damping;
        clientInfo.setTiltX(tiltX);
        double bellTiltX = tiltX * amplitude;
        bellTiltX = Math.max(-maxAngleX, Math.min(maxAngleX, bellTiltX));
        bellTiltX *= -20;
        double angleZ = Math.sin(phase + tiltX * 0.3) * amplitude * maxAngleZ;
        double angleY = Math.sin(phase + 2.4) * amplitude * maxAngleY;
        GlStateManager.rotate((float) angleZ, 0F, 1F, 0F);
        GlStateManager.rotate((float) bellTiltX, 1F, 0F, 0F);

        // Set Position
        boolean hasHelmet = entity.hasItemInSlot(EntityEquipmentSlot.CHEST);
        double helmetOffsetY = hasHelmet ? 0.12 : 0.1;
        double helmetOffsetZ = hasHelmet ? -0.04 : 0 + 0.04;
        GlStateManager.rotate(15F, 1F, 0F, 0F);
        GlStateManager.translate(0.0, 1.15 + (isSlim ? 0 : 0.12), 0.3);
        float cScale = isSlim ? 0.7F : 1.0F;
        GlStateManager.scale(cScale, cScale, cScale);
        GlStateManager.translate(0.0F, helmetOffsetY, helmetOffsetZ);
        // Change Rotation and Adjust to body.
        GlStateManager.rotate(180F, 0F, 0F, 1F);
        GlStateManager.translate(-8F * scale, -8F * scale, 8F * scale);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float swing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
        GlStateManager.translate(0, 0.2, -1);
        // Do Render
        GlStateManager.color(1, 1, 1, 1);
        this.getModel().render(entity, swing, Math.min(entity.limbSwingAmount, 1.0F), (float) entity.ticksExisted + partialTicks, yaw, pitch, scale, colorPrimary, colorSecondary);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }


    public BipedJsonModel getModel() {
        return TAIL;
    }
}
