<<<<<<< Updated upstream
package xzeroair.trinkets.container;

import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.SlotItemHandler;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketSlot extends SlotItemHandler {

	protected EntityPlayer player;

	public TrinketSlot(EntityPlayer player, ITrinketContainerHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return ((ITrinketContainerHandler) this.getItemHandler()).isItemValidForSlot(this.getSlotIndex(), stack, player);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		final ItemStack stack = this.getStack();
		if (stack.isEmpty()) {
			return false;
		}
		if (stack.getItem() instanceof IAccessoryInterface) {
			return ((IAccessoryInterface) stack.getItem()).canUnequipAccessory(stack, player);
		}
		return true;
	}

	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack) {
		if (!this.getHasStack() && !((ITrinketContainerHandler) this.getItemHandler()).isEventBlocked()) {
			if (stack.getItem() instanceof IAccessoryInterface) {
				((IAccessoryInterface) stack.getItem()).onAccessoryUnequipped(stack, player);
			} else {
				if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
					if (stack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
						stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(stack, player);
					}
				}
			}
		}
		return super.onTake(player, stack);
	}

	@Override
	public void putStack(ItemStack stack) {
		ItemStack currentStack = this.getStack();
		if (this.getHasStack() && !ItemStack.areItemStacksEqual(stack, currentStack)) {
			if (!((ITrinketContainerHandler) this.getItemHandler()).isEventBlocked()) {
				if (currentStack.getItem() instanceof IAccessoryInterface) {
					((IAccessoryInterface) currentStack.getItem()).onAccessoryUnequipped(currentStack, player);
				} else {
					if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
						if (this.getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
							this.getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(currentStack, player);
						}
					}
				}
			}
		}
		final ItemStack oldstack = this.getStack().copy();
		super.putStack(stack);
		currentStack = this.getStack();
		if (this.getHasStack() && !ItemStack.areItemStacksEqual(oldstack, currentStack)) {
			if (!((ITrinketContainerHandler) this.getItemHandler()).isEventBlocked()) {
				if (currentStack.getItem() instanceof IAccessoryInterface) {
					((IAccessoryInterface) currentStack.getItem()).onAccessoryEquipped(currentStack, player);
				} else {
					if (Loader.isModLoaded("baubles") && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
						if (this.getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
							this.getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onEquipped(currentStack, player);
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
=======
package xzeroair.trinkets.container;

import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.SlotItemHandler;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketSlot extends SlotItemHandler {

	protected EntityPlayer player;

	public TrinketSlot(EntityPlayer player, ITrinketContainerHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return ((ITrinketContainerHandler) this.getItemHandler()).isItemValidForSlot(this.getSlotIndex(), stack, player);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		final ItemStack stack = this.getStack();
		if (stack.isEmpty()) {
			return false;
		}
		if (stack.getItem() instanceof IAccessoryInterface) {
			return ((IAccessoryInterface) stack.getItem()).canUnequipAccessory(stack, player);
		}
		return true;
	}

	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack) {
		if (!this.getHasStack() && !((ITrinketContainerHandler) this.getItemHandler()).isEventBlocked()) {
			if (stack.getItem() instanceof IAccessoryInterface) {
				((IAccessoryInterface) stack.getItem()).onAccessoryUnequipped(stack, player);
			} else {
				if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
					if (stack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
						stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(stack, player);
					}
				}
			}
		}
		return super.onTake(player, stack);
	}

	@Override
	public void putStack(ItemStack stack) {
		ItemStack currentStack = this.getStack();
		if (this.getHasStack() && !ItemStack.areItemStacksEqual(stack, currentStack)) {
			if (!((ITrinketContainerHandler) this.getItemHandler()).isEventBlocked()) {
				if (currentStack.getItem() instanceof IAccessoryInterface) {
					((IAccessoryInterface) currentStack.getItem()).onAccessoryUnequipped(currentStack, player);
				} else {
					if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
						if (this.getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
							this.getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(currentStack, player);
						}
					}
				}
			}
		}
		final ItemStack oldstack = this.getStack().copy();
		super.putStack(stack);
		currentStack = this.getStack();
		if (this.getHasStack() && !ItemStack.areItemStacksEqual(oldstack, currentStack)) {
			if (!((ITrinketContainerHandler) this.getItemHandler()).isEventBlocked()) {
				if (currentStack.getItem() instanceof IAccessoryInterface) {
					((IAccessoryInterface) currentStack.getItem()).onAccessoryEquipped(currentStack, player);
				} else {
					if (Loader.isModLoaded("baubles") && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
						if (this.getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
							this.getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onEquipped(currentStack, player);
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
>>>>>>> Stashed changes
