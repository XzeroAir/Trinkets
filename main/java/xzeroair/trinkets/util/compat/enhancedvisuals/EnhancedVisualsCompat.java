package xzeroair.trinkets.util.compat.enhancedvisuals;

import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public class EnhancedVisualsCompat {

    private static final String MOD_LANG_NAME = "Enhanced Visuals";

    public static boolean isModActive() {
        return Trinkets.MOD_COMPAT.EnhancedVisuals && TrinketsConfig.getClientStore().MOD_COMPAT_ENHANCED_VISUALS;
    }

    public static String getModName() {
        return MOD_LANG_NAME;
    }
}
