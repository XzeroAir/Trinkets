package xzeroair.trinkets.entity.ai;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;

public class EnderQueensKnightAI extends EntityAITarget {
	EntityPlayer queen;
	EntityEnderman knight;
	EntityLivingBase attacker;
	private int timestamp;

	public EnderQueensKnightAI(EntityEnderman theDefendingKnightIn) {
		super(theDefendingKnightIn, false);
		knight = theDefendingKnightIn;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		final AxisAlignedBB bBox = knight.getEntityBoundingBox().grow(16, 4, 16);
		final List<EntityPlayer> entLivList = knight.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, bBox);
		if (!entLivList.isEmpty()) {
			for (final EntityPlayer stuff : entLivList) {
				final EntityPlayer player = stuff;
				final boolean ability = TrinketHelper.entityHasAbility(Abilities.enderQueen, player);
				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara) || ability) {
					queen = player;
				} else {
					queen = null;
				}
			}
		}

		if (queen == null) {
			return false;
		} else {
			attacker = queen.getRevengeTarget();
			final int i = queen.getRevengeTimer();
			return (i != timestamp) && this.isSuitableTarget(attacker, false);
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(attacker);
		final EntityLivingBase entitylivingbase = queen;

		if (entitylivingbase != null) {
			timestamp = entitylivingbase.getRevengeTimer();
		}

		super.startExecuting();
	}
}