package xzeroair.trinkets.traits.abilities;

import xzeroair.trinkets.traits.abilities.elements.AbilityBreathBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBreath;

public class AbilityDragonBreath extends AbilityBreathBase {

    public AbilityDragonBreath() {
        this(TrinketsConfig.SERVER.ABILITIES.DRAGON_BREATH);
    }

    public AbilityDragonBreath(ConfigAbilityBreath config) {
        super(TrinketsRegistryNames.ModAbilities.BREATH_DRAGON, config);
    }

}