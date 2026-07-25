package xzeroair.trinkets.traits.abilities.elements.fire;

import xzeroair.trinkets.traits.abilities.elements.AbilityBreathBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBreath;

public class AbilityFireBreath extends AbilityBreathBase {

    public AbilityFireBreath() {
        this(TrinketsConfig.SERVER.ABILITIES.FIRE_BREATH);
    }

    public AbilityFireBreath(ConfigAbilityBreath config) {
        super(TrinketsRegistryNames.ModAbilities.BREATH_FIRE, config);
    }
}
