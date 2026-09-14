package xzeroair.trinkets.traits.abilities.compat.firstaid;

import ichttt.mods.firstaid.api.CapabilityExtendedHealthSystem;
import ichttt.mods.firstaid.api.damagesystem.AbstractDamageablePart;
import ichttt.mods.firstaid.api.damagesystem.AbstractPlayerDamageModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import xzeroair.trinkets.init.TrinketsDamageSource;
import xzeroair.trinkets.traits.abilities.AbilityLinkedVitality;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityLinkedVitality;

@Interface(modid = "firstaid", iface = "xzeroair.trinkets.util.compat.firstaid.IFirstAidAbility", striprefs = true)
public class AbilityLinkedVitalityFirstAid extends AbilityLinkedVitality implements IFirstAidAbility {

    public AbilityLinkedVitalityFirstAid(ConfigAbilityLinkedVitality config) {
        super(config);
    }

    @Override
    public float damaged(EntityLivingBase attacked, DamageSource source, float damage) {
        return damage;
    }

    @Override
    @Method(modid = "firstaid")
    public boolean firstAidHit(EntityLivingBase entity, DamageSource source, float undistributedDamage, AbstractPlayerDamageModel before, AbstractPlayerDamageModel after) {
        if (!FirstAidCompat.isModEnabled() || entity.world.isRemote || this.isLinkedVitalityDamage(source)) {
            return false;
        }
        final EntityLivingBase partner = this.linkedPartner;
        if (partner == null || !partner.isEntityAlive() || !this.linkedPartnerHasAbility(entity, partner)) {
            return false;
        }
        final float totalDamage = this.getFirstAidDamage(before, after);
        if (totalDamage <= 0F) {
            return false;
        }
        float wearerSharedDamage = totalDamage * this.getPartnerDamageShare();
        float partnerDamage = wearerSharedDamage * this.getPartnerDamageMultiplier();
        final float safePartnerDamage = this.getFirstAidSafeDamageCapacity(partner);
        if (this.CONFIG.PROTECT_PARTNER && this.getPartnerDamageMultiplier() == 1F && partnerDamage > safePartnerDamage && this.canFirstAidSurviveDamage(before, after, totalDamage - safePartnerDamage)) {
            partnerDamage = safePartnerDamage;
            wearerSharedDamage = safePartnerDamage;
        }
        if (partnerDamage > 0F && !this.transferFirstAidDamage(entity, partner, partnerDamage)) {
            return false;
        }
        this.restoreFirstAidDamage(before, after, wearerSharedDamage);
        after.scheduleResync();
        this.syncPartnerInfo(entity);
        return false;
    }

    @Method(modid = "firstaid")
    protected float getFirstAidDamage(AbstractPlayerDamageModel before, AbstractPlayerDamageModel after) {
        float totalDamage = 0F;
        final AbstractDamageablePart[] beforeParts = FirstAidCompat.getBodyParts(before);
        final AbstractDamageablePart[] afterParts = FirstAidCompat.getBodyParts(after);
        for (int i = 0; i < beforeParts.length; i++) {
            totalDamage += Math.max(0F, beforeParts[i].currentHealth - afterParts[i].currentHealth);
        }
        return totalDamage;
    }

    @Method(modid = "firstaid")
    protected void restoreFirstAidDamage(AbstractPlayerDamageModel before, AbstractPlayerDamageModel after, float restoredDamage) {
        final float totalDamage = this.getFirstAidDamage(before, after);
        if (totalDamage <= 0F) {
            return;
        }
        final float restoredFraction = restoredDamage / totalDamage;
        final AbstractDamageablePart[] beforeParts = FirstAidCompat.getBodyParts(before);
        final AbstractDamageablePart[] afterParts = FirstAidCompat.getBodyParts(after);
        for (int i = 0; i < beforeParts.length; i++) {
            final float originalDamage = Math.max(0F, beforeParts[i].currentHealth - afterParts[i].currentHealth);
            afterParts[i].currentHealth = Math.min(afterParts[i].getMaxHealth(), afterParts[i].currentHealth + originalDamage * restoredFraction);
        }
    }

    @Method(modid = "firstaid")
    protected float getFirstAidSafeDamageCapacity(EntityLivingBase partner) {
        if (!(partner instanceof EntityPlayer)) {
            return this.getSafeDamageCapacity(partner);
        }
        final AbstractPlayerDamageModel model = partner.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
        if (model == null) {
            return this.getSafeDamageCapacity(partner);
        }
        float capacity = 0F;
        for (AbstractDamageablePart part : FirstAidCompat.getBodyParts(model)) {
            final float minimumHealth = part == model.HEAD || part == model.BODY ? 1F : 0F;
            capacity += Math.max(0F, part.currentHealth - minimumHealth);
        }
        return capacity;
    }

