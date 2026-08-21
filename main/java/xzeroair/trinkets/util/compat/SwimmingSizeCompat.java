package xzeroair.trinkets.util.compat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.util.compat.ido.IdoCompat;
import xzeroair.trinkets.util.compat.tropicraft.TropicraftCompat;

/**
 * Resolves temporary player-size ownership without linking Trinkets to an
 * optional mod's classes. Ido and Tropicraft take ownership of their own
 * temporary swimming dimensions while their compatibility toggles are on.
 */
public final class SwimmingSizeCompat {

    private SwimmingSizeCompat() {
    }

    public static boolean isSwimming(EntityLivingBase entity) {
        return IdoCompat.isSwimming(entity)
                || TropicraftCompat.isSwimming(entity);
    }
}
