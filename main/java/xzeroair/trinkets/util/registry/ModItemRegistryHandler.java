<<<<<<< Updated upstream
package xzeroair.trinkets.util.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

@EventBusSubscriber(modid = Reference.MODID)
public class ModItemRegistryHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		Trinkets.log.info("Registering Crafting");
		event.getRegistry().registerAll(ModItems.crafting.ITEMS.toArray(new Item[0]));

		if (TrinketsConfig.SERVER.Food.foods_enabled) {
			Trinkets.log.info("Registering Foods");
			event.getRegistry().registerAll(ModItems.foods.ITEMS.toArray(new Item[0]));
		}

		Trinkets.log.info("Registering Misc");
		event.getRegistry().registerAll(ModItems.misc.ITEMS.toArray(new Item[0]));

		if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
			Trinkets.log.info("Registering Baubles");
			for (final Item item : ModItems.baubles.ITEMS) {
				if (item instanceof IAccessoryInterface) {
					if (((IAccessoryInterface) item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
		} else {
			Trinkets.log.info("Registering Trinkets");
			for (final Item item : ModItems.trinkets.ITEMS) {
				if (item instanceof IAccessoryInterface) {
					if (((IAccessoryInterface) item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
			Trinkets.log.info("Registering Race Trinkets");
			for (final Item item : ModItems.RaceTrinkets.ITEMS) {
				if (item instanceof IAccessoryInterface) {
					if (((IAccessoryInterface) item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		Trinkets.log.info("Registering Flower Blocks");
		event.getRegistry().registerAll(ModBlocks.Flowers.BLOCKS.toArray(new Block[0]));
		Trinkets.log.info("Registering Placeable Blocks");
		event.getRegistry().registerAll(ModBlocks.Placeables.BLOCKS.toArray(new Block[0]));
	}

	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<PotionType> event) {
		if (TrinketsConfig.SERVER.Potion.potions_enabled) {
			Trinkets.log.info("Registering Potions");
			ModPotionTypes.registerPotionTypes();
		}
	}

	//	@SubscribeEvent
	//	public static void colorHandlerEvent(ColorHandlerEvent.Item event) {
	//		//				event.getItemColors().registerItemColorHandler(new IItemColor() {
	//		//
	//		//					@Override
	//		//					public int colorMultiplier(ItemStack stack, int tintIndex) {
	//		//						return 0;
	//		//					}
	//		//				}, itemsIn);
	//	}
	//
	//	@SubscribeEvent
	//	public static void colorHandlerEvent(ColorHandlerEvent.Block event) {
	//
	//	}

	/**
	 * @param event
	 */
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		//		final ModelResourceLocation normal = new ModelResourceLocation(new ResourceLocation(Reference.MODID, "trait_tuarian"), "inventory");
		//		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeddyBear.class, new TETeddyBearRenderer());
		//		ModelBakery.registerItemVariants(ModItems.misc.ViewerItem, normal);
		//		ModelLoader.setCustomMeshDefinition(ModItems.misc.ViewerItem, stack -> {
		//			final String name = stack.getDisplayName().toLowerCase();
		//			return normal;
		//		});
		for (final Item item : ModItems.misc.ITEMS) {
			if (item instanceof IsModelLoaded) {
				((IsModelLoaded) item).registerModels();
			}
		}
		for (final Item item : ModItems.crafting.ITEMS) {
			if (item instanceof IsModelLoaded) {
				((IsModelLoaded) item).registerModels();
			}
		}
		for (final Block block : ModBlocks.Flowers.BLOCKS) {
			if (block instanceof IsModelLoaded) {
				((IsModelLoaded) block).registerModels();
			}
		}
		for (final Block block : ModBlocks.Placeables.BLOCKS) {
			if (block instanceof IsModelLoaded) {
				((IsModelLoaded) block).registerModels();
			}
		}
		if (TrinketsConfig.SERVER.Food.foods_enabled) {
			for (final Item item : ModItems.foods.ITEMS) {
				if (item instanceof IsModelLoaded) {
					((IsModelLoaded) item).registerModels();
				}
			}
		}
		if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
			for (final Item item : ModItems.baubles.ITEMS) {
				if (item instanceof IsModelLoaded) {
					((IsModelLoaded) item).registerModels();
				}
			}
		} else {
			for (final Item item : ModItems.trinkets.ITEMS) {
				if (item instanceof IsModelLoaded) {
					((IsModelLoaded) item).registerModels();
				}
			}
			for (final Item item : ModItems.RaceTrinkets.ITEMS) {
				if (item instanceof IsModelLoaded) {
					((IsModelLoaded) item).registerModels();
				}
			}
		}
	}
}
=======
package xzeroair.trinkets.util.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

@EventBusSubscriber(modid = Reference.MODID)
public class ModItemRegistryHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		Trinkets.log.info("Registering Crafting");
		event.getRegistry().registerAll(ModItems.crafting.ITEMS.toArray(new Item[0]));

		if (TrinketsConfig.SERVER.Food.foods_enabled) {
			Trinkets.log.info("Registering Foods");
			event.getRegistry().registerAll(ModItems.foods.ITEMS.toArray(new Item[0]));
		}

		Trinkets.log.info("Registering Misc");
		event.getRegistry().registerAll(ModItems.misc.ITEMS.toArray(new Item[0]));

		if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
			Trinkets.log.info("Registering Baubles");
			for (final Item item : ModItems.baubles.ITEMS) {
				if (item instanceof IAccessoryInterface) {
					if (((IAccessoryInterface) item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
		} else {
			Trinkets.log.info("Registering Trinkets");
			for (final Item item : ModItems.trinkets.ITEMS) {
				if (item instanceof IAccessoryInterface) {
					if (((IAccessoryInterface) item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
			Trinkets.log.info("Registering Race Trinkets");
			for (final Item item : ModItems.RaceTrinkets.ITEMS) {
				if (item instanceof IAccessoryInterface) {
					if (((IAccessoryInterface) item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		Trinkets.log.info("Registering Flower Blocks");
		event.getRegistry().registerAll(ModBlocks.Flowers.BLOCKS.toArray(new Block[0]));
		Trinkets.log.info("Registering Placeable Blocks");
		event.getRegistry().registerAll(ModBlocks.Placeables.BLOCKS.toArray(new Block[0]));
	}

	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<PotionType> event) {
		if (TrinketsConfig.SERVER.Potion.potions_enabled) {
			Trinkets.log.info("Registering Potions");
			ModPotionTypes.registerPotionTypes();
		}
	}

	//	@SubscribeEvent
	//	public static void colorHandlerEvent(ColorHandlerEvent.Item event) {
	//		//				event.getItemColors().registerItemColorHandler(new IItemColor() {
	//		//
	//		//					@Override
	//		//					public int colorMultiplier(ItemStack stack, int tintIndex) {
	//		//						return 0;
	//		//					}
	//		//				}, itemsIn);
	//	}
	//
	//	@SubscribeEvent
	//	public static void colorHandlerEvent(ColorHandlerEvent.Block event) {
	//
	//	}

	/**
	 * @param event
	 */
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		//		final ModelResourceLocation normal = new ModelResourceLocation(new ResourceLocation(Reference.MODID, "trait_tuarian"), "inventory");
		//		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeddyBear.class, new TETeddyBearRenderer());
		//		ModelBakery.registerItemVariants(ModItems.misc.ViewerItem, normal);
		//		ModelLoader.setCustomMeshDefinition(ModItems.misc.ViewerItem, stack -> {
		//			final String name = stack.getDisplayName().toLowerCase();
		//			return normal;
		//		});
		for (final Item item : ModItems.misc.ITEMS) {
			if (item instanceof IsModelLoaded) {
				((IsModelLoaded) item).registerModels();
			}
		}
		for (final Item item : ModItems.crafting.ITEMS) {
			if (item instanceof IsModelLoaded) {
				((IsModelLoaded) item).registerModels();
			}
		}
		for (final Block block : ModBlocks.Flowers.BLOCKS) {
			if (block instanceof IsModelLoaded) {
				((IsModelLoaded) block).registerModels();
			}
		}
		for (final Block block : ModBlocks.Placeables.BLOCKS) {
			if (block instanceof IsModelLoaded) {
				((IsModelLoaded) block).registerModels();
			}
		}
		if (TrinketsConfig.SERVER.Food.foods_enabled) {
			for (final Item item : ModItems.foods.ITEMS) {
				if (item instanceof IsModelLoaded) {
					((IsModelLoaded) item).registerModels();
				}
			}
		}
		if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
			for (final Item item : ModItems.baubles.ITEMS) {
				if (item instanceof IsModelLoaded) {
					((IsModelLoaded) item).registerModels();
				}
			}
		} else {
			for (final Item item : ModItems.trinkets.ITEMS) {
				if (item instanceof IsModelLoaded) {
					((IsModelLoaded) item).registerModels();
				}
			}
			for (final Item item : ModItems.RaceTrinkets.ITEMS) {
				if (item instanceof IsModelLoaded) {
					((IsModelLoaded) item).registerModels();
				}
			}
		}
	}
}
>>>>>>> Stashed changes
