package xzeroair.trinkets.util.interfaces;

import net.minecraft.item.ItemStack;
import xzeroair.trinkets.util.config.ConfigDefaultReusedConstants;

public interface IEffectsProvider {

    default String[] getEffectsToAdd() {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }

    default String[] getEffectsToAdd(ItemStack stack) {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }
}
