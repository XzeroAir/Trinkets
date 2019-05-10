package xzeroair.trinkets.util.interfaces;

import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IBaubleSlotInterface extends IBauble {

	/**
	 * @param itemstack
	 * @param player
	 */
	default int getEquippedSlot(ItemStack itemstack, EntityLivingBase player) {
		return -1;
	}

}
