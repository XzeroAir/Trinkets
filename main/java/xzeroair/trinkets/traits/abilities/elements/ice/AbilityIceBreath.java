package xzeroair.trinkets.traits.abilities.elements.ice;

import xzeroair.trinkets.traits.abilities.elements.AbilityBreathBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBreath;

public class AbilityIceBreath extends AbilityBreathBase {

    public AbilityIceBreath() {
        this(TrinketsConfig.SERVER.ABILITIES.ICE_BREATH);
    }

    public AbilityIceBreath(ConfigAbilityBreath config) {
        super(TrinketsRegistryNames.ModAbilities.BREATH_ICE, config);
    }
}
