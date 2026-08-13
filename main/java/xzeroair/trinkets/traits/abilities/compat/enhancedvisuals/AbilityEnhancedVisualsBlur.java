package xzeroair.trinkets.traits.abilities.compat.enhancedvisuals;

import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsBlur;

public class AbilityEnhancedVisualsBlur extends AbilityEnhancedVisualsMod {

    protected ConfigAbilityEnhancedVisualsBlur CONFIG;

    public AbilityEnhancedVisualsBlur() {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.CLEAR_VISION);
    }

    public AbilityEnhancedVisualsBlur(ConfigAbilityEnhancedVisualsBlur config) {
        super(TrinketsRegistryNames.ModAbilities.ENHANCED_VISUALS_BLUR);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }
}
