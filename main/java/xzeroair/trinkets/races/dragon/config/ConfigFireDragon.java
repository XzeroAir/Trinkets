package xzeroair.trinkets.races.dragon.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBreath;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityFire;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;

public class ConfigFireDragon {

//    private static final String[] DEFAULT_FIRE_EFFECTS = ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY;

    public ConfigFireDragon() {
    }

    /// Fire Abilities
    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigFireDragonAbilities ABILITIES = new ConfigFireDragonAbilities();

    public class ConfigFireDragonAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_FIRE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_FIRE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_FIRE)
        public ConfigAbilityImmunityFire FIRE_IMMUNITY = new ConfigAbilityImmunityFire();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_FIRE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_FIRE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_FIRE)
        public ConfigAbilityBreath FIRE_BREATH = new ConfigAbilityBreath();

    }

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_EFFECTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EFFECTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EFFECTS)
    public String[] EFFECTS_TO_ADD = {};

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_RESISTANCES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RESISTANCES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RESISTANCES)
    public String[] EFFECTS_TO_REMOVE = {"lycanitesmobs:smouldering"};

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_DAMAGE_TYPES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DAMAGE_TYPES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DAMAGE_TYPES)
    public String[] DAMAGE_TYPES_TO_IGNORE = {"*;isFire"};
    /// Set Fire Dragon immune to fire

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
    public CompatFire COMPAT = new CompatFire();

    public class CompatFire {

        @Config.Name(ConstantsConfigLang.CONFIG_SURVIVAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL)
        public ConfigSurvivalCompat SURVIVAL = new ConfigSurvivalCompat().setImmuneToHeat(); /// Set Fire Dragon immune to heat

    }
}
