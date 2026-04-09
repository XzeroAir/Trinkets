package xzeroair.trinkets.races.dragon.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBreath;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityFrostWalker;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityIce;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;

public class ConfigIceDragon {

//    private static final String[] DEFAULT_ICE_EFFECTS = {"minecraft:slowness:100:2"};

    public ConfigIceDragon() {
    }

    /// Ice Abilities
    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigIceDragonAbilities ABILITIES = new ConfigIceDragonAbilities();

    public class ConfigIceDragonAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_ICE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_ICE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_ICE)
        public ConfigAbilityImmunityIce ICE_IMMUNITY = new ConfigAbilityImmunityIce();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_ICE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_ICE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_ICE)
        public ConfigAbilityBreath ICE_BREATH = new ConfigAbilityBreath("minecraft:slowness:100:2");

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_FROST_WALKER_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_FROST_WALKER_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_FROST_WALKER)
        public ConfigAbilityFrostWalker FROST_WALKER = new ConfigAbilityFrostWalker();


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
    public String[] DAMAGE_TYPES_TO_IGNORE = {"*;isIce"};

    @Config.Name("90. " + ConstantsConfigLang.CONFIG_ATTRIBUTES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ATTRIBUTES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ATTRIBUTES)
    public String[] ATTRIBUTES = {
            //@formatter:off
            "Name:generic.maxHealth, Amount:0.25, Operation:1",
            "Name:generic.attackDamage, Amount:0.5, Operation:1",
            "Name:generic.armorToughness, Amount:0.5, Operation:1",
            "Name:xat.flyspeed, Amount:-0.6, Operation:2"
            //@formatter:on
    };

    @Config.Name(ConstantsConfigLang.CONFIG_COMPAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COMPAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COMPAT)
    public CompatIce COMPAT = new CompatIce();

    public class CompatIce {

        @Config.Name(ConstantsConfigLang.CONFIG_SURVIVAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL)
        public ConfigSurvivalCompat SURVIVAL = new ConfigSurvivalCompat().setImmuneToCold(); /// Set Ice Dragon immune to cold

    }

}
