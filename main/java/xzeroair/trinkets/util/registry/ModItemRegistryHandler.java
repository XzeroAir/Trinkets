package xzeroair.trinkets.util.registry;

import net.minecraft.item.Item;
import net.minecraft.potion.PotionType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

@EventBusSubscriber
public class ModItemRegistryHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {

		event.getRegistry().registerAll(ModItems.crafting.ITEMS.toArray(new Item[0]));

		if(TrinketsConfig.SERVER.Food.foods_enabled) {
			event.getRegistry().registerAll(ModItems.foods.ITEMS.toArray(new Item[0]));
		}

		if(Loader.isModLoaded("baubles")) {
			//			event.getRegistry().registerAll(ModItems.baubles.ITEMS.toArray(new Item[0]));
			for(final Item item : ModItems.baubles.ITEMS) {
				if(item instanceof IAccessoryInterface) {
					if(((IAccessoryInterface)item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
		} else {
			//			event.getRegistry().registerAll(ModItems.trinkets.ITEMS.toArray(new Item[0]));
			for(final Item item : ModItems.trinkets.ITEMS) {
				if(item instanceof IAccessoryInterface) {
					if(((IAccessoryInterface)item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<PotionType> event) {
		if(TrinketsConfig.SERVER.Potion.potions_enabled) {
			ModPotionTypes.registerPotionTypes();
		}
	}
	/**
	 * @param event
	 */
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for(final Item item : ModItems.crafting.ITEMS) {
			if(item instanceof IsModelLoaded) {
				((IsModelLoaded)item).registerModels();
			}
		}
		if(TrinketsConfig.SERVER.Food.foods_enabled) {
			for(final Item item : ModItems.foods.ITEMS) {
				if(item instanceof IsModelLoaded) {
					((IsModelLoaded)item).registerModels();
				}
			}
		}
		if(Loader.isModLoaded("baubles")) {
			for(final Item item : ModItems.baubles.ITEMS) {
				if(item instanceof IsModelLoaded) {
					((IsModelLoaded)item).registerModels();
				}
			}
		} else {
			for(final Item item : ModItems.trinkets.ITEMS) {
				if(item instanceof IsModelLoaded) {
					((IsModelLoaded)item).registerModels();
				}
			}
		}
	}
}
