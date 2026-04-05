package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityEnderQueen;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsStatic;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class ConfigEnderCrown {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ITEMS_ENDER_CROWN;

    public ConfigEnderCrown() {
    }

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigAbilities ABILITIES = new ConfigAbilities();

    public class ConfigAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN)
        public ConfigAbilityEnderQueen ENDER_QUEEN = new ConfigAbilityEnderQueen();

        @Config.Name(ConstantsConfigLang.CONFIG_EXTERNAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_EXTERNAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_EXTERNAL)
        public ConfigExternal EXTERNAL = new ConfigExternal();

        public class ConfigExternal {

            @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_ENDER_EYES_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_ENDER_EYES_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_ENDER_EYES)
            public ConfigAbilityEnhancedVisualsStatic ENDER_EYES = new ConfigAbilityEnhancedVisualsStatic();

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
        public ConfigSurvivalCompat SURVIVAL = new ConfigSurvivalCompat().setImmuneToCold();

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
