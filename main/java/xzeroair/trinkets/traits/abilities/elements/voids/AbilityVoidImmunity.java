package xzeroair.trinkets.traits.abilities.elements.voids;

import xzeroair.trinkets.traits.abilities.elements.AbilityElementalImmunity;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityVoid;

public class AbilityVoidImmunity extends AbilityElementalImmunity {

    public AbilityVoidImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.VOID_IMMUNITY);
    }

    public AbilityVoidImmunity(ConfigAbilityImmunityVoid config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_VOID, "isVoid", config.ENABLED);
    }
}
