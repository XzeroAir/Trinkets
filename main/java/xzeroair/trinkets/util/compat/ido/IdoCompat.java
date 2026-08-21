package xzeroair.trinkets.util.compat.ido;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public final class IdoCompat {

    private IdoCompat() {
    }

    /**
     * Ido owns temporary player dimensions while its sprint-swimming or crawling
     * behavior is active. This deliberately uses only vanilla player state, so
     * Trinkets has no class-linkage dependency on Ido.
     */
    public static boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.Ido
                && TrinketsConfig.getClientStore().MOD_COMPAT_IDO_SWIMMING;
    }

    public static boolean isSwimming(EntityLivingBase entity) {
        return entity != null
                && isModEnabled()
                && !entity.isRiding()
                && entity.isInWater()
                && entity.isSprinting();
    }
}
