package xzeroair.trinkets.util;

public class ConstantsLang {

    public static final ConstantsLang INSTANCE = new ConstantsLang();

    public final static String DAMAGE_TYPE = Reference.MODID + ".damagetype";
    public final static String DAMAGE_TYPE_POISON = DAMAGE_TYPE + "." + TrinketsRegistryNames.ModDamageTypes.POISON;
    public final static String DAMAGE_TYPE_WATER = DAMAGE_TYPE + "." + TrinketsRegistryNames.ModDamageTypes.WATER;
    public final static String DAMAGE_TYPE_BLEED = DAMAGE_TYPE + "." + TrinketsRegistryNames.ModDamageTypes.BLEED;

    /**
     * xat.effect
     */
    public final static String EFFECT = Reference.MODID + ".effect";
    //@formatter:off
    public final static String EFFECT_IMMUNITY_ICE          = EFFECT + "." + TrinketsRegistryNames.ModElements.ICE + "_resistance";
    public final static String EFFECT_IMMUNITY_FIRE         = EFFECT + "." + TrinketsRegistryNames.ModElements.FIRE + "_resistance";
    public final static String EFFECT_IMMUNITY_LIGHTNING    = EFFECT + "." + TrinketsRegistryNames.ModElements.LIGHTNING + "_resistance";
    public final static String EFFECT_IMMUNITY_EARTH        = EFFECT + "." + TrinketsRegistryNames.ModElements.EARTH + "_resistance";
    public final static String EFFECT_IMMUNITY_WATER        = EFFECT + "." + TrinketsRegistryNames.ModElements.WATER + "_resistance";
    public final static String EFFECT_IMMUNITY_AIR          = EFFECT + "." + TrinketsRegistryNames.ModElements.AIR + "_resistance";
    public final static String EFFECT_IMMUNITY_POISON       = EFFECT + "." + TrinketsRegistryNames.ModElements.POISON + "_resistance";
    public final static String EFFECT_IMMUNITY_LIGHT        = EFFECT + "." + TrinketsRegistryNames.ModElements.LIGHT + "_resistance";
    public final static String EFFECT_IMMUNITY_DARK         = EFFECT + "." + TrinketsRegistryNames.ModElements.DARK + "_resistance";
    public final static String EFFECT_IMMUNITY_VOID         = EFFECT + "." + TrinketsRegistryNames.ModElements.VOID + "_resistance";
    //@formatter:ob
    /**
     * xat.effect.potion
     */
    public final static String POTION = "potion." + EFFECT;
    //@formatter:off
    public final static String POTION_IMMUNITY_ICE          = POTION + "." + TrinketsRegistryNames.ModElements.ICE + "_resistance";
    public final static String POTION_IMMUNITY_FIRE         = POTION + "." + TrinketsRegistryNames.ModElements.FIRE + "_resistance";
    public final static String POTION_IMMUNITY_LIGHTNING    = POTION + "." + TrinketsRegistryNames.ModElements.LIGHTNING + "_resistance";
    public final static String POTION_IMMUNITY_EARTH        = POTION + "." + TrinketsRegistryNames.ModElements.EARTH + "_resistance";
    public final static String POTION_IMMUNITY_WATER        = POTION + "." + TrinketsRegistryNames.ModElements.WATER + "_resistance";
    public final static String POTION_IMMUNITY_AIR          = POTION + "." + TrinketsRegistryNames.ModElements.AIR + "_resistance";
    public final static String POTION_IMMUNITY_POISON       = POTION + "." + TrinketsRegistryNames.ModElements.POISON + "_resistance";
    public final static String POTION_IMMUNITY_LIGHT        = POTION + "." + TrinketsRegistryNames.ModElements.LIGHT + "_resistance";
    public final static String POTION_IMMUNITY_DARK         = POTION + "." + TrinketsRegistryNames.ModElements.DARK + "_resistance";
    public final static String POTION_IMMUNITY_VOID         = POTION + "." + TrinketsRegistryNames.ModElements.VOID + "_resistance";
    //@formatter:ob

