package xzeroair.trinkets.traits.abilities.elements.lightning;

import xzeroair.trinkets.traits.abilities.elements.AbilityBreathBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBreath;

public class AbilityLightningBreath extends AbilityBreathBase {

    public AbilityLightningBreath() {
        this(TrinketsConfig.SERVER.ABILITIES.LIGHTNING_BREATH);
    }

    public AbilityLightningBreath(ConfigAbilityBreath config) {
        super(TrinketsRegistryNames.ModAbilities.BREATH_LIGHTNING, config);
    }
}
