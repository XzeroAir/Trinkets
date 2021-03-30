package xzeroair.trinkets.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.GenericAttribute;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.AlphaWolfPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class AlphaWolf extends EntityWolf implements IJumpingMount {

	protected static final IAttribute JUMP_STRENGTH = (new RangedAttribute((IAttribute) null, "wolf.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);

	protected boolean isJumping;
	protected float jumpPower;
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
	}

	@Override
	public void onEntityUpdate() {
		if (!this.getPassengers().isEmpty()) {
			Entity driver = this.getControllingPassenger();
			if (driver instanceof EntityLivingBase) {
				driver = this.getControllingPassenger();
				if (!this.isPotionActive(MobEffects.REGENERATION)) {
					this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0, false, false));
				}
				if (((EntityLivingBase) driver).isPotionActive(MobEffects.WATER_BREATHING) || TrinketHelper.AccessoryCheck((EntityLivingBase) driver, ModItems.trinkets.TrinketSea)) {
					this.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 100, 0, false, false));
				}
				if (((EntityLivingBase) driver).isPotionActive(MobEffects.FIRE_RESISTANCE)) {
					this.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 100, 0, false, false));
				}
			}
		}
		super.onEntityUpdate();
		if (this.getPassengers().isEmpty() && (ticksExisted > 20)) {
			if (world.isRemote) {
				NetworkHandler.INSTANCE.sendToServer(new AlphaWolfPacket(this));
			}
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	@Override
	public void setDead() {
		super.setDead();
	}

	public void setTamedBy(EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			super.setTamedBy((EntityPlayer) entity);
		}
	}

	public void storeOldWolf(EntityWolf wolf) {
		NBTTagCompound tag = new NBTTagCompound();
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
		aiSit = new EntityAISit(this);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, aiSit);
		tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
		tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
		tasks.addTask(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
		tasks.addTask(9, new EntityAIBeg(this, 8.0F));
		tasks.addTask(10, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
		targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, AbstractSkeleton.class, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(JUMP_STRENGTH);
		GenericAttribute atkDamage = new GenericAttribute(this, 4, UUID.fromString("76c436ad-d830-48ff-8b3c-fa3bcc1891c2"), 2, SharedMonsterAttributes.ATTACK_DAMAGE);
		atkDamage.addModifier();
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
	 * The speed it takes to move the entityliving's rotationPitch through the
	 * faceEntity method. This is only currently use in wolves.
	 */
	@Override
	public int getVerticalFaceSpeed() {
		return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		Entity entity = source.getTrueSource();
		return this.isBeingRidden() && (entity != null) && this.isRidingOrBeingRiddenBy(entity) ? false : super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

		if (flag) {
			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		return super.processInteract(player, hand);

	}

	@Override
	public boolean canMateWith(EntityAnimal otherAnimal) {
		return false;
	}

	@Override
	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
		if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
			if (target instanceof EntityWolf) {
				EntityWolf entitywolf = (EntityWolf) target;

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
			EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();
			rotationYaw = entitylivingbase.rotationYaw;
			prevRotationYaw = rotationYaw;
			rotationPitch = entitylivingbase.rotationPitch * 0.5F;
			this.setRotation(rotationYaw, rotationPitch);
			renderYawOffset = rotationYaw;
			rotationYawHead = renderYawOffset;
			strafe = entitylivingbase.moveStrafing * 0.5F;
			forward = entitylivingbase.moveForward;

			//			if (forward <= 0.0F) {
			//				forward *= 0.25F;
			//				//                this.gallopTime = 0;
			//			}

			//			if (onGround && (jumpPower == 0.0F)) {
			//				strafe = 0.0F;
			//				forward = 0.0F;
			//			}
			if ((jumpPower > 0.0F) && !this.isJumping() && onGround) {
				motionY = this.getJumpStrength() * jumpPower;
				if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
					motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
				}
				this.setJumping(true);
				isAirBorne = true;

				if (forward > 0.0F) {
					float f = MathHelper.sin(rotationYaw * 0.017453292F);
					float f1 = MathHelper.cos(rotationYaw * 0.017453292F);
					motionX += -0.4F * f * jumpPower;
					motionZ += 0.4F * f1 * jumpPower;
					this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
				}

				jumpPower = 0.0F;
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
				jumpPower = 0.0F;
				this.setJumping(false);
			}

			prevLimbSwingAmount = limbSwingAmount;
			double d1 = posX - prevPosX;
			double d0 = posZ - prevPosZ;
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
	@SideOnly(Side.CLIENT)
	public void setJumpPower(int jumpPowerIn) {
		if (this.isTamed())//this.isHorseSaddled())
		{
			if (jumpPowerIn < 0) {
				jumpPowerIn = 0;
			}

			if (jumpPowerIn >= 90) {
				jumpPower = 1.0F;
			} else {
				jumpPower = 0.4F + ((0.4F * jumpPowerIn) / 90.0F);
			}
		}
	}

	@Override
	public boolean canJump() {
		return true;//this.isHorseSaddled();
	}

	@Override
	public void handleStartJump(int p_184775_1_) {
	}

	@Override
	public void handleStopJump() {
	}

	@Override
	protected void jump() {
		super.jump();
	}

	@Override
	public boolean canBeSteered() {
		return this.getControllingPassenger() instanceof EntityLivingBase;
	}

	@Override
	public void updatePassenger(Entity passenger) {
		//		super.updatePassenger(passenger);
		if (this.isPassenger(passenger)) {
			passenger.setPosition(posX, posY + this.getMountedYOffset() + passenger.getYOffset(), posZ);
		}
	}

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;//super.shouldDismountInWater(rider);
	}

	@Override
	public double getMountedYOffset() {
		return height * (0.75D + 0.3D);
	}

	//	private void Mount(EntityPlayer player) {
	//		if (!world.isRemote) {
	//			EntityProperties prop = Capabilities.getEntityRace(player);
	//			if ((prop != null) && prop.getRaceProperties().isGoblin()) {
	//				player.startRiding(this);
	//			}
	//		}
	//	}

	public void MountedAttack(EntityPlayer player, double maxDist) {
		RayTraceHelper.Beam beam = new RayTraceHelper.Beam(player.world, player, maxDist, 1D, true);
		//		this.jump();
		RayTraceHelper.rayTraceEntity(beam, target -> {
			if ((target instanceof EntityLivingBase) && (target != this)) {
				double d0 = target.posX - posX;
				double d1 = target.posZ - posZ;
				float f = MathHelper.sqrt((d0 * d0) + (d1 * d1));

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
