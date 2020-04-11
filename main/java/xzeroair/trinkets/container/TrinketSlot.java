package xzeroair.trinkets.container;

import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.SlotItemHandler;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketSlot extends SlotItemHandler {

	int slot;
	EntityPlayer player;

	public TrinketSlot(EntityPlayer player, ITrinketContainerHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.slot = index;
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return ((ITrinketContainerHandler) this.getItemHandler()).isItemValidForSlot(this.slot, stack, this.player);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		final ItemStack stack = this.getStack();
		if (stack.isEmpty()) {
			return false;
		}
		TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		if ((iCap != null) && (this.getStack().getItem() instanceof IAccessoryInterface)) {
			return ((IAccessoryInterface) this.getStack().getItem()).playerCanUnequip(this.getStack(), player);
		}
		return true;
	}

	@Override
	public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
		if (!this.getHasStack() && !((ITrinketContainerHandler) this.getItemHandler()).isEventBlocked()) {
			TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
			if (iCap != null) {
				((IAccessoryInterface) stack.getItem()).playerUnequipped(stack, this.player);
				iCap.setSlot(-1);
			} else {
				if (Loader.isModLoaded("baubles")) {
					if (stack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
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
		if (this.getHasStack() && !ItemStack.areItemStacksEqual(stack, this.getStack())) {
			if (!((ITrinketContainerHandler) this.getItemHandler()).isEventBlocked()) {
				TrinketProperties iCap = Capabilities.getTrinketProperties(this.getStack());
				if ((iCap != null) && (this.getStack().getItem() instanceof IAccessoryInterface)) {
					((IAccessoryInterface) this.getStack().getItem()).playerUnequipped(this.getStack(), this.player);
					iCap.setSlot(-1);
				} else {
					if (Loader.isModLoaded("baubles")) {
						if (this.getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
							this.getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(this.getStack(), this.player);
						}
					}
				}
			}
		}
		final ItemStack oldstack = this.getStack().copy();
		super.putStack(stack);

		if (this.getHasStack() && !ItemStack.areItemStacksEqual(oldstack, this.getStack())) {
			if (!((ITrinketContainerHandler) this.getItemHandler()).isEventBlocked()) {
				TrinketProperties iCap = Capabilities.getTrinketProperties(this.getStack());
				if (iCap != null) {
					((IAccessoryInterface) this.getStack().getItem()).playerEquipped(this.getStack(), this.player);
				} else {
					if (Loader.isModLoaded("baubles")) {
						if (this.getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
							this.getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onEquipped(this.getStack(), this.player);
						}
					}
				}
			}
		}
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
