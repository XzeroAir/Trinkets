package xzeroair.trinkets.capabilities.Trinket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilityItemStackBase;
import xzeroair.trinkets.capabilities.elements.ItemElementalAttributes;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.base.TrinketRaceBase;
import xzeroair.trinkets.items.trinkets.TrinketDamageShield;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SyncItemDataPacket;
import xzeroair.trinkets.network.UpdateDataForTrinketPacket;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IHeldAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableInventoryAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.NBTHelper;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;

public class TrinketProperties extends CapabilityItemStackBase<TrinketProperties, ItemStack> {

    private final String TAG_KEY = Capabilities.ITEM_TRINKET_CAP_TAG;

    protected int exp;
    protected int variant;
    protected float mana;
    protected boolean mainAbility;
    protected boolean altAbility;
    protected boolean isFirstUpdate;
    protected boolean isFirstEntityUpdate;
    protected SlotInformation slotInfo, oldSlotInfo;
    protected boolean sync;
    protected String crafter;
    protected String crafterUUID;
    protected ItemElementalAttributes elementalAttributes;
    protected Map<Integer, IAbilityInterface> itemAbilities;
    protected Map<String, IAbilityInterface> activeAbilities;

    public TrinketProperties(ItemStack stack) {
        super(stack);
        this.exp = 0;
        this.variant = 0;
        this.mana = 0;
        this.mainAbility = false;
        this.altAbility = false;
        this.isFirstUpdate = true;
        this.isFirstEntityUpdate = true;
        this.crafter = "";
        this.crafterUUID = "";
        this.elementalAttributes = new ItemElementalAttributes(stack);
        this.slotInfo = new SlotInformation(stack, ItemHandlerType.NONE.getName(), -1);
        this.oldSlotInfo = new SlotInformation(stack, ItemHandlerType.NONE.getName(), -1);
        this.itemAbilities = new TreeMap<>();
        this.activeAbilities = new TreeMap<>();
//        initAbilities(stack);
    }

