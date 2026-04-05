package xzeroair.trinkets.client.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.effects.TrinketRingEffects;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ShieldAuraEffect extends Particle {

    private boolean depth;

    public ShieldAuraEffect(World world, Vec3d vec, int color, float a, boolean bool) {
        super(world, vec.x, vec.y, vec.z);
        final int r = (color & 16711680) >> 16;
        final int g = (color & 65280) >> 8;
        final int b = (color & 255) >> 0;
        this.posX = vec.x;
        this.posY = vec.y;
        this.posZ = vec.z;
        this.prevPosX = vec.x;
        this.prevPosY = vec.y;
        this.prevPosZ = vec.z;
        this.particleRed = r / 255.0F;
        this.particleGreen = g / 255.0F;
        this.particleBlue = b / 255.0F;
        this.particleAlpha = a;
        this.depth = bool;
        this.particleMaxAge = 120;
        this.particleAge = 0;
    }

    @Override
    public void renderParticle(@Nonnull BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Tessellator tessellator = Tessellator.getInstance();
        int segments = 8;
        float baseRadius = 2.0f;

        float time = entity.ticksExisted + partialTicks;

        float speedX = 0.0f;
        float speedY = 10.0f;
        float speedZ = 10.0f;

        float rotX = ((time * speedX) % 360f);
        float rotY = ((time * speedY) % 180f);
        float rotZ = ((time * speedZ) % 180f);

        float pulse = (float) Math.sin(time * 0.5f) * (baseRadius * 0.8F);
        float radius = baseRadius + pulse;

        float alpha = 0.5f + 0.3f * (float) Math.sin(time * 0.2f);
        double x = 0;
        double y = 0;//entity.height * 0.5;
        double z = 0;
        float tPos = entity.width * 0.5F;
        float yPos = entity.height * 0.5F;
        final float f5 = (float) ((this.prevPosX + ((this.posX - this.prevPosX) * partialTicks)) - interpPosX);
        final float f6 = (float) ((this.prevPosY + ((this.posY - this.prevPosY) * partialTicks)) - interpPosY);
        final float f7 = (float) ((this.prevPosZ + ((this.posZ - this.prevPosZ) * partialTicks)) - interpPosZ);
        GlStateManager.pushMatrix();
//        GlStateManager.translate(0, yPos, -0.0);
//        GlStateManager.rotate(90F, 0, 0, 1);
        GlStateManager.translate(f5, f6, f7);
        TrinketRingEffects.renderRing(tessellator, buffer, segments, rotX, 0, 0, radius, 0.2F, 0.5F, 1.0F, alpha);
//        renderRing(tessellator, buffer, segments, 0, rotY, 0, radius, 0.2F, 0.5F, 1.0F, alpha);
//        renderRing(tessellator, buffer, segments, 0, 0, rotZ, radius, 0.2F, 0.5F, 1.0F, alpha);
//        GlStateManager.rotate(-90F, 0, 0, 1);
        GlStateManager.popMatrix();
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
    }

    @Override
    public int getFXLayer() {
        return 3;
    }
}
