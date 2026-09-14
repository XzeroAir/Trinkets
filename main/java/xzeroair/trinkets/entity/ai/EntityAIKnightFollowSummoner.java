package xzeroair.trinkets.entity.ai;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.entity.EntitySummonWrapper;

public class EntityAIKnightFollowSummoner extends EntityAIBase {

    private static final double FOLLOW_SPEED = 1.0D;
    private static final float START_DISTANCE = 8.0F;
    private static final float STOP_DISTANCE = 6.0F;

    private final EntitySummonWrapper knight;
    private final PathNavigate navigation;
    private EntityLivingBase summoner;
    private int pathRecalculationTicks;
    private float oldWaterCost;

    public EntityAIKnightFollowSummoner(EntitySummonWrapper knight) {
        this.knight = knight;
        this.navigation = knight.getNavigator();
        this.setMutexBits(3);
        if (!(this.navigation instanceof PathNavigateGround) && !(this.navigation instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported navigator for knight following");
        }
    }

    @Override
    public boolean shouldExecute() {
        if (this.knight.suppressMovement()) {
            return false;
        }
        final EntityLivingBase owner = this.knight.getSummoner();
        if (owner == null || !owner.isEntityAlive() || this.knight.getDistanceSq(owner) < START_DISTANCE * START_DISTANCE) {
            return false;
        }
        this.summoner = owner;
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.knight.suppressMovement() && this.summoner != null && this.summoner.isEntityAlive() && this.knight.getDistanceSq(this.summoner) > STOP_DISTANCE * STOP_DISTANCE;
    }

    @Override
    public void startExecuting() {
        this.pathRecalculationTicks = 0;
        this.oldWaterCost = this.knight.getPathPriority(PathNodeType.WATER);
        this.knight.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void resetTask() {
        this.summoner = null;
        this.navigation.clearPath();
        this.knight.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void updateTask() {
        if (this.summoner == null || this.knight.getLeashed()) {
            return;
        }
        this.knight.getLookHelper().setLookPositionWithEntity(this.summoner, 10.0F, this.knight.getVerticalFaceSpeed());
        if (--this.pathRecalculationTicks <= 0) {
            this.pathRecalculationTicks = 10;
            final double distance = Math.sqrt(this.knight.getDistanceSq(this.summoner));
            final double followSpeed = FOLLOW_SPEED + Math.min(1.0D, Math.max(0.0D, (distance - START_DISTANCE) / START_DISTANCE));
            if (!this.navigation.tryMoveToEntityLiving(this.summoner, followSpeed) || !this.reachesSummoner()) {
                this.knight.pathFailed(this.summoner);
            }
            final float teleportDistance = this.knight.getFollowTeleportDistance(this.summoner);
            if (teleportDistance > 0F && distance >= teleportDistance) {
                this.teleportNearSummoner();
            }
        }
    }

    private boolean reachesSummoner() {
        final Path path = this.navigation.getPath();
        final PathPoint finalPoint = path == null ? null : path.getFinalPathPoint();
        return finalPoint != null && this.summoner.getDistanceSq(finalPoint.x + 0.5D, finalPoint.y, finalPoint.z + 0.5D) <= 1.0D;
    }

    private void teleportNearSummoner() {
        final int x = MathHelper.floor(this.summoner.posX) - 2;
        final int z = MathHelper.floor(this.summoner.posZ) - 2;
        final int y = MathHelper.floor(this.summoner.getEntityBoundingBox().minY);
        for (int xOffset = 0; xOffset <= 4; ++xOffset) {
            for (int zOffset = 0; zOffset <= 4; ++zOffset) {
                if ((xOffset >= 1 && zOffset >= 1 && xOffset <= 3 && zOffset <= 3) || !this.isTeleportFriendlyBlock(x, z, y, xOffset, zOffset)) {
                    continue;
                }
                this.knight.setLocationAndAngles(x + xOffset + 0.5D, y, z + zOffset + 0.5D, this.knight.rotationYaw, this.knight.rotationPitch);
                this.navigation.clearPath();
                return;
            }
        }
        if (this.isTeleportFriendlyPosition(this.summoner.posX, y, this.summoner.posZ)) {
            this.knight.setLocationAndAngles(this.summoner.posX, y, this.summoner.posZ, this.knight.rotationYaw, this.knight.rotationPitch);
            this.navigation.clearPath();
        }
    }

    private boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
        return this.isTeleportFriendlyPosition(x + xOffset + 0.5D, y, z + zOffset + 0.5D);
    }

    private boolean isTeleportFriendlyPosition(double x, double y, double z) {
        final BlockPos position = new BlockPos(x, y - 1D, z);
        final IBlockState state = this.knight.world.getBlockState(position);
        final double halfWidth = this.knight.width * 0.5D;
        final AxisAlignedBB bounds = new AxisAlignedBB(x - halfWidth, y, z - halfWidth, x + halfWidth, y + this.knight.height, z + halfWidth);
        return state.getBlockFaceShape(this.knight.world, position, EnumFacing.DOWN) == BlockFaceShape.SOLID && this.knight.world.getCollisionBoxes(this.knight, bounds)
                                                                                                                                 .isEmpty() && !this.knight.world.containsAnyLiquid(bounds);
    }
}
