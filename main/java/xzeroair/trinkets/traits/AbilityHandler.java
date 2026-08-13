package xzeroair.trinkets.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.network.AbilityCacheSyncPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.traits.abilities.interfaces.*;
import xzeroair.trinkets.util.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class AbilityHandler {

    public static final String capKey = Reference.MODID + ":abilities";
    private static final String DISABLED_SOURCES = "DisabledSources";
    private final EntityProperties parentProperties;
    protected Map<String, AbilityHolder> active = new TreeMap<>();
    protected boolean hasChanged = false;

    public AbilityHandler(EntityProperties properties) {
        this.parentProperties = properties;
    }

    public EntityProperties getParentProperties() {
        return this.parentProperties;
    }

    // Exposes the active ability map for event handlers and UI code.
    public Map<String, AbilityHolder> getActiveAbilities() {
        return this.active;
    }

    // Registers a batch of non-item abilities with a generic OTHER source context.
    public void registerAbilities(EntityLivingBase entity, String source, @Nonnull List<? extends IAbilityInterface> abilities) {
        for (IAbilityInterface ability : abilities) {
            this.registerAbility(entity, source, new SlotInformation(ItemHandlerType.OTHER), ability);
        }
    }

    // Registers a batch of abilities using the provided source slot information.
    public void registerAbilities(EntityLivingBase entity, String source, SlotInformation info, @Nonnull List<? extends IAbilityInterface> abilities) {
        for (IAbilityInterface ability : abilities) {
            this.registerAbility(entity, source, info, ability);
        }
    }

    // Registers a race ability, replacing any lower-priority source already owning the same ability key.
    public IAbilityInterface registerRaceAbility(EntityLivingBase entity, String source, IAbilityInterface ability) {
        return this.replaceAbility(entity, source, new SlotInformation(ItemHandlerType.RACE), ability);
    }

    /**
     * Returns the ability if it failed to replace the ability
     * Returns null if it successfully added the ability
     * Returns the old Ability if it was replaced
     *
     * @param source
     * @param info
     * @param ability
     * @return
     */
    // Adds or replaces an ability when the incoming source is allowed to take ownership of that key.
    public IAbilityInterface replaceAbility(@Nonnull EntityLivingBase entity, String source, SlotInformation info, @Nonnull IAbilityInterface ability) {
        final String key = ability.getRegistryName().toString();
        if (!entity.world.isRemote) {
            if (!ability.isAbilityEnabled()) {
                if (!this.hasKillOrder(source, key)) {
                    this.addKillOrder(source, key);
                    this.sendKillOrder(entity, source, key);
                }
                return ability;
            } else {
                if (this.hasKillOrder(source, key)) {
                    this.removeKillOrder(source, key);
                }
            }
        } else {
            if (this.hasKillOrder(source, key)) {
                return ability;
            }
        }
        if (ability.shouldRemove()) {
            return ability;
        }
        if (info == null) {
            info = new SlotInformation(ItemHandlerType.OTHER);
        }
        AbilityHolder value = this.active.get(key);
        if (value == null) {
            AbilityHolder holder = new AbilityHolder(this, source, info, ability);
            holder.getAbility().setFirstUpdate(true);
            this.active.put(key, holder);
            return null;
        } else {
            if (!value.sameAbilityOrigin(source, info, ability)) {
                value.getAbility().onAbilityRemoved(entity);
                AbilityHolder holder = new AbilityHolder(this, source, info, ability);
                holder.getAbility().setFirstUpdate(true);
                this.active.put(key, holder);
                return value.getAbility();
            }
        }
        return ability;
    }

    @Nullable
    public IAbilityInterface registerAbility(EntityLivingBase entity, String source, IAbilityInterface ability) {
        return this.registerAbility(entity, source, new SlotInformation(ItemHandlerType.OTHER), ability);
    }

    @Nullable
    // Adds an ability only if no source currently owns that ability key.
    public IAbilityInterface registerAbility(@Nonnull EntityLivingBase entity, String source, SlotInformation info, @Nonnull IAbilityInterface ability) {
        final String key = ability.getRegistryName().toString();
        if (!entity.world.isRemote) {
            if (!ability.isAbilityEnabled()) {
                if (!this.hasKillOrder(source, key)) {
                    this.addKillOrder(source, key);
                    this.sendKillOrder(entity, source, key);
                }
                return ability;
            } else {
                if (this.hasKillOrder(source, key)) {
                    this.removeKillOrder(source, key);
                }
            }
        } else {
            if (this.hasKillOrder(source, key)) {
                return ability;
            }
        }
        if (ability.shouldRemove()) {
            return ability;
        }
        if (info == null) {
            info = new SlotInformation(ItemHandlerType.OTHER);
        }
        if (!this.active.containsKey(key)) {
            AbilityHolder holder = new AbilityHolder(this, source, info, ability);
            holder.getAbility().setFirstUpdate(true);
            this.active.put(key, holder);
            return null;
        }
        return ability;
    }

    @Nullable
    // Removes an active ability by registry key and runs its teardown hook.
    public IAbilityInterface removeAbility(String ability) {
        if (this.active.containsKey(ability)) {
            AbilityHolder oldHolder = this.active.remove(ability);
            IAbilityInterface oldAbility = oldHolder.getAbility();
            oldAbility.onAbilityRemoved(this.parentProperties.getEntity());
            return oldAbility;
        }
        return null;
    }

    @Nullable
    // Returns the cached holder for an active ability key, if present.
    public AbilityHolder getAbilityHolder(String ability) {
        if (this.active.containsKey(ability)) {
            return this.active.get(ability);
        }
        return null;
    }

    @Nullable
    // Returns the active ability instance for the given registry key.
    public IAbilityInterface getAbility(String ability) {
        AbilityHolder holder = this.getAbilityHolder(ability);
        if (holder != null) {
            return holder.getAbility();
        }
        return null;
    }

    // Drops abilities that were already marked for removal before the main update pass runs.
    public void onUpdatePre(EntityLivingBase entity) {
        this.active.values().removeIf(cache -> cache.getAbility().shouldRemove());
    }

    // Runs first-update initialization, ticking, sync, and removal checks for active abilities.
    public void onUpdate(EntityLivingBase entity) {
        for (Entry<String, AbilityHolder> entry : this.active.entrySet()) {
            final AbilityHolder cache = entry.getValue();
            final IAbilityInterface ability = cache.getAbility();
            if (ability.shouldRemove()) {
                ability.onAbilityRemoved(entity);
            } else {
                if (ability.isFirstUpdate()) {
                    if (!entity.world.isRemote) {
                        this.loadAbilityFromEntity(entity, ability);
                    }
                    if (ability.shouldRemove() || !ability.isAbilityEnabled()) {
                        ability.onAbilityRemoved(entity);
                        ability.setFirstUpdate(false);
                        continue;
                    }
                    ability.onAbilityAdded(this.parentProperties.getEntity());
                }
                this.processAbility(ability, this.parentProperties.getEntity());
                if (ability.hasChanged()) {
                    this.saveInfoOnChange(this.parentProperties.getEntity(), ability);
                    this.sendNBTToPlayer(this.parentProperties.getEntity(), ability);
                    ability.setChanged(false);
                }
                ability.setFirstUpdate(false);
                if (this.shouldRemove(entry, this.parentProperties.getEntity())) {
                    ability.scheduleRemoval();
                }
                if (ability.shouldRemove()) {
                    ability.onAbilityRemoved(entity);
                }
            }
        }
    }

    // Clears the dirty flag after the handler has finished its tick work.
    public void onUpdatePost(EntityLivingBase entity) {
        if (this.hasChanged) {
            this.hasChanged = false;
        }
    }

    // Reports whether this handler changed during the current update cycle.
    public boolean hasChanged() {
        return this.hasChanged;
    }

    // Dispatches an ability's per-tick behavior based on the interfaces it implements.
    private void processAbility(IAbilityInterface ability, EntityLivingBase entity) {
        try {
            final AbilityHolder holder = this.getAbilityHolder(ability.getRegistryName().toString());
            final SlotInformation info = holder != null ? holder.getInfo() : null;
            if (ability instanceof ITickableAbility) {
                ((ITickableAbility) ability).tickAbility(entity);
            }
            if (ability instanceof IEquippedAbility) {
                final IEquippedAbility a = ((IEquippedAbility) ability);
                a.head(entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD), entity);
                a.chest(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST), entity);
                a.legs(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS), entity);
                a.feet(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET), entity);
            }
            if (ability instanceof IHeldAbility) {
                final IHeldAbility a = ((IHeldAbility) ability);
                a.heldMainHand(entity.getHeldItemMainhand(), entity);
                a.heldOffhand(entity.getHeldItemOffhand(), entity);
            }
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) entity;
                if (ability instanceof IContainerAbility) {
                    final IContainerAbility a = ((IContainerAbility) ability);
                    a.inventoryContainer(player.inventoryContainer);
                    a.openContainer(player.openContainer);
                    a.playerInventory(player.inventory);
                }
                if (ability instanceof ITickableInventoryAbility) {
                    final ITickableInventoryAbility a = ((ITickableInventoryAbility) ability);
                    final int inHand = player.inventory.currentItem;
                    if (info != null) {
                        final ItemStack stack = info.getStackFromHandler(player);
                        if (!stack.isEmpty()) {
                            final int slot = info.getSlot();
                            final boolean selected = info.getHandlerType().equals(ItemHandlerType.MAINHAND) && (inHand == slot);
                            a.onUpdate(stack, player.world, player, slot, selected);
                        } else {
                            final NonNullList<ItemStack> inventory = player.inventory.mainInventory;
                            for (int index = 0; index < inventory.size(); index++) {
                                final ItemStack IStack = inventory.get(index);
                                if (!IStack.isEmpty()) {
                                    a.onUpdate(IStack, player.world, player, index, inHand == index);
                                }
                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            Trinkets.LOGGER.error("Error with ability:{}", ability.getRegistryName().toString());
            e.printStackTrace();
        }
    }

    // Evaluates whether the current source is still valid for the cached ability owner.
    private boolean shouldRemove(@Nonnull Entry<String, AbilityHolder> entry, EntityLivingBase entity) {
        final String key = entry.getKey();
        final AbilityHolder cache = entry.getValue();
        final String source = cache.getSourceID();
        if (this.hasKillOrder(source, key)) {
            return true;
        }
        final SlotInformation sourceInfo = cache.getInfo();
        final IAbilityInterface ability = cache.getAbility();
        switch (sourceInfo.getHandlerType()) {
            case NONE:
                return true;
            case OTHER:
                return false;
            case RACE:
                RaceCache raceCache = this.parentProperties.getCurrentRaceCache();
                boolean eleReq = (ability.getRequiredElement() != null && !(raceCache.comparePrimaryElement(ability.getRequiredElement())));
                boolean race = raceCache.getRace().getRegistryName().toString().contentEquals(source);
                return !race || raceCache.getRace().isNone() || eleReq;
            case POTION:
                Potion potion = Potion.getPotionFromResourceLocation(source);
                return potion == null || !entity.isPotionActive(potion);
            default:
                ItemStack s = sourceInfo.getStackFromHandler(entity);
                if (s.isEmpty()) {
                    return true;
                }
                boolean sameSource = s.getItem().getRegistryName().toString().contentEquals(sourceInfo.getItemID());
                if (!sameSource) {
                    return true;
                }
//                final Element raceEle = Capabilities.getEntityProperties(entity, Elements.NEUTRAL, (prop, rtn) -> prop.getCurrentRace().getElement());
                boolean remove = Capabilities.getTrinketProperties(s, false, (prop, bool) -> {
                    return !sourceInfo.compare(prop.getSlotInfo());//|| (ability.getRequiredElement() != null && (raceEle != ability.getRequiredElement()));
                });
                return remove;
        }
    }
//    private boolean shouldRemove(SlotInformation info, String source, EntityLivingBase entity) {
// 		case TRINKETS:
//			final ITrinketContainerHandler TrinketHandler = getTrinketHandler(entity);
//			return TrinketHandler != null ? getTrinketHandler(entity).getStackInSlot(this.getSlot()) : ItemStack.EMPTY;
//		case BAUBLES:
//			if (Trinkets.Baubles) {
//				IItemHandler BaublesHandler = BaublesHelper.getBaublesHandler(entity);
//				if (BaublesHandler != null)
//					return BaublesHandler.getStackInSlot(this.getSlot());
//			}
//			return ItemStack.EMPTY;
//		case HEAD:
//			return entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
//		case CHEST:
//			return entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
//		case LEGS:
//			return entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
//		case FEET:
//			return entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
//		case OFFHAND:
//			return entity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
//		case MAINHAND:
//			return entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
//		case HOTBAR:
//			return (entity instanceof EntityPlayer) && InventoryPlayer.isHotbar(this.getSlot()) ? ((EntityPlayer) entity).inventory.getStackInSlot(this.getSlot()) : ItemStack.EMPTY;
//		case INVENTORY:
//			return (entity instanceof EntityPlayer) ? ((EntityPlayer) entity).inventory.getStackInSlot(this.getSlot()) : ItemStack.EMPTY;
//}

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
    // Transfers active ability ownership from another handler during capability copy operations.
    public void copyFrom(AbilityHandler source, boolean wasDeath, boolean keepInv) {
        // Active runtime ability instances are rebuilt from their owning race/item/potion sources.
        // Do not copy them across entity ownership boundaries.
    }


    // Checks whether a specific source is currently disabled for the given ability key.
    public boolean hasKillOrder(String source, String ability) {
        if (source == null || source.isEmpty() || ability == null || ability.isEmpty()) {
            return false;
        }

        NBTTagCompound playerCap = this.parentProperties.getTag().getCompoundTag(capKey);
        if (!playerCap.hasKey(ability)) {
            return false;
        }

        NBTTagCompound abilityTag = playerCap.getCompoundTag(ability);
        if (!abilityTag.hasKey(DISABLED_SOURCES)) {
            return false;
        }

        NBTTagCompound disabledSources = abilityTag.getCompoundTag(DISABLED_SOURCES);
        return disabledSources.hasKey(source) && disabledSources.getBoolean(source);
    }

    // Marks a specific ability source as disabled in persistent capability data.
    public void addKillOrder(String source, String ability) {
        if (source == null || source.isEmpty() || ability == null || ability.isEmpty()) {
            return;
        }

        NBTTagCompound rootTag = this.parentProperties.getTag();
        if (!rootTag.hasKey(capKey)) {
            rootTag.setTag(capKey, new NBTTagCompound());
        }

        NBTTagCompound playerCap = rootTag.getCompoundTag(capKey);
        if (!playerCap.hasKey(ability)) {
            playerCap.setTag(ability, new NBTTagCompound());
        }

        NBTTagCompound abilityTag = playerCap.getCompoundTag(ability);
        if (!abilityTag.hasKey(DISABLED_SOURCES)) {
            abilityTag.setTag(DISABLED_SOURCES, new NBTTagCompound());
        }

        abilityTag.getCompoundTag(DISABLED_SOURCES).setBoolean(source, true);
    }

    // Removes a disabled source marker and notifies clients that the source is enabled again.
    public void removeKillOrder(String source, String ability) {
        if (source == null || source.isEmpty() || ability == null || ability.isEmpty()) {
            return;
        }

        NBTTagCompound playerCap = this.parentProperties.getTag().getCompoundTag(capKey);
        if (!playerCap.hasKey(ability)) {
            return;
        }

        NBTTagCompound abilityTag = playerCap.getCompoundTag(ability);
        if (!abilityTag.hasKey(DISABLED_SOURCES)) {
            return;
        }

        NBTTagCompound disabledSources = abilityTag.getCompoundTag(DISABLED_SOURCES);
        if (!disabledSources.hasKey(source)) {
            return;
        }

        disabledSources.removeTag(source);

        if (disabledSources.isEmpty()) {
            abilityTag.removeTag(DISABLED_SOURCES);
        }
        if (abilityTag.isEmpty()) {
            playerCap.removeTag(ability);
        }

        EntityLivingBase entity = this.parentProperties.getEntity();
        World world = entity.getEntityWorld();
        if (world instanceof WorldServer && entity instanceof EntityPlayerMP) {
            NBTTagCompound syncTag = new NBTTagCompound();
            syncTag.setString("Ability", ability);
            syncTag.setString("Source", source);
            syncTag.setBoolean("ENABLED", true);
            NetworkHandler.sendToClients((WorldServer) world, entity.getPosition(), new AbilityCacheSyncPacket(entity, syncTag));
        }
    }

    // Broadcasts a disabled-source update so clients suppress the same ability source locally.
    public void sendKillOrder(EntityLivingBase entity, String source, String ability) {
        if (entity == null || source == null || source.isEmpty() || ability == null || ability.isEmpty()) {
            return;
        }

        World world = entity.getEntityWorld();
        if (world instanceof WorldServer && entity instanceof EntityPlayerMP) {
            NBTTagCompound syncTag = new NBTTagCompound();
            syncTag.setString("Ability", ability);
            syncTag.setString("Source", source);
            syncTag.setBoolean("DISABLED", true);
            NetworkHandler.sendToClients((WorldServer) world, entity.getPosition(), new AbilityCacheSyncPacket(entity, syncTag));
        }
    }

    // Sends per-ability storage and transient sync data to the owner and tracking clients.
    private void sendNBTToPlayer(EntityLivingBase entity, IAbilityInterface ability) {
        World world = entity.getEntityWorld();
        if (world instanceof WorldServer && entity instanceof EntityPlayerMP) {
            String key = ability.getRegistryName().toString();
            NBTTagCompound sync = new NBTTagCompound();
            NBTTagCompound tag = ability.saveStorage(new NBTTagCompound());
            NBTTagCompound data = ability.sendAbilityData();
            if (data != null && !data.isEmpty()) {
                sync.setTag("data", data);
            }
            if (tag != null && !tag.isEmpty()) {
                sync.setTag(key, tag);
            }
            if (!sync.isEmpty()) {
                sync.setString("Ability", key);
                AbilityCacheSyncPacket packet = new AbilityCacheSyncPacket(entity, sync);
                NetworkHandler.sendTo(packet, (EntityPlayerMP) entity);
                NetworkHandler.sendToClients((WorldServer) world, entity.getPosition(), packet);
            }
        }
    }

    // Writes changed ability data back into the owning entity capability on the server.
    private void saveInfoOnChange(EntityLivingBase entity, IAbilityInterface ability) {
        if (!entity.world.isRemote) {
            this.saveInfoToEntity(entity, ability);
        }
    }

    // Persists the current enabled state and saved storage for a single ability source.
    private void saveInfoToEntity(EntityLivingBase entity, IAbilityInterface ability) {
        if (entity == null || ability == null || ability.getRegistryName() == null) {
            return;
        }

        World world = entity.getEntityWorld();
        if (!(world instanceof WorldServer) || !(entity instanceof EntityPlayerMP)) {
            return;
        }

        NBTTagCompound rootTag = this.parentProperties.getTag();
        if (!rootTag.hasKey(capKey)) {
            rootTag.setTag(capKey, new NBTTagCompound());
        }

        final String abilityName = ability.getRegistryName().toString();
        NBTTagCompound abilitiesTag = rootTag.getCompoundTag(capKey);
        if (!abilitiesTag.hasKey(abilityName)) {
            abilitiesTag.setTag(abilityName, new NBTTagCompound());
        }

        final String source = ability.getAbilityHolder().getSourceID();
        NBTTagCompound abilityTag = abilitiesTag.getCompoundTag(abilityName);
        if (!ability.isAbilityEnabled()) {
            if (!abilityTag.hasKey(DISABLED_SOURCES)) {
                abilityTag.setTag(DISABLED_SOURCES, new NBTTagCompound());
            }
            abilityTag.getCompoundTag(DISABLED_SOURCES).setBoolean(source, true);
            return;
        }
        if (abilityTag.hasKey(DISABLED_SOURCES)) {
            NBTTagCompound disabledSources = abilityTag.getCompoundTag(DISABLED_SOURCES);
            if (disabledSources.hasKey(source)) {
                disabledSources.removeTag(source);
            }
            if (disabledSources.isEmpty()) {
                abilityTag.removeTag(DISABLED_SOURCES);
            }
        }
        ability.saveStorage(abilityTag);
    }

    // Restores saved storage for an ability and applies kill-order state on first load.
    public void loadAbilityFromEntity(EntityLivingBase entity, IAbilityInterface ability) {
        if (ability == null || ability.getRegistryName() == null) {
            return;
        }
        NBTTagCompound rootTag = this.parentProperties.getTag();
        if (!rootTag.hasKey(capKey)) {
            rootTag.setTag(capKey, new NBTTagCompound());
        }
        final String abilityName = ability.getRegistryName().toString();
        NBTTagCompound abilitiesTag = rootTag.getCompoundTag(capKey);
        if (!abilitiesTag.hasKey(abilityName)) {
            return;
        }
        final String source = ability.getAbilityHolder().getSourceID();
        NBTTagCompound abilityTag = abilitiesTag.getCompoundTag(abilityName);
        if (this.hasKillOrder(source, abilityName)) {
            ability.scheduleRemoval();
            ability.setAbilityEnabled(false);
            return;
        }
        if (!abilityTag.isEmpty()) {
            ability.loadStorage(abilityTag);
        }
    }

    // Loads a single ability's saved storage from a keyed NBT payload.
    public void loadAbilityFromNBT(@Nonnull IAbilityInterface ability, @Nonnull NBTTagCompound compound) {
        String key = ability.getRegistryName().toString();
        if (compound.hasKey(key)) {
            ability.loadStorage(compound.getCompoundTag(key));
        }
    }

    // Serializes all active ability storage into a dedicated ability compound.
    public NBTTagCompound saveAbilitiesToNBT(@Nonnull NBTTagCompound compound) {
        if (!compound.hasKey(capKey)) {
            compound.setTag(capKey, new NBTTagCompound());
        }
        for (Entry<String, AbilityHolder> entry : this.active.entrySet()) {
            String key = entry.getKey();
            AbilityHolder value = entry.getValue();
            NBTTagCompound tag = compound.getCompoundTag(capKey);
            if (!tag.hasKey(key)) {
                NBTTagCompound abilityTag = value.getAbility().saveStorage(new NBTTagCompound());
                if (!abilityTag.isEmpty()) {
                    tag.setTag(key, abilityTag);
                }
            } else {
                try {
                    NBTTagCompound ability = value.getAbility().saveStorage(new NBTTagCompound());
                    if (!ability.isEmpty()) {
                        tag.setTag(key, ability);
                    }
                } catch (final Exception e) {
                    Trinkets.LOGGER.error("Error when saving ability:{}", key);
                    e.printStackTrace();
                }
            }
        }
        return compound;
    }

    // Loads saved storage for any active abilities present in the supplied compound.
    public void loadAbilitiesFromNBT(@Nonnull NBTTagCompound compound) {
        if (compound.hasKey(capKey)) {
            final NBTTagCompound tag = compound.getCompoundTag(capKey);
            for (Entry<String, AbilityHolder> entry : this.active.entrySet()) {
                String key = entry.getKey();
                if (tag.hasKey(key)) {
                    AbilityHolder value = entry.getValue();
                    try {
                        this.loadAbilityFromNBT(value.getAbility(), tag.getCompoundTag(key));
                    } catch (final Exception e) {
                        Trinkets.LOGGER.error("Error when loading ability:{}", key);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Ensures the ability capability tag exists when the parent capability is serialized.
    public NBTTagCompound saveToNBT(@Nonnull NBTTagCompound compound) {
        if (!compound.hasKey(capKey)) {
            compound.setTag(capKey, new NBTTagCompound());
        }
        NBTTagCompound tag = compound.getCompoundTag(capKey);
        return compound;
    }

    // Reserved hook for loading handler-level data from the parent capability compound.
    public void loadFromNBT(@Nonnull NBTTagCompound compound) {
        if (compound.hasKey(capKey)) {
            NBTTagCompound tag = compound.getCompoundTag(capKey);

        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Call Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public static class AbilityHolder {
        protected AbilityHandler handler;
        protected String source;
        protected SlotInformation info;
        protected IAbilityInterface ability;

        // Captures the source metadata for a single active ability owner.
        public AbilityHolder(AbilityHandler handler, String source, SlotInformation info, @Nonnull IAbilityInterface ability) {
            this.handler = handler;
            this.source = source;
            this.info = info;
            this.ability = ability.cacheAbilityHolder(this);
        }

        // Returns the ability handler that owns this holder.
        public final AbilityHandler getHandler() {
            return this.handler;
        }

        // Returns the source id that originally registered this ability.
        public final String getSourceID() {
            return this.source;
        }

        // Returns the cached source slot/type information for this ability owner.
        public final SlotInformation getInfo() {
            return this.info;
        }

        // Returns the active ability instance cached by this holder.
        public final IAbilityInterface getAbility() {
            return this.ability;
        }

        // Compares this holder against another holder using the same ownership rules as registration.
        public final boolean sameAbilityOrigin(@Nonnull AbilityHolder other) {
            return this.sameAbilityOrigin(other.getSourceID(), other.getInfo(), other.getAbility());
        }

        // Decides whether this holder keeps ownership against an incoming source for the same ability key.
        public final boolean sameAbilityOrigin(String otherSource, @Nonnull SlotInformation otherInfo, @Nonnull IAbilityInterface otherAbility) {
            final ItemHandlerType handlerType = this.getInfo().getHandlerType();
            final ItemHandlerType otherHandlerType = otherInfo.getHandlerType();
            final boolean isRaceAbility = handlerType == ItemHandlerType.RACE;
            final boolean isOtherRaceAbility = otherHandlerType == ItemHandlerType.RACE;
            boolean sameSource = this.getSourceID().contentEquals(otherSource);
            boolean sameElementRequired = this.getAbility().getRequiredElement() == otherAbility.getRequiredElement();

            if (isRaceAbility && !isOtherRaceAbility) {
                return true;
            }
            if (!isRaceAbility && isOtherRaceAbility) {
                return false;
            }
            if (handlerType != otherHandlerType) {
                return true;
            }

            switch (handlerType) {
                case RACE:
                    return sameSource && sameElementRequired;
                case POTION:
                    return sameSource;
                default:
                    return sameSource && sameElementRequired;
            }
        }
    }
}
