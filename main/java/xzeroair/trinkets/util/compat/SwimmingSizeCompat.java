package xzeroair.trinkets.util.compat;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.util.compat.aquaacrobatics.AquaAcrobaticsCompat;
import xzeroair.trinkets.util.compat.betterdiving.BetterDivingCompat;
import xzeroair.trinkets.util.compat.ido.IdoCompat;
import xzeroair.trinkets.util.compat.tropicraft.TropicraftCompat;

/**
 * Lets enabled external swimming compatibility own temporary player dimensions
 * and swimming mechanics without directly linking their APIs.
 */
public final class SwimmingSizeCompat {

    private SwimmingSizeCompat() {
    }

    public static boolean hasExternalSwimmingMod() {
        return BetterDivingCompat.isModEnabled()
                || IdoCompat.isModEnabled()
                || TropicraftCompat.isModEnabled()
                || AquaAcrobaticsCompat.isModEnabled();
    }

    public static boolean isSwimming(EntityLivingBase entity) {
        return IdoCompat.isSwimming(entity)
                || TropicraftCompat.isSwimming(entity)
                || AquaAcrobaticsCompat.isSwimming(entity);
    }
}
