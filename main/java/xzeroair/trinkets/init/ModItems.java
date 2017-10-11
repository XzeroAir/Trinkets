package xzeroair.trinkets.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.items.Itemglowing_ingot;
import xzeroair.trinkets.util.Reference;

public class ModItems {
	//top of mod class
	
	
	public static Item glowing_ingot;
	
	
	//init
	
	public static void init() {
		
		glowing_ingot = new Itemglowing_ingot();
		
	}
//	
//	@SubscribeEvent
//	public void registerItems(RegistryEvent.Register<Item> event) {
//		event.getRegistry().registerAll(glowingingot);
//	}
	public static void register() {
		
		ForgeRegistries.ITEMS.register(glowing_ingot);
		
	}
	public static void registerRenders() {
		
		registerRender(glowing_ingot);
		
	}
	private static void registerRender(Item item) {
		
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		
	}

}
