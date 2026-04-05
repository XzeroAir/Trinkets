package xzeroair.trinkets.init;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.ItemBase;
import xzeroair.trinkets.items.base.RaceFood;
import xzeroair.trinkets.items.base.TrinketRaceBase;
import xzeroair.trinkets.items.baubles.*;
import xzeroair.trinkets.items.foods.Mana_Candy;
import xzeroair.trinkets.items.foods.Mana_Crystal;
import xzeroair.trinkets.items.foods.Mana_Reagent;
import xzeroair.trinkets.items.foods.Restore_Item;
import xzeroair.trinkets.items.misc.TrinketExpDevice;
import xzeroair.trinkets.items.trinkets.*;
import xzeroair.trinkets.items.trinkets.cosmetic.BaubleCosmetic;
import xzeroair.trinkets.items.trinkets.cosmetic.TrinketCosmetic;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;

import java.util.ArrayList;
import java.util.List;

// @formatter:off
public class ModItems {

	//top of mod class

	public static class Materials {

		public static final String GLOWING_NAME = TrinketsRegistryNames.ModPotions.POTION_GLOWING;
		public static final ArmorMaterial GLOWING = EnumHelper.addArmorMaterial(GLOWING_NAME,
				Reference.MODID+":" + GLOWING_NAME, 25, new int[] { 0, 0, 0, 0 }, 0,
				SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F);

	}
	//Crafting Materials
	public static class crafting {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item glowing_powder 			= new ItemBase(TrinketsRegistryNames.ModCrafting.glowing_powder).setMaxDamage(0);
		public static final Item glowing_ingot 				= new ItemBase(TrinketsRegistryNames.ModCrafting.glowing_ingot).setMaxDamage(0);
		public static final Item glowing_gem 				= new ItemBase(TrinketsRegistryNames.ModCrafting.glowing_gem).setMaxDamage(0);

		public static final Item spark_powder 				= new ItemBase(TrinketsRegistryNames.ModCrafting.spark_powder).setMaxDamage(0);

		protected static void registerItems() {
			registerItem(glowing_powder);
			registerItem(glowing_ingot);
			registerItem(glowing_gem);
			registerItem(spark_powder);
		}

		private static void registerItem(Item item) {
			ITEMS.add(item);
		}
	}

	//Foods
	public static class foods   {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item dwarf_stout	 			= new RaceFood(TrinketsRegistryNames.ModFood.FOOD_DWARF, 16, EnumAction.DRINK ,EntityRaces.dwarf);
		public static final Item elf_sap	 				= new RaceFood(TrinketsRegistryNames.ModFood.FOOD_ELF, 32, EnumAction.DRINK, EntityRaces.elf);
		public static final Item faelis_nip	 				= new RaceFood(TrinketsRegistryNames.ModFood.FOOD_FAELIS, 32, EnumAction.EAT, EntityRaces.faelis);
		public static final Item fairy_food	 				= new RaceFood(TrinketsRegistryNames.ModFood.FOOD_FAIRY, 16, EnumAction.DRINK, EntityRaces.fairy);
		public static final Item goblin_soup	 			= new RaceFood(TrinketsRegistryNames.ModFood.FOOD_GOBLIN, 32, EnumAction.DRINK, EntityRaces.goblin);
		public static final Item titan_spirit	 			= new RaceFood(TrinketsRegistryNames.ModFood.FOOD_TITAN, 32, EnumAction.DRINK, EntityRaces.titan);
		public static final Item dragon_gem	 				= new RaceFood(TrinketsRegistryNames.ModFood.FOOD_DRAGON, 32, EnumAction.EAT, EntityRaces.dragon).addElementSubType(Elements.FIRE, Elements.ICE, Elements.LIGHTNING);
		public static final Item taurus_food	 			= new RaceFood(TrinketsRegistryNames.ModFood.FOOD_TAURUS, 32, EnumAction.EAT, EntityRaces.taurus);
		public static final Item mana_candy					= new Mana_Candy(TrinketsRegistryNames.ModFood.FOOD_MANA_CANDY);
		public static final Item mana_crystal				= new Mana_Crystal(TrinketsRegistryNames.ModFood.FOOD_MANA_CRYSTAL);
		public static final Item mana_reagent				= new Mana_Reagent(TrinketsRegistryNames.ModFood.FOOD_MANA_REAGENT);
		public static final Item restore_Item				= new Restore_Item(TrinketsRegistryNames.ModFood.FOOD_RESTORATION_SERUM);

