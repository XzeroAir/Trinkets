package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.Entity;

public class ItemEffectHandler {

	public static void pull(Entity ent, double x, double y, double z)
	{
		double dX = x - ent.posX;
		double dY = y - ent.posY;
		double dZ = z - ent.posZ;
		double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

		double vel = 1.0 - (dist / 15.0);
		if (vel > 0.0D)
		{
			vel *= vel;
			ent.motionX += (dX / dist) * vel * 0.1;
			ent.motionY += (dY / dist) * vel * 0.1;
			ent.motionZ += (dZ / dist) * vel * 0.1;
		}
	}

	public static void push(Entity ent, double x, double y, double z)
	{
		double dX = x - ent.posX;
		double dY = y - ent.posY;
		double dZ = z - ent.posZ;
		double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

		double vel = 1.0 - (dist / 15.0);
		if (vel > 0.0D)
		{
			vel *= vel;
			ent.motionX -= (dX / dist) * vel * 0.1;
			ent.motionY -= (dY / dist) * vel * 0.1;
			ent.motionZ -= (dZ / dist) * vel * 0.1;
		}
	}
}
