package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBouncy;
import xzeroair.trinkets.util.helpers.SoundHelper;

import javax.annotation.Nonnull;

public class AbilityBouncy extends Ability implements ITickableAbility, IJumpAbility {

    protected final ConfigAbilityBouncy CONFIG;
    protected float fallingVelocity;
    protected float pendingBounceVelocity;
    protected float lastBounceVelocity;
    protected float pendingFallDistance;
    protected double airborneMotionX, airborneMotionZ;

    public AbilityBouncy(ConfigAbilityBouncy config) {
        super(TrinketsRegistryNames.ModAbilities.BOUNCY);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    public void tickAbility(@Nonnull EntityLivingBase entity) {
        if (this.pendingBounceVelocity > 0F) {
            entity.motionX = this.airborneMotionX;
            entity.motionY = this.pendingBounceVelocity;
            entity.motionZ = this.airborneMotionZ;
            entity.onGround = false;
            entity.velocityChanged = true;
            SoundHelper.playSound(entity, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            this.lastBounceVelocity = this.pendingBounceVelocity;
            this.pendingBounceVelocity = 0F;
            this.fallingVelocity = 0F;
            return;
        }
        if (entity.onGround) {
            this.fallingVelocity = 0F;
            this.lastBounceVelocity = 0F;
        } else {
            this.airborneMotionX = entity.motionX;
            this.airborneMotionZ = entity.motionZ;
            if (entity.motionY < this.fallingVelocity) {
                this.fallingVelocity = (float) entity.motionY;
            }
        }
    }

    @Override
    public boolean fall(EntityLivingBase entity, float distance, float multiplier, boolean cancel) {
        if (!this.canBounce(entity, distance, cancel)) {
            if (this.lastBounceVelocity > 0F && this.lastBounceVelocity < this.CONFIG.MINIMUM_REPEAT_BOUNCE_VELOCITY) {
                this.lastBounceVelocity = 0F;
            }
            return cancel;
        }
        final float bounceVelocity = this.getBounceVelocity(distance);
        SoundHelper.playSound(entity, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.pendingBounceVelocity = bounceVelocity;
        return cancel;
    }

    @Override
    public float fallDistance(EntityLivingBase entity, float distance) {
        this.pendingFallDistance = distance;
        return distance;
    }

    @Override
    public float fallDamageMultiplier(EntityLivingBase entity, float multiplier) {
        if (!this.canBounce(entity, this.pendingFallDistance, false)) {
            return multiplier;
        }
        return multiplier * this.CONFIG.FALL_DAMAGE_MULTIPLIER;
    }

    protected boolean canBounce(EntityLivingBase entity, float distance, boolean cancel) {
        if (cancel || entity.isSneaking() || distance <= this.CONFIG.MINIMUM_FALL_DISTANCE) {
            return false;
        }
        return (this.lastBounceVelocity <= 0F || this.lastBounceVelocity >= this.CONFIG.MINIMUM_REPEAT_BOUNCE_VELOCITY)
                && this.getBounceVelocity(distance) > 0F;
    }

    protected float getBounceVelocity(float distance) {
        float impactVelocity = -this.fallingVelocity;
        if (impactVelocity <= 0F) {
            impactVelocity = distance * this.CONFIG.FALL_DISTANCE_MULTIPLIER;
        }
        final float baseBounceVelocity = Math.min(impactVelocity * this.CONFIG.BOUNCE_MULTIPLIER, this.CONFIG.MAX_BOUNCE_VELOCITY);
        return baseBounceVelocity * (float) Math.sqrt(1F - this.CONFIG.FALL_DAMAGE_MULTIPLIER);
    }
}
