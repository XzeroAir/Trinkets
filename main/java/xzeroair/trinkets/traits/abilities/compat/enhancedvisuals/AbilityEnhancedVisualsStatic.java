package xzeroair.trinkets.traits.abilities.compat.enhancedvisuals;

import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsStatic;

public class AbilityEnhancedVisualsStatic extends AbilityEnhancedVisualsMod {

    protected ConfigAbilityEnhancedVisualsStatic CONFIG;

    public AbilityEnhancedVisualsStatic() {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.ENDER_EYES);
    }

    public AbilityEnhancedVisualsStatic(ConfigAbilityEnhancedVisualsStatic config) {
        super(TrinketsRegistryNames.ModAbilities.ENHANCED_VISUALS_STATIC);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

}
