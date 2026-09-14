package xzeroair.trinkets.client.particles;

import net.minecraft.client.particle.ParticleSnowShovel;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleColoredSnow extends ParticleSnowShovel {

    public ParticleColoredSnow(World world, double x, double y, double z, double motionX, double motionY, double motionZ, int color) {
        this(world, x, y, z, motionX, motionY, motionZ, color, 0.03F, 12);
    }

    public ParticleColoredSnow(World world, double x, double y, double z, double motionX, double motionY, double motionZ, int color, float gravity, int maxAge) {
        super(world, x, y, z, motionX, motionY, motionZ, 0.75F);
        this.particleRed = (color >> 16 & 255) / 255.0F;
        this.particleGreen = (color >> 8 & 255) / 255.0F;
        this.particleBlue = (color & 255) / 255.0F;
        this.particleGravity = gravity;
        this.canCollide = false;
        this.setMaxAge(maxAge);
    }
}
