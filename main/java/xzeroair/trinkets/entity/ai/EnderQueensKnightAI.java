package xzeroair.trinkets.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsRegistryNames;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EnderQueensKnightAI extends EntityAITarget {
    public static final String SUMMONED_TAG = "xat:summoned";
    public static final String QUEEN_UUID_TAG = "xat:ender_queen_owner_uuid";
    public static final String FOLLOWING_TAG = "xat:ender_queen_following";

    private static final int DYNAMIC_QUEEN_SEARCH_INTERVAL = 20;
    private static final double DYNAMIC_QUEEN_RANGE_SQUARED = 256.0D;

    private final EntityEnderman KNIGHT;
    @Nullable
    private EntityPlayer QUEEN;
    @Nullable
    private EntityLivingBase attacker;
    private int revengeTimestamp;
    private int attackTimestamp;
    private int nextQueenSearchTick;
    private boolean targetFromRevenge;

    public EnderQueensKnightAI(EntityEnderman theDefendingKnightIn) {
        super(theDefendingKnightIn, false);
        this.KNIGHT = theDefendingKnightIn;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        final World world = this.KNIGHT.getEntityWorld();
        if (!world.isBlockLoaded(this.KNIGHT.getPosition()) || !this.KNIGHT.addedToChunk) {
            return false;
        }

        this.QUEEN = this.findQueen(world);
        if (this.QUEEN == null) {
            return false;
        }

        final EntityLivingBase revengeTarget = this.QUEEN.getRevengeTarget();
        final int currentRevengeTimestamp = this.QUEEN.getRevengeTimer();
        if ((currentRevengeTimestamp != this.revengeTimestamp) && this.isQueenTarget(revengeTarget)) {
            this.attacker = revengeTarget;
            this.targetFromRevenge = true;
            return true;
        }

        final EntityLivingBase attackedTarget = this.QUEEN.getLastAttackedEntity();
        final int currentAttackTimestamp = this.QUEEN.getLastAttackedEntityTime();
        if ((currentAttackTimestamp != this.attackTimestamp) && this.isQueenTarget(attackedTarget)) {
            this.attacker = attackedTarget;
            this.targetFromRevenge = false;
            return true;
        }

        return false;
    }

    private boolean isQueenTarget(@Nullable EntityLivingBase target) {
        if ((target == null) || (target == this.QUEEN) || !this.isSuitableTarget(target, false)) {
            return false;
        }
        return !(target instanceof EntityPlayer) || this.QUEEN.canAttackPlayer((EntityPlayer) target);
    }

    @Nullable
    private EntityPlayer findQueen(World world) {
        final NBTTagCompound tag = this.KNIGHT.getEntityData();
        if (tag.hasKey(QUEEN_UUID_TAG)) {
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
        if (!tag.hasKey(QUEEN_UUID_TAG)) {
            return null;
        }
        try {
            final UUID uuid = UUID.fromString(tag.getString(QUEEN_UUID_TAG));
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

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        if (this.QUEEN != null) {
            if (this.targetFromRevenge) {
                this.revengeTimestamp = this.QUEEN.getRevengeTimer();
            } else {
                this.attackTimestamp = this.QUEEN.getLastAttackedEntityTime();
            }
        }

        if ((this.attacker != null) && (this.KNIGHT.getDistanceSq(this.attacker.getPosition()) > 6.0D)) {
            this.teleportToEntity(this.attacker);
        }

        super.startExecuting();
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
