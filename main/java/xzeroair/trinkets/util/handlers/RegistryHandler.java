package xzeroair.trinkets.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

@EventBusSubscriber
public class RegistryHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
	}
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
	}
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for(Item item : ModItems.ITEMS) {
			if(item instanceof IsModelLoaded) {
				((IsModelLoaded)item).registerModels();
			}
		}

		for(Block block : ModBlocks.BLOCKS) {
			if(block instanceof IsModelLoaded) {
				((IsModelLoaded)block).registerModels();
			}
		}
	}
	@SubscribeEvent
	public static void registerEnchantments(Register<Enchantment> event) {
		//		for(Enchantment enchantment: ModEnchantments.ENCHANTMENTS) {
		//			event.getRegistry().register(enchantment);
		//		}
	}
}
