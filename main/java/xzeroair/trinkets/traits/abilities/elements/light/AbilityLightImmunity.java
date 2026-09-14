package xzeroair.trinkets.traits.abilities.elements.light;

import xzeroair.trinkets.traits.abilities.elements.AbilityElementalImmunity;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityLight;

public class AbilityLightImmunity extends AbilityElementalImmunity {

    public AbilityLightImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.LIGHT_IMMUNITY);
    }

    public AbilityLightImmunity(ConfigAbilityImmunityLight config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_LIGHT, "isLight", config.ENABLED);
    }
}
