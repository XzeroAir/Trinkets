package xzeroair.trinkets.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class MovingThrownProjectile extends EntityFireball implements IProjectile {

	private ItemStack stack;
	private HitResult hitResult;
	private int xTile;
	private int yTile;
	private int zTile;
	private Block inTile;
	private int inData;
	protected boolean inGround;
	protected int timeInGround;
	private int ticksInGround;
	private int ticksInAir;
	private double damage;
	private int knockbackStrength;
	public int throwableShake;
	public Entity ignoreEntity;
	private int ignoreTime;
	int color;

	private boolean ignoreBlocks = false;

	//TODO Rewrite this

	public MovingThrownProjectile(World worldIn) {
		super(worldIn);
		color = 12582912;
	}

	public MovingThrownProjectile(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ, int color) {
		//		this(worldIn, shooter.posX, (shooter.posY + shooter.getEyeHeight()) - 0.10000000149011612D, shooter.posZ);
		super(worldIn, shooter, accelX, accelY, accelZ);
		//		isImmuneToFire = true;
		this.color = color;
		final float f = -MathHelper.sin(shooter.rotationYaw * 0.017453292F) * MathHelper.cos(shooter.rotationPitch * 0.017453292F);
		final float f1 = -MathHelper.sin((shooter.rotationPitch + 0) * 0.017453292F);
		final float f2 = MathHelper.cos(shooter.rotationYaw * 0.017453292F) * MathHelper.cos(shooter.rotationPitch * 0.017453292F);
		damage = TrinketsConfig.SERVER.races.dragon.breath_damage;
		this.setSize(0.5F, 0.5F);
		//		final float f = MathHelper.sqrt((x * x) + (y * y) + (z * z));
		final double d0 = MathHelper.sqrt((f * f) + (f1 * f1) + (f2 * f2));
		//		//MathHelper.sqrt((accelX * accelX) + (accelY * accelY) + (accelZ * accelZ));
		accelerationX = (accelX / d0) * (0.1D * (this.isFlying(shooter) ? 4 : 1));
		accelerationY = (accelY / d0) * (0.1D * (this.isFlying(shooter) ? 4 : 1));
		accelerationZ = (accelZ / d0) * (0.1D * (this.isFlying(shooter) ? 4 : 1));
		//		shootingEntity = shooter;
	}

	private boolean isFlying(EntityLivingBase entity) {
		if ((entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isFlying) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets throwable heading based on an entity that's throwing it
	 */
	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
		final float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		final float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
		final float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		//		final float f = rotationPitchIn;
		//		final float f1 = rotationYawIn;
		//		final float f2 = pitchOffset;
		this.shoot(f, f1, f2, velocity, inaccuracy);
		motionX += entityThrower.motionX;
		motionZ += entityThrower.motionZ;

		if (!entityThrower.onGround) {
			motionY += entityThrower.motionY;
		}
	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
	 * direction.
	 */
	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		final float f = MathHelper.sqrt((x * x) + (y * y) + (z * z));
		x = x / f;
		y = y / f;
		z = z / f;
		x = x + (rand.nextGaussian() * 0.007499999832361937D * inaccuracy);
		y = y + (rand.nextGaussian() * 0.007499999832361937D * inaccuracy);
		z = z + (rand.nextGaussian() * 0.007499999832361937D * inaccuracy);
		x = x * velocity;
		y = y * velocity;
		z = z * velocity;
		motionX = x;
		motionY = y;
		motionZ = z;
		final float f1 = MathHelper.sqrt((x * x) + (z * z));
		rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		rotationPitch = (float) (MathHelper.atan2(y, f1) * (180D / Math.PI));
		prevRotationYaw = rotationYaw;
		prevRotationPitch = rotationPitch;
		ticksInGround = 0;
	}

	/**
	 * Updates the entity motion clientside, called by packets from the server
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;

		if ((prevRotationPitch == 0.0F) && (prevRotationYaw == 0.0F)) {
			final float f = MathHelper.sqrt((x * x) + (z * z));
			rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
			rotationPitch = (float) (MathHelper.atan2(y, f) * (180D / Math.PI));
			prevRotationYaw = rotationYaw;
			prevRotationPitch = rotationPitch;
		}
	}

	public MovingThrownProjectile setIgnoreBlocks(boolean ignoreBlocks) {
		this.ignoreBlocks = ignoreBlocks;
		return this;
	}

	public MovingThrownProjectile setColor(int color) {
		this.color = color;
		return this;
	}

	public void setSizes(float width, float height) {
		this.setSize(width, height);
	}

	@Override
	protected boolean isFireballFiery() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void onUpdate() {
		//		super.onUpdate();
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		if (!world.isRemote) {
			this.setFlag(6, this.isGlowing());
		}

		this.onEntityUpdate();

		if (throwableShake > 0) {
			--throwableShake;
		}

		if (inGround) {
			if (world.getBlockState(new BlockPos(xTile, yTile, zTile)).getBlock() == inTile) {
				++ticksInGround;

				if (ticksInGround == 1200) {
					this.setDead();
				}

				return;
			}

			inGround = false;
			motionX *= rand.nextFloat() * 0.2F;
			motionY *= rand.nextFloat() * 0.2F;
			motionZ *= rand.nextFloat() * 0.2F;
			ticksInGround = 0;
			ticksInAir = 0;
		} else {
			++ticksInAir;
		}

		Vec3d vec3d = new Vec3d(posX, posY, posZ);
		Vec3d vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
		RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d1);
		vec3d = new Vec3d(posX, posY, posZ);
		vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

		if (raytraceresult != null) {
			vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		//		final Predicate<Entity> Targets = Predicates.and(
		//				EntitySelectors.NOT_SPECTATING,
		//				ent -> (ent != null) && !ent.canBeCollidedWith() && (ent != shootingEntity)
		//						&& !(ent instanceof MovingThrownProjectile)
		//		);
		Entity entity = null;
		//		final List<Entity> list = world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(motionX, motionY, motionZ).grow(1.0D), Targets);
		final List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(motionX, motionY, motionZ).grow(1.0D));
		double d0 = 0.0D;
		boolean flag = false;

		for (final Entity entity1 : list) {
			if (entity1.canBeCollidedWith()) {
				if (entity1 == ignoreEntity) {
					flag = true;
				} else if ((shootingEntity != null) && (ticksExisted < 2) && (ignoreEntity == null)) {
					ignoreEntity = entity1;
					flag = true;
				} else {
					flag = false;
					final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
					final RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

					if (raytraceresult1 != null) {
						final double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);

						if ((d1 < d0) || (d0 == 0.0D)) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}
		}
		if (ignoreEntity != null) {
			if (flag) {
				ignoreTime = 2;
			} else if (ignoreTime-- <= 0) {
				ignoreEntity = null;
			}
		}
		//		System.out.println(entity);
		if (entity != null) {
			raytraceresult = new RayTraceResult(entity);
		}

		if (raytraceresult != null) {
			if ((raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) && (world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL)) {
				this.setPortal(raytraceresult.getBlockPos());
			} else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
				this.onImpact(raytraceresult);
			}
		}

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		final float f = MathHelper.sqrt((motionX * motionX) + (motionZ * motionZ));
		rotationYaw = (float) (MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));

		for (rotationPitch = (float) (MathHelper.atan2(motionY, f) * (180D / Math.PI)); (rotationPitch - prevRotationPitch) < -180.0F; prevRotationPitch -= 360.0F) {
			;
		}

		while ((rotationPitch - prevRotationPitch) >= 180.0F) {
			prevRotationPitch += 360.0F;
		}

		while ((rotationYaw - prevRotationYaw) < -180.0F) {
			prevRotationYaw -= 360.0F;
		}

		while ((rotationYaw - prevRotationYaw) >= 180.0F) {
			prevRotationYaw += 360.0F;
		}

		rotationPitch = prevRotationPitch + ((rotationPitch - prevRotationPitch) * 0.2F);
		rotationYaw = prevRotationYaw + ((rotationYaw - prevRotationYaw) * 0.2F);
		float f1 = 0.99F;
		final float f2 = 0.03F;//this.getGravityVelocity();

		if (this.isInWater()) {
			for (int j = 0; j < 4; ++j) {
				final float f3 = 0.25F;
				world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - (motionX * 0.25D), posY - (motionY * 0.25D), posZ - (motionZ * 0.25D), motionX, motionY, motionZ);
			}

			f1 = 0.8F;
		}

		motionX *= f1;
		motionY *= f1;
		motionZ *= f1;

		if (!this.hasNoGravity()) {
			motionY -= f2;
		}

		final int life = 30;
		this.setPosition(posX, posY, posZ);
		this.spawnParticle();
		//		//		}

		if (ticksExisted >= life) {
			this.setDead();
		}
		if (this.isInWater()) {
			this.setDead();
		}
		//		if (onGround) {
		//		}
	}

	int frame = 0;

	public void spawnParticle() {
		try {
			final Random random = Reference.random;
			//			for (int i = 0; i < 1; i++) {
			final double d0 = (random.nextFloat() * 2.0F) - 1.0F;
			final double d1 = (random.nextFloat() * 2.0F) - 1.0F;
			final double d2 = (random.nextFloat() * 2.0F) - 1.0F;

			if (((d0 * d0) + (d1 * d1) + (d2 * d2)) <= 1.0D) {
				final double d3 = posX + ((d0 * 1F) / 4.0D);
				final double d4 = posY + (1F / 2.0F) + ((d1 * 1F) / 4.0D);
				final double d5 = posZ + ((d2 * 1F) / 4.0D);
				//            this.world.spawnParticle(this.particleTypes, false, d3, d4, d5, d0, d1 + 0.2D, d2);
				if ((world instanceof WorldServer)) {
					NetworkHandler.sendToClients(
							(WorldServer) world, this.getPosition(),
							new EffectsRenderPacket(this, d3, d4, d5, d0, d1 + 0.2D, d2, color, 4, 1F, 1F)
					);
					//				new EffectsRenderPacket(this, posX, posY, posZ, d3, d4, d5, color, 4, 1F, 1F)
				}
			}
			//			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

		//		final float f2 = random.nextFloat() * 4.0F;
		//		final float f3 = random.nextFloat() * ((float) Math.PI * 2F);
		//		final double d6 = MathHelper.cos(f3) * f2;
		//		final double d7 = 0.01D + (random.nextDouble() * 0.5D);
		//		final double d8 = MathHelper.sin(f3) * f2;

		//			if (!world.isRemote) {
		//				final NBTTagCompound tag = new NBTTagCompound();
		//				tag.setInteger("BreathColor", color);
		//				tag.setDouble("x", posX);
		//				tag.setDouble("y", posY);
		//				tag.setDouble("z", posZ);
		//				//				NetworkHandler.INSTANCE.sendToAllTracking(new UpdateThrowable(this, tag), this);
		//		}
	}

	@Override
	protected void onImpact(RayTraceResult movingObject) {
		final boolean flag = world.getGameRules().getBoolean("mobGriefing");
		final Entity hit = movingObject.entityHit;
		if (!world.isRemote) {
			if ((hit != null) && (hit instanceof MovingThrownProjectile)) {
				return;
			}
			if ((shootingEntity != null) && (hit != null)) {
				boolean pvpEnabled = false;
				try {
					if (shootingEntity instanceof EntityPlayerMP) {
						pvpEnabled = ((EntityPlayerMP) shootingEntity).getServer().isPVPEnabled();
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				final AxisAlignedBB bb1 = this.getEntityBoundingBox().grow(1);
				final Predicate<Entity> Targets = Predicates.and(
						EntitySelectors.NOT_SPECTATING,
						ent -> (ent != null) && ent.canBeCollidedWith() && (ent != shootingEntity)
								&& !(ent instanceof MovingThrownProjectile) && !ent.isImmuneToFire()
				);
				//
				final List<Entity> splash = world.getEntitiesInAABBexcluding(this, bb1, Targets);
				for (final Entity e : splash) {
					if ((e instanceof EntityPlayer) && !pvpEnabled) {

					} else {
						if (!e.isImmuneToFire()) {
							e.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, this, shootingEntity).setDamageBypassesArmor().setFireDamage().setMagicDamage(), (float) damage);
							e.setFire(5);
						}
					}
				}
				this.setDead();
			}
			if ((movingObject.entityHit != null) && !(movingObject.entityHit instanceof MovingThrownProjectile) && !movingObject.entityHit.isEntityEqual(shootingEntity)) {
				this.applyEnchantments(shootingEntity, movingObject.entityHit);
				this.setDead();
			}
			if ((movingObject.typeOfHit != Type.ENTITY) ||
					((movingObject.entityHit != null) &&
							!(movingObject.entityHit instanceof MovingThrownProjectile))) {
				if ((movingObject.typeOfHit == Type.BLOCK) && flag) {
					final BlockPos blockpos = movingObject.getBlockPos().offset(movingObject.sideHit);
					if (world.isAirBlock(blockpos)) {
						world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
					}
				}
				this.setDead();
			}
		}
		//		this.setDead();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	public void setAim(Entity fireball, Entity entity, float p_184547_2_, float p_184547_3_, float p_184547_4_, float p_184547_5_, float p_184547_6_) {
		final float f = -MathHelper.sin(p_184547_3_ * 0.017453292F) * MathHelper.cos(p_184547_2_ * 0.017453292F);
		final float f1 = -MathHelper.sin(p_184547_2_ * 0.017453292F);
		final float f2 = MathHelper.cos(p_184547_3_ * 0.017453292F) * MathHelper.cos(p_184547_2_ * 0.017453292F);
		fireball.motionX = entity.motionX;
		fireball.motionZ = entity.motionZ;
		if (!entity.onGround) {
			fireball.motionY = entity.motionY;
		}
		this.setThrowableHeading(fireball, f, f1, f2, p_184547_5_, p_184547_6_);
	}

	public void setThrowableHeading(Entity fireball, double x, double y, double z, float velocity, float inaccuracy) {
		x = x + (rand.nextGaussian() * 0.007499999832361937D * inaccuracy);
		y = y + (rand.nextGaussian() * 0.007499999832361937D * inaccuracy);
		z = z + (rand.nextGaussian() * 0.007499999832361937D * inaccuracy);
		x = x * velocity;
		y = y * velocity;
		z = z * velocity;
		fireball.motionX = x;
		fireball.motionY = y;
		fireball.motionZ = z;
		final float f1 = MathHelper.sqrt((x * x) + (z * z));
		fireball.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		fireball.rotationPitch = (float) (MathHelper.atan2(y, f1) * (180D / Math.PI));
		fireball.prevRotationYaw = fireball.rotationYaw;
		fireball.prevRotationPitch = fireball.rotationPitch;
	}

	@Override
	public float getCollisionBorderSize() {
		return 1F;
	}

	public interface HitResult extends Serializable {

		public static class EmptyResult implements HitResult, Serializable {
			@Override
			public void onHit(MovingThrownProjectile entity, RayTraceResult result, boolean isServer) {
			}
		}

		void onHit(MovingThrownProjectile entity, RayTraceResult result, boolean isServer);
	}

	@Override
	public boolean isInRangeToRender3d(double x, double y, double z) {
		return true;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("BreathColor")) {
			color = compound.getInteger("BreathColor");
		}
		if (compound.hasKey("frame")) {
			frame = compound.getInteger("frame");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("BreathColor", color);
		compound.setInteger("frame", frame);
		return super.writeToNBT(compound);
	}
}
