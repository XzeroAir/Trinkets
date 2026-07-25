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
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.model.BipedJsonModel;
import xzeroair.trinkets.client.races.IRenderModelInterface;
import xzeroair.trinkets.util.Reference;

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
        ModelBase model = renderer.getMainModel();
        if (model instanceof ModelBiped) {
            ((ModelBiped) model).bipedBody.postRender(scale);
        }
        boolean TEST_MODE = false;   // true = shake using time, false = shake using movement
        double maxAngleZ = isSlim ? 20 : 18;    // main sway (side-to-side)
        double maxAngleX = isSlim ? 0.8 : 0.6;     // movement drag tilt clamp
        double speed = isSlim ? 0.35 : 0.33;        // sway speed
        double followStrength = isSlim ? 6.0 : 4.5;
        double damping = 0.9;
        double motionGain = isSlim ? 1.5 : 1.35;
        double amplitudeCap = isSlim ? 1.0 : 0.9;
        double amplitudeDecay = isSlim ? 0.95 : 0.94;
        double tiltMultiplier = isSlim ? -20 : -14;
        double phase = (entity.ticksExisted + partialTicks) * speed;
        double forwardMotion = clientInfo.getForwardMotion();
        double amplitude = clientInfo.getAmplitude();

        if (TEST_MODE) {
            amplitude = 1.0;
        } else {
            amplitude += clientInfo.getMotion() * motionGain;
            amplitude = Math.min(amplitude, amplitudeCap);
            amplitude *= amplitudeDecay;
        }

        clientInfo.setAmplitude(amplitude);
        double targetTilt = -forwardMotion * followStrength * amplitude;
        double tiltX = clientInfo.getTiltX();
        tiltX += (targetTilt - clientInfo.getTiltX()) * 0.08; // follow strength
        tiltX *= damping;
        clientInfo.setTiltX(tiltX);
        double tailTiltX = tiltX * amplitude;
        tailTiltX = Math.max(-maxAngleX, Math.min(maxAngleX, tailTiltX));
        tailTiltX *= tiltMultiplier;
        double angleZ = Math.sin(phase + tiltX * 0.3) * amplitude * maxAngleZ;
        GlStateManager.rotate((float) angleZ, 0F, 1F, 0F);
        GlStateManager.rotate((float) tailTiltX, 1F, 0F, 0F);

        // Set Position
        boolean hasChestArmor = entity.hasItemInSlot(EntityEquipmentSlot.CHEST);
        double armorOffsetY = hasChestArmor ? 0.12 : 0.1;
        double armorOffsetZ = hasChestArmor ? -0.04 : 0.04;
        double offsetY = isSlim ? 1.15 : 1.27;
        double offsetZ = isSlim ? 0.3 : 0.34;
        float cScale = isSlim ? 0.7F : 0.82F;
        GlStateManager.rotate(15F, 1F, 0F, 0F);
        GlStateManager.translate(0.0, offsetY, offsetZ);
        GlStateManager.scale(cScale, cScale, cScale);
        GlStateManager.translate(0.0F, armorOffsetY, armorOffsetZ);
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
