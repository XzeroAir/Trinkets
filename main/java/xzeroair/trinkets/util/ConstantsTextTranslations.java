package xzeroair.trinkets.util;

import net.minecraft.util.text.TextComponentTranslation;

public class ConstantsTextTranslations {

    public static final TextComponentTranslation FIRE_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_FIRE);
    public static final TextComponentTranslation ICE_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_ICE);
    public static final TextComponentTranslation LIGHTNING_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_LIGHTNING);
    public static final TextComponentTranslation LIGHT_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_LIGHT);
    public static final TextComponentTranslation DARK_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_DARK);
    public static final TextComponentTranslation WATER_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_WATER);
    public static final TextComponentTranslation EARTH_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_EARTH);
    public static final TextComponentTranslation POISON_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_POISON);
    public static final TextComponentTranslation AIR_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_AIR);
    public static final TextComponentTranslation VOID_IMMUNITY = new TextComponentTranslation(ConstantsLang.EFFECT_IMMUNITY_VOID);


    public static final TextComponentTranslation KEY_ALT = new TextComponentTranslation(Reference.MODID + ".holdalt");
    public static final TextComponentTranslation KEY_CTRL = new TextComponentTranslation(Reference.MODID + ".holdctrl");
    public static final TextComponentTranslation KEY_SHIFT = new TextComponentTranslation(Reference.MODID + ".holdshift");

    private static final String RACE_GUI_PATH = "gui." + Reference.MODID + ".race";
    private static final String RACE_GUI_TRAIT_MAIN = RACE_GUI_PATH + ".trait.main";
    private static final String RACE_GUI_TRAIT_ALT = RACE_GUI_PATH + ".trait.alt";
    public static final TextComponentTranslation GUI_HIDE_MAIN_TRAIT = new TextComponentTranslation(RACE_GUI_TRAIT_MAIN);
    public static final TextComponentTranslation GUI_MAIN_TRAIT_VARIANT = new TextComponentTranslation(RACE_GUI_TRAIT_MAIN + ".variant");
    public static final TextComponentTranslation GUI_HIDE_ALT_TRAIT = new TextComponentTranslation(RACE_GUI_TRAIT_ALT);
    public static final TextComponentTranslation GUI_ALT_TRAIT_VARIANT = new TextComponentTranslation(RACE_GUI_TRAIT_ALT + ".variant");
    public static final TextComponentTranslation GUI_RACE_INFO = new TextComponentTranslation(RACE_GUI_PATH + ".stats");
    public static final TextComponentTranslation GUI_FLIP_PLAYER = new TextComponentTranslation(RACE_GUI_PATH + ".flip");
    public static final TextComponentTranslation GUI_MANA_BAR = new TextComponentTranslation(ConstantsConfigLang.CONFIG_MAGIC_HUD);

    public static class TextMinecraft {
        public static final String MINECRAFT_POTION_FIRE_RESISTANCE = "effect.fireResistance";
        public static final String MINECRAFT_POTION_WATER_BREATHING = "effect.water_breathing";
        public static final String MINECRAFT_POTION_RESISTANCE = "effect.resistance";
        public static final String MINECRAFT_POTION_WITHER = "effect.wither";
        public static final TextComponentTranslation MINECRAFT_WITHER = new TextComponentTranslation(MINECRAFT_POTION_WITHER);
        public static final TextComponentTranslation MINECRAFT_RESISTANCE = new TextComponentTranslation(MINECRAFT_POTION_RESISTANCE);
        public static final TextComponentTranslation MINECRAFT_WATER_BREATHING = new TextComponentTranslation(MINECRAFT_POTION_WATER_BREATHING);
        public static final TextComponentTranslation MINECRAFT_FIRE_RESISTANCE = new TextComponentTranslation(MINECRAFT_POTION_FIRE_RESISTANCE);
    }
}
