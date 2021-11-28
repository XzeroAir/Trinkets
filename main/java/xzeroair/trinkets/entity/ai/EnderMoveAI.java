package xzeroair.trinkets.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.AxisAlignedBB;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;

public class EnderMoveAI extends EntityAIFollow {
	private final EntityLiving enderman;
	private final Predicate<EntityLiving> followPredicate;
	private EntityPlayer followingEntity;
	private final double speedModifier;
	private final PathNavigate navigation;
	private int timeToRecalcPath;
	private final float stopDistance;
	private float oldWaterCost;
	private final float areaSize;

	public EnderMoveAI(final EntityEnderman entity) {
		super(entity, 1, 6F, 16);
		enderman = entity;
		followPredicate = new Predicate<EntityLiving>() {
			@Override
			public boolean apply(@Nullable EntityLiving following) {
				return (following != null) && (enderman.getClass() != following.getClass());
			}
		};
		speedModifier = 1;
		navigation = enderman.getNavigator();
		stopDistance = 6;
		areaSize = 16;
		this.setMutexBits(3);

		if (!(enderman.getNavigator() instanceof PathNavigateGround) && !(enderman.getNavigator() instanceof PathNavigateFlying)) {
			throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		final AxisAlignedBB bBox = enderman.getEntityBoundingBox();
		final List<EntityLivingBase> list = enderman.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, bBox.grow(16, 4, 16));
		if (!list.isEmpty()) {
			for (final EntityLivingBase entityliving : list) {
				if (entityliving instanceof EntityPlayer) {
					final boolean tiara = TrinketHelper.AccessoryCheck(entityliving, ModItems.trinkets.TrinketEnderTiara);
					final boolean ability = TrinketHelper.entityHasAbility(Abilities.enderQueen, entityliving);
					if (!entityliving.isInvisible() && (tiara || ability)) {
						followingEntity = (EntityPlayer) entityliving;
						int count = 0;
						if (followingEntity != null) {
							final List<EntityEnderman> allies = enderman.getEntityWorld().getEntitiesWithinAABB(EntityEnderman.class, entityliving.getEntityBoundingBox().grow(8, 4, 8));
							for (final EntityEnderman ally : allies) {
								count++;
							}
						}
						if ((count < 4)) {
							return true;
						}
					}
				}
			}

		}

		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		int count = 0;
		if (followingEntity != null) {
			final List<EntityEnderman> allies = enderman.getEntityWorld().getEntitiesWithinAABB(EntityEnderman.class, followingEntity.getEntityBoundingBox().grow(6, 4, 6));
			for (final EntityEnderman ally : allies) {
				count++;
			}
		}
		return (followingEntity != null) && (count < 4) && !navigation.noPath() && ((enderman.getDistanceSq(followingEntity) > (stopDistance * stopDistance)));
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		timeToRecalcPath = 0;
		oldWaterCost = enderman.getPathPriority(PathNodeType.WATER);
		enderman.setPathPriority(PathNodeType.WATER, 0.0F);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void resetTask() {
		followingEntity = null;
		navigation.clearPath();
		enderman.setPathPriority(PathNodeType.WATER, oldWaterCost);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void updateTask() {
		if ((followingEntity != null) && !enderman.getLeashed()) {
			enderman.getLookHelper().setLookPositionWithEntity(followingEntity, 10.0F, enderman.getVerticalFaceSpeed());

			if (--timeToRecalcPath <= 0) {
				timeToRecalcPath = 10;
				final double d0 = enderman.posX - followingEntity.posX;
				final double d1 = enderman.posY - followingEntity.posY;
				final double d2 = enderman.posZ - followingEntity.posZ;
				final double d3 = (d0 * d0) + (d1 * d1) + (d2 * d2);

				if (d3 > (stopDistance * stopDistance)) {
					navigation.tryMoveToEntityLiving(followingEntity, speedModifier);
				} else {
					navigation.clearPath();

					if (d3 <= stopDistance) {
						final double d4 = followingEntity.posX - enderman.posX;
						final double d5 = followingEntity.posZ - enderman.posZ;
						navigation.tryMoveToXYZ(enderman.posX - d4, enderman.posY, enderman.posZ - d5, speedModifier);
					}
				}
			}
		}
	}
}