    @Method(modid = "firstaid")
    protected boolean canFirstAidSurviveDamage(AbstractPlayerDamageModel before, AbstractPlayerDamageModel after, float retainedDamage) {
        final float totalDamage = this.getFirstAidDamage(before, after);
        if (totalDamage <= 0F) {
            return false;
        }
        final float retainedFraction = retainedDamage / totalDamage;
        final AbstractDamageablePart[] beforeParts = FirstAidCompat.getBodyParts(before);
        final AbstractDamageablePart[] afterParts = FirstAidCompat.getBodyParts(after);
        for (int i = 0; i < beforeParts.length; i++) {
            final float originalDamage = Math.max(0F, beforeParts[i].currentHealth - afterParts[i].currentHealth);
            final float minimumHealth = beforeParts[i] == before.HEAD || beforeParts[i] == before.BODY ? 1F : 0F;
            if (beforeParts[i].currentHealth - originalDamage * retainedFraction < minimumHealth) {
                return false;
            }
        }
        return true;
    }

    @Method(modid = "firstaid")
    protected boolean transferFirstAidDamage(EntityLivingBase wearer, EntityLivingBase partner, float sharedDamage) {
        if (!this.CONFIG.DIRECT_DAMAGE) {
            return partner.attackEntityFrom(TrinketsDamageSource.linkedVitality, sharedDamage);
        }
        if (!(partner instanceof EntityPlayer)) {
            return this.transferPartnerDamage(wearer, partner, sharedDamage);
        }
        final AbstractPlayerDamageModel model = partner.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
        if (model == null) {
            return this.transferPartnerDamage(wearer, partner, sharedDamage);
        }
        final AbstractDamageablePart[] bodyParts = FirstAidCompat.getBodyParts(model);
        float maximumHealth = 0F;
        for (AbstractDamageablePart part : bodyParts) {
            maximumHealth += part.getMaxHealth();
        }
        if (maximumHealth <= 0F) {
            return false;
        }
        final float damageFraction = sharedDamage / maximumHealth;
        for (AbstractDamageablePart part : bodyParts) {
            final float minimumHealth = part == model.HEAD || part == model.BODY ? 1F : 0F;
            if (part.currentHealth - part.getMaxHealth() * damageFraction < minimumHealth) {
                return partner.attackEntityFrom(TrinketsDamageSource.linkedVitality, sharedDamage);
            }
        }
        for (AbstractDamageablePart part : bodyParts) {
            part.currentHealth = Math.max(0F, part.currentHealth - part.getMaxHealth() * damageFraction);
        }
        model.scheduleResync();
        this.sendDirectTransferFeedback(wearer, partner);
        return true;
    }

    @Override
    @Method(modid = "firstaid")
    protected float getLinkedPartnerHealth(EntityLivingBase partner) {
        final AbstractPlayerDamageModel model = this.getFirstAidModel(partner);
        if (model == null) {
            return super.getLinkedPartnerHealth(partner);
        }
        float health = 0F;
        for (AbstractDamageablePart part : FirstAidCompat.getBodyParts(model)) {
            health += part.currentHealth;
        }
        return health;
    }

    @Override
    @Method(modid = "firstaid")
    protected float getLinkedPartnerMaximumHealth(EntityLivingBase partner) {
        final AbstractPlayerDamageModel model = this.getFirstAidModel(partner);
        if (model == null) {
            return super.getLinkedPartnerMaximumHealth(partner);
        }
        float maximumHealth = 0F;
        for (AbstractDamageablePart part : FirstAidCompat.getBodyParts(model)) {
            maximumHealth += part.getMaxHealth();
        }
        return maximumHealth;
    }

