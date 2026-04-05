package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EntityHelper {

	public static boolean isCreative(Entity entity) {
		if (entity instanceof EntityPlayer) {
			return ((EntityPlayer) entity).isCreative();
		}
		return false;
	}

	public static boolean canFly(Entity entity) {
		if (entity instanceof EntityPlayer) {
			return ((EntityPlayer) entity).capabilities.allowFlying;
		}
		return false;
	}

	public static boolean isFlying(Entity entity) {
		if (entity instanceof EntityPlayer) {
			return ((EntityPlayer) entity).capabilities.isFlying;
		}
		return false;
	}

	public static boolean canFlyIsFlying(Entity entity) {
		if (entity instanceof EntityPlayer) {
			return ((EntityPlayer) entity).capabilities.allowFlying && ((EntityPlayer) entity).capabilities.isFlying;
		}
		return false;
	}

	public static boolean isSpectator(Entity entity) {
		if (entity instanceof EntityPlayer) {
			return ((EntityPlayer) entity).isSpectator();
		}
		return false;
	}

}