		protected static void registerItems() {
			registerItem(dwarf_stout);
			registerItem(elf_sap);
			registerItem(faelis_nip);
			registerItem(fairy_food);
			registerItem(goblin_soup);
			registerItem(titan_spirit);
			registerItem(dragon_gem);
			registerItem(taurus_food);
			registerItem(mana_candy);
			registerItem(mana_crystal);
			registerItem(mana_reagent);
			registerItem(restore_Item);
		}

		private static void registerItem(Item item) {
			ITEMS.add(item);
		}
	}

	//Items With effects
	public static class misc  {
		public static final List<Item> ITEMS = new ArrayList<>();

		protected static void registerItems() {
		}

		private static void registerItem(Item item) {
			ITEMS.add(item);
		}
	}

	//baubles
	public static class baubles {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item BaubleWeightless		= new BaubleWeightless(TrinketsRegistryNames.ModItems.WEIGHTLESS_STONE);
		public static final Item BaubleInertiaNull 		= new BaubleInertiaNull(TrinketsRegistryNames.ModItems.INERTIA_NULL_STONE);
		public static final Item BaubleGreaterInertia 	= new BaubleGreaterInertia(TrinketsRegistryNames.ModItems.GREATER_INERTIA_STONE);
		public static final Item BaubleGlowRing			= new BaubleGlowRing(TrinketsRegistryNames.ModItems.GLOW_RING);
		public static final Item BaubleSea 				= new BaubleSeaStone(TrinketsRegistryNames.ModItems.SEA_STONE);
		public static final Item BaublePolarized 		= new BaublePolarized(TrinketsRegistryNames.ModItems.POLARIZED_STONE);
		public static final Item BaubleDragonsEye 		= new BaubleDragonsEye(TrinketsRegistryNames.ModItems.DRAGONS_EYE).addElementSubType(Elements.FIRE, Elements.ICE, Elements.LIGHTNING);
		public static final Item BaubleWitherRing 		= new BaubleWitherRing(TrinketsRegistryNames.ModItems.WITHER_RING);
		public static final Item BaublePoison 			= new BaublePoisonStone(TrinketsRegistryNames.ModItems.POISON_STONE);
		public static final Item BaubleEnderTiara 		= new BaubleEnderTiara(TrinketsRegistryNames.ModItems.ENDER_TIARA);
		public static final Item BaubleDamageShield		= new BaubleDamageShield(TrinketsRegistryNames.ModItems.HONOR_SHIELD);
		public static final Item BaubleArcingOrb		= new BaubleArcingOrb(TrinketsRegistryNames.ModItems.ARCING_ORB);
		public static final Item BaubleTeddyBear		= new BaubleTeddyBear(TrinketsRegistryNames.ModItems.TEDDY_BEAR);
		public static final Item BaubleFaelisClaw		= new BaubleFaelisClaws(TrinketsRegistryNames.ModItems.FAELIS_CLAWS);
		public static final Item BaubleExpDevice		= new BaubleExpDevice(TrinketsRegistryNames.ModItems.LEVELING_DEVICE);

