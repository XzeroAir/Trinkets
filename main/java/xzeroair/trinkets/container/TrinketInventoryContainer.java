package xzeroair.trinkets.container;

import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TrinketInventoryContainer extends Container {

    public final InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public final InventoryCraftResult craftResult = new InventoryCraftResult();

    @Nullable
    public ITrinketContainerHandler trinket;
    public final int SLOTS;

    private final EntityPlayer player;
    private static final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    public TrinketInventoryContainer(InventoryPlayer playerInv, boolean client, EntityPlayer player) {
        this.SLOTS = TrinketsConfig.SERVER.GUI.SLOTS;
        this.player = player;

        this.trinket = TrinketHelper.getTrinketHandler(player);

        this.addSlotToContainer(new SlotCrafting(playerInv.player, this.craftMatrix, this.craftResult, 0, 154, 28));

        //Crafting Area
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + (i * 2), 98 + (j * 18), 18 + (i * 18)));
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
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem().isValidArmor(stack, slot, player);
                }

                @Override
                public boolean canTakeStack(@Nonnull EntityPlayer playerIn) {
                    final ItemStack itemstack = this.getStack();
                    return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(playerIn);
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
            public boolean isItemValid(@Nonnull ItemStack stack) {
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

        int X = x;
        int Y = y;
        int c = 0;
        int l = 0;
        for (int i = 0; i < (this.SLOTS); i++) {
            if ((l == 4) || (l == 7)) {
                Y += 4;
            }
            this.addSlotToContainer(new TrinketSlot(player, this.trinket, i, X - (c * 18), Y + (l * 18)));
            l++;
            if ((l % 8) == 0) {
                l = 0;
                c += 1;
                Y = y;
            }
        }
        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(@Nonnull IInventory par1IInventory) {
        this.slotChangedCraftingGrid(this.player.getEntityWorld(), this.player, this.craftMatrix, this.craftResult);
    }

    @Override
    public void onContainerClosed(@Nonnull EntityPlayer player) {
        super.onContainerClosed(player);
        this.craftResult.clear();

        if (!player.world.isRemote) {
            this.clearContainer(player, player.world, this.craftMatrix);
        }
        if (!TrinketsConfig.getClientStore().TRINKET_CONTAINER_ENABLED) {
            TrinketHelper.getTrinketHandler(player, Trinket -> {
                for (int i = 0; i < Trinket.getSlots(); i++) {
                    ItemStack s = Trinket.getStackInSlot(i);
                    if (!s.isEmpty()) {
                        ItemStack extracted = Trinket.extractItem(i, s.getCount(), false);
                        Capabilities.getTrinketProperties(extracted, prop -> prop.itemUnequipped(player));
                        player.inventory.placeItemBackInInventory(player.world, extracted);
                        Trinket.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            });
        }
    }

    @Override
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer player, int index) {
        final Slot slot = this.inventorySlots.get(index);
        ItemStack itemstack = ItemStack.EMPTY;

        if ((slot != null) && slot.getHasStack()) {
            final ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);

            final int slotShift = this.trinket.getSlots();
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
            else if ((entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) && !this.inventorySlots.get(8 - entityequipmentslot.getIndex()).getHasStack()) {
                final int i = 8 - entityequipmentslot.getIndex();

                if (!this.mergeItemStack(itemstack1, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // inv -> offhand
            else if ((entityequipmentslot == EntityEquipmentSlot.OFFHAND) && !this.inventorySlots.get(45).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // inv -> Trinkets
            //			else if ((index >= 9) && (index < 45))
            else if (itemstack.hasCapability(Capabilities.ITEM_TRINKET, null) && (itemstack.getItem() instanceof IAccessoryInterface)) {
                final boolean canEquip = ((IAccessoryInterface) itemstack1.getItem()).canEquipAccessory(itemstack1, this.player);
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

            if (itemstack1.isEmpty() && !this.trinket.isEventBlocked() && (slot instanceof TrinketSlot) && (itemstack.getItem() instanceof IAccessoryInterface)) {
                ((IAccessoryInterface) itemstack.getItem()).onAccessoryUnequipped(itemstack, player);
            }

            if (Trinkets.MOD_COMPAT.Baubles && !TrinketsConfig.SERVER.GUI.TRINKETS_CONTAINER_ALLOW_BAUBLES) {
                final IBaublesItemHandler baubles = this.player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
                if (itemstack1.isEmpty() && !baubles.isEventBlocked() && (slot instanceof TrinketSlot) && itemstack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
                    itemstack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(itemstack, player);
                }
            }

            final ItemStack itemstack2 = slot.onTake(player, itemstack1);

            if (index == 0) {
                player.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean canMergeSlot(@Nonnull ItemStack stack, Slot slot) {
        return (slot.inventory != this.craftResult) && super.canMergeSlot(stack, slot);
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return TrinketsConfig.getClientStore().TRINKET_CONTAINER_ENABLED;
    }

}
