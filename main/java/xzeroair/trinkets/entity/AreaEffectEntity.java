package xzeroair.trinkets.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.util.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class AreaEffectEntity extends Entity {
    private static final String ACTION_TYPE = "Type";
    private static final String ACTIONS_TAG = "Actions";
    private static final String ENTITY_BLACKLIST_TAG = "EntityBlacklist";
    private static final String BLOCK_BLACKLIST_TAG = "BlockBlacklist";
    private static final String ACTION_POTION = "potion";
    private static final String ACTION_REPAIR_ITEM = "repair_item";
    private static final String ACTION_GROW_BLOCK = "grow_block";
    private static final String ACTION_PLACE_FIRE = "place_fire";
    private static final String ACTION_PLACE_SNOW = "place_snow";
    private static final String ACTION_FREEZE_LIQUID = "freeze_liquid";
    private static final String ACTION_DAMAGE = "damage";
    private static final String WATER_BEHAVIOR_TAG = "WaterBehavior";
    private static final String LAVA_BEHAVIOR_TAG = "LavaBehavior";
    private static final double LIQUID_MOVEMENT_PER_TICK = 0.1D;
    private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(AreaEffectEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> VERTICAL_RADIUS = EntityDataManager.createKey(AreaEffectEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(AreaEffectEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.createKey(AreaEffectEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PARTICLE = EntityDataManager.createKey(AreaEffectEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PARTICLE_PARAM_1 = EntityDataManager.createKey(AreaEffectEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PARTICLE_PARAM_2 = EntityDataManager.createKey(AreaEffectEntity.class, DataSerializers.VARINT);
    private PotionType potion;
    private final List<PotionEffect> effects;
    private final List<AreaEffectAction> actions;
    private final Map<Entity, Integer> reapplicationDelayMap;
    private final Map<Long, Integer> blockReapplicationDelayMap;
    private final Set<String> entityRegistryBlacklist;
    private final Set<String> blockRegistryBlacklist;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private int pulseInterval;
    private boolean colorSet;
    private boolean affectedByGravity;
    private double gravityPerTick;
    private LiquidBehavior waterBehavior;
    private LiquidBehavior lavaBehavior;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private Predicate<Entity> entityTargetPredicate;
    private Predicate<BlockPos> blockTargetPredicate;
    @Nullable
    private EntityLivingBase owner;
    @Nullable
    private UUID ownerUniqueId;

    public enum LiquidBehavior {
        EXPIRE,
        STAY,
        FLOAT,
        SINK
    }

    public AreaEffectEntity(World worldIn) {
        super(worldIn);
        this.potion = PotionTypes.EMPTY;
        this.effects = Lists.newArrayList();
        this.actions = Lists.newArrayList();
        this.reapplicationDelayMap = Maps.newHashMap();
        this.blockReapplicationDelayMap = Maps.newHashMap();
        this.entityRegistryBlacklist = new LinkedHashSet<>();
        this.blockRegistryBlacklist = new LinkedHashSet<>();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.pulseInterval = 20;
        this.gravityPerTick = 0.03D;
        this.waterBehavior = LiquidBehavior.STAY;
        this.lavaBehavior = LiquidBehavior.STAY;
        this.entityTargetPredicate = entity -> true;
        this.blockTargetPredicate = pos -> true;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setRadius(3.0F);
        this.setVerticalRadius(1.5F);
    }

    public AreaEffectEntity(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(COLOR, 0);
        this.getDataManager().register(RADIUS, 0.5F);
        this.getDataManager().register(VERTICAL_RADIUS, 1.5F);
        this.getDataManager().register(IGNORE_RADIUS, Boolean.FALSE);
        this.getDataManager().register(PARTICLE, EnumParticleTypes.SPELL_MOB.getParticleID());
        this.getDataManager().register(PARTICLE_PARAM_1, 0);
        this.getDataManager().register(PARTICLE_PARAM_2, 0);
    }

    public void setRadius(float radiusIn) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.setSize(radiusIn * 2.0F, Math.max(0.5F, this.getVerticalRadius() * 2.0F));
        this.setPosition(d0, d1, d2);

        if (!this.world.isRemote) {
            this.getDataManager().set(RADIUS, radiusIn);
        }
    }

    public float getRadius() {
        return this.getDataManager().get(RADIUS);
    }

    public void setVerticalRadius(float radiusIn) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.setSize(this.getRadius() * 2.0F, Math.max(0.5F, radiusIn * 2.0F));
        this.setPosition(d0, d1, d2);

        if (!this.world.isRemote) {
            this.getDataManager().set(VERTICAL_RADIUS, radiusIn);
        }
    }

    public float getVerticalRadius() {
        return this.getDataManager().get(VERTICAL_RADIUS);
    }

    public void setPulseInterval(int pulseInterval) {
        this.pulseInterval = Math.max(1, pulseInterval);
    }

    public int getPulseInterval() {
        return this.pulseInterval;
    }

    public void setAffectedByGravity(boolean affectedByGravity) {
        this.affectedByGravity = affectedByGravity;
        this.noClip = !affectedByGravity;
    }

    public boolean isAffectedByGravity() {
        return this.affectedByGravity;
    }

    public void setGravityPerTick(double gravityPerTick) {
        this.gravityPerTick = Math.max(0.0D, gravityPerTick);
    }

    public double getGravityPerTick() {
        return this.gravityPerTick;
    }

    public AreaEffectEntity setWaterBehavior(LiquidBehavior behavior) {
        this.waterBehavior = behavior == null ? LiquidBehavior.STAY : behavior;
        return this;
    }

    public AreaEffectEntity setLavaBehavior(LiquidBehavior behavior) {
        this.lavaBehavior = behavior == null ? LiquidBehavior.STAY : behavior;
        return this;
    }

    public AreaEffectEntity addAction(AreaEffectAction action) {
        if (action != null) {
            this.actions.add(action);
        }
        return this;
    }

    public List<AreaEffectAction> getActions() {
        return this.actions;
    }

    public AreaEffectEntity setEntityTargetPredicate(@Nullable Predicate<Entity> predicate) {
        this.entityTargetPredicate = predicate == null ? entity -> true : predicate;
        return this;
    }

    public AreaEffectEntity setBlockTargetPredicate(@Nullable Predicate<BlockPos> predicate) {
        this.blockTargetPredicate = predicate == null ? pos -> true : predicate;
        return this;
    }

    public AreaEffectEntity blacklistEntityRegistry(String... registryNames) {
        if (registryNames != null) {
            for (String registryName : registryNames) {
                if (registryName != null && !registryName.trim().isEmpty()) {
                    this.entityRegistryBlacklist.add(registryName.trim().toLowerCase());
                }
            }
        }
        return this;
    }

    public AreaEffectEntity blacklistBlockRegistry(String... registryNames) {
        if (registryNames != null) {
            for (String registryName : registryNames) {
                if (registryName != null && !registryName.trim().isEmpty()) {
                    this.blockRegistryBlacklist.add(registryName.trim().toLowerCase());
                }
            }
        }
        return this;
    }

    public boolean canOwnerModifyBlock(BlockPos pos, EnumFacing side) {
        final EntityLivingBase owner = this.getOwner();
        return !(owner instanceof EntityPlayer) || ((EntityPlayer) owner).canPlayerEdit(pos, side, ItemStack.EMPTY);
    }

    protected boolean isEntityTargetAllowed(Entity entity) {
        if (entity == null || entity == this || !this.entityTargetPredicate.test(entity)) {
            return false;
        }

        final ResourceLocation registryName = EntityList.getKey(entity);
        return registryName == null || !this.entityRegistryBlacklist.contains(registryName.toString().toLowerCase());
    }

    protected boolean isBlockTargetAllowed(BlockPos pos) {
        if (pos == null || !this.blockTargetPredicate.test(pos)) {
            return false;
        }

        final Block block = this.world.getBlockState(pos).getBlock();
        final ResourceLocation registryName = block.getRegistryName();
        return registryName == null || !this.blockRegistryBlacklist.contains(registryName.toString().toLowerCase());
    }

    public void setPotion(PotionType potionIn) {
        this.potion = potionIn;

        if (!this.colorSet) {
            this.updateFixedColor();
        }
    }

    protected void updateFixedColor() {
        if (this.potion == PotionTypes.EMPTY && this.effects.isEmpty()) {
            this.getDataManager().set(COLOR, 0);
        } else {
            this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.effects)));
        }
    }

    public void addEffect(PotionEffect effect) {
        this.effects.add(effect);

        if (!this.colorSet) {
            this.updateFixedColor();
        }
    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int colorIn) {
        this.colorSet = true;
        this.getDataManager().set(COLOR, colorIn);
    }

    @Nullable
    public EnumParticleTypes getParticle() {
        return EnumParticleTypes.getParticleFromId(this.getDataManager().get(PARTICLE));
    }

    public void setParticle(@Nullable EnumParticleTypes particleIn) {
        EnumParticleTypes particle = particleIn == null ? EnumParticleTypes.SPELL_MOB : particleIn;
        this.getDataManager().set(PARTICLE, particle.getParticleID());
    }

    public int getParticleParam1() {
        return this.getDataManager().get(PARTICLE_PARAM_1);
    }

    public void setParticleParam1(int particleParam) {
        this.getDataManager().set(PARTICLE_PARAM_1, particleParam);
    }

    public int getParticleParam2() {
        return this.getDataManager().get(PARTICLE_PARAM_2);
    }

    public void setParticleParam2(int particleParam) {
        this.getDataManager().set(PARTICLE_PARAM_2, particleParam);
    }

    protected void setIgnoreRadius(boolean ignoreRadius) {
        this.getDataManager().set(IGNORE_RADIUS, ignoreRadius);
    }

    public boolean shouldIgnoreRadius() {
        return this.getDataManager().get(IGNORE_RADIUS);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int durationIn) {
        this.duration = Math.max(0, durationIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote && !this.handleLiquidBehavior()) {
            return;
        }

        this.updateGravityMotion();

        if (this.world.isRemote) {
            this.spawnClientParticles();
        } else {
            this.updateServerEffect();
        }
    }

    private boolean handleLiquidBehavior() {
        final Material material = this.world.getBlockState(new BlockPos(this)).getMaterial();
        if (material == Material.WATER) {
            return this.applyLiquidBehavior(this.waterBehavior);
        }
        if (material == Material.LAVA) {
            return this.applyLiquidBehavior(this.lavaBehavior);
        }
        return true;
    }

    private boolean applyLiquidBehavior(LiquidBehavior behavior) {
        switch (behavior) {
            case EXPIRE:
                this.setDead();
                return false;
            case FLOAT:
                this.move(MoverType.SELF, 0.0D, LIQUID_MOVEMENT_PER_TICK, 0.0D);
                return true;
            case SINK:
                this.move(MoverType.SELF, 0.0D, -LIQUID_MOVEMENT_PER_TICK, 0.0D);
                return true;
            case STAY:
            default:
                return true;
        }
    }

    protected void updateGravityMotion() {
        if (!this.affectedByGravity) {
            return;
        }

        this.motionY -= this.gravityPerTick;
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;
    }

    protected void spawnClientParticles() {
        boolean ignoreRadius = this.shouldIgnoreRadius();
        float radius = this.getRadius();
        EnumParticleTypes particle = this.getParticle();
        if (particle == null) {
            particle = EnumParticleTypes.SPELL_MOB;
        }
        int[] particleArgs = this.getParticleArguments(particle);

        if (ignoreRadius) {
            this.spawnWaitingParticles(particle, particleArgs);
        } else {
            this.spawnAreaParticles(radius, particle, particleArgs);
        }
    }

    protected int[] getParticleArguments(EnumParticleTypes particle) {
        int[] particleArgs = new int[particle.getArgumentCount()];

        if (particleArgs.length > 0) {
            particleArgs[0] = this.getParticleParam1();
        }

        if (particleArgs.length > 1) {
            particleArgs[1] = this.getParticleParam2();
        }

        return particleArgs;
    }

    protected void spawnWaitingParticles(EnumParticleTypes particle, int[] particleArgs) {
        if (!this.rand.nextBoolean()) {
            return;
        }

        for (int i = 0; i < 2; ++i) {
            float angle = this.rand.nextFloat() * ((float) Math.PI * 2F);
            float distance = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
            float offsetX = MathHelper.cos(angle) * distance;
            float offsetZ = MathHelper.sin(angle) * distance;

            if (particle == EnumParticleTypes.SPELL_MOB) {
                this.spawnSpellMobParticle(this.posX + (double) offsetX, this.posY, this.posZ + (double) offsetZ, this.rand.nextBoolean() ? 16777215 : this.getColor());
            } else {
                this.world.spawnAlwaysVisibleParticle(particle.getParticleID(), this.posX + (double) offsetX, this.posY, this.posZ + (double) offsetZ, 0.0D, 0.0D, 0.0D, particleArgs);
            }
        }
    }

    protected void spawnAreaParticles(float radius, EnumParticleTypes particle, int[] particleArgs) {
        int particleCount = MathHelper.ceil(Math.min(24F, (float) Math.PI * radius * radius * 0.5F));

        for (int i = 0; i < particleCount; ++i) {
            float angle = this.rand.nextFloat() * ((float) Math.PI * 2F);
            float distance = MathHelper.sqrt(this.rand.nextFloat()) * radius;
            float offsetX = MathHelper.cos(angle) * distance;
            float offsetZ = MathHelper.sin(angle) * distance;

            if (particle == EnumParticleTypes.SPELL_MOB) {
                this.spawnSpellMobParticle(this.posX + (double) offsetX, this.posY, this.posZ + (double) offsetZ, this.getColor());
            } else {
                this.world.spawnAlwaysVisibleParticle(particle.getParticleID(), this.posX + (double) offsetX, this.posY, this.posZ + (double) offsetZ, (0.5D - this.rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.rand.nextDouble()) * 0.15D, particleArgs);
            }
        }
    }

    protected void spawnSpellMobParticle(double x, double y, double z, int color) {
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), x, y, z, (float) red / 255.0F, (float) green / 255.0F, (float) blue / 255.0F);
    }

    protected void updateServerEffect() {
        if (this.ticksExisted >= this.waitTime + this.duration) {
            this.setDead();
            return;
        }

        boolean waiting = this.ticksExisted < this.waitTime;
        if (this.shouldIgnoreRadius() != waiting) {
            this.setIgnoreRadius(waiting);
        }

        if (waiting) {
            return;
        }

        float radius = this.updateRadiusPerTick();
        if (this.isDead || this.ticksExisted % this.pulseInterval != 0) {
            return;
        }

        this.clearExpiredReapplicationDelays();
        radius = this.processLegacyPotionEffects(radius);
        if (this.isDead) {
            return;
        }
        this.processActionEntities(radius);
        this.processActionBlocks(radius);
    }

    protected float updateRadiusPerTick() {
        float radius = this.getRadius();
        if (this.radiusPerTick == 0.0F) {
            return radius;
        }

        radius += this.radiusPerTick;

        if (radius < 0.5F) {
            this.setDead();
            return radius;
        }

        this.setRadius(radius);
        return radius;
    }

    protected void clearExpiredReapplicationDelays() {
        this.clearExpiredEntityReapplicationDelays();
        this.clearExpiredBlockReapplicationDelays();
    }

    protected void clearExpiredEntityReapplicationDelays() {
        Iterator<Map.Entry<Entity, Integer>> iterator = this.reapplicationDelayMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Entity, Integer> entry = iterator.next();

            if (this.ticksExisted >= entry.getValue()) {
                iterator.remove();
            }
        }
    }

    protected void clearExpiredBlockReapplicationDelays() {
        Iterator<Map.Entry<Long, Integer>> iterator = this.blockReapplicationDelayMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();

            if (this.ticksExisted >= entry.getValue()) {
                iterator.remove();
            }
        }
    }

    protected float processLegacyPotionEffects(float radius) {
        List<PotionEffect> potions = Lists.newArrayList();

        for (PotionEffect potionEffect : this.potion.getEffects()) {
            potions.add(new PotionEffect(potionEffect.getPotion(), potionEffect.getDuration() / 4, potionEffect.getAmplifier(), potionEffect.getIsAmbient(), potionEffect.doesShowParticles()));
        }

        potions.addAll(this.effects);

        if (potions.isEmpty()) {
            this.reapplicationDelayMap.clear();
            return radius;
        }

        List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, this.getAreaBoundingBox(radius));

        for (Entity entity : list) {
            if (!(entity instanceof EntityLivingBase) || !this.isEntityTargetAllowed(entity)) {
                continue;
            }

            EntityLivingBase living = (EntityLivingBase) entity;
            if (this.reapplicationDelayMap.containsKey(living) || !living.canBeHitWithPotion() || !this.isInArea(living, radius)) {
                continue;
            }

            this.reapplicationDelayMap.put(living, this.ticksExisted + this.reapplicationDelay);

            for (PotionEffect potionEffect : potions) {
                if (potionEffect.getPotion().isInstant()) {
                    potionEffect.getPotion().affectEntity(this, this.getOwner(), living, potionEffect.getAmplifier(), 0.5D);
                } else {
                    living.addPotionEffect(new PotionEffect(potionEffect));
                }
            }

            radius = this.applyUseModifiers(radius);
            if (this.isDead) {
                return radius;
            }
        }

        return radius;
    }

    protected float applyUseModifiers(float radius) {
        if (this.radiusOnUse != 0.0F) {
            radius += this.radiusOnUse;

            if (radius < 0.5F) {
                this.setDead();
                return radius;
            }

            this.setRadius(radius);
        }

        if (this.durationOnUse != 0) {
            this.duration += this.durationOnUse;

            if (this.duration <= 0) {
                this.setDead();
            }
        }

        return radius;
    }

    protected void processActionEntities(float radius) {
        if (this.actions.isEmpty()) {
            return;
        }

        List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, this.getAreaBoundingBox(radius));

        for (Entity entity : list) {
            this.processActionEntity(entity, radius);
        }
    }

    protected void processActionEntity(Entity entity, float radius) {
        if (!this.isEntityTargetAllowed(entity) || !this.isInArea(entity, radius) || this.reapplicationDelayMap.containsKey(entity)) {
            return;
        }

        boolean affected = false;

        for (AreaEffectAction action : this.actions) {
            if (action.canAffectEntity(this, entity)) {
                action.affectEntity(this, entity);
                affected = true;
            }
        }

        if (affected) {
            this.reapplicationDelayMap.put(entity, this.ticksExisted + this.reapplicationDelay);
        }
    }

    protected void processActionBlocks(float radius) {
        if (this.actions.isEmpty()) {
            return;
        }

        AxisAlignedBB box = this.getAreaBoundingBox(radius);
        int minX = MathHelper.floor(box.minX);
        int minY = MathHelper.floor(box.minY);
        int minZ = MathHelper.floor(box.minZ);
        int maxX = MathHelper.floor(box.maxX);
        int maxY = MathHelper.floor(box.maxY);
        int maxZ = MathHelper.floor(box.maxZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    this.processActionBlock(new BlockPos(x, y, z), radius);
                }
            }
        }
    }

    protected void processActionBlock(BlockPos pos, float radius) {
        long key = pos.toLong();

        if (!this.isBlockTargetAllowed(pos) || !this.isBlockInArea(pos, radius) || this.blockReapplicationDelayMap.containsKey(key)) {
            return;
        }

        boolean affected = false;

        for (AreaEffectAction action : this.actions) {
            if (action.canAffectBlock(this, pos)) {
                action.affectBlock(this, pos);
                affected = true;
            }
        }

        if (affected) {
            this.blockReapplicationDelayMap.put(key, this.ticksExisted + this.reapplicationDelay);
        }
    }

    protected AxisAlignedBB getAreaBoundingBox(float radius) {
        float verticalRadius = this.getVerticalRadius();
        return new AxisAlignedBB(this.posX - radius, this.posY - verticalRadius, this.posZ - radius, this.posX + radius, this.posY + verticalRadius, this.posZ + radius);
    }

    protected boolean isInArea(Entity entity, float radius) {
        double dx = entity.posX - this.posX;
        double dz = entity.posZ - this.posZ;
        double dy = Math.abs(entity.posY + (double) entity.height * 0.5D - this.posY);
        return dx * dx + dz * dz <= (double) (radius * radius) && dy <= (double) this.getVerticalRadius();
    }

    protected boolean isBlockInArea(BlockPos pos, float radius) {
        double dx = (double) pos.getX() + 0.5D - this.posX;
        double dz = (double) pos.getZ() + 0.5D - this.posZ;
        double dy = Math.abs((double) pos.getY() + 0.5D - this.posY);
        return dx * dx + dz * dz <= (double) (radius * radius) && dy <= (double) this.getVerticalRadius();
    }

    public void setRadiusOnUse(float radiusOnUseIn) {
        this.radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(float radiusPerTickIn) {
        this.radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(int waitTimeIn) {
        this.waitTime = Math.max(0, waitTimeIn);
    }

    public void setReapplicationDelay(int reapplicationDelay) {
        this.reapplicationDelay = Math.max(1, reapplicationDelay);
    }

    public int getReapplicationDelay() {
        return this.reapplicationDelay;
    }

    public void setOwner(@Nullable EntityLivingBase ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    @Nullable
    public EntityLivingBase getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer) {
            Entity entity = ((WorldServer) this.world).getEntityFromUuid(this.ownerUniqueId);

            if (entity instanceof EntityLivingBase) {
                this.owner = (EntityLivingBase) entity;
            }
        }

        return this.owner;
    }

    private LiquidBehavior readLiquidBehavior(NBTTagCompound compound, String key) {
        if (!compound.hasKey(key, 8)) {
            return LiquidBehavior.STAY;
        }
        try {
            return LiquidBehavior.valueOf(compound.getString(key));
        } catch (IllegalArgumentException ignored) {
            return LiquidBehavior.STAY;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.actions.clear();
        this.effects.clear();
        this.entityRegistryBlacklist.clear();
        this.blockRegistryBlacklist.clear();
        this.colorSet = false;
        this.owner = null;
        this.setPotion(PotionTypes.EMPTY);
        this.ticksExisted = compound.getInteger("Age");
        this.setDuration(compound.getInteger("Duration"));
        this.setWaitTime(compound.getInteger("WaitTime"));
        this.setReapplicationDelay(compound.getInteger("ReapplicationDelay"));
        this.setPulseInterval(compound.hasKey("PulseInterval") ? compound.getInteger("PulseInterval") : this.reapplicationDelay);
        this.durationOnUse = compound.getInteger("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.affectedByGravity = compound.getBoolean("AffectedByGravity");
        this.gravityPerTick = compound.hasKey("GravityPerTick") ? compound.getDouble("GravityPerTick") : 0.03D;
        this.waterBehavior = this.readLiquidBehavior(compound, WATER_BEHAVIOR_TAG);
        this.lavaBehavior = this.readLiquidBehavior(compound, LAVA_BEHAVIOR_TAG);
        this.setRadius(compound.getFloat("Radius"));
        this.setVerticalRadius(compound.hasKey("VerticalRadius") ? compound.getFloat("VerticalRadius") : 1.5F);
        this.noClip = !this.affectedByGravity;
        this.ownerUniqueId = compound.hasUniqueId("OwnerUUID") ? compound.getUniqueId("OwnerUUID") : null;

        if (compound.hasKey("Particle", 8)) {
            EnumParticleTypes enumparticletypes = EnumParticleTypes.getByName(compound.getString("Particle"));

            if (enumparticletypes != null) {
                this.setParticle(enumparticletypes);
                this.setParticleParam1(compound.getInteger("ParticleParam1"));
                this.setParticleParam2(compound.getInteger("ParticleParam2"));
            }
        }

        if (compound.hasKey("Color", 99)) {
            this.setColor(compound.getInteger("Color"));
        }

        if (compound.hasKey("Potion", 8)) {
            this.setPotion(PotionUtils.getPotionTypeFromNBT(compound));
        }

        if (compound.hasKey("Effects", 9)) {
            NBTTagList nbttaglist = compound.getTagList("Effects", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttaglist.getCompoundTagAt(i));

                if (potioneffect != null) {
                    this.addEffect(potioneffect);
                }
            }
        }

        if (compound.hasKey(ENTITY_BLACKLIST_TAG, 9)) {
            NBTTagList list = compound.getTagList(ENTITY_BLACKLIST_TAG, 8);
            for (int i = 0; i < list.tagCount(); ++i) {
                this.blacklistEntityRegistry(list.getStringTagAt(i));
            }
        }

        if (compound.hasKey(BLOCK_BLACKLIST_TAG, 9)) {
            NBTTagList list = compound.getTagList(BLOCK_BLACKLIST_TAG, 8);
            for (int i = 0; i < list.tagCount(); ++i) {
                this.blacklistBlockRegistry(list.getStringTagAt(i));
            }
        }

        if (compound.hasKey(ACTIONS_TAG, 9)) {
            NBTTagList list = compound.getTagList(ACTIONS_TAG, 10);
            for (int i = 0; i < list.tagCount(); ++i) {
                this.readActionTag(list.getCompoundTagAt(i));
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Age", this.ticksExisted);
        compound.setInteger("Duration", this.duration);
        compound.setInteger("WaitTime", this.waitTime);
        compound.setInteger("ReapplicationDelay", this.reapplicationDelay);
        compound.setInteger("PulseInterval", this.pulseInterval);
        compound.setInteger("DurationOnUse", this.durationOnUse);
        compound.setFloat("RadiusOnUse", this.radiusOnUse);
        compound.setFloat("RadiusPerTick", this.radiusPerTick);
        compound.setFloat("Radius", this.getRadius());
        compound.setFloat("VerticalRadius", this.getVerticalRadius());
        compound.setBoolean("AffectedByGravity", this.affectedByGravity);
        compound.setDouble("GravityPerTick", this.gravityPerTick);
        compound.setString(WATER_BEHAVIOR_TAG, this.waterBehavior.name());
        compound.setString(LAVA_BEHAVIOR_TAG, this.lavaBehavior.name());
        EnumParticleTypes particle = this.getParticle();
        if (particle == null) {
            particle = EnumParticleTypes.SPELL_MOB;
        }
        compound.setString("Particle", particle.getParticleName());
        compound.setInteger("ParticleParam1", this.getParticleParam1());
        compound.setInteger("ParticleParam2", this.getParticleParam2());

        if (this.ownerUniqueId != null) {
            compound.setUniqueId("OwnerUUID", this.ownerUniqueId);
        }

        if (this.colorSet) {
            compound.setInteger("Color", this.getColor());
        }

        if (this.potion != PotionTypes.EMPTY && this.potion != null) {
            compound.setString("Potion", PotionType.REGISTRY.getNameForObject(this.potion).toString());
        }

        if (!this.effects.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : this.effects) {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            compound.setTag("Effects", nbttaglist);
        }

        if (!this.entityRegistryBlacklist.isEmpty()) {
            NBTTagList list = new NBTTagList();
            for (String entry : this.entityRegistryBlacklist) {
                list.appendTag(new NBTTagString(entry));
            }
            compound.setTag(ENTITY_BLACKLIST_TAG, list);
        }

        if (!this.blockRegistryBlacklist.isEmpty()) {
            NBTTagList list = new NBTTagList();
            for (String entry : this.blockRegistryBlacklist) {
                list.appendTag(new NBTTagString(entry));
            }
            compound.setTag(BLOCK_BLACKLIST_TAG, list);
        }

        if (!this.actions.isEmpty()) {
            NBTTagList list = new NBTTagList();
            for (AreaEffectAction action : this.actions) {
                NBTTagCompound actionTag = this.writeActionTag(action);
                if (!actionTag.isEmpty()) {
                    list.appendTag(actionTag);
                }
            }
            if (list.tagCount() > 0) {
                compound.setTag(ACTIONS_TAG, list);
            }
        }
    }

    private void readActionTag(NBTTagCompound actionTag) {
        final String type = actionTag.getString(ACTION_TYPE);
        switch (type) {
            case ACTION_POTION:
                if (actionTag.hasKey("Effect", 10)) {
                    PotionEffect effect = PotionEffect.readCustomPotionEffectFromNBT(actionTag.getCompoundTag("Effect"));
                    if (effect != null) {
                        this.addAction(new PotionAreaAction(effect, actionTag.hasKey("InstantStrength") ? actionTag.getDouble("InstantStrength") : 0.5D));
                    }
                }
                break;
            case ACTION_REPAIR_ITEM:
                this.addAction(new RepairItemAreaAction(actionTag.getInteger("RepairAmount")));
                break;
            case ACTION_GROW_BLOCK:
                this.addAction(new GrowBlockAreaAction(actionTag.getInteger("MaxBlocksPerPulse")));
                break;
            case ACTION_PLACE_FIRE:
                this.addAction(new PlaceFireAreaAction());
                break;
            case ACTION_PLACE_SNOW:
                this.addAction(new PlaceSnowAreaAction());
                break;
            case ACTION_FREEZE_LIQUID:
                this.addAction(new FreezeLiquidAreaAction(actionTag.getInteger("MaxBlocksPerPulse")));
                break;
            case ACTION_DAMAGE:
                this.addAction(new DamageAreaAction(actionTag.getFloat("Damage")));
                break;
            default:
                break;
        }
    }

    private NBTTagCompound writeActionTag(AreaEffectAction action) {
        NBTTagCompound actionTag = new NBTTagCompound();
        if (action instanceof PotionAreaAction) {
            PotionAreaAction potionAction = (PotionAreaAction) action;
            if (potionAction.effect != null) {
                actionTag.setString(ACTION_TYPE, ACTION_POTION);
                actionTag.setTag("Effect", potionAction.effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
                actionTag.setDouble("InstantStrength", potionAction.instantStrength);
            }
        } else if (action instanceof RepairItemAreaAction) {
            RepairItemAreaAction repairAction = (RepairItemAreaAction) action;
            actionTag.setString(ACTION_TYPE, ACTION_REPAIR_ITEM);
            actionTag.setInteger("RepairAmount", repairAction.repairAmount);
        } else if (action instanceof GrowBlockAreaAction) {
            GrowBlockAreaAction growAction = (GrowBlockAreaAction) action;
            actionTag.setString(ACTION_TYPE, ACTION_GROW_BLOCK);
            actionTag.setInteger("MaxBlocksPerPulse", growAction.maxBlocksPerPulse);
        } else if (action instanceof PlaceFireAreaAction) {
            actionTag.setString(ACTION_TYPE, ACTION_PLACE_FIRE);
        } else if (action instanceof PlaceSnowAreaAction) {
            actionTag.setString(ACTION_TYPE, ACTION_PLACE_SNOW);
        } else if (action instanceof FreezeLiquidAreaAction) {
            FreezeLiquidAreaAction freezeAction = (FreezeLiquidAreaAction) action;
            actionTag.setString(ACTION_TYPE, ACTION_FREEZE_LIQUID);
            actionTag.setInteger("MaxBlocksPerPulse", freezeAction.maxBlocksPerPulse);
        } else if (action instanceof DamageAreaAction) {
            DamageAreaAction damageAction = (DamageAreaAction) action;
            actionTag.setString(ACTION_TYPE, ACTION_DAMAGE);
            actionTag.setFloat("Damage", damageAction.damage);
        }
        return actionTag;
    }

    @Override
    public void notifyDataManagerChange(@Nonnull DataParameter<?> key) {
        if (RADIUS.equals(key) || VERTICAL_RADIUS.equals(key)) {
            this.setRadius(this.getRadius());
        }

        super.notifyDataManagerChange(key);
    }

    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }

    public interface AreaEffectAction {
        default boolean canAffectEntity(AreaEffectEntity area, Entity entity) {
            return false;
        }

        default void affectEntity(AreaEffectEntity area, Entity entity) {
        }

        default boolean canAffectBlock(AreaEffectEntity area, BlockPos pos) {
            return false;
        }

        default void affectBlock(AreaEffectEntity area, BlockPos pos) {
        }
    }

    public static class PotionAreaAction implements AreaEffectAction {
        private final PotionEffect effect;
        private final double instantStrength;

        public PotionAreaAction(PotionEffect effect) {
            this(effect, 0.5D);
        }

        public PotionAreaAction(PotionEffect effect, double instantStrength) {
            this.effect = effect;
            this.instantStrength = instantStrength;
        }

        @Override
        public boolean canAffectEntity(AreaEffectEntity area, Entity entity) {
            return this.effect != null && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).canBeHitWithPotion();
        }

        @Override
        public void affectEntity(AreaEffectEntity area, Entity entity) {
            EntityLivingBase living = (EntityLivingBase) entity;
            if (this.effect.getPotion().isInstant()) {
                this.effect.getPotion().affectEntity(area, area.getOwner(), living, this.effect.getAmplifier(), this.instantStrength);
            } else {
                living.addPotionEffect(new PotionEffect(this.effect));
            }
        }
    }

    public static class DamageAreaAction implements AreaEffectAction {
        private final float damage;

        public DamageAreaAction(float damage) {
            this.damage = Math.max(0.0F, damage);
        }

        @Override
        public boolean canAffectEntity(AreaEffectEntity area, Entity entity) {
            return this.damage > 0.0F && entity instanceof EntityLivingBase;
        }

        @Override
        public void affectEntity(AreaEffectEntity area, Entity entity) {
            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(area, area.getOwner()), this.damage);
        }
    }

    public static abstract class ItemAreaAction implements AreaEffectAction {
        @Override
        public boolean canAffectEntity(AreaEffectEntity area, Entity entity) {
            if (!(entity instanceof EntityItem)) {
                return false;
            }

            EntityItem item = (EntityItem) entity;
            return this.canAffectItem(area, item, item.getItem());
        }

        @Override
        public void affectEntity(AreaEffectEntity area, Entity entity) {
            if (entity instanceof EntityItem) {
                EntityItem item = (EntityItem) entity;
                this.affectItem(area, item, item.getItem());
            }
        }

        protected boolean canAffectItem(AreaEffectEntity area, EntityItem item, ItemStack stack) {
            return false;
        }

        protected void affectItem(AreaEffectEntity area, EntityItem item, ItemStack stack) {
        }
    }

    public static class RepairItemAreaAction extends ItemAreaAction {
        private final int repairAmount;

        public RepairItemAreaAction(int repairAmount) {
            this.repairAmount = Math.max(0, repairAmount);
        }

        @Override
        protected boolean canAffectItem(AreaEffectEntity area, EntityItem item, ItemStack stack) {
            return this.repairAmount > 0 && !stack.isEmpty() && stack.isItemStackDamageable() && stack.isItemDamaged();
        }

        @Override
        protected void affectItem(AreaEffectEntity area, EntityItem item, ItemStack stack) {
            int previousDamage = stack.getItemDamage();
            int newDamage = Math.max(0, previousDamage - this.repairAmount);
            if (newDamage != previousDamage) {
                stack.setItemDamage(newDamage);
                item.setItem(stack);
                spawnBonemealParticles(area, new BlockPos(item), 8, 0.35D, 0.2D);
            }
        }
    }

    public static class GrowBlockAreaAction implements AreaEffectAction {
        private final int maxBlocksPerPulse;
        private int blocksGrownThisPulse;
        private int lastPulseTick = -1;

        public GrowBlockAreaAction(int maxBlocksPerPulse) {
            this.maxBlocksPerPulse = Math.max(0, maxBlocksPerPulse);
        }

        @Override
        public boolean canAffectBlock(AreaEffectEntity area, BlockPos pos) {
            this.resetPulse(area);
            if (this.maxBlocksPerPulse <= 0 || this.blocksGrownThisPulse >= this.maxBlocksPerPulse) {
                return false;
            }

            if (!area.canOwnerModifyBlock(pos, EnumFacing.UP)) {
                return false;
            }

            IBlockState state = area.world.getBlockState(pos);
            Block block = state.getBlock();
            return block instanceof IGrowable && ((IGrowable) block).canGrow(area.world, pos, state, false);
        }

        @Override
        public void affectBlock(AreaEffectEntity area, BlockPos pos) {
            this.resetPulse(area);
            if (this.maxBlocksPerPulse <= 0 || this.blocksGrownThisPulse >= this.maxBlocksPerPulse) {
                return;
            }

            IBlockState state = area.world.getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof IGrowable) {
                IGrowable growable = (IGrowable) block;
                if (growable.canUseBonemeal(area.world, area.world.rand, pos, state)) {
                    growable.grow(area.world, area.world.rand, pos, state);
                    this.blocksGrownThisPulse++;
                    spawnBonemealParticles(area, pos, 18, 0.65D, 0.45D);
                }
            }
        }

        private void resetPulse(AreaEffectEntity area) {
            if (this.lastPulseTick != area.ticksExisted) {
                this.lastPulseTick = area.ticksExisted;
                this.blocksGrownThisPulse = 0;
            }
        }
    }

    public static class PlaceFireAreaAction implements AreaEffectAction {
        @Override
        public boolean canAffectBlock(AreaEffectEntity area, BlockPos pos) {
            if (!area.world.getGameRules().getBoolean(Reference.MINECRAFT_GAMERULE_MOBGRIEFING)
                    || !area.canOwnerModifyBlock(pos, EnumFacing.UP)) {
                return false;
            }

            final IBlockState state = area.world.getBlockState(pos);
            return (area.world.isAirBlock(pos) || state.getMaterial().isReplaceable())
                    && Blocks.FIRE.canPlaceBlockAt(area.world, pos);
        }

        @Override
        public void affectBlock(AreaEffectEntity area, BlockPos pos) {
            area.world.setBlockState(pos, Blocks.FIRE.getDefaultState());
            area.world.neighborChanged(pos, Blocks.FIRE, pos);
        }
    }

    public static class FreezeLiquidAreaAction implements AreaEffectAction {
        private final int maxBlocksPerPulse;
        private int blocksFrozenThisPulse;
        private int lastPulseTick = -1;

        public FreezeLiquidAreaAction(int maxBlocksPerPulse) {
            this.maxBlocksPerPulse = Math.max(0, maxBlocksPerPulse);
        }

        @Override
        public boolean canAffectBlock(AreaEffectEntity area, BlockPos pos) {
            this.resetPulse(area);
            if (this.maxBlocksPerPulse <= 0 || this.blocksFrozenThisPulse >= this.maxBlocksPerPulse
                    || !area.world.getGameRules().getBoolean(Reference.MINECRAFT_GAMERULE_MOBGRIEFING)
                    || !area.canOwnerModifyBlock(pos, EnumFacing.UP)) {
                return false;
            }

            final IBlockState state = area.world.getBlockState(pos);
            final Block block = state.getBlock();
            final IBlockState above = area.world.getBlockState(pos.up());
            if (!above.getMaterial().isReplaceable()) {
                return false;
            }
            if (state.getMaterial() == Material.WATER) {
                return (block == Blocks.WATER || block == Blocks.FLOWING_WATER)
                        && state.getValue(BlockLiquid.LEVEL) == 0
                        && area.world.mayPlace(Blocks.FROSTED_ICE, pos, false, EnumFacing.DOWN, null);
            }
            return state.getMaterial() == Material.LAVA
                    && (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA)
                    && state.getValue(BlockLiquid.LEVEL) == 0
                    && area.world.mayPlace(ModBlocks.Placeables.TEMP_BLOCK, pos, false, EnumFacing.DOWN, null);
        }

        @Override
        public void affectBlock(AreaEffectEntity area, BlockPos pos) {
            this.resetPulse(area);
            if (this.blocksFrozenThisPulse >= this.maxBlocksPerPulse) {
                return;
            }

            final IBlockState state = area.world.getBlockState(pos);
            if (state.getMaterial() == Material.WATER) {
                area.world.setBlockState(pos, Blocks.FROSTED_ICE.getDefaultState());
                area.world.scheduleUpdate(pos, Blocks.FROSTED_ICE, MathHelper.getInt(Reference.random, 60, 120));
            } else if (state.getMaterial() == Material.LAVA) {
                area.world.setBlockState(pos, ModBlocks.Placeables.TEMP_BLOCK.getDefaultState());
                area.world.scheduleUpdate(pos, ModBlocks.Placeables.TEMP_BLOCK, MathHelper.getInt(Reference.random, 60, 120));
            } else {
                return;
            }
            this.blocksFrozenThisPulse++;
        }

        private void resetPulse(AreaEffectEntity area) {
            if (this.lastPulseTick != area.ticksExisted) {
                this.lastPulseTick = area.ticksExisted;
                this.blocksFrozenThisPulse = 0;
            }
        }
    }

    public static class PlaceSnowAreaAction implements AreaEffectAction {
        @Override
        public boolean canAffectBlock(AreaEffectEntity area, BlockPos pos) {
            if (!area.world.getGameRules().getBoolean(Reference.MINECRAFT_GAMERULE_MOBGRIEFING)
                    || !area.canOwnerModifyBlock(pos, EnumFacing.UP)) {
                return false;
            }

            final IBlockState state = area.world.getBlockState(pos);
            final Block block = state.getBlock();
            if (block instanceof BlockSnow) {
                return block.getMetaFromState(state) < 7;
            }
            return (area.world.isAirBlock(pos) || state.getMaterial().isReplaceable())
                    && Blocks.SNOW_LAYER.canPlaceBlockAt(area.world, pos);
        }

        @Override
        public void affectBlock(AreaEffectEntity area, BlockPos pos) {
            final IBlockState state = area.world.getBlockState(pos);
            final Block block = state.getBlock();
            if (block instanceof BlockSnow) {
                final int layers = block.getMetaFromState(state);
                if (layers < 7) {
                    area.world.setBlockState(pos, block.getStateFromMeta(layers + 1));
                    area.world.neighborChanged(pos, block, pos);
                }
                return;
            }

            area.world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
            area.world.neighborChanged(pos, Blocks.SNOW_LAYER, pos);
        }
    }

    protected static void spawnBonemealParticles(AreaEffectEntity area, BlockPos pos, int count, double horizontalSpread, double verticalSpread) {
        if (area == null || area.world.isRemote) {
            return;
        }

        area.world.playEvent(2005, pos, 0);

        if (area.world instanceof WorldServer) {
            WorldServer world = (WorldServer) area.world;
            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
                    pos.getX() + 0.5D,
                    pos.getY() + 0.5D,
                    pos.getZ() + 0.5D,
                    Math.max(1, count),
                    horizontalSpread,
                    verticalSpread,
                    horizontalSpread,
                    0.02D);
        }
    }
}