package xzeroair.trinkets.traits.abilities.compat.survival;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.config.abilities.external.survival.ConfigAbilitySurvivalCold;

import java.util.List;

public class AbilityColdImmunity extends AbilitySurvivalMod implements ITickableAbility, IPotionAbility {

    private final ConfigAbilitySurvivalCold CONFIG;

    public AbilityColdImmunity() {
        this(true);
    }

    public AbilityColdImmunity(boolean enabled) {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.IMMUNITY_COLD, enabled);
    }

    public AbilityColdImmunity(ConfigAbilitySurvivalCold config, boolean enabled) {
        super(TrinketsRegistryNames.ModAbilities.SURVIVAL_COLD_IMMUNITY);
        this.CONFIG = config;
        this.setAbilityEnabled(enabled);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        SurvivalCompat.immuneToCold(entity);
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final List<Potion> hypothermia = SurvivalCompat.getHypothermiaEffects();
        if (hypothermia.contains(effect.getPotion())) return true;
        return cancel;
    }

}
