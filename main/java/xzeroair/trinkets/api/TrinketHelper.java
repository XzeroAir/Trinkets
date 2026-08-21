package xzeroair.trinkets.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.compat.baubles.BaublesHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TrinketHelper {

    public static ItemStack getTrinketInSlot(EntityLivingBase entity, final int slot) {
        return getTrinketHandler(entity, ItemStack.EMPTY, (handler, rtn) -> handler.getStackInSlot(slot));
    }

    /**
     * @param entity
     * @return Trinket Container
     */
    public static ITrinketContainerHandler getTrinketHandler(EntityLivingBase entity) {
        return Capabilities.getTrinketContainer(entity, handler -> handler.setPlayer(entity));
    }

    /**
     * @param entity
     * @param consumer
     * @return Trinket Container
     */
    public static ITrinketContainerHandler getTrinketHandler(EntityLivingBase entity, Consumer<ITrinketContainerHandler> consumer) {
        return Capabilities.getTrinketContainer(entity, handler -> {
            handler.setPlayer(entity);
            if (consumer != null) {
                consumer.accept(handler);
            }
        });
    }

    /**
     * @param entity
     * @return function return
     */
    public static <R> R getTrinketHandler(EntityLivingBase entity, R ret, BiFunction<ITrinketContainerHandler, R, R> func) {
        return Capabilities.getTrinketContainer(entity, ret, (handler, rtn) -> {
            handler.setPlayer(entity);
            return func.apply(handler, rtn);
        });
    }

    public static ItemStack getStackFromHandler(EntityLivingBase entity, ItemHandlerType handler, int slot) {
        if (entity == null) {
            return ItemStack.EMPTY;
        }
        switch (handler) {
            case TRINKETS:
                return getTrinketInSlot(entity, slot);
            case BAUBLES:
                return BaublesHelper.getBaubleInSlot(entity, slot);
            case HEAD:
                return entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            case CHEST:
                return entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            case LEGS:
                return entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            case FEET:
                return entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            case OFFHAND:
                return entity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
            case MAINHAND:
                return entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
            case HOTBAR:
                return (entity instanceof EntityPlayer) && InventoryPlayer.isHotbar(slot) ? ((EntityPlayer) entity).inventory.getStackInSlot(slot) : ItemStack.EMPTY;
            case INVENTORY:
                return (entity instanceof EntityPlayer) ? ((EntityPlayer) entity).inventory.getStackInSlot(slot) : ItemStack.EMPTY;
            default:
                return ItemStack.EMPTY;
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static class SlotInformation {
        protected int Slot = -1;
        protected String handler;
        protected ItemStack stack;
        protected boolean hasChanged;

        public static SlotInformation EMPTY = new SlotInformation();

        public SlotInformation() {
            this(ItemStack.EMPTY, ItemHandlerType.NONE, -1);
        }

        public SlotInformation(ItemHandlerType handler) {
            this(handler.getName(), -1);
        }

        public SlotInformation(ItemHandlerType handler, int slot) {
            this(handler.getName(), slot);
        }

        public SlotInformation(String handler, int slot) {
            this(ItemStack.EMPTY, handler, slot);
        }

        public SlotInformation(ItemStack stack, ItemHandlerType handler, int slot) {
            this(stack, handler.getName(), slot);
        }

        public SlotInformation(ItemStack stack, String handler, int slot) {
            if (stack == null) {
                stack = ItemStack.EMPTY;
            }
            this.stack = stack;
            this.setChanged(false);
            this.setSlot(slot);
            this.setHandler(handler);
        }

        public void setSlot(int slot) {
            if (this.Slot != slot) {
                this.Slot = slot;
//                this.setChanged(true);
            }
        }

        public int getSlot() {
            return this.Slot;
        }

        public void setHandler(ItemHandlerType handler) {
            this.setHandler(handler.getName());
        }

        public void setHandler(String handler) {
            if (this.handler == null || this.handler.compareToIgnoreCase(handler) != 0) {
                this.handler = handler;
//                this.setChanged(true);
            }
        }

        public boolean changed() {
            return this.hasChanged;
        }

        public void setChanged() {
            this.setChanged(true);
        }

        public void setChanged(boolean bool) {
            this.hasChanged = bool;
        }

        public String getItemID() {
            return this.stack.isEmpty() ? "EMPTY" : this.stack.getItem().getRegistryName().toString();
        }

        public String getHandler() {
            return this.handler;
        }

        public ItemHandlerType getHandlerType() {
            return ItemHandlerType.byName(this.handler);
        }

        public ItemStack getSourceStack() {
            return this.stack;
        }

        public ItemStack getStackFromHandler(EntityLivingBase entity) {
            return TrinketHelper.getStackFromHandler(entity, this.getHandlerType(), this.getSlot());
        }

        public enum ItemHandlerType {

            NONE(0, "None"), RACE(1, "Race"), TRINKETS(2, "Trinkets"), BAUBLES(3, "Baubles"), INVENTORY(4, "Inventory"), HOTBAR(5, "Hotbar"), HEAD(6, "Head"), CHEST(7, "Chest"), LEGS(8, "Legs"), FEET(9, "Feet"), OFFHAND(10, "OffHand"), MAINHAND(11, "MainHand"), POTION(12, "Potion"), OTHER(13, "Other");

            private static final ItemHandlerType[] ID = new ItemHandlerType[values().length];
            private final int id;
            private final String name;

            ItemHandlerType(int id, String name) {
                this.id = id;
                this.name = name;
            }

            public int getId() {
                return this.id;
            }

            public String getName() {
                return this.name;
            }

            public static ItemHandlerType byName(String name) {
                for (int i = 0; i < values().length; i++) {
                    if (byID(i).getName().contentEquals(name)) {
                        return byID(i);
                    }
                }
                return NONE;
            }

            public static ItemHandlerType byID(int value) {
                if ((value < 0) || (value >= values().length)) {
                    value = 0;
                }
                return values()[value];
            }
        }

        public boolean compare(ItemStack stack, int slot, ItemHandlerType handler) {
            return this.getSourceStack().isItemEqual(stack) && this.getSlot() == slot && this.getHandlerType().compareTo(handler) == 0;
        }

        public boolean compare(SlotInformation other) {
            return this.compare(other.getSourceStack(), other.getSlot(), other.getHandlerType());
        }

        @Override
        public String toString() {
            return "ID:" + this.getItemID() + ", Handler:" + this.getHandler() + ", Slot:" + this.getSlot();//super.toString();
        }
    }

    @Nullable
    public static SlotInformation getSlotInfoForItem(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final SlotInformation info = getSlotInfoForItemFromAccessory(entity, predicate);
        if (info == null) {
            final SlotInformation equipmentInfo = getSlotInfoForItemFromEquipment(entity, predicate);
            if (equipmentInfo == null) {
                final SlotInformation heldInfo = getSlotInfoForItemFromHeldEquipment(entity, predicate);
                if ((heldInfo == null) && (entity instanceof EntityPlayer)) {
                    return getSlotInfoForItemFromPlayerInventory(entity, predicate, true);
                }
                return heldInfo;
            }
            return equipmentInfo;
        }
        return info;
    }

    @Nullable
    public static SlotInformation getSlotInfoForItemFromAccessory(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final SlotInformation info = getTrinketSlotInformation(entity, predicate);
        if (info == null) {
            return BaublesHelper.getBaubleSlotInformation(entity, predicate);
        }
        return info;
    }

    @Nullable
    public static SlotInformation getSlotInfoForItemFromEquipment(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        if (entity == null) {
            return null;
        }
        final ItemStack head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (predicate.test(head)) {
            return getSlotInfo(head, ItemHandlerType.HEAD.getName(), EntityEquipmentSlot.HEAD.getSlotIndex());
        }
        final ItemStack chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (predicate.test(chest)) {
            return getSlotInfo(chest, ItemHandlerType.CHEST.getName(), EntityEquipmentSlot.CHEST.getSlotIndex());
        }
        final ItemStack legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if (predicate.test(legs)) {
            return getSlotInfo(legs, ItemHandlerType.LEGS.getName(), EntityEquipmentSlot.LEGS.getSlotIndex());
        }
        final ItemStack feet = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (predicate.test(feet)) {
            return getSlotInfo(feet, ItemHandlerType.FEET.getName(), EntityEquipmentSlot.FEET.getSlotIndex());
        }
        return null;
    }

    @Nullable
    public static SlotInformation getSlotInfoForItemFromHeldEquipment(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        if (entity == null) {
            return null;
        }
        final ItemStack mainHand = entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        if (predicate.test(mainHand)) {
            return getSlotInfo(mainHand, ItemHandlerType.MAINHAND.getName(), EntityEquipmentSlot.MAINHAND.getSlotIndex());
        }
        final ItemStack offHand = entity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        if (predicate.test(offHand)) {
            return getSlotInfo(offHand, ItemHandlerType.OFFHAND.getName(), EntityEquipmentSlot.OFFHAND.getSlotIndex());
        }
        return null;
    }

    @Nullable
    public static SlotInformation getSlotInfoForItemFromPlayerInventory(EntityLivingBase entity, Predicate<ItemStack> predicate, boolean onHotbar) {
        if (entity instanceof EntityPlayer) {
            final InventoryPlayer inventory = ((EntityPlayer) entity).inventory;
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                final ItemStack stack = inventory.getStackInSlot(i);
                final boolean isHotBar = InventoryPlayer.isHotbar(i);
                final boolean flag = onHotbar == isHotBar;
                if (predicate.test(stack) && flag) {
                    final String type = isHotBar ? ItemHandlerType.HOTBAR.getName() : ItemHandlerType.INVENTORY.getName();
                    return getSlotInfo(stack, type, i);
                }
            }
        }
        return null;
    }

    @Nonnull
    public static SlotInformation getSlotInfo(ItemStack stack, String handler, int slot) {
        return new SlotInformation(stack, handler, slot);
    }

    public static List<SlotInformation> getSlotInfoForAccessories(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final List<SlotInformation> list = new ArrayList<>();
        final List<SlotInformation> trinkets = getSlotInfoForTrinkets(entity, predicate);
        final List<SlotInformation> baubles = BaublesHelper.getSlotInfoForBaubles(entity, predicate);
        list.addAll(trinkets);
        list.addAll(baubles);
        return list;
    }

    public static List<SlotInformation> getSlotInfoForEquipment(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final List<SlotInformation> list = new ArrayList<>();
        list.addAll(getSlotInfoForArmor(entity, predicate));
        list.addAll(getSlotInfoForHeldEquipment(entity, predicate));
        return list;
    }

    public static List<SlotInformation> getSlotInfoForArmor(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final List<SlotInformation> list = new ArrayList<>();
        if (entity == null) {
            return list;
        }
        final ItemStack head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (predicate.test(head)) {
            final SlotInformation info = getSlotInfo(head, ItemHandlerType.HEAD.getName(), EntityEquipmentSlot.HEAD.getSlotIndex());
            list.add(info);
        }
        final ItemStack chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (predicate.test(chest)) {
            final SlotInformation info = getSlotInfo(chest, ItemHandlerType.CHEST.getName(), EntityEquipmentSlot.CHEST.getSlotIndex());
            list.add(info);
        }
        final ItemStack legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if (predicate.test(legs)) {
            final SlotInformation info = getSlotInfo(legs, ItemHandlerType.LEGS.getName(), EntityEquipmentSlot.LEGS.getSlotIndex());
            list.add(info);
        }
        final ItemStack feet = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (predicate.test(feet)) {
            final SlotInformation info = getSlotInfo(feet, ItemHandlerType.FEET.getName(), EntityEquipmentSlot.FEET.getSlotIndex());
            list.add(info);
        }
        return list;
    }

    public static List<SlotInformation> getSlotInfoForHeldEquipment(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final List<SlotInformation> list = new ArrayList<>();
        if (entity == null) {
            return list;
        }
        final ItemStack mainHand = entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        if (predicate.test(mainHand)) {
            final SlotInformation info = getSlotInfo(mainHand, ItemHandlerType.MAINHAND.getName(), EntityEquipmentSlot.MAINHAND.getSlotIndex());
            list.add(info);
        }
        final ItemStack offHand = entity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        if (predicate.test(offHand)) {
            final SlotInformation info = getSlotInfo(offHand, ItemHandlerType.OFFHAND.getName(), EntityEquipmentSlot.OFFHAND.getSlotIndex());
            list.add(info);
        }
        return list;
    }

    public static List<SlotInformation> getSlotInfoForItems(EntityLivingBase entity, Predicate<ItemStack> predicate, boolean onHotbar) {
        final List<SlotInformation> list = new ArrayList<>();
        if (entity instanceof EntityPlayer) {
            final InventoryPlayer inventory = ((EntityPlayer) entity).inventory;
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                final ItemStack stack = inventory.getStackInSlot(i);
                final boolean isHotBar = InventoryPlayer.isHotbar(i);
                final boolean flag = onHotbar == isHotBar;
                if (predicate.test(stack) && flag) {
                    final String type = isHotBar ? ItemHandlerType.HOTBAR.getName() : ItemHandlerType.INVENTORY.getName();
                    final SlotInformation info = getSlotInfo(stack, type, i);
                    list.add(info);
                }
            }
        }
        return list;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static ItemStack getHead(EntityLivingBase entity) {
        return getHead(entity, null);
    }

    public static ItemStack getChest(EntityLivingBase entity) {
        return getChest(entity, null);
    }

    public static ItemStack getLegs(EntityLivingBase entity) {
        return getLegs(entity, null);
    }

    public static ItemStack getFeet(EntityLivingBase entity) {
        return getFeet(entity, null);
    }

    public static ItemStack getHead(EntityLivingBase entity, @Nullable Predicate<ItemStack> predicate) {
        final ItemStack item = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (predicate != null && !item.isEmpty() && !predicate.test(item)) {
            return ItemStack.EMPTY;
        }
        return item;
    }

    public static ItemStack getChest(EntityLivingBase entity, @Nullable Predicate<ItemStack> predicate) {
        final ItemStack item = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (predicate != null && !item.isEmpty() && !predicate.test(item)) {
            return ItemStack.EMPTY;
        }
        return item;
    }

    public static ItemStack getLegs(EntityLivingBase entity, @Nullable Predicate<ItemStack> predicate) {
        final ItemStack item = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if (predicate != null && !item.isEmpty() && !predicate.test(item)) {
            return ItemStack.EMPTY;
        }
        return item;
    }

    public static ItemStack getFeet(EntityLivingBase entity, @Nullable Predicate<ItemStack> predicate) {
        final ItemStack item = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (predicate != null && !item.isEmpty() && !predicate.test(item)) {
            return ItemStack.EMPTY;
        }
        return item;
    }

    public static boolean AccessoryCheck(EntityLivingBase entity, Item item) {
        if (item == null) {
            return false;
        }
        return AccessoryCheck(entity, stack -> !stack.isEmpty() && stack.getItem().getRegistryName().toString().contentEquals(item.getRegistryName().toString()));
    }

    public static boolean AccessoryCheck(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        if (!getTrinketStack(entity, predicate).isEmpty()) {
            return true;
        } else return !BaublesHelper.getBaubleStack(entity, predicate).isEmpty();
    }

    public static boolean AccessoryCheck(EntityLivingBase entity, Item... items) {
        for (final Item item : items) {
            if (AccessoryCheck(entity, item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean AccessoryCheck(EntityLivingBase entity, List<Item> items) {
        for (final Item item : items) {
            if (AccessoryCheck(entity, item)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getAccessory(EntityLivingBase entity, Item item) {
        if (item == null) {
            return ItemStack.EMPTY;
        }
        return getAccessory(entity, stack -> !stack.isEmpty() && (stack.getItem().getRegistryName().toString().contentEquals(item.getRegistryName().toString())));
    }

    public static ItemStack getAccessory(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final ItemStack stack1 = getTrinketStack(entity, predicate);
        return stack1.isEmpty() ? BaublesHelper.getBaubleStack(entity, predicate) : stack1;
    }

    public static void applyToAccessories(EntityLivingBase entity, Consumer<ItemStack> consumer) {
        applyToTrinkets(entity, consumer);
        BaublesHelper.applyToBaubles(entity, consumer);
    }

    public static int countAccessories(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        int amount = 0;
        amount += countTrinkets(entity, predicate);
        amount += BaublesHelper.countBaubles(entity, predicate);
        return amount;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static boolean TrinketCheck(EntityLivingBase entity, Item item) {
        return !getTrinketStack(entity, item).isEmpty();
    }

    public static ItemStack getTrinketStack(EntityLivingBase entity, Item item) {
        if (item == null) {
            return ItemStack.EMPTY;
        }
        return getTrinketStack(entity, stack -> !stack.isEmpty() && (stack.getItem().getRegistryName().toString().contentEquals(item.getRegistryName().toString())));
    }

    public static ItemStack getTrinketStack(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final ITrinketContainerHandler Trinket = getTrinketHandler(entity);
        if (Trinket != null) {
            for (int i = 0; i < Trinket.getSlots(); i++) {
                if (!Trinket.getStackInSlot(i).isEmpty()) {
                    final ItemStack stack = Trinket.getStackInSlot(i);
                    if (predicate.test(stack)) {
                        return stack;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static void applyToTrinkets(EntityLivingBase entity, Consumer<ItemStack> consumer) {
        getTrinketHandler(entity, Trinket -> {
            for (int i = 0; i < Trinket.getSlots(); i++) {
                if (!Trinket.getStackInSlot(i).isEmpty()) {
                    final ItemStack stack = Trinket.getStackInSlot(i);
                    if (consumer != null) {
                        consumer.accept(stack);
                    }
                }
            }
        });
    }

    public static int countTrinkets(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        int ret = 0;
        final ITrinketContainerHandler Trinket = getTrinketHandler(entity);
        if (Trinket != null) {
            for (int i = 0; i < Trinket.getSlots(); i++) {
                if (!Trinket.getStackInSlot(i).isEmpty()) {
                    final ItemStack stack = Trinket.getStackInSlot(i);
                    if (predicate.test(stack)) {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }

    @Nullable
    public static SlotInformation getTrinketSlotInformation(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final ITrinketContainerHandler Trinket = getTrinketHandler(entity);
        if (Trinket != null) {
            for (int i = 0; i < Trinket.getSlots(); i++) {
                if (!Trinket.getStackInSlot(i).isEmpty()) {
                    final ItemStack stack = Trinket.getStackInSlot(i);
                    if (predicate.test(stack)) {
                        return getSlotInfo(stack, ItemHandlerType.TRINKETS.getName(), i);
                    }
                }
            }
        }
        return null;
    }

    public static List<SlotInformation> getSlotInfoForTrinkets(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final List<SlotInformation> list = new ArrayList<>();
        final ITrinketContainerHandler Trinket = getTrinketHandler(entity);
        if (Trinket != null) {
            for (int i = 0; i < Trinket.getSlots(); i++) {
                if (!Trinket.getStackInSlot(i).isEmpty()) {
                    final ItemStack stack = Trinket.getStackInSlot(i);
                    if (predicate.test(stack)) {
                        final SlotInformation info = getSlotInfo(stack, ItemHandlerType.TRINKETS.getName(), i);
                        list.add(info);
                    }
                }
            }
        }
        return list;
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static ItemStack getItemStackFromSlot(EntityLivingBase player, int slot, int handler) {
        if ((player instanceof EntityPlayer) && (slot >= 0)) {
            if (handler == 1) {
                final ITrinketContainerHandler Trinket = getTrinketHandler(player);
                if (!Trinket.getStackInSlot(slot).isEmpty()) {
                    return Trinket.getStackInSlot(slot);
                }
            } else if (handler == 2) {
                return BaublesHelper.getBaubleInSlot(player, slot);
            }
        }
        return ItemStack.EMPTY;
    }

    public static NBTTagCompound getTagCompoundSafe(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }
        return tagCompound;
    }

    public static boolean entityHasAbility(EntityLivingBase entity, IAbilityInterface... abilities) {
        for (IAbilityInterface ability : abilities) {
            if (entityHasAbility(entity, ability)) {
                return true;
            }
        }
        return false;
    }

    public static boolean entityHasAbility(EntityLivingBase entity, IAbilityInterface ability) {
        if (ability == null) {
            return false;
        }
        return entityHasAbility(entity, ability.getRegistryName().toString());
    }

    public static boolean entityHasAbility(EntityLivingBase entity, String... abilities) {
        for (String ability : abilities) {
            if (entityHasAbility(entity, ability)) {
                return true;
            }
        }
        return false;
    }

    public static boolean entityHasAbility(EntityLivingBase entity, String ability) {
        return Capabilities.getEntityProperties(entity, false, (prop, rtn) -> prop.getAbilityHandler().getAbility(ability) != null);
    }

    public static boolean isEntityRace(final EntityLivingBase entity, final EntityRace... races) {
        for (EntityRace race : races) {
            if (isEntityRace(entity, race)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEntityRace(final EntityLivingBase entity, final EntityRace race) {
        return Capabilities.getEntityProperties(entity, false, (prop, rtn) -> prop.getCurrentRaceCache().compareRace(race));
    }

    public static boolean isEntityBoss(Entity entity) {
        if (entity == null || (entity instanceof EntityPlayer && !(entity instanceof FakePlayer))) {
            return false;
        }
        try {
            return entity instanceof EntityLivingBase && !entity.isNonBoss() || entity instanceof EntityWither || entity instanceof EntityDragon;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
