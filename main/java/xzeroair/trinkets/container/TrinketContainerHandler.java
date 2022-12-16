<<<<<<< Updated upstream
package xzeroair.trinkets.container;

import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketContainerHandler extends ItemStackHandler implements ITrinketContainerHandler {

	private static int slots = TrinketsConfig.SERVER.GUI.guiSlotsRows;
	private boolean[] changed = new boolean[slots];
	private boolean blockEvents = false;
	private EntityLivingBase player;

	public TrinketContainerHandler() {
		super(slots);
	}

	@Override
	public void setSize(int size) {
		if ((size < slots) || (size > slots)) {
			size = slots;
		}
		super.setSize(size);
		final boolean[] old = changed;
		changed = new boolean[size];
		for (int i = 0; (i < old.length) && (i < changed.length); i++) {
			changed[i] = old[i];
		}
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if ((stack == null) || (stack.isEmpty() || this.isItemValidForSlot(slot, stack, player))) {
			super.setStackInSlot(slot, stack);
		}
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!this.isItemValidForSlot(slot, stack, player)) {
			return stack;
		}
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	public void setPlayer(EntityLivingBase player) {
		this.player = player;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase player) {
		if ((stack == null) || stack.isEmpty() || !((stack.getItem() instanceof IAccessoryInterface))) {
			return false;
		}
		if ((stack.getItem() instanceof IAccessoryInterface)) {
			final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
			return trinket.canEquipAccessory(stack, player);
		} else if (TrinketsConfig.compat.baubles && Trinkets.Baubles && (stack.getItem() instanceof IBauble)) {
			final IBauble bauble = stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
			return true;//bauble.canEquip(stack, player);// && bauble.getBaubleType(stack).hasSlot(slot);
		} else {
			return false;
		}
	}

	@Override
	public boolean isEventBlocked() {
		return blockEvents;
	}

	@Override
	public void setEventBlock(boolean blockEvents) {
		this.blockEvents = blockEvents;
	}

}
=======
package xzeroair.trinkets.container;

import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketContainerHandler extends ItemStackHandler implements ITrinketContainerHandler {

	private static int slots = TrinketsConfig.SERVER.GUI.guiSlotsRows;
	private boolean[] changed = new boolean[slots];
	private boolean blockEvents = false;
	private EntityLivingBase player;

	public TrinketContainerHandler() {
		super(slots);
	}

	@Override
	public void setSize(int size) {
		if ((size < slots) || (size > slots)) {
			size = slots;
		}
		super.setSize(size);
		final boolean[] old = changed;
		changed = new boolean[size];
		for (int i = 0; (i < old.length) && (i < changed.length); i++) {
			changed[i] = old[i];
		}
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if ((stack == null) || (stack.isEmpty() || this.isItemValidForSlot(slot, stack, player))) {
			super.setStackInSlot(slot, stack);
		}
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!this.isItemValidForSlot(slot, stack, player)) {
			return stack;
		}
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	public void setPlayer(EntityLivingBase player) {
		this.player = player;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase player) {
		if ((stack == null) || stack.isEmpty() || !((stack.getItem() instanceof IAccessoryInterface))) {
			return false;
		}
		if ((stack.getItem() instanceof IAccessoryInterface)) {
			final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
			return trinket.canEquipAccessory(stack, player);
		} else if (TrinketsConfig.compat.baubles && Trinkets.Baubles && (stack.getItem() instanceof IBauble)) {
			final IBauble bauble = stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
			return true;//bauble.canEquip(stack, player);// && bauble.getBaubleType(stack).hasSlot(slot);
		} else {
			return false;
		}
	}

	@Override
	public boolean isEventBlocked() {
		return blockEvents;
	}

	@Override
	public void setEventBlock(boolean blockEvents) {
		this.blockEvents = blockEvents;
	}

}
>>>>>>> Stashed changes
