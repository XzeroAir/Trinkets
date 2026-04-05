package xzeroair.trinkets.util.interfaces;

import net.minecraft.item.ItemStack;
import xzeroair.trinkets.util.config.ConfigDefaultReusedConstants;

public interface IResistanceProvider {

    default String[] getEffectsToRemove() {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }

    default String[] getEffectsToRemove(ItemStack stack) {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }
}
