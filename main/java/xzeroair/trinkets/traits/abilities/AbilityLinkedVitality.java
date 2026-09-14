package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.init.TrinketsDamageSource;
import xzeroair.trinkets.traits.abilities.interfaces.*;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityLinkedVitality;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nullable;
import java.util.UUID;

public class AbilityLinkedVitality extends Ability implements ITickableAbility, IAttackAbility, IHealAbility, ILifecycleAbility {

    private static final String PARTNER_ACTIVE_TAG = "LinkedPartnerActive";
    private static final String PARTNER_NAME_TAG = "LinkedPartnerName";
    private static final String PARTNER_HEALTH_TAG = "LinkedPartnerHealth";
    private static final String PARTNER_MAXIMUM_HEALTH_TAG = "LinkedPartnerMaximumHealth";
    private static final double NEARBY_PARTNER_RANGE = 32D;
    private static final int DIRECT_TRANSFER_FEEDBACK_INTERVAL = 20;
    private static final int LINK_COOLDOWN_TICKS = 20 * 10;

    protected final ConfigAbilityLinkedVitality CONFIG;
    protected UUID linkedPartnerUUID;
    protected EntityLivingBase linkedPartner;
    protected boolean linkedPartnerActive;
    protected int delayedPartnerInfoSync;
    protected String linkedPartnerName = "";
    protected float linkedPartnerHealth;
    protected float linkedPartnerMaximumHealth;
    protected int lastDirectTransferFeedbackTick = -1;
    protected int linkCooldownTicks;
    protected boolean sharingHealing;

    public AbilityLinkedVitality(ConfigAbilityLinkedVitality config) {
        super(TrinketsRegistryNames.ModAbilities.LINKED_VITALITY);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final int damageSharePercentage = Math.round(this.getPartnerDamageShare() * 100F);
        final int experiencePercentage = Math.round(this.CONFIG.EXPERIENCE_BONUS * 100F);
        final TranslationHelper.OptionEntry damageShareEntry = new TranslationHelper.OptionEntry("damageshare", true, damageSharePercentage + "%");
        final TranslationHelper.OptionEntry anyDistanceDamageEntry = new TranslationHelper.OptionEntry("damageanydistance", !this.CONFIG.REQUIRE_NEARBY_PARTNER, "");
        final TranslationHelper.OptionEntry nearbyDamageEntry = new TranslationHelper.OptionEntry("damagenearby", this.CONFIG.REQUIRE_NEARBY_PARTNER, "");
        final TranslationHelper.OptionEntry healthBalanceEntry = new TranslationHelper.OptionEntry("balancehealth", this.CONFIG.BALANCE_HEALTH, "");
        final TranslationHelper.OptionEntry experienceEntry = new TranslationHelper.OptionEntry("partnerxp", experiencePercentage > 0, experiencePercentage + "%");
        return helper.formatAddVariables(key, renderID, damageShareEntry, anyDistanceDamageEntry, nearbyDamageEntry, healthBalanceEntry, experienceEntry);
    }

    @Override
    public int killedEntityExpDrop(EntityLivingBase target, int originalExperience, int droppedExperience) {
        final EntityLivingBase owner = this.getAbilityHolder().getOwner();
        if (droppedExperience <= 0 || this.CONFIG.EXPERIENCE_BONUS <= 0F || !this.isPartnerNearby(owner)) {
            return droppedExperience;
        }
        return droppedExperience + Math.round(droppedExperience * this.CONFIG.EXPERIENCE_BONUS);
    }

    protected boolean isPartnerNearby(EntityLivingBase owner) {
        final EntityLivingBase partner = this.linkedPartner;
        return partner != null && partner.world == owner.world && partner.isEntityAlive() && this.linkedPartnerHasAbility(owner, partner) && owner.getDistanceSq(partner) <= NEARBY_PARTNER_RANGE * NEARBY_PARTNER_RANGE;
    }

    @Override
    public void onAbilityAdded(EntityLivingBase entity) {
        this.linkCooldownTicks = LINK_COOLDOWN_TICKS;
        final TrinketProperties properties = this.getLocketProperties(entity);
        this.linkedPartnerUUID = properties == null ? null : properties.getLinkedEntityUUID();
        this.setChanged(true);
        this.refreshLinkedPartner(entity);
    }

    @Override
    public void onJoinWorld(EntityLivingBase entity) {
        this.refreshLinkedPartner(entity);
    }

    @Override
    public void onChangedDimension(EntityLivingBase entity, int fromDimension, int toDimension) {
        this.refreshLinkedPartner(entity);
        this.delayedPartnerInfoSync = 2;
    }

