package xzeroair.trinkets.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.BlockHelperUtil;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.TargetingPredicates;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class EntityRangedAttack extends Entity {

    private static final DataParameter<Integer> BREATH_COLOR = EntityDataManager.createKey(EntityRangedAttack.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> VISUAL_TYPE = EntityDataManager.createKey(EntityRangedAttack.class, DataSerializers.VARINT);
    private static final DataParameter<Float> VISUAL_ORIGIN_X = EntityDataManager.createKey(EntityRangedAttack.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> VISUAL_ORIGIN_Y = EntityDataManager.createKey(EntityRangedAttack.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> VISUAL_ORIGIN_Z = EntityDataManager.createKey(EntityRangedAttack.class, DataSerializers.FLOAT);
    private static final double CONTACT_STEP_EPSILON = 0.0001D;
    private static final float IMPACT_AREA_RADIUS = 3.0F;
    private static final float IMPACT_AREA_VERTICAL_RADIUS = 1.5F;
    private static final int IMPACT_AREA_DURATION = 80;
    private static final float IMPACT_AREA_MIN_RADIUS = 0.5F;
    private static final int MAX_ACTIVE_IMPACT_AREAS_PER_PLAYER = 3;
    private static final int IMPACT_AREA_PULSE_INTERVAL = 10;
    private static final int IMPACT_AREA_REAPPLICATION_DELAY = 20;
    private static final int FIRE_IMPACT_INITIAL_MAX_BLOCKS = 6;
    private static final int FIRE_IMPACT_DELAYED_ATTEMPT_INTERVAL = 20;
    private static final float FIRE_IMPACT_DELAYED_ATTEMPT_CHANCE = 0.7F;
    private static final ElementalImpactBehavior DEFAULT_IMPACT_BEHAVIOR = new DefaultImpactBehavior();
    private static final ElementalImpactBehavior FIRE_IMPACT_BEHAVIOR = new FireImpactBehavior();
    private static final ElementalImpactBehavior ICE_IMPACT_BEHAVIOR = new IceImpactBehavior();
    private static final ElementalImpactBehavior LIGHT_IMPACT_BEHAVIOR = new LightImpactBehavior();
    private static final ElementalImpactBehavior LIGHTNING_IMPACT_BEHAVIOR = new LightningImpactBehavior();

    public static final int VISUAL_DEFAULT = 0;
    public static final int VISUAL_FIRE = 1;
    public static final int VISUAL_ICE = 2;
    public static final int VISUAL_LIGHTNING = 3;
    public static final int VISUAL_LIGHTNING_BOLT = 4;
    public static final int VISUAL_LASER = 5;

    protected int color;
    protected Element element;

    public EntityLivingBase shootingEntity;
    @Nullable
    private UUID shootingEntityUUID;
    @Nullable
    private String shootingEntityName;
    private float damage;

    private boolean ignoreBlocks = false;
    private boolean passThroughCaster = true;
    private boolean chargeCreepers;
    private ImpactTemplate impactTemplate = ImpactTemplate.DEFAULT;
    protected boolean interactWithTerrain;
    protected boolean renderImpactAreaCircle;
    private final PhysicsTemplate physics;
    private final EnergyBehavior energy;
    private DefaultCollisionBehavior defaultCollisionBehavior = new DefaultCollisionBehavior();
    private OnCollisionBehavior collisionBehavior = this.defaultCollisionBehavior;
    private OnTerminationBehavior terminationBehavior = EntityRangedAttack::applyDefaultTermination;
    private boolean hasResolvedContact;
    private boolean terminated;

    protected String[] EFFECTS;

    public EntityRangedAttack(World world) {
        super(world);
        this.setSize(1F, 1F);
        this.color = 12582912;
        this.element = Elements.NEUTRAL;
        this.damage = 1.0F;
        this.interactWithTerrain = false;
        this.renderImpactAreaCircle = true;
        this.physics = new PhysicsTemplate();
        this.energy = new EnergyBehavior();
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

    public EntityRangedAttack setPassThroughCaster(boolean passThroughCaster) {
        this.passThroughCaster = passThroughCaster;
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

    public int getVisualType() {
        return this.getDataManager().get(VISUAL_TYPE);
    }

    public EntityRangedAttack setVisualType(int visualType) {
        if (!this.world.isRemote) {
            this.getDataManager().set(VISUAL_TYPE, visualType);
        }
        return this;
    }

    public EntityRangedAttack setVisualOrigin(Vec3d origin) {
        if (origin == null) {
            return this;
        }
        if (!this.world.isRemote) {
            this.getDataManager().set(VISUAL_ORIGIN_X, (float) origin.x);
            this.getDataManager().set(VISUAL_ORIGIN_Y, (float) origin.y);
            this.getDataManager().set(VISUAL_ORIGIN_Z, (float) origin.z);
        }
        return this;
    }

    public Vec3d getVisualOrigin() {
        return new Vec3d(this.getDataManager().get(VISUAL_ORIGIN_X), this.getDataManager()
                                                                         .get(VISUAL_ORIGIN_Y), this.getDataManager()
                                                                                                   .get(VISUAL_ORIGIN_Z));
    }

    public EntityRangedAttack setAirDrag(float airDrag) {
        this.physics.airDrag = airDrag;
        return this;
    }

    public EntityRangedAttack setWaterDrag(float waterDrag) {
        this.physics.waterDrag = waterDrag;
        return this;
    }

    public EntityRangedAttack setLavaDrag(float lavaDrag) {
        this.physics.lavaDrag = lavaDrag;
        return this;
    }

    public EntityRangedAttack setGravityPerTick(float gravityPerTick) {
        this.physics.gravityPerTick = gravityPerTick;
        return this;
    }

    public EntityRangedAttack setLifetimeTicks(int lifetimeTicks) {
        this.physics.lifetimeTicks = Math.max(1, lifetimeTicks);
        return this;
    }

    public EntityRangedAttack setMaxLifetimeTicks(int maxLifetimeTicks) {
        this.physics.maxLifetimeTicks = Math.max(1, maxLifetimeTicks);
        return this;
    }

    public EntityRangedAttack setExpireInWater(boolean expireInWater) {
        this.physics.expireInWater = expireInWater;
        return this;
    }

    public EntityRangedAttack setExpireInLava(boolean expireInLava) {
        this.physics.expireInLava = expireInLava;
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

    public static PhysicsTemplate arrowTemplate() {
        return new PhysicsTemplate().setGravity(0.05F);
    }

    public static PhysicsTemplate thrownTemplate() {
        return new PhysicsTemplate().setGravity(0.03F);
    }

    public static DefaultCollisionBehavior defaultCollisionBehavior() {
        return new DefaultCollisionBehavior();
    }

    public EntityRangedAttack setPhysicsTemplate(PhysicsTemplate template) {
        if (template != null) {
            this.physics.copyFrom(template);
        }
        return this;
    }

    public EntityRangedAttack setDefaultCollisionBehavior(DefaultCollisionBehavior collisionBehavior) {
        this.defaultCollisionBehavior = collisionBehavior == null ? new DefaultCollisionBehavior() : collisionBehavior.copy();
        this.collisionBehavior = this.defaultCollisionBehavior;
        return this;
    }

    public EntityRangedAttack setOnCollisionBehavior(OnCollisionBehavior collisionBehavior) {
        if (collisionBehavior instanceof DefaultCollisionBehavior) {
            return this.setDefaultCollisionBehavior((DefaultCollisionBehavior) collisionBehavior);
        }
        if (collisionBehavior == null) {
            this.collisionBehavior = this.defaultCollisionBehavior;
        } else {
            this.defaultCollisionBehavior = new DefaultCollisionBehavior();
            this.collisionBehavior = collisionBehavior;
        }
        return this;
    }

    public EntityRangedAttack setOnTerminationBehavior(OnTerminationBehavior terminationBehavior) {
        this.terminationBehavior = terminationBehavior == null ? EntityRangedAttack::applyDefaultTermination : terminationBehavior;
        return this;
    }

    public EntityRangedAttack setEnergyDecayPerTick(float decayPerTick) {
        this.energy.decayPerTick = Math.max(0.0F, decayPerTick);
        if (this.energy.decayPerTick > 0.0F) {
            this.energy.enabled = true;
        }
        return this;
    }

    public EntityRangedAttack setEnergyEnabled(boolean enabled) {
        this.energy.enabled = enabled;
        return this;
    }

    public EntityRangedAttack setEnergyFizzleThreshold(float threshold) {
        this.energy.fizzleThreshold = Math.max(0.0F, threshold);
        return this;
    }

    public EntityRangedAttack setEnergyDamageScaling(boolean enabled) {
        this.energy.scaleDamage = enabled;
        if (enabled) {
            this.energy.enabled = true;
        }
        return this;
    }

    public float getEnergy() {
        return this.energy.remainingEnergy;
    }

    public float getInitialEnergy() {
        return this.energy.initialEnergy;
    }

    public float consumeEnergy(float amount) {
        return this.energy.consume(this, amount);
    }

    public EntityRangedAttack setElement(Element element) {
        if (element == null) {
            this.element = Elements.NEUTRAL;
        } else {
            this.element = element;
        }
        if (!this.world.isRemote) {
            this.getDataManager().set(VISUAL_TYPE, this.getElementVisualType(this.element));
        }
        return this;
    }

    private int getElementVisualType(Element element) {
        if (Elements.FIRE.equals(element)) {
            return VISUAL_FIRE;
        }
        if (Elements.ICE.equals(element)) {
            return VISUAL_ICE;
        }
        if (Elements.LIGHTNING.equals(element)) {
            return VISUAL_LIGHTNING;
        }
        return VISUAL_DEFAULT;
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

    public EntityRangedAttack setRenderImpactAreaCircle(boolean renderImpactAreaCircle) {
        this.renderImpactAreaCircle = renderImpactAreaCircle;
        return this;
    }


    public EntityRangedAttack setChargeCreepers(boolean chargeCreepers) {
        this.chargeCreepers = chargeCreepers;
        return this;
    }

    public EntityRangedAttack setImpactTemplate(ImpactTemplate impactTemplate) {
        this.impactTemplate = impactTemplate == null ? ImpactTemplate.DEFAULT : impactTemplate;
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
        this.energy.captureInitial(this);
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
                this.terminate(TerminationReason.OWNER_INVALID, null);
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

        if (!this.world.isRemote && !this.resolveImpacts()) {
            return;
        }

        this.moveProjectile();
        this.updateRotationFromMotion();
        this.applyMotionDecay();

        this.setPosition(this.posX, this.posY, this.posZ);

        this.terminateIfExpired();
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
            this.terminate(TerminationReason.TERRAIN_INTERACTION, null);
            return false;
        }

        if (this.isInsideOfMaterial(Material.LAVA) || this.isInLava()) {
            if (!this.canModifyTerrain(pos, EnumFacing.UP)) {
                return true;
            }
            BlockHelperUtil.freezeLava(this.world, this.posX, this.posY, this.posZ, 0, 1.0D);
            this.terminate(TerminationReason.TERRAIN_INTERACTION, null);
            return false;
        }

        return true;
    }

    protected boolean canModifyTerrain(BlockPos pos, EnumFacing side) {
        final EntityLivingBase shooter = this.getShootingEntity();
        return !(shooter instanceof EntityPlayer) || ((EntityPlayer) shooter).canPlayerEdit(pos, side, ItemStack.EMPTY);
    }

    private boolean resolveImpacts() {
        final Vec3d end = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        final Set<UUID> contactedEntities = new HashSet<>();
        final boolean pvpEnabled = this.isPvpEnabled();
        Vec3d cursor = new Vec3d(this.posX, this.posY, this.posZ);

        while (cursor.squareDistanceTo(end) > (CONTACT_STEP_EPSILON * CONTACT_STEP_EPSILON)) {
            final RayTraceResult blockHit = this.ignoreBlocks ? null : this.world.rayTraceBlocks(cursor, end);
            final Vec3d entityEnd = blockHit == null ? end : blockHit.hitVec;
            for (final RayTraceResult hit : this.findEntityContacts(cursor, entityEnd, contactedEntities, pvpEnabled)) {
                if (!this.handleImpact(hit, this.physics.entityCollisionResponse, contactedEntities, pvpEnabled)) {
                    return false;
                }
            }

            if (blockHit == null) {
                return true;
            }
            if ((blockHit.typeOfHit == Type.BLOCK) && (this.world.getBlockState(blockHit.getBlockPos())
                                                                 .getBlock() == Blocks.PORTAL)) {
                this.setPortal(blockHit.getBlockPos());
                return true;
            }
            final ContactResponse response = this.physics.getBlockCollisionResponse(this, blockHit);
            if (!this.handleImpact(blockHit, response, contactedEntities, pvpEnabled)) {
                return false;
            }
            if (response != ContactResponse.PASS_THROUGH || blockHit.hitVec == null) {
                return true;
            }

            final Vec3d direction = end.subtract(cursor).normalize();
            if (direction.lengthSquared() <= (CONTACT_STEP_EPSILON * CONTACT_STEP_EPSILON)) {
                return true;
            }
            cursor = blockHit.hitVec.add(direction.scale(CONTACT_STEP_EPSILON));
        }
        return true;
    }

    private List<RayTraceResult> findEntityContacts(Vec3d start, Vec3d end, Set<UUID> contactedEntities, boolean pvpEnabled) {
        final List<RayTraceResult> contacts = new ArrayList<>();
        final List<Entity> candidates = this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(start, end).grow(1.0D), entity -> entity != null);
        for (final Entity candidate : candidates) {
            if (!this.canContactEntity(candidate, contactedEntities, pvpEnabled)) {
                continue;
            }
            final RayTraceResult hit = candidate.getEntityBoundingBox()
                                                .grow(candidate.getCollisionBorderSize())
                                                .calculateIntercept(start, end);
            if (hit != null) {
                contacts.add(new RayTraceResult(candidate, hit.hitVec));
            }
        }
        contacts.sort(Comparator.comparingDouble(hit -> start.squareDistanceTo(hit.hitVec)));
        return contacts;
    }

    private boolean canContactEntity(Entity candidate, Set<UUID> contactedEntities, boolean pvpEnabled) {
        return TargetingPredicates.isCombatTarget(candidate, pvpEnabled) && (!this.passThroughCaster || candidate != this.getShootingEntity()) && !(candidate instanceof EntityRangedAttack) && !contactedEntities.contains(candidate.getUniqueID());
    }

    private boolean handleImpact(RayTraceResult hit, ContactResponse response, Set<UUID> contactedEntities, boolean pvpEnabled) {
        if (net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hit)) {
            return true;
        }
        if (!this.world.isRemote) {
            if (!this.hasResolvedContact) {
                this.collisionBehavior.onFirstContact(this, hit);
            }
            this.collisionBehavior.onCollision(this, hit, pvpEnabled);
        }
        this.hasResolvedContact = true;
        if (this.energy.shouldFizzle()) {
            if (!this.world.isRemote) {
                this.collisionBehavior.onLastContact(this, hit);
            }
            this.terminate(TerminationReason.ENERGY_FIZZLE, hit);
            return false;
        }
        if (response == ContactResponse.PASS_THROUGH) {
            if (hit.entityHit != null) {
                contactedEntities.add(hit.entityHit.getUniqueID());
            }
            return true;
        }
        if (!this.world.isRemote) {
            this.collisionBehavior.onLastContact(this, hit);
        }
        this.terminate(hit.typeOfHit == Type.ENTITY ? TerminationReason.ENTITY_CONTACT : TerminationReason.BLOCK_CONTACT, hit);
        return false;
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
        final float drag = this.physics.getDrag(this);

        if (this.isInWater()) {
            for (int j = 0; j < 4; ++j) {
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - (this.motionX * 0.25D), this.posY - (this.motionY * 0.25D), this.posZ - (this.motionZ * 0.25D), this.motionX, this.motionY, this.motionZ);
            }
        }

        this.motionX *= drag;
        this.motionY *= drag;
        this.motionZ *= drag;

        if (!this.hasNoGravity()) {
            this.motionY -= this.physics.gravityPerTick;
        }
        this.energy.applyDecay(this);
    }

    private void terminateIfExpired() {
        if (this.energy.shouldFizzle()) {
            this.terminate(TerminationReason.ENERGY_FIZZLE, null);
        } else if ((this.ticksExisted >= this.physics.lifetimeTicks) || (this.ticksExisted >= this.physics.maxLifetimeTicks)) {
            this.terminate(TerminationReason.LIFETIME_EXPIRED, null);
        } else if (this.physics.expireInWater && this.isInWater()) {
            this.terminate(TerminationReason.WATER_EXPIRY, null);
        } else if (this.physics.expireInLava && (this.isInLava() || this.isInsideOfMaterial(Material.LAVA))) {
            this.terminate(TerminationReason.LAVA_EXPIRY, null);
        }
    }

    private void terminate(TerminationReason reason, @Nullable RayTraceResult hit) {
        if (this.terminated || this.isDead) {
            return;
        }
        this.terminated = true;
        if (!this.world.isRemote) {
            final Vec3d location = (hit == null || hit.hitVec == null) ? new Vec3d(this.posX, this.posY, this.posZ) : hit.hitVec;
            this.terminationBehavior.onTermination(this, reason, hit, location);
        }
        this.setDead();
    }

    private void resetMotion() {
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    private boolean isInOtherFluid() {
        final AxisAlignedBB bounds = this.getEntityBoundingBox();
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = MathHelper.floor(bounds.minX); x <= MathHelper.floor(bounds.maxX); ++x) {
            for (int y = MathHelper.floor(bounds.minY); y <= MathHelper.floor(bounds.maxY); ++y) {
                for (int z = MathHelper.floor(bounds.minZ); z <= MathHelper.floor(bounds.maxZ); ++z) {
                    final Material material = this.world.getBlockState(pos.setPos(x, y, z)).getMaterial();
                    if (material.isLiquid() && material != Material.WATER && material != Material.LAVA) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Vec3d createHeadingVector(double x, double y, double z, float inaccuracy) {
        Vec3d heading = this.normalizeVector(x, y, z);
        if (inaccuracy > 0.0F) {
            heading = heading.add(this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy, this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy, this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy);
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
        this.energy.captureInitial(this);
    }


    /*
     * Hit dispatch
     */

    protected void onHit(RayTraceResult movingObject) {
        if ((this.world == null) || this.world.isRemote || movingObject.entityHit instanceof EntityRangedAttack) {
            return;
        }
        this.applyDefaultCollision(movingObject, this.isPvpEnabled());
    }

    private void applyDefaultCollision(RayTraceResult movingObject, boolean pvpEnabled) {
        if (movingObject.typeOfHit == Type.ENTITY && movingObject.entityHit != null) {
            this.handleEntityHit(movingObject, pvpEnabled);
        }
    }

    private void applyDefaultTermination(TerminationReason reason, @Nullable RayTraceResult hit, Vec3d location) {
        final ElementalImpactBehavior impactBehavior = this.getElementalImpactBehavior();
        if (reason == TerminationReason.BLOCK_CONTACT) {
            impactBehavior.applyBlockImpact(this, hit);
        }
        if ((reason == TerminationReason.BLOCK_CONTACT) || (reason == TerminationReason.TERRAIN_INTERACTION) || ((reason == TerminationReason.ENTITY_CONTACT) && impactBehavior.hasEntityImpact())) {
            this.spawnImpactArea(location);
        }
    }

    private ElementalImpactBehavior getElementalImpactBehavior() {
        if (Elements.FIRE.equals(this.element)) {
            return FIRE_IMPACT_BEHAVIOR;
        }
        if (Elements.ICE.equals(this.element)) {
            return ICE_IMPACT_BEHAVIOR;
        }
        if (Elements.LIGHT.equals(this.element)) {
            return LIGHT_IMPACT_BEHAVIOR;
        }
        if (Elements.LIGHTNING.equals(this.element)) {
            return LIGHTNING_IMPACT_BEHAVIOR;
        }
        return DEFAULT_IMPACT_BEHAVIOR;
    }

    private void spawnImpactArea(Vec3d impact) {
        final EntityAreaEffect area = new EntityAreaEffect(this.world, impact.x, impact.y, impact.z);
        area.setOwner(this.getShootingEntity());
        area.setBreathImpact(true);
        area.setRadius(IMPACT_AREA_RADIUS);
        area.setVerticalRadius(IMPACT_AREA_VERTICAL_RADIUS);
        area.setWaitTime(0);
        area.setDuration(IMPACT_AREA_DURATION);
        area.setRadiusPerTick(-(IMPACT_AREA_RADIUS - IMPACT_AREA_MIN_RADIUS) / (float) IMPACT_AREA_DURATION);
        area.setPulseInterval(IMPACT_AREA_PULSE_INTERVAL);
        area.setReapplicationDelay(IMPACT_AREA_REAPPLICATION_DELAY);
        area.setColor(this.color);
        area.setRenderCircle(this.renderImpactAreaCircle);
        area.setTargetProfile(EntityAreaEffect.TargetProfile.COMBAT);

        this.getElementalImpactBehavior().configureArea(this, area);

        for (final String effectId : this.EFFECTS) {
            final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(effectId);
            if (potion.getPotion() != null) {
                area.addAction(new EntityAreaEffect.PotionAreaAction(potion.getPotionEffect()));
            }
        }


        if (!area.getActions().isEmpty()) {
            this.enforceImpactAreaLimit(area);
            this.world.spawnEntity(area);
        }
    }

    private void enforceImpactAreaLimit(EntityAreaEffect newArea) {
        final EntityLivingBase owner = newArea.getOwner();
        if (!(owner instanceof EntityPlayer)) {
            return;
        }

        final List<EntityAreaEffect> activeAreas = this.world.getEntities(EntityAreaEffect.class, area -> area != null && !area.isDead && area.isBreathImpact() && area.isOwnedBy(owner.getUniqueID()));
        while (activeAreas.size() >= MAX_ACTIVE_IMPACT_AREAS_PER_PLAYER) {
            EntityAreaEffect oldest = null;
            for (EntityAreaEffect area : activeAreas) {
                if (oldest == null || area.ticksExisted > oldest.ticksExisted) {
                    oldest = area;
                }
            }
            if (oldest == null) {
                return;
            }
            oldest.setDead();
            activeAreas.remove(oldest);
        }
    }

    private void handleEntityHit(RayTraceResult hit, boolean pvpEnabled) {
        final Entity directHit = hit.entityHit;
        if (directHit == null || hit.hitVec == null) {
            return;
        }

        this.applyHitToTarget(directHit, pvpEnabled);
        final AxisAlignedBB splashBounds = new AxisAlignedBB(hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, hit.hitVec.x, hit.hitVec.y, hit.hitVec.z).grow(1.0D);
        final Predicate<Entity> targets = Predicates.and(TargetingPredicates.combat(pvpEnabled), ent -> (ent != this.shootingEntity) && (ent != directHit) && !(ent instanceof EntityRangedAttack) && !ent.isImmuneToFire());
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
        if (!TargetingPredicates.isCombatTarget(target, pvpEnabled) || target == this.shootingEntity || target instanceof EntityRangedAttack) {
            return false;
        }

        return !(target instanceof EntityPlayer) || pvpEnabled;
    }

    private boolean isPvpEnabled() {
        try {
            MinecraftServer server = this.world.getMinecraftServer();
            if ((server == null) && (this.shootingEntity instanceof EntityPlayerMP)) {
                server = this.shootingEntity.getServer();
            }
            return (server != null) && server.isPVPEnabled();
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void applyElementalDamage(Entity target) {
        this.getElementalImpactBehavior().applyDirectDamage(this, target);
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

    @Override
    protected void entityInit() {
        this.getDataManager().register(BREATH_COLOR, 12582912);
        this.getDataManager().register(VISUAL_TYPE, VISUAL_DEFAULT);
        this.getDataManager().register(VISUAL_ORIGIN_X, 0.0F);
        this.getDataManager().register(VISUAL_ORIGIN_Y, 0.0F);
        this.getDataManager().register(VISUAL_ORIGIN_Z, 0.0F);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        NBTHelper.hasInteger(compound, "BreathColor", this::setColor);
        NBTHelper.hasDouble(compound, "VisualOriginX", originX -> NBTHelper.hasDouble(compound, "VisualOriginY", originY -> NBTHelper.hasDouble(compound, "VisualOriginZ", originZ -> this.setVisualOrigin(new Vec3d(originX, originY, originZ)))));
        NBTHelper.hasFloat(compound, "Damage", value -> this.damage = value);
        NBTHelper.hasInteger(compound, "ElementId", value -> this.setElement(Element.getById(value)));
        NBTHelper.hasInteger(compound, "VisualType", this::setVisualType);
        NBTHelper.hasUUID(compound, "OwnerUUID", value -> this.shootingEntityUUID = value);
        NBTHelper.hasString(compound, "OwnerName", value -> this.shootingEntityName = value);
        NBTHelper.hasBoolean(compound, "IgnoreBlocks", value -> this.ignoreBlocks = value);
        NBTHelper.hasBoolean(compound, "PassThroughCaster", value -> this.passThroughCaster = value);
        NBTHelper.hasBoolean(compound, "ChargeCreepers", value -> this.chargeCreepers = value);
        NBTHelper.hasInteger(compound, "ImpactTemplate", value -> this.impactTemplate = ImpactTemplate.values()[MathHelper.clamp(value, 0, ImpactTemplate.values().length - 1)]);
        NBTHelper.hasBoolean(compound, "TerrainInteraction", value -> this.interactWithTerrain = value);
        NBTHelper.hasBoolean(compound, "HasResolvedContact", value -> this.hasResolvedContact = value);
        this.physics.readFromNBT(compound);
        this.energy.readFromNBT(compound);
        this.defaultCollisionBehavior.readFromNBT(compound);
        this.collisionBehavior = this.defaultCollisionBehavior;
        NBTHelper.hasDouble(compound, "MotionX", value -> this.motionX = value);
        NBTHelper.hasDouble(compound, "MotionY", value -> this.motionY = value);
        NBTHelper.hasDouble(compound, "MotionZ", value -> this.motionZ = value);
        NBTHelper.hasTagList(compound, "Effects", list -> {
            this.EFFECTS = new String[list.tagCount()];
            for (int i = 0; i < list.tagCount(); ++i) {
                this.EFFECTS[i] = list.getStringTagAt(i);
            }
        });
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        compound.setInteger("BreathColor", this.getColor());
        final Vec3d origin = this.getVisualOrigin();
        compound.setInteger("VisualType", this.getVisualType());
        compound.setDouble("VisualOriginX", origin.x);
        compound.setDouble("VisualOriginY", origin.y);
        compound.setDouble("VisualOriginZ", origin.z);
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
        compound.setBoolean("PassThroughCaster", this.passThroughCaster);
        compound.setBoolean("ChargeCreepers", this.chargeCreepers);
        compound.setInteger("ImpactTemplate", this.impactTemplate.ordinal());
        compound.setBoolean("TerrainInteraction", this.interactWithTerrain);
        compound.setBoolean("HasResolvedContact", this.hasResolvedContact);
        this.physics.writeToNBT(compound);
        this.energy.writeToNBT(compound);
        this.defaultCollisionBehavior.writeToNBT(compound);
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

    public enum ContactResponse {
        STOP, PASS_THROUGH
    }

    public enum TransparentBlockProfile {
        NONE, NON_OPAQUE
    }

    public enum TerminationReason {
        ENTITY_CONTACT, BLOCK_CONTACT, TERRAIN_INTERACTION, ENERGY_FIZZLE, LIFETIME_EXPIRED, WATER_EXPIRY, LAVA_EXPIRY, OWNER_INVALID
    }

    public enum ImpactTemplate {
        DEFAULT, LIGHTNING_BOLT
    }

    public interface OnCollisionBehavior {
        void onCollision(EntityRangedAttack projectile, RayTraceResult hit);

        default void onCollision(EntityRangedAttack projectile, RayTraceResult hit, boolean pvpEnabled) {
            this.onCollision(projectile, hit);
        }

        default void onFirstContact(EntityRangedAttack projectile, RayTraceResult hit) {
        }

        default void onLastContact(EntityRangedAttack projectile, RayTraceResult hit) {
        }
    }

    public interface OnTerminationBehavior {
        void onTermination(EntityRangedAttack projectile, TerminationReason reason, @Nullable RayTraceResult hit, Vec3d location);
    }

    public static class DefaultCollisionBehavior implements OnCollisionBehavior {

        private float entityContactEnergyCost;
        private float blockContactEnergyCost;
        private float transparentBlockEnergyCost;

        public DefaultCollisionBehavior setEntityContactEnergyCost(float cost) {
            this.entityContactEnergyCost = Math.max(0.0F, cost);
            return this;
        }

        public DefaultCollisionBehavior setBlockContactEnergyCost(float cost) {
            this.blockContactEnergyCost = Math.max(0.0F, cost);
            return this;
        }

        public DefaultCollisionBehavior setTransparentBlockEnergyCost(float cost) {
            this.transparentBlockEnergyCost = Math.max(0.0F, cost);
            return this;
        }

        @Override
        public void onCollision(EntityRangedAttack projectile, RayTraceResult hit) {
            this.applyCollision(projectile, hit, projectile.isPvpEnabled());
        }

        @Override
        public void onCollision(EntityRangedAttack projectile, RayTraceResult hit, boolean pvpEnabled) {
            this.applyCollision(projectile, hit, pvpEnabled);
        }

        private void applyCollision(EntityRangedAttack projectile, RayTraceResult hit, boolean pvpEnabled) {
            projectile.applyDefaultCollision(hit, pvpEnabled);
            if (hit.typeOfHit == Type.ENTITY) {
                projectile.consumeEnergy(this.entityContactEnergyCost);
            } else if (hit.typeOfHit == Type.BLOCK) {
                projectile.consumeEnergy(projectile.physics.isTransparentBlockPassThrough(projectile, hit) ? this.transparentBlockEnergyCost : this.blockContactEnergyCost);
            }
        }

        private DefaultCollisionBehavior copy() {
            return new DefaultCollisionBehavior().setEntityContactEnergyCost(this.entityContactEnergyCost)
                                                 .setBlockContactEnergyCost(this.blockContactEnergyCost)
                                                 .setTransparentBlockEnergyCost(this.transparentBlockEnergyCost);
        }

        private void readFromNBT(NBTTagCompound compound) {
            NBTHelper.hasFloat(compound, "EntityContactEnergyCost", value -> this.entityContactEnergyCost = Math.max(0.0F, value));
            NBTHelper.hasFloat(compound, "BlockContactEnergyCost", value -> this.blockContactEnergyCost = Math.max(0.0F, value));
            NBTHelper.hasFloat(compound, "TransparentBlockEnergyCost", value -> this.transparentBlockEnergyCost = Math.max(0.0F, value));
        }

        private void writeToNBT(NBTTagCompound compound) {
            compound.setFloat("EntityContactEnergyCost", this.entityContactEnergyCost);
            compound.setFloat("BlockContactEnergyCost", this.blockContactEnergyCost);
            compound.setFloat("TransparentBlockEnergyCost", this.transparentBlockEnergyCost);
        }
    }

    private static class ElementalImpactBehavior {

        void configureArea(EntityRangedAttack projectile, EntityAreaEffect area) {
            area.setWaterBehavior(EntityAreaEffect.LiquidBehavior.EXPIRE);
            area.setLavaBehavior(EntityAreaEffect.LiquidBehavior.EXPIRE);
            if (Elements.NEUTRAL.equals(projectile.element)) {
                area.addAction(new EntityAreaEffect.DamageAreaAction(projectile.getDamage() * 0.1F));
            }
        }

        void applyBlockImpact(EntityRangedAttack projectile, @Nullable RayTraceResult hit) {
        }

        boolean hasEntityImpact() {
            return false;
        }

        void applyDirectDamage(EntityRangedAttack projectile, Entity target) {
            target.attackEntityFrom(this.createDragonDamageSource(projectile, true), projectile.energy.getImpactDamage(projectile.damage));
            target.setFire(5);
        }

        protected DamageSource createDragonDamageSource(EntityRangedAttack projectile, boolean fireDamage) {
            final EntityLivingBase shooter = projectile.getShootingEntity();
            final EntityDamageSourceIndirect source = new EntityDamageSourceIndirect(DamageSource.DRAGON_BREATH.damageType, projectile, shooter == null ? projectile : shooter);
            source.setDamageBypassesArmor();
            if (fireDamage) {
                source.setFireDamage();
            }
            return source.setMagicDamage();
        }
    }

    private static class DefaultImpactBehavior extends ElementalImpactBehavior {
    }

    private static class FireImpactBehavior extends ElementalImpactBehavior {

        @Override
        void configureArea(EntityRangedAttack projectile, EntityAreaEffect area) {
            super.configureArea(projectile, area);
            area.setLavaBehavior(EntityAreaEffect.LiquidBehavior.FLOAT);
            area.setParticle(EnumParticleTypes.FLAME);
            if (projectile.interactWithTerrain) {
                area.addAction(new EntityAreaEffect.PlaceFireAreaAction(FIRE_IMPACT_INITIAL_MAX_BLOCKS, FIRE_IMPACT_DELAYED_ATTEMPT_INTERVAL, FIRE_IMPACT_DELAYED_ATTEMPT_CHANCE));
            }
        }

        @Override
        boolean hasEntityImpact() {
            return true;
        }
    }

    private static class IceImpactBehavior extends ElementalImpactBehavior {

        @Override
        void configureArea(EntityRangedAttack projectile, EntityAreaEffect area) {
            super.configureArea(projectile, area);
            area.setWaterBehavior(EntityAreaEffect.LiquidBehavior.FLOAT);
            area.setParticle(EnumParticleTypes.SNOW_SHOVEL);
            if (projectile.interactWithTerrain) {
                area.addAction(new EntityAreaEffect.FreezeLiquidAreaAction(4));
                area.addAction(new EntityAreaEffect.PlaceSnowAreaAction(1));
            }
        }

        @Override
        boolean hasEntityImpact() {
            return false;
        }

        @Override
        void applyDirectDamage(EntityRangedAttack projectile, Entity target) {
            target.attackEntityFrom(this.createDragonDamageSource(projectile, false), projectile.energy.getImpactDamage(projectile.damage));
        }
    }

    private static class LightImpactBehavior extends ElementalImpactBehavior {

        @Override
        void applyDirectDamage(EntityRangedAttack projectile, Entity target) {
            target.attackEntityFrom(this.createDragonDamageSource(projectile, false), projectile.energy.getImpactDamage(projectile.damage));
        }

        @Override
        void applyBlockImpact(EntityRangedAttack projectile, @Nullable RayTraceResult hit) {
            if (!projectile.interactWithTerrain || hit == null || hit.getBlockPos() == null) {
                return;
            }
            final BlockPos pos = hit.getBlockPos();
            if (!projectile.world.getGameRules()
                                 .getBoolean(Reference.MINECRAFT_GAMERULE_MOBGRIEFING) || !projectile.canModifyTerrain(pos, EnumFacing.UP)) {
                return;
            }
            final IBlockState state = projectile.world.getBlockState(pos);
            final Block block = state.getBlock();
            if (state.getMaterial() == Material.WATER) {
                projectile.world.setBlockToAir(pos);
            } else if (block instanceof BlockIce || block == Blocks.PACKED_ICE || block == Blocks.FROSTED_ICE) {
                projectile.world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
            } else if (block instanceof BlockSand) {
                projectile.world.setBlockState(pos, Blocks.GLASS.getDefaultState(), 3);
            } else if (block == Blocks.STONE || block == Blocks.COBBLESTONE) {
                ModBlocks.Placeables.TEMP_BLOCK.setHeatedStone(projectile.world, pos);
            } else if (block == Blocks.GRASS) {
                projectile.world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 3);
            }
        }
    }

    private static class LightningImpactBehavior extends ElementalImpactBehavior {

        @Override
        void configureArea(EntityRangedAttack projectile, EntityAreaEffect area) {
            super.configureArea(projectile, area);
            if (projectile.impactTemplate == ImpactTemplate.LIGHTNING_BOLT) {
                area.setRadius(2.5F);
                area.setDuration(20);
                area.setRadiusPerTick(-0.1F);
            }
            area.setParticle(EnumParticleTypes.CRIT_MAGIC);
            area.addAction(new EntityAreaEffect.DamageAreaAction(projectile.getDamage() * 0.1F));
        }

        @Override
        boolean hasEntityImpact() {
            return true;
        }

        @Override
        void applyDirectDamage(EntityRangedAttack projectile, Entity target) {
            final EntityLivingBase shooter = projectile.getShootingEntity();
            final EntityDamageSourceIndirect source = new EntityDamageSourceIndirect(DamageSource.LIGHTNING_BOLT.damageType, projectile, shooter == null ? projectile : shooter);
            source.setDamageBypassesArmor();
            source.setMagicDamage();
            target.attackEntityFrom(source, projectile.energy.getImpactDamage(projectile.damage));
            target.setFire(shooter == null ? 2 : 1);
            if (projectile.chargeCreepers && (target instanceof EntityCreeper)) {
                final EntityCreeper creeper = (EntityCreeper) target;
                creeper.onStruckByLightning(new EntityLightningBolt(projectile.world, creeper.posX, creeper.posY, creeper.posZ, true));
                creeper.extinguish();
            }
        }
    }

    public static class PhysicsTemplate {

        private float airDrag = 0.99F;
        private float waterDrag = 0.6F;
        private float lavaDrag = 0.5F;
        private float gravityPerTick = 0.03F;
        private int lifetimeTicks = 30;
        private int maxLifetimeTicks = 2400;
        private boolean expireInWater = true;
        private boolean expireInLava = true;
        private ContactResponse entityCollisionResponse = ContactResponse.STOP;
        private ContactResponse blockCollisionResponse = ContactResponse.STOP;
        private TransparentBlockProfile transparentBlockProfile = TransparentBlockProfile.NONE;

        public PhysicsTemplate setGravity(float gravityPerTick) {
            this.gravityPerTick = gravityPerTick;
            return this;
        }

        public PhysicsTemplate setDrag(Material material, float drag) {
            if (material == Material.AIR) {
                this.airDrag = drag;
            } else if (material == Material.WATER) {
                this.waterDrag = drag;
            } else if (material == Material.LAVA) {
                this.lavaDrag = drag;
            } else {
                Trinkets.LOGGER.warn("Ignoring unsupported projectile drag material: {}", material);
            }
            return this;
        }

        public PhysicsTemplate setEntityCollisionResponse(ContactResponse response) {
            this.entityCollisionResponse = response == null ? ContactResponse.STOP : response;
            return this;
        }

        public PhysicsTemplate setBlockCollisionResponse(ContactResponse response) {
            this.blockCollisionResponse = response == null ? ContactResponse.STOP : response;
            return this;
        }

        public PhysicsTemplate setTransparentBlockProfile(TransparentBlockProfile profile) {
            this.transparentBlockProfile = profile == null ? TransparentBlockProfile.NONE : profile;
            return this;
        }



        public PhysicsTemplate setLifetimeTicks(int lifetimeTicks) {
            this.lifetimeTicks = Math.max(1, lifetimeTicks);
            return this;
        }

        public PhysicsTemplate setMaxLifetimeTicks(int maxLifetimeTicks) {
            this.maxLifetimeTicks = Math.max(1, maxLifetimeTicks);
            return this;
        }

        public PhysicsTemplate setExpireInWater(boolean expireInWater) {
            this.expireInWater = expireInWater;
            return this;
        }

        public PhysicsTemplate setExpireInLava(boolean expireInLava) {
            this.expireInLava = expireInLava;
            return this;
        }

        private void copyFrom(PhysicsTemplate template) {
            this.airDrag = template.airDrag;
            this.waterDrag = template.waterDrag;
            this.lavaDrag = template.lavaDrag;
            this.gravityPerTick = template.gravityPerTick;
            this.lifetimeTicks = template.lifetimeTicks;
            this.maxLifetimeTicks = template.maxLifetimeTicks;
            this.expireInWater = template.expireInWater;
            this.expireInLava = template.expireInLava;
            this.entityCollisionResponse = template.entityCollisionResponse;
            this.blockCollisionResponse = template.blockCollisionResponse;
            this.transparentBlockProfile = template.transparentBlockProfile;
        }

        private ContactResponse getBlockCollisionResponse(EntityRangedAttack projectile, RayTraceResult hit) {
            if (this.blockCollisionResponse == ContactResponse.PASS_THROUGH || this.isTransparentBlockPassThrough(projectile, hit)) {
                return ContactResponse.PASS_THROUGH;
            }
            return ContactResponse.STOP;
        }

        private boolean isTransparentBlockPassThrough(EntityRangedAttack projectile, RayTraceResult hit) {
            if (this.transparentBlockProfile == TransparentBlockProfile.NONE || hit == null || hit.typeOfHit != Type.BLOCK || hit.getBlockPos() == null) {
                return false;
            }
            return !projectile.world.getBlockState(hit.getBlockPos()).getMaterial().isOpaque();
        }

        private float getDrag(EntityRangedAttack projectile) {
            if (projectile.isInWater()) {
                return this.waterDrag;
            }
            if (projectile.isInLava() || projectile.isInsideOfMaterial(Material.LAVA)) {
                return this.lavaDrag;
            }
            return projectile.isInOtherFluid() ? this.waterDrag : this.airDrag;
        }

        private void readFromNBT(NBTTagCompound compound) {
            NBTHelper.hasFloat(compound, "AirDrag", value -> this.airDrag = value);
            NBTHelper.hasFloat(compound, "WaterDrag", value -> this.waterDrag = value);
            NBTHelper.hasFloat(compound, "LavaDrag", value -> this.lavaDrag = value);
            NBTHelper.hasFloat(compound, "GravityPerTick", value -> this.gravityPerTick = value);
            NBTHelper.hasInteger(compound, "LifetimeTicks", value -> this.lifetimeTicks = Math.max(1, value));
            NBTHelper.hasInteger(compound, "MaxLifetimeTicks", value -> this.maxLifetimeTicks = Math.max(1, value));
            NBTHelper.hasBoolean(compound, "ExpireInWater", value -> this.expireInWater = value);
            NBTHelper.hasBoolean(compound, "ExpireInLava", value -> this.expireInLava = value);
            NBTHelper.hasInteger(compound, "EntityCollisionResponse", value -> this.entityCollisionResponse = ContactResponse.values()[MathHelper.clamp(value, 0, ContactResponse.values().length - 1)]);
            NBTHelper.hasInteger(compound, "BlockCollisionResponse", value -> this.blockCollisionResponse = ContactResponse.values()[MathHelper.clamp(value, 0, ContactResponse.values().length - 1)]);
            NBTHelper.hasInteger(compound, "TransparentBlockProfile", value -> this.transparentBlockProfile = TransparentBlockProfile.values()[MathHelper.clamp(value, 0, TransparentBlockProfile.values().length - 1)]);
        }

        private void writeToNBT(NBTTagCompound compound) {
            compound.setFloat("AirDrag", this.airDrag);
            compound.setFloat("WaterDrag", this.waterDrag);
            compound.setFloat("LavaDrag", this.lavaDrag);
            compound.setFloat("GravityPerTick", this.gravityPerTick);
            compound.setInteger("LifetimeTicks", this.lifetimeTicks);
            compound.setInteger("MaxLifetimeTicks", this.maxLifetimeTicks);
            compound.setBoolean("ExpireInWater", this.expireInWater);
            compound.setBoolean("ExpireInLava", this.expireInLava);
            compound.setInteger("EntityCollisionResponse", this.entityCollisionResponse.ordinal());
            compound.setInteger("BlockCollisionResponse", this.blockCollisionResponse.ordinal());
            compound.setInteger("TransparentBlockProfile", this.transparentBlockProfile.ordinal());
        }
    }

    private static class EnergyBehavior {

        private float initialEnergy;
        private float remainingEnergy;
        private float decayPerTick;
        private float fizzleThreshold;
        private boolean enabled;
        private boolean scaleDamage;

        private void captureInitial(EntityRangedAttack projectile) {
            if (!this.enabled) {
                return;
            }
            final float speed = this.getMotionMagnitude(projectile);
            this.initialEnergy = speed;
            this.remainingEnergy = speed;
        }

        private void applyDecay(EntityRangedAttack projectile) {
            if (!this.enabled || this.decayPerTick <= 0.0F || this.remainingEnergy <= 0.0F) {
                return;
            }
            final float speed = this.getMotionMagnitude(projectile);
            this.remainingEnergy = Math.max(0.0F, Math.min(this.remainingEnergy, speed) - this.decayPerTick);
            if (speed <= 1.0E-7F || this.remainingEnergy <= this.fizzleThreshold) {
                projectile.resetMotion();
                return;
            }
            final double scale = this.remainingEnergy / speed;
            projectile.motionX *= scale;
            projectile.motionY *= scale;
            projectile.motionZ *= scale;
        }

        private boolean shouldFizzle() {
            return this.enabled && this.remainingEnergy <= this.fizzleThreshold;
        }

        private float getImpactDamage(float baseDamage) {
            if (!this.enabled || !this.scaleDamage || this.initialEnergy <= 1.0E-7F) {
                return baseDamage;
            }
            return baseDamage * MathHelper.clamp(this.remainingEnergy / this.initialEnergy, 0.0F, 1.0F);
        }

        private float consume(EntityRangedAttack projectile, float amount) {
            if (!this.enabled || amount <= 0.0F || this.remainingEnergy <= 0.0F) {
                return 0.0F;
            }
            final float consumed = Math.min(this.remainingEnergy, amount);
            this.remainingEnergy -= consumed;
            if (this.remainingEnergy <= this.fizzleThreshold) {
                projectile.resetMotion();
                return consumed;
            }
            final float speed = this.getMotionMagnitude(projectile);
            if (speed <= 1.0E-7F) {
                return consumed;
            }
            final double scale = this.remainingEnergy / speed;
            projectile.motionX *= scale;
            projectile.motionY *= scale;
            projectile.motionZ *= scale;
            return consumed;
        }

        private float getMotionMagnitude(EntityRangedAttack projectile) {
            return (float) Math.sqrt((projectile.motionX * projectile.motionX) + (projectile.motionY * projectile.motionY) + (projectile.motionZ * projectile.motionZ));
        }

        private void readFromNBT(NBTTagCompound compound) {
            NBTHelper.hasFloat(compound, "InitialEnergy", value -> this.initialEnergy = Math.max(0.0F, value));
            NBTHelper.hasFloat(compound, "RemainingEnergy", value -> this.remainingEnergy = Math.max(0.0F, value));
            NBTHelper.hasFloat(compound, "EnergyDecayPerTick", value -> this.decayPerTick = Math.max(0.0F, value));
            NBTHelper.hasFloat(compound, "EnergyFizzleThreshold", value -> this.fizzleThreshold = Math.max(0.0F, value));
            NBTHelper.hasBoolean(compound, "ScaleDamageWithEnergy", value -> this.scaleDamage = value);
            NBTHelper.hasBoolean(compound, "EnergyEnabled", value -> this.enabled = value);
        }

        private void writeToNBT(NBTTagCompound compound) {
            if (!this.enabled) {
                return;
            }
            compound.setFloat("InitialEnergy", this.initialEnergy);
            compound.setFloat("RemainingEnergy", this.remainingEnergy);
            compound.setBoolean("EnergyEnabled", true);
            compound.setFloat("EnergyDecayPerTick", this.decayPerTick);
            compound.setFloat("EnergyFizzleThreshold", this.fizzleThreshold);
            compound.setBoolean("ScaleDamageWithEnergy", this.scaleDamage);
        }
    }
}
