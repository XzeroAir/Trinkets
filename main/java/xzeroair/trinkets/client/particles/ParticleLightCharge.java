package xzeroair.trinkets.client.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleLightCharge extends Particle {

    private final double targetX;
    private final double targetY;
    private final double targetZ;
    private final double originX;
    private final double originY;
    private final double originZ;

    public ParticleLightCharge(World world, double x, double y, double z, double targetX, double targetY, double targetZ, int color) {
        super(world, x, y, z);
        this.originX = x;
        this.originY = y;
        this.originZ = z;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.particleRed = ((color >> 16) & 255) / 255.0F;
        this.particleGreen = ((color >> 8) & 255) / 255.0F;
        this.particleBlue = (color & 255) / 255.0F;
        this.particleAlpha = 0.95F;
        this.particleScale = 0.36F + (world.rand.nextFloat() * 0.18F);
        this.particleMaxAge = 10 + world.rand.nextInt(4);
        this.setParticleTextureIndex(7);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
            return;
        }
        final float progress = MathHelper.clamp((float) this.particleAge / (float) this.particleMaxAge, 0.0F, 1.0F);
        final float easedProgress = progress * progress;
        this.posX = this.originX + ((this.targetX - this.originX) * easedProgress);
        this.posY = this.originY + ((this.targetY - this.originY) * easedProgress);
        this.posZ = this.originZ + ((this.targetZ - this.originZ) * easedProgress);
        this.particleAlpha = 0.95F * (1.0F - progress);
        this.particleScale *= 0.93F;
    }

    @Override
    public int getBrightnessForRender(float partialTick) {
        return 15728880;
    }
}
