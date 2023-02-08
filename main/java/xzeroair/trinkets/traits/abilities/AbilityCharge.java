package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class AbilityCharge extends Ability {

	public AbilityCharge() {
		super("Charge");
		// Battering Ram? Speed Based Attack?
	}

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

	protected boolean movingForward(EntityLivingBase entity, EnumFacing facing) {
		if (((facing.getDirectionVec().getX() * entity.motionX) > 0) || ((facing.getDirectionVec().getZ() * entity.motionZ) > 0)) {
			return true;
		}
		// return ((facing.getDirectionVec().getX() * player.motionX) +
		// (facing.getDirectionVec().getZ() * player.motionZ)) > 0;
		return false;
	}

}
