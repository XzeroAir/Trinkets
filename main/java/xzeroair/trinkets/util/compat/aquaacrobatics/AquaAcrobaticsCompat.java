package xzeroair.trinkets.util.compat.aquaacrobatics;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

/**
 * Avoids direct Aqua Acrobatics API linkage while it owns modern swimming.
 */
public final class AquaAcrobaticsCompat {

    private AquaAcrobaticsCompat() {
    }

    public static boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.AquaAcrobatics
                && (Trinkets.proxy.getSide().isClient() ? TrinketsConfig.getClientStore().MOD_COMPAT_AQUA_ACROBATICS : TrinketsConfig.compat.AQUA_ACROBATICS);
    }

    public static boolean isSwimming(EntityLivingBase entity) {
        return entity != null
                && isModEnabled()
                && !entity.isRiding()
                && entity.isInWater()
                && entity.isSprinting();
    }
}
