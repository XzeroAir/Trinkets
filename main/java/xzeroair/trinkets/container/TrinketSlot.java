package xzeroair.trinkets.container;

import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.SlotItemHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.IContainerHandler;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketSlot extends SlotItemHandler {

	int slot;
	EntityPlayer player;

	public TrinketSlot(EntityPlayer player, IContainerHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		slot = index;
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return ((IContainerHandler)getItemHandler()).isItemValidForSlot(slot, stack, player);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		final ItemStack stack = getStack();
		if(stack.isEmpty()) {
			return false;
		}
		if(getStack().hasCapability(TrinketProvider.itemCapability, null)) {
			return getStack().getCapability(TrinketProvider.itemCapability, null).playerCanUnequip(getStack(), player);
		}
		return true;
	}

	@Override
	public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
		if (!getHasStack() && !((IContainerHandler)getItemHandler()).isEventBlocked()) {// &&
			if(stack.hasCapability(TrinketProvider.itemCapability, null)) {
				((IAccessoryInterface)stack.getItem()).playerUnequipped(stack, player);
				stack.getCapability(TrinketProvider.itemCapability, null).setWornSlot(-1);
				//				stack.getCapability(TrinketProvider.itemCapability, null).playerEquipped(stack, playerIn);;
			} else {
				if(Loader.isModLoaded("baubles")) {
					if(stack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
						stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(stack, playerIn);
					}
				}
			}
		}
		super.onTake(playerIn, stack);
		return stack;
	}

	@Override
	public void putStack(ItemStack stack) {
		if (getHasStack() && !ItemStack.areItemStacksEqual(stack,getStack())){
			if(!((IContainerHandler)getItemHandler()).isEventBlocked()) {
				if(getStack().hasCapability(TrinketProvider.itemCapability, null)) {
					((IAccessoryInterface)getStack().getItem()).playerUnequipped(getStack(), player);
					stack.getCapability(TrinketProvider.itemCapability, null).setWornSlot(-1);
					//					getStack().getCapability(TrinketProvider.itemCapability, null).playerUnequipped(getStack(), player);
				} else {
					if(Loader.isModLoaded("baubles")) {
						if(getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
							getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(getStack(), player);
						}
					}
				}
			}
		}
		final ItemStack oldstack = getStack().copy();
		super.putStack(stack);

		if (getHasStack() && !ItemStack.areItemStacksEqual(oldstack,getStack())) {
			if (!((IContainerHandler)getItemHandler()).isEventBlocked()) {
				if(getStack().hasCapability(TrinketProvider.itemCapability, null)) {
					((IAccessoryInterface)getStack().getItem()).playerEquipped(getStack(), player);
					//					getStack().getCapability(TrinketProvider.itemCapability, null).playerEquipped(getStack(), player);
				} else {
					if(Loader.isModLoaded("baubles")) {
						if(getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
							getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onEquipped(getStack(), player);
						}
					}
				}
			}
		}
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

}
