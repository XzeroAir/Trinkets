package xzeroair.trinkets.init;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.items.potions.PotionObject;
import xzeroair.trinkets.items.potions.TransformationPotion;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.TrinketsConfig;

public class ModPotionTypes {

	public static final String baseSparkling = "sparkling";
	public static final String enhancedGlittering = "glittering";
	public static final String advancedGlowing = "glowing";
	public static final String restore = "restorative";

	public static HashMap<String, Potion> TrinketPotions = new HashMap();//new ArrayList<>();
	public static HashMap<String, PotionType> TrinketPotionTypes = new HashMap();//new ArrayList<>();
	public static HashMap<String, PotionObject> TrinketPotionObjects = new HashMap();//new ArrayList<>();

	public static PotionObject createBasePotion(String name, int color, int X, int Y, int duration, Item ingredient) {
		Potion potion = new BasePotion(name, duration, color, X, Y);
		PotionObject potObj = new PotionObject(potion, name, color, duration, ingredient);
		TrinketPotionObjects.put(potObj.getName(), potObj);
		return potObj;
	}

	public static PotionObject createPotion(String name, int color, int X, int Y, int duration, PotionType base, Item ingredient) {
		Potion potion = new BasePotion(name, duration, color, X, Y);
		PotionObject potObj = new PotionObject(potion, base, name, color, duration, ingredient);
		TrinketPotionObjects.put(potObj.getName(), potObj);
		return potObj;
	}

	public static PotionObject createRacePotion(EntityRace race, int X, int Y, int duration, PotionType base, Item ingredient) {
		Potion potion = new TransformationPotion(race.getName().toLowerCase(), race.getPrimaryColor(), race.getUUID().toString(), X, Y);
		PotionObject potObj = new PotionObject(potion, base, race.getName().toLowerCase(), race.getPrimaryColor(), duration, ingredient);
		TrinketPotionObjects.put(potObj.getName(), potObj);
		return potObj;
	}

	public static void registerPotionTypes() {

		Trinkets.log.info("Generating Potions");
		PotionObject pot = createBasePotion(baseSparkling, 16777160, 0, 0, 0, ModItems.crafting.glowing_powder).registerPotion();
		pot = createPotion(enhancedGlittering, 16777120, 0, 0, 0, TrinketPotionObjects.get(baseSparkling).getPotionType(), ModItems.crafting.glowing_ingot).registerPotion();
		pot = createPotion(advancedGlowing, 16777080, 0, 0, 0, TrinketPotionObjects.get(enhancedGlittering).getPotionType(), ModItems.crafting.glowing_gem).registerPotion();
		pot = createPotion(restore, 16777215, 0, 0, 0, TrinketPotionObjects.get(baseSparkling).getPotionType(), Item.getByNameOrId(TrinketsConfig.SERVER.Potion.restoreCatalyst)).registerPotion();

		pot = createRacePotion(
				EntityRaces.human, 0, 0,
				TrinketsConfig.SERVER.Potion.human.Duration,
				TrinketPotionObjects.get(baseSparkling).getPotionType(),
				Item.getByNameOrId(TrinketsConfig.SERVER.Potion.human.catalyst)
		)
				.registerPotion();

		pot = createRacePotion(
				EntityRaces.fairy, 1, 0,
				TrinketsConfig.SERVER.Potion.fairy.Duration,
				TrinketPotionObjects.get(advancedGlowing).getPotionType(),
				Item.getByNameOrId(TrinketsConfig.SERVER.Potion.fairy.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.dwarf, 2, 0,
				TrinketsConfig.SERVER.Potion.dwarf.Duration,
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				Item.getByNameOrId(TrinketsConfig.SERVER.Potion.dwarf.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.titan, 3, 0,
				TrinketsConfig.SERVER.Potion.titan.Duration,
				TrinketPotionObjects.get(advancedGlowing).getPotionType(),
				Item.getByNameOrId(TrinketsConfig.SERVER.Potion.titan.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.goblin, 4, 0,
				TrinketsConfig.SERVER.Potion.goblin.Duration,
				TrinketPotionObjects.get(baseSparkling).getPotionType(),
				Item.getByNameOrId(TrinketsConfig.SERVER.Potion.goblin.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.elf, 5, 0,
				TrinketsConfig.SERVER.Potion.elf.Duration,
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				Item.getByNameOrId(TrinketsConfig.SERVER.Potion.elf.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.faelis, 6, 0,
				TrinketsConfig.SERVER.Potion.faelis.Duration,
				TrinketPotionObjects.get(enhancedGlittering).getPotionType(),
				Item.getByNameOrId(TrinketsConfig.SERVER.Potion.faelis.catalyst)
		)
				.registerPotion();
		pot = createRacePotion(
				EntityRaces.dragon, 7, 0,
				TrinketsConfig.SERVER.Potion.dragon.Duration,
				TrinketPotionObjects.get(advancedGlowing).getPotionType(),
				Item.getByNameOrId(TrinketsConfig.SERVER.Potion.dragon.catalyst)
		)
				.registerPotion();

		Trinkets.log.info("Finished Generating Potions");
	}
}
