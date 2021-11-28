package xzeroair.trinkets.capabilities;

import java.util.concurrent.Callable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerStorage;
import xzeroair.trinkets.capabilities.TileEntityCap.ManaEssenceProperties;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.container.TrinketContainerHandler;

public class Capabilities {

	public static void init() {
		registerNewCap(ITrinketContainerHandler.class, TrinketContainerStorage.storage, new TrinketContainerFactory());

		// RACE
		registerNewCap(EntityProperties.class);

		// MAGIC
		registerNewCap(MagicStats.class);

		// STATUS EFFECTS
		registerNewCap(StatusHandler.class);

		// VIP STATUS COSMETICS
		registerNewCap(VipStatus.class);

		// ITEMs
		registerNewCap(TrinketProperties.class);

		// TEs
		registerNewCap(ManaEssenceProperties.class);
	}

	@CapabilityInject(EntityProperties.class)
	public static Capability<EntityProperties> ENTITY_RACE;

	@CapabilityInject(MagicStats.class)
	public static Capability<MagicStats> ENTITY_MAGIC;

	@CapabilityInject(StatusHandler.class)
	public static Capability<StatusHandler> STATUS_HANDLER;

	@CapabilityInject(TrinketProperties.class)
	public static Capability<TrinketProperties> ITEM_TRINKET;

	@CapabilityInject(VipStatus.class)
	public static Capability<VipStatus> VIP_STATUS;

	@CapabilityInject(ManaEssenceProperties.class)
	public static Capability<ManaEssenceProperties> TILE_ENTITY_MANA_ESSENCE;

	public static ManaEssenceProperties getTileEntityManaProperties(TileEntity te) {
		//		if(te == null) {
		//			return null;
		//		}
		return te.getCapability(TILE_ENTITY_MANA_ESSENCE, null);
	}

	public static StatusHandler getStatusHandler(Entity entity) {
		if (entity == null) {
			return null;
		}
		return entity.getCapability(STATUS_HANDLER, null);
	}

	public static EntityProperties getEntityRace(Entity entity) {
		if (entity == null) {
			return null;
		}
		return entity.getCapability(ENTITY_RACE, null);
	}

	public static TrinketProperties getTrinketProperties(ItemStack stack) {
		if ((stack == null) || stack.isEmpty()) {
			return null;
		}
		return stack.getCapability(ITEM_TRINKET, null);
	}

	public static VipStatus getVipStatus(Entity entity) {
		if (entity == null) {
			return null;
		}
		return entity.getCapability(VIP_STATUS, null);
	}

	public static MagicStats getMagicStats(Entity entity) {
		if (entity == null) {
			return null;
		}
		return entity.getCapability(ENTITY_MAGIC, null);
	}

	private static class TrinketContainerFactory implements Callable<ITrinketContainerHandler> {
		@Override
		public ITrinketContainerHandler call() throws Exception {
			return new TrinketContainerHandler();
		}
	}

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
