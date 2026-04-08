package xzeroair.trinkets.client.races.taurus;

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

public class RaceTaurusBell implements IRenderModelInterface {

    public static final BipedJsonModel TUARIAN_COWBELL = new BipedJsonModel(new ResourceLocation(Reference.MODID, "race/taurus/tuarian_cowbell"));

    public static final RaceTaurusBell INSTANCE = new RaceTaurusBell();

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
        // Do Bell Ring
        boolean TEST_MODE = false;   // true = shake using time, false = shake using movement
        double maxAngleZ = 15;    // main bell swing (side-to-side)
        double maxAngleX = 1.0;     // movement drag tilt
        double maxAngleY = 0.10;    // optional twist
        double speed = 0.35;        // shake speed
        double followStrength = 6.0;
        double damping = 0.92;
        double phase = (entity.ticksExisted + partialTicks) * speed;
        double forwardMotion = clientInfo.getForwardMotion();
        double amplitude = clientInfo.getAmplitude();

        if (TEST_MODE) {
            amplitude = 1.0;
        } else {
            // add energy from movement (reduced gain)
            amplitude += clientInfo.getMotion() * 1.5;

            // clamp
            amplitude = Math.min(amplitude, 1.0);

            // slow decay (THIS controls "how long it rings")
            amplitude *= 0.97;   // was ~0.92–0.95, now much slower
        }

        clientInfo.setAmplitude(amplitude);
        double targetTilt = -forwardMotion * followStrength * amplitude;
        double tiltX = clientInfo.getTiltX();
        tiltX += (targetTilt - clientInfo.getTiltX()) * 0.08; // follow strength
        tiltX *= damping;
        clientInfo.setTiltX(tiltX);
        double bellTiltX = tiltX * amplitude;
        bellTiltX = Math.max(-maxAngleX, Math.min(maxAngleX, bellTiltX));
        bellTiltX *= -30;
        double angleZ = Math.sin(phase + tiltX * 0.3) * amplitude * maxAngleZ;
        double angleY = Math.sin(phase + 2.4) * amplitude * maxAngleY;
        GlStateManager.rotate((float) angleZ, 0F, 0F, 1F);
        GlStateManager.rotate((float) bellTiltX, 1F, 0F, 0F);

        // Set Position
        boolean hasHelmet = entity.hasItemInSlot(EntityEquipmentSlot.CHEST);
        double helmetOffsetY = hasHelmet ? 0.12 : 0.1;
        double helmetOffsetZ = hasHelmet ? -0.04 : 0 + 0.04;
        GlStateManager.rotate(-15F, 1F, 0F, 0F);
        GlStateManager.translate(0.0, 0.44 + (isSlim ? 0 : 0.12), -0.16);
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
        this.getModel().render(entity, swing, Math.min(entity.limbSwingAmount, 1.0F), (float) entity.ticksExisted + partialTicks, yaw, pitch, scale, colorPrimary);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public BipedJsonModel getModel() {
        return TUARIAN_COWBELL;
    }
}
