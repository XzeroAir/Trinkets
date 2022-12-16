package xzeroair.trinkets.entity;

import java.util.Map.Entry;
import java.util.UUID;

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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class AlphaWolf extends EntityWolf {

	private NBTTagCompound storedWolf;

	public AlphaWolf(World world) {
		super(world);
		this.setScaleForAge(false);
		this.setSize(0.6F, 1.2F);
		stepHeight = 1.0F;
	}

	@Override
	protected void initEntityAI() {
		tasks.taskEntries.clear();
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		final boolean canRide = TrinketsConfig.SERVER.races.goblin.rider;
		if (!canRide) {
			this.setDead();
		}
		if (!this.isEntityAlive() || world.isRemote) {
			return;
		}
		if (!this.getPassengers().isEmpty()) {
			final Entity rider = this.getControllingPassenger();
			if (rider instanceof EntityLivingBase) {
				final EntityLivingBase driver = (EntityLivingBase) rider;
				if (!this.isPotionActive(MobEffects.REGENERATION)) {
					this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1, false, false));
				}
				for (Entry<Potion, PotionEffect> effect : driver.getActivePotionMap().entrySet()) {
					final Potion key = effect.getKey();
					if (!key.isBadEffect() && !this.isPotionActive(key)) {
						this.addPotionEffect(effect.getValue());
					}
				}
			}
		}
		final boolean r = this.getPassengers().contains(this.getOwner());
		if (!r && (ticksExisted > 20)) {
			this.setDead();
		}
	}

	@Override
	public void setDead() {
		if (!world.isRemote) {
			try {
				final Entity oldWolf = EntityList.createEntityFromNBT(this.getPreviousWolf(), world);
				if (oldWolf != null) {
					oldWolf.setLocationAndAngles(posX, posY + 1.1F, posZ, rotationYaw, 0F);
					world.spawnEntity(oldWolf);
					//		world.onEntityAdded(oldWolf);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		super.setDead();
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

			if (forward <= 0.0F) {
				forward *= 0.25F;
			}

			this.setSprinting(entitylivingbase.isSprinting());

			//			if (onGround && !this.isJumping()) {
			//				strafe = 0.0F;
			//				forward = 0.0F;
			//			}

			if (this.isJumping() && onGround) {
				//				this.setJumping(true);
				this.jump();
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

	@Override
	protected void jump() {
		super.jump();
		//		motionY = this.getJumpUpwardsMotion();//(float) this.getEntityAttribute(JumpAttribute.Jump).getAttributeValue();
		//		//
		//		if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
		//			motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
		//		}
		//
		//		if (this.isSprinting()) {
		//			final float f = rotationYaw * 0.017453292F;
		//			motionX -= MathHelper.sin(f) * 0.2D;
		//			motionZ += MathHelper.cos(f) * 0.2D;
		//		}
		//
		//		isAirBorne = true;
	}

	//TODO make this work without a target under the crosshair
	// Maybe Make this a ground dash instead of a jumping dash
	public void MountedAttack(EntityPlayer player, double maxDist) {
		final Vec3d pos1 =
				//				new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);//player.getPositionEyes(1F);//
				this.getPositionEyes(1F);
		//		pos1 = pos1.add(pos1.x * 2D, pos1.y * 2D, pos1.z * 2D);
		final Vec3d lookVec = player.getLookVec();
		final Vec3d targetLoc = pos1.add(lookVec.x * maxDist, lookVec.y * maxDist, lookVec.z * maxDist);
		final double d0 = targetLoc.x - posX;
		final double d1 = targetLoc.z - posZ;
		final float f = MathHelper.sqrt((d0 * d0) + (d1 * d1));
		final double m1 = 0.4D;
		final double m2 = 1 - m1;
		final double multiplier = 4D;

		if (f >= 1.0E-4D) {
			final double f1 = ((d0 / f) * multiplier * m1) + (motionX * m2);
			final double f2 = ((d1 / f) * multiplier * m1) + (motionZ * m2);
			motionX += f1;//((d0 / f) * 0.5D * 0.800000011920929D) + (motionX * 0.20000000298023224D);
			motionZ += f2;//((d1 / f) * 0.5D * 0.800000011920929D) + (motionZ * 0.20000000298023224D);
		}
		motionY = 0.42;

		//		final RayTraceResult result = RayTraceHelper.getRaytraceResult(player, pos1, targetLoc, maxDist, (ent) -> {
		//			return !((ent == null) || (ent == this.getControllingPassenger()) || (ent == this));
		//		});
		//
		//		if ((result != null)) {
		//			if ((result.typeOfHit == Type.BLOCK)) {
		//				final BlockPos pos = result.getBlockPos();
		//				if (player.world.getBlockState(pos).getCollisionBoundingBox(player.world, pos) != Block.NULL_AABB) {
		//					maxDist = result.hitVec.distanceTo(pos1);
		//					targetLoc = pos1.add(lookVec.x * maxDist, lookVec.y * maxDist, lookVec.z * maxDist);
		//				}
		//			} else if ((result.typeOfHit == Type.ENTITY)) {
		//				this.attackEntityAsMob(result.entityHit);
		//			}
		//		}
		this.swingArm(EnumHand.MAIN_HAND);

		final RayTraceHelper.Beam beam = new RayTraceHelper.Beam(player.world, player, maxDist, 1D, true);
		RayTraceHelper.rayTraceEntity(beam, target -> {
			if ((target instanceof EntityLivingBase) && (target != this)) {
				this.attackEntityAsMob(target);
				return true;
			}
			return false;
		});

	}

	/*
	 * COMBAT LOGIC
	 */

	/**
	 * Called when this entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		final Entity entity = source.getTrueSource();
		return this.isBeingRidden() && (entity != null) && this.isRidingOrBeingRiddenBy(entity) ? false : super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		// TODO Maybe rework how attacking works?
		return super.attackEntityAsMob(entityIn);
		//		final boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
		//		if (flag) {
		//			this.applyEnchantments(this, entityIn);
		//		}
		//		return flag;
	}

	/*
	 * LOGIC GETTERS AND SETTERS
	 */

	public boolean isJumping() {
		return isJumping;
	}

	/*
	 * DEFAULTS
	 */
	@Override
	protected float getJumpUpwardsMotion() {
		final float jumpmotion = (float) this.getEntityAttribute(JumpAttribute.Jump).getAttributeValue();
		return jumpmotion;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier);
		// TODO Modify This to work naturally with Jump Height and bypass the jump event I currently use

		//        float[] ret = net.minecraftforge.common.ForgeHooks.onLivingFall(this, distance, damageMultiplier);
		//        if (ret == null) return;
		//        distance = ret[0]; damageMultiplier = ret[1];
		//        super.fall(distance, damageMultiplier);
		//        PotionEffect potioneffect = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
		//        float f = potioneffect == null ? 0.0F : (float)(potioneffect.getAmplifier() + 1);
		//        int i = MathHelper.ceil((distance - 3.0F - f) * damageMultiplier);
		//
		//        if (i > 0)
		//        {
		//            this.playSound(this.getFallSound(i), 1.0F, 1.0F);
		//            this.attackEntityFrom(DamageSource.FALL, (float)i);
		//            int j = MathHelper.floor(this.posX);
		//            int k = MathHelper.floor(this.posY - 0.20000000298023224D);
		//            int l = MathHelper.floor(this.posZ);
		//            IBlockState iblockstate = this.world.getBlockState(new BlockPos(j, k, l));
		//
		//            if (iblockstate.getMaterial() != Material.AIR)
		//            {
		//                SoundType soundtype = iblockstate.getBlock().getSoundType(iblockstate, world, new BlockPos(j, k, l), this);
		//                this.playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
		//            }
		//        }
	}

	@Override
	public boolean canBeSteered() {
		return this.getControllingPassenger() instanceof EntityLivingBase;
	}

	@Override
	public Entity getControllingPassenger() {
		return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
	}

	@Override
	public void updatePassenger(Entity passenger) {
		// TODO Maybe do something here?
		super.updatePassenger(passenger);
	}

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}

	@Override
	public double getMountedYOffset() {
		return height;
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
	public boolean canMateWith(EntityAnimal otherAnimal) {
		return false;
	}

	@Override
	public boolean isChild() {
		return false;
	}

	/*
	 * STORAGE
	 */

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
}
