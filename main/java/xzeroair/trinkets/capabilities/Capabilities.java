package xzeroair.trinkets.capabilities;

import java.util.concurrent.Callable;

import net.minecraftforge.common.capabilities.CapabilityManager;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.IContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerStorage;
import xzeroair.trinkets.capabilities.TrinketCap.DefaultTrinketCapability;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketStorage;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapStorage;
import xzeroair.trinkets.capabilities.sizeCap.SizeDefaultCap;
import xzeroair.trinkets.container.TrinketContainerHandler;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class Capabilities {

	public static void init() {
		CapabilityManager.INSTANCE.register(ISizeCap.class, new SizeCapStorage(), new CababilityFactory());
		//		CapabilityManager.INSTANCE.register(IItemCap.class, new ItemStorage(), new ItemCapabilityFactory());
		CapabilityManager.INSTANCE.register(IAccessoryInterface.class, new TrinketStorage(), new ItemCapabilityFactory());
		CapabilityManager.INSTANCE.register(IContainerHandler.class, TrinketContainerStorage.storage, new TrinketContainerFactory());
	}

	//	@CapabilityInject(IAccessoryInterface.class)
	//	public static final Capability<IAccessoryInterface> CAPABILITY_ITEM_TRINKET = null;
	//
	//	public static class CapabilityItemTrinketStorage implements IStorage<IAccessoryInterface> {
	//
	//		@Override
	//		public NBTBase writeNBT (Capability<IAccessoryInterface> capability, IAccessoryInterface instance, EnumFacing side) {
	//			return null;
	//		}
	//
	//		@Override
	//		public void readNBT (Capability<IAccessoryInterface> capability, IAccessoryInterface instance, EnumFacing side, NBTBase nbt) {
	//
	//		}
	//	}

	private static class TrinketContainerFactory implements Callable<IContainerHandler> {
		@Override
		public IContainerHandler call() throws Exception {
			return new TrinketContainerHandler();
		}
	}

	private static class CababilityFactory implements Callable<ISizeCap> {
		@Override
		public ISizeCap call() throws Exception {
			return new SizeDefaultCap();
		}
	}
	private static class ItemCapabilityFactory implements Callable<IAccessoryInterface> {
		@Override
		public IAccessoryInterface call() throws Exception {
			return new DefaultTrinketCapability();
		}
	}
	//	private static class ItemCapabilityFactory implements Callable<IItemCap> {
	//		@Override
	//		public IItemCap call() throws Exception {
	//			return new DefaultItemCapability();
	//		}
	//	}
}
