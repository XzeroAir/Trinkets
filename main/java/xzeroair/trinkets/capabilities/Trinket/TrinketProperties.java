package xzeroair.trinkets.capabilities.Trinket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.capabilities.race.ElementalAttributes;
import xzeroair.trinkets.items.base.TrinketRaceBase;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncItemDataPacket;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.traits.abilities.base.ItemAbilityProvider;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableInventoryAbility;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.traits.elements.IElementProvider;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.NBTHelper;

import java.util.*;
import java.util.Map.Entry;

public class TrinketProperties extends CapabilityBase<TrinketProperties, ItemStack> {

    protected int variant;
    protected int exp;
    protected float mana;
    protected boolean mainAbility;
    protected boolean altAbility;
    protected SlotInformation slotInfo;
    protected boolean sync;
    protected String crafter;
    protected String crafterUUID;
    protected ElementalAttributes elements;
    protected Map<Integer, IAbilityInterface> itemAbilities;
    protected Map<String, IAbilityInterface> activeAbilities;

    public TrinketProperties(ItemStack stack) {
        super(stack);
        variant = 0;
        exp = 0;
        mana = 0;
        mainAbility = false;
        altAbility = false;
        crafter = "";
        crafterUUID = "";
        elements = new ElementalAttributes();
        if (stack.getItem() instanceof IElementProvider) {
            elements.setPrimaryElement(((IElementProvider) stack.getItem()).getPrimaryElement());
        }
        slotInfo = new SlotInformation(stack, ItemHandlerType.NONE.getName(), -1);
        itemAbilities = new TreeMap<>();
        activeAbilities = new TreeMap<>();
//        initAbilities(stack);
    }

