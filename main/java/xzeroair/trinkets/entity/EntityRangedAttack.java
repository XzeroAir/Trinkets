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
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;
import xzeroair.trinkets.util.helpers.PotionHelper;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class EntityRangedAttack extends EntityArrow {

    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private int ticksInGround;
    private int ticksInAir;
    public Entity ignoreEntity;
    private int ignoreTime;
    int color;
    protected Element element;

    public EntityLivingBase shootingEntity;
    private int ticksAlive;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;

    private boolean ignoreBlocks = false;

    public EntityRangedAttack(World world) {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(1F, 1F);
        color = 12582912;
        element = Elements.NEUTRAL;
        this.setDamage(TrinketsConfig.SERVER.races.dragon.elementConfig.fire.breath_damage);
    }

    public EntityRangedAttack(World world, double x, double y, double z, double accelX, double accelY, double accelZ) {
        this(world);
        this.setLocationAndAngles(x, y, z, rotationYaw, rotationPitch);
        this.setPosition(x, y, z);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        final double d0 = MathHelper.sqrt((accelX * accelX) + (accelY * accelY) + (accelZ * accelZ));
        accelerationX = (accelX / d0) * 0.1D;
        accelerationY = (accelY / d0) * 0.1D;
        accelerationZ = (accelZ / d0) * 0.1D;
    }

    public EntityRangedAttack(World world, EntityLivingBase shooter, double accelX, double accelY, double accelZ, int color) {
        //		this(worldIn, shooter.posX, (shooter.posY + shooter.getEyeHeight()) - 0.10000000149011612D, shooter.posZ);
        this(world);
        shootingEntity = shooter;
        this.setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(posX, posY, posZ);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        //		isImmuneToFire = true;
        final float f = -MathHelper.sin(shooter.rotationYaw * 0.017453292F) * MathHelper.cos(shooter.rotationPitch * 0.017453292F);
        final float f1 = -MathHelper.sin((shooter.rotationPitch + 0) * 0.017453292F);
        final float f2 = MathHelper.cos(shooter.rotationYaw * 0.017453292F) * MathHelper.cos(shooter.rotationPitch * 0.017453292F);
        //		final float f = MathHelper.sqrt((x * x) + (y * y) + (z * z));
        final double d0 = MathHelper.sqrt((f * f) + (f1 * f1) + (f2 * f2));
        //		//MathHelper.sqrt((accelX * accelX) + (accelY * accelY) + (accelZ * accelZ));
        accelerationX = (accelX / d0) * (0.1D * (this.isFlying(shooter) ? 4 : 1));
        accelerationY = (accelY / d0) * (0.1D * (this.isFlying(shooter) ? 4 : 1));
        accelerationZ = (accelZ / d0) * (0.1D * (this.isFlying(shooter) ? 4 : 1));

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
            if (element == Elements.ICE) {
                setDamage(TrinketsConfig.SERVER.races.dragon.elementConfig.ice.breath_damage);
            } else if (element == Elements.LIGHTNING) {
                setDamage(TrinketsConfig.SERVER.races.dragon.elementConfig.lightning.breath_damage);
            }
        }
        return this;
    }

    @Override
    public void onUpdate() {
        //		super.onUpdate();
        if (world.isRemote || (((shootingEntity == null) || !shootingEntity.isDead) && world.isBlockLoaded(new BlockPos(this)))) {
            lastTickPosX = posX;
            lastTickPosY = posY;
            lastTickPosZ = posZ;
            if (!world.isRemote) {
                this.setFlag(6, this.isGlowing());
            }

            this.onEntityUpdate();

            //			this.setFire(1);

            if (arrowShake > 0) {
                --arrowShake;
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

            if (element == Elements.ICE && TrinketsConfig.SERVER.races.dragon.elementConfig.ice.terrain) {
                if (this.isInsideOfMaterial(Material.WATER) || this.isInWater()) {
                    BlockHelperUtil.freezeWater(world, posX, posY, posZ, 0, 1.0D);
                    this.setDead();
                    return;
                }
                if (this.isInsideOfMaterial(Material.LAVA) || this.isInLava()) {
                    BlockHelperUtil.freezeLava(world, posX, posY, posZ, 0, 1.0D);
                    this.setDead();
                    return;
                }
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
            if (entity != null) {
                raytraceresult = new RayTraceResult(entity);
            }
            if (raytraceresult != null) {
                if ((raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) && (world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL)) {
                    this.setPortal(raytraceresult.getBlockPos());
                } else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                }
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            final float f = MathHelper.sqrt((motionX * motionX) + (motionZ * motionZ));
            rotationYaw = (float) (MathHelper.atan2(motionX, motionZ) * (180D / Math.PI));

            for (rotationPitch = (float) (MathHelper.atan2(motionY, f) * (180D / Math.PI)); (rotationPitch - prevRotationPitch) < -180.0F; prevRotationPitch -= 360.0F) {

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

            if (ticksExisted >= life) {
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
                final double d3 = posX + ((d0 * 1F) / 4.0D);
                final double d4 = posY + (1F / 2.0F) + ((d1 * 1F) / 4.0D);
                final double d5 = posZ + ((d2 * 1F) / 4.0D);
                if ((world instanceof WorldServer)) {
                    if (element.equals(Elements.LIGHTNING)) {
                        NetworkHandler.sendToClients((WorldServer) world, this.getPosition(), new EffectsRenderPacket(this, d3, d4, d5, d0, d1 + 0.2D, d2, color, 7, 0.8F, 1F));
                    } else {
                        NetworkHandler.sendToClients((WorldServer) world, this.getPosition(), new EffectsRenderPacket(this, d3, d4, d5, d0, d1 + 0.2D, d2, color, 4, 1F, 1F));
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHit(RayTraceResult movingObject) {
        final boolean flag = world.getGameRules().getBoolean("mobGriefing");
        final Entity hitEntity = movingObject.entityHit;
        if ((world == null) || (world.isRemote) || (hitEntity instanceof EntityRangedAttack)) {
            return;
        }
        boolean pvpEnabled = false;
        MinecraftServer server = world.getMinecraftServer();
        if ((server == null) && (shootingEntity instanceof EntityPlayerMP)) {
            server = shootingEntity.getServer();
        }
        if (server != null) {
            pvpEnabled = server.isPVPEnabled();
        }
        Type hitType = movingObject.typeOfHit;
        if (hitType == Type.BLOCK) {
            final BlockPos hitBlock = movingObject.getBlockPos();
            if (flag) {
                final IBlockState state = world.getBlockState(hitBlock);
                final Block block = state.getBlock();
                final BlockPos blockpos = hitBlock.offset(movingObject.sideHit);
                if (element.equals(Elements.LIGHTNING) && TrinketsConfig.SERVER.races.dragon.elementConfig.lightning.terrain) {
                    String[] effects = TrinketsConfig.SERVER.races.dragon.elementConfig.lightning.effects;
                    if (effects.length > 0) {
                        EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
                        entityareaeffectcloud.setOwner(this.shootingEntity);
                        entityareaeffectcloud.setRadius(3.0F);
                        entityareaeffectcloud.setRadiusOnUse(-0.5F);
                        entityareaeffectcloud.setWaitTime(10);
                        entityareaeffectcloud.setDuration(60);
                        entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());
//                        entityareaeffectcloud.setPotion(type);

                        for (String str : effects) {
                            final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(str);
                            if (potion.getPotion() != null) {
                                entityareaeffectcloud.addEffect(potion.getPotionEffect());
                            }
                        }
                        entityareaeffectcloud.setColor(this.color);
                        this.world.spawnEntity(entityareaeffectcloud);
                    }
                } else if (element.equals(Elements.ICE) && TrinketsConfig.SERVER.races.dragon.elementConfig.ice.terrain) {
                    Consumer<BlockPos> func = (i) -> {
                        final IBlockState s = world.getBlockState(i);
                        final Block b = s.getBlock();
                        if (b instanceof BlockSnow) {
                            try {
                                int layers = b.getMetaFromState(s);
                                if (layers < 7) {
                                    world.setBlockState(i, b.getStateFromMeta(layers + 1));
                                    world.neighborChanged(i, Blocks.SNOW_LAYER, i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (world.isAirBlock(i)) {
                                world.setBlockState(i, Blocks.SNOW_LAYER.getDefaultState());
                                world.neighborChanged(i, Blocks.SNOW_LAYER, i);
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
                } else if (element.equals(Elements.FIRE) && TrinketsConfig.SERVER.races.dragon.elementConfig.fire.terrain) {
//                        if ((block instanceof BlockStone) || (block == Blocks.COBBLESTONE)) {
//                            world.setBlockState(hitBlock, Blocks.MAGMA.getDefaultState());
//                            world.neighborChanged(hitBlock, Blocks.MAGMA, hitBlock);
//                        } else if (block instanceof BlockSand) {
//                            world.setBlockState(hitBlock, Blocks.GLASS.getDefaultState());
//                            world.neighborChanged(hitBlock, Blocks.GLASS, hitBlock);
//                        } else
                    if (block instanceof BlockIce) {
                        if (world.provider.doesWaterVaporize()) {
                            world.setBlockToAir(hitBlock);
                        } else {
                            world.setBlockState(hitBlock, Blocks.WATER.getDefaultState());
                            world.neighborChanged(hitBlock, Blocks.WATER, hitBlock);
                        }
                    }
                    if (world.isAirBlock(blockpos)) {
                        world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                        world.neighborChanged(blockpos, Blocks.FIRE, blockpos);
                    }
                } else {
                    if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.DE_FIRE_RESIST && TrinketsConfig.SERVER.races.dragon.elementConfig.fire.terrain) {
                        if (world.isAirBlock(blockpos)) {
                            world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                            world.neighborChanged(blockpos, Blocks.FIRE, blockpos);
                        }
                    }
                }
            }
        } else if (hitType == Type.ENTITY) {
            if (hitEntity != null) {
                final AxisAlignedBB bb1 = this.getEntityBoundingBox().grow(1);
                final Predicate<Entity> Targets = Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> (ent != null) && ent.canBeCollidedWith() && (ent != shootingEntity) && !(ent instanceof EntityRangedAttack) && !ent.isImmuneToFire());
                //
                final List<Entity> splash = world.getEntitiesInAABBexcluding(this, bb1, Targets);
                for (final Entity e : splash) {
                    if ((e instanceof EntityPlayer) && !pvpEnabled) {

                    } else {
                        this.applyEnchantments(shootingEntity, e);
                        if (element.equals(Elements.LIGHTNING)) {
                            e.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.LIGHTNING_BOLT.damageType, this, shootingEntity).setDamageBypassesArmor().setMagicDamage(), (float) this.getDamage());
                            if (e instanceof EntityLivingBase) {
                                for (final String potID : TrinketsConfig.SERVER.races.dragon.elementConfig.lightning.effects) {
                                    final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(potID);
                                    if (potion.getPotion() != null) {
                                        ((EntityLivingBase) e).addPotionEffect(potion.getPotionEffect());
                                    }
                                }
                            }
                            e.setFire(1);
                        } else if (element.equals(Elements.ICE)) {
                            e.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, this, shootingEntity).setDamageBypassesArmor().setMagicDamage(), (float) this.getDamage());
                            if (e instanceof EntityLivingBase) {
                                if (e instanceof EntityLivingBase) {
                                    for (final String potID : TrinketsConfig.SERVER.races.dragon.elementConfig.ice.effects) {
                                        final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(potID);
                                        if (potion.getPotion() != null) {
                                            ((EntityLivingBase) e).addPotionEffect(potion.getPotionEffect());
                                        }
                                    }
                                }
                            }
                        } else {
                            e.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, this, shootingEntity).setDamageBypassesArmor().setFireDamage().setMagicDamage(), (float) this.getDamage());
                            if (e instanceof EntityLivingBase) {
                                if (e instanceof EntityLivingBase) {
                                    for (final String potID : TrinketsConfig.SERVER.races.dragon.elementConfig.fire.effects) {
                                        final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(potID);
                                        if (potion.getPotion() != null) {
                                            ((EntityLivingBase) e).addPotionEffect(potion.getPotionEffect());
                                        }
                                    }
                                }
                            }
                            e.setFire(5);
                        }
                    }
                }
            }
        } else {

        }
        this.setDead();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !this.isEntityInvulnerable(source);
    }

    @Override
    public float getCollisionBorderSize() {
        return super.getCollisionBorderSize();//1F;
    }

    public interface HitResult extends Serializable {

        public static class EmptyResult implements HitResult, Serializable {
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
            color = compound.getInteger("BreathColor");
        }
        //		if (compound.hasKey("frame")) {
        //			frame = compound.getInteger("frame");
        //		}
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("BreathColor", color);
        //		compound.setInteger("frame", frame);
        return super.writeToNBT(compound);
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }
}
