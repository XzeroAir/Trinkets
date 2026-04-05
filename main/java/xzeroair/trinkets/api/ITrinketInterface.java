package xzeroair.trinkets.api;

import net.minecraft.item.ItemStack;

public interface ITrinketInterface {

    default String getAccessoryType() {
        return "trinket";
    }

    int getSlot(ItemStack stack);
 
    String getItemHandler(ItemStack stack);

}