    public void initAbilitiesOnce(ItemStack stack) {
        if (itemAbilities.isEmpty()) {
            if (stack.getItem() instanceof TrinketRaceBase) {
                try {
                    TrinketRaceBase raceItem = (TrinketRaceBase) stack.getItem();
                    EntityRacePropertiesHandler s = raceItem.getRace().getRaceHandler(null, getPrimaryElement());
                    Collection<IAbilityInterface> Abilities = s.getRaceAbilities().values();
                    s.startTransformation();
                    int i = 0;
                    for (IAbilityInterface ability : Abilities) {
                        itemAbilities.put(i, ability);
                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (stack.getItem() instanceof ItemAbilityProvider) {
                    List<IAbilityInterface> abilities = new ArrayList<>();
                    ItemAbilityProvider provider = (ItemAbilityProvider) stack.getItem();
                    provider.initAbilities(stack, null, abilities);
                    int i = 0;
                    for (IAbilityInterface ability : abilities) {
                        itemAbilities.put(i, ability);
                        i++;
                    }
                }
            }
        }
    }

    public void initAbilities(ItemStack stack) {
        if (!itemAbilities.isEmpty()) {
            itemAbilities.clear();
        }
        if (!activeAbilities.isEmpty()) {
            activeAbilities.clear();
        }
//        if (stack.getItem() instanceof IElementProvider) {
//            IElementProvider elementProvider = (IElementProvider) stack.getItem();
//        }
        initAbilitiesOnce(stack);
    }

    public ElementalAttributes getElementAttributes() {
        return elements;
    }

    public Element getPrimaryElement() {
        return getElementAttributes().getPrimaryElement();
    }

    @Override
    public NBTTagCompound getTag() {
        if (object.getTagCompound() == null) {
            object.setTagCompound(this.saveToNBT(new NBTTagCompound()));
        }
        return object.getTagCompound();
    }

    public SlotInformation getSlotInfo() {
        return slotInfo;
    }

    public ActionResult<ItemStack> itemRightClicked(World world, EntityPlayer player, EnumHand hand, ActionResult<ItemStack> defaultResult) {
        final ItemStack stack = player.getHeldItem(hand);
        if (player.world.isRemote) {
            return defaultResult;
        }
        if (player.isSneaking()) {
            toggleAltAbility(!altAbility());
        } else {
            toggleMainAbility(!mainAbility());
        }
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    public EnumActionResult itemLeftClicked(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand, EnumActionResult defaultResult) {
        return defaultResult;
    }

    public EnumActionResult itemUsed(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, EnumActionResult defaultResult) {
        return defaultResult;
    }

    public void onCrafted(ItemStack stack, World world, EntityPlayer player) {
        if (player == null) {
            return;
        }
        try {
            if (player.getUniqueID() != null) {
                this.setCrafterUUID(player.getUniqueID().toString());
            }
            this.setCrafter(player.getDisplayNameString());
            //			stack.setStackDisplayName(this.getCrafter() + "'s " + stack.getDisplayName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCrafter() {
        return crafter;
    }

    public String getCrafterUUID() {
        return crafterUUID;
    }

    public void setCrafter(String crafter) {
        if (!crafter.isEmpty() && !this.crafter.contentEquals(crafter)) {
            this.crafter = crafter;
//            this.saveToNBT(this.getTag());
        }
    }

    public void setCrafterUUID(String crafterUUID) {
        if (!crafterUUID.isEmpty() && !this.crafterUUID.contentEquals(crafterUUID)) {
            this.crafterUUID = crafterUUID;
//            this.saveToNBT(this.getTag());
        }
    }

    public Map<Integer, IAbilityInterface> getAbilitiesProvided() {
        return this.itemAbilities;
    }

    /**
     * TODO Test this
     * Runs when in inventory
     *
     * @param stack
     * @param world
     * @param entity
     * @param itemSlot
     * @param isSelected
     */
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack current = player.inventory.getCurrentItem();
            boolean selectedMainHand = isSelected && compareStacks(stack, current, false);
            boolean selectedOffHand = isSelected && current.isEmpty();// && !compareStacks(stack, current, false);
            boolean isHotBar = (!selectedOffHand && !selectedMainHand) && InventoryPlayer.isHotbar(itemSlot);
            ItemHandlerType type = ItemHandlerType.INVENTORY;
            if (isHotBar) {
                type = ItemHandlerType.HOTBAR;
            }
            if (selectedMainHand) {
                type = ItemHandlerType.MAINHAND;
            } else {
                if ((itemSlot <= 3)) {
                    ItemStack s = player.inventory.getStackInSlot(itemSlot);
                    if (!compareStacks(stack, s, false)) {
                        if (compareStacks(stack, player.inventory.armorItemInSlot(itemSlot), false)) {
                            switch (itemSlot) {
                                case 0:
                                    type = ItemHandlerType.FEET;
                                    break;
                                case 1:
                                    type = ItemHandlerType.LEGS;
                                    break;
                                case 2:
                                    type = ItemHandlerType.CHEST;
                                    break;
                                case 3:
                                    type = ItemHandlerType.HEAD;
                                    break;
                            }
                        } else {
                            if (compareStacks(stack, player.getHeldItemOffhand(), false)) {
                                type = ItemHandlerType.OFFHAND;
                            }
                        }
                    }
                }
            }
            final boolean moved = slotInfo.getSlot() != itemSlot;
            final boolean typeChanged = slotInfo.getHandlerType().compareTo(type) != 0;
            if (moved || typeChanged) {
                slotInfo.setHandler(type);
                slotInfo.setSlot(itemSlot);
                slotInfo.setChanged(true);
                if (!this.activeAbilities.isEmpty()) {
                    this.activeAbilities.clear();
                }
            }

            if (!itemAbilities.isEmpty()) {
                final ItemHandlerType cacheType = slotInfo.getHandlerType();
                for (Entry<Integer, IAbilityInterface> attachedAbility : itemAbilities.entrySet()) {
                    String key = attachedAbility.getValue().getRegistryName().toString();
                    if (attachedAbility.getValue().getRequiredElement() == null || (attachedAbility.getValue().getRequiredElement() != null && getPrimaryElement().equals(attachedAbility.getValue().getRequiredElement()))) {
                        if (((cacheType == ItemHandlerType.INVENTORY) || (cacheType == ItemHandlerType.HOTBAR)) && (attachedAbility.getValue() instanceof ITickableInventoryAbility)) {
                            if (!activeAbilities.containsKey(key)) {
                                activeAbilities.put(key, attachedAbility.getValue());
                            }
                        }
                        if (((cacheType == ItemHandlerType.MAINHAND) || (cacheType == ItemHandlerType.OFFHAND)) && (attachedAbility.getValue() instanceof IHeldAbility)) {
                            if (!activeAbilities.containsKey(key)) {
                                activeAbilities.put(key, attachedAbility.getValue());
                            }
                        }
                    }
                }
                if (!activeAbilities.isEmpty()) {
                    Capabilities.getEntityProperties(entity, prop -> {
                        for (IAbilityInterface ability : activeAbilities.values()) {
                            prop.getAbilityHandler().replaceAbility(stack.getItem().getRegistryName().toString(), new SlotInformation(stack, cacheType, itemSlot), ability);
                        }
                    });
                }
            } else {
                initAbilitiesOnce(stack);
            }

            if (slotInfo.changed()) {
                slotInfo.setChanged(false);
            }
            if (!tickHandler.getCounters().isEmpty()) {
                for (Entry<String, Counter> counter : tickHandler.getCounters().entrySet()) {
                    counter.getValue().Tick();
                }
            }
        }
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        this.onEntityArmorTick(world, player, stack);
    }

    public void onEntityArmorTick(World world, EntityLivingBase entity, ItemStack stack) {

    }

    /**
     * Runs when worn in bauble/trinket slot
     *
     * @param stack
     * @param entity
     */
    public void onEntityTick(ItemStack stack, EntityLivingBase entity) {
        ItemStack logicCheck = slotInfo.getStackFromHandler(entity);
        if (this.compareStacks(stack, logicCheck, false)) {
            if (!itemAbilities.isEmpty()) {
                for (Entry<Integer, IAbilityInterface> attachedAbility : itemAbilities.entrySet()) {
                    if (attachedAbility.getValue().getRequiredElement() == null || attachedAbility.getValue().getRequiredElement() != null && getPrimaryElement().equals(attachedAbility.getValue().getRequiredElement())) {
                        String key = attachedAbility.getValue().getRegistryName().toString();
                        if (!activeAbilities.containsKey(key)) {
                            activeAbilities.put(key, attachedAbility.getValue());
                        }
                    }
                }
                if (!activeAbilities.isEmpty()) {
                    Capabilities.getEntityProperties(entity, prop -> {
                        for (IAbilityInterface ability : activeAbilities.values()) {
                            prop.getAbilityHandler().replaceAbility(stack.getItem().getRegistryName().toString(), new SlotInformation(stack, getSlotInfo().getHandler(), getSlotInfo().getSlot()), ability);
                        }
                    });
                }
            } else {
                initAbilitiesOnce(stack);
            }
        }
        if (sync) {
            this.sendInformationToTracking(entity);
            sync = false;
        }
    }

    public void onPlayerTick(ItemStack stack, EntityPlayer player) {
        this.onEntityTick(stack, player);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Keybind handler~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private boolean compareWithStack(ItemStack second, boolean ignoreDurability) {
        return this.compareStacks(object, second, ignoreDurability);
        //		if (second == null || second.isEmpty()) {
        //			return false;
        //		}
        //		Item firstItem = this.object.getItem();
        //		Item secondItem = second.getItem();
        //		String firstItemID = firstItem.getRegistryName().toString();
        //		String secondItemID = secondItem.getRegistryName().toString();
        //		if (!firstItemID.contentEquals(secondItemID)) {
        //			return false;
        //		} else {
        //			if (object.isItemEqual(second)) {
        //				return true;
        //			} else if (object.isItemEqualIgnoreDurability(second) && ignoreDurability) {
        //				return true;
        //			} else {
        //				return false;
        //			}
        //		}
    }

    private boolean compareStacks(ItemStack first, ItemStack second, boolean ignoreDurability) {
        if ((first == null) || (second == null)) {
            return false;
        }
        if ((first.isEmpty() && !second.isEmpty()) || (!first.isEmpty() && second.isEmpty())) {
            return false;
        }
        Item firstItem = first.getItem();
        Item secondItem = second.getItem();
        String firstItemID = firstItem.getRegistryName().toString();
        String secondItemID = secondItem.getRegistryName().toString();
        //		firstItem.getRegistryName().compareTo(secondItem.getRegistryName());
        if (!firstItemID.contentEquals(secondItemID)) {
            return false;
        } else {
            if (first.isItemEqual(second)) {
                return true;
            } else if (first.isItemEqualIgnoreDurability(second) && ignoreDurability) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void itemEquipped(ItemStack stack, EntityLivingBase entity) {
        final boolean isClient = entity.world.isRemote;
        if (!isClient) {
            final SlotInformation info = TrinketHelper.getSlotInfoForItemFromAccessory(entity, s -> s.isItemEqual(stack));
            if (info != null) {
                final int slot = info.getSlot();
                final int handler = info.getHandlerType().getId();
                slotInfo.setHandler(info.getHandlerType());
                slotInfo.setSlot(slot);
                slotInfo.setChanged();
                final NBTTagCompound tag = new NBTTagCompound();
                this.saveToNBT(tag);
                if (entity instanceof EntityPlayer) {
                    NetworkHandler.sendTo(new SyncItemDataPacket(entity, object, tag, slot, handler, handler == ItemHandlerType.BAUBLES.getId(), false), (EntityPlayerMP) entity);
                }
            }
        }
    }

    public void itemUnequipped(ItemStack stack, EntityLivingBase entity) {
        final boolean isClient = entity.world.isRemote;
        if (!isClient) {
            final int slot = slotInfo.getSlot();
            final int handler = slotInfo.getHandlerType().getId();
            slotInfo.setHandler(ItemHandlerType.NONE);
            slotInfo.setSlot(-1);
            slotInfo.setChanged();
            this.turnOff();
            tickHandler.clearCounters();
            final NBTTagCompound tag = new NBTTagCompound();
            this.saveToNBT(tag);
            if (entity instanceof EntityPlayer) {
                NetworkHandler.sendTo(new SyncItemDataPacket(entity, object, tag, slot, handler, handler == ItemHandlerType.BAUBLES.getId(), false), (EntityPlayerMP) entity);
            }
        }
    }

    public void turnOff() {
        this.toggleMainAbility(false);
        this.toggleAltAbility(false);
    }

    public boolean isEquipped() {
        return (slotInfo.getHandlerType().equals(ItemHandlerType.TRINKETS) || slotInfo.getHandlerType().equals(ItemHandlerType.BAUBLES)) && (slotInfo.getSlot() > -1);//ItemStack.areItemsEqual(object, this.getStackFromSlot(e, slot, handler));//TrinketHelper.AccessoryCheck(entity, stack.getItem());
    }

    //Handle Network stuff

    public void sendInformationToPlayer(EntityLivingBase e, EntityLivingBase receiver) {
        if (!e.getEntityWorld().isRemote && (receiver instanceof EntityPlayer)) {
            final NBTTagCompound tag = new NBTTagCompound();
            this.saveToNBT(tag);
            NetworkHandler.sendTo(new SyncItemDataPacket(e, object, tag, slotInfo.getSlot(), slotInfo.getHandlerType().getId()), (EntityPlayerMP) receiver);
        }
    }

    public void sendInformationToServer(EntityLivingBase e) {
        if (e.getEntityWorld().isRemote) {
            final NBTTagCompound tag = new NBTTagCompound();
            this.saveToNBT(tag);
            NetworkHandler.sendToServer(new SyncItemDataPacket(e, object, tag, slotInfo.getSlot(), slotInfo.getHandlerType().getId()));
        }
    }

    public void sendInformationToTracking(EntityLivingBase e) {
        final World world = e.getEntityWorld();
        if (!world.isRemote && (world instanceof WorldServer)) {
            final WorldServer w = (WorldServer) world;
            final NBTTagCompound tag = new NBTTagCompound();
            this.saveToNBT(tag);
            final int slot = slotInfo.getSlot();
            final int handler = slotInfo.getHandlerType().getId();
            final SyncItemDataPacket packet = new SyncItemDataPacket(e, object, tag, slot, handler, (handler == ItemHandlerType.BAUBLES.getId()), this.isEquipped());
            NetworkHandler.sendToTracking(packet, e);
        }
    }

    public void scheduleResync() {
        sync = true;
    }

    //Handle Network stuff end.

    public boolean mainAbility() {
        return mainAbility;
    }

    public void toggleMainAbility(boolean bool) {
        if (mainAbility != bool) {
            mainAbility = bool;
//            this.saveToNBT(this.getTag());
        }
    }

    public boolean altAbility() {
        return altAbility;
    }

    public void toggleAltAbility(boolean bool) {
        if (altAbility != bool) {
            altAbility = bool;
//            this.saveToNBT(this.getTag());
        }
    }

    public int StoredExp() {
        return exp;
    }

    public void setStoredExp(int integer) {
        if (exp != integer) {
            exp = integer;
//            this.saveToNBT(this.getTag());
        }
    }

    public int getSlot() {
        return slotInfo.getSlot();
    }

    public float StoredMana() {
        return mana;
    }

    public void setStoredMana(float mana) {
        if (this.mana != mana) {
            this.mana = mana;
//            this.saveToNBT(this.getTag());
        }
    }

    public final int getVariant() {
        return variant;
    }

    public final void setVariant(int variant) {
        if (this.variant != variant) {
            this.variant = variant;
//            this.saveToNBT(this.getTag());
        }
    }

    /*
     * int target = -1; int slot = -1; int count = 0; int exp = 0; boolean
     * mainAbility = false; boolean altAbility = false;
     */

    @Override
    public NBTTagCompound saveToNBT(NBTTagCompound compound) {
        compound.setInteger("variant", variant);
        compound.setInteger("slot", slotInfo.getSlot());
        compound.setString("handler", slotInfo.getHandler());
        compound.setBoolean("changed", slotInfo.changed());
        compound.setInteger("exp", exp);
        compound.setFloat("mana", mana);
        compound.setBoolean("main.ability", mainAbility);
        compound.setBoolean("alt.ability", altAbility);
        compound.setString("crafter.name", crafter);
        compound.setString("crafter.uuid", crafterUUID);
        try {
            tickHandler.saveCountersToNBT(compound);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            elements.saveToNBT(compound);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compound;
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        NBTHelper.hasInteger(compound, "variant", (value) -> {
            variant = value;
        });
        NBTHelper.hasInteger(compound, "slot", (value) -> {
            slotInfo.setSlot(value);
        });
        NBTHelper.hasString(compound, "handler", (string) -> {
            slotInfo.setHandler(string);
        });
        NBTHelper.hasBoolean(compound, "changed", (bool) -> {
            slotInfo.setChanged(bool);
        });
        NBTHelper.hasInteger(compound, "exp", (value) -> {
            exp = value;
        });
        NBTHelper.hasInteger(compound, "mana", (value) -> {
            mana = value;
        });
        NBTHelper.hasBoolean(compound, "main.ability", (bool) -> {
            mainAbility = bool;
        });
        NBTHelper.hasBoolean(compound, "alt.ability", (bool) -> {
            altAbility = bool;
        });
        NBTHelper.hasString(compound, "crafter.name", (string) -> {
            crafter = string;
        });
        NBTHelper.hasString(compound, "crafter.uuid", (string) -> {
            crafterUUID = string;
        });
        try {
            tickHandler.loadCountersFromNBT(compound);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            elements.loadFromNBT(compound);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}