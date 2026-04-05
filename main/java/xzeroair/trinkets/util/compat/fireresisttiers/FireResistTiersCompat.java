package xzeroair.trinkets.util.compat.fireresisttiers;

import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public class FireResistTiersCompat {

    public static boolean isModActive() {
        return Trinkets.MOD_COMPAT.FireResistanceTiers && TrinketsConfig.compat.FIRE_RESISTANCE_TIERS;
    }

}
