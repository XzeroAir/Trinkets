package xzeroair.trinkets.util.compat.defiledlands;

import net.minecraft.potion.Potion;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public class CompatDefiledLands {

    public static final String DEFILED_LANDS_MODID = "defiledlands";
    public static final String DEFILED_LANDS_BLEEDING = DEFILED_LANDS_MODID + ":" + "bleeding";

    public static boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.DefiledLands && TrinketsConfig.compat.DEFILED_LANDS;
    }

    public static Potion getPotionBleeding() {
        if (isModEnabled()) {
            return Potion.getPotionFromResourceLocation(DEFILED_LANDS_BLEEDING);
        }
        return null;
    }

}
