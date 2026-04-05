package xzeroair.trinkets.util.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.util.config.ConfigDefaultReusedConstants;

public interface IAttributeProvider {

    default String[] getAttributeConfig() {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }

    default String[] getAttributeConfig(ItemStack stack) {
        return ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;
    }

    default void initAttributes(String[] attributeConfig, EntityLivingBase entity) {

    }
}
