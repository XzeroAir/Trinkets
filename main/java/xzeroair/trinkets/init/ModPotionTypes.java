package xzeroair.trinkets.init;

import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.items.potions.IceResistance;
import xzeroair.trinkets.items.potions.LightningResistance;
import xzeroair.trinkets.items.potions.PotionObject;
import xzeroair.trinkets.items.potions.TransformationPotion;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.StringUtils;

public class ModPotionTypes {

	public static final String baseSparkling = "sparkling";
	public static final String enhancedGlittering = "glittering";
	public static final String advancedGlowing = "glowing";
	public static final String restore = "restorative";
	public static final String iceResist = "ice_resistance";
	public static final String lightningResist = "lightning_resistance";

	public static HashMap<String, Potion> TrinketPotions = new HashMap();//new ArrayList<>();
	public static HashMap<String, PotionType> TrinketPotionTypes = new HashMap();//new ArrayList<>();
	public static HashMap<String, PotionObject> TrinketPotionObjects = new HashMap();//new ArrayList<>();
	public static HashMap<String, PotionObject> TrinketRacePotionObjects = new HashMap();//new ArrayList<>();

	/*
	 * Base Potions
	 */

	protected static PotionObject createBasePotion(String potionName, int color, int duration, Ingredient craftingIngredient) {
		return createBasePotion(potionName, color, duration, false, craftingIngredient);
	}

	protected static PotionObject createBasePotion(String potionName, int color, int duration, boolean isBadEffect, Ingredient craftingIngredient) {
		return createBasePotion(potionName, color, duration, duration * 3, isBadEffect, craftingIngredient);
	}

	protected static PotionObject createBasePotion(String potionName, int color, int duration, int extendedDuration, Ingredient craftingIngredient) {
		return createBasePotion(potionName, color, duration, extendedDuration, false, craftingIngredient);
	}

	protected static PotionObject createBasePotion(String potionName, int color, int duration, int extendedDuration, boolean isBadEffect, Ingredient craftingIngredient) {
		return createBasePotion(Reference.MODID, potionName, color, duration, extendedDuration, isBadEffect, craftingIngredient);
	}

	public static PotionObject createBasePotion(String modid, String potionName, int color, int duration, boolean isBadEffect, Ingredient craftingIngredient) {
		final Potion potion = new BasePotion(potionName, color, duration, isBadEffect);
		return getBasePotionObject(potion, modid, potionName, color, duration, craftingIngredient);
	}

	public static PotionObject createBasePotion(String modid, String potionName, int color, int duration, int extendedDuration, boolean isBadEffect, Ingredient craftingIngredient) {
		final Potion potion = new BasePotion(potionName, color, duration, isBadEffect);
		return getBasePotionObject(potion, modid, potionName, color, duration, extendedDuration, craftingIngredient);
	}

	public static PotionObject getBasePotionObject(Potion potionProduct, String modid, String potionName, int color, int duration, Ingredient craftingIngredient) {
		return getBasePotionObject(potionProduct, modid, potionName, color, duration, duration * 3, craftingIngredient);
	}

	public static PotionObject getBasePotionObject(Potion potionProduct, String modid, String potionName, int color, int duration, int extendedDuration, Ingredient craftingIngredient) {
		final PotionObject obj = new PotionObject(potionProduct, modid, potionName, color, duration, extendedDuration, craftingIngredient);
		final boolean isInternal = modid.contentEquals(Reference.MODID);
		if (isInternal) {
			TrinketPotionObjects.put(obj.getName(), obj);
		}
		return obj;
	}

	/*
	 *
	 */
	protected static PotionObject createCompoundPotion(PotionType craftingBase, String potionName, int color, int duration, Ingredient craftingIngredient) {
		final Potion potion = new BasePotion(potionName, color, duration, false);
		return createCompoundPotion(potion, craftingBase, Reference.MODID, potionName, color, duration, duration * 3, craftingIngredient);
	}

