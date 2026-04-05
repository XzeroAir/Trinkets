package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilitySafeGuard;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsBlur;
import xzeroair.trinkets.util.config.abilities.external.firstaid.ConfigAbilityHardHead;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class ConfigDamageShield {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ITEMS_SHIELD_OF_HONOR;

    public ConfigDamageShield() {

    }

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigAbilities ABILITIES = new ConfigAbilities();

    public class ConfigAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD)
        public ConfigAbilitySafeGuard SAFE_GUARD = new ConfigAbilitySafeGuard();

        @Config.Name(ConstantsConfigLang.CONFIG_EXTERNAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_EXTERNAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_EXTERNAL)
        public ExternalAbilities EXTERNAL = new ExternalAbilities();

        public class ExternalAbilities {

            @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_FIRST_AID_HARD_HEAD)
            public ConfigAbilityHardHead HARD_HEAD = new ConfigAbilityHardHead();

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
        public BaubleCompat BAUBLES = new BaubleCompat("body");
    }

}
