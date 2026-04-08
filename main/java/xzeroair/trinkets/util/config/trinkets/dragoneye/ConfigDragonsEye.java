package xzeroair.trinkets.util.config.trinkets.dragoneye;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityGreedyEyes;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityFire;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityNightVision;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class ConfigDragonsEye {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ITEMS_DRAGONS_EYE;

    public ConfigDragonsEye() {
    }

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigAbilities ABILITIES = new ConfigAbilities();

    public class ConfigAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES)
        public ConfigAbilityGreedyEyes GREEDY_EYES = new ConfigAbilityGreedyEyes();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NIGHT_VISION_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_NIGHT_VISION_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_NIGHT_VISION)
        public ConfigAbilityNightVision NIGHT_VISION = new ConfigAbilityNightVision();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_FIRE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_FIRE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_FIRE)
        public ConfigAbilityImmunityFire FIRE_IMMUNITY = new ConfigAbilityImmunityFire(false);

    }

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS)
    public ElementConfig ELEMENTS = new ElementConfig();

    public class ElementConfig {

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE)
        public ConfigFireDragonsEye FIRE = new ConfigFireDragonsEye();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_ICE)
        public ConfigIceDragonsEye ICE = new ConfigIceDragonsEye();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING)
        public ConfigLightningDragonsEye LIGHTNING = new ConfigLightningDragonsEye();

    }


    @Config.RequiresMcRestart
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_EFFECTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EFFECTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EFFECTS)
    public String[] EFFECTS_TO_ADD = {};

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_RESISTANCES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RESISTANCES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RESISTANCES)
    public String[] EFFECTS_TO_REMOVE = {};

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_DAMAGE_TYPES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DAMAGE_TYPES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DAMAGE_TYPES)
    public String[] DAMAGE_TYPES_TO_IGNORE = {"onAttacked:*;isMagic", "onHurt:*;isMagic"};

    @Config.Name("90. " + ConstantsConfigLang.CONFIG_ATTRIBUTES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ATTRIBUTES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ATTRIBUTES)
    public String[] ATTRIBUTES = {};

    @Config.Name(ConstantsConfigLang.CONFIG_COMPAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COMPAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COMPAT)
    public Compatibility COMPAT = new Compatibility();

    public class Compatibility {

        @Config.Name(ConstantsConfigLang.CONFIG_SURVIVAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL)
        public ConfigSurvivalCompat SURVIVAL = new ConfigSurvivalCompat();

        @Config.Name(ConstantsConfigLang.CONFIG_BAUBLES_NAME)
        @Config.Comment({
                //@formatter:off
                "If the mod Baubles is installed what bauble slot should it use",
                "Available Types:",
                "Trinket, Any, All",
                "Amulet, Necklace, Pendant",
                "Ring, Rings",
                "Belt",
                "Head, Hat",
                "Body, Chest",
                "Charm"
                //@formatter:on
        })
        @Config.LangKey(ConstantsConfigLang.CONFIG_BAUBLES)
        public BaubleCompat BAUBLES = new BaubleCompat("head");
    }

}
