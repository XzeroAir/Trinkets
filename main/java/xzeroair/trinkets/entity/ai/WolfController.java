package xzeroair.trinkets.entity.ai;

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

import java.util.List;
import java.util.Set;

public class WolfController extends EntityAIBase {

    private final EntityTameable tameable;
    private EntityLivingBase owner;
    private final World world;
    private final PathNavigate petPathfinder;
    private final double speed;
    private final Set<Item> temptItem;

    //TODO Remove this?

    public WolfController(EntityTameable tameableIn, double speedIn, Item temptItemIn) {
        this(tameableIn, speedIn, Sets.newHashSet(temptItemIn));
    }

    public WolfController(EntityTameable tameableIn, double speedIn, Set<Item> temptItemIn) {
        this.tameable = tameableIn;
        this.world = tameableIn.world;
        this.petPathfinder = tameableIn.getNavigator();
        this.speed = speedIn;
        this.temptItem = temptItemIn;
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
        EntityLivingBase entitylivingbase = this.tameable.getOwner();

        if (entitylivingbase == null) {
            return false;
        } else if ((entitylivingbase instanceof EntityPlayer) && ((EntityPlayer) entitylivingbase).isSpectator()) {
            return false;
        } else if (this.tameable.isSitting()) {
            return false;
        } else if (!this.tameable.isBeingRidden()) {
            return false;
        } else {
            List<Entity> riders = this.tameable.getPassengers();
            if (riders.isEmpty()) {
                return false;
            }
            this.owner = entitylivingbase;
            //				EntityLivingBase rider = (EntityLivingBase) riders.get(0);
            //				System.out.println("Running " + rider.getName());
            return riders.get(0) == this.owner;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (this.owner != null) {
            return this.owner.getHeldItemOffhand().getItem() == Items.BONE;
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
        this.owner = null;
        this.tameable.getNavigator().clearPath();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        this.tameable.getLookHelper().setLookPositionWithEntity(this.owner, this.tameable.getHorizontalFaceSpeed() + 20, this.tameable.getVerticalFaceSpeed());
        if (this.owner != null) {
            if (this.owner.getHeldItemOffhand().getItem() == Items.BONE) {
                BlockPos vec = this.owner.getPosition().offset(this.owner.getHorizontalFacing());
                this.tameable.getNavigator().tryMoveToXYZ(vec.getX(), vec.getY(), vec.getZ(), this.speed);
            } else {
                this.tameable.getNavigator().clearPath();
            }
        } else {
            this.tameable.getNavigator().clearPath();
        }
    }
}