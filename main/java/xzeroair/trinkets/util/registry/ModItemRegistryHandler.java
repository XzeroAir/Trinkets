package xzeroair.trinkets.util.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

@EventBusSubscriber
public class ModItemRegistryHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {

		event.getRegistry().registerAll(ModItems.crafting.ITEMS.toArray(new Item[0]));

		if (TrinketsConfig.SERVER.Food.foods_enabled) {
			event.getRegistry().registerAll(ModItems.foods.ITEMS.toArray(new Item[0]));
		}

		event.getRegistry().registerAll(ModItems.misc.ITEMS.toArray(new Item[0]));

		//TODO Setup a getConfig in EntityRace maybe? or Learn Json
		//		for(EntityRace.Registry.) {
		//		EntityRace.Registry.forEach(race -> {
		//			Item item = new TrinketRaceBase(race.getName().toLowerCase() + "_ring", race, TrinketsConfig.SERVER.Items.raceRings.ELF_RING, TrinketsConfig.SERVER.races.elf.Attributes);
		//			event.getRegistry().register(item);
		//		});
		//		}

		if (Loader.isModLoaded("baubles")) {
			for (final Item item : ModItems.baubles.ITEMS) {
				if (item instanceof IAccessoryInterface) {
					if (((IAccessoryInterface) item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
		} else {
			for (final Item item : ModItems.trinkets.ITEMS) {
				if (item instanceof IAccessoryInterface) {
					if (((IAccessoryInterface) item).ItemEnabled()) {
						event.getRegistry().register(item);
					}
				}
			}
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
		event.getRegistry().registerAll(ModBlocks.Flowers.BLOCKS.toArray(new Block[0]));
		GameRegistry.registerTileEntity(TileEntityMoonRose.class, new ResourceLocation(Reference.MODID + ":moon_rose"));
		//		for (Block block : ModBlocks.Flowers.BLOCKS) {
		//			event.getRegistry().register(block);
		//		}
	}

	@SubscribeEvent
	public static void onPotionRegister(RegistryEvent.Register<PotionType> event) {
		if (TrinketsConfig.SERVER.Potion.potions_enabled) {
			ModPotionTypes.registerPotionTypes();
		}
	}

	/**
	 * @param event
	 */
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
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
		for (Block block : ModBlocks.Flowers.BLOCKS) {
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
		if (Loader.isModLoaded("baubles")) {
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