    /**
     * xat.effect.splash_potion
     */
    public final static String POTION_SPLASH = "splash_potion." + EFFECT;
    //@formatter:off
    public final static String POTION_SPLASH_IMMUNITY_ICE          = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.ICE + "_resistance";
    public final static String POTION_SPLASH_IMMUNITY_FIRE         = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.FIRE + "_resistance";
    public final static String POTION_SPLASH_IMMUNITY_LIGHTNING    = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.LIGHTNING + "_resistance";
    public final static String POTION_SPLASH_IMMUNITY_EARTH        = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.EARTH + "_resistance";
    public final static String POTION_SPLASH_IMMUNITY_WATER        = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.WATER + "_resistance";
    public final static String POTION_SPLASH_IMMUNITY_AIR          = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.AIR + "_resistance";
    public final static String POTION_SPLASH_IMMUNITY_POISON       = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.POISON + "_resistance";
    public final static String POTION_SPLASH_IMMUNITY_LIGHT        = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.LIGHT + "_resistance";
    public final static String POTION_SPLASH_IMMUNITY_DARK         = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.DARK + "_resistance";
    public final static String POTION_SPLASH_IMMUNITY_VOID         = POTION_SPLASH + "." + TrinketsRegistryNames.ModElements.VOID + "_resistance";
    //@formatter:ob
    /**
     * xat.effect.lingering_potion
     */
    public final static String POTION_LINGERING = "lingering_potion." + EFFECT;
    //@formatter:off
    public final static String POTION_LINGERING_IMMUNITY_ICE          = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.ICE + "_resistance";
    public final static String POTION_LINGERING_IMMUNITY_FIRE         = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.FIRE + "_resistance";
    public final static String POTION_LINGERING_IMMUNITY_LIGHTNING    = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.LIGHTNING + "_resistance";
    public final static String POTION_LINGERING_IMMUNITY_EARTH        = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.EARTH + "_resistance";
    public final static String POTION_LINGERING_IMMUNITY_WATER        = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.WATER + "_resistance";
    public final static String POTION_LINGERING_IMMUNITY_AIR          = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.AIR + "_resistance";
    public final static String POTION_LINGERING_IMMUNITY_POISON       = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.POISON + "_resistance";
    public final static String POTION_LINGERING_IMMUNITY_LIGHT        = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.LIGHT + "_resistance";
    public final static String POTION_LINGERING_IMMUNITY_DARK         = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.DARK + "_resistance";
    public final static String POTION_LINGERING_IMMUNITY_VOID         = POTION_LINGERING + "." + TrinketsRegistryNames.ModElements.VOID + "_resistance";
    //@formatter:ob
    /**
     * xat.effect.tipped_arrow
     */
    public final static String TIPPED_ARROW = "tipped_arrow." + EFFECT;
    //@formatter:off
    public final static String TIPPED_ARROW_IMMUNITY_ICE          = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.ICE + "_resistance";
    public final static String TIPPED_ARROW_IMMUNITY_FIRE         = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.FIRE + "_resistance";
    public final static String TIPPED_ARROW_IMMUNITY_LIGHTNING    = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.LIGHTNING + "_resistance";
    public final static String TIPPED_ARROW_IMMUNITY_EARTH        = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.EARTH + "_resistance";
    public final static String TIPPED_ARROW_IMMUNITY_WATER        = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.WATER + "_resistance";
    public final static String TIPPED_ARROW_IMMUNITY_AIR          = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.AIR + "_resistance";
    public final static String TIPPED_ARROW_IMMUNITY_POISON       = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.POISON + "_resistance";
    public final static String TIPPED_ARROW_IMMUNITY_LIGHT        = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.LIGHT + "_resistance";
    public final static String TIPPED_ARROW_IMMUNITY_DARK         = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.DARK + "_resistance";
    public final static String TIPPED_ARROW_IMMUNITY_VOID         = TIPPED_ARROW + "." + TrinketsRegistryNames.ModElements.VOID + "_resistance";
    //@formatter:ob

