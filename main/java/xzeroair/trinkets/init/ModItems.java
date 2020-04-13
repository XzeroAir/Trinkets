package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import xzeroair.trinkets.items.GemGlowing;
import xzeroair.trinkets.items.IngotGlowing;
import xzeroair.trinkets.items.PowderGlowing;
import xzeroair.trinkets.items.baubles.BaubleArcingOrb;
import xzeroair.trinkets.items.baubles.BaubleDamageShield;
import xzeroair.trinkets.items.baubles.BaubleDragonsEye;
import xzeroair.trinkets.items.baubles.BaubleDwarfRing;
import xzeroair.trinkets.items.baubles.BaubleEnderTiara;
import xzeroair.trinkets.items.baubles.BaubleFairyRing;
import xzeroair.trinkets.items.baubles.BaubleGlowRing;
import xzeroair.trinkets.items.baubles.BaubleGreaterInertia;
import xzeroair.trinkets.items.baubles.BaubleInertiaNull;
import xzeroair.trinkets.items.baubles.BaublePoison;
import xzeroair.trinkets.items.baubles.BaublePolarized;
import xzeroair.trinkets.items.baubles.BaubleSea;
import xzeroair.trinkets.items.baubles.BaubleTitanRing;
import xzeroair.trinkets.items.baubles.BaubleWeightless;
import xzeroair.trinkets.items.baubles.BaubleWitherRing;
import xzeroair.trinkets.items.foods.Dwarf_Stout;
import xzeroair.trinkets.items.foods.Fairy_Food;
import xzeroair.trinkets.items.foods.Titan_Spirit;
import xzeroair.trinkets.items.trinkets.TrinketArcingOrb;
import xzeroair.trinkets.items.trinkets.TrinketDamageShield;
import xzeroair.trinkets.items.trinkets.TrinketDragonsEye;
import xzeroair.trinkets.items.trinkets.TrinketDwarfRing;
import xzeroair.trinkets.items.trinkets.TrinketEnderTiara;
import xzeroair.trinkets.items.trinkets.TrinketFairyRing;
import xzeroair.trinkets.items.trinkets.TrinketGlowRing;
import xzeroair.trinkets.items.trinkets.TrinketGreaterInertia;
import xzeroair.trinkets.items.trinkets.TrinketInertiaNull;
import xzeroair.trinkets.items.trinkets.TrinketPoison;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.items.trinkets.TrinketSea;
import xzeroair.trinkets.items.trinkets.TrinketTitanRing;
import xzeroair.trinkets.items.trinkets.TrinketWeightless;
import xzeroair.trinkets.items.trinkets.TrinketWitherRing;

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
	public static final String LightningBoots	= "lightning_boots";
	public static final String ArcingOrb		= "arcing_orb";
	public static final String TitanRing		= "titan_ring";

	//top of mod class

	//Crafting Materials
	public static class crafting {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item glowing_ingot 				= new IngotGlowing("glowing_ingot");
		public static final Item glowing_powder 			= new PowderGlowing("glowing_powder");
		public static final Item glowing_gem 				= new GemGlowing("glowing_gem");

	}

	//Foods
	public static class foods {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item dwarf_stout 				= new Dwarf_Stout("dwarf_stout");
		public static final Item fairy_food 				= new Fairy_Food("fairy_dew");
		public static final Item titan_spirit				= new Titan_Spirit("titan_spirit");

	}

	//Items With effects

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
		public static final Item BaubleFairyRing 		= new BaubleFairyRing(FairyRing);
		public static final Item BaubleDwarfRing 		= new BaubleDwarfRing(DwarfRing);
		public static final Item BaubleWitherRing 		= new BaubleWitherRing(WitherRing);
		public static final Item BaublePoison 			= new BaublePoison(Poison);
		public static final Item BaubleEnderTiara 		= new BaubleEnderTiara(EnderTiara);
		public static final Item BaubleDamageShield		= new BaubleDamageShield(DamageShield);
		public static final Item BaubleLightningBoots	= new BaubleArcingOrb(ArcingOrb);
		public static final Item BaubleTitanRing		= new BaubleTitanRing(TitanRing);

	}
	//	trinkets
	public static class trinkets {
		public static final List<Item> ITEMS = new ArrayList<>();

		public static final Item TrinketWeightless		= new TrinketWeightless(Weightless);
		public static final Item TrinketInertiaNull 	= new TrinketInertiaNull(InertiaNull);
		public static final Item TrinketGreaterInertia 	= new TrinketGreaterInertia(GreaterInertia);
		public static final Item TrinketGlowRing		= new TrinketGlowRing(GlowRing);
		public static final Item TrinketSea 			= new TrinketSea(Sea);
		public static final Item TrinketPolarized 		= new TrinketPolarized(Polarized);
		public static final Item TrinketDragonsEye 		= new TrinketDragonsEye(DragonsEye);
		public static final Item TrinketFairyRing 		= new TrinketFairyRing(FairyRing);
		public static final Item TrinketDwarfRing 		= new TrinketDwarfRing(DwarfRing);
		public static final Item TrinketWitherRing 		= new TrinketWitherRing(WitherRing);
		public static final Item TrinketPoison 			= new TrinketPoison(Poison);
		public static final Item TrinketEnderTiara 		= new TrinketEnderTiara(EnderTiara);
		public static final Item TrinketDamageShield	= new TrinketDamageShield(DamageShield);
		public static final Item TrinketLightningBoots	= new TrinketArcingOrb(LightningBoots);
		public static final Item TrinketTitanRing		= new TrinketTitanRing(TitanRing);
	}

}
