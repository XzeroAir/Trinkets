package xzeroair.trinkets.capabilities.InventoryContainerCapability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IContainerHandler extends IItemHandlerModifiable {

	boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase player);

	boolean isEventBlocked();
	void setEventBlock(boolean blockEvents);

	void setPlayer(EntityLivingBase player);

}