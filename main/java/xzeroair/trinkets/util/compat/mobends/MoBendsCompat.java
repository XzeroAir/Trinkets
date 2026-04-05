package xzeroair.trinkets.util.compat.mobends;

import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public class MoBendsCompat {

    public MoBendsCompat() {
    }

    public static final boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.MoBends && TrinketsConfig.compat.MO_BENDS;
    }
}