		// Race Rings
		public static final Item BaubleFairyRing 		= new BaubleRaceBase(TrinketsRegistryNames.ModItems.RING_FAIRY, EntityRaces.fairy, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.FAIRY_RING);
		public static final Item BaubleDwarfRing 		= new BaubleRaceBase(TrinketsRegistryNames.ModItems.RING_DWARF, EntityRaces.dwarf, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.DWARF_RING);
		public static final Item BaubleTitanRing		= new BaubleRaceBase(TrinketsRegistryNames.ModItems.RING_TITAN, EntityRaces.titan, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.TITAN_RING);
		public static final Item BaubleGoblinRing		= new BaubleRaceBase(TrinketsRegistryNames.ModItems.RING_GOBLIN, EntityRaces.goblin, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.GOBLIN_RING);
		public static final Item BaubleElfRing			= new BaubleRaceBase(TrinketsRegistryNames.ModItems.RING_ELF, EntityRaces.elf, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.ELF_RING);
		public static final Item BaubleFaelisRing		= new BaubleRaceBase(TrinketsRegistryNames.ModItems.RING_FAELIS, EntityRaces.faelis, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.FAELIS_RING);
		public static final Item BaubleDragonRing		= new BaubleRaceBase(TrinketsRegistryNames.ModItems.RING_DRAGON, EntityRaces.dragon, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.DRAGON_RING).addElementSubType(Elements.FIRE, Elements.ICE, Elements.LIGHTNING);
		public static final Item BaubleTaurusRing		= new BaubleRaceBase(TrinketsRegistryNames.ModItems.RING_TAURUS, EntityRaces.taurus, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.TAURUS_RING);

		public static final Item BaubleTraitCosmetic 	= new BaubleCosmetic(TrinketsRegistryNames.ModItems.COSMETIC);

		protected static void registerItems() {
			// Trinkets
			registerItem(BaubleWeightless);
			registerItem(BaubleInertiaNull);
			registerItem(BaubleGreaterInertia);
			registerItem(BaubleGlowRing);
			registerItem(BaubleSea);
			registerItem(BaublePolarized);
			registerItem(BaubleDragonsEye);
			registerItem(BaubleWitherRing);
			registerItem(BaublePoison);
			registerItem(BaubleEnderTiara);
			registerItem(BaubleDamageShield);
			registerItem(BaubleArcingOrb);
			registerItem(BaubleTeddyBear);
			registerItem(BaubleFaelisClaw);

			registerItem(BaubleTraitCosmetic);
//			registerItem(BaubleExpDevice);
			// Rings
			registerItem(BaubleFairyRing);
			registerItem(BaubleDwarfRing);
			registerItem(BaubleTitanRing);
			registerItem(BaubleGoblinRing);
			registerItem(BaubleElfRing);
			registerItem(BaubleFaelisRing);
			registerItem(BaubleDragonRing);
			registerItem(BaubleTaurusRing);
		}

		private static void registerItem(Item item) {
			ITEMS.add(item);
		}
	}
	//	trinkets
	public static class trinkets {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item TrinketWeightless		= new TrinketWeightless(TrinketsRegistryNames.ModItems.WEIGHTLESS_STONE);
		public static final Item TrinketInertiaNull 	= new TrinketInertiaNull(TrinketsRegistryNames.ModItems.INERTIA_NULL_STONE);
		public static final Item TrinketGreaterInertia 	= new TrinketGreaterInertia(TrinketsRegistryNames.ModItems.GREATER_INERTIA_STONE);
		public static final Item TrinketGlowRing		= new TrinketGlowRing(TrinketsRegistryNames.ModItems.GLOW_RING);
		public static final Item TrinketSea 			= new TrinketSeaStone(TrinketsRegistryNames.ModItems.SEA_STONE);
		public static final Item TrinketPolarized 		= new TrinketPolarized(TrinketsRegistryNames.ModItems.POLARIZED_STONE);
		public static final Item TrinketDragonsEye 		= new TrinketDragonsEye(TrinketsRegistryNames.ModItems.DRAGONS_EYE).addElementSubType(Elements.FIRE, Elements.ICE, Elements.LIGHTNING);
		public static final Item TrinketWitherRing 		= new TrinketWitherRing(TrinketsRegistryNames.ModItems.WITHER_RING);
		public static final Item TrinketPoison 			= new TrinketPoisonStone(TrinketsRegistryNames.ModItems.POISON_STONE);
		public static final Item TrinketEnderTiara 		= new TrinketEnderTiara(TrinketsRegistryNames.ModItems.ENDER_TIARA);
		public static final Item TrinketDamageShield	= new TrinketDamageShield(TrinketsRegistryNames.ModItems.HONOR_SHIELD);
		public static final Item TrinketArcingOrb		= new TrinketArcingOrb(TrinketsRegistryNames.ModItems.ARCING_ORB);
		public static final Item TrinketTeddyBear		= new TrinketTeddyBear(TrinketsRegistryNames.ModItems.TEDDY_BEAR);

