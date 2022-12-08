package xzeroair.trinkets.api;

import net.minecraft.item.ItemStack;

public interface ITrinketInterface {

	default String getAccessoryType() {
		return "trinket";
	}

	public int getSlot(ItemStack stack);

	public String getItemHandler(ItemStack stack);

}
