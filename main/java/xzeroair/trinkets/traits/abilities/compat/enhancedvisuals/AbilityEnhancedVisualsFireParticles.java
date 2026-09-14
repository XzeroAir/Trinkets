package xzeroair.trinkets.traits.abilities.compat.enhancedvisuals;

import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsFireParticles;

public class AbilityEnhancedVisualsFireParticles extends AbilityEnhancedVisualsMod {

    public AbilityEnhancedVisualsFireParticles(ConfigAbilityEnhancedVisualsFireParticles config) {
        super(TrinketsRegistryNames.ModAbilities.ENHANCED_VISUALS_GRITLESS);
        this.setAbilityEnabled(config.ENABLED);
    }
}
