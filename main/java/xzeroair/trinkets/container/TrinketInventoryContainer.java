package xzeroair.trinkets.container;

import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketInventoryContainer extends Container {

	public final InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
	public final InventoryCraftResult craftResult = new InventoryCraftResult();
	public ITrinketContainerHandler trinket;

	private final EntityPlayer player;
	private static final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };

	public TrinketInventoryContainer(InventoryPlayer playerInv, boolean client, EntityPlayer player) {
		this.player = player;

		trinket = player.getCapability(TrinketContainerProvider.containerCap, null);

		this.addSlotToContainer(new SlotCrafting(playerInv.player, craftMatrix, craftResult, 0, 154, 28));

		//Crafting Area
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 2; ++j) {
				this.addSlotToContainer(new Slot(craftMatrix, j + (i * 2), 98 + (j * 18), 18 + (i * 18)));
			}
		}
		// End Crafting

		//Equipment Slots
		for (int k = 0; k < 4; k++) {
			final EntityEquipmentSlot slot = equipmentSlots[k];
			this.addSlotToContainer(new Slot(playerInv, 36 + (3 - k), 8, 8 + (k * 18)) {
				@Override
				public int getSlotStackLimit() {
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack stack) {
					return stack.getItem().isValidArmor(stack, slot, player);
				}

				@Override
				public boolean canTakeStack(EntityPlayer playerIn) {
					final ItemStack itemstack = this.getStack();
					return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
				}

				@Override
				public String getSlotTexture() {
					return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
				}
			});
		}
		//End Equipment Slots

		//PlayerInv
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInv, j + ((i + 1) * 9), 8 + (j * 18), 84 + (i * 18)));
			}
		}
		//End PlayerInv

		//PlayerHotBar
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(playerInv, i, 8 + (i * 18), 142));
		}
		//End PlayerHotBar

		//Player OffHand
		this.addSlotToContainer(new Slot(playerInv, 40, 77, 62) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return super.isItemValid(stack);
			}

			@Override
			public String getSlotTexture() {
				return "minecraft:items/empty_armor_slot_shield";
			}
		});
		//End PlayerOffHand

		// After Main Inventory Created, Generate Trinket Inventory
		final int x = TrinketsConfig.CLIENT.GUI.X + 1;
		final int y = TrinketsConfig.CLIENT.GUI.Y + 1;

		final int slots = TrinketsConfig.SERVER.GUI.guiSlotsRows;
		int X = x;
		int Y = y;
		int c = 0;
		int l = 0;
		for (int i = 0; i < (slots); i++) {
			if ((l == 4) || (l == 7)) {
				Y += 4;
			}
			this.addSlotToContainer(new TrinketSlot(player, trinket, i, X - (c * 18), Y + (l * 18)));
			l++;
			if ((l % 8) == 0) {
				l = 0;
				c += 1;
				Y = y;
			}
		}
		this.onCraftMatrixChanged(craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		this.slotChangedCraftingGrid(player.getEntityWorld(), player, craftMatrix, craftResult);
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		craftResult.clear();

		if (!player.world.isRemote) {
			this.clearContainer(player, player.world, craftMatrix);
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		final Slot slot = inventorySlots.get(index);
		ItemStack itemstack = ItemStack.EMPTY;

		if ((slot != null) && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			final EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);

			final int slotShift = trinket.getSlots();
			//TrinketsConfig.SERVER.GUI.guiSlotsRows;

			if (index == 0) {
				if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			} else if ((index >= 1) && (index < 5)) {
				if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
					return ItemStack.EMPTY;
				}
			} else if ((index >= 5) && (index < 9)) {
				if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
					return ItemStack.EMPTY;
				}
			}

			// Trinkets -> inv
			else if ((index >= 46) && (index < (46 + slotShift))) {
				if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
					return ItemStack.EMPTY;
				}
			}

			// inv -> armor
			else if ((entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) && !inventorySlots.get(8 - entityequipmentslot.getIndex()).getHasStack()) {
				final int i = 8 - entityequipmentslot.getIndex();

				if (!this.mergeItemStack(itemstack1, i, i + 1, false)) {
					return ItemStack.EMPTY;
				}
			}

			// inv -> offhand
			else if ((entityequipmentslot == EntityEquipmentSlot.OFFHAND) && !inventorySlots.get(45).getHasStack()) {
				if (!this.mergeItemStack(itemstack1, 45, 46, false)) {
					return ItemStack.EMPTY;
				}
			}

			// inv -> Trinkets
			//			else if ((index >= 9) && (index < 45))
			else if (itemstack.hasCapability(Capabilities.ITEM_TRINKET, null) && (itemstack.getItem() instanceof IAccessoryInterface)) {
				final boolean canEquip = ((IAccessoryInterface) itemstack1.getItem()).canEquipAccessory(itemstack1, player);
				final boolean placeItem = canEquip && !this.mergeItemStack(itemstack1, 46, 46 + slotShift, false);
				if (placeItem) {
					return ItemStack.EMPTY;
				}
			}

			// inv -> hotbar
			else if ((index >= (9)) && (index < (36))) {
				if (!this.mergeItemStack(itemstack1, 36, 45, false)) {
					return ItemStack.EMPTY;
				}
			}
			// hotbar -> inv
			else if ((index >= (36)) && (index < (45))) {
				if (!this.mergeItemStack(itemstack1, 9, 36, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty() && !trinket.isEventBlocked() && (slot instanceof TrinketSlot) &&
					(itemstack.getItem() instanceof IAccessoryInterface)) {
				((IAccessoryInterface) itemstack.getItem()).onAccessoryUnequipped(itemstack, playerIn);
			}

			if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
				final IBaublesItemHandler baubles = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
				if (itemstack1.isEmpty() && !baubles.isEventBlocked() && (slot instanceof TrinketSlot) &&
						itemstack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
					itemstack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(itemstack, playerIn);
				}
			}

			final ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slot) {
		return (slot.inventory != craftResult) && super.canMergeSlot(stack, slot);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}