package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import xzeroair.trinkets.items.ViewerItem;
import xzeroair.trinkets.items.base.BaseArrow;
import xzeroair.trinkets.items.base.BaseBow;
import xzeroair.trinkets.items.base.ItemBase;
import xzeroair.trinkets.items.base.RaceFood;
import xzeroair.trinkets.items.base.TrinketRaceBase;
import xzeroair.trinkets.items.baubles.BaubleArcingOrb;
import xzeroair.trinkets.items.baubles.BaubleDamageShield;
import xzeroair.trinkets.items.baubles.BaubleDragonsEye;
import xzeroair.trinkets.items.baubles.BaubleEnderTiara;
import xzeroair.trinkets.items.baubles.BaubleFaelisClaws;
import xzeroair.trinkets.items.baubles.BaubleGlowRing;
import xzeroair.trinkets.items.baubles.BaubleGreaterInertia;
import xzeroair.trinkets.items.baubles.BaubleInertiaNull;
import xzeroair.trinkets.items.baubles.BaublePoison;
import xzeroair.trinkets.items.baubles.BaublePolarized;
import xzeroair.trinkets.items.baubles.BaubleRaceBase;
import xzeroair.trinkets.items.baubles.BaubleSea;
import xzeroair.trinkets.items.baubles.BaubleTeddyBear;
import xzeroair.trinkets.items.baubles.BaubleWeightless;
import xzeroair.trinkets.items.baubles.BaubleWitherRing;
import xzeroair.trinkets.items.foods.Mana_Candy;
import xzeroair.trinkets.items.foods.Mana_Crystal;
import xzeroair.trinkets.items.foods.Mana_Reagent;
import xzeroair.trinkets.items.foods.Restore_Item;
import xzeroair.trinkets.items.trinkets.TrinketArcingOrb;
import xzeroair.trinkets.items.trinkets.TrinketDamageShield;
import xzeroair.trinkets.items.trinkets.TrinketDragonsEye;
import xzeroair.trinkets.items.trinkets.TrinketEnderTiara;
import xzeroair.trinkets.items.trinkets.TrinketFaelisClaws;
import xzeroair.trinkets.items.trinkets.TrinketGlowRing;
import xzeroair.trinkets.items.trinkets.TrinketGreaterInertia;
import xzeroair.trinkets.items.trinkets.TrinketInertiaNull;
import xzeroair.trinkets.items.trinkets.TrinketPoisonStone;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.items.trinkets.TrinketRibbonBow;
import xzeroair.trinkets.items.trinkets.TrinketSeaStone;
import xzeroair.trinkets.items.trinkets.TrinketTeddyBear;
import xzeroair.trinkets.items.trinkets.TrinketWeightless;
import xzeroair.trinkets.items.trinkets.TrinketWitherRing;
import xzeroair.trinkets.util.TrinketsConfig;

// @formatter:off
public class ModItems {
	public static final String Weightless		= "weightless_stone";
	public static final String InertiaNull 		= "inertia_null_stone";
	public static final String GreaterInertia 	= "greater_inertia_stone";
	public static final String GlowRing			= "glow_ring";
	public static final String Sea				= "sea_stone";
	public static final String Polarized 		= "polarized_stone";
	public static final String DragonsEye 		= "dragons_eye";
	public static final String FairyRing 		= "fairy_ring";
	public static final String DwarfRing 		= "dwarf_ring";
	public static final String WitherRing 		= "wither_ring";
	public static final String Poison 			= "poison_stone";
	public static final String EnderTiara 		= "ender_tiara";
	public static final String DamageShield		= "damage_shield";
	public static final String ArcingOrb		= "arcing_orb";
	public static final String TitanRing		= "titan_ring";
	public static final String TeddyBear		= "teddy_bear";

	public static final String GoblinRing		= "goblin_ring";
	public static final String ElfRing			= "elf_ring";
	public static final String SuccubusRing		= "succubus_ring";
	public static final String FaelisRing		= "faelis_ring";

	public static final String DragonRing		= "dragon_ring";
	public static final String DragonShield		= "dragon_shield";

	public static final String Horn				= "horn_of_plenty";

	public static final String GlitterBow		= "glitter_bow";
	public static final String RibbonBow		= "ribbon_bow";
	public static final String FaelisClaws		= "faelis_claw";


	//top of mod class

