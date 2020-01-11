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
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.IContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketInventoryContainer extends Container {

	public final InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
	public final InventoryCraftResult craftResult = new InventoryCraftResult();
	public IContainerHandler trinket;

	private final EntityPlayer player;
	private static final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

	public TrinketInventoryContainer(InventoryPlayer playerInv, boolean client, EntityPlayer player) {
		this.player = player;

		trinket = player.getCapability(TrinketContainerProvider.containerCap, null);

		addSlotToContainer(new SlotCrafting(playerInv.player, craftMatrix, craftResult, 0, 154, 28));

		//Crafting Area
		for (int i = 0; i < 2; ++i)
		{
			for (int j = 0; j < 2; ++j)
			{
				addSlotToContainer(new Slot(craftMatrix, j + (i * 2), 98 + (j * 18), 18 + (i * 18)));
			}
		}
		// End Crafting

		//Equipment Slots
		for (int k = 0; k < 4; k++)
		{
			final EntityEquipmentSlot slot = equipmentSlots[k];
			addSlotToContainer(new Slot(playerInv, 36 + (3 - k), 8, 8 + (k * 18))
			{
				@Override
				public int getSlotStackLimit()
				{
					return 1;
				}
				@Override
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem().isValidArmor(stack, slot, player);
				}
				@Override
				public boolean canTakeStack(EntityPlayer playerIn)
				{
					final ItemStack itemstack = getStack();
					return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
				}
				@Override
				public String getSlotTexture()
				{
					return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
				}
			});
		}
		//End Equipment Slots

		//PlayerInv
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				addSlotToContainer(new Slot(playerInv, j + ((i + 1) * 9), 8 + (j * 18), 84 + (i * 18)));
			}
		}
		//End PlayerInv

		//PlayerHotBar
		for (int i = 0; i < 9; ++i)
		{
			addSlotToContainer(new Slot(playerInv, i, 8 + (i * 18), 142));
		}
		//End PlayerHotBar

		//Player OffHand
		addSlotToContainer(new Slot(playerInv, 40, 77, 62)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return super.isItemValid(stack);
			}
			@Override
			public String getSlotTexture()
			{
				return "minecraft:items/empty_armor_slot_shield";
			}
		});
		//End PlayerOffHand

		// After Main Inventory Created, Generate Trinket Inventory
		final int slotRowAmount = TrinketsConfig.SERVER.GUI.guiSlotsRows;
		final int slotRowLength = TrinketsConfig.SERVER.GUI.guiSlotsRowLength;

		final int X = TrinketsConfig.CLIENT.GUI.X+1;
		final int Y = TrinketsConfig.CLIENT.GUI.Y+1;

		final int xPos = (X+6) + ((slotRowAmount-1)*18);//X + 6;

		final int SX = 18;
		final int SY = 18;
		final int SZ = 8;

		for(int r = 0; r < 1;r++) {
			for(int i = 0; i < 8;i++) {
				final int x = X - (r * SX);
				int y = Y + (i * SY);
				final int index = (i);
				if(i > 3) {
					if(i > 6) {
						y = y + 8;
					} else {
						y = y + 4;
					}
				}
				addSlotToContainer(new TrinketSlot(player,trinket,index, x, y));
			}
		}
		if(slotRowAmount > 1) {
			for(int r = 1; r < slotRowAmount;r++) {
				for(int i = 0; i < slotRowLength;i++) {
					final int x = X - (r * SX);
					int y = Y + (i * SY);
					final int index = ((r*(slotRowLength+1))+i);
					if(i > 3) {
						if(i > 6) {
							y = y + 8;
						} else {
							y = y + 4;
						}
					}
					addSlotToContainer(new TrinketSlot(player,trinket,index, x, y));
				}
			}
		}
		onCraftMatrixChanged(craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		slotChangedCraftingGrid(player.getEntityWorld(), player, craftMatrix, craftResult);
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		craftResult.clear();

		if (!player.world.isRemote)
		{
			clearContainer(player, player.world, craftMatrix);
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);

		if ((slot != null) && slot.getHasStack())
		{
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			final EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);

			final int slotShift = TrinketsConfig.SERVER.GUI.guiSlotsRows*TrinketsConfig.SERVER.GUI.guiSlotsRowLength;//this.trinket.getSlots();

			if (index == 0)
			{
				if (!mergeItemStack(itemstack1, 9, 45, true))
				{
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if ((index >= 1) && (index < 5))
			{
				if (!mergeItemStack(itemstack1, 9, 45, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if ((index >= 5) && (index < 9))
			{
				if (!mergeItemStack(itemstack1, 9, 45, false))
				{
					return ItemStack.EMPTY;
				}
			}

			// Trinkets -> inv
			else if ((index >= 46) && (index < (46+slotShift)))
			{
				if (!mergeItemStack(itemstack1, 9, 45, false))
				{
					return ItemStack.EMPTY;
				}
			}
			// inv -> Trinkets
			//			else if ((index >= 9) && (index < 45))
			else if (itemstack.hasCapability(TrinketProvider.itemCapability, null))
			{
				final IAccessoryInterface trinket = itemstack.getCapability(TrinketProvider.itemCapability, null);
				if (trinket.playerCanEquip(itemstack1, player) && !mergeItemStack(itemstack1, 46, 46+slotShift, false))
				{
					return ItemStack.EMPTY;
				}
			}

			// inv -> armor
			else if ((entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) && !inventorySlots.get(8 - entityequipmentslot.getIndex()).getHasStack())
			{
				final int i = 8 - entityequipmentslot.getIndex();

				if (!mergeItemStack(itemstack1, i, i + 1, false))
				{
					return ItemStack.EMPTY;
				}
			}

			// inv -> offhand
			else if ((entityequipmentslot == EntityEquipmentSlot.OFFHAND) && !inventorySlots.get(45).getHasStack())
			{
				if (!mergeItemStack(itemstack1, 45, 46, false))
				{
					return ItemStack.EMPTY;
				}
			}

			// inv -> hotbar
			else if ((index >= (9)) && (index < (36)))
			{
				if (!mergeItemStack(itemstack1, 36, 45, false))
				{
					return ItemStack.EMPTY;
				}
			}
			// hotbar -> inv
			else if ((index >= (36)) && (index < (45)))
			{
				if (!mergeItemStack(itemstack1, 9, 36, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(itemstack1, 9, 45, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty() && !trinket.isEventBlocked() && (slot instanceof TrinketSlot) &&
					(itemstack.getItem() instanceof IAccessoryInterface)) {
				//				final IAccessoryInterface accessory = (IAccessoryInterface) itemstack.getItem();
				//				accessory.playerUnequipped(itemstack, playerIn);
				if(itemstack.hasCapability(TrinketProvider.itemCapability, null)) {
					((IAccessoryInterface)itemstack.getItem()).playerUnequipped(itemstack, playerIn);
					//					itemstack.getCapability(TrinketProvider.itemCapability, null).playerUnequipped(itemstack, playerIn);
				}
			}

			if(Loader.isModLoaded("baubles")) {
				final IBaublesItemHandler baubles = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
				if (itemstack1.isEmpty() && !baubles.isEventBlocked() && (slot instanceof TrinketSlot) &&
						itemstack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
					itemstack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(itemstack, playerIn);
				}
			}

			final ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

			if (index == 0)
			{
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slot)
	{
		return (slot.inventory != craftResult) && super.canMergeSlot(stack, slot);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}