package xzeroair.trinkets.client.particles;

import net.minecraft.util.math.Vec3d;
import xzeroair.trinkets.util.Reference;

import java.util.Random;

public class LightningVertex {

    private final double z;
    private final double y;
    private final double x;

    private final Random rand = Reference.random;
    private final int rx;
    private final int ry;
    private final int rz;

    public LightningVertex(Vec3d vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        this.rx = this.rand.nextInt(4) * (this.rand.nextBoolean() ? -1 : 1);
        this.ry = this.rand.nextInt(4) * (this.rand.nextBoolean() ? -1 : 1);
        this.rz = this.rand.nextInt(4) * (this.rand.nextBoolean() ? -1 : 1);
    }

    public Vec3d getVec() {
        return new Vec3d(this.x + (this.rx * 0.1F), this.y + (this.ry * 0.1F), this.z + (this.rz * 0.1F));
    }

}