	//Crafting Materials
	public static class crafting {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item glowing_powder 			= new ItemBase("glowing_powder").setMaxDamage(0);
		public static final Item glowing_ingot 				= new ItemBase("glowing_ingot").setMaxDamage(0);
		public static final Item glowing_gem 				= new ItemBase("glowing_gem").setMaxDamage(0);

	}

	//Foods
	public static class foods {
		public static final List<Item> ITEMS = new ArrayList<>();


		public static final Item dwarf_stout	 			= new RaceFood("dwarf_stout", 16, EnumAction.DRINK ,EntityRaces.dwarf);
		public static final Item elf_sap	 				= new RaceFood("elf_sap", 32, EnumAction.DRINK, EntityRaces.elf);
		public static final Item faelis_nip	 				= new RaceFood("faelis_food", 32, EnumAction.EAT, EntityRaces.faelis);
		public static final Item fairy_food	 				= new RaceFood("fairy_dew", 16, EnumAction.DRINK, EntityRaces.fairy);
		public static final Item goblin_soup	 			= new RaceFood("goblin_soup", 32, EnumAction.DRINK, EntityRaces.goblin);
		public static final Item titan_spirit	 			= new RaceFood("titan_spirit", 32, EnumAction.DRINK, EntityRaces.titan);
		public static final Item dragon_gem	 				= new RaceFood("dragon_gem", 32, EnumAction.EAT, EntityRaces.dragon);
		public static final Item mana_candy					= new Mana_Candy("mana_candy");
		public static final Item mana_crystal				= new Mana_Crystal("mana_crystal");
		public static final Item mana_reagent				= new Mana_Reagent("mana_reagent");
		public static final Item restore_Item				= new Restore_Item("restoration_serum");

	}

	//Items With effects
	public static class misc {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item Bow = new BaseBow("trinket_bow");
		public static final Item BowArrow = new BaseArrow("trinket_arrow");
		public static final Item RibbonBow = new TrinketRibbonBow("ribbon_bow");
		public static final Item ViewerItem = new ViewerItem("viewer_item");
//		public static final Item GlitterBow = new TrinketGlitterBow();


	}

	//baubles
	public static class baubles {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item BaubleWeightless		= new BaubleWeightless(Weightless);
		public static final Item BaubleInertiaNull 		= new BaubleInertiaNull(InertiaNull);
		public static final Item baubleGreaterInertia 	= new BaubleGreaterInertia(GreaterInertia);
		public static final Item BaubleGlowRing			= new BaubleGlowRing(GlowRing);
		public static final Item BaubleSea 				= new BaubleSea(Sea);
		public static final Item BaublePolarized 		= new BaublePolarized(Polarized);
		public static final Item BaubleDragonsEye 		= new BaubleDragonsEye(DragonsEye);
		public static final Item BaubleWitherRing 		= new BaubleWitherRing(WitherRing);
		public static final Item BaublePoison 			= new BaublePoison(Poison);
		public static final Item BaubleEnderTiara 		= new BaubleEnderTiara(EnderTiara);
		public static final Item BaubleDamageShield		= new BaubleDamageShield(DamageShield);
		public static final Item BaubleArcingOrb		= new BaubleArcingOrb(ArcingOrb);
		public static final Item BaubleTeddyBear		= new BaubleTeddyBear(TeddyBear);

		public static final Item BaubleFaelisClaw		= new BaubleFaelisClaws(FaelisClaws);

		// Race Rings
		public static final Item BaubleFairyRing 		= new BaubleRaceBase(FairyRing, EntityRaces.fairy, TrinketsConfig.SERVER.Items.raceRings.FAIRY_RING, TrinketsConfig.SERVER.races.fairy.Attributes);// new TrinketFairyRing(FairyRing);
		public static final Item BaubleDwarfRing 		= new BaubleRaceBase(DwarfRing, EntityRaces.dwarf, TrinketsConfig.SERVER.Items.raceRings.DWARF_RING, TrinketsConfig.SERVER.races.dwarf.Attributes);//new TrinketDwarfRing(DwarfRing);
		public static final Item BaubleTitanRing		= new BaubleRaceBase(TitanRing, EntityRaces.titan, TrinketsConfig.SERVER.Items.raceRings.TITAN_RING, TrinketsConfig.SERVER.races.titan.Attributes);//new TrinketTitanRing(TitanRing);
		public static final Item BaubleGoblinRing		= new BaubleRaceBase(GoblinRing, EntityRaces.goblin, TrinketsConfig.SERVER.Items.raceRings.GOBLIN_RING, TrinketsConfig.SERVER.races.goblin.Attributes);//new TrinketGoblinRing(GoblinRing);
		public static final Item BaubleElfRing			= new BaubleRaceBase(ElfRing, EntityRaces.elf, TrinketsConfig.SERVER.Items.raceRings.ELF_RING, TrinketsConfig.SERVER.races.elf.Attributes);//new TrinketElfRing(ElfRing);

