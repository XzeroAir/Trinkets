package xzeroair.trinkets.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
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
import java.util.UUID;

public class EnderMoveAI extends EntityAIBase {
    private static final int MAX_ACTIVE_FOLLOWERS = 3;
    private static final int DYNAMIC_QUEEN_SEARCH_INTERVAL = 20;
    private static final int TELEPORT_COOLDOWN_TICKS = 40;
    private static final double DYNAMIC_QUEEN_RANGE_SQUARED = 256.0D;
    private static final double FOLLOW_START_DISTANCE_SQUARED = 64.0D;
    private static final double FOLLOW_STOP_DISTANCE_SQUARED = 36.0D;
    private static final double TELEPORT_DISTANCE_SQUARED = 576.0D;

    private final EntityEnderman KNIGHT;
    private final PathNavigate navigation;
    @Nullable
    private EntityPlayer QUEEN;
    private int timeToRecalcPath;
    private int teleportCooldown;
    private int nextQueenSearchTick;
    private float oldWaterCost;

    public EnderMoveAI(final EntityEnderman entity) {
        this.KNIGHT = entity;
        this.navigation = this.KNIGHT.getNavigator();
        this.setMutexBits(3);

        if (!(this.navigation instanceof PathNavigateGround) && !(this.navigation instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for Ender Queen following");
        }
    }

    @Override
    public boolean shouldExecute() {
        if (!TrinketsConfig.SERVER.ITEMS.ENDER_CROWN.ABILITIES.ENDER_QUEEN.ENDERMAN_FOLLOW) {
            return false;
        }
        final World world = this.KNIGHT.getEntityWorld();
        if (!world.isBlockLoaded(this.KNIGHT.getPosition()) || !this.KNIGHT.addedToChunk) {
            return false;
        }

        this.QUEEN = this.findQueen(world);
        return (this.QUEEN != null) && (this.KNIGHT.getDistanceSq(this.QUEEN) > FOLLOW_START_DISTANCE_SQUARED) && (this.getActiveFollowerCount(this.QUEEN) < MAX_ACTIVE_FOLLOWERS);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if ((this.QUEEN == null) || !this.isQueenActive(this.QUEEN) || !this.KNIGHT.getEntityData().getBoolean(EnderQueensKnightAI.FOLLOWING_TAG)) {
            return false;
        }
        if (!this.KNIGHT.getEntityData().hasKey(EnderQueensKnightAI.QUEEN_UUID_TAG) && (this.KNIGHT.getDistanceSq(this.QUEEN) > DYNAMIC_QUEEN_RANGE_SQUARED)) {
            return false;
        }
        return this.KNIGHT.getDistanceSq(this.QUEEN) > FOLLOW_STOP_DISTANCE_SQUARED;
    }

    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.teleportCooldown = 0;
        this.oldWaterCost = this.KNIGHT.getPathPriority(PathNodeType.WATER);
        this.KNIGHT.setPathPriority(PathNodeType.WATER, 0.0F);
        this.KNIGHT.getEntityData().setBoolean(EnderQueensKnightAI.FOLLOWING_TAG, true);
    }

    @Override
    public void resetTask() {
        this.QUEEN = null;
        this.navigation.clearPath();
        this.KNIGHT.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
        this.KNIGHT.getEntityData().removeTag(EnderQueensKnightAI.FOLLOWING_TAG);
    }