    @Override
    @Method(modid = "firstaid")
    protected void balanceHealth(EntityLivingBase entity, EntityLivingBase partner) {
        final AbstractPlayerDamageModel entityModel = this.getFirstAidModel(entity);
        final AbstractPlayerDamageModel partnerModel = this.getFirstAidModel(partner);
        if (entityModel == null && partnerModel == null) {
            super.balanceHealth(entity, partner);
            return;
        }
        final AbstractDamageablePart[] entityParts = entityModel == null ? null : FirstAidCompat.getBodyParts(entityModel);
        final AbstractDamageablePart[] partnerParts = partnerModel == null ? null : FirstAidCompat.getBodyParts(partnerModel);
        final float entityHealth = this.getBalancedHealth(entity, entityParts);
        final float partnerHealth = this.getBalancedHealth(partner, partnerParts);
        final boolean entityIsHealthier = entityHealth >= partnerHealth;
        final EntityLivingBase healthierPartner = entityIsHealthier ? entity : partner;
        final EntityLivingBase injuredPartner = entityIsHealthier ? partner : entity;
        final AbstractPlayerDamageModel healthierModel = entityIsHealthier ? entityModel : partnerModel;
        final AbstractPlayerDamageModel injuredModel = entityIsHealthier ? partnerModel : entityModel;
        final AbstractDamageablePart[] healthierParts = entityIsHealthier ? entityParts : partnerParts;
        final AbstractDamageablePart[] injuredParts = entityIsHealthier ? partnerParts : entityParts;
        final float healthierHealth = entityIsHealthier ? entityHealth : partnerHealth;
        final float injuredHealth = entityIsHealthier ? partnerHealth : entityHealth;
        final float healthTransfer = this.getHealthBalanceTransfer(injuredHealth, this.getBalancedMaximumHealth(injuredPartner, injuredParts), healthierHealth);
        if (healthTransfer <= 0.001F) {
            return;
        }
        this.adjustBalancedHealth(healthierPartner, healthierModel, healthierParts, -healthTransfer);
        this.adjustBalancedHealth(injuredPartner, injuredModel, injuredParts, healthTransfer);
        this.syncPartnerInfo(entity);
    }

    @Method(modid = "firstaid")
    protected float getBalancedHealth(EntityLivingBase entity, AbstractDamageablePart[] bodyParts) {
        if (bodyParts == null) {
            return entity.getHealth();
        }
        float health = 0F;
        for (AbstractDamageablePart part : bodyParts) {
            health += part.currentHealth;
        }
        return health;
    }

    @Method(modid = "firstaid")
    protected float getBalancedMaximumHealth(EntityLivingBase entity, AbstractDamageablePart[] bodyParts) {
        if (bodyParts == null) {
            return entity.getMaxHealth();
        }
        float maximumHealth = 0F;
        for (AbstractDamageablePart part : bodyParts) {
            maximumHealth += part.getMaxHealth();
        }
        return maximumHealth;
    }

    @Override
    @Method(modid = "firstaid")
    protected float getSharedHealingCapacity(EntityLivingBase partner) {
        final AbstractPlayerDamageModel model = this.getFirstAidModel(partner);
        if (model == null) {
            return super.getSharedHealingCapacity(partner);
        }
        return Math.max(0F, this.getBalancedMaximumHealth(partner, FirstAidCompat.getBodyParts(model)) - this.getBalancedHealth(partner, FirstAidCompat.getBodyParts(model)));
    }

    @Override
    @Method(modid = "firstaid")
    protected void applyDirectSharedHealing(EntityLivingBase partner, float sharedHealing) {
        final AbstractPlayerDamageModel model = this.getFirstAidModel(partner);
        if (model == null) {
            super.applyDirectSharedHealing(partner, sharedHealing);
            return;
        }
        this.adjustBalancedHealth(partner, model, FirstAidCompat.getBodyParts(model), sharedHealing);
    }

    @Method(modid = "firstaid")
    protected void adjustBalancedHealth(EntityLivingBase entity, AbstractPlayerDamageModel model, AbstractDamageablePart[] bodyParts, float healthChange) {
        if (bodyParts == null) {
            entity.setHealth(entity.getHealth() + healthChange);
            return;
        }
        float availableHealth = 0F;
        for (AbstractDamageablePart part : bodyParts) {
            availableHealth += healthChange > 0F ? part.getMaxHealth() - part.currentHealth : part.currentHealth;
        }
        if (availableHealth <= 0F) {
            return;
        }
        for (AbstractDamageablePart part : bodyParts) {
            final float availablePartHealth = healthChange > 0F ? part.getMaxHealth() - part.currentHealth : part.currentHealth;
            part.currentHealth += healthChange * (availablePartHealth / availableHealth);
        }
        model.scheduleResync();
    }

    @Method(modid = "firstaid")
    protected AbstractPlayerDamageModel getFirstAidModel(EntityLivingBase entity) {
        return entity instanceof EntityPlayer ? entity.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null) : null;
    }
}
