package xzeroair.trinkets.traits.abilities.elements.earth;

import xzeroair.trinkets.traits.abilities.elements.AbilityElementalImmunity;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityEarth;

public class AbilityEarthImmunity extends AbilityElementalImmunity {

    public AbilityEarthImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.EARTH_IMMUNITY);
    }

    public AbilityEarthImmunity(ConfigAbilityImmunityEarth config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_EARTH, "isEarth", config.ENABLED);
    }
}
