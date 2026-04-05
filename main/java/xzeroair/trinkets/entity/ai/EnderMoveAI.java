package xzeroair.trinkets.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;

import javax.annotation.Nullable;
import java.util.List;

public class EnderMoveAI extends EntityAIFollow {

    private final String FOLLOWER_TAG = "isFollower";
    private final String QUEEN_TAG = "QUEEN_UUID";

    private final Predicate<EntityLiving> followPredicate;
    private final double speedModifier;
    private final EntityLiving KNIGHT;
    private final PathNavigate navigation;
    private final float stopDistance;
    private final float areaSize;
    private EntityPlayer QUEEN;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public EnderMoveAI(final EntityEnderman entity) {
        super(entity, 1, 6F, 16);
        this.KNIGHT = entity;
        this.followPredicate = (@Nullable EntityLiving following) -> (following != null) && (this.KNIGHT.getClass() != following.getClass());
        this.speedModifier = 1;
        this.navigation = this.KNIGHT.getNavigator();
        this.stopDistance = 6;
        this.areaSize = 16;
        this.setMutexBits(3);

        if (!(this.KNIGHT.getNavigator() instanceof PathNavigateGround) && !(this.KNIGHT.getNavigator() instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (!TrinketsConfig.SERVER.ITEMS.ENDER_CROWN.ABILITIES.ENDER_QUEEN.ENDERMAN_FOLLOW) {
            return false;
        }
        World world = this.KNIGHT.getEntityWorld();
        if (!world.isBlockLoaded(this.KNIGHT.getPosition()) || !this.KNIGHT.addedToChunk) {
            return false;
        }
        NBTTagCompound tag = this.KNIGHT.getEntityData();
        if (tag.hasKey(this.FOLLOWER_TAG)) {
            final AxisAlignedBB bBox = this.KNIGHT.getEntityBoundingBox();
//            if (tag.getBoolean(FOLLOWER_TAG)) {
//                if (QUEEN != null && world.playerEntities.contains(QUEEN)) {
//                    final boolean ability = TrinketHelper.entityHasAbility(QUEEN, ConstantsRegistryIds.ModAbilities.ENDER_QUEEN);
//                    if (ability) {
//                        if (KNIGHT.dimension == QUEEN.dimension && world.isBlockLoaded(QUEEN.getPosition())) {
//                            if (KNIGHT.getDistanceSq(QUEEN.getPosition()) > 16) {
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }
            final List<EntityLivingBase> list = this.KNIGHT.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, bBox.grow(16, 4, 16));
            if (!list.isEmpty()) {
                for (final EntityLivingBase entityliving : list) {
                    if (entityliving instanceof EntityPlayer) {
                        final boolean ability = TrinketHelper.entityHasAbility(entityliving, TrinketsRegistryNames.ModAbilities.ENDER_QUEEN);
                        if (!entityliving.isInvisible() && (ability)) {
                            this.QUEEN = (EntityPlayer) entityliving;
                            int count = 0;
                            final List<EntityEnderman> allies = this.KNIGHT.getEntityWorld().getEntitiesWithinAABB(EntityEnderman.class, entityliving.getEntityBoundingBox().grow(8, 4, 8));
                            for (final EntityEnderman ally : allies) {
                                count++;
                            }
                            if ((count < 4)) {
                                return true;
                            }
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
        if (this.QUEEN != null) {
            final List<EntityEnderman> allies = this.KNIGHT.getEntityWorld().getEntitiesWithinAABB(EntityEnderman.class, this.QUEEN.getEntityBoundingBox().grow(6, 4, 6));
            for (final EntityEnderman ally : allies) {
                count++;
            }
        }
        return this.navigation.noPath() || ((this.QUEEN != null) && (count < 4) && !this.navigation.noPath() && ((this.KNIGHT.getDistanceSq(this.QUEEN) > (this.stopDistance * this.stopDistance))));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.KNIGHT.getPathPriority(PathNodeType.WATER);
        this.KNIGHT.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    @Override
    public void resetTask() {
        this.QUEEN = null;
        this.navigation.clearPath();
        this.KNIGHT.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        if ((this.QUEEN != null) && !this.KNIGHT.getLeashed()) {
            this.KNIGHT.getLookHelper().setLookPositionWithEntity(this.QUEEN, 10.0F, this.KNIGHT.getVerticalFaceSpeed());

            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                final double d0 = this.KNIGHT.posX - this.QUEEN.posX;
                final double d1 = this.KNIGHT.posY - this.QUEEN.posY;
                final double d2 = this.KNIGHT.posZ - this.QUEEN.posZ;
                final double d3 = (d0 * d0) + (d1 * d1) + (d2 * d2);

                if (d3 > (this.stopDistance * this.stopDistance)) {
                    this.navigation.tryMoveToEntityLiving(this.QUEEN, this.speedModifier);
                } else {
                    this.navigation.clearPath();

                    if (d3 <= this.stopDistance) {
                        final double d4 = this.QUEEN.posX - this.KNIGHT.posX;
                        final double d5 = this.QUEEN.posZ - this.KNIGHT.posZ;
                        this.navigation.tryMoveToXYZ(this.KNIGHT.posX - d4, this.KNIGHT.posY, this.KNIGHT.posZ - d5, this.speedModifier);
                    }
                }
            }
        }
    }

    /**
     * Teleport the KNIGHT to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_) {
        Vec3d vec3d = new Vec3d(this.KNIGHT.posX - p_70816_1_.posX, ((this.KNIGHT.getEntityBoundingBox().minY + (this.KNIGHT.height / 2.0F)) - p_70816_1_.posY) + p_70816_1_.getEyeHeight(), this.KNIGHT.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        final double d0 = 16.0D;
        final double d1 = (this.KNIGHT.posX + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.x * d0);
        final double d2 = (this.KNIGHT.posY + (Reference.random.nextInt(16) - 8)) - (vec3d.y * 16.0D);
        final double d3 = (this.KNIGHT.posZ + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.z * d0);
        return this.teleportTo(d1, d2, d3);
    }

    private boolean teleportTo(double x, double y, double z) {
        final net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this.KNIGHT, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        final boolean flag = this.KNIGHT.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag) {
            this.KNIGHT.world.playSound(null, this.KNIGHT.prevPosX, this.KNIGHT.prevPosY, this.KNIGHT.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.KNIGHT.getSoundCategory(), 1.0F, 1.0F);
            this.KNIGHT.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }
}