    @Override
    public void onLogout(EntityLivingBase entity) {
        final EntityLivingBase partner = this.linkedPartner;
        this.linkedPartner = null;
        this.syncLinkedPartnerInfo(entity);
        if (partner != null) {
            final AbilityLinkedVitality partnerAbility = this.getLinkedVitalityAbility(partner);
            if (partnerAbility != null && entity.getUniqueID().equals(partnerAbility.getLinkedPartnerUUID())) {
                partnerAbility.linkedPartner = null;
                partnerAbility.syncLinkedPartnerInfo(partner);
            }
        }
    }

    @Override
    public float damaged(EntityLivingBase attacked, DamageSource source, float damage) {
        if (attacked.world.isRemote || this.isLinkedVitalityDamage(source) || this.isLinkCooldownActive()) {
            return damage;
        }
        final EntityLivingBase partner = this.linkedPartner;
        if (partner == null || !partner.isEntityAlive() || !this.linkedPartnerHasAbility(attacked, partner)) {
            return damage;
        }
        if (this.CONFIG.REQUIRE_NEARBY_PARTNER && !this.isPartnerNearby(attacked)) {
            return damage;
        }
        final AbilityLinkedVitality partnerAbility = this.getLinkedVitalityAbility(partner);
        if (partnerAbility == null || partnerAbility.isLinkCooldownActive()) {
            return damage;
        }
        float wearerSharedDamage = damage * this.getPartnerDamageShare();
        float partnerDamage = wearerSharedDamage * this.getPartnerDamageMultiplier();
        float wearerDamage = damage - wearerSharedDamage;
        final float safePartnerDamage = this.getSafeDamageCapacity(partner);
        if (this.CONFIG.PROTECT_PARTNER && this.getPartnerDamageMultiplier() == 1F && partnerDamage > safePartnerDamage && attacked.getHealth() > damage - safePartnerDamage) {
            partnerDamage = safePartnerDamage;
            wearerDamage = damage - partnerDamage;
        }
        if (partnerDamage <= 0F || this.transferPartnerDamage(attacked, partner, partnerDamage)) {
            this.syncPartnerInfo(attacked);
            return wearerDamage;
        }
        return damage;
    }

    @Override
    public float onHeal(EntityLivingBase entity, float healAmount) {
        if (entity.world.isRemote || this.isLinkCooldownActive() || this.sharingHealing || !this.CONFIG.SHARE_HEALING || healAmount <= 0F) {
            return healAmount;
        }
        final EntityLivingBase partner = this.linkedPartner;
        if (partner == null || !partner.isEntityAlive() || !this.linkedPartnerHasAbility(entity, partner) || this.CONFIG.REQUIRE_NEARBY_PARTNER && !this.isPartnerNearby(entity)) {
            return healAmount;
        }
        final AbilityLinkedVitality partnerAbility = this.getLinkedVitalityAbility(partner);
        if (partnerAbility == null || partnerAbility.isLinkCooldownActive()) {
            return healAmount;
        }
        final float sharedHealing = Math.min(healAmount * this.getPartnerHealingShare(), this.getSharedHealingCapacity(partner));
        if (sharedHealing <= 0F) {
            return healAmount;
        }
        this.sharingHealing = true;
        partnerAbility.sharingHealing = true;
        try {
            if (this.CONFIG.DIRECT_HEAL) {
                this.applyDirectSharedHealing(partner, sharedHealing);
            } else {
                partner.heal(sharedHealing);
            }
        } finally {
            partnerAbility.sharingHealing = false;
            this.sharingHealing = false;
        }
        this.syncPartnerInfo(entity);
        return healAmount - sharedHealing;
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (entity.world.isRemote) {
            return;
        }
        if (this.delayedPartnerInfoSync > 0 && --this.delayedPartnerInfoSync == 0) {
            this.setChanged(true);
        }
        if (this.hasChanged()) {
            this.syncLinkedPartnerInfo(entity);
        }
        if (this.linkCooldownTicks > 0) {
            this.linkCooldownTicks--;
            return;
        }
        final EntityLivingBase partner = this.linkedPartner;
        if (partner == null || !partner.isEntityAlive() || !this.linkedPartnerHasAbility(entity, partner)) {
            return;
        }
        final AbilityLinkedVitality partnerAbility = this.getLinkedVitalityAbility(partner);
        if (partnerAbility == null || partnerAbility.isLinkCooldownActive()) {
            return;
        }
        if (this.CONFIG.REQUIRE_NEARBY_PARTNER && !this.isPartnerNearby(entity)) {
            return;
        }
        if (this.CONFIG.BALANCE_HEALTH && entity.getUniqueID().compareTo(partner.getUniqueID()) <= 0) {
            this.balanceHealth(entity, partner);
        }
    }

    protected boolean isLinkCooldownActive() {
        return this.linkCooldownTicks > 0;
    }

    protected float getPartnerDamageShare() {
        return Math.max(0F, Math.min(1F, this.CONFIG.DAMAGE_SHARE));
    }

    protected float getPartnerDamageMultiplier() {
        return Math.max(0F, this.CONFIG.PARTNER_DAMAGE_MULTIPLIER);
    }

    protected float getPartnerHealingShare() {
        return Math.max(0F, Math.min(1F, this.CONFIG.HEAL_SHARE));
    }

    protected float getSharedHealingCapacity(EntityLivingBase partner) {
        return Math.max(0F, partner.getMaxHealth() - partner.getHealth());
    }

    protected void applyDirectSharedHealing(EntityLivingBase partner, float sharedHealing) {
        partner.setHealth(Math.min(partner.getMaxHealth(), partner.getHealth() + sharedHealing));
    }

    protected void balanceHealth(EntityLivingBase entity, EntityLivingBase partner) {
        final EntityLivingBase healthierPartner = entity.getHealth() >= partner.getHealth() ? entity : partner;
        final EntityLivingBase injuredPartner = healthierPartner == entity ? partner : entity;
        final float healthTransfer = this.getHealthBalanceTransfer(injuredPartner.getHealth(), injuredPartner.getMaxHealth(), healthierPartner.getHealth());
        if (healthTransfer <= 0.001F) {
            return;
        }
        healthierPartner.setHealth(healthierPartner.getHealth() - healthTransfer);
        injuredPartner.setHealth(injuredPartner.getHealth() + healthTransfer);
        this.syncPartnerInfo(entity);
    }

    protected float getHealthBalanceTransfer(float injuredHealth, float injuredMaximumHealth, float healthierHealth) {
        if (healthierHealth <= injuredHealth || injuredMaximumHealth <= injuredHealth) {
            return 0F;
        }
        return Math.min((healthierHealth - injuredHealth) * 0.5F, injuredMaximumHealth - injuredHealth);
    }

    protected boolean isLinkedVitalityDamage(DamageSource source) {
        return source == TrinketsDamageSource.linkedVitality;
    }

    protected float getSafeDamageCapacity(EntityLivingBase entity) {
        return Math.max(0F, entity.getHealth() - 1F);
    }

    protected boolean transferPartnerDamage(EntityLivingBase wearer, EntityLivingBase partner, float sharedDamage) {
        if (this.CONFIG.DIRECT_DAMAGE && sharedDamage <= this.getSafeDamageCapacity(partner)) {
            partner.setHealth(partner.getHealth() - sharedDamage);
            this.sendDirectTransferFeedback(wearer, partner);
            return true;
        }
        return partner.attackEntityFrom(TrinketsDamageSource.linkedVitality, sharedDamage);
    }

    protected void sendDirectTransferFeedback(EntityLivingBase wearer, EntityLivingBase partner) {
        if (this.lastDirectTransferFeedbackTick >= 0 && wearer.ticksExisted - this.lastDirectTransferFeedbackTick < DIRECT_TRANSFER_FEEDBACK_INTERVAL) {
            return;
        }
        this.lastDirectTransferFeedbackTick = wearer.ticksExisted;
        if (wearer instanceof EntityPlayer) {
            ((EntityPlayer) wearer).sendStatusMessage(new TextComponentTranslation(this.getTranslationKey() + ".direct_share", partner.getDisplayName()), true);
        }
        if (partner instanceof EntityPlayer) {
            ((EntityPlayer) partner).sendStatusMessage(new TextComponentTranslation(this.getTranslationKey() + ".direct_received", wearer.getDisplayName()), true);
        }
    }

    @Nullable
    protected TrinketProperties getLocketProperties(EntityLivingBase entity) {
        if (this.getAbilityHolder() == null) {
            return null;
        }
        return Capabilities.getTrinketProperties(this.getAbilityHolder().getInfo().getStackFromHandler(entity));
    }

    protected void refreshLinkedPartner(EntityLivingBase entity) {
        this.linkedPartner = this.getLinkedPartner(entity);
        if (entity.world.isRemote) {
            return;
        }
        this.syncLinkedPartnerInfo(entity);
        if (this.linkedPartner != null) {
            final AbilityLinkedVitality partnerAbility = this.getLinkedVitalityAbility(this.linkedPartner);
            if (partnerAbility != null && entity.getUniqueID().equals(partnerAbility.getLinkedPartnerUUID())) {
                partnerAbility.linkedPartner = entity;
                partnerAbility.syncLinkedPartnerInfo(this.linkedPartner);
            }
        }
    }

    @Nullable
    protected EntityLivingBase getLinkedPartner(EntityLivingBase entity) {
        if (entity.getServer() == null || this.linkedPartnerUUID == null) {
            return null;
        }
        for (WorldServer world : entity.getServer().worlds) {
            final Entity partner = world.getEntityFromUuid(this.linkedPartnerUUID);
            if (partner instanceof EntityLivingBase) {
                return (EntityLivingBase) partner;
            }
        }
        return null;
    }

    @Nullable
    protected AbilityLinkedVitality getLinkedVitalityAbility(EntityLivingBase entity) {
        if (entity == null) {
            return null;
        }
        return Capabilities.getEntityProperties(entity, null, (properties, result) -> {
            final IAbilityInterface ability = properties.getAbilityHandler()
                    .getAbility(this.getRegistryName().toString());
            return ability instanceof AbilityLinkedVitality ? (AbilityLinkedVitality) ability : null;
        });
    }

    protected void syncPartnerInfo(EntityLivingBase entity) {
        this.setChanged(true);
        final AbilityLinkedVitality partnerAbility = this.getLinkedVitalityAbility(this.linkedPartner);
        if (partnerAbility != null) {
            partnerAbility.setChanged(true);
        }
    }

    protected void syncLinkedPartnerInfo(EntityLivingBase entity) {
        final EntityLivingBase partner = this.linkedPartner;
        final boolean active = partner != null && partner.isEntityAlive() && this.linkedPartnerHasAbility(entity, partner);
        final String name = active ? partner.getDisplayName().getUnformattedText() : "";
        final float health = active ? this.getLinkedPartnerHealth(partner) : 0F;
        final float maximumHealth = active ? this.getLinkedPartnerMaximumHealth(partner) : 0F;
        if (this.linkedPartnerActive == active && this.linkedPartnerName.equals(name) && Float.compare(this.linkedPartnerHealth, health) == 0 && Float.compare(this.linkedPartnerMaximumHealth, maximumHealth) == 0) {
            return;
        }
        this.linkedPartnerActive = active;
        this.linkedPartnerName = name;
        this.linkedPartnerHealth = health;
        this.linkedPartnerMaximumHealth = maximumHealth;
        this.setChanged(true);
    }

    protected float getLinkedPartnerHealth(EntityLivingBase partner) {
        return partner.getHealth();
    }

    protected float getLinkedPartnerMaximumHealth(EntityLivingBase partner) {
        return partner.getMaxHealth();
    }

    public UUID getLinkedPartnerUUID() {
        return this.linkedPartnerUUID;
    }

    public boolean isLinkedPartnerActive() {
        return this.linkedPartnerActive;
    }

    public String getLinkedPartnerName() {
        return this.linkedPartnerName;
    }

    public float getLinkedPartnerHealth() {
        return this.linkedPartnerHealth;
    }

    public float getLinkedPartnerMaximumHealth() {
        return this.linkedPartnerMaximumHealth;
    }

    protected boolean linkedPartnerHasAbility(EntityLivingBase wearer, EntityLivingBase partner) {
        if (this.linkedPartnerUUID == null) {
            return false;
        }
        final AbilityLinkedVitality partnerAbility = this.getLinkedVitalityAbility(partner);
        return partnerAbility != null && wearer.getUniqueID().equals(partnerAbility.getLinkedPartnerUUID());
    }

    @Override
    public NBTTagCompound sendAbilityData() {
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean(PARTNER_ACTIVE_TAG, this.linkedPartnerActive);
        tag.setString(PARTNER_NAME_TAG, this.linkedPartnerName);
        tag.setFloat(PARTNER_HEALTH_TAG, this.linkedPartnerHealth);
        tag.setFloat(PARTNER_MAXIMUM_HEALTH_TAG, this.linkedPartnerMaximumHealth);
        return tag;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        NBTHelper.hasBoolean(tag, PARTNER_ACTIVE_TAG, (bool) -> linkedPartnerActive = bool);
        NBTHelper.hasString(tag, PARTNER_NAME_TAG, (s) -> linkedPartnerName = s);
        NBTHelper.hasFloat(tag, PARTNER_HEALTH_TAG, (value) -> linkedPartnerHealth = value);
        NBTHelper.hasFloat(tag, PARTNER_MAXIMUM_HEALTH_TAG, (value) -> linkedPartnerMaximumHealth = value);
    }
}
