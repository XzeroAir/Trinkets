package xzeroair.trinkets.util.interfaces;

import net.minecraft.item.ItemStack;
import xzeroair.trinkets.util.config.ConfigDefaultReusedConstants;

public interface IImmunityProvider {

    default String[] getDamageTypesToIgnoreConfig() {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }

    default String[] getDamageTypesToIgnoreConfig(ItemStack stack) {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }

}