    @Override
    public void updateTask() {
        if ((this.QUEEN == null) || this.KNIGHT.getLeashed()) {
            return;
        }
        this.KNIGHT.getLookHelper().setLookPositionWithEntity(this.QUEEN, 10.0F, this.KNIGHT.getVerticalFaceSpeed());

        final double distance = this.KNIGHT.getDistanceSq(this.QUEEN);
        if (this.teleportCooldown > 0) {
            this.teleportCooldown--;
        }
        if ((distance > TELEPORT_DISTANCE_SQUARED) && (this.teleportCooldown <= 0)) {
            this.teleportCooldown = TELEPORT_COOLDOWN_TICKS;
            if (this.teleportToEntity(this.QUEEN)) {
                return;
            }
        }

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (distance > FOLLOW_STOP_DISTANCE_SQUARED) {
                this.navigation.tryMoveToEntityLiving(this.QUEEN, 1.0D);
            } else {
                this.navigation.clearPath();
            }
        }
    }

    private int getActiveFollowerCount(EntityPlayer queen) {
        final AxisAlignedBB bounds = queen.getEntityBoundingBox().grow(16.0D, 4.0D, 16.0D);
        final List<EntityEnderman> endermen = this.KNIGHT.world.getEntitiesWithinAABB(EntityEnderman.class, bounds);
        int count = 0;
        for (final EntityEnderman enderman : endermen) {
            if ((enderman != this.KNIGHT) && enderman.getEntityData().getBoolean(EnderQueensKnightAI.FOLLOWING_TAG)) {
                count++;
            }
        }
        return count;
    }

    @Nullable
    private EntityPlayer findQueen(World world) {
        final NBTTagCompound tag = this.KNIGHT.getEntityData();
        if (tag.hasKey(EnderQueensKnightAI.QUEEN_UUID_TAG)) {
            return this.findSummoner(world, tag);
        }

        if (this.isDynamicQueen(this.QUEEN)) {
            return this.QUEEN;
        }
        if (this.KNIGHT.ticksExisted < this.nextQueenSearchTick) {
            return null;
        }
        this.nextQueenSearchTick = this.KNIGHT.ticksExisted + DYNAMIC_QUEEN_SEARCH_INTERVAL + Math.floorMod(this.KNIGHT.getEntityId(), DYNAMIC_QUEEN_SEARCH_INTERVAL);

        final AxisAlignedBB bounds = this.KNIGHT.getEntityBoundingBox().grow(16.0D, 4.0D, 16.0D);
        final List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, bounds);
        EntityPlayer closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (final EntityPlayer player : players) {
            if (this.isDynamicQueen(player)) {
                final double distance = this.KNIGHT.getDistanceSq(player);
                if (distance < closestDistance) {
                    closest = player;
                    closestDistance = distance;
                }
            }
        }
        return closest;
    }

    @Nullable
    private EntityPlayer findSummoner(World world, NBTTagCompound tag) {
        if (!tag.hasKey(EnderQueensKnightAI.QUEEN_UUID_TAG)) {
            return null;
        }
        try {
            final UUID uuid = UUID.fromString(tag.getString(EnderQueensKnightAI.QUEEN_UUID_TAG));
            EntityPlayer player = world.getPlayerEntityByUUID(uuid);
            if (world.getMinecraftServer() != null) {
                player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(uuid);
            }
            if ((player != null) && !TrinketHelper.entityHasAbility(player, TrinketsRegistryNames.ModAbilities.ENDER_QUEEN)) {
                this.KNIGHT.setDead();
                return null;
            }
            return ((player != null) && (player.world == world) && this.isQueenActive(player)) ? player : null;
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private boolean isDynamicQueen(@Nullable EntityPlayer player) {
        return this.isQueenActive(player) && (this.KNIGHT.getDistanceSq(player) <= DYNAMIC_QUEEN_RANGE_SQUARED);
    }

    private boolean isQueenActive(@Nullable EntityPlayer player) {
        return (player != null) && player.isEntityAlive() && TrinketHelper.entityHasAbility(player, TrinketsRegistryNames.ModAbilities.ENDER_QUEEN);
    }

    protected boolean teleportToEntity(Entity target) {
        Vec3d direction = new Vec3d(this.KNIGHT.posX - target.posX, ((this.KNIGHT.getEntityBoundingBox().minY + (this.KNIGHT.height / 2.0F)) - target.posY) + target.getEyeHeight(), this.KNIGHT.posZ - target.posZ).normalize();
        final double distance = 16.0D;
        final double x = (this.KNIGHT.posX + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (direction.x * distance);
        final double y = (this.KNIGHT.posY + (Reference.random.nextInt(16) - 8)) - (direction.y * distance);
        final double z = (this.KNIGHT.posZ + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (direction.z * distance);
        return this.teleportTo(x, y, z);
    }

    private boolean teleportTo(double x, double y, double z) {
        final net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this.KNIGHT, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        final boolean teleported = this.KNIGHT.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (teleported) {
            this.KNIGHT.world.playSound(null, this.KNIGHT.prevPosX, this.KNIGHT.prevPosY, this.KNIGHT.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.KNIGHT.getSoundCategory(), 1.0F, 1.0F);
            this.KNIGHT.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return teleported;
    }
}
