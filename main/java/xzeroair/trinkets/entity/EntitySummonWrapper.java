package xzeroair.trinkets.entity;

import com.google.common.base.Optional;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.entity.ai.EntityAIKnightFollowSummoner;
import xzeroair.trinkets.entity.ai.EntityAIKnightTarget;
import xzeroair.trinkets.entity.ai.EntityAIResponsiveMeleeAttack;
import xzeroair.trinkets.entity.wrapper.EntityWrapperBehaviorTemplate;
import xzeroair.trinkets.entity.wrapper.EntityWrapperBehaviorTemplates;
import xzeroair.trinkets.entity.wrapper.ShoulderDismountMode;
import xzeroair.trinkets.util.helpers.NBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A living world entity that presents a supported contained entity through renderer delegation.
 *
 * <p>This entity owns its world presence and future replacement behavior. The contained entity is
 * never added to the world; it only preserves controlled source state and provides the concrete
 * type required by the selected source renderer.</p>
 */
public class EntitySummonWrapper extends EntityCreature implements IEntityOwnable {

    private static final String SOURCE_ID_TAG = "SourceId";
    private static final String SOURCE_DATA_TAG = "SourceData";
    private static final String SUMMONER_UUID_TAG = "SummonerUUID";
    private static final String TEMPORARY_SUMMON_TAG = "TemporarySummon";
    private static final String PERSISTENT_SUMMON_ID_TAG = "PersistentSummonId";
    private static final String BEHAVIOR_TEMPLATE_TAG = "BehaviorTemplate";
    private static final String PRESENTATION_COLOR_TAG = "PresentationColor";
    private static final String SHOULDER_SLOT_TAG = "ShoulderSlot";
    private static final byte NO_SHOULDER_SLOT = 0;
    private static final byte LEFT_SHOULDER_SLOT = 1;
    private static final byte RIGHT_SHOULDER_SLOT = 2;
    private static final float KNIGHT_ATTACK_DAMAGE = 2.0F;
    private static final DataParameter<String> SOURCE_ID = EntityDataManager.createKey(EntitySummonWrapper.class, DataSerializers.STRING);
    private static final DataParameter<NBTTagCompound> SOURCE_DATA = EntityDataManager.createKey(EntitySummonWrapper.class, DataSerializers.COMPOUND_TAG);
    private static final DataParameter<String> BEHAVIOR_TEMPLATE = EntityDataManager.createKey(EntitySummonWrapper.class, DataSerializers.STRING);
    private static final DataParameter<Integer> PRESENTATION_COLOR = EntityDataManager.createKey(EntitySummonWrapper.class, DataSerializers.VARINT);
    private static final DataParameter<Float> ACTIVE_WIDTH = EntityDataManager.createKey(EntitySummonWrapper.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> ACTIVE_HEIGHT = EntityDataManager.createKey(EntitySummonWrapper.class, DataSerializers.FLOAT);
    private static final DataParameter<Optional<UUID>> SUMMONER_UUID = EntityDataManager.createKey(EntitySummonWrapper.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Byte> SHOULDER_SLOT = EntityDataManager.createKey(EntitySummonWrapper.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> COLLISION_GRACE_TICKS = EntityDataManager.createKey(EntitySummonWrapper.class, DataSerializers.VARINT);

    @Nullable
    private EntityLivingBase sourceEntity;
    @Nullable
    private UUID summonerUuid;
    private float previousActiveWidth = 0.6F;
    private float previousActiveHeight = 1.8F;
    private float visualActiveWidth = 0.6F;
    private float visualActiveHeight = 1.8F;
    private boolean temporarySummon = true;
    @Nullable
    private UUID persistentSummonId;

    public EntitySummonWrapper(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
    }

    private static boolean isUsableSourceType(@Nonnull ResourceLocation sourceId) {
        if (!EntityList.isRegistered(sourceId)) {
            return false;
        }
        final Class<? extends Entity> entityClass = EntityList.getClass(sourceId);
        return entityClass != null && EntityLivingBase.class.isAssignableFrom(entityClass) && !EntitySummonWrapper.class.isAssignableFrom(entityClass);
    }

    /**
     * Configures this wrapper from a source living entity after the requesting mechanic has applied
     * its own source whitelist policy.
     */
    public boolean setSource(@Nonnull EntityLivingBase source) {
        final ResourceLocation sourceId = EntityList.getKey(source);
        if (sourceId == null) {
            return false;
        }

        final NBTTagCompound sourceData = new NBTTagCompound();
        source.writeToNBT(sourceData);
        return this.setSource(sourceId, sourceData);
    }

    /**
     * Configures this wrapper from a previously saved living source entity.
     */
    public boolean setSource(@Nonnull ResourceLocation sourceId, @Nonnull NBTTagCompound sourceData) {
        if (!isUsableSourceType(sourceId)) {
            return false;
        }

        final NBTTagCompound storedData = sourceData.copy();
        storedData.setString("id", sourceId.toString());
        final Entity source = EntityList.createEntityFromNBT(storedData, this.world);
        if (!(source instanceof EntityLivingBase)) {
            return false;
        }

        this.sourceEntity = (EntityLivingBase) source;
        this.adoptSourceProperties(this.sourceEntity);
        this.getDataManager().set(SOURCE_ID, sourceId.toString());
        this.getDataManager().set(SOURCE_DATA, storedData);
        return true;
    }

    private void adoptSourceProperties(@Nonnull EntityLivingBase source) {
        this.setActiveSize(source.width, source.height);
        for (final IAttributeInstance sourceAttribute : source.getAttributeMap().getAllAttributes()) {
            final IAttribute attribute = sourceAttribute.getAttribute();
            IAttributeInstance wrapperAttribute = this.getAttributeMap().getAttributeInstance(attribute);
            if (wrapperAttribute == null) {
                wrapperAttribute = this.getAttributeMap().registerAttribute(attribute);
            }
            for (final AttributeModifier modifier : new ArrayList<>(wrapperAttribute.getModifiers())) {
                wrapperAttribute.removeModifier(modifier);
            }
            wrapperAttribute.setBaseValue(sourceAttribute.getBaseValue());
            for (final AttributeModifier modifier : sourceAttribute.getModifiers()) {
                wrapperAttribute.applyModifier(modifier);
            }
        }
        this.setHealth(Math.min(source.getHealth(), this.getMaxHealth()));
    }

    public boolean hasSource() {
        return this.getSourceId() != null && NBTHelper.hasKey(this.getDataManager().get(SOURCE_DATA), "id", 8);
    }

    @Nullable
    public ResourceLocation getSourceId() {
        final String id = this.getDataManager().get(SOURCE_ID);
        if (id.isEmpty()) {
            return null;
        }
        try {
            return new ResourceLocation(id);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    @Nonnull
    public NBTTagCompound getSourceData() {
        return this.getDataManager().get(SOURCE_DATA).copy();
    }

    @Nullable
    public EntityLivingBase getSourceEntity() {
        if (this.sourceEntity == null) {
            final ResourceLocation sourceId = this.getSourceId();
            final NBTTagCompound sourceData = this.getDataManager().get(SOURCE_DATA);
            if (sourceId == null || !isUsableSourceType(sourceId) || !NBTHelper.hasKey(sourceData, "id", 8)) {
                return null;
            }
            final Entity source = EntityList.createEntityFromNBT(sourceData, this.world);
            if (source instanceof EntityLivingBase) {
                this.sourceEntity = (EntityLivingBase) source;
                this.setSize(this.getActiveWidth(), this.getActiveHeight());
            }
        }
        return this.sourceEntity;
    }

    public boolean setBehaviorTemplate(@Nonnull ResourceLocation templateId) {
        final EntityWrapperBehaviorTemplate template = EntityWrapperBehaviorTemplates.get(templateId);
        if (template == null) {
            return false;
        }
        this.getDataManager().set(BEHAVIOR_TEMPLATE, templateId.toString());
        template.apply(this);
        return true;
    }

    @Nullable
    public ResourceLocation getBehaviorTemplateId() {
        final String templateId = this.getDataManager().get(BEHAVIOR_TEMPLATE);
        if (templateId.isEmpty()) {
            return null;
        }
        try {
            return new ResourceLocation(templateId);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public void setPresentationColor(int color) {
        this.getDataManager().set(PRESENTATION_COLOR, color & 0xFFFFFF);
    }

    public int getPresentationColor() {
        return this.getDataManager().get(PRESENTATION_COLOR);
    }

    public void setActiveSize(float width, float height) {
        final double x = this.posX;
        final double y = this.posY;
        final double z = this.posZ;
        this.setSize(width, height);
        this.setPosition(x, y, z);
        this.getDataManager().set(ACTIVE_WIDTH, width);
        this.getDataManager().set(ACTIVE_HEIGHT, height);
    }

    public float getActiveWidth() {
        return this.getDataManager().get(ACTIVE_WIDTH);
    }

    public float getActiveHeight() {
        return this.getDataManager().get(ACTIVE_HEIGHT);
    }

    public float getPreviousActiveWidth() {
        return this.previousActiveWidth;
    }

    public float getPreviousActiveHeight() {
        return this.previousActiveHeight;
    }

    public float getVisualActiveWidth() {
        return this.visualActiveWidth;
    }

    public float getVisualActiveHeight() {
        return this.visualActiveHeight;
    }

    @Nullable
    private EntityWrapperBehaviorTemplate getBehaviorTemplate() {
        final ResourceLocation templateId = this.getBehaviorTemplateId();
        return templateId == null ? null : EntityWrapperBehaviorTemplates.get(templateId);
    }

    private void setShoulderSlot(byte shoulderSlot) {
        this.getDataManager().set(SHOULDER_SLOT, shoulderSlot);
        final boolean perched = shoulderSlot != NO_SHOULDER_SLOT;
        this.noClip = perched;
        this.setNoAI(perched);
        if (perched) {
            this.getNavigator().clearPath();
            this.setAttackTarget(null);
        }
    }

    private boolean isShoulderSlotAvailable(EntityPlayer summoner, byte shoulderSlot) {
        if ((shoulderSlot == LEFT_SHOULDER_SLOT && !summoner.getLeftShoulderEntity()
                                                            .isEmpty()) || (shoulderSlot == RIGHT_SHOULDER_SLOT && !summoner.getRightShoulderEntity()
                                                                                                                            .isEmpty())) {
            return false;
        }
        for (final EntitySummonWrapper wrapper : this.world.getEntitiesWithinAABB(EntitySummonWrapper.class, summoner.getEntityBoundingBox()
                                                                                                                     .grow(1.0D))) {
            if (wrapper != this && shoulderSlot == wrapper.getDataManager()
                                                          .get(SHOULDER_SLOT) && this.summonerUuid != null && this.summonerUuid.equals(wrapper.summonerUuid)) {
                return false;
            }
        }
        return true;
    }

    private boolean canPerchOn(EntityPlayer summoner) {
        return !summoner.isSpectator() && !summoner.isPlayerSleeping();
    }

    private boolean shouldDismountFromShoulder(EntityWrapperBehaviorTemplate template, EntityPlayer summoner) {
        if (!this.canPerchOn(summoner)) {
            return true;
        }
        if (template.getShoulderDismountMode(this, summoner) == ShoulderDismountMode.VANILLA) {
            return summoner.fallDistance > 0.5F || summoner.isInWater() || summoner.isRiding() || summoner.capabilities.isFlying;
        }
        return summoner.isSneaking();
    }

    private boolean tryStartShoulderPerch(EntityWrapperBehaviorTemplate template, EntityPlayer player) {
        if (!template.canPerchOnSummoner(this, player) || this.isPerchedOnSummoner() || this.shouldDismountFromShoulder(template, player)) {
            return false;
        }
        final byte shoulderSlot = this.isShoulderSlotAvailable(player, LEFT_SHOULDER_SLOT) ? LEFT_SHOULDER_SLOT : this.isShoulderSlotAvailable(player, RIGHT_SHOULDER_SLOT) ? RIGHT_SHOULDER_SLOT : NO_SHOULDER_SLOT;
        if (shoulderSlot == NO_SHOULDER_SLOT) {
            return false;
        }
        this.setShoulderSlot(shoulderSlot);
        return true;
    }

    private void updateShoulderPerch(EntityWrapperBehaviorTemplate template, EntityLivingBase summoner) {
        if (!this.isPerchedOnSummoner()) {
            return;
        }
        if (!template.canPerchOnSummoner(this, summoner) || !(summoner instanceof EntityPlayer)) {
            this.setShoulderSlot(NO_SHOULDER_SLOT);
            return;
        }

        final EntityPlayer player = (EntityPlayer) summoner;
        byte shoulderSlot = this.getDataManager().get(SHOULDER_SLOT);
        if (this.shouldDismountFromShoulder(template, player)) {
            this.setShoulderSlot(NO_SHOULDER_SLOT);
            return;
        }
        if (!this.isShoulderSlotAvailable(player, shoulderSlot)) {
            shoulderSlot = shoulderSlot == LEFT_SHOULDER_SLOT ? RIGHT_SHOULDER_SLOT : LEFT_SHOULDER_SLOT;
            if (!this.isShoulderSlotAvailable(player, shoulderSlot)) {
                this.setShoulderSlot(NO_SHOULDER_SLOT);
                return;
            }
            this.setShoulderSlot(shoulderSlot);
        }

        final float yaw = player.renderYawOffset * 0.017453292F;
        final double shoulderOffset = player.width * 0.6D * (shoulderSlot == LEFT_SHOULDER_SLOT ? 1D : -1D);
        final double x = player.posX + MathHelper.cos(yaw) * shoulderOffset;
        final double y = player.posY + player.height * 0.75D;
        final double z = player.posZ + MathHelper.sin(yaw) * shoulderOffset;
        this.setPositionAndRotation(x, y, z, player.renderYawOffset, 0F);
        this.motionX = 0D;
        this.motionY = 0D;
        this.motionZ = 0D;
        this.fallDistance = 0F;
    }

    public void setMoveHelper(@Nonnull EntityMoveHelper moveHelper) {
        this.moveHelper = moveHelper;
    }

    public void setAttackDamage(float attackDamage) {
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(attackDamage);
    }

    public void setSummonHealth(float health) {
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
        this.setHealth(health);
    }

    public void killedEntity(EntityLivingBase target) {
        final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
        if (template != null) {
            template.killedEntity(this, target);
        }
    }

    public void pathFailed(EntityLivingBase target) {
        final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
        if (template != null) {
            template.pathFailed(this, target);
        }
    }

    public void ownerUnavailable(EntityLivingBase summoner) {
        final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
        if (template != null) {
            template.ownerUnavailable(this, summoner);
        }
    }

    public void ownerChangingDimension(EntityLivingBase summoner) {
        final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
        if (template != null) {
            template.ownerChangingDimension(this, summoner);
        }
    }

    public void died() {
        final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
        if (template != null) {
            template.died(this);
        }
    }

    public boolean suppressMovement() {
        final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
        return template != null && template.suppressMovement(this);
    }

    public float getFollowTeleportDistance(EntityLivingBase summoner) {
        final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
        return template == null ? -1.0F : template.getFollowTeleportDistance(this, summoner);
    }

    @Override
    public ITextComponent getDisplayName() {
        final EntityLivingBase source = this.getSourceEntity();
        return source == null ? super.getDisplayName() : source.getDisplayName();
    }

    @Override
    public String getName() {
        final EntityLivingBase source = this.getSourceEntity();
        return source == null ? super.getName() : source.getName();
    }

    public void setSummoner(@Nonnull EntityLivingBase summoner) {
        this.summonerUuid = summoner.getUniqueID();
        this.getDataManager().set(SUMMONER_UUID, Optional.of(this.summonerUuid));
    }

    public void setTemporarySummon(boolean temporarySummon) {
        this.temporarySummon = temporarySummon;
    }

    public boolean isTemporarySummon() {
        return this.temporarySummon;
    }

    public void setPersistentSummonId(@Nonnull UUID persistentSummonId) {
        this.persistentSummonId = persistentSummonId;
    }

    @Nullable
    public UUID getPersistentSummonId() {
        return this.persistentSummonId;
    }

    public boolean isSummonedBy(EntityLivingBase summoner) {
        return this.summonerUuid != null && this.summonerUuid.equals(summoner.getUniqueID());
    }

    public void setCollisionGraceTicks(int ticks) {
        this.getDataManager().set(COLLISION_GRACE_TICKS, Math.max(0, ticks));
    }

    private boolean hasCollisionGrace() {
        return this.getDataManager().get(COLLISION_GRACE_TICKS) > 0;
    }

    @Nullable
    public EntityLivingBase getSummoner() {
        if (this.summonerUuid == null || !(this.world instanceof WorldServer)) {
            return null;
        }
        final Entity entity = ((WorldServer) this.world).getEntityFromUuid(this.summonerUuid);
        return entity instanceof EntityLivingBase && entity.isEntityAlive() ? (EntityLivingBase) entity : null;
    }

    @Override
    @Nullable
    public UUID getOwnerId() {
        return this.summonerUuid;
    }

    @Override
    @Nullable
    public EntityLivingBase getOwner() {
        return this.getSummoner();
    }

    public boolean isPerchedOnSummoner() {
        return this.getDataManager().get(SHOULDER_SLOT) != NO_SHOULDER_SLOT;
    }

    public boolean isPerchedOn(EntityPlayer player) {
        final Optional<UUID> summonerId = this.getDataManager().get(SUMMONER_UUID);
        return this.isPerchedOnSummoner() && summonerId.isPresent() && summonerId.get().equals(player.getUniqueID());
    }

    public boolean isPerchedOnLeftShoulder() {
        return this.getDataManager().get(SHOULDER_SLOT) == LEFT_SHOULDER_SLOT;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (this.world.isRemote) {
            return true;
        }
        if (player != this.getSummoner()) {
            return false;
        }
        final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
        if (template == null) {
            return false;
        }
        return template.processInteract(this, player, hand) || this.tryStartShoulderPerch(template, player);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isPerchedOnSummoner() && !this.hasCollisionGrace() && super.canBeCollidedWith();
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        if (!this.isPerchedOnSummoner() && !this.hasCollisionGrace()) {
            super.applyEntityCollision(entity);
        }
    }

    @Override
    protected void collideWithNearbyEntities() {
        if (!this.isPerchedOnSummoner() && !this.hasCollisionGrace()) {
            super.collideWithNearbyEntities();
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        final IAttributeInstance attackDamage = this.getAttributeMap()
                                                    .registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        attackDamage.setBaseValue(KNIGHT_ATTACK_DAMAGE);
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        this.setLastAttackedEntity(target);
        return target.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
                                                                                      .getAttributeValue());
    }

    private boolean isTemporarySummonValid() {
        final EntityLivingBase summoner = this.getSummoner();
        if (summoner == null) {
            return false;
        }
        final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
        return template == null || template.isSummonerValid(this, summoner);
    }

    @Override
    public void onUpdate() {
        if (this.world.isRemote) {
            this.previousActiveWidth = this.visualActiveWidth;
            this.previousActiveHeight = this.visualActiveHeight;
            this.visualActiveWidth += (this.getActiveWidth() - this.visualActiveWidth) * 0.5F;
            this.visualActiveHeight += (this.getActiveHeight() - this.visualActiveHeight) * 0.5F;
        }
        super.onUpdate();
        if (!this.world.isRemote) {
            if (this.hasCollisionGrace()) {
                this.setCollisionGraceTicks(this.getDataManager().get(COLLISION_GRACE_TICKS) - 1);
            }
            final EntityLivingBase summoner = this.getSummoner();
            if (this.temporarySummon && (summoner == null || !this.isTemporarySummonValid())) {
                this.setDead();
                return;
            }
            final EntityWrapperBehaviorTemplate template = this.getBehaviorTemplate();
            if (summoner != null && template != null) {
                template.tick(this, summoner);
                this.updateShoulderPerch(template, summoner);
            }
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(SOURCE_ID, "");
        this.getDataManager().register(SOURCE_DATA, new NBTTagCompound());
        this.getDataManager().register(BEHAVIOR_TEMPLATE, "");
        this.getDataManager().register(PRESENTATION_COLOR, -1);
        this.getDataManager().register(ACTIVE_WIDTH, 0.6F);
        this.getDataManager().register(ACTIVE_HEIGHT, 1.8F);
        this.getDataManager().register(SUMMONER_UUID, Optional.absent());
        this.getDataManager().register(SHOULDER_SLOT, NO_SHOULDER_SLOT);
        this.getDataManager().register(COLLISION_GRACE_TICKS, 0);
    }

    @Override
    protected void initEntityAI() {
        this.targetTasks.addTask(1, new EntityAIKnightTarget(this));
        this.tasks.addTask(2, new EntityAIResponsiveMeleeAttack(this, 1.0D));
        this.tasks.addTask(3, new EntityAIKnightFollowSummoner(this));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        final double savedMaximumHealth = this.getMaxHealth();
        final float savedHealth = this.getHealth();
        ResourceLocation templateId = null;
        if (compound.hasKey(BEHAVIOR_TEMPLATE_TAG, 8)) {
            try {
                templateId = new ResourceLocation(compound.getString(BEHAVIOR_TEMPLATE_TAG));
            } catch (IllegalArgumentException exception) {
                Trinkets.LOGGER.warn("Ignoring invalid entity-wrapper behavior template {}.", compound.getString(BEHAVIOR_TEMPLATE_TAG), exception);
            }
        }
        this.sourceEntity = null;
        this.summonerUuid = compound.hasUniqueId(SUMMONER_UUID_TAG) ? compound.getUniqueId(SUMMONER_UUID_TAG) : null;
        this.getDataManager().set(SUMMONER_UUID, Optional.fromNullable(this.summonerUuid));
        this.temporarySummon = !compound.hasKey(TEMPORARY_SUMMON_TAG) || compound.getBoolean(TEMPORARY_SUMMON_TAG);
        this.persistentSummonId = compound.hasUniqueId(PERSISTENT_SUMMON_ID_TAG) ? compound.getUniqueId(PERSISTENT_SUMMON_ID_TAG) : null;
        if (compound.hasKey(PRESENTATION_COLOR_TAG, 3)) {
            this.setPresentationColor(compound.getInteger(PRESENTATION_COLOR_TAG));
        }
        if (NBTHelper.hasKey(compound, SHOULDER_SLOT_TAG, Constants.NBT.TAG_BYTE)) {
            final byte shoulderSlot = compound.getByte(SHOULDER_SLOT_TAG);
            this.setShoulderSlot(shoulderSlot == LEFT_SHOULDER_SLOT || shoulderSlot == RIGHT_SHOULDER_SLOT ? shoulderSlot : NO_SHOULDER_SLOT);
        }
        if (!NBTHelper.hasTagCompound(compound, SOURCE_DATA_TAG) || !compound.hasKey(SOURCE_ID_TAG, 8)) {
            return;
        }

        try {
            this.setSource(new ResourceLocation(compound.getString(SOURCE_ID_TAG)), compound.getCompoundTag(SOURCE_DATA_TAG));
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(savedMaximumHealth);
            this.setHealth(Math.min(savedHealth, (float) savedMaximumHealth));
            if (templateId != null) {
                this.setBehaviorTemplate(templateId);
            }
        } catch (IllegalArgumentException exception) {
            Trinkets.LOGGER.warn("Ignoring invalid entity-wrapper source id {}.", compound.getString(SOURCE_ID_TAG), exception);
        }
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return this.temporarySummon && super.writeToNBTOptional(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean(TEMPORARY_SUMMON_TAG, this.temporarySummon);
        if (this.persistentSummonId != null) {
            compound.setUniqueId(PERSISTENT_SUMMON_ID_TAG, this.persistentSummonId);
        }
        final ResourceLocation templateId = this.getBehaviorTemplateId();
        if (templateId != null) {
            compound.setString(BEHAVIOR_TEMPLATE_TAG, templateId.toString());
        }
        if (this.getPresentationColor() >= 0) {
            compound.setInteger(PRESENTATION_COLOR_TAG, this.getPresentationColor());
        }
        if (this.isPerchedOnSummoner()) {
            compound.setByte(SHOULDER_SLOT_TAG, this.getDataManager().get(SHOULDER_SLOT));
        }
        if (this.summonerUuid != null) {
            compound.setUniqueId(SUMMONER_UUID_TAG, this.summonerUuid);
        }
        final ResourceLocation sourceId = this.getSourceId();
        if (sourceId != null && this.hasSource()) {
            compound.setString(SOURCE_ID_TAG, sourceId.toString());
            compound.setTag(SOURCE_DATA_TAG, this.getSourceData());
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (SOURCE_ID.equals(key) || SOURCE_DATA.equals(key)) {
            this.sourceEntity = null;
        }
        if (ACTIVE_WIDTH.equals(key) || ACTIVE_HEIGHT.equals(key)) {
            this.setSize(this.getActiveWidth(), this.getActiveHeight());
        }
        super.notifyDataManagerChange(key);
    }
}
