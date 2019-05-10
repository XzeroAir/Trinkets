package xzeroair.trinkets.capabilities;

import java.util.concurrent.Callable;

import net.minecraftforge.common.capabilities.CapabilityManager;
import xzeroair.trinkets.capabilities.ItemCap.DefaultItemCapability;
import xzeroair.trinkets.capabilities.ItemCap.IItemCap;
import xzeroair.trinkets.capabilities.ItemCap.ItemStorage;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapStorage;
import xzeroair.trinkets.capabilities.sizeCap.SizeDefaultCap;

public class Capabilities {

	public static void init() {
		CapabilityManager.INSTANCE.register(ISizeCap.class, new SizeCapStorage(), new CababilityFactory());
		CapabilityManager.INSTANCE.register(IItemCap.class, new ItemStorage(), new ItemCapabilityFactory());
	}

	private static class CababilityFactory implements Callable<ISizeCap> {
		@Override
		public ISizeCap call() throws Exception {
			return new SizeDefaultCap();
		}
	}
	private static class ItemCapabilityFactory implements Callable<IItemCap> {
		@Override
		public IItemCap call() throws Exception {
			return new DefaultItemCapability();
		}
	}
}