	protected static PotionObject createCompoundPotion(PotionType craftingBase, String potionName, int color, int duration, int extendedDuration, Ingredient craftingIngredient) {
		final Potion potion = new BasePotion(potionName, color, duration, false);
		return createCompoundPotion(potion, craftingBase, Reference.MODID, potionName, color, duration, extendedDuration, craftingIngredient);
	}

	protected static PotionObject createCompoundPotion(Potion potionProduct, PotionType craftingBase, String potionName, int color, int duration, Ingredient craftingIngredient) {
		return createCompoundPotion(potionProduct, craftingBase, Reference.MODID, potionName, color, duration, duration * 3, craftingIngredient);
	}

	protected static PotionObject createCompoundPotion(Potion potionProduct, PotionType craftingBase, String potionName, int color, int duration, int extendedDuration, Ingredient craftingIngredient) {
		return createCompoundPotion(potionProduct, craftingBase, Reference.MODID, potionName, color, duration, extendedDuration, craftingIngredient);
	}

	public static PotionObject createCompoundPotion(Potion potionProduct, PotionType craftingBase, String modid, String potionName, int color, int duration, Ingredient craftingIngredient) {
		return createCompoundPotion(potionProduct, craftingBase, modid, potionName, color, duration, duration * 3, craftingIngredient);
	}

	public static PotionObject createCompoundPotion(Potion potionProduct, PotionType craftingBase, String modid, String potionName, int color, int duration, int extendedDuration, Ingredient craftingIngredient) {
		final PotionObject obj = new PotionObject(potionProduct, craftingBase, modid, potionName, color, duration, extendedDuration, craftingIngredient);
		final boolean isInternal = modid.contentEquals(Reference.MODID);
		if (isInternal) {
			TrinketPotionObjects.put(obj.getName(), obj);
		}
		return obj;
	}

	/*
	 *
	 */
	public static PotionObject createRacePotion(EntityRace race, PotionType craftingBase, int duration, Ingredient craftingIngredient) {
		return createRacePotion(race, craftingBase, duration, duration * 3, craftingIngredient);
	}

	public static PotionObject createRacePotion(EntityRace race, PotionType craftingBase, int duration, int extendedDuration, Ingredient craftingIngredient) {
		return createRacePotion(race, craftingBase, duration, extendedDuration, false, craftingIngredient);
	}

	public static PotionObject createRacePotion(EntityRace race, PotionType craftingBase, int duration, int extendedDuration, boolean isBadEffect, Ingredient craftingIngredient) {
		final String modid = race.getRegistryName().getNamespace().toString().toLowerCase();
		final String name = race.getRegistryName().getPath().toString().toLowerCase();
		final int color = race.getPrimaryColor();
		final Potion potion = new TransformationPotion(modid, name, color, duration, race.getUUID().toString(), false);
		final PotionObject obj = createCompoundPotion(potion, craftingBase, modid, name, color, duration, extendedDuration, craftingIngredient);
		TrinketRacePotionObjects.put(obj.getName(), obj);
		return obj;
	}