    ///  RACES
    //@formatter:off
    public final static String POTION_RACE_HUMAN    = EFFECT + "." + TrinketsRegistryNames.ModRaces.HUMAN;
    public final static String POTION_RACE_DWARF    = EFFECT + "." + TrinketsRegistryNames.ModRaces.DWARF;
    public final static String POTION_RACE_ELF      = EFFECT + "." + TrinketsRegistryNames.ModRaces.ELF;
    public final static String POTION_RACE_FAELIS   = EFFECT + "." + TrinketsRegistryNames.ModRaces.FAELIS;
    public final static String POTION_RACE_GOBLIN   = EFFECT + "." + TrinketsRegistryNames.ModRaces.GOBLIN;
    public final static String POTION_RACE_TITAN    = EFFECT + "." + TrinketsRegistryNames.ModRaces.TITAN;
    public final static String POTION_RACE_FAIRY    = EFFECT + "." + TrinketsRegistryNames.ModRaces.FAIRY;
    public final static String POTION_RACE_TAURUS   = EFFECT + "." + TrinketsRegistryNames.ModRaces.TAURUS;
    //@formatter:on
    ///
    //@formatter:off
    public final static String RACE = Reference.MODID + ".race";
    public final static String RACE_HUMAN        = RACE + "." + TrinketsRegistryNames.ModRaces.HUMAN;
    public final static String RACE_DWARF        = RACE + "." + TrinketsRegistryNames.ModRaces.DWARF;
    public final static String RACE_ELF          = RACE + "." + TrinketsRegistryNames.ModRaces.ELF;
    public final static String RACE_FAELIS       = RACE + "." + TrinketsRegistryNames.ModRaces.FAELIS;
    public final static String RACE_GOBLIN       = RACE + "." + TrinketsRegistryNames.ModRaces.GOBLIN;
    public final static String RACE_TITAN        = RACE + "." + TrinketsRegistryNames.ModRaces.TITAN;
    public final static String RACE_FAIRY        = RACE + "." + TrinketsRegistryNames.ModRaces.FAIRY;
    public final static String RACE_TAURUS       = RACE + "." + TrinketsRegistryNames.ModRaces.TAURUS;
    //@formatter:on
    ///


    /// ELEMENTS
    //@formatter:off
    public final static String ELEMENT = Reference.MODID + ".element";
    public final static String ELEMENT_NEUTRAL         = ELEMENT + "." + TrinketsRegistryNames.ModElements.NEUTRAL;
    public final static String ELEMENT_EARTH           = ELEMENT + "." + TrinketsRegistryNames.ModElements.EARTH;
    public final static String ELEMENT_WATER           = ELEMENT + "." + TrinketsRegistryNames.ModElements.WATER;
    public final static String ELEMENT_FIRE            = ELEMENT + "." + TrinketsRegistryNames.ModElements.FIRE;
    public final static String ELEMENT_ICE             = ELEMENT + "." + TrinketsRegistryNames.ModElements.ICE;
    public final static String ELEMENT_AIR             = ELEMENT + "." + TrinketsRegistryNames.ModElements.AIR;
    public final static String ELEMENT_LIGHTNING       = ELEMENT + "." + TrinketsRegistryNames.ModElements.LIGHTNING;
    public final static String ELEMENT_POISON          = ELEMENT + "." + TrinketsRegistryNames.ModElements.POISON;
    public final static String ELEMENT_LIGHT           = ELEMENT + "." + TrinketsRegistryNames.ModElements.LIGHT;
    public final static String ELEMENT_DARK            = ELEMENT + "." + TrinketsRegistryNames.ModElements.DARK;
    public final static String ELEMENT_VOID            = ELEMENT + "." + TrinketsRegistryNames.ModElements.VOID;
    //@formatter:on
    ///


}
