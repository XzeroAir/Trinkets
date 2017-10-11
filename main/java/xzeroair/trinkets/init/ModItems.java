package xzeroair.trinkets.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xzeroair.trinkets.items.Itemglow_ring;
import xzeroair.trinkets.items.Itemglowing_ingot;

public class ModItems {
	//top of mod class
	
	public static Item glowing_ingot;
    public static Item glow_ring;
	
	
	//init
	public static void init() {
		
		glowing_ingot = new Itemglowing_ingot();
		glow_ring = new Itemglow_ring();
		
	}
	public static void register() {
		
		ForgeRegistries.ITEMS.register(glowing_ingot);
		ForgeRegistries.ITEMS.register(glow_ring);
		
	}
	public static void registerRenders() {
		
		registerRender(glowing_ingot);
		registerRender(glow_ring);
		
	}
	public static void registerRender(Item item) {
		
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
		
	}

}
