package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityAffinityWater;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityWater;
import xzeroair.trinkets.util.config.abilities.ConfigAbilitySkilledSwimmer;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsBlur;
import xzeroair.trinkets.util.config.abilities.external.survival.ConfigAbilitySurvivalThirstAbsorption;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class ConfigSeaStone {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ITEMS_SEA_STONE;

    public ConfigSeaStone() {

    }

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigAbilities ABILITIES = new ConfigAbilities();

    public class ConfigAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER)
        public ConfigAbilityAffinityWater WATER_AFFINITY = new ConfigAbilityAffinityWater();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_WATER_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_WATER_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_WATER)
        public ConfigAbilityImmunityWater WATER_IMMUNITY = new ConfigAbilityImmunityWater();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_SWIMMER_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_SWIMMER_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_SWIMMER)
        public ConfigAbilitySkilledSwimmer SKILLED_SWIMMER = new ConfigAbilitySkilledSwimmer();

        @Config.Name(ConstantsConfigLang.CONFIG_EXTERNAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_EXTERNAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_EXTERNAL)
        public ConfigExternal EXTERNAL = new ConfigExternal();

        public class ConfigExternal {

            @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION)
            public ConfigAbilitySurvivalThirstAbsorption WATER_ABSORPTION = new ConfigAbilitySurvivalThirstAbsorption();

            @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_CLEAR_VISION_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_CLEAR_VISION_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_CLEAR_VISION)
            public ConfigAbilityEnhancedVisualsBlur CLEAR_VISION = new ConfigAbilityEnhancedVisualsBlur();

        }

    }

    @Config.RequiresMcRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
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
    public String[] DAMAGE_TYPES_TO_IGNORE = {};

    @Config.Name("90. " + ConstantsConfigLang.CONFIG_ATTRIBUTES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ATTRIBUTES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ATTRIBUTES)
    public String[] ATTRIBUTES = {
            //@formatter:off
            "Name:forge.swimSpeed, Amount:4, Operation:2"
            //@formatter:on
    };

    @Config.Name(ConstantsConfigLang.CONFIG_COMPAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COMPAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COMPAT)
    public Compatibility COMPAT = new Compatibility();

    public class Compatibility {

        @Config.Name(ConstantsConfigLang.CONFIG_SURVIVAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL)
        public ConfigSurvivalCompat SURVIVAL = new ConfigSurvivalCompat().setImmuneToThirst().setImmuneToParasites();

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
        public BaubleCompat BAUBLES = new BaubleCompat("amulet");
    }

}
