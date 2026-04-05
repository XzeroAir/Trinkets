package xzeroair.trinkets.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerStorage;
import xzeroair.trinkets.capabilities.TileEntityCap.TileEntityProperties;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.util.Reference;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Capabilities {

    public static final String ITEM_TRINKET_CAP_TAG = Reference.MODID + ":Trinket";
    public static final String ELEMENT_CAP_TAG = Reference.MODID + ":Elements";

    public static void init() {
        registerNewCap(ITrinketContainerHandler.class, TrinketContainerStorage.storage, new TrinketContainerFactory());

        // RACE
//        registerNewCap(EntityElementalAttributes.class, new CapabilityStorage<EntityElementalAttributes>());
        registerNewCap(EntityProperties.class, new CapabilityStorage<EntityProperties>());

        // MAGIC
        registerNewCap(MagicStats.class, new CapabilityStorage<MagicStats>());

        // STATUS EFFECTS
        registerNewCap(StatusHandler.class, new CapabilityStorage<StatusHandler>());

        // VIP STATUS COSMETICS
        registerNewCap(VipStatus.class, new CapabilityStorage<VipStatus>());

        // ITEMs
        registerNewCap(TrinketProperties.class, new CapabilityStorage<TrinketProperties>());
//        registerNewCap(ItemElementalAttributes.class, new CapabilityStorage<ItemElementalAttributes>());

        // TEs
        registerNewCap(TileEntityProperties.class, new CapabilityStorage<TileEntityProperties>());
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @CapabilityInject(TileEntityProperties.class)
    public static Capability<TileEntityProperties> TILE_ENTITY_PROPERTIES;

    @Nullable
    public static TileEntityProperties getTEProperties(TileEntity tileEntity) {
        return getCapabilityWithConsumer(tileEntity, TILE_ENTITY_PROPERTIES, null);
    }

    @Nullable
    public static TileEntityProperties getTEProperties(TileEntity tileEntity, Consumer<TileEntityProperties> consumer) {
        return getCapabilityWithConsumer(tileEntity, TILE_ENTITY_PROPERTIES, consumer);
    }

    public static <R> R getTEProperties(TileEntity tileEntity, R ret, BiFunction<TileEntityProperties, R, R> func) {
        return getCapabilityWithReturn(tileEntity, TILE_ENTITY_PROPERTIES, ret, func);
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @CapabilityInject(StatusHandler.class)
    public static Capability<StatusHandler> STATUS_HANDLER;

    @Nullable
    public static StatusHandler getStatusHandler(Entity entity) {
        return getCapabilityWithConsumer(entity, STATUS_HANDLER, null);
    }

    @Nullable
    public static StatusHandler getStatusHandler(Entity entity, Consumer<StatusHandler> consumer) {
        return getCapabilityWithConsumer(entity, STATUS_HANDLER, consumer);
    }

    public static <R> R getStatusHandler(Entity entity, R ret, BiFunction<StatusHandler, R, R> func) {
        return getCapabilityWithReturn(entity, STATUS_HANDLER, ret, func);
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//    @CapabilityInject(EntityElementalAttributes.class)
//    public static Capability<EntityElementalAttributes> ENTITY_ELEMENTAL_ATTRIBUTES;
//
//
//    public static EntityElementalAttributes getElementProperties(Entity entity) {
//        return getCapabilityWithConsumer(entity, ENTITY_ELEMENTAL_ATTRIBUTES, null);
//    }
//
//    public static EntityElementalAttributes getElementProperties(Entity entity, Consumer<EntityElementalAttributes> consumer) {
//        return getCapabilityWithConsumer(entity, ENTITY_ELEMENTAL_ATTRIBUTES, consumer);
//    }
//
//    public static <R> R getElementProperties(Entity entity, R ret, BiFunction<EntityElementalAttributes, R, R> func) {
//        return getCapabilityWithReturn(entity, ENTITY_ELEMENTAL_ATTRIBUTES, ret, func);
//    }

    @CapabilityInject(EntityProperties.class)
    public static Capability<EntityProperties> ENTITY_PROPERTIES;

    @Nullable
    public static EntityProperties getEntityProperties(Entity entity) {
        return getCapabilityWithConsumer(entity, ENTITY_PROPERTIES, null);
    }

    @Nullable
    public static EntityProperties getEntityProperties(Entity entity, Consumer<EntityProperties> consumer) {
        return getCapabilityWithConsumer(entity, ENTITY_PROPERTIES, consumer);
    }

    public static <R> R getEntityProperties(Entity entity, R ret, BiFunction<EntityProperties, R, R> func) {
        return getCapabilityWithReturn(entity, ENTITY_PROPERTIES, ret, func);
    }

    @Nullable
    @Deprecated
    public static EntityProperties getEntityRace(Entity entity) {
        return getEntityProperties(entity);
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @CapabilityInject(TrinketProperties.class)
    public static Capability<TrinketProperties> ITEM_TRINKET;

    @Nullable
    public static TrinketProperties getTrinketProperties(ItemStack stack) {
        return getCapabilityWithConsumer(stack, ITEM_TRINKET, null);
    }

    @Nullable
    public static TrinketProperties getTrinketProperties(ItemStack stack, Consumer<TrinketProperties> consumer) {
        return getCapabilityWithConsumer(stack, ITEM_TRINKET, consumer);
    }

    public static <R> R getTrinketProperties(ItemStack stack, R ret, BiFunction<TrinketProperties, R, R> func) {
        return getCapabilityWithReturn(stack, ITEM_TRINKET, ret, func);
    }

//    @CapabilityInject(ItemElementalAttributes.class)
//    public static Capability<ItemElementalAttributes> ITEM_ELEMENTAL_ATTRIBUTES;
//
//
//    public static ItemElementalAttributes getElementProperties(ItemStack stack) {
//        return getCapabilityWithConsumer(stack, ITEM_ELEMENTAL_ATTRIBUTES, null);
//    }
//
//    public static ItemElementalAttributes getElementProperties(ItemStack stack, Consumer<ItemElementalAttributes> consumer) {
//        return getCapabilityWithConsumer(stack, ITEM_ELEMENTAL_ATTRIBUTES, consumer);
//    }
//
//    public static <R> R getElementProperties(ItemStack stack, R ret, BiFunction<ItemElementalAttributes, R, R> func) {
//        return getCapabilityWithReturn(stack, ITEM_ELEMENTAL_ATTRIBUTES, ret, func);
//    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @CapabilityInject(VipStatus.class)
    public static Capability<VipStatus> VIP_STATUS;

    @Nullable
    public static VipStatus getVipStatus(Entity entity) {
        return getCapabilityWithConsumer(entity, VIP_STATUS, null);
    }

    @Nullable
    public static VipStatus getVipStatus(Entity entity, Consumer<VipStatus> consumer) {
        return getCapabilityWithConsumer(entity, VIP_STATUS, consumer);
    }

    public static <R> R getVipStatus(Entity entity, R ret, BiFunction<VipStatus, R, R> func) {
        return getCapabilityWithReturn(entity, VIP_STATUS, ret, func);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @CapabilityInject(MagicStats.class)
    public static Capability<MagicStats> ENTITY_MAGIC;

    @Nullable
    public static MagicStats getMagicStats(Entity entity) {
        return getCapabilityWithConsumer(entity, ENTITY_MAGIC, null);
    }

    @Nullable
    public static MagicStats getMagicStats(Entity entity, Consumer<MagicStats> consumer) {
        return getCapabilityWithConsumer(entity, ENTITY_MAGIC, consumer);
    }

    public static <R> R getMagicStats(Entity entity, R ret, BiFunction<MagicStats, R, R> func) {
        return getCapabilityWithReturn(entity, ENTITY_MAGIC, ret, func);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Nullable
    public static <C> C getCapability(ICapabilitySerializable object, @Nullable Capability<C> capability) {
        if (object != null && capability != null) {
            return object.getCapability(capability, null);
        } else {
            return null;
        }
    }

    public static <C> C getCapabilityWithConsumer(ICapabilitySerializable object, @Nullable Capability<C> capability, @Nullable Consumer<C> consumer) {
        final C cap = getCapability(object, capability);
        if (cap != null) {
            if (consumer != null) {
                try {
                    consumer.accept(cap);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
            return cap;
        }
        return null;
    }

    public static <C, R> R getCapabilityWithReturn(ICapabilitySerializable object, @Nullable Capability<C> capability, R ret, BiFunction<C, R, R> func) {
        final C cap = getCapability(object, capability);
        if (cap != null) {
            if (func != null) {
                try {
                    return func.apply(cap, ret);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;//func == null ? null : func.apply(cap);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Nullable
    public static ITrinketContainerHandler getTrinketContainer(Entity entity) {
        return getCapabilityWithConsumer(entity, TrinketContainerProvider.containerCap, null);
    }

    @Nullable
    public static ITrinketContainerHandler getTrinketContainer(Entity entity, Consumer<ITrinketContainerHandler> consumer) {
        return getCapabilityWithConsumer(entity, TrinketContainerProvider.containerCap, consumer);
    }

    public static <R> R getTrinketContainer(Entity entity, R ret, BiFunction<ITrinketContainerHandler, R, R> func) {
        return getCapabilityWithReturn(entity, TrinketContainerProvider.containerCap, ret, func);
    }

    private static class TrinketContainerFactory implements Callable<ITrinketContainerHandler> {
        @Override
        public ITrinketContainerHandler call() throws Exception {
            return new TrinketContainerHandler();
        }
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static <T> void registerNewCap(Class<T> type) {
        registerNewCap(type, new Capability.IStorage<T>() {
            @Override
            public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
                throw new UnsupportedOperationException();
            }
        });
    }

    private static <T> void registerNewCap(Class<T> type, Capability.IStorage<T> storage) {
        registerNewCap(type, storage, () -> null);
    }

    private static <T> void registerNewCap(Class<T> type, Capability.IStorage<T> storage, Callable<? extends T> factory) {
        CapabilityManager.INSTANCE.register(type, storage, factory);
    }
}
