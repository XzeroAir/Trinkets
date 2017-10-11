package xzeroair.trinkets.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import xzeroair.trinkets.init.ModItems;

@EventBusSubscriber
public class ClientProxy implements CommonProxy {
		

	@Override
	public void init() {
		ModItems.registerRenders();
	}
}
