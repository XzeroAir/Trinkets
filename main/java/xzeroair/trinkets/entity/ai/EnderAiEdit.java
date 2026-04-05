package xzeroair.trinkets.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsRegistryNames;

import javax.annotation.Nullable;

public class EnderAiEdit extends EntityAINearestAttackableTarget<EntityPlayer> {

    private final EntityEnderman ENDER_MAN;
    private final String FOLLOWER_TAG = "isFollower";
    private final String QUEEN_TAG = "QUEEN_UUID";
    @Nullable
    private EntityPlayer player;
    private int aggroTime;
    private int teleportTime;

    public EnderAiEdit(EntityEnderman enderman) {
        super(enderman, EntityPlayer.class, false);
        this.ENDER_MAN = enderman;
    }

    private boolean shouldAttackPlayer(EntityPlayer player) {
        final ItemStack itemstack = TrinketHelper.getHead(player);
        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            return false;
        }
        NBTTagCompound tag = this.ENDER_MAN.getEntityData();
        if ((itemstack.getItem() == ModItems.trinkets.TrinketEnderTiara) || (tag.hasKey(this.QUEEN_TAG) && tag.getString(this.QUEEN_TAG).compareTo(player.getCachedUniqueIdString()) == 0) || (TrinketHelper.entityHasAbility(player, TrinketsRegistryNames.ModAbilities.ENDER_QUEEN)) || (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDragonsEye, ModItems.trinkets.TrinketEnderTiara)) || TrinketHelper.isEntityRace(player, EntityRaces.dragon)) {
            return false;
        } else {
            final Vec3d vec3d = player.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(this.ENDER_MAN.posX - player.posX, (this.ENDER_MAN.getEntityBoundingBox().minY + this.ENDER_MAN.getEyeHeight()) - (player.posY + player.getEyeHeight()), this.ENDER_MAN.posZ - player.posZ);
            final double d0 = vec3d1.length();
            vec3d1 = vec3d1.normalize();
            final double d1 = vec3d.dotProduct(vec3d1);
            return d1 > (1.0D - (0.025D / d0)) && player.canEntityBeSeen(this.ENDER_MAN);
        }
    }

    protected boolean teleportRandomly() {
        final double d0 = this.ENDER_MAN.posX + ((Reference.random.nextDouble() - 0.5D) * 64.0D);
        final double d1 = this.ENDER_MAN.posY + (Reference.random.nextInt(64) - 32);
        final double d2 = this.ENDER_MAN.posZ + ((Reference.random.nextDouble() - 0.5D) * 64.0D);
        return this.teleportTo(d0, d1, d2);
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_) {
        Vec3d vec3d = new Vec3d(this.ENDER_MAN.posX - p_70816_1_.posX, ((this.ENDER_MAN.getEntityBoundingBox().minY + (this.ENDER_MAN.height / 2.0F)) - p_70816_1_.posY) + p_70816_1_.getEyeHeight(), this.ENDER_MAN.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        final double d0 = 16.0D;
        final double d1 = (this.ENDER_MAN.posX + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.x * d0);
        final double d2 = (this.ENDER_MAN.posY + (Reference.random.nextInt(16) - 8)) - (vec3d.y * 16.0D);
        final double d3 = (this.ENDER_MAN.posZ + ((Reference.random.nextDouble() - 0.5D) * 8.0D)) - (vec3d.z * d0);
        return this.teleportTo(d1, d2, d3);
    }

    private boolean teleportTo(double x, double y, double z) {
        final net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this.ENDER_MAN, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        final boolean flag = this.ENDER_MAN.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag) {
            this.ENDER_MAN.world.playSound(null, this.ENDER_MAN.prevPosX, this.ENDER_MAN.prevPosY, this.ENDER_MAN.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.ENDER_MAN.getSoundCategory(), 1.0F, 1.0F);
            this.ENDER_MAN.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        final double d0 = this.getTargetDistance();
        this.player = this.ENDER_MAN.world.getNearestAttackablePlayer(this.ENDER_MAN.posX, this.ENDER_MAN.posY, this.ENDER_MAN.posZ, d0, d0, null, (@Nullable EntityPlayer p_apply_1_) -> (p_apply_1_ != null) && EnderAiEdit.this.shouldAttackPlayer(p_apply_1_));
        return this.player != null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.aggroTime = 5;
        this.teleportTime = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    @Override
    public void resetTask() {
        this.player = null;
        super.resetTask();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        if (this.player != null) {
            if (!this.shouldAttackPlayer(this.player)) {
                return false;
            } else {
                this.ENDER_MAN.faceEntity(this.player, 10.0F, 10.0F);
                return true;
            }
        } else {
            return (this.targetEntity != null) && this.targetEntity.isEntityAlive() || super.shouldContinueExecuting();
        }
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        if (this.player != null) {
            if (--this.aggroTime <= 0) {
                this.targetEntity = this.player;
                this.player = null;
                super.startExecuting();
            }
        } else {
            if (this.targetEntity != null) {
                if (this.shouldAttackPlayer(this.targetEntity)) {
                    if (this.targetEntity.getDistanceSq(this.ENDER_MAN) < 16.0D) {
                        this.teleportRandomly();
                    }

                    this.teleportTime = 0;
                } else if ((this.targetEntity.getDistanceSq(this.ENDER_MAN) > 256.0D) && (this.teleportTime++ >= 30) && this.teleportToEntity(this.targetEntity)) {
                    this.teleportTime = 0;
                }
            }

            super.updateTask();
        }
    }
}
