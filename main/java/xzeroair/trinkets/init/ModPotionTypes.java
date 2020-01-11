package xzeroair.trinkets.init;

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
import xzeroair.trinkets.util.TrinketsConfig;

public class ModPotionTypes {

	public static final Potion Base = new BasePotion("enhanced", 16777137);
	public static final Potion RESTORING = new PotionRestorative("restorative");
	public static final Potion Fairy = new PotionFairy("fairy");
	public static final Potion Dwarf = new PotionDwarf("dwarf");

	public static final PotionType Enhanced = new PotionType("enhanced", new PotionEffect[]{
			new PotionEffect(Base, 0)
	}).setRegistryName("enhanced");

	public static final PotionType Restorative = new PotionType("restorative", new PotionEffect[]{
			new PotionEffect(RESTORING, 0)
	}).setRegistryName("restorative");

	public static final PotionType FairyType = new PotionType("fairy", new PotionEffect[]{
			new PotionEffect(Fairy, TrinketsConfig.SERVER.Potion.FairyDuration)
	}).setRegistryName("fairy");

	public static final PotionType DwarfType = new PotionType("dwarf", new PotionEffect[]{
			new PotionEffect(Dwarf, TrinketsConfig.SERVER.Potion.FairyDuration)
	}).setRegistryName("dwarf");

	public static void registerPotionTypes() {

		final Item Catalyst = Item.getByNameOrId(TrinketsConfig.SERVER.Potion.catalyst);
		final Item fairyCatalyst = Item.getByNameOrId(TrinketsConfig.SERVER.Potion.fairy_catalyst);
		final Item dwarfCatalyst = Item.getByNameOrId(TrinketsConfig.SERVER.Potion.dwarf_catalyst);

		ForgeRegistries.POTIONS.registerAll(Base, RESTORING, Fairy, Dwarf);
		ForgeRegistries.POTION_TYPES.registerAll(Enhanced, Restorative, FairyType, DwarfType);
		PotionHelper.addMix(PotionTypes.MUNDANE	, 	ModItems.crafting.glowing_powder, Enhanced);
		PotionHelper.addMix(PotionTypes.AWKWARD	,	ModItems.crafting.glowing_powder, Enhanced);
		PotionHelper.addMix(PotionTypes.THICK	,	ModItems.crafting.glowing_powder, Enhanced);
		PotionHelper.addMix(Enhanced ,	Catalyst, Restorative);
		PotionHelper.addMix(Enhanced ,	fairyCatalyst, FairyType);
		PotionHelper.addMix(Enhanced ,	dwarfCatalyst, DwarfType);
	}
}
