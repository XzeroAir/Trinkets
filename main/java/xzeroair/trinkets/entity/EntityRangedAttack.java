package xzeroair.trinkets.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class EntityRangedAttack extends EntityArrow {

    private final int xTile;
    private final int yTile;
    private final int zTile;
    private Block inTile;
    private int ticksInGround;
    private int ticksInAir;
    @Nullable
    public Entity ignoreEntity;
    private int ignoreTime;
    protected int color;
    protected Element element;

    public EntityLivingBase shootingEntity;
    private int ticksAlive;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;

    private boolean ignoreBlocks = false;
    protected boolean interactWithTerrain;

    protected String[] EFFECTS;

    public EntityRangedAttack(World world) {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(1F, 1F);
        this.color = 12582912;
        this.element = Elements.NEUTRAL;
        this.setDamage(1);
        this.interactWithTerrain = false;
        this.EFFECTS = new String[0];
    }

    public EntityRangedAttack(World world, double x, double y, double z, double accelX, double accelY, double accelZ) {
        this(world);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        final double d0 = MathHelper.sqrt((accelX * accelX) + (accelY * accelY) + (accelZ * accelZ));
        this.accelerationX = (accelX / d0) * 0.1D;
        this.accelerationY = (accelY / d0) * 0.1D;
        this.accelerationZ = (accelZ / d0) * 0.1D;
    }

    public EntityRangedAttack(World world, EntityLivingBase shooter, double accelX, double accelY, double accelZ, int color) {
        //		this(worldIn, shooter.posX, (shooter.posY + shooter.getEyeHeight()) - 0.10000000149011612D, shooter.posZ);
        this(world);
        this.shootingEntity = shooter;
        this.setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        //		isImmuneToFire = true;
        final float f = -MathHelper.sin(shooter.rotationYaw * 0.017453292F) * MathHelper.cos(shooter.rotationPitch * 0.017453292F);
        final float f1 = -MathHelper.sin((shooter.rotationPitch + 0) * 0.017453292F);
        final float f2 = MathHelper.cos(shooter.rotationYaw * 0.017453292F) * MathHelper.cos(shooter.rotationPitch * 0.017453292F);
        //		final float f = MathHelper.sqrt((x * x) + (y * y) + (z * z));
        final double d0 = MathHelper.sqrt((f * f) + (f1 * f1) + (f2 * f2));
        //		//MathHelper.sqrt((accelX * accelX) + (accelY * accelY) + (accelZ * accelZ));
        this.accelerationX = (accelX / d0) * (0.1D * (this.isFlying(shooter) ? 4 : 1));
        this.accelerationY = (accelY / d0) * (0.1D * (this.isFlying(shooter) ? 4 : 1));
        this.accelerationZ = (accelZ / d0) * (0.1D * (this.isFlying(shooter) ? 4 : 1));
        this.color = color;
    }

    private boolean isFlying(EntityLivingBase entity) {
        return (entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isFlying;
    }

    public EntityRangedAttack setIgnoreBlocks(boolean ignoreBlocks) {
        this.ignoreBlocks = ignoreBlocks;
        return this;
    }

    public EntityRangedAttack setColor(int color) {
        this.color = color;
        return this;
    }

    public void setSizes(float width, float height) {
        this.setSize(width, height);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public EntityRangedAttack setElement(Element element) {
        if (element == null) {
            this.element = Elements.NEUTRAL;
        } else {
            this.element = element;
        }
        return this;
    }

    public EntityRangedAttack setEffects(String[] effects) {
        if (effects != null) {
            this.EFFECTS = effects;
        }
        return this;
    }

    public EntityRangedAttack setAllowTerrainInteraction(boolean can) {
        this.interactWithTerrain = can;
        return this;
    }

    @Override
    public void onUpdate() {
        //		super.onUpdate();
        if (this.world.isRemote || (((this.shootingEntity == null) || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this)))) {
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            if (!this.world.isRemote) {
                this.setFlag(6, this.isGlowing());
            }

            this.onEntityUpdate();

            //			this.setFire(1);

            if (this.arrowShake > 0) {
                --this.arrowShake;
            }

            if (this.inGround) {
                if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
                    ++this.ticksInGround;

                    if (this.ticksInGround == 1200) {
                        this.setDead();
                    }

                    return;
                }

                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            } else {
                ++this.ticksInAir;
            }

            if (this.interactWithTerrain) {
                if (Elements.ICE.equals(this.element)) {
                    if (this.isInsideOfMaterial(Material.WATER) || this.isInWater()) {
                        BlockHelperUtil.freezeWater(this.world, this.posX, this.posY, this.posZ, 0, 1.0D);
                        this.setDead();
                        return;
                    }
                    if (this.isInsideOfMaterial(Material.LAVA) || this.isInLava()) {
                        BlockHelperUtil.freezeLava(this.world, this.posX, this.posY, this.posZ, 0, 1.0D);
                        this.setDead();
                        return;
                    }
                }
            }

            Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
            vec3d = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

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
            final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
            double d0 = 0.0D;
            boolean flag = false;

            for (final Entity entity1 : list) {
                if (entity1.canBeCollidedWith()) {
                    if (entity1 == this.ignoreEntity) {
                        flag = true;
                    } else if ((this.shootingEntity != null) && (this.ticksExisted < 2) && (this.ignoreEntity == null)) {
                        this.ignoreEntity = entity1;
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
            if (this.ignoreEntity != null) {
                if (flag) {
                    this.ignoreTime = 2;
                } else if (this.ignoreTime-- <= 0) {
                    this.ignoreEntity = null;
                }
            }
            if (entity != null) {
                raytraceresult = new RayTraceResult(entity);
            }
            if (raytraceresult != null) {
                if ((raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) && (this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL)) {
                    this.setPortal(raytraceresult.getBlockPos());
                } else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                }
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            final float f = MathHelper.sqrt((this.motionX * this.motionX) + (this.motionZ * this.motionZ));
            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

            for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f) * (180D / Math.PI)); (this.rotationPitch - this.prevRotationPitch) < -180.0F; this.prevRotationPitch -= 360.0F) {
            }

            while ((this.rotationPitch - this.prevRotationPitch) >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while ((this.rotationYaw - this.prevRotationYaw) < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while ((this.rotationYaw - this.prevRotationYaw) >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + ((this.rotationPitch - this.prevRotationPitch) * 0.2F);
            this.rotationYaw = this.prevRotationYaw + ((this.rotationYaw - this.prevRotationYaw) * 0.2F);
            float f1 = 0.99F;
            final float f2 = 0.03F;//this.getGravityVelocity();

            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    final float f3 = 0.25F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - (this.motionX * 0.25D), this.posY - (this.motionY * 0.25D), this.posZ - (this.motionZ * 0.25D), this.motionX, this.motionY, this.motionZ);
                }
                f1 = 0.8F;
            }

            this.motionX *= f1;
            this.motionY *= f1;
            this.motionZ *= f1;

            if (!this.hasNoGravity()) {
                this.motionY -= f2;
            }

            final int life = 30;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.spawnParticle();

            if (this.ticksExisted >= life) {
                this.setDead();
            }
            if (this.isInWater()) {
                this.setDead();
            }
            //		if (onGround) {
            //		}
        }
    }

    public void spawnParticle() {
        try {
            final Random random = Reference.random;
            final double d0 = (random.nextFloat() * 2.0F) - 1.0F;
            final double d1 = (random.nextFloat() * 2.0F) - 1.0F;
            final double d2 = (random.nextFloat() * 2.0F) - 1.0F;

            if (((d0 * d0) + (d1 * d1) + (d2 * d2)) <= 1.0D) {
                final double d3 = this.posX + ((d0 * 1F) / 4.0D);
                final double d4 = this.posY + (1F / 2.0F) + ((d1 * 1F) / 4.0D);
                final double d5 = this.posZ + ((d2 * 1F) / 4.0D);
                if ((this.world instanceof WorldServer)) {
                    if (Elements.LIGHTNING.equals(this.element)) {
                        NetworkHandler.sendToClients((WorldServer) this.world, this.getPosition(), new EffectsRenderPacket(this, d3, d4, d5, d0, d1 + 0.2D, d2, this.color, 7, 0.8F, 1F));
                    } else {
                        NetworkHandler.sendToClients((WorldServer) this.world, this.getPosition(), new EffectsRenderPacket(this, d3, d4, d5, d0, d1 + 0.2D, d2, this.color, 4, 1F, 1F));
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHit(RayTraceResult movingObject) {
        final boolean flag = this.world.getGameRules().getBoolean(Reference.MINECRAFT_GAMERULE_MOBGRIEFING);
        final Entity hitEntity = movingObject.entityHit;
        if ((this.world == null) || (this.world.isRemote) || (hitEntity instanceof EntityRangedAttack)) {
            return;
        }
        boolean pvpEnabled = false;
        MinecraftServer server = this.world.getMinecraftServer();
        if ((server == null) && (this.shootingEntity instanceof EntityPlayerMP)) {
            server = this.shootingEntity.getServer();
        }
        if (server != null) {
            pvpEnabled = server.isPVPEnabled();
        }
        Type hitType = movingObject.typeOfHit;
        if (hitType == Type.BLOCK) {
            final BlockPos hitBlock = movingObject.getBlockPos();
            if (flag && this.interactWithTerrain) {
                final IBlockState state = this.world.getBlockState(hitBlock);
                final Block block = state.getBlock();
                final BlockPos blockpos = hitBlock.offset(movingObject.sideHit);
                if (Elements.LIGHTNING.equals(this.element)) {
                    if (this.EFFECTS.length > 0) {
                        EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
                        entityareaeffectcloud.setOwner(this.shootingEntity);
                        entityareaeffectcloud.setRadius(3.0F);
                        entityareaeffectcloud.setRadiusOnUse(-0.5F);
                        entityareaeffectcloud.setWaitTime(10);
                        entityareaeffectcloud.setDuration(60);
                        entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());
//                        entityareaeffectcloud.setPotion(type);

                        for (String str : this.EFFECTS) {
                            final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(str);
                            if (potion.getPotion() != null) {
                                entityareaeffectcloud.addEffect(potion.getPotionEffect());
                            }
                        }
                        entityareaeffectcloud.setColor(this.color);
                        this.world.spawnEntity(entityareaeffectcloud);
                    }
                } else if (Elements.ICE.equals(this.element)) {
                    Consumer<BlockPos> func = (i) -> {
                        final IBlockState s = this.world.getBlockState(i);
                        final Block b = s.getBlock();
                        if (b instanceof BlockSnow) {
                            try {
                                int layers = b.getMetaFromState(s);
                                if (layers < 7) {
                                    this.world.setBlockState(i, b.getStateFromMeta(layers + 1));
                                    this.world.neighborChanged(i, Blocks.SNOW_LAYER, i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (this.world.isAirBlock(i)) {
                                this.world.setBlockState(i, Blocks.SNOW_LAYER.getDefaultState());
                                this.world.neighborChanged(i, Blocks.SNOW_LAYER, i);
                            }
                        }
                    };
                    func.accept(hitBlock);
                    func.accept(blockpos);
                    func.accept(blockpos.east());
                    func.accept(blockpos.west());
                    func.accept(blockpos.south());
                    func.accept(blockpos.north());
                    func.accept(blockpos.down());
                } else if (this.element == Elements.FIRE) {
//                        if ((block instanceof BlockStone) || (block == Blocks.COBBLESTONE)) {
//                            world.setBlockState(hitBlock, Blocks.MAGMA.getDefaultState());
//                            world.neighborChanged(hitBlock, Blocks.MAGMA, hitBlock);
//                        } else if (block instanceof BlockSand) {
//                            world.setBlockState(hitBlock, Blocks.GLASS.getDefaultState());
//                            world.neighborChanged(hitBlock, Blocks.GLASS, hitBlock);
//                        } else
                    if (block instanceof BlockIce) {
                        if (this.world.provider.doesWaterVaporize()) {
                            this.world.setBlockToAir(hitBlock);
                        } else {
                            this.world.setBlockState(hitBlock, Blocks.WATER.getDefaultState());
                            this.world.neighborChanged(hitBlock, Blocks.WATER, hitBlock);
                        }
                    }
                    if (this.world.isAirBlock(blockpos)) {
                        this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                        this.world.neighborChanged(blockpos, Blocks.FIRE, blockpos);
                    }
                }
            }
        } else if (hitType == Type.ENTITY) {
            if (hitEntity != null) {
                final AxisAlignedBB bb1 = this.getEntityBoundingBox().grow(1);
                final Predicate<Entity> Targets = Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> (ent != null) && ent.canBeCollidedWith() && (ent != this.shootingEntity) && !(ent instanceof EntityRangedAttack) && !ent.isImmuneToFire());
                //
                final List<Entity> splash = this.world.getEntitiesInAABBexcluding(this, bb1, Targets);
                for (final Entity e : splash) {
                    if ((!(e instanceof EntityPlayer)) || pvpEnabled) {
                        this.applyEnchantments(this.shootingEntity, e);
                        if (Elements.LIGHTNING.equals(this.element)) {
                            e.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.LIGHTNING_BOLT.damageType, this, this.shootingEntity).setDamageBypassesArmor().setMagicDamage(), (float) this.getDamage());
                            e.setFire(1);
                        } else if (Elements.ICE.equals(this.element)) {
                            e.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, this, this.shootingEntity).setDamageBypassesArmor().setMagicDamage(), (float) this.getDamage());
                        } else {
                            e.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, this, this.shootingEntity).setDamageBypassesArmor().setFireDamage().setMagicDamage(), (float) this.getDamage());
                            e.setFire(5);
                        }
                        if (e instanceof EntityLivingBase) {
                            for (final String potID : this.EFFECTS) {
                                final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(potID);
                                if (potion.getPotion() != null) {
                                    ((EntityLivingBase) e).addPotionEffect(potion.getPotionEffect());
                                }
                            }
                        }
                    }
                }
            }
        }
        this.setDead();
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        return !this.isEntityInvulnerable(source);
    }

    @Override
    public float getCollisionBorderSize() {
        return super.getCollisionBorderSize();//1F;
    }

    public interface HitResult extends Serializable {

        class EmptyResult implements HitResult, Serializable {
            @Override
            public void onHit(EntityRangedAttack entity, RayTraceResult result, boolean isServer) {
            }
        }

        void onHit(EntityRangedAttack entity, RayTraceResult result, boolean isServer);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("BreathColor")) {
            this.color = compound.getInteger("BreathColor");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        compound.setInteger("BreathColor", this.color);
        return super.writeToNBT(compound);
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }
}
