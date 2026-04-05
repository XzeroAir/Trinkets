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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class AbilityHandler {

    public static final String capKey = Reference.MODID + ":abilities";
    public static final String KILL_ORDER = "DISABLED";
    public static final String REMOVE_KILL_ORDER = "ENABLED";
    private final EntityProperties parentProperties;
    protected Map<String, AbilityHolder> active = new TreeMap<>();
    protected List<String> removedAbilities = new ArrayList<>();
    protected boolean hasChanged = false;

    public AbilityHandler(EntityProperties properties) {
        this.parentProperties = properties;
    }

    public Map<String, AbilityHolder> getActiveAbilities() {
        return this.active;
    }

    public void registerAbilities(EntityLivingBase entity, String source, @Nonnull List<? extends IAbilityInterface> abilities) {
        for (IAbilityInterface ability : abilities) {
            this.registerAbility(entity, source, new SlotInformation(ItemHandlerType.OTHER), ability);
        }
    }

    public void registerAbilities(EntityLivingBase entity, String source, SlotInformation info, @Nonnull List<? extends IAbilityInterface> abilities) {
        for (IAbilityInterface ability : abilities) {
            this.registerAbility(entity, source, info, ability);
        }
    }

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
    public IAbilityInterface replaceAbility(@Nonnull EntityLivingBase entity, String source, SlotInformation info, @Nonnull IAbilityInterface ability) {
        final String key = ability.getRegistryName().toString();
        if (!entity.world.isRemote) {
            if (!ability.isAbilityEnabled()) {
                if (!this.hasKillOrder(key)) {
                    this.addKillOrder(key);
                    this.sendKillOrder(entity, key);
                }
                return ability;
            } else {
                if (this.hasKillOrder(key)) {
                    this.removeKillOrder(key);
                }
            }
        } else {
            if (this.hasKillOrder(key)) {
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
            AbilityHolder holder = new AbilityHolder(source, info, ability);
            holder.getAbility().setFirstUpdate(true);
            this.active.put(key, holder);
            return null;
        } else {
            if (!value.compare(source, info, ability)) {
                value.getAbility().onAbilityRemoved(entity);
                AbilityHolder holder = new AbilityHolder(source, info, ability);
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
    public IAbilityInterface registerAbility(EntityLivingBase entity, String source, SlotInformation info, IAbilityInterface ability) {
        final String key = ability.getRegistryName().toString();
        if (!entity.world.isRemote) {
            if (!ability.isAbilityEnabled()) {
                if (!this.hasKillOrder(key)) {
                    this.addKillOrder(key);
                    this.sendKillOrder(entity, key);
                }
                return ability;
            } else {
                if (this.hasKillOrder(key)) {
                    this.removeKillOrder(key);
                }
            }
        } else {
            if (this.hasKillOrder(key)) {
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
            AbilityHolder holder = new AbilityHolder(source, info, ability);
            holder.getAbility().setFirstUpdate(true);
            this.active.put(key, holder);
            return null;
        }
        return ability;
    }

    @Nullable
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
    public AbilityHolder getAbilityHolder(String ability) {
        if (this.active.containsKey(ability)) {
            return this.active.get(ability);
        }
        return null;
    }

    @Nullable
    public IAbilityInterface getAbility(String ability) {
        AbilityHolder holder = this.getAbilityHolder(ability);
        if (holder != null) {
            return holder.getAbility();
        }
        return null;
    }

    public void onUpdatePre(EntityLivingBase entity) {
//        for (Entry<String, AbilityHolder> entry : active.entrySet()) {
//            final String key = entry.getKey();
//            if (this.removedAbilities.contains(key)) {
//                entry.getValue().getAbility().scheduleRemoval();
//                this.removedAbilities.remove(key);
//            }
//        }
        this.active.values().removeIf(cache -> cache.getAbility().shouldRemove());
    }

    public void onUpdate(EntityLivingBase entity) {
        for (Entry<String, AbilityHolder> entry : this.active.entrySet()) {
            final String key = entry.getKey();
            final AbilityHolder cache = entry.getValue();
            final String source = cache.getSourceID();
            final SlotInformation sourceInfo = cache.getInfo();
            final IAbilityInterface ability = cache.getAbility();
            if (ability.shouldRemove()) {
                ability.onAbilityRemoved(entity);
            } else {
                if (ability.isFirstUpdate()) {
                    this.loadAbilityFromEntityOnFirstUpdate(this.parentProperties.getEntity(), ability);
                    ability.onAbilityAdded(this.parentProperties.getEntity());
                }
                this.processAbility(ability, this.parentProperties.getEntity());
                if (ability.hasChanged()) {
                    this.saveInfoOnChange(this.parentProperties.getEntity(), ability);
                    this.sendNBTToPlayerOnChange(this.parentProperties.getEntity(), ability);
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

    public void onUpdatePost(EntityLivingBase entity) {
        if (this.hasChanged) {
            this.hasChanged = false;
        }
    }

    public boolean hasChanged() {
        return this.hasChanged;
    }

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

    private boolean shouldRemove(Entry<String, AbilityHolder> entry, EntityLivingBase entity) {
        final String key = entry.getKey();
        if (this.hasKillOrder(key)) {
            return true;
        }
        final AbilityHolder cache = entry.getValue();
        final String source = cache.getSourceID();
        final SlotInformation sourceInfo = cache.getInfo();
        final IAbilityInterface ability = cache.getAbility();
        switch (sourceInfo.getHandlerType()) {
            case NONE:
                return true;
            case OTHER:
                return false;
            case RACE:
                RaceCache raceCache = this.parentProperties.getCurrentRace();
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
//                final Element raceEle = Capabilities.getEntityProperties(entity, Elements.NEUTRAL, (prop, rtn) -> prop.getCurrentRace().getElement());
                boolean remove = Capabilities.getTrinketProperties(s, false, (prop, bool) -> {
                    boolean sameSource = prop.getItem().getRegistryName().toString().contentEquals(sourceInfo.getItemID());
                    if (sameSource) {
                        boolean sameInfo = sourceInfo.compare(prop.getSlotInfo());
                        return !sameInfo;
                    }
                    return !sameSource;// || (ability.getRequiredElement() != null && (raceEle != ability.getRequiredElement()));
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
    public void copyFrom(AbilityHandler source, boolean wasDeath, boolean keepInv) {

        if (wasDeath) {

        } else {
        }
        this.active = source.active;
        this.hasChanged = true;
    }


    public boolean hasKillOrder(String ability) {
        if (ability == null || ability.isEmpty()) {
            return false;
        }
        NBTTagCompound tag = this.parentProperties.getTag();
        if (tag.hasKey(capKey)) {
            NBTTagCompound playerCap = tag.getCompoundTag(capKey);
            if (playerCap.hasKey(ability)) {
                return playerCap.getCompoundTag(ability).hasKey(KILL_ORDER);
            }
        }
        return false;
    }

    public void removeKillOrder(String ability) {
        if (ability == null || ability.isEmpty()) {
            return;
        }
        NBTTagCompound tag = this.parentProperties.getTag();
        if (tag.hasKey(capKey)) {
            NBTTagCompound playerCap = tag.getCompoundTag(capKey);
            if (playerCap.hasKey(ability)) {
                NBTTagCompound playerAbilityTag = playerCap.getCompoundTag(ability);
                if (playerAbilityTag.hasKey(KILL_ORDER)) {
                    playerAbilityTag.removeTag(KILL_ORDER);
                    if (playerAbilityTag.isEmpty()) {
                        playerCap.removeTag(ability);
                    }
                    World world = this.parentProperties.getEntity().getEntityWorld();
                    if (world instanceof WorldServer && this.parentProperties.getEntity() instanceof EntityPlayerMP) {
                        NBTTagCompound syncTag = new NBTTagCompound();
                        syncTag.setString("Ability", ability);
                        syncTag.setBoolean("ENABLED", true);
                        NetworkHandler.sendToClients((WorldServer) world, this.parentProperties.getEntity().getPosition(), new AbilityCacheSyncPacket(this.parentProperties.getEntity(), syncTag));
                    }
                }
            }
        }
    }

    public void addKillOrder(String ability) {
        NBTTagCompound tag = this.parentProperties.getTag();
        if (!tag.hasKey(capKey)) {
            tag.setTag(capKey, new NBTTagCompound());
        }
        NBTTagCompound abilitiesCap = tag.getCompoundTag(capKey);
        if (!abilitiesCap.hasKey(ability)) {
            NBTTagCompound disabledTag = new NBTTagCompound();
            disabledTag.setBoolean(KILL_ORDER, true);
            abilitiesCap.setTag(ability, disabledTag);
        } else {
            NBTTagCompound playerAbilityTag = abilitiesCap.getCompoundTag(ability);
            playerAbilityTag.setBoolean(KILL_ORDER, true);
        }
    }

    public void sendKillOrder(EntityLivingBase entity, String ability) {
        if (ability == null || ability.isEmpty()) {
            return;
        }
        World world = entity.getEntityWorld();
        if (world instanceof WorldServer && entity instanceof EntityPlayerMP) {
            NBTTagCompound syncTag = new NBTTagCompound();
            syncTag.setString("Ability", ability);
            syncTag.setBoolean("DISABLED", true);
            NetworkHandler.sendToClients((WorldServer) world, entity.getPosition(), new AbilityCacheSyncPacket(entity, syncTag));
        }
    }

    private void sendNBTToPlayerOnChange(EntityLivingBase entity, IAbilityInterface ability) {
        this.sendNBTToPlayer(entity, ability);
    }

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

    private void saveInfoOnChange(EntityLivingBase entity, IAbilityInterface ability) {
        if (!entity.world.isRemote) {
            this.saveInfoToEntity(entity, ability);
        }
    }

    private void saveInfoToEntity(EntityLivingBase entity, IAbilityInterface ability) {
        final String key = ability.getRegistryName().toString();
        World world = entity.getEntityWorld();
        if (world instanceof WorldServer && entity instanceof EntityPlayerMP) {
            NBTTagCompound tag = this.parentProperties.getTag();
            if (!tag.hasKey(capKey)) {
                tag.setTag(capKey, new NBTTagCompound());
            }
            NBTTagCompound abilitiesTag = tag.getCompoundTag(capKey);
            if (!abilitiesTag.hasKey(key)) {
                if (!ability.isAbilityEnabled()) {
                    NBTTagCompound disabledTag = new NBTTagCompound();
                    disabledTag.setBoolean(KILL_ORDER, true);
                    abilitiesTag.setTag(key, disabledTag);
                    return;
                }
                NBTTagCompound abilityTag = ability.saveStorage(new NBTTagCompound());
                if (abilityTag != null && !abilityTag.isEmpty()) {
                    abilitiesTag.setTag(key, abilityTag);
                }
            }
            if (abilitiesTag.hasKey(key)) {
                NBTTagCompound abilityTag = abilitiesTag.getCompoundTag(key);
                if (!ability.isAbilityEnabled()) {
                    abilitiesTag.setBoolean(KILL_ORDER, true);
                    return;
                }
                if (abilitiesTag.hasKey(KILL_ORDER)) {
                    abilitiesTag.removeTag(KILL_ORDER);
                }
                ability.saveStorage(abilityTag);
            }
        }
    }

    public void loadAbilityFromEntityOnFirstUpdate(EntityLivingBase entity, IAbilityInterface ability) {
        if (!entity.world.isRemote) {
            this.loadAbilityFromEntity(entity, ability);
        }
    }

    public void loadAbilityFromEntity(EntityLivingBase entity, IAbilityInterface ability) {
        String key = ability.getRegistryName().toString();
        NBTTagCompound entityTag = this.parentProperties.getTag();
        if (!entityTag.hasKey(capKey)) {
            entityTag.setTag(capKey, new NBTTagCompound());
        }
        NBTTagCompound abilitiesTag = entityTag.getCompoundTag(capKey);
        if (abilitiesTag.hasKey(key)) {
            NBTTagCompound abilityTag = abilitiesTag.getCompoundTag(key);
            if (abilityTag.hasKey(KILL_ORDER)) {
                ability.scheduleRemoval();
                ability.setAbilityEnabled(false);
                return;
            }
            if (!abilityTag.isEmpty()) {
                ability.loadStorage(abilityTag);
            }
        }
    }

    public void loadAbilityFromNBT(IAbilityInterface ability, NBTTagCompound compound) {
        String key = ability.getRegistryName().toString();
        if (compound.hasKey(key)) {
            ability.loadStorage(compound.getCompoundTag(key));
        }
    }

    public NBTTagCompound saveAbilitiesToNBT(NBTTagCompound compound) {
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
                    Trinkets.LOGGER.error("Error when saving ability:" + key);
                    e.printStackTrace();
                }
            }
        }
        return compound;
    }

    public void loadAbilitiesFromNBT(NBTTagCompound compound) {
        if (compound.hasKey(capKey)) {
            final NBTTagCompound tag = compound.getCompoundTag(capKey);
            for (Entry<String, AbilityHolder> entry : this.active.entrySet()) {
                String key = entry.getKey();
                if (tag.hasKey(key)) {
                    AbilityHolder value = entry.getValue();
                    try {
                        this.loadAbilityFromNBT(value.getAbility(), tag.getCompoundTag(key));
                    } catch (final Exception e) {
                        Trinkets.LOGGER.error("Error when loading ability:" + key);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public NBTTagCompound saveToNBT(NBTTagCompound compound) {
        if (!compound.hasKey(capKey)) {
            compound.setTag(capKey, new NBTTagCompound());
        }
        NBTTagCompound tag = compound.getCompoundTag(capKey);
        return compound;
    }

    public void loadFromNBT(NBTTagCompound compound) {
        if (compound.hasKey(capKey)) {
            NBTTagCompound tag = compound.getCompoundTag(capKey);

        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Call Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    public static class AbilityHolder {
        protected String source;
        protected SlotInformation info;
        protected IAbilityInterface ability;

        public AbilityHolder(String source, SlotInformation info, IAbilityInterface ability) {
            this.source = source;
            this.info = info;
            this.ability = ability.cacheAbilityHolder(this);
        }

        public final String getSourceID() {
            return this.source;
        }

        public final SlotInformation getInfo() {
            return this.info;
        }

        public final IAbilityInterface getAbility() {
            return this.ability;
        }

        public final boolean compare(AbilityHolder other) {
            return this.compare(other.getSourceID(), other.getInfo(), other.getAbility());
        }

        public final boolean compare(String otherSource, SlotInformation otherInfo, IAbilityInterface otherAbility) {
            boolean isRaceAbility = this.getInfo().getHandlerType().compareTo(ItemHandlerType.RACE) == 0;
            boolean isOtherRaceAbility = otherInfo.getHandlerType().compareTo(ItemHandlerType.RACE) == 0;
            boolean check = isRaceAbility && !isOtherRaceAbility;
            boolean sameSource = this.getSourceID().contentEquals(otherSource);
            boolean sameElementRequired = this.getAbility().getRequiredElement() == otherAbility.getRequiredElement();
            boolean sameInfo = this.getInfo().compare(otherInfo);
            boolean isSame = (sameSource && sameElementRequired);
            return !check || isSame && sameInfo;
        }
    }
}
