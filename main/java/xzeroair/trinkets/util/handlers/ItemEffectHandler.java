package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import xzeroair.trinkets.util.TrinketsConfig;

public class ItemEffectHandler {

	static int time = 0;

	public static void count() {
		time++;
	}

	public static int getCount() {
		return time;
	}

	public static void resetCount() {
		time = 0;
	}

	static double prev_posx;
	static double prev_posy;
	static double prev_posz;

	public static void startVec(EntityLivingBase player) {
		prev_posx = player.posX;
		prev_posy = player.posY;
		prev_posz = player.posZ;
	}

	public static Vec3d lastVec() {
		final Vec3d lastPosVec = new Vec3d(prev_posx, prev_posy, prev_posz);
		return lastPosVec;
	}

	public static double entitySpeed(EntityLivingBase player) {
		final Vec3d currentPosVec = new Vec3d(player.posX, player.posY, player.posZ);
		final double distanceTraveled = lastVec().distanceTo(currentPosVec);

		//		prev_posx = player.posX;
		//		prev_posy = player.posY;
		//		prev_posz = player.posZ;

		return distanceTraveled;
	}

	public static void pull(Entity ent, double x, double y, double z)
	{
		final double spd = TrinketsConfig.SERVER.C05_Polarized_Stone_Speed;
		final double dX = x - ent.posX;
		final double dY = y - ent.posY;
		final double dZ = z - ent.posZ;
		final double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

		double vel = 1.0 - (dist / 15.0);
		if (vel > 0.0D)
		{
			vel *= vel;
			ent.motionX += (dX / dist) * vel * spd;
			ent.motionY += (dY / dist) * vel * spd;
			ent.motionZ += (dZ / dist) * vel * spd;
		}
	}

	public static void push(Entity ent, double x, double y, double z)
	{
		final double spd = TrinketsConfig.SERVER.C05_Polarized_Stone_Speed;
		final double dX = x - ent.posX;
		final double dY = y - ent.posY;
		final double dZ = z - ent.posZ;
		final double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

		double vel = 1.0 - (dist / 15.0);
		if (vel > 0.0D)
		{
			vel *= vel;
			ent.motionX -= (dX / dist) * vel * spd;
			ent.motionY -= (dY / dist) * vel * spd;
			ent.motionZ -= (dZ / dist) * vel * spd;
		}
	}
}
