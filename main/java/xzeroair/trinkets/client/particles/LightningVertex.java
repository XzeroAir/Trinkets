package xzeroair.trinkets.client.particles;

import java.util.Random;

import net.minecraft.util.math.Vec3d;
import xzeroair.trinkets.util.Reference;

public class LightningVertex {

	private double z;
	private double y;
	private double x;

	private Random rand = Reference.random;
	private int rx;
	private int ry;
	private int rz;

	public LightningVertex(Vec3d vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		rx = rand.nextInt(4) * (rand.nextBoolean() ? -1 : 1);
		ry = rand.nextInt(4) * (rand.nextBoolean() ? -1 : 1);
		rz = rand.nextInt(4) * (rand.nextBoolean() ? -1 : 1);
	}

	public Vec3d getVec() {
		return new Vec3d(x + (rx * 0.1F), y + (ry * 0.1F), z + (rz * 0.1F));
	}

}
