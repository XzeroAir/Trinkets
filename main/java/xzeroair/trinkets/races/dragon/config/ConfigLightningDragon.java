package xzeroair.trinkets.races.dragon.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityBreath;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityLightning;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityLightningBolt;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;

public class ConfigLightningDragon {

//    private static final String[] DEFAULT_LIGHTNING_EFFECTS = {"minecraft:slowness:20:4", "minecraft:weakness:20:1"};

    public ConfigLightningDragon() {
    }

    /// Lightning Abilities
    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigLightningDragonAbilities ABILITIES = new ConfigLightningDragonAbilities();

    public class ConfigLightningDragonAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_LIGHTNING_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_LIGHTNING_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_LIGHTNING)
        public ConfigAbilityImmunityLightning LIGHTNING_IMMUNITY = new ConfigAbilityImmunityLightning();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_LIGHTNING_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_LIGHTNING_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_LIGHTNING)
        public ConfigAbilityBreath LIGHTNING_BREATH = new ConfigAbilityBreath("minecraft:slowness:20:4", "minecraft:weakness:20:1");

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_LIGHTNING_BOLT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LIGHTNING_BOLT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LIGHTNING_BOLT)
        public ConfigAbilityLightningBolt LIGHTNING_BOLT = new ConfigAbilityLightningBolt(40F, 200F, 20);

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
    public String[] DAMAGE_TYPES_TO_IGNORE = {};

    @Config.Name(ConstantsConfigLang.CONFIG_COMPAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COMPAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COMPAT)
    public CompatLightning COMPAT = new CompatLightning();

    public class CompatLightning {

        @Config.Name(ConstantsConfigLang.CONFIG_SURVIVAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL)
        public ConfigSurvivalCompat SURVIVAL = new ConfigSurvivalCompat();

    }

}
