package xzeroair.trinkets.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class AlphaWolf extends EntityWolf {

    private static final String STORED_WOLF_TAG = "xat.wolf.stored";
    private static final int RETALIATION_COOLDOWN_TICKS = 40;
    private static final double RETALIATION_RANGE_SQUARED = 25.0D;
    private static final float RETALIATION_DAMAGE = 9.0F;

    private NBTTagCompound storedWolf;
    private int retaliationCooldown;

    public AlphaWolf(World world) {
        super(world);
        this.setScaleForAge(false);
        this.setSize(0.6F, 1.2F);
        this.stepHeight = 1.0F;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }

    @Override
    public ITextComponent getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        } else {
            String s = EntityList.getEntityString(this);

            if (s == null) {
                s = "generic";
            }

            return I18n.translateToLocal(Reference.MODID + ".entity." + s + ".name");
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        final boolean canRide = TrinketsConfig.SERVER.RACES.GOBLIN.ABILITIES.WOLF_RIDER.ENABLED;
        if (!canRide) {
            this.setDead();
        }
        if (!this.isEntityAlive() || this.world.isRemote) {
            return;
        }
        if (this.retaliationCooldown > 0) {
            this.retaliationCooldown--;
        }
        if (!this.getPassengers().isEmpty()) {
            final Entity rider = this.getControllingPassenger();
            if (rider instanceof EntityLivingBase) {
                final EntityLivingBase driver = (EntityLivingBase) rider;
                if (!driver.isPotionActive(MobEffects.STRENGTH)) {
                    driver.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100, 0, false, false));
                }
                if (!driver.isPotionActive(MobEffects.REGENERATION)) {
                    driver.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0, false, false));
                }
                if (!this.isPotionActive(MobEffects.REGENERATION)) {
                    this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1, false, false));
                }
                if (driver.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    this.addPotionEffect(driver.getActivePotionEffect(MobEffects.FIRE_RESISTANCE));
                }
                if (driver.isPotionActive(MobEffects.INVISIBILITY)) {
                    this.addPotionEffect(driver.getActivePotionEffect(MobEffects.INVISIBILITY));
                }
            }
        }
        final boolean r = this.getPassengers().contains(this.getOwner());
        if (!r && (this.ticksExisted > 1)) {
            this.setDead();
        }
    }

    @Override
    public void setDead() {
        if (!this.world.isRemote) {
            try {
                if (this.hasStoredWolf()) {
                    final Entity oldWolf = EntityList.createEntityFromNBT(this.storedWolf, this.world);
                    if (oldWolf != null) {
                        oldWolf.setLocationAndAngles(this.posX, this.posY + 1.1F, this.posZ, this.rotationYaw, 0F);
                        if (this.world.spawnEntity(oldWolf)) {
                            this.storedWolf = null;
                        }
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        super.setDead();
    }

    @Override
    public boolean startRiding(@Nonnull Entity entity) {
        return super.startRiding(entity);
    }

    @Override
    public boolean startRiding(@Nonnull Entity entity, boolean force) {
        return super.startRiding(entity, force);
    }

    @Override
    protected boolean canBeRidden(@Nonnull Entity entity) {
        return super.canBeRidden(entity);
    }

    @Override
    public void dismountEntity(@Nonnull Entity entity) {
        super.dismountEntity(entity);
    }

    @Override
    public void dismountRidingEntity() {
        super.dismountRidingEntity();
    }

    @Override
    protected void removePassenger(@Nonnull Entity passenger) {
        //		System.out.println("Trigger?"); // Runs on the server when logging out due to the forced unmounting
        super.removePassenger(passenger);
    }

    @Override
    public void removePassengers() {
        //		System.out.println("Trigger????");
        super.removePassengers();
    }

    public void setTamedBy(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            super.setTamedBy((EntityPlayer) entity);
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(JumpAttribute.Jump).setBaseValue(0.7D);
        final UpdatingAttribute atkDamage = new UpdatingAttribute(UUID.fromString("76c436ad-d830-48ff-8b3c-fa3bcc1891c2"), SharedMonsterAttributes.ATTACK_DAMAGE).setSavedInNBT(true);
        atkDamage.addModifier(this, 4, 2);
    }

    @Override
    public boolean shouldAttackEntity(@Nonnull EntityLivingBase target, @Nonnull EntityLivingBase owner) {
        if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
            if (target instanceof EntityWolf) {
                final EntityWolf entitywolf = (EntityWolf) target;
                if (entitywolf.isTamed() && (entitywolf.getOwner() == owner)) {
                    return false;
                }
            }
            if ((target instanceof EntityPlayer) && (owner instanceof EntityPlayer) && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) target)) {
                return false;
            } else {
                return !(target instanceof AbstractHorse) || !((AbstractHorse) target).isTame();
            }
        } else {
            return false;
        }
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.isBeingRidden() && this.canBeSteered()) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();
            if (entitylivingbase == null) {
                return;
            }
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            strafe = entitylivingbase.moveStrafing * 0.5F;
            forward = entitylivingbase.moveForward;

            if (forward <= 0.0F) {
                forward *= 0.25F;
            }

            this.setSprinting(entitylivingbase.isSprinting());

            //			if (onGround && !this.isJumping()) {
            //				strafe = 0.0F;
            //				forward = 0.0F;
            //			}

            if (this.isJumping() && this.onGround) {
                //				this.setJumping(true);
                this.jump();
            }

            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.travel(strafe, vertical, forward);
            } else if (entitylivingbase instanceof EntityPlayer) {
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            if (this.onGround) {
                this.setJumping(false);
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            final double d1 = this.posX - this.prevPosX;
            final double d0 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt((d1 * d1) + (d0 * d0)) * 4.0F;
            if (f2 > 1.0F) {
                f2 = 1.0F;
            }

            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        } else {
            this.jumpMovementFactor = 0.02F;
            super.travel(strafe, vertical, forward);
        }
    }

    public void MountedAttack(EntityPlayer player, double maxDist) {
        final List<Entity> targets = new ArrayList<>();
        final RayTraceHelper.Beam beam = new RayTraceHelper.Beam(this.world, this, this.getPositionEyes(1F), player.getLookVec(), maxDist, true);
        RayTraceHelper.rayTraceEntity(beam, target -> {
            if ((target instanceof EntityLivingBase) && (target != player) && this.shouldAttackEntity((EntityLivingBase) target, player)) {
                targets.add(target);
            }
            return true;
        });

        targets.sort(Comparator.comparingDouble(target -> target.getPositionVector().subtract(beam.getStart()).dotProduct(beam.getLookVec())));

        boolean hitTarget = false;
        final float baseDamage = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        for (int index = 0; index < targets.size(); index++) {
            final float damageMultiplier = index == 0 ? 1.0F : index == 1 ? 0.5F : 0.2F;
            hitTarget = this.attackEntityAsMob(targets.get(index), player, baseDamage * damageMultiplier) || hitTarget;
        }

        final Vec3d lookVec = player.getLookVec();
        final Vec3d targetLoc = this.getPositionEyes(1F).add(lookVec.x * maxDist, lookVec.y * maxDist, lookVec.z * maxDist);
        final double d0 = targetLoc.x - this.posX;
        final double d1 = targetLoc.z - this.posZ;
        final float f = MathHelper.sqrt((d0 * d0) + (d1 * d1));
        final double m1 = 0.4D;
        final double m2 = 1 - m1;
        final double multiplier = 4D;

        if (f >= 1.0E-4D) {
            final double f1 = ((d0 / f) * multiplier * m1) + (this.motionX * m2);
            final double f2 = ((d1 / f) * multiplier * m1) + (this.motionZ * m2);
            this.motionX += f1;//((d0 / f) * 0.5D * 0.800000011920929D) + (motionX * 0.20000000298023224D);
            this.motionZ += f2;//((d1 / f) * 0.5D * 0.800000011920929D) + (motionZ * 0.20000000298023224D);
        }
        if (hitTarget) {
            this.motionY = 0.42D;
        }
        this.swingArm(EnumHand.MAIN_HAND);

    }

    /*
     * COMBAT LOGIC
     */

    /**
     * Called when this entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        final Entity attacker = source.getTrueSource();
        final boolean damaged = (!this.isBeingRidden() || (attacker == null) || !this.isRidingOrBeingRiddenBy(attacker)) && super.attackEntityFrom(source, amount);
        if (damaged && !this.world.isRemote) {
            this.retaliate(attacker);
        }
        return damaged;
    }

    private void retaliate(Entity attacker) {
        if ((this.retaliationCooldown > 0) || !(attacker instanceof EntityLivingBase)) {
            return;
        }
        final EntityLivingBase target = (EntityLivingBase) attacker;
        final EntityLivingBase owner = this.getOwner();
        if ((owner == null) || !this.shouldAttackEntity(target, owner) || !this.canEntityBeSeen(target) || (this.getDistanceSq(target) > RETALIATION_RANGE_SQUARED)) {
            return;
        }

        this.retaliationCooldown = RETALIATION_COOLDOWN_TICKS;
        this.lungeTowards(target, 1.0D, 0.32D);
        this.attackEntityAsMob(target, RETALIATION_DAMAGE);
    }

    private void lungeTowards(Entity target, double strength, double upwardMotion) {
        final double deltaX = target.posX - this.posX;
        final double deltaZ = target.posZ - this.posZ;
        final float distance = MathHelper.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));
        if (distance >= 1.0E-4D) {
            this.motionX += (deltaX / distance) * strength;
            this.motionZ += (deltaZ / distance) * strength;
        }
        if (this.onGround) {
            this.motionY = upwardMotion;
        }
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
        return super.attackEntityAsMob(entityIn);
    }

    protected boolean attackEntityAsMob(@Nonnull Entity entityIn, float damage) {
        final boolean hit = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
        if (hit) {
            this.applyEnchantments(this, entityIn);
        }
        return hit;
    }

    protected boolean attackEntityAsMob(@Nonnull Entity entityIn, @Nonnull EntityLivingBase indirectAttacker, float damage) {
        final boolean hit = entityIn.attackEntityFrom(new EntityDamageSourceIndirect("mob", this, indirectAttacker), damage);
        if (hit) {
            this.applyEnchantments(this, entityIn);
        }
        return hit;
    }

    /*
     * LOGIC GETTERS AND SETTERS
     */

    public boolean isJumping() {
        return this.isJumping;
    }

    /*
     * DEFAULTS
     */
    @Override
    protected float getJumpUpwardsMotion() {
        return (float) this.getEntityAttribute(JumpAttribute.Jump).getAttributeValue();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof EntityLivingBase;
    }

    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        super.updatePassenger(passenger);
    }

    @Override
    public boolean shouldDismountInWater(@Nonnull Entity rider) {
        return false;
    }

    @Override
    public double getMountedYOffset() {
        return this.height;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;//super.getAmbientSound();
    }

    @Override
    protected boolean canDropLoot() {
        return false;
    }

    @Override
    public boolean canMateWith(@Nonnull EntityAnimal otherAnimal) {
        return false;
    }

    @Override
    public boolean isChild() {
        return false;
    }

    /*
     * STORAGE
     */

    public void storeOldWolf(@Nonnull EntityWolf wolf) {
        final NBTTagCompound tag = new NBTTagCompound();
        wolf.writeToNBT(tag);
        this.storedWolf = tag;
    }

    public void storeOldWolf(NBTTagCompound tag) {
        this.storedWolf = tag;
    }

    public boolean hasStoredWolf() {
        return (this.storedWolf != null) && this.storedWolf.hasKey("id");
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.hasStoredWolf()) {
            compound.setTag(STORED_WOLF_TAG, this.storedWolf);
        }
        compound.setBoolean("xat:summoned", true);
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.storedWolf = compound.hasKey(STORED_WOLF_TAG, 10) ? compound.getCompoundTag(STORED_WOLF_TAG) : null;
    }
}