	public static void registerPotionTypes() {

		Trinkets.log.info("Generating Potions");

		createBasePotion(
				baseSparkling,
				16777160,
				0,
				Ingredient.fromItem(ModItems.crafting.glowing_powder)
		).registerWithPotion();
		createCompoundPotion(
				TrinketPotionObjects.get(baseSparkling).getPotionType(),
				enhancedGlittering,
				16777120,
				0,
				Ingredient.fromItem(ModItems.crafting.glowing_ingot)
		).registerWithPotion();
		createCompoundPotion(
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				advancedGlowing,
				16777080,
				0,
				Ingredient.fromItem(ModItems.crafting.glowing_gem)
		).registerWithPotion();
		createCompoundPotion(
				new IceResistance(iceResist, 3600, 15132390, false),
				TrinketPotionObjects.get(baseSparkling).getPotionType(),
				iceResist,
				15132390,
				3600,
				9600,
				Ingredient.fromItem(Item.getItemFromBlock(Blocks.SNOW))
		).registerWithPotion();
		createCompoundPotion(
				new LightningResistance(lightningResist, 3600, 15132390, false),
				TrinketPotionObjects.get(baseSparkling).getPotionType(),
				lightningResist,
				15132390,
				3600,
				9600,
				Ingredient.fromItem(ModItems.foods.mana_candy)
		).registerWithPotionWithoutRecipe();

		/*
		 * Create Race Potions
		 */
		createRacePotion(
				EntityRaces.human,
				TrinketPotionObjects.get(baseSparkling).getPotionType(),
				TrinketsConfig.SERVER.Potion.human.Duration,
				getCatalyst(TrinketsConfig.SERVER.Potion.human.catalyst)
		).registerWithPotion();
		createRacePotion(
				EntityRaces.fairy,
				TrinketPotionObjects.get(advancedGlowing).getPotionType(),
				TrinketsConfig.SERVER.Potion.fairy.Duration,
				getCatalyst(TrinketsConfig.SERVER.Potion.fairy.catalyst)
		).registerWithPotion();
		createRacePotion(
				EntityRaces.dwarf,
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				TrinketsConfig.SERVER.Potion.dwarf.Duration,
				getCatalyst(TrinketsConfig.SERVER.Potion.dwarf.catalyst)
		).registerWithPotion();
		createRacePotion(
				EntityRaces.titan,
				TrinketPotionObjects.get(advancedGlowing).getPotionType(),
				TrinketsConfig.SERVER.Potion.titan.Duration,
				getCatalyst(TrinketsConfig.SERVER.Potion.titan.catalyst)
		).registerWithPotion();
		createRacePotion(
				EntityRaces.goblin,
				TrinketPotionObjects.get(baseSparkling).getPotionType(),
				TrinketsConfig.SERVER.Potion.goblin.Duration,
				getCatalyst(TrinketsConfig.SERVER.Potion.goblin.catalyst)
		).registerWithPotion();
		createRacePotion(
				EntityRaces.elf,
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				TrinketsConfig.SERVER.Potion.elf.Duration,
				getCatalyst(TrinketsConfig.SERVER.Potion.elf.catalyst)
		).registerWithPotion();
		createRacePotion(
				EntityRaces.faelis,
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				TrinketsConfig.SERVER.Potion.faelis.Duration,
				getCatalyst(TrinketsConfig.SERVER.Potion.faelis.catalyst)
		).registerWithPotion();
		createRacePotion(
				EntityRaces.dragon,
				TrinketPotionObjects.get(advancedGlowing).getPotionType(),
				TrinketsConfig.SERVER.Potion.dragon.Duration,
				getCatalyst(TrinketsConfig.SERVER.Potion.dragon.catalyst)
		).registerWithPotion();

		Trinkets.log.info("Finished Generating Potions");
	}

	private static Ingredient getCatalyst(String catalyst) {
		final String[] itemConfig = catalyst.replace(";", ":").split(":");
		final String modIDString = StringUtils.getStringFromArray(itemConfig, 0);
		final String itemIDString = StringUtils.getStringFromArray(itemConfig, 1);
		final String metaString = StringUtils.getStringFromArray(itemConfig, 2).replaceAll("[^\\d]", "");
		//		return Item.getByNameOrId(modIDString + ":" + itemIDString);
		int meta = OreDictionary.WILDCARD_VALUE;
		try {
			if (!metaString.isEmpty()) {
				meta = Integer.parseInt(metaString);
			}
		} catch (final Exception e) {
			Trinkets.log.warn("Invalid catalyst meta from | " + catalyst);
		}
		return Ingredient.fromStacks(new ItemStack(Item.getByNameOrId(modIDString + ":" + itemIDString), 1, meta));
	}
}
