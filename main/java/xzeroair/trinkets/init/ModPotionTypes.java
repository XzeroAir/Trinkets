package xzeroair.trinkets.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.ConstantsTextureResourceLocation;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.items.potions.IceResistance;
import xzeroair.trinkets.items.potions.LightningResistance;
import xzeroair.trinkets.items.potions.PotionObject;
import xzeroair.trinkets.items.potions.TransformationPotion;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.helpers.StringUtils;

import java.util.HashMap;

public class ModPotionTypes {

    public static final String baseSparkling = "sparkling";
    public static final String enhancedGlittering = "glittering";
    public static final String advancedGlowing = "glowing";
    public static final String restore = "restorative";
    public static final String iceResist = "ice_resistance";
    public static final String lightningResist = "lightning_resistance";

    public static final String dragon = TrinketsRegistryNames.ModRaces.DRAGON;
    public static final String dragon_fire = TrinketsRegistryNames.ModRaces.DRAGON + "_" + TrinketsRegistryNames.ModElements.FIRE;
    public static final String dragon_ice = TrinketsRegistryNames.ModRaces.DRAGON + "_" + TrinketsRegistryNames.ModElements.ICE;
    public static final String dragon_lightning = TrinketsRegistryNames.ModRaces.DRAGON + "_" + TrinketsRegistryNames.ModElements.LIGHTNING;

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

    public static PotionObject createRacePotion(EntityRace race, Element element, PotionType craftingBase, int duration, Ingredient craftingIngredient) {
        return createRacePotion(race, element, craftingBase, duration, duration * 3, craftingIngredient);
    }

    public static PotionObject createRacePotion(EntityRace race, Element element, PotionType craftingBase, int duration, int extendedDuration, Ingredient craftingIngredient) {
        return createRacePotion(race, element, craftingBase, duration, extendedDuration, false, craftingIngredient);
    }

    public static PotionObject createRacePotion(EntityRace race, Element element, PotionType craftingBase, int duration, int extendedDuration, boolean isBadEffect, Ingredient craftingIngredient) {
        final String modid = race.getRegistryName().getNamespace().toString();
        final String name = race.getRegistryName().getPath().toString() + (element.equals(Elements.NEUTRAL) ? "" : "_" + element.getName());
        final int color = element.equals(Elements.NEUTRAL) ? race.getPrimaryColor() : element.getPrimaryColor();
        final Potion potion = new TransformationPotion(modid.toLowerCase(), name.toLowerCase(), color, duration, new RaceCache(race, element, element, true, duration, modid + ":" + name), ConstantsTextureResourceLocation.getPotionIconForRace(race, element));
        final PotionObject obj = createCompoundPotion(potion, craftingBase, modid.toLowerCase(), name.toLowerCase(), color, duration, extendedDuration, craftingIngredient);
        TrinketRacePotionObjects.put(obj.getName(), obj);
        return obj;
    }

    public static void registerPotionTypes() {

        Trinkets.LOGGER.info("Generating Potions");

        createBasePotion(baseSparkling, 16777160, 0, Ingredient.fromItem(ModItems.crafting.glowing_powder)).registerWithPotion();
        createCompoundPotion(TrinketPotionObjects.get(baseSparkling).getPotionType(), enhancedGlittering, 16777120, 0, Ingredient.fromItem(ModItems.crafting.glowing_ingot)).registerWithPotion();
        createCompoundPotion(TrinketPotionObjects.get(enhancedGlittering).getPotionType(), advancedGlowing, 16777080, 0, Ingredient.fromItem(ModItems.crafting.glowing_gem)).registerWithPotion();
        createCompoundPotion(new IceResistance(iceResist, 3600, 15132390, false), TrinketPotionObjects.get(baseSparkling).getPotionType(), iceResist, 15132390, 3600, 9600, Ingredient.fromItem(Item.getItemFromBlock(Blocks.SNOW))).registerWithPotion();
        createCompoundPotion(new LightningResistance(lightningResist, 3600, 15132390, false), TrinketPotionObjects.get(baseSparkling).getPotionType(), lightningResist, 15132390, 3600, 9600, Ingredient.fromItem(ModItems.foods.mana_crystal)).registerWithPotionWithoutRecipe();
        /*
         * Create Race Potions
         */
        createRacePotion(EntityRaces.human, Elements.NEUTRAL, TrinketPotionObjects.get(baseSparkling).getPotionType(), TrinketsConfig.SERVER.POTIONS.HUMAN.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.HUMAN.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.fairy, Elements.NEUTRAL, TrinketPotionObjects.get(advancedGlowing).getPotionType(), TrinketsConfig.SERVER.POTIONS.FAIRY.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.FAIRY.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.dwarf, Elements.NEUTRAL, TrinketPotionObjects.get(enhancedGlittering).getPotionType(), TrinketsConfig.SERVER.POTIONS.DWARF.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.DWARF.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.titan, Elements.NEUTRAL, TrinketPotionObjects.get(advancedGlowing).getPotionType(), TrinketsConfig.SERVER.POTIONS.TITAN.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.TITAN.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.goblin, Elements.NEUTRAL, TrinketPotionObjects.get(baseSparkling).getPotionType(), TrinketsConfig.SERVER.POTIONS.GOBLIN.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.GOBLIN.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.elf, Elements.NEUTRAL, TrinketPotionObjects.get(enhancedGlittering).getPotionType(), TrinketsConfig.SERVER.POTIONS.ELF.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.ELF.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.faelis, Elements.NEUTRAL, TrinketPotionObjects.get(enhancedGlittering).getPotionType(), TrinketsConfig.SERVER.POTIONS.FAELIS.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.FAELIS.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.dragon, Elements.NEUTRAL, TrinketPotionObjects.get(advancedGlowing).getPotionType(), TrinketsConfig.SERVER.POTIONS.DRAGON.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.DRAGON.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.dragon, Elements.FIRE, TrinketPotionObjects.get(dragon).getPotionType(), TrinketsConfig.SERVER.POTIONS.DRAGON_FIRE.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.DRAGON_FIRE.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.dragon, Elements.ICE, TrinketPotionObjects.get(dragon).getPotionType(), TrinketsConfig.SERVER.POTIONS.DRAGON_ICE.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.DRAGON_ICE.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.dragon, Elements.LIGHTNING, TrinketPotionObjects.get(dragon).getPotionType(), TrinketsConfig.SERVER.POTIONS.DRAGON_LIGHTNING.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.DRAGON_LIGHTNING.catalyst)).registerWithPotion();
        createRacePotion(EntityRaces.taurus, Elements.NEUTRAL, TrinketPotionObjects.get(enhancedGlittering).getPotionType(), TrinketsConfig.SERVER.POTIONS.TAURUS.Duration, getCatalyst(TrinketsConfig.SERVER.POTIONS.TAURUS.catalyst)).registerWithPotion();

        Trinkets.LOGGER.info("Finished Generating Potions");
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
            Trinkets.LOGGER.warn("Invalid catalyst meta from | " + catalyst);
        }
        return Ingredient.fromStacks(new ItemStack(Item.getByNameOrId(modIDString + ":" + itemIDString), 1, meta));
    }
}
