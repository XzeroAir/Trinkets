package xzeroair.trinkets.traits.abilities.compat.survival;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.config.abilities.external.survival.ConfigAbilitySurvivalHeat;

import java.util.List;

public class AbilityHeatImmunity extends AbilitySurvivalMod implements ITickableAbility, IPotionAbility {

    private final ConfigAbilitySurvivalHeat CONFIG;

    public AbilityHeatImmunity() {
        this(true);
    }

    public AbilityHeatImmunity(boolean enabled) {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.IMMUNITY_HEAT, enabled);
    }

    public AbilityHeatImmunity(ConfigAbilitySurvivalHeat config, boolean enabled) {
        super(TrinketsRegistryNames.ModAbilities.SURVIVAL_HEAT_IMMUNITY);
        this.CONFIG = config;
        this.setAbilityEnabled(enabled);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        SurvivalCompat.immuneToHeat(entity);
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final List<Potion> hyperthermia = SurvivalCompat.getHyperthermiaEffects();
        if (hyperthermia.contains(effect.getPotion())) return true;
        return cancel;
    }

}