package xzeroair.trinkets.traits.abilities.compat.survival;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.config.abilities.external.survival.ConfigAbilitySurvivalThirst;

import java.util.List;

public class AbilityThirstImmunity extends AbilitySurvivalMod implements ITickableAbility, IPotionAbility {

    private final ConfigAbilitySurvivalThirst CONFIG;

    public AbilityThirstImmunity() {
        this(true);
    }

    public AbilityThirstImmunity(boolean enabled) {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.IMMUNITY_THIRST, enabled);
    }

    public AbilityThirstImmunity(ConfigAbilitySurvivalThirst config, boolean enabled) {
        super(TrinketsRegistryNames.ModAbilities.SURVIVAL_THIRST_IMMUNITY);
        this.CONFIG = config;
        this.setAbilityEnabled(enabled);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        SurvivalCompat.clearThirst(entity);
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final List<Potion> thirsts = SurvivalCompat.getThirstEffects();
        if (thirsts.contains(effect.getPotion())) return true;
        return cancel;
    }

}