    public void initAbilitiesOnce(Entity entity) {
        if (this.itemAbilities.isEmpty() && this.getItem() instanceof AccessoryBase) {
            final AccessoryBase accessory = (AccessoryBase) this.getItem();
            if (accessory instanceof TrinketRaceBase) {
                try {
                    TrinketRaceBase raceItem = (TrinketRaceBase) accessory;
                    EntityRacePropertiesHandler s = raceItem.getRace().getRaceHandler(null, Capabilities.getEntityProperties(entity), new RaceCache(raceItem.getRace(), raceItem.getPrimaryElement(this.getItemStack())));
                    Collection<IAbilityInterface> Abilities = s.getRaceAbilities().values();
                    s.startTransformation();
                    int i = 0;
                    for (IAbilityInterface ability : Abilities) {
                        this.itemAbilities.put(i, ability);
                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                List<IAbilityInterface> abilities = new ArrayList<>();
                accessory.initAbilities(this.getItemStack(), null, abilities);
                int i = 0;
                for (IAbilityInterface ability : abilities) {
                    this.itemAbilities.put(i, ability);
                    i++;
                }
            }
        }
    }

    public void addAbilitiesToPlayer(EntityLivingBase player, SlotInformation info) {
        if (!this.itemAbilities.isEmpty()) {
            final ItemHandlerType cacheType = info.getHandlerType();
            for (Entry<Integer, IAbilityInterface> attachedAbility : this.itemAbilities.entrySet()) {
                String key = attachedAbility.getValue().getRegistryName().toString();
                if (attachedAbility.getValue().getRequiredElement() == null || (attachedAbility.getValue().getRequiredElement() != null && this.getElementalAttributes().comparePrimaryElement(attachedAbility.getValue().getRequiredElement()))) {
                    if (((cacheType == ItemHandlerType.INVENTORY) || (cacheType == ItemHandlerType.HOTBAR)) && (attachedAbility.getValue() instanceof ITickableInventoryAbility)) {
                        if (!this.activeAbilities.containsKey(key)) {
                            this.activeAbilities.put(key, attachedAbility.getValue());
                        }
                    }
                    if (((cacheType == ItemHandlerType.MAINHAND) || (cacheType == ItemHandlerType.OFFHAND)) && (attachedAbility.getValue() instanceof IHeldAbility)) {
                        if (!this.activeAbilities.containsKey(key)) {
                            this.activeAbilities.put(key, attachedAbility.getValue());
                        }
                    }
                }
            }
            if (!this.activeAbilities.isEmpty()) {
                Capabilities.getEntityProperties(player, prop -> {
                    for (IAbilityInterface ability : this.activeAbilities.values()) {
                        prop.getAbilityHandler().registerAbility(player, this.getItem().getRegistryName().toString(), info, ability);
                    }
                });
            }
//        } else {
//            if (!(this.getItem() instanceof TrinketRaceBase)) {
//                this.initAbilitiesOnce(player, this.getItemStack());
//            }
        }
    }

    public ItemElementalAttributes getElementalAttributes() {
        return this.elementalAttributes;
    }

    public SlotInformation getSlotInfo() {
        return this.slotInfo;
    }

    public SlotInformation getOldSlotInfo() {
        return this.oldSlotInfo;
    }

    public ActionResult<ItemStack> itemRightClicked(World world, EntityPlayer player, EnumHand hand, ActionResult<ItemStack> defaultResult) {
        final ItemStack stack = player.getHeldItem(hand);
        if (world != null && !world.isRemote) {
            if (player.isSneaking()) {
                this.toggleAltAbility(!this.altAbility());
            } else {
                this.toggleMainAbility(!this.mainAbility());
            }
            this.scheduleResync();
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

    public void onCrafted(World world, EntityPlayer player) {
        if (player == null) {
            return;
        }
        try {
            if (player.getUniqueID() != null) {
                this.setCrafterUUID(player.getUniqueID().toString());
            }
            this.setCrafter(player.getDisplayNameString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCrafter() {
        return this.crafter;
    }

    public String getCrafterUUID() {
        return this.crafterUUID;
    }

    public void setCrafter(String crafter) {
        if (!crafter.isEmpty() && !this.crafter.contentEquals(crafter)) {
            this.crafter = crafter;
        }
    }

    public void setCrafterUUID(String crafterUUID) {
        if (!crafterUUID.isEmpty() && !this.crafterUUID.contentEquals(crafterUUID)) {
            this.crafterUUID = crafterUUID;
        }
    }

    public boolean updateSlotInfo(SlotInformation info) {
        return this.updateSlotInfo(this.getItemStack(), info.getSlot(), info.getHandlerType());
    }

    public boolean updateSlotInfo(ItemStack stack, int slot, ItemHandlerType handler) {
        final SlotInformation currentInfo = this.getSlotInfo();
        if (!currentInfo.compare(stack, slot, handler)) {
            this.oldSlotInfo = currentInfo;
            this.oldSlotInfo.setChanged(false);
            this.slotInfo = new SlotInformation(stack, handler, slot);
            return true;
        }
        return false;
    }

    public Map<Integer, IAbilityInterface> getAbilitiesProvided() {
        return this.itemAbilities;
    }

    public void onUpdate(World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack current = player.inventory.getCurrentItem();
            boolean selectedMainHand = isSelected && this.compareStacks(this.getItemStack(), current, false);
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
                    if (!this.compareStacks(this.getItemStack(), s, false)) {
                        ItemStack stackInSlot = player.inventory.armorInventory.get(itemSlot);
                        if (this.compareStacks(this.getItemStack(), stackInSlot, false)) {
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
                            if (this.compareStacks(this.getItemStack(), player.getHeldItemOffhand(), false)) {
                                type = ItemHandlerType.OFFHAND;
                            }
                        }
                    }
                }
            }

            if (!world.isRemote) {
                final boolean moved = this.getSlotInfo().getSlot() != itemSlot;
                final boolean typeChanged = this.getSlotInfo().getHandlerType().compareTo(type) != 0;
                if (this.isFirstUpdate || moved || typeChanged) {
                    if (this.getItem() instanceof TrinketDamageShield) {
                        this.setVariant(0);
                    }
                    if (this.updateSlotInfo(this.getItemStack(), itemSlot, type)) {
                        this.saveToNBT(this.getTag());
                        this.getSlotInfo().setChanged(true);
                    }
                    if (player instanceof EntityPlayerMP) {
                        UpdateDataForTrinketPacket packet = new UpdateDataForTrinketPacket(player, this.getTag(), itemSlot, type, 1);
                        NetworkHandler.sendTo(packet, (EntityPlayerMP) player);
                    }

                }
            }
            this.initAbilitiesOnce(player);
            this.addAbilitiesToPlayer(player, this.getSlotInfo());
            if (this.getSlotInfo().changed()) {
                if (!this.itemAbilities.isEmpty()) {
                    this.itemAbilities.clear();
                }
                if (!this.activeAbilities.isEmpty()) {
                    this.activeAbilities.clear();
                }
                this.getSlotInfo().setChanged(false);
            }
            if (!this.getTickHandler().getCounters().isEmpty()) {
                for (Entry<String, Counter> counter : this.getTickHandler().getCounters().entrySet()) {
                    counter.getValue().Tick();
                }
            }
            if (this.sync) {
                this.sendInformationToTracking(player);
                this.sync = false;
            }
        }
        if (this.isFirstUpdate) {
            this.isFirstUpdate = false;
        }
    }

    public void onArmorTick(World world, EntityPlayer player) {
        this.onEntityArmorTick(world, player);
    }

    public void onEntityArmorTick(World world, EntityLivingBase entity) {

    }

    /**
     * Runs when worn in bauble/trinket slot
     *
     */
    public void onEntityTick(EntityLivingBase entity) {
        if (this.isFirstEntityUpdate) {
            if (!this.itemAbilities.isEmpty()) {
                this.itemAbilities.clear();
            }
            if (!this.activeAbilities.isEmpty()) {
                this.activeAbilities.clear();
            }
            if (!(this.getItem() instanceof TrinketRaceBase)) {
                this.initAbilitiesOnce(entity);
            }
        }
        ItemStack logicCheck = this.getSlotInfo().getStackFromHandler(entity);
        if (this.compareStacks(this.getItemStack(), logicCheck, false)) {
            if (!this.itemAbilities.isEmpty()) {
                for (Entry<Integer, IAbilityInterface> attachedAbility : this.itemAbilities.entrySet()) {
                    if (attachedAbility.getValue().getRequiredElement() == null || attachedAbility.getValue().getRequiredElement() != null && this.getElementalAttributes().comparePrimaryElement(attachedAbility.getValue().getRequiredElement())) {
                        String key = attachedAbility.getValue().getRegistryName().toString();
                        if (!this.activeAbilities.containsKey(key)) {
                            this.activeAbilities.put(key, attachedAbility.getValue());
                        }
                    }
                }
                if (!this.activeAbilities.isEmpty()) {
                    Capabilities.getEntityProperties(entity, prop -> {
                        for (IAbilityInterface ability : this.activeAbilities.values()) {
                            prop.getAbilityHandler().replaceAbility(entity, this.getItem().getRegistryName().toString(), new SlotInformation(this.getItemStack(), this.getSlotInfo().getHandler(), this.getSlotInfo().getSlot()), ability);
                        }
                    });
                }
            }
        }
        if (this.sync) {
            this.sendInformationToTracking(entity);
            this.sync = false;
        }
        if (this.isFirstEntityUpdate) {
            this.isFirstEntityUpdate = false;
        }
    }

    public void onPlayerTick(ItemStack stack, EntityPlayer player) {
        this.onEntityTick(player);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~Keybind handler~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public boolean compareWithStack(ItemStack second, boolean ignoreDurability) {
        return this.compareStacks(this.getItemStack(), second, ignoreDurability);
    }

    public boolean compareStacks(ItemStack first, ItemStack second, boolean ignoreDurability) {
        if ((first == null) || (second == null)) {
            return false;
        }
        if ((first.isEmpty() && !second.isEmpty()) || (!first.isEmpty() && second.isEmpty())) {
            return false;
        }
        Item firstItem = first.getItem();
        Item secondItem = second.getItem();
        ResourceLocation firstItemID = firstItem.getRegistryName();
        ResourceLocation secondItemID = secondItem.getRegistryName();
        //		firstItem.getRegistryName().compareTo(secondItem.getRegistryName());
        if (firstItemID.compareTo(secondItemID) != 0) {
            return false;
        } else {
            if (first.isItemEqual(second)) {
                return true;
            } else return first.isItemEqualIgnoreDurability(second) && ignoreDurability;
        }
    }

    public void itemEquipped(EntityLivingBase entity) {
        final boolean isClient = entity.world.isRemote;
        if (!isClient) {
            final SlotInformation info = TrinketHelper.getSlotInfoForItemFromAccessory(entity, s -> s.isItemEqual(this.getItemStack()));
            if (info != null) {
                if (this.getItem() instanceof TrinketDamageShield) {
                    if (TrinketsConfig.SERVER.MISC.VIPS) {
                        Capabilities.getVipStatus(entity, status -> {
                            this.setVariant(status.getStatus());
                        });
                    }
                }
                this.getSlotInfo().setHandler(info.getHandlerType());
                this.getSlotInfo().setSlot(info.getSlot());
                this.getSlotInfo().setChanged();
                this.saveToNBT(this.getTag());
                this.sendInformationToPlayer(entity);
                this.scheduleResync();
            }
        }
    }

    public void itemUnequipped(EntityLivingBase entity) {
        final boolean isClient = entity.world.isRemote;
        if (!isClient) {
            this.getSlotInfo().setHandler(ItemHandlerType.NONE);
            this.getSlotInfo().setSlot(-1);
            this.getSlotInfo().setChanged();
            if (this.getItem() instanceof TrinketDamageShield) {
                this.setVariant(0);
            }
            this.saveToNBT(this.getTag());
            this.getTickHandler().clearCounters();
            this.sendInformationToPlayer(entity);
            this.scheduleResync();
        }
    }

    public void turnOff() {
        this.toggleMainAbility(false);
        this.toggleAltAbility(false);
    }

    public boolean isEquipped() {
        return (this.getSlotInfo().getHandlerType().equals(ItemHandlerType.TRINKETS) || this.getSlotInfo().getHandlerType().equals(ItemHandlerType.BAUBLES)) && (this.getSlotInfo().getSlot() > -1);
    }

    //Handle Network stuff

    public void sendInformationToPlayer(EntityLivingBase receiver) {
        this.sendInformationToPlayer(receiver, receiver);
    }

    public void sendInformationToPlayer(EntityLivingBase e, EntityLivingBase receiver) {
        if (!e.getEntityWorld().isRemote && (receiver instanceof EntityPlayerMP)) {
            final SyncItemDataPacket packet = new SyncItemDataPacket(e, this.getItemStack(), this.saveToNBT(new NBTTagCompound()), this.getSlotInfo().getSlot(), this.getSlotInfo().getHandlerType(), (this.getSlotInfo().getHandlerType().getId() == ItemHandlerType.BAUBLES.getId()), this.isEquipped());
            NetworkHandler.sendTo(packet, (EntityPlayerMP) receiver);
        }
    }

    public void sendInformationToTracking(EntityLivingBase e) {
        final World world = e.getEntityWorld();
        if (!world.isRemote && (world instanceof WorldServer)) {
            final SyncItemDataPacket packet = new SyncItemDataPacket(e, this.getItemStack(), this.saveToNBT(new NBTTagCompound()), this.getSlotInfo().getSlot(), this.getSlotInfo().getHandlerType(), (this.getSlotInfo().getHandlerType().getId() == ItemHandlerType.BAUBLES.getId()), this.isEquipped());
            NetworkHandler.sendToClients((WorldServer) world, e.getPosition(), packet);
        }
    }

    public void scheduleResync() {
        this.sync = true;
    }

    //Handle Network stuff end.

    public boolean mainAbility() {
        return this.mainAbility;
    }

    public void toggleMainAbility(boolean bool) {
        if (this.mainAbility != bool) {
            this.mainAbility = bool;
        }
    }

    public boolean altAbility() {
        return this.altAbility;
    }

    public void toggleAltAbility(boolean bool) {
        if (this.altAbility != bool) {
            this.altAbility = bool;
        }
    }

    public int StoredExp() {
        return this.exp;
    }

    public void setStoredExp(int integer) {
        if (this.exp != integer) {
            this.exp = integer;
        }
    }

    public int getSlot() {
        return this.slotInfo.getSlot();
    }

    public float StoredMana() {
        return this.mana;
    }

    public void setStoredMana(float mana) {
        if (this.mana != mana) {
            this.mana = mana;
        }
    }

    public int getVariant() {
        return this.variant;
    }

    public void setVariant(int variant) {
        if (this.variant != variant) {
            this.variant = variant;
        }
    }

    @Override
    public NBTTagCompound saveToNBT(@Nonnull NBTTagCompound compound) {
        compound.setString("crafter.name", this.getCrafter());
        compound.setString("crafter.uuid", this.getCrafterUUID());
        if (this.getItem() instanceof AccessoryBase) {
            compound.setInteger("slot", this.getSlotInfo().getSlot());
            compound.setString("handler", this.getSlotInfo().getHandler());
        }
        compound.setInteger("variant", this.getVariant());
        compound.setInteger("exp", this.StoredExp());
        compound.setFloat("mana", this.StoredMana());
        if (this.getItem() instanceof AccessoryBase) {
            compound.setBoolean("main.ability", this.mainAbility());
            compound.setBoolean("alt.ability", this.altAbility());
        }
        this.getTickHandler().saveCountersToNBT(compound);
        this.getElementalAttributes().saveToNBT(compound);
        return compound;
    }

    @Override
    public void loadFromNBT(NBTTagCompound compound) {
        NBTHelper.hasString(compound, "crafter.name", this::setCrafter);
        NBTHelper.hasString(compound, "crafter.uuid", this::setCrafterUUID);
        if (this.getItem() instanceof AccessoryBase) {
            NBTHelper.hasInteger(compound, "slot", (value) -> {
                this.getSlotInfo().setSlot(value);
            });
            NBTHelper.hasString(compound, "handler", (string) -> {
                this.getSlotInfo().setHandler(string);
            });
        }
        NBTHelper.hasInteger(compound, "variant", this::setVariant);
        NBTHelper.hasInteger(compound, "exp", this::setStoredExp);
        NBTHelper.hasFloat(compound, "mana", this::setStoredMana);
        if (this.getItem() instanceof AccessoryBase) {
            NBTHelper.hasBoolean(compound, "main.ability", this::toggleMainAbility);
            NBTHelper.hasBoolean(compound, "alt.ability", this::toggleAltAbility);
        }
        this.getTickHandler().loadCountersFromNBT(compound);
        this.getElementalAttributes().loadFromNBT(compound);
    }
}
