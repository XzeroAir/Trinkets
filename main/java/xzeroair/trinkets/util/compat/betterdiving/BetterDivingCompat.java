package xzeroair.trinkets.util.compat.betterdiving;

import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public final class BetterDivingCompat {

    private BetterDivingCompat() {
    }

    public static boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.BetterDiving
                && (Trinkets.proxy.getSide().isClient() ? TrinketsConfig.getClientStore().MOD_COMPAT_BETTER_DIVING : TrinketsConfig.compat.BETTER_DIVING);
    }

    public static void setOxygen(EntityPlayer player, int value) {
        //		if (TrinketsConfig.compat.betterdiving && Loader.isModLoaded("better_diving")) {
        //			final ICapabilityDivingAttributes oxygen = player.getCapability(CapabilityDivingAttributesProvider.DIVING_ATTRIBUTES, null);
        //			if (value > 100) {
        //				value = oxygen.getOxygenCapacityFromPlayer();
        //			}
        //			final int o = MathHelper.clamp(value, 0, oxygen.getOxygenCapacityFromPlayer());
        //			oxygen.setOxygen(o);
        //		}
    }
}
