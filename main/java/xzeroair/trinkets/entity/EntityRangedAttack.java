package xzeroair.trinkets.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class EntityRangedAttack extends Entity {

    private static final DataParameter<Integer> BREATH_COLOR = EntityDataManager.createKey(EntityRangedAttack.class, DataSerializers.VARINT);
    private static final float IMPACT_AREA_RADIUS = 3.0F;
    private static final float IMPACT_AREA_VERTICAL_RADIUS = 1.5F;
    private static final int IMPACT_AREA_DURATION = 60;
    private static final int IMPACT_AREA_PULSE_INTERVAL = 10;
    private static final int IMPACT_AREA_REAPPLICATION_DELAY = 20;

    @Nullable
    public Entity ignoreEntity;
    private int ignoreTime;
    protected int color;
    protected Element element;

    public EntityLivingBase shootingEntity;
    @Nullable
    private UUID shootingEntityUUID;
    @Nullable
    private String shootingEntityName;
    private float damage;

    private boolean ignoreBlocks = false;
    protected boolean interactWithTerrain;
    protected float airDrag;
    protected float waterDrag;
    protected float lavaDrag;
    protected float gravityPerTick;
    protected int lifetimeTicks;
    protected int maxLifetimeTicks;
    protected boolean expireInWater;
    protected boolean expireInLava;

    protected String[] EFFECTS;

    public EntityRangedAttack(World world) {
        super(world);
        this.setSize(1F, 1F);
        this.color = 12582912;
        this.element = Elements.NEUTRAL;
        this.damage = 1.0F;
        this.interactWithTerrain = false;
        this.airDrag = 0.99F;
        this.waterDrag = 0.8F;
        this.lavaDrag = 0.8F;
        this.gravityPerTick = 0.03F;
        this.lifetimeTicks = 30;
        this.maxLifetimeTicks = 2400;
        this.expireInWater = true;
        this.expireInLava = true;
        this.EFFECTS = new String[0];
    }

    public EntityRangedAttack(World world, double x, double y, double z) {
        this(world);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
        this.resetMotion();
    }

    public EntityRangedAttack(World world, EntityLivingBase shooter, int color) {
        //		this(worldIn, shooter.posX, (shooter.posY + shooter.getEyeHeight()) - 0.10000000149011612D, shooter.posZ);
        this(world);
        this.shootingEntity = shooter;
        this.shootingEntityUUID = shooter.getUniqueID();
        this.shootingEntityName = shooter.getName();
        this.setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.resetMotion();
        this.color = color;
    }

    public EntityRangedAttack setIgnoreBlocks(boolean ignoreBlocks) {
        this.ignoreBlocks = ignoreBlocks;
        return this;
    }

    public EntityRangedAttack setColor(int color) {
        this.color = color;
        if (!this.world.isRemote) {
            this.getDataManager().set(BREATH_COLOR, color);
        }
        return this;
    }

    public int getColor() {
        return this.getDataManager().get(BREATH_COLOR);
    }

    public EntityRangedAttack setAirDrag(float airDrag) {
        this.airDrag = airDrag;
        return this;
    }

    public EntityRangedAttack setWaterDrag(float waterDrag) {
        this.waterDrag = waterDrag;
        return this;
    }

    public EntityRangedAttack setLavaDrag(float lavaDrag) {
        this.lavaDrag = lavaDrag;
        return this;
    }

    public EntityRangedAttack setGravityPerTick(float gravityPerTick) {
        this.gravityPerTick = gravityPerTick;
        return this;
    }

    public EntityRangedAttack setLifetimeTicks(int lifetimeTicks) {
        this.lifetimeTicks = Math.max(1, lifetimeTicks);
        return this;
    }

    public EntityRangedAttack setMaxLifetimeTicks(int maxLifetimeTicks) {
        this.maxLifetimeTicks = Math.max(1, maxLifetimeTicks);
        return this;
    }

    public EntityRangedAttack setExpireInWater(boolean expireInWater) {
        this.expireInWater = expireInWater;
        return this;
    }

    public EntityRangedAttack setExpireInLava(boolean expireInLava) {
        this.expireInLava = expireInLava;
        return this;
    }

    public void setSizes(float width, float height) {
        this.setSize(width, height);
    }

    public EntityRangedAttack setDamage(double damage) {
        this.damage = (float) damage;
        return this;
    }

    public float getDamage() {
        return this.damage;
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

    /*
     * Motion setup
     */

    public void shoot(Entity shooter, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
        final float x = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        final float y = -MathHelper.sin((pitch + pitchOffset) * 0.017453292F);
        final float z = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        this.shoot(x, y, z, velocity, inaccuracy);

        this.motionX += shooter.motionX;
        this.motionZ += shooter.motionZ;
        if (!shooter.onGround) {
            this.motionY += shooter.motionY;
        }
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        final Vec3d heading = this.createHeadingVector(x, y, z, inaccuracy);
        this.initializeMotion(heading, velocity);
    }

    /*
     * Main update flow
     */

    @Override
    public void onUpdate() {
        if (!this.canUpdateProjectile()) {
            if (!this.world.isRemote && this.shouldDestroyWithoutUpdate()) {
                this.setDead();
            }
            return;
        }

        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }

        this.onEntityUpdate();

        if (!this.handleTerrainInteraction()) {
            return;
        }

        final RayTraceResult hitResult = this.traceImpact();
        if (hitResult != null) {
            if ((hitResult.typeOfHit == RayTraceResult.Type.BLOCK) && (this.world.getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.PORTAL)) {
                this.setPortal(hitResult.getBlockPos());
            } else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitResult)) {
                this.onHit(hitResult);
                if (this.isDead) {
                    return;
                }
            }
        }

        this.moveProjectile();
        this.updateRotationFromMotion();
        this.applyMotionDecay();

        this.setPosition(this.posX, this.posY, this.posZ);

        if (this.shouldExpire()) {
            this.setDead();
        }
    }

    private boolean canUpdateProjectile() {
        return this.world.isRemote || (((this.shootingEntity == null) || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this)));
    }

    private boolean shouldDestroyWithoutUpdate() {
        return (this.shootingEntity != null) && this.shootingEntity.isDead;
    }

    private boolean handleTerrainInteraction() {
        if (!this.interactWithTerrain) {
            return true;
        }

        if (Elements.ICE.equals(this.element)) {
            return this.handleIceTerrainInteraction();
        }

        return true;
    }

    protected boolean handleIceTerrainInteraction() {
        if (this.world.isRemote) {
            return true;
        }

        final BlockPos pos = new BlockPos(this);
        if (this.isInsideOfMaterial(Material.WATER) || this.isInWater()) {
            if (!this.canModifyTerrain(pos, EnumFacing.UP)) {
                return true;
            }
            BlockHelperUtil.freezeWater(this.world, this.posX, this.posY, this.posZ, 0, 1.0D);
            this.spawnImpactArea(new Vec3d(this.posX, this.posY, this.posZ));
            this.setDead();
            return false;
        }

        if (this.isInsideOfMaterial(Material.LAVA) || this.isInLava()) {
            if (!this.canModifyTerrain(pos, EnumFacing.UP)) {
                return true;
            }
            BlockHelperUtil.freezeLava(this.world, this.posX, this.posY, this.posZ, 0, 1.0D);
            this.spawnImpactArea(new Vec3d(this.posX, this.posY, this.posZ));
            this.setDead();
            return false;
        }

        return true;
    }

    protected boolean canModifyTerrain(BlockPos pos, EnumFacing side) {
        final EntityLivingBase shooter = this.getShootingEntity();
        return !(shooter instanceof EntityPlayer) || ((EntityPlayer) shooter).canPlayerEdit(pos, side, ItemStack.EMPTY);
    }

    private RayTraceResult traceImpact() {
        final Vec3d start = new Vec3d(this.posX, this.posY, this.posZ);
        final Vec3d end = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult hitResult = this.ignoreBlocks ? null : this.world.rayTraceBlocks(start, end);
        Vec3d entityTraceEnd = end;

        if (hitResult != null) {
            entityTraceEnd = hitResult.hitVec;
        }

        final Entity hitEntity = this.findHitEntity(start, entityTraceEnd);
        if (hitEntity != null) {
            hitResult = new RayTraceResult(hitEntity);
        }

        return hitResult;
    }

    @Nullable
    private Entity findHitEntity(Vec3d start, Vec3d end) {
        Entity hitEntity = null;
        final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
        double closestDistance = 0.0D;
        boolean trackingIgnoredEntity = false;

        for (final Entity candidate : list) {
            if (!candidate.canBeCollidedWith()) {
                continue;
            }

            if (candidate == this.ignoreEntity) {
                trackingIgnoredEntity = true;
                continue;
            }

            if ((this.shootingEntity != null) && (this.ticksExisted < 2) && (this.ignoreEntity == null)) {
                this.ignoreEntity = candidate;
                trackingIgnoredEntity = true;
                continue;
            }

            trackingIgnoredEntity = false;
            final AxisAlignedBB axisalignedbb = candidate.getEntityBoundingBox().grow(0.30000001192092896D);
            final RayTraceResult candidateHit = axisalignedbb.calculateIntercept(start, end);

            if (candidateHit == null) {
                continue;
            }

            final double hitDistance = start.squareDistanceTo(candidateHit.hitVec);
            if ((hitDistance < closestDistance) || (closestDistance == 0.0D)) {
                hitEntity = candidate;
                closestDistance = hitDistance;
            }
        }

        if (this.ignoreEntity != null) {
            if (trackingIgnoredEntity) {
                this.ignoreTime = 2;
            } else if (this.ignoreTime-- <= 0) {
                this.ignoreEntity = null;
            }
        }

        return hitEntity;
    }

    private void moveProjectile() {
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
    }

    private void updateRotationFromMotion() {
        final float horizontalMotion = MathHelper.sqrt((this.motionX * this.motionX) + (this.motionZ * this.motionZ));
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

        for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, horizontalMotion) * (180D / Math.PI)); (this.rotationPitch - this.prevRotationPitch) < -180.0F; this.prevRotationPitch -= 360.0F) {
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
    }

    private void applyMotionDecay() {
        float drag = this.airDrag;

        if (this.isInWater()) {
            for (int j = 0; j < 4; ++j) {
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - (this.motionX * 0.25D), this.posY - (this.motionY * 0.25D), this.posZ - (this.motionZ * 0.25D), this.motionX, this.motionY, this.motionZ);
            }
            drag = this.waterDrag;
        } else if (this.isInLava() || this.isInsideOfMaterial(Material.LAVA)) {
            drag = this.lavaDrag;
        }

        this.motionX *= drag;
        this.motionY *= drag;
        this.motionZ *= drag;

        if (!this.hasNoGravity()) {
            this.motionY -= this.gravityPerTick;
        }
    }

    private boolean shouldExpire() {
        return (this.ticksExisted >= this.lifetimeTicks)
                || (this.ticksExisted >= this.maxLifetimeTicks)
                || (this.expireInWater && this.isInWater())
                || (this.expireInLava && (this.isInLava() || this.isInsideOfMaterial(Material.LAVA)));
    }

    private void resetMotion() {
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    private Vec3d createHeadingVector(double x, double y, double z, float inaccuracy) {
        Vec3d heading = this.normalizeVector(x, y, z);
        if (inaccuracy > 0.0F) {
            heading = heading.add(
                    this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy,
                    this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy,
                    this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy
            );
            heading = heading.normalize();
        }
        return heading;
    }

    private Vec3d normalizeVector(double x, double y, double z) {
        final double magnitude = MathHelper.sqrt((x * x) + (y * y) + (z * z));
        if (magnitude <= 1.0E-7D) {
            return Vec3d.ZERO;
        }
        return new Vec3d(x / magnitude, y / magnitude, z / magnitude);
    }

    private void initializeMotion(Vec3d heading, float velocity) {
        this.motionX = heading.x * velocity;
        this.motionY = heading.y * velocity;
        this.motionZ = heading.z * velocity;
        this.updateRotationFromMotion();
    }


    /*
     * Hit dispatch
     */

    protected void onHit(RayTraceResult movingObject) {
        final Entity hitEntity = movingObject.entityHit;
        if ((this.world == null) || (this.world.isRemote) || (hitEntity instanceof EntityRangedAttack)) {
            return;
        }
        if (movingObject.typeOfHit == Type.BLOCK) {
            this.handleBlockHit(movingObject);
        } else if (movingObject.typeOfHit == Type.ENTITY && hitEntity != null) {
            this.handleEntityHit(hitEntity);
        }
        this.setDead();
    }

    private void handleBlockHit(RayTraceResult movingObject) {
        final Vec3d impact = movingObject.hitVec == null
                ? new Vec3d(movingObject.getBlockPos()).add(0.5D, 0.5D, 0.5D)
                : movingObject.hitVec;
        this.spawnImpactArea(impact);
    }

    private void spawnImpactArea(Vec3d impact) {
        final AreaEffectEntity area = new AreaEffectEntity(this.world, impact.x, impact.y, impact.z);
        area.setOwner(this.getShootingEntity());
        area.setRadius(IMPACT_AREA_RADIUS);
        area.setVerticalRadius(IMPACT_AREA_VERTICAL_RADIUS);
        area.setWaitTime(0);
        area.setDuration(IMPACT_AREA_DURATION);
        area.setRadiusPerTick(-IMPACT_AREA_RADIUS / (float) IMPACT_AREA_DURATION);
        area.setPulseInterval(IMPACT_AREA_PULSE_INTERVAL);
        area.setReapplicationDelay(IMPACT_AREA_REAPPLICATION_DELAY);
        area.setColor(this.color);
        if (Elements.ICE.equals(this.element)) {
            area.setWaterBehavior(AreaEffectEntity.LiquidBehavior.FLOAT);
            area.setLavaBehavior(AreaEffectEntity.LiquidBehavior.EXPIRE);
        } else {
            area.setWaterBehavior(AreaEffectEntity.LiquidBehavior.EXPIRE);
            area.setLavaBehavior(AreaEffectEntity.LiquidBehavior.EXPIRE);
        }
        area.setEntityTargetPredicate(target -> this.canAffectTarget(target, this.isPvpEnabled()));

        for (final String effectId : this.EFFECTS) {
            final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(effectId);
            if (potion.getPotion() != null) {
                area.addAction(new AreaEffectEntity.PotionAreaAction(potion.getPotionEffect()));
            }
        }

        if (this.interactWithTerrain) {
            if (Elements.FIRE.equals(this.element)) {
                area.addAction(new AreaEffectEntity.PlaceFireAreaAction());
            } else if (Elements.ICE.equals(this.element)) {
                area.addAction(new AreaEffectEntity.FreezeLiquidAreaAction(4));
                area.addAction(new AreaEffectEntity.PlaceSnowAreaAction());
            }
        }

        if (!area.getActions().isEmpty()) {
            this.world.spawnEntity(area);
        }
    }

    private void handleEntityHit(Entity directHit) {
        final boolean pvpEnabled = this.isPvpEnabled();
        this.applyHitToTarget(directHit, pvpEnabled);

        final AxisAlignedBB splashBounds = this.getEntityBoundingBox().grow(1);
        final Predicate<Entity> targets = Predicates.and(
                EntitySelectors.NOT_SPECTATING,
                ent -> (ent != null)
                        && ent.canBeCollidedWith()
                        && (ent != this.shootingEntity)
                        && (ent != directHit)
                        && !(ent instanceof EntityRangedAttack)
                        && !ent.isImmuneToFire()
        );
        final List<Entity> splash = this.world.getEntitiesInAABBexcluding(this, splashBounds, targets);

        for (final Entity target : splash) {
            this.applyHitToTarget(target, pvpEnabled);
        }
    }

    /*
     * Entity damage effects
     */

    private void applyHitToTarget(Entity target, boolean pvpEnabled) {
        if (!this.canAffectTarget(target, pvpEnabled)) {
            return;
        }

        this.applyImpactEnchantments(target);
        this.applyElementalDamage(target);
        this.applyConfiguredEffects(target);
    }

    private boolean canAffectTarget(Entity target, boolean pvpEnabled) {
        if (target == null || !target.canBeCollidedWith() || target == this.shootingEntity || target instanceof EntityRangedAttack) {
            return false;
        }

        return !(target instanceof EntityPlayer) || pvpEnabled;
    }

    private boolean isPvpEnabled() {
        MinecraftServer server = this.world.getMinecraftServer();
        if ((server == null) && (this.shootingEntity instanceof EntityPlayerMP)) {
            server = this.shootingEntity.getServer();
        }
        return (server != null) && server.isPVPEnabled();
    }

    /*
     * Block and terrain effects
     */

    private void applyIceTerrainEffect(BlockPos hitBlock, BlockPos offsetBlock) {
        this.placeOrGrowSnow(hitBlock);
        this.placeOrGrowSnow(offsetBlock);
        this.placeOrGrowSnow(offsetBlock.east());
        this.placeOrGrowSnow(offsetBlock.west());
        this.placeOrGrowSnow(offsetBlock.south());
        this.placeOrGrowSnow(offsetBlock.north());
        this.placeOrGrowSnow(offsetBlock.down());
    }

    private void placeOrGrowSnow(BlockPos pos) {
        if (!this.canModifyTerrain(pos, EnumFacing.UP)) {
            return;
        }

        final IBlockState state = this.world.getBlockState(pos);
        final Block block = state.getBlock();
        if (this.extinguishFire(pos, block)) {
            return;
        }
        if (block instanceof BlockSnow) {
            try {
                final int layers = block.getMetaFromState(state);
                if (layers < 7) {
                    this.world.setBlockState(pos, block.getStateFromMeta(layers + 1));
                    this.world.neighborChanged(pos, Blocks.SNOW_LAYER, pos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (this.world.isAirBlock(pos)) {
            this.world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
            this.world.neighborChanged(pos, Blocks.SNOW_LAYER, pos);
        }
    }

    protected boolean extinguishFire(BlockPos pos, Block block) {
        if (!this.canModifyTerrain(pos, EnumFacing.UP)) {
            return false;
        }
        if (block == Blocks.FIRE) {
            this.world.setBlockToAir(pos);
            this.world.neighborChanged(pos, block, pos);
            return true;
        }
        return false;
    }

    protected void applyFireTerrainEffect(BlockPos hitBlock, BlockPos offsetBlock, EnumFacing impactSide) {
        this.meltSnowOrIce(hitBlock);

        if (this.placeFire(offsetBlock)) {
            return;
        }

        this.meltSnowOrIce(offsetBlock);
        if (this.placeFire(offsetBlock)) {
            return;
        }

        if (this.placeFire(hitBlock.up())) {
            return;
        }

        if ((impactSide != null) && impactSide.getAxis().isHorizontal()) {
            this.meltSnowOrIce(offsetBlock.up());
            this.placeFire(offsetBlock.up());
        }
    }

    protected boolean meltSnowOrIce(BlockPos pos) {
        if (!this.canModifyTerrain(pos, EnumFacing.UP)) {
            return false;
        }

        final IBlockState state = this.world.getBlockState(pos);
        final Block block = state.getBlock();
        if (block instanceof BlockIce || block == Blocks.PACKED_ICE || block == Blocks.FROSTED_ICE) {
            if (this.world.provider.doesWaterVaporize()) {
                this.world.setBlockToAir(pos);
            } else {
                this.world.setBlockState(pos, Blocks.WATER.getDefaultState());
                this.world.neighborChanged(pos, Blocks.WATER, pos);
            }
            return true;
        }
        if (block instanceof BlockSnow || block == Blocks.SNOW) {
            this.world.setBlockToAir(pos);
            this.world.neighborChanged(pos, block, pos);
            return true;
        }
        return false;
    }

    protected boolean placeFire(BlockPos pos) {
        if (!this.canModifyTerrain(pos, EnumFacing.UP)) {
            return false;
        }

        final IBlockState state = this.world.getBlockState(pos);
        if ((state.getMaterial() == Material.WATER) || (state.getMaterial() == Material.LAVA)) {
            return false;
        }
        if ((this.world.isAirBlock(pos) || state.getMaterial().isReplaceable()) && Blocks.FIRE.canPlaceBlockAt(this.world, pos)) {
            this.world.setBlockState(pos, Blocks.FIRE.getDefaultState());
            this.world.neighborChanged(pos, Blocks.FIRE, pos);
            return true;
        }
        return false;
    }

    private void applyElementalDamage(Entity target) {
        final EntityLivingBase shooter = this.getShootingEntity();
        if (shooter == null) {
            target.attackEntityFrom(this.createFallbackDamageSource(), this.getDamage());
            if (Elements.LIGHTNING.equals(this.element)) {
                target.setFire(1);
            } else if (!Elements.ICE.equals(this.element)) {
                target.setFire(5);
            }
            return;
        }

        if (Elements.LIGHTNING.equals(this.element)) {
            target.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.LIGHTNING_BOLT.damageType, this, shooter).setDamageBypassesArmor().setMagicDamage(), this.getDamage());
            target.setFire(1);
            return;
        }

        if (Elements.ICE.equals(this.element)) {
            target.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, this, shooter).setDamageBypassesArmor().setMagicDamage(), this.getDamage());
            return;
        }

        target.attackEntityFrom(new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, this, shooter).setDamageBypassesArmor().setFireDamage().setMagicDamage(), this.getDamage());
        target.setFire(5);
    }

    @Nullable
    private EntityLivingBase getShootingEntity() {
        if ((this.shootingEntity == null) && (this.shootingEntityUUID != null) && (this.world instanceof WorldServer)) {
            final Entity entity = ((WorldServer) this.world).getEntityFromUuid(this.shootingEntityUUID);
            if (entity instanceof EntityLivingBase) {
                this.shootingEntity = (EntityLivingBase) entity;
            }
        }
        return this.shootingEntity;
    }

    private DamageSource createFallbackDamageSource() {
        if (Elements.LIGHTNING.equals(this.element)) {
            return new EntityDamageSourceIndirect(DamageSource.LIGHTNING_BOLT.damageType, this, this).setDamageBypassesArmor().setMagicDamage();
        }
        if (Elements.ICE.equals(this.element)) {
            return new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, this, this).setDamageBypassesArmor().setMagicDamage();
        }
        return new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, this, this).setDamageBypassesArmor().setFireDamage().setMagicDamage();
    }

    private void applyConfiguredEffects(Entity target) {
        if (!(target instanceof EntityLivingBase)) {
            return;
        }

        final EntityLivingBase livingTarget = (EntityLivingBase) target;
        for (final String effectId : this.EFFECTS) {
            final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(effectId);
            if (potion.getPotion() != null) {
                livingTarget.addPotionEffect(potion.getPotionEffect());
            }
        }
    }

    private EnumFacing getImpactSide() {
        return EnumFacing.getFacingFromVector((float) this.motionX, (float) this.motionY, (float) this.motionZ).getOpposite();
    }

    /*
     * Vanilla overrides and persistence
     */

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        return !this.isEntityInvulnerable(source);
    }

    private void applyImpactEnchantments(Entity target) {
        if (!(this.shootingEntity instanceof EntityLivingBase) || !(target instanceof EntityLivingBase)) {
            return;
        }

        final EntityLivingBase shooter = this.shootingEntity;
        final EntityLivingBase livingTarget = (EntityLivingBase) target;
        EnchantmentHelper.applyThornEnchantments(livingTarget, shooter);
        EnchantmentHelper.applyArthropodEnchantments(shooter, livingTarget);
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
    protected void entityInit() {
        this.getDataManager().register(BREATH_COLOR, 12582912);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("BreathColor")) {
            this.setColor(compound.getInteger("BreathColor"));
        }
        if (compound.hasKey("Damage")) {
            this.damage = compound.getFloat("Damage");
        }
        if (compound.hasKey("ElementId")) {
            this.element = Element.getById(compound.getInteger("ElementId"));
            if (this.element == null) {
                this.element = Elements.NEUTRAL;
            }
        }
        if (compound.hasUniqueId("OwnerUUID")) {
            this.shootingEntityUUID = compound.getUniqueId("OwnerUUID");
        }
        if (compound.hasKey("OwnerName", 8)) {
            this.shootingEntityName = compound.getString("OwnerName");
        }
        this.ignoreBlocks = compound.getBoolean("IgnoreBlocks");
        this.interactWithTerrain = compound.getBoolean("TerrainInteraction");
        this.airDrag = compound.hasKey("AirDrag") ? compound.getFloat("AirDrag") : this.airDrag;
        this.waterDrag = compound.hasKey("WaterDrag") ? compound.getFloat("WaterDrag") : this.waterDrag;
        this.lavaDrag = compound.hasKey("LavaDrag") ? compound.getFloat("LavaDrag") : this.lavaDrag;
        this.gravityPerTick = compound.hasKey("GravityPerTick") ? compound.getFloat("GravityPerTick") : this.gravityPerTick;
        this.lifetimeTicks = compound.hasKey("LifetimeTicks") ? Math.max(1, compound.getInteger("LifetimeTicks")) : this.lifetimeTicks;
        this.maxLifetimeTicks = compound.hasKey("MaxLifetimeTicks") ? Math.max(1, compound.getInteger("MaxLifetimeTicks")) : this.maxLifetimeTicks;
        this.expireInWater = !compound.hasKey("ExpireInWater") || compound.getBoolean("ExpireInWater");
        this.expireInLava = !compound.hasKey("ExpireInLava") || compound.getBoolean("ExpireInLava");
        this.motionX = compound.getDouble("MotionX");
        this.motionY = compound.getDouble("MotionY");
        this.motionZ = compound.getDouble("MotionZ");

        if (compound.hasKey("Effects", 9)) {
            final NBTTagList list = compound.getTagList("Effects", 8);
            this.EFFECTS = new String[list.tagCount()];
            for (int i = 0; i < list.tagCount(); ++i) {
                this.EFFECTS[i] = list.getStringTagAt(i);
            }
        }
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        compound.setInteger("BreathColor", this.getColor());
        compound.setFloat("Damage", this.damage);
        compound.setInteger("ElementId", Element.getIdFromElement(this.element));
        if (this.shootingEntity != null) {
            compound.setUniqueId("OwnerUUID", this.shootingEntity.getUniqueID());
            compound.setString("OwnerName", this.shootingEntity.getName());
        } else if (this.shootingEntityUUID != null) {
            compound.setUniqueId("OwnerUUID", this.shootingEntityUUID);
            if (this.shootingEntityName != null) {
                compound.setString("OwnerName", this.shootingEntityName);
            }
        }
        compound.setBoolean("IgnoreBlocks", this.ignoreBlocks);
        compound.setBoolean("TerrainInteraction", this.interactWithTerrain);
        compound.setFloat("AirDrag", this.airDrag);
        compound.setFloat("WaterDrag", this.waterDrag);
        compound.setFloat("LavaDrag", this.lavaDrag);
        compound.setFloat("GravityPerTick", this.gravityPerTick);
        compound.setInteger("LifetimeTicks", this.lifetimeTicks);
        compound.setInteger("MaxLifetimeTicks", this.maxLifetimeTicks);
        compound.setBoolean("ExpireInWater", this.expireInWater);
        compound.setBoolean("ExpireInLava", this.expireInLava);
        compound.setDouble("MotionX", this.motionX);
        compound.setDouble("MotionY", this.motionY);
        compound.setDouble("MotionZ", this.motionZ);

        if (this.EFFECTS.length > 0) {
            final NBTTagList list = new NBTTagList();
            for (final String effect : this.EFFECTS) {
                list.appendTag(new NBTTagString(effect));
            }
            compound.setTag("Effects", list);
        }
    }
}
