package xzeroair.trinkets.util.compat.betterdiving;

import meldexun.better_diving.capability.diving.CapabilityDivingAttributesProvider;
import meldexun.better_diving.capability.diving.ICapabilityDivingAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.util.TrinketsConfig;

public class BetterDivingCompat {

	public static void setOxygen(EntityPlayer player, int value) {
		if(TrinketsConfig.compat.betterdiving && Loader.isModLoaded("better_diving")) {
			final ICapabilityDivingAttributes oxygen = player.getCapability(CapabilityDivingAttributesProvider.DIVING_ATTRIBUTES, null);
			if(oxygen != null) {
				if(value > 100) {
					value = oxygen.getOxygenCapacityFromPlayer();
				}
				final int o = MathHelper.clamp(value, 0, oxygen.getOxygenCapacityFromPlayer());
				oxygen.setOxygen(o);
			}
		}
	}

}
