package xzeroair.trinkets.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class AlphaWolf extends EntityWolf {

	protected static final IAttribute JUMP_STRENGTH = (new RangedAttribute((IAttribute) null, "wolf.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);

	private EntityLivingBase attackTarget;

	private NBTTagCompound storedWolf;

	public AlphaWolf(World world) {
		super(world);
		this.setSize(0.6F, 1.2F);
		stepHeight = 1.0F;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (!this.isEntityAlive() || world.isRemote) {
			return;
		}
		final boolean canRide = TrinketsConfig.SERVER.races.goblin.rider;
		if (!canRide) {
			this.setDead();
		}
		if (!this.getPassengers().isEmpty()) {
			final Entity rider = this.getControllingPassenger();
			if (rider instanceof EntityLivingBase) {
				final EntityLivingBase driver = (EntityLivingBase) rider;
				if (!this.isPotionActive(MobEffects.REGENERATION)) {
					this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0, false, false));
				}
				if (driver.isPotionActive(MobEffects.WATER_BREATHING) || TrinketHelper.AccessoryCheck(driver, ModItems.trinkets.TrinketSea)) {
					this.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 100, 0, false, false));
				}
				if (driver.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
					this.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 100, 0, false, false));
				}
			}
		}
		final boolean r = this.getPassengers().contains(this.getOwner());
		if (!r && (ticksExisted > 20)) {
			this.setDead();
		}
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	@Override
	public void setDead() {
		if (!world.isRemote) {
			try {
				final Entity oldWolf = EntityList.createEntityFromNBT(this.getPreviousWolf(), world);
				if (oldWolf != null) {
					oldWolf.readFromNBT(this.getPreviousWolf());
					oldWolf.setLocationAndAngles(posX, posY + 1.1F, posZ, rotationYaw, 0F);
					if (oldWolf instanceof EntityLivingBase) {
						((EntityLivingBase) oldWolf).setHealth(this.getHealth());
					}
					world.spawnEntity(oldWolf);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		super.setDead();
	}

	@Override
	protected boolean canDropLoot() {
		return false;//super.canDropLoot();
	}

	@Override
	public void dismountEntity(Entity entityIn) {
		super.dismountEntity(entityIn);
	}

	@Override
	public void dismountRidingEntity() {
		super.dismountRidingEntity();
	}

	public void setTamedBy(EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			super.setTamedBy((EntityPlayer) entity);
		}
	}

	public void storeOldWolf(EntityWolf wolf) {
		final NBTTagCompound tag = new NBTTagCompound();
		wolf.writeToNBT(tag);
		storedWolf = tag;
	}

	public void storeOldWolf(NBTTagCompound tag) {
		storedWolf = tag;
	}

	public NBTTagCompound getPreviousWolf() {
		return storedWolf;
	}

	@Override
	protected void initEntityAI() {
		//		super.initEntityAI();
		tasks.taskEntries.clear();
		//		aiSit = new EntityAISit(this);
		//		tasks.addTask(1, new EntityAISwimming(this));
		//		tasks.addTask(2, aiSit);
		//		tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
		//		tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
		//		tasks.addTask(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		//		tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
		//		tasks.addTask(9, new EntityAIBeg(this, 8.0F));
		//		tasks.addTask(10, new EntityAILookIdle(this));
		//		targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		//		targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		//		targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
		//		targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, AbstractSkeleton.class, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(JUMP_STRENGTH);
		final UpdatingAttribute atkDamage = new UpdatingAttribute(this, UUID.fromString("76c436ad-d830-48ff-8b3c-fa3bcc1891c2"), SharedMonsterAttributes.ATTACK_DAMAGE);
		atkDamage.addModifier(4, 2);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setTag("xat.wolf.stored", storedWolf);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasKey("xat.wolf.stored")) {
			storedWolf = (NBTTagCompound) compound.getTag("xat.wolf.stored");
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return null;//super.getAmbientSound();
	}

	@Override
	public float getEyeHeight() {
		return height * 0.8F;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		final Entity entity = source.getTrueSource();
		return this.isBeingRidden() && (entity != null) && this.isRidingOrBeingRiddenBy(entity) ? false : super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		final boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
		if (flag) {
			this.applyEnchantments(this, entityIn);
		}
		return flag;
	}

	@Override
	public boolean canMateWith(EntityAnimal otherAnimal) {
		return false;
	}

	@Override
	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
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
	public Entity getControllingPassenger() {
		return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
	}

	@Override
	public void travel(float strafe, float vertical, float forward) {
		if (this.isBeingRidden() && this.canBeSteered()) {
			final EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();
			rotationYaw = entitylivingbase.rotationYaw;
			prevRotationYaw = rotationYaw;
			rotationPitch = entitylivingbase.rotationPitch * 0.5F;
			this.setRotation(rotationYaw, rotationPitch);
			renderYawOffset = rotationYaw;
			rotationYawHead = renderYawOffset;
			strafe = entitylivingbase.moveStrafing * 0.5F;
			forward = entitylivingbase.moveForward;
			if (this.isJumping() && onGround) {
				this.jump();
				//				final double jp = this.getJumpStrength();
				//				motionY = jp;//this.getJumpStrength() * jumpPower;
				//				if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
				//					motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
				//				}
				//				isAirBorne = true;
				//
				//				if (forward > 0.0F) {
				//					final float f = MathHelper.sin(rotationYaw * 0.017453292F);
				//					final float f1 = MathHelper.cos(rotationYaw * 0.017453292F);
				//					motionX += -0.4F * f * jp;//jumpPower;
				//					motionZ += 0.4F * f1 * jp;//jumpPower;
				//					this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
				//				}
			}

			jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

			if (this.canPassengerSteer()) {
				this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
				super.travel(strafe, vertical, forward);
			} else if (entitylivingbase instanceof EntityPlayer) {
				motionX = 0.0D;
				motionY = 0.0D;
				motionZ = 0.0D;
			}

			if (onGround) {
				this.setJumping(false);
			}

			prevLimbSwingAmount = limbSwingAmount;
			final double d1 = posX - prevPosX;
			final double d0 = posZ - prevPosZ;
			float f2 = MathHelper.sqrt((d1 * d1) + (d0 * d0)) * 4.0F;
			if (f2 > 1.0F) {
				f2 = 1.0F;
			}

			limbSwingAmount += (f2 - limbSwingAmount) * 0.4F;
			limbSwing += limbSwingAmount;
		} else {
			jumpMovementFactor = 0.02F;
			super.travel(strafe, vertical, forward);
		}
	}

	public boolean isJumping() {
		return isJumping;
	}

	@Override
	public void setJumping(boolean jumping) {
		isJumping = jumping;
	}

	public double getJumpStrength() {
		return this.getEntityAttribute(JUMP_STRENGTH).getAttributeValue();
	}

	@Override
	protected void jump() {
		motionY = this.getJumpStrength();

		if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
			motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
		}
		//				final double jp = this.getJumpStrength();
		//				motionY = jp;//this.getJumpStrength() * jumpPower;
		//				if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
		//					motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
		//				}
		//				this.setJumping(true);
		//				isAirBorne = true;
		//
		//				if (forward > 0.0F) {
		//					final float f = MathHelper.sin(rotationYaw * 0.017453292F);
		//					final float f1 = MathHelper.cos(rotationYaw * 0.017453292F);
		//					motionX += -0.4F * f * jp;//jumpPower;
		//					motionZ += 0.4F * f1 * jp;//jumpPower;
		//		this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
		//				}

		if (this.isSprinting()) {
			final float f = MathHelper.sin(rotationYaw * 0.017453292F);
			final float f1 = MathHelper.cos(rotationYaw * 0.017453292F);
			motionX -= 0.2F * f * this.getJumpStrength();
			motionZ += 0.2F * f1 * this.getJumpStrength();
		}

		isAirBorne = true;
		net.minecraftforge.common.ForgeHooks.onLivingJump(this);
	}

	@Override
	public boolean canBeSteered() {
		return this.getControllingPassenger() instanceof EntityLivingBase;
	}

	@Override
	public void updateRidden() {
		super.updateRidden();
	}

	@Override
	public void updatePassenger(Entity passenger) {
		//		System.out.println("Hmmmmm");
		super.updatePassenger(passenger);
		//		if (this.isPassenger(passenger)) {
		//			double f = -0.1D;
		//
		//			if (this.getPassengers().size() > 1) {
		//				f = this.getPassengers().indexOf(passenger) == 0 ? 0.2D : -0.6D;
		//
		//				if (passenger instanceof EntityAnimal) {
		//					f += 0.2D;
		//				}
		//			}
		//
		//			final Vec3d vec3d = new Vec3d(f, 0.0D, 0.0D).rotateYaw((-rotationYaw * 0.017453292F) - ((float) Math.PI / 2F));
		//			passenger.setPosition(posX + vec3d.x, posY + this.getMountedYOffset() + passenger.getYOffset(), posZ + vec3d.z);
		//
		//			if (!(passenger instanceof EntityPlayer)) {
		//				passenger.setRenderYawOffset(rotationYaw);
		//				final float f2 = MathHelper.wrapDegrees(passenger.rotationYaw - rotationYaw);
		//				final float f1 = MathHelper.clamp(f2, -105.0F, 105.0F);
		//				passenger.prevRotationYaw += f1 - f2;
		//				passenger.rotationYaw += f1 - f2;
		//				passenger.setRotationYawHead(passenger.rotationYaw);
		//
		//				if ((passenger instanceof EntityAnimal) && (this.getPassengers().size() > 1)) {
		//					final int j = (passenger.getEntityId() % 2) == 0 ? 90 : 270;
		//					passenger.setRenderYawOffset(((EntityAnimal) passenger).renderYawOffset + j);
		//					passenger.setRotationYawHead(passenger.getRotationYawHead() + j);
		//				}
		//			}
		//		}
		//		if (this.isPassenger(passenger)) {
		//			passenger.setPosition(posX, posY + this.getMountedYOffset() + passenger.getYOffset(), posZ);
		//		}
	}

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;//super.shouldDismountInWater(rider);
	}

	@Override
	public double getMountedYOffset() {
		return height * (0.75D + 0.3D);
	}

	//TODO make this work without a target under the crosshair
	// Maybe Make this a ground dash instead of a jumping dash
	public void MountedAttack(EntityPlayer player, double maxDist) {
		final RayTraceHelper.Beam beam = new RayTraceHelper.Beam(player.world, player, maxDist, 1D, true);
		//		this.jump();
		RayTraceHelper.rayTraceEntity(beam, target -> {
			if ((target instanceof EntityLivingBase) && (target != this)) {
				final double d0 = target.posX - posX;
				final double d1 = target.posZ - posZ;
				final float f = MathHelper.sqrt((d0 * d0) + (d1 * d1));

				if (f >= 1.0E-4D) {
					motionX += ((d0 / f) * 0.5D * 0.800000011920929D) + (motionX * 0.20000000298023224D);
					motionZ += ((d1 / f) * 0.5D * 0.800000011920929D) + (motionZ * 0.20000000298023224D);
				}
				motionY = 0.4;
				this.jump();
				this.swingArm(EnumHand.MAIN_HAND);
				this.attackEntityAsMob(target);
				return true;
			} else {
				//				motionY *= 0.5F;
				//				if (this.isSprinting()) {
				//					float f = rotationYaw * 0.017453292F;
				//					motionX -= MathHelper.sin(f) * 0.05F;
				//					motionZ += MathHelper.cos(f) * 0.05F;
				//				}
				return false;
			}
		});

	}
}
