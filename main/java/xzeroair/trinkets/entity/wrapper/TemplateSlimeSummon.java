package xzeroair.trinkets.entity.wrapper;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.entity.EntitySummonWrapper;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.traits.abilities.AbilitySummonSlime;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper.ConfigObject;
import xzeroair.trinkets.util.config.abilities.ConfigAbilitySummonSlime;
import xzeroair.trinkets.util.helpers.SoundHelper;

public final class TemplateSlimeSummon implements EntityWrapperBehaviorTemplate {

    public static final TemplateSlimeSummon INSTANCE = new TemplateSlimeSummon();
    private static final ResourceLocation ID = new ResourceLocation(Reference.MODID, "slime_hop");
    private static final float SMALL_SUMMON_WIDTH = 0.51000005F;
    private static final float MEDIUM_SUMMON_WIDTH = 0.9F;

    private TemplateSlimeSummon() {
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void apply(EntitySummonWrapper wrapper) {
        wrapper.setMoveHelper(new SlimeHopMoveHelper(wrapper));
        wrapper.tasks.addTask(4, new SlimeIdleAI(wrapper));
        this.applyGrowth(wrapper, wrapper.getMaxHealth(), wrapper.getHealth());
    }

    @Override
    public void killedEntity(EntitySummonWrapper wrapper, EntityLivingBase target) {
        final float maximumHealth = wrapper.getMaxHealth();
        final float healthCap = this.getConfig().MAXIMUM_SUMMON_HEALTH;
        if (maximumHealth < healthCap) {
            final float gainedHealth = Math.min(this.getConfig().HEALTH_PER_KILL, healthCap - maximumHealth);
            if (gainedHealth > 0F) {
                this.applyGrowth(wrapper, maximumHealth + gainedHealth, wrapper.getHealth() + gainedHealth);
            }
        }
        final int regenerationDuration = this.getConfig().KILL_REGENERATION_DURATION;
        if (regenerationDuration > 0) {
            wrapper.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, regenerationDuration, 0, false, false));
        }
    }

    @Override
    public void tick(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        if (wrapper.isEntityInsideOpaqueBlock()) {
            this.moveOutOfBlocks(wrapper, summoner);
        }
        final EntityLivingBase source = wrapper.getSourceEntity();
        if (source instanceof EntitySlime && ((EntitySlime) source).getSlimeSize() != this.getSlimeSize(wrapper.getMaxHealth())) {
            this.applyGrowth(wrapper, wrapper.getMaxHealth(), wrapper.getHealth());
        } else {
            this.ensureActiveSize(wrapper, this.getSlimeSize(wrapper.getMaxHealth()));
        }
    }

    private void moveOutOfBlocks(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        final int centerX = MathHelper.floor(summoner.posX);
        final int centerZ = MathHelper.floor(summoner.posZ);
        final int y = MathHelper.floor(summoner.getEntityBoundingBox().minY);
        for (int radius = 1; radius <= 2; ++radius) {
            for (int xOffset = -radius; xOffset <= radius; ++xOffset) {
                for (int zOffset = -radius; zOffset <= radius; ++zOffset) {
                    if (Math.abs(xOffset) != radius && Math.abs(zOffset) != radius) {
                        continue;
                    }
                    final double x = centerX + xOffset + 0.5D;
                    final double z = centerZ + zOffset + 0.5D;
                    if (this.isSafePosition(wrapper, x, y, z)) {
                        wrapper.setLocationAndAngles(x, y, z, wrapper.rotationYaw, wrapper.rotationPitch);
                        wrapper.getNavigator().clearPath();
                        return;
                    }
                }
            }
        }
    }

    private boolean isSafePosition(EntitySummonWrapper wrapper, double x, double y, double z) {
        final BlockPos floor = new BlockPos(x, y - 1D, z);
        final IBlockState floorState = wrapper.world.getBlockState(floor);
        final double halfWidth = wrapper.width * 0.5D;
        final AxisAlignedBB bounds = new AxisAlignedBB(x - halfWidth, y, z - halfWidth, x + halfWidth, y + wrapper.height, z + halfWidth);
        return floorState.getBlockFaceShape(wrapper.world, floor, EnumFacing.UP) == BlockFaceShape.SOLID && wrapper.world.getCollisionBoxes(wrapper, bounds)
                                                                                                                         .isEmpty() && !wrapper.world.containsAnyLiquid(bounds);
    }

    private void applyGrowth(EntitySummonWrapper wrapper, float maximumHealth, float health) {
        maximumHealth = Math.min(maximumHealth, this.getConfig().MAXIMUM_SUMMON_HEALTH);
        final int slimeSize = this.getSlimeSize(maximumHealth);
        final EntityLivingBase source = wrapper.getSourceEntity();
        if (source instanceof EntitySlime && ((EntitySlime) source).getSlimeSize() != slimeSize) {
            final ResourceLocation sourceId = wrapper.getSourceId();
            if (sourceId != null) {
                final NBTTagCompound sourceData = wrapper.getSourceData();
                sourceData.setInteger("Size", slimeSize - 1);
                if (wrapper.setSource(sourceId, sourceData)) {
                    wrapper.getNavigator().clearPath();
                }
            }
        }
        this.ensureActiveSize(wrapper, slimeSize);
        wrapper.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maximumHealth);
        wrapper.setHealth(Math.min(health, maximumHealth));
        wrapper.setAttackDamage(Math.max(2.0F, (float) Math.floor(maximumHealth / 5.0F) * 2.0F));
    }

    private void ensureActiveSize(EntitySummonWrapper wrapper, int slimeSize) {
        final float size = slimeSize == 2 ? MEDIUM_SUMMON_WIDTH : SMALL_SUMMON_WIDTH;
        if (wrapper.getActiveWidth() != size || wrapper.getActiveHeight() != size) {
            wrapper.setActiveSize(size, size);
            wrapper.getNavigator().clearPath();
        }
    }

    private int getSlimeSize(float maximumHealth) {
        return maximumHealth >= this.getConfig().MAXIMUM_SUMMON_HEALTH * 0.5F ? 2 : 1;
    }

    private ConfigAbilitySummonSlime getConfig() {
        return TrinketsConfig.SERVER.RACES.SLIME.ABILITIES.SUMMON_SLIME;
    }

    @Override
    public void ownerUnavailable(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        AbilitySummonSlime.storePersistentSummon(wrapper, summoner);
    }

    @Override
    public void ownerChangingDimension(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        AbilitySummonSlime.storeDimensionFollowingSummon(wrapper, summoner);
    }

    @Override
    public void died(EntitySummonWrapper wrapper) {
        AbilitySummonSlime.removePersistentSummon(wrapper);
    }

    @Override
    public boolean processInteract(EntitySummonWrapper wrapper, EntityPlayer player, EnumHand hand) {
        final ItemStack stack = player.getHeldItem(hand);
        try {
            if (!new ConfigObject(this.getConfig().SUMMON_FOOD_ITEM).doesItemMatchEntry(stack)) {
                return false;
            }
        } catch (IllegalArgumentException exception) {
            return false;
        }
        if (wrapper.getHealth() < wrapper.getMaxHealth()) {
            wrapper.heal(this.getConfig().SLIMEBALL_HEALING);
            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return true;
    }

    @Override
    public float getFollowTeleportDistance(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        return this.getConfig().FOLLOW_TELEPORT_DISTANCE;
    }

    @Override
    public boolean isSummonerValid(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        return Capabilities.getEntityProperties(summoner, false, (properties, valid) -> properties.getCurrentRaceCache()
                                                                                                  .compareRace(EntityRaces.slime));
    }

    @Override
    public boolean canPerchOnSummoner(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        return true;
    }

    @Override
    public ShoulderDismountMode getShoulderDismountMode(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        return TrinketsConfig.SERVER.RACES.SLIME.ABILITIES.SUMMON_SLIME.SHOULDER_DISMOUNT_MODE;
    }

    private static class SlimeHopMoveHelper extends EntityMoveHelper {

        private final EntitySummonWrapper wrapper;
        private int jumpDelay;

        private SlimeHopMoveHelper(EntitySummonWrapper wrapper) {
            super(wrapper);
            this.wrapper = wrapper;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action != Action.MOVE_TO) {
                this.wrapper.setMoveForward(0.0F);
                return;
            }

            final double xDistance = this.posX - this.wrapper.posX;
            final double zDistance = this.posZ - this.wrapper.posZ;
            final double distanceSquared = xDistance * xDistance + zDistance * zDistance;
            final float stoppingDistance = Math.max(0.05F, this.wrapper.width * 0.5F);
            if (distanceSquared < stoppingDistance * stoppingDistance) {
                this.action = Action.WAIT;
                this.wrapper.setMoveForward(0.0F);
                return;
            }

            this.action = Action.WAIT;
            final float yaw = (float) (MathHelper.atan2(zDistance, xDistance) * (180.0D / Math.PI)) - 90.0F;
            this.wrapper.rotationYaw = this.limitAngle(this.wrapper.rotationYaw, yaw, 90.0F);
            this.wrapper.rotationYawHead = this.wrapper.rotationYaw;
            this.wrapper.renderYawOffset = this.wrapper.rotationYaw;
            final float speed = (float) (this.speed * this.wrapper.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                                                                  .getAttributeValue());

            if (!this.wrapper.onGround) {
                this.wrapper.setAIMoveSpeed(speed);
                return;
            }

            if (this.jumpDelay-- <= 0) {
                this.jumpDelay = this.wrapper.getRNG().nextInt(10) + 5;
                this.wrapper.setAIMoveSpeed(speed * 1.25F);
                this.wrapper.getJumpHelper().setJumping();
                if (!this.wrapper.world.isRemote) {
                    SoundHelper.playSound(this.wrapper.world, this.wrapper.posX, this.wrapper.posY, this.wrapper.posZ, SoundEvents.ENTITY_SMALL_SLIME_JUMP, this.wrapper.getSoundCategory(), 1.0F, (this.wrapper.getRNG()
                                                                                                                                                                                                                .nextFloat() - this.wrapper.getRNG()
                                                                                                                                                                                                                                           .nextFloat()) * 0.2F + 1.0F);
                }
            } else {
                this.wrapper.setAIMoveSpeed(0.0F);
                this.wrapper.setMoveForward(0.0F);
            }
        }
    }

    private static class SlimeIdleAI extends EntityAIBase {

        private static final double IDLE_RANGE = 6.0D;
        private static final int IDLE_CHANCE = 80;
        private final EntitySummonWrapper wrapper;

        private SlimeIdleAI(EntitySummonWrapper wrapper) {
            this.wrapper = wrapper;
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            final EntityLivingBase summoner = this.wrapper.getSummoner();
            return !this.wrapper.suppressMovement() && this.wrapper.onGround && this.wrapper.getAttackTarget() == null
                    && summoner != null && this.wrapper.getDistanceSq(summoner) <= IDLE_RANGE * IDLE_RANGE
                    && this.wrapper.getRNG().nextInt(IDLE_CHANCE) == 0;
        }

        @Override
        public void startExecuting() {
            final float yaw = this.wrapper.rotationYaw + this.wrapper.getRNG().nextFloat() * 120.0F - 60.0F;
            this.wrapper.rotationYaw = yaw;
            this.wrapper.rotationYawHead = yaw;
            this.wrapper.renderYawOffset = yaw;
            if (this.wrapper.getRNG().nextBoolean()) {
                final double radians = Math.toRadians(yaw);
                final double distance = 0.5D + this.wrapper.getRNG().nextDouble();
                this.wrapper.getMoveHelper().setMoveTo(this.wrapper.posX - Math.sin(radians) * distance, this.wrapper.posY,
                        this.wrapper.posZ + Math.cos(radians) * distance, 0.6D);
            }
        }
    }
}
