package xzeroair.trinkets.traits.abilities.compat.survival;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.config.abilities.external.survival.ConfigAbilitySurvivalParasites;

public class AbilityParasitesImmunity extends AbilitySurvivalMod implements ITickableAbility, IPotionAbility {

    private final ConfigAbilitySurvivalParasites CONFIG;

    public AbilityParasitesImmunity() {
        this(true);
    }

    public AbilityParasitesImmunity(boolean enabled) {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.IMMUNITY_PARASITES, enabled);
    }

    public AbilityParasitesImmunity(ConfigAbilitySurvivalParasites config, boolean enabled) {
        super(TrinketsRegistryNames.ModAbilities.SURVIVAL_PARASITES_IMMUNITY);
        this.CONFIG = config;
        this.setAbilityEnabled(enabled);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        SurvivalCompat.clearParasites(entity);
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final Potion parasites = SurvivalCompat.getSDParasitesPotionEffect();
        if ((parasites != null) && effect.getPotion().equals(parasites)) return true;
        return cancel;
    }

}