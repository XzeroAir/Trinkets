package xzeroair.trinkets.entity.ai;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WolfController extends EntityAIBase {

	private final EntityTameable tameable;
	private EntityLivingBase owner;
	World world;
	private final PathNavigate petPathfinder;
	private final double speed;
	private final Set<Item> temptItem;

	//TODO Remove this?

	public WolfController(EntityTameable tameableIn, double speedIn, Item temptItemIn) {
		this(tameableIn, speedIn, Sets.newHashSet(temptItemIn));
	}

	public WolfController(EntityTameable tameableIn, double speedIn, Set<Item> temptItemIn) {
		tameable = tameableIn;
		world = tameableIn.world;
		petPathfinder = tameableIn.getNavigator();
		speed = speedIn;
		temptItem = temptItemIn;
		this.setMutexBits(3);

		if (!(tameableIn.getNavigator() instanceof PathNavigateGround)) {
			throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = tameable.getOwner();

		if (entitylivingbase == null) {
			return false;
		} else if ((entitylivingbase instanceof EntityPlayer) && ((EntityPlayer) entitylivingbase).isSpectator()) {
			return false;
		} else if (tameable.isSitting()) {
			return false;
		} else if (!tameable.isBeingRidden()) {
			return false;
		} else {
			List<Entity> riders = tameable.getPassengers();
			if (riders.isEmpty()) {
				return false;
			}
			owner = entitylivingbase;
			if ((riders.get(0) != owner)) {
				return false;
				//				EntityLivingBase rider = (EntityLivingBase) riders.get(0);
				//				System.out.println("Running " + rider.getName());
			}
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		if (owner != null) {
			if (owner.getHeldItemOffhand().getItem() == Items.BONE) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {

	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void resetTask() {
		owner = null;
		tameable.getNavigator().clearPath();
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void updateTask() {
		tameable.getLookHelper().setLookPositionWithEntity(owner, tameable.getHorizontalFaceSpeed() + 20, tameable.getVerticalFaceSpeed());
		if (owner != null) {
			if (owner.getHeldItemOffhand().getItem() == Items.BONE) {
				BlockPos vec = owner.getPosition().offset(owner.getHorizontalFacing());
				tameable.getNavigator().tryMoveToXYZ(vec.getX(), vec.getY(), vec.getZ(), speed);
			} else {
				tameable.getNavigator().clearPath();
			}
		} else {
			tameable.getNavigator().clearPath();
		}
	}
}