package xzeroair.trinkets.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

//@EventBusSubscriber
public class ClientProxy extends CommonProxy {
		

//	@Override
//	public void init() {
//		ModItems.registerRenders();
//	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
		
	}
}
