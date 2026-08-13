package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityHelper {

	private static final double GROUND_PROBE_DISTANCE = 0.0625D;

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

	public static boolean isGrounded(Entity entity) {
		return isGrounded(entity, GROUND_PROBE_DISTANCE);
	}

	public static boolean isGrounded(Entity entity, double probeDistance) {
		if (entity == null) {
			return false;
		}
		if (entity.onGround) {
			return true;
		}
		if (entity.world == null) {
			return false;
		}
		final AxisAlignedBB bounds = entity.getEntityBoundingBox();
		if (bounds == null) {
			return false;
		}
		final AxisAlignedBB probe = bounds.grow(-1.0E-7D, 0.0D, -1.0E-7D).offset(0.0D, -Math.max(0.0D, probeDistance), 0.0D);
		return !entity.world.getCollisionBoxes(entity, probe).isEmpty();
	}

	public static boolean isGrounded(Entity entity, AxisAlignedBB bounds) {
		return isGrounded(entity, bounds, GROUND_PROBE_DISTANCE);
	}

	public static boolean isGrounded(Entity entity, AxisAlignedBB bounds, double probeDistance) {
		if (entity == null) {
			return false;
		}
		if (entity.onGround) {
			return true;
		}
		if ((entity.world == null) || (bounds == null)) {
			return false;
		}
		final AxisAlignedBB probe = bounds.grow(-1.0E-7D, 0.0D, -1.0E-7D).offset(0.0D, -Math.max(0.0D, probeDistance), 0.0D);
		return !entity.world.getCollisionBoxes(entity, probe).isEmpty();
	}

}
