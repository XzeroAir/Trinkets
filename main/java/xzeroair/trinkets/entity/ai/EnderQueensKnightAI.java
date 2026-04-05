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

public class EnderQueensKnightAI extends EntityAITarget {
    private final EntityEnderman KNIGHT;
    private EntityPlayer QUEEN;
    @Nullable
    private EntityLivingBase attacker;
    private int timestamp;
    private final String FOLLOWER_TAG = "isFollower";
    private final String QUEEN_TAG = "QUEEN_UUID";
    private boolean isTargetSet = false;

    public EnderQueensKnightAI(EntityEnderman theDefendingKnightIn) {
        super(theDefendingKnightIn, false);
        this.KNIGHT = theDefendingKnightIn;
        this.setMutexBits(1);
    }

    private void reset() {
        NBTTagCompound tag = this.KNIGHT.getEntityData();
        if (tag.hasKey(this.FOLLOWER_TAG)) {
            tag.removeTag(this.FOLLOWER_TAG);
            if (tag.hasKey(this.QUEEN_TAG)) {
                this.QUEEN = null;
                tag.removeTag(this.QUEEN_TAG);
            }
        }
        this.isTargetSet = false;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        World world = this.KNIGHT.getEntityWorld();
        if (!world.isBlockLoaded(this.KNIGHT.getPosition()) || !this.KNIGHT.addedToChunk) {
            return false;
        }
        NBTTagCompound tag = this.KNIGHT.getEntityData();
        if (tag.hasKey(this.FOLLOWER_TAG)) {
            if (this.QUEEN != null && world.playerEntities.contains(this.QUEEN)) {
                final boolean ability = TrinketHelper.entityHasAbility(this.QUEEN, TrinketsRegistryNames.ModAbilities.ENDER_QUEEN);
                if (ability) {
//                        if (KNIGHT.dimension == QUEEN.dimension && world.isBlockLoaded(QUEEN.getPosition())) {
//                            if (KNIGHT.getDistanceSq(QUEEN.getPosition()) > 32) {
//                                this.teleportToEntity(QUEEN);
//                            }
//                        }
                } else {
                    this.reset();
                }
            } else {
                this.reset();
            }
        } else {
            final AxisAlignedBB bBox = this.KNIGHT.getEntityBoundingBox().grow(16, 4, 16);
            final List<EntityPlayer> entLivList = this.KNIGHT.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, bBox);
            if (!entLivList.isEmpty()) {
                for (final EntityPlayer player : entLivList) {
                    final boolean ability = TrinketHelper.entityHasAbility(player, TrinketsRegistryNames.ModAbilities.ENDER_QUEEN);
                    if (ability) {
                        this.QUEEN = player;
                        tag.setBoolean(this.FOLLOWER_TAG, true);
                        tag.setString(this.QUEEN_TAG, player.getCachedUniqueIdString());
                        break;
                    }
                }
            }
        }

        if (this.QUEEN == null) return false;
        else {
            this.attacker = this.QUEEN.getRevengeTarget();
            final int i = this.QUEEN.getRevengeTimer();
            return (i != this.timestamp) && this.isSuitableTarget(this.attacker, false);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        final EntityLivingBase entitylivingbase = this.QUEEN;

        if (entitylivingbase != null) {
            this.timestamp = entitylivingbase.getRevengeTimer();
        }

        if (this.attacker != null && this.KNIGHT.getDistanceSq(this.attacker.getPosition()) > 6) {
            this.teleportToEntity(this.attacker);
        }

        super.startExecuting();
    }

    /**
     * Teleport the enderman to another entity
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