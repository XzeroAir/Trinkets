package xzeroair.trinkets.entity.ai;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;

public class EnderAiEdit extends EntityAINearestAttackableTarget<EntityPlayer> {

	private final EntityEnderman enderman;
	/** The player */
	private EntityPlayer player;
	private int aggroTime;
	private int teleportTime;

	public EnderAiEdit(EntityEnderman enderman) {
		super(enderman, EntityPlayer.class, false);
		this.enderman = enderman;
	}

	private boolean shouldAttackPlayer(EntityPlayer player) {
		final ItemStack itemstack = player.inventory.armorInventory.get(3);
		final boolean eye = TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye);
		final boolean tiara = TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara);
		final boolean ability = TrinketHelper.entityHasAbility(Abilities.enderQueen, player);
		if ((ability) || (tiara == true) || (eye == true) || (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN))) {
			return false;
		} else {
			final Vec3d vec3d = player.getLook(1.0F).normalize();
			Vec3d vec3d1 = new Vec3d(enderman.posX - player.posX, (enderman.getEntityBoundingBox().minY + enderman.getEyeHeight()) - (player.posY + player.getEyeHeight()), enderman.posZ - player.posZ);
			final double d0 = vec3d1.length();
			vec3d1 = vec3d1.normalize();
			final double d1 = vec3d.dotProduct(vec3d1);
			return d1 > (1.0D - (0.025D / d0)) ? player.canEntityBeSeen(enderman) : false;
		}
	}

	protected boolean teleportRandomly() {
		final double d0 = enderman.posX + ((Reference.random.nextDouble() - 0.5D) * 64.0D);
		final double d1 = enderman.posY + (Reference.random.nextInt(64) - 32);
		final double d2 = enderman.posZ + ((Reference.random.nextDouble() - 0.5D) * 64.0D);
		return this.teleportTo(d0, d1, d2);
	}

	/**
	 * Teleport the enderman to another entity
	 */
	protected boolean teleportToEntity(Entity p_70816_1_) {
		Vec3d vec3d = new Vec3d(enderman.posX - p_70816_1_.posX, ((enderman.getEntityBoundingBox().minY + (enderman.height / 2.0F)) - p_70816_1_.posY) + p_70816_1_.getEyeHeight(), enderman.posZ - p_70816_1_.posZ);
		vec3d = vec3d.normalize();
		final double d0 = 16.0D;
		final double d1 = (enderman.posX + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.x * d0);
		final double d2 = (enderman.posY + (Reference.random.nextInt(16) - 8)) - (vec3d.y * 16.0D);
		final double d3 = (enderman.posZ + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.z * d0);
		return this.teleportTo(d1, d2, d3);
	}

	private boolean teleportTo(double x, double y, double z) {
		final net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(enderman, x, y, z, 0);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
			return false;
		}
		final boolean flag = enderman.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

		if (flag) {
			enderman.world.playSound((EntityPlayer) null, enderman.prevPosX, enderman.prevPosY, enderman.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, enderman.getSoundCategory(), 1.0F, 1.0F);
			enderman.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}

		return flag;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		final double d0 = this.getTargetDistance();
		player = enderman.world.getNearestAttackablePlayer(enderman.posX, enderman.posY, enderman.posZ, d0, d0, (Function) null, new Predicate<EntityPlayer>() {
			@Override
			public boolean apply(@Nullable EntityPlayer p_apply_1_) {
				return (p_apply_1_ != null) && EnderAiEdit.this.shouldAttackPlayer(p_apply_1_);
			}
		});
		return player != null;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		aggroTime = 5;
		teleportTime = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void resetTask() {
		player = null;
		super.resetTask();
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		if (player != null) {
			if (!this.shouldAttackPlayer(player)) {
				return false;
			} else {
				enderman.faceEntity(player, 10.0F, 10.0F);
				return true;
			}
		} else {
			return (targetEntity != null) && targetEntity.isEntityAlive() ? true : super.shouldContinueExecuting();
		}
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void updateTask() {
		if (player != null) {
			if (--aggroTime <= 0) {
				targetEntity = player;
				player = null;
				super.startExecuting();
			}
		} else {
			if (targetEntity != null) {
				if (this.shouldAttackPlayer(targetEntity)) {
					if (targetEntity.getDistanceSq(enderman) < 16.0D) {
						this.teleportRandomly();
					}

					teleportTime = 0;
				} else if ((targetEntity.getDistanceSq(enderman) > 256.0D) && (teleportTime++ >= 30) && this.teleportToEntity(targetEntity)) {
					teleportTime = 0;
				}
			}

			super.updateTask();
		}
	}
}
