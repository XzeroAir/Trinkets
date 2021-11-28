package xzeroair.trinkets.init;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.items.potions.PotionObject;
import xzeroair.trinkets.items.potions.TransformationPotion;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.CallHelper;

public class ModPotionTypes {

	public static final String baseSparkling = "sparkling";
	public static final String enhancedGlittering = "glittering";
	public static final String advancedGlowing = "glowing";
	public static final String restore = "restorative";

	public static HashMap<String, Potion> TrinketPotions = new HashMap();//new ArrayList<>();
	public static HashMap<String, PotionType> TrinketPotionTypes = new HashMap();//new ArrayList<>();
	public static HashMap<String, PotionObject> TrinketPotionObjects = new HashMap();//new ArrayList<>();

	public static PotionObject createBasePotion(String name, int color, int X, int Y, int duration, Ingredient ingredient) {
		final Potion potion = new BasePotion(name, duration, color, X, Y);
		final PotionObject potObj = new PotionObject(potion, name, color, duration, ingredient);
		TrinketPotionObjects.put(potObj.getName(), potObj);
		return potObj;
	}

	public static PotionObject createPotion(String name, int color, int X, int Y, int duration, PotionType base, Ingredient ingredient) {
		final Potion potion = new BasePotion(name, duration, color, X, Y);
		final PotionObject potObj = new PotionObject(potion, base, name, color, duration, ingredient);
		TrinketPotionObjects.put(potObj.getName(), potObj);
		return potObj;
	}

	public static PotionObject createRacePotion(EntityRace race, int X, int Y, int duration, PotionType base, Ingredient ingredient) {
		final Potion potion = new TransformationPotion(race.getName().toLowerCase(), race.getPrimaryColor(), race.getUUID().toString(), X, Y);
		final PotionObject potObj = new PotionObject(potion, base, race.getName().toLowerCase(), race.getPrimaryColor(), duration, ingredient);
		TrinketPotionObjects.put(potObj.getName(), potObj);
		return potObj;
	}

	public static void registerPotionTypes() {

		Trinkets.log.info("Generating Potions");
		PotionObject pot = createBasePotion(baseSparkling, 16777160, 0, 0, 0, Ingredient.fromItem(ModItems.crafting.glowing_powder)).registerPotion();
		pot = createPotion(enhancedGlittering, 16777120, 0, 0, 0, TrinketPotionObjects.get(baseSparkling).getPotionType(), Ingredient.fromItem(ModItems.crafting.glowing_ingot)).registerPotion();
		pot = createPotion(advancedGlowing, 16777080, 0, 0, 0, TrinketPotionObjects.get(enhancedGlittering).getPotionType(), Ingredient.fromItem(ModItems.crafting.glowing_gem)).registerPotion();
		//		pot = createPotion(restore, 16777215, 0, 0, 0, TrinketPotionObjects.get(baseSparkling).getPotionType(), Item.getByNameOrId(TrinketsConfig.SERVER.Potion.restoreCatalyst)).registerPotion();

		pot = createRacePotion(
				EntityRaces.human, 0, 0,
				TrinketsConfig.SERVER.Potion.human.Duration,
				TrinketPotionObjects.get(baseSparkling).getPotionType(),
				getCatalyst(TrinketsConfig.SERVER.Potion.human.catalyst)
		)
				.registerPotion();

		pot = createRacePotion(
				EntityRaces.fairy, 1, 0,
				TrinketsConfig.SERVER.Potion.fairy.Duration,
				TrinketPotionObjects.get(advancedGlowing).getPotionType(),
				getCatalyst(TrinketsConfig.SERVER.Potion.fairy.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.dwarf, 2, 0,
				TrinketsConfig.SERVER.Potion.dwarf.Duration,
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				getCatalyst(TrinketsConfig.SERVER.Potion.dwarf.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.titan, 3, 0,
				TrinketsConfig.SERVER.Potion.titan.Duration,
				TrinketPotionObjects.get(advancedGlowing).getPotionType(),
				getCatalyst(TrinketsConfig.SERVER.Potion.titan.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.goblin, 4, 0,
				TrinketsConfig.SERVER.Potion.goblin.Duration,
				TrinketPotionObjects.get(baseSparkling).getPotionType(),
				getCatalyst(TrinketsConfig.SERVER.Potion.goblin.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.elf, 5, 0,
				TrinketsConfig.SERVER.Potion.elf.Duration,
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				getCatalyst(TrinketsConfig.SERVER.Potion.elf.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.faelis, 6, 0,
				TrinketsConfig.SERVER.Potion.faelis.Duration,
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				getCatalyst(TrinketsConfig.SERVER.Potion.faelis.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.dragon, 7, 0,
				TrinketsConfig.SERVER.Potion.dragon.Duration,
				TrinketPotionObjects.get(advancedGlowing).getPotionType(),
				getCatalyst(TrinketsConfig.SERVER.Potion.dragon.catalyst)
		)
				.registerPotion();

		Trinkets.log.info("Finished Generating Potions");
	}

	private static Ingredient getCatalyst(String catalyst) {
		final String[] itemConfig = catalyst.replace(";", ":").split(":");
		final String modIDString = CallHelper.getStringFromArray(itemConfig, 0);
		final String itemIDString = CallHelper.getStringFromArray(itemConfig, 1);
		final String metaString = CallHelper.getStringFromArray(itemConfig, 2).replaceAll("[^\\d]", "");
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