		public static final Item TrinketFaelisClaw 		= new TrinketFaelisClaws(TrinketsRegistryNames.ModItems.FAELIS_CLAWS);

		public static final Item TrinketExpDevice		= new TrinketExpDevice(TrinketsRegistryNames.ModItems.LEVELING_DEVICE);

		public static final Item TrinketTraitCosmetic 	= new TrinketCosmetic(TrinketsRegistryNames.ModItems.COSMETIC);

		protected static void registerItems() {
			registerItem(TrinketWeightless);
			registerItem(TrinketInertiaNull);
			registerItem(TrinketGreaterInertia);
			registerItem(TrinketGlowRing);
			registerItem(TrinketSea);
			registerItem(TrinketPolarized);
			registerItem(TrinketDragonsEye);
			registerItem(TrinketWitherRing);
			registerItem(TrinketPoison);
			registerItem(TrinketEnderTiara);
			registerItem(TrinketDamageShield);
			registerItem(TrinketArcingOrb);
			registerItem(TrinketTeddyBear);
			registerItem(TrinketFaelisClaw);
//			registerItem(TrinketExpDevice);
			registerItem(TrinketTraitCosmetic);
		}

		private static void registerItem(Item item) {
			ITEMS.add(item);
		}
	}

	public static class RaceTrinkets {
		public static final List<Item> ITEMS = new ArrayList<>();
		public static final Item TrinketFairyRing 		= new TrinketRaceBase(TrinketsRegistryNames.ModItems.RING_FAIRY, EntityRaces.fairy, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.FAIRY_RING);
		public static final Item TrinketDwarfRing 		= new TrinketRaceBase(TrinketsRegistryNames.ModItems.RING_DWARF, EntityRaces.dwarf, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.DWARF_RING);
		public static final Item TrinketTitanRing		= new TrinketRaceBase(TrinketsRegistryNames.ModItems.RING_TITAN, EntityRaces.titan, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.TITAN_RING);
		public static final Item TrinketGoblinRing		= new TrinketRaceBase(TrinketsRegistryNames.ModItems.RING_GOBLIN, EntityRaces.goblin, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.GOBLIN_RING);
		public static final Item TrinketElfRing			= new TrinketRaceBase(TrinketsRegistryNames.ModItems.RING_ELF, EntityRaces.elf, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.ELF_RING);
		public static final Item TrinketFaelisRing		= new TrinketRaceBase(TrinketsRegistryNames.ModItems.RING_FAELIS, EntityRaces.faelis, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.FAELIS_RING);
		public static final Item TrinketDragonRing		= new TrinketRaceBase(TrinketsRegistryNames.ModItems.RING_DRAGON, EntityRaces.dragon, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.DRAGON_RING).addElementSubType(Elements.FIRE, Elements.ICE, Elements.LIGHTNING);
		public static final Item TrinketTaurusRing		= new TrinketRaceBase(TrinketsRegistryNames.ModItems.RING_TAURUS, EntityRaces.taurus, TrinketsConfig.SERVER.ITEMS.TRANSFORMATION.TAURUS_RING);

		protected static void registerItems() {
			registerItem(TrinketFairyRing);
			registerItem(TrinketDwarfRing);
			registerItem(TrinketTitanRing);
			registerItem(TrinketGoblinRing);
			registerItem(TrinketElfRing);
			registerItem(TrinketFaelisRing);
			registerItem(TrinketDragonRing);
			registerItem(TrinketTaurusRing);
		}

		private static void registerItem(Item item) {
			ITEMS.add(item);
		}
	}

	public static void registerItems() {
		crafting.registerItems();
		foods.registerItems();
		misc.registerItems();
		trinkets.registerItems();
		RaceTrinkets.registerItems();
		if(Trinkets.MOD_COMPAT.Baubles) {
			baubles.registerItems();
		}
	}

}
