package xzeroair.trinkets.traits.abilities.compat.enhancedvisuals;

import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsBlur;

public class AbilityEnhancedVisualsSplash extends AbilityEnhancedVisualsMod {

    protected ConfigAbilityEnhancedVisualsBlur CONFIG;

    public AbilityEnhancedVisualsSplash() {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.CLEAR_VISION);
    }

    public AbilityEnhancedVisualsSplash(ConfigAbilityEnhancedVisualsBlur config) {
        super(TrinketsRegistryNames.ModAbilities.ENHANCED_VISUALS_SPLASH);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }
}
