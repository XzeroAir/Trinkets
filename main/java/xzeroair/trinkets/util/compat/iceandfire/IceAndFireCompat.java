package xzeroair.trinkets.util.compat.iceandfire;

import net.minecraft.potion.Potion;
import xzeroair.trinkets.Trinkets;

public class IceAndFireCompat {

    public static Potion getPotionEffectByName(String name) {
        if (Trinkets.IceAndFire) {
            try {
                return Potion.getPotionFromResourceLocation("iceandfire:" + name);
            } catch (Exception e) {
               return null;
            }
        }
        return null;
    }
}
