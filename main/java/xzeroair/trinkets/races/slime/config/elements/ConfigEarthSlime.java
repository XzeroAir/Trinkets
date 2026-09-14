package xzeroair.trinkets.races.slime.config.elements;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityEarth;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;

public class ConfigEarthSlime {

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigAbilities ABILITIES = new ConfigAbilities();

    public class ConfigAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_EARTH_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_EARTH_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_EARTH)
        public ConfigAbilityImmunityEarth EARTH_IMMUNITY = new ConfigAbilityImmunityEarth();
    }

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
    public String[] DAMAGE_TYPES_TO_IGNORE = {
            "onHurt:*;isEarth",
            "onHurt:*;isWater;2.00",
            "onHurt:*;isMagic;0.85",
            "onHurt:*;isAir;0.85",
            "onHurt:*;isDark;0.85",
            "onHurt:*;isEarth;0.85",
            "onHurt:*;isFire;0.85",
            "onHurt:*;isIce;0.85",
            "onHurt:*;isLight;0.85",
            "onHurt:*;isLightning;0.85",
            "onHurt:*;isPoison;0.85",
            "onHurt:*;isVoid;0.85",
            "onHurt:*;isWater;0.85",
            "onHurt:*;*;0.45"
    };

    @Config.Name("90. " + ConstantsConfigLang.CONFIG_ATTRIBUTES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ATTRIBUTES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ATTRIBUTES)
    public String[] ATTRIBUTES = {
            "Name:generic.knockbackResistance; Amount:0.2; Operation:1"
    };

    @Config.Name(ConstantsConfigLang.CONFIG_COMPAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COMPAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COMPAT)
    public Compatibility COMPAT = new Compatibility();

    public class Compatibility {

        @Config.Name(ConstantsConfigLang.CONFIG_SURVIVAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL)
        public ConfigSurvivalCompat SURVIVAL = new ConfigSurvivalCompat();
    }
}
