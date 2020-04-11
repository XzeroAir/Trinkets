package xzeroair.trinkets.init;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.items.potions.PotionDwarf;
import xzeroair.trinkets.items.potions.PotionFairy;
import xzeroair.trinkets.items.potions.PotionRestorative;
import xzeroair.trinkets.items.potions.PotionTitan;
import xzeroair.trinkets.util.TrinketsConfig;

public class ModPotionTypes {

	public static final Potion Base = new BasePotion("enhanced", 16777137);
	public static final Potion RESTORING = new PotionRestorative("restorative");
	public static final Potion Fairy = new PotionFairy("fairy");
	public static final Potion Dwarf = new PotionDwarf("dwarf");
	public static final Potion Titan = new PotionTitan("titan");

	public static final PotionType Enhanced = new PotionType(
			"enhanced", new PotionEffect[] {
					new PotionEffect(Base, 1)
			}
	).setRegistryName("enhanced");

	public static final PotionType Restorative = new PotionType(
			"restorative", new PotionEffect[] {
					new PotionEffect(RESTORING, 1)
			}
	).setRegistryName("restorative");

	public static final PotionType FairyType = new PotionType(
			"fairy", new PotionEffect[] {
					new PotionEffect(Fairy, TrinketsConfig.SERVER.Potion.FairyDuration)
			}
	).setRegistryName("fairy");

	public static final PotionType ExtendedFairyType = new PotionType(
			"fairy", new PotionEffect[] {
					new PotionEffect(Fairy, TrinketsConfig.SERVER.Potion.FairyDuration * 3)
			}
	).setRegistryName("extended_fairy");

	public static final PotionType DwarfType = new PotionType(
			"dwarf", new PotionEffect[] {
					new PotionEffect(Dwarf, TrinketsConfig.SERVER.Potion.FairyDuration)
			}
	).setRegistryName("dwarf");

	public static final PotionType ExtendedDwarfType = new PotionType(
			"dwarf", new PotionEffect[] {
					new PotionEffect(Dwarf, TrinketsConfig.SERVER.Potion.FairyDuration * 3)
			}
	).setRegistryName("extended_dwarf");

	public static final PotionType TitanType = new PotionType(
			"titan", new PotionEffect[] {
					new PotionEffect(Titan, TrinketsConfig.SERVER.Potion.TitanDuration)
			}
	).setRegistryName("titan");

	public static final PotionType ExtendedTitanType = new PotionType(
			"titan", new PotionEffect[] {
					new PotionEffect(Titan, TrinketsConfig.SERVER.Potion.TitanDuration * 3)
			}
	).setRegistryName("extended_titan");

	public static void registerPotionTypes() {

		final Item Catalyst = Item.getByNameOrId(TrinketsConfig.SERVER.Potion.catalyst);
		final Item fairyCatalyst = Item.getByNameOrId(TrinketsConfig.SERVER.Potion.fairy_catalyst);
		final Item dwarfCatalyst = Item.getByNameOrId(TrinketsConfig.SERVER.Potion.dwarf_catalyst);
		final Item titanCatalyst = Item.getByNameOrId(TrinketsConfig.SERVER.Potion.titan_catalyst);

		ForgeRegistries.POTIONS.registerAll(Base, RESTORING, Fairy, Dwarf, Titan);
		ForgeRegistries.POTION_TYPES.registerAll(Enhanced, Restorative, FairyType, DwarfType, TitanType, ExtendedFairyType, ExtendedDwarfType, ExtendedTitanType);
		PotionHelper.addMix(PotionTypes.MUNDANE, ModItems.crafting.glowing_powder, Enhanced);
		PotionHelper.addMix(PotionTypes.AWKWARD, ModItems.crafting.glowing_powder, Enhanced);
		PotionHelper.addMix(PotionTypes.THICK, ModItems.crafting.glowing_powder, Enhanced);
		PotionHelper.addMix(Enhanced, Catalyst, Restorative);
		PotionHelper.addMix(Enhanced, fairyCatalyst, FairyType);
		PotionHelper.addMix(Enhanced, dwarfCatalyst, DwarfType);
		PotionHelper.addMix(Enhanced, titanCatalyst, TitanType);
		PotionHelper.addMix(FairyType, Items.REDSTONE, ExtendedFairyType);
		PotionHelper.addMix(DwarfType, Items.REDSTONE, ExtendedDwarfType);
		PotionHelper.addMix(TitanType, Items.REDSTONE, ExtendedTitanType);
	}
}
