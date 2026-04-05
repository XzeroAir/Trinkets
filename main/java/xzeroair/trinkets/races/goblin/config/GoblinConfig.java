package xzeroair.trinkets.races.goblin.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityClimbing;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityWolfRider;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.config.race.RaceMagicConfig;
import xzeroair.trinkets.util.config.race.RaceSizeConfig;

public class GoblinConfig {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_RACES_GOBLIN;

    public GoblinConfig() {
    }

    /// Default Abilities
    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigAbilities ABILITIES = new ConfigAbilities();

    public class ConfigAbilities {

        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_WOLF_RIDER_COMMENT)
        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_WOLF_RIDER_NAME)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_WOLF_RIDER)
        public ConfigAbilityWolfRider WOLF_RIDER = new ConfigAbilityWolfRider();

        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING_COMMENT)
        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING_NAME)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING)
        public ConfigAbilityClimbing CLIMBING = new ConfigAbilityClimbing();

    }

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_RACES_GOBLIN_RESISTANCE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_GOBLIN_RESISTANCE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_GOBLIN_RESISTANCE)
    public boolean NATURAL_RESISTANCE_TO_EXPLOSIVES = true;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_RACES_GOBLIN_CREEPERS_FRIENDLY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_GOBLIN_CREEPERS_FRIENDLY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_GOBLIN_CREEPERS_FRIENDLY)
    public boolean FRIENDLY_CREEPERS = true;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_RACES_GOBLIN_CREEPERS_EXPLODE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_GOBLIN_CREEPERS_EXPLODE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_GOBLIN_CREEPERS_EXPLODE)
    public boolean CREEPERS_EXPLODE_ON_CONTACT = true;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL)
    public boolean CAN_MOUNT = true;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT)
    public boolean CAN_CONTROL_BOATS = true;

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED)
    public boolean MOUNT_WHITELIST = false;

    @Config.Name("07. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST)
    public String[] MOUNT_BLACKLIST = {};

    @Config.Name("08. " + ConstantsConfigLang.CONFIG_EFFECTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EFFECTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EFFECTS)
    public String[] EFFECTS_TO_ADD = {};

    @Config.Name("09. " + ConstantsConfigLang.CONFIG_RESISTANCES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RESISTANCES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RESISTANCES)
    public String[] EFFECTS_TO_REMOVE = {};

    @Config.Name("10. " + ConstantsConfigLang.CONFIG_DAMAGE_TYPES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DAMAGE_TYPES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DAMAGE_TYPES)
    public String[] DAMAGE_TYPES_TO_IGNORE = {};


    @Config.Name("90. " + ConstantsConfigLang.CONFIG_ATTRIBUTES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ATTRIBUTES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ATTRIBUTES)
    public String[] ATTRIBUTES = {
            //@formatter:off
            "Name:generic.maxHealth, Amount:-0.4, Operation:2",
            "Name:generic.movementSpeed, Amount:0.2, Operation:1",
            "Name:generic.attackDamage, Amount:0.5, Operation:1",
            "Name:generic.luck, Amount:1, Operation:0",
            "Name:forge.swimSpeed, Amount:0.1, Operation:1"
            //formatter:on
    };

    @Config.Name( ConstantsConfigLang.CONFIG_MAGIC_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC)
    public RaceMagicConfig MAGIC = new RaceMagicConfig(75);

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_SIZE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_SIZE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_SIZE)
    public RaceSizeConfig SIZE = new RaceSizeConfig(50, 50);

    /// Default Compat
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
