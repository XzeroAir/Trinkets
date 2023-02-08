package xzeroair.trinkets;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

@EventBusSubscriber(modid = Reference.MODID)
public class Registries {

	private static final ResourceLocation ELEMENTS_ID = new ResourceLocation(Reference.MODID, "elements");
	private static final ResourceLocation RACES_ID = new ResourceLocation(Reference.MODID, "races");
	private static ForgeRegistry<Element> elementRegistry;
	private static ForgeRegistry<EntityRace> raceRegistry;

	@SubscribeEvent
	public static void registerNewRegistry(RegistryEvent.NewRegistry event) {
		RegistryBuilder<Element> elementReg = new RegistryBuilder<>();

		ForgeRegistry<Element> elements = (ForgeRegistry<Element>) elementReg
				.setType(Element.class)
				//				.add((AddCallback<Element>) (owner, stage, id, obj, oldObj) -> Trinkets.log.info("Registering " + obj.getName() + " in association with " + oldObj.getName()))
				//				.set((DummyFactory<Element>) key -> new Element.EmptyElement(key.toString()))
				//				.set((MissingFactory<Element>) (key, isNetwork) -> new Element.EmptyElement(key.toString()))
				.allowModification()
				.setName(ELEMENTS_ID)
				.create();
		elementRegistry = elements;
		RegistryBuilder<EntityRace> racesReg = new RegistryBuilder<>();
		ForgeRegistry<EntityRace> races = (ForgeRegistry<EntityRace>) racesReg.setType(EntityRace.class)
				.allowModification()
				.setName(RACES_ID)
				.create();
		raceRegistry = races;
		//		RegistryBuilder<EntityRace> racesReg = new RegistryBuilder<>();
		//		ForgeRegistry<EntityRace> reg2 = racesReg.setType(EntityRace.class)
		//				//				.add((AddCallback<EntityRace>) (owner, stage, id, obj, oldObj) -> Trinkets.log.info("Registering " + obj.getName() + " in association with " + oldObj.getName()))
		//				//				.set((DummyFactory<EntityRace>) key -> (new EntityRace(key.toString(), "00000000-0000-0000-0000-000000000000", 11107684, 16374701)).setRaceSize(100).setMagicAffinity(100))
		//				//				.set((MissingFactory<EntityRace>) (key, isNetwork) -> (new EntityRace(key.toString(), "00000000-0000-0000-0000-000000000000", 11107684, 16374701)).setRaceSize(100).setMagicAffinity(100))
		//				.allowModification()
		//				.setName(new ResourceLocation(Reference.MODID, "races"))
		//				.create();
	}

	public static ForgeRegistry<Element> getElementRegistry() {
		return elementRegistry;
	}

	public static ForgeRegistry<EntityRace> getRaceRegistry() {
		return raceRegistry;
	}

	@SubscribeEvent
	public static void registerItem(RegistryEvent.Register<Item> event) {
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
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		Trinkets.log.info("Registering Flower Blocks");
		event.getRegistry().registerAll(ModBlocks.Flowers.BLOCKS.toArray(new Block[0]));
		Trinkets.log.info("Registering Placeable Blocks");
		event.getRegistry().registerAll(ModBlocks.Placeables.BLOCKS.toArray(new Block[0]));
	}

	@SubscribeEvent
	public static void registerPotion(RegistryEvent.Register<PotionType> event) {
		if (TrinketsConfig.SERVER.Potion.potions_enabled) {
			Trinkets.log.info("Registering Potions");
			ModPotionTypes.registerPotionTypes();
		}
	}

	@SubscribeEvent
	public static void registerModel(ModelRegistryEvent event) {
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
