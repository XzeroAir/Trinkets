package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityWeightless;

import javax.annotation.Nonnull;

public class AbilityWeightless extends Ability implements ITickableAbility, IPotionAbility, IJumpAbility {

    protected final ConfigAbilityWeightless CONFIG;

    public AbilityWeightless() {
        this(TrinketsConfig.SERVER.ABILITIES.WEIGHTLESS);
    }

    public AbilityWeightless(ConfigAbilityWeightless config) {
        super(TrinketsRegistryNames.ModAbilities.WEIGHTLESS);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    public void tickAbility(@Nonnull EntityLivingBase entity) {
        if (!entity.onGround) {
            entity.motionY = 0;
            if ((!(entity.isSneaking())) && entity.isSwingInProgress) {
                entity.motionY += 0.1;
            }
            if (entity.isSneaking() && entity.isSwingInProgress) {
                entity.motionY -= 0.1;
            }
        }
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        if (LycanitesCompat.isWeight(effect)) {
            return true;
        }
        return cancel;
    }

    @Override
    public boolean fall(EntityLivingBase entity, float distance, float damage, boolean cancel) {
        return true;
    }

}