		public static final Item BaubleFaelisRing		= new BaubleRaceBase(FaelisRing, EntityRaces.faelis, TrinketsConfig.SERVER.Items.raceRings.FAELIS_RING, TrinketsConfig.SERVER.races.faelis.Attributes);//new TrinketElfRing(ElfRing);

		public static final Item BaubleDragonRing		= new BaubleRaceBase(DragonRing, EntityRaces.dragon, TrinketsConfig.SERVER.Items.raceRings.DRAGON_RING, TrinketsConfig.SERVER.races.dragon.Attributes);//new TrinketElfRing(ElfRing);

	}
	//	trinkets
	public static class trinkets {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item TrinketWeightless		= new TrinketWeightless(Weightless);
		public static final Item TrinketInertiaNull 	= new TrinketInertiaNull(InertiaNull);
		public static final Item TrinketGreaterInertia 	= new TrinketGreaterInertia(GreaterInertia);
		public static final Item TrinketGlowRing		= new TrinketGlowRing(GlowRing);
		public static final Item TrinketSea 			= new TrinketSeaStone(Sea);
		public static final Item TrinketPolarized 		= new TrinketPolarized(Polarized);
		public static final Item TrinketDragonsEye 		= new TrinketDragonsEye(DragonsEye);
		public static final Item TrinketWitherRing 		= new TrinketWitherRing(WitherRing);
		public static final Item TrinketPoison 			= new TrinketPoisonStone(Poison);
		public static final Item TrinketEnderTiara 		= new TrinketEnderTiara(EnderTiara);
		public static final Item TrinketDamageShield	= new TrinketDamageShield(DamageShield);
		public static final Item TrinketArcingOrb		= new TrinketArcingOrb(ArcingOrb);
		public static final Item TrinketTeddyBear		= new TrinketTeddyBear(TeddyBear);

		public static final Item TrinketFaelisClaw 		= new TrinketFaelisClaws(FaelisClaws);

	}

	public static class RaceTrinkets {
		public static final List<Item> ITEMS = new ArrayList<>();
		public static final Item TrinketFairyRing 		= new TrinketRaceBase(FairyRing, EntityRaces.fairy, TrinketsConfig.SERVER.Items.raceRings.FAIRY_RING, TrinketsConfig.SERVER.races.fairy.Attributes);// new TrinketFairyRing(FairyRing);
		public static final Item TrinketDwarfRing 		= new TrinketRaceBase(DwarfRing, EntityRaces.dwarf, TrinketsConfig.SERVER.Items.raceRings.DWARF_RING, TrinketsConfig.SERVER.races.dwarf.Attributes);//new TrinketDwarfRing(DwarfRing);
		public static final Item TrinketTitanRing		= new TrinketRaceBase(TitanRing, EntityRaces.titan, TrinketsConfig.SERVER.Items.raceRings.TITAN_RING, TrinketsConfig.SERVER.races.titan.Attributes);//new TrinketTitanRing(TitanRing);
		public static final Item TrinketGoblinRing		= new TrinketRaceBase(GoblinRing, EntityRaces.goblin, TrinketsConfig.SERVER.Items.raceRings.GOBLIN_RING, TrinketsConfig.SERVER.races.goblin.Attributes);//new TrinketGoblinRing(GoblinRing);
		public static final Item TrinketElfRing			= new TrinketRaceBase(ElfRing, EntityRaces.elf, TrinketsConfig.SERVER.Items.raceRings.ELF_RING, TrinketsConfig.SERVER.races.elf.Attributes);//new TrinketElfRing(ElfRing);
		public static final Item TrinketFaelisRing		= new TrinketRaceBase(FaelisRing, EntityRaces.faelis, TrinketsConfig.SERVER.Items.raceRings.FAELIS_RING, TrinketsConfig.SERVER.races.faelis.Attributes);//new TrinketElfRing(ElfRing);
		public static final Item TrinketDragonRing		= new TrinketRaceBase(DragonRing, EntityRaces.dragon, TrinketsConfig.SERVER.Items.raceRings.DRAGON_RING, TrinketsConfig.SERVER.races.dragon.Attributes);//new TrinketElfRing(ElfRing);

	}

}
