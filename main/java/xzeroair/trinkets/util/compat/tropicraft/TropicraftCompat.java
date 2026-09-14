package xzeroair.trinkets.util.compat.tropicraft;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public final class TropicraftCompat {

    private TropicraftCompat() {
    }

    /**
     * Tropicraft owns temporary player dimensions while its swim system is in
     * water. This deliberately uses only vanilla player state, so Trinkets has
     * no class-linkage dependency on Tropicraft.
     */
    public static boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.Tropicraft
                && (Trinkets.proxy.getSide().isClient() ? TrinketsConfig.getClientStore().MOD_COMPAT_TROPICRAFT_SWIMMING : TrinketsConfig.compat.TROPICRAFT_SWIMMING);
    }

    public static boolean isSwimming(EntityLivingBase entity) {
        return entity != null
                && isModEnabled()
                && !entity.isRiding()
                && entity.isInWater();
    }
}
