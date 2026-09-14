package xzeroair.trinkets.traits.abilities.elements.air;

import xzeroair.trinkets.traits.abilities.elements.AbilityElementalImmunity;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityAir;

public class AbilityAirImmunity extends AbilityElementalImmunity {

    public AbilityAirImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.AIR_IMMUNITY);
    }

    public AbilityAirImmunity(ConfigAbilityImmunityAir config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_AIR, "isAir", config.ENABLED);
    }
}
