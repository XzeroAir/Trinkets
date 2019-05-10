package xzeroair.trinkets.entity.ai;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class EnderAiEdit extends EntityAINearestAttackableTarget<EntityPlayer> {

	private final EntityEnderman enderman;
	/** The player */
	private EntityPlayer player;
	private int aggroTime;
	private int teleportTime;

	public EnderAiEdit(EntityEnderman enderman)
	{
		super(enderman, EntityPlayer.class, false);
		this.enderman = enderman;
	}

	private boolean shouldAttackPlayer(EntityPlayer player)
	{
		final ItemStack itemstack = player.inventory.armorInventory.get(3);
		final boolean eye = TrinketHelper.baubleCheck(player, ModItems.dragons_eye);
		final boolean tiara = TrinketHelper.baubleCheck(player, ModItems.ender_tiara);
		if((tiara == true) || (eye == true) || (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN))){
			return false;
		}
		else
		{
			final Vec3d vec3d = player.getLook(1.0F).normalize();
			Vec3d vec3d1 = new Vec3d(this.enderman.posX - player.posX, (this.enderman.getEntityBoundingBox().minY + this.enderman.getEyeHeight()) - (player.posY + player.getEyeHeight()), this.enderman.posZ - player.posZ);
			final double d0 = vec3d1.length();
			vec3d1 = vec3d1.normalize();
			final double d1 = vec3d.dotProduct(vec3d1);
			return d1 > (1.0D - (0.025D / d0)) ? player.canEntityBeSeen(this.enderman) : false;
		}
	}

	protected boolean teleportRandomly()
	{
		final double d0 = this.enderman.posX + ((Reference.random.nextDouble() - 0.5D) * 64.0D);
		final double d1 = this.enderman.posY + (Reference.random.nextInt(64) - 32);
		final double d2 = this.enderman.posZ + ((Reference.random.nextDouble() - 0.5D) * 64.0D);
		return teleportTo(d0, d1, d2);
	}

	/**
	 * Teleport the enderman to another entity
	 */
	protected boolean teleportToEntity(Entity p_70816_1_)
	{
		Vec3d vec3d = new Vec3d(this.enderman.posX - p_70816_1_.posX, ((this.enderman.getEntityBoundingBox().minY + (this.enderman.height / 2.0F)) - p_70816_1_.posY) + p_70816_1_.getEyeHeight(), this.enderman.posZ - p_70816_1_.posZ);
		vec3d = vec3d.normalize();
		final double d0 = 16.0D;
		final double d1 = (this.enderman.posX + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.x * d0);
		final double d2 = (this.enderman.posY + (Reference.random.nextInt(16) - 8)) - (vec3d.y * 16.0D);
		final double d3 = (this.enderman.posZ + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.z * d0);
		return teleportTo(d1, d2, d3);
	}

	private boolean teleportTo(double x, double y, double z)
	{
		final net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this.enderman, x, y, z, 0);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
			return false;
		}
		final boolean flag = this.enderman.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

		if (flag)
		{
			this.enderman.world.playSound((EntityPlayer)null, this.enderman.prevPosX, this.enderman.prevPosY, this.enderman.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.enderman.getSoundCategory(), 1.0F, 1.0F);
			this.enderman.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}

		return flag;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		final double d0 = getTargetDistance();
		this.player = this.enderman.world.getNearestAttackablePlayer(this.enderman.posX, this.enderman.posY, this.enderman.posZ, d0, d0, (Function)null, new Predicate<EntityPlayer>()
		{
			@Override
			public boolean apply(@Nullable EntityPlayer p_apply_1_)
			{
				return (p_apply_1_ != null) && EnderAiEdit.this.shouldAttackPlayer(p_apply_1_);
			}
		});
		return this.player != null;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.aggroTime = 5;
		this.teleportTime = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	@Override
	public void resetTask()
	{
		this.player = null;
		super.resetTask();
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting()
	{
		if (this.player != null)
		{
			if (!shouldAttackPlayer(this.player))
			{
				return false;
			}
			else
			{
				this.enderman.faceEntity(this.player, 10.0F, 10.0F);
				return true;
			}
		}
		else
		{
			return (this.targetEntity != null) && this.targetEntity.isEntityAlive() ? true : super.shouldContinueExecuting();
		}
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void updateTask()
	{
		if (this.player != null)
		{
			if (--this.aggroTime <= 0)
			{
				this.targetEntity = this.player;
				this.player = null;
				super.startExecuting();
			}
		}
		else
		{
			if (this.targetEntity != null)
			{
				if (shouldAttackPlayer(this.targetEntity))
				{
					if (this.targetEntity.getDistanceSq(this.enderman) < 16.0D)
					{
						teleportRandomly();
					}

					this.teleportTime = 0;
				}
				else if ((this.targetEntity.getDistanceSq(this.enderman) > 256.0D) && (this.teleportTime++ >= 30) && teleportToEntity(this.targetEntity))
				{
					this.teleportTime = 0;
				}
			}

			super.updateTask();
		}
	}
}
