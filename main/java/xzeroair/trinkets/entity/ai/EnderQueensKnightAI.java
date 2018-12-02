package xzeroair.trinkets.entity.ai;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class EnderQueensKnightAI extends EntityAITarget
{
	EntityPlayer queen;
	EntityEnderman knight;
	EntityLivingBase attacker;
	private int timestamp;

	public EnderQueensKnightAI(EntityEnderman theDefendingKnightIn)
	{
		super(theDefendingKnightIn, false);
		this.knight = theDefendingKnightIn;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		AxisAlignedBB bBox = knight.getEntityBoundingBox().grow(16, 4, 16);
		List<EntityPlayer> entLivList = knight.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, bBox);
		if(!entLivList.isEmpty()) {
			for(EntityPlayer stuff : entLivList) {
				EntityPlayer player = (EntityPlayer) stuff;
				if(TrinketHelper.baubleCheck(player, ModItems.ender_tiara)) {
					queen = player;
				}
			}
		}

		if (queen == null)
		{
			return false;
		}
		else
		{
			this.attacker = queen.getRevengeTarget();
			int i = queen.getRevengeTimer();
			return (i != this.timestamp) && this.isSuitableTarget(this.attacker, false);
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.attacker);
		EntityLivingBase entitylivingbase = queen;

		if (entitylivingbase != null)
		{
			this.timestamp = entitylivingbase.getRevengeTimer();
		}

		super.startExecuting();
	}
}