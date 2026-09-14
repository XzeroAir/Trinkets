package xzeroair.trinkets.races.fairy.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.races.fairy.config.elements.*;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityClimbing;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityElytraFlight;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityFlight;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityHealCloud;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.config.race.RaceMagicConfig;
import xzeroair.trinkets.util.config.race.RaceSizeConfig;
import xzeroair.trinkets.util.config.race.ConfigRaceEquipmentWeight;

public class FairyConfig {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_RACES_FAIRY;

    public FairyConfig() {
    }

    /// Default Abilities
    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigAbilities ABILITIES = new ConfigAbilities();

    public class ConfigAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_CREATIVE_FLIGHT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_CREATIVE_FLIGHT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_CREATIVE_FLIGHT)
        public ConfigAbilityFlight FLIGHT = new ConfigAbilityFlight();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT)
        public ConfigAbilityElytraFlight ELYTRA_FLIGHT = new ConfigAbilityElytraFlight(true, true, 0F, 0F, 0.42D);

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING)
        public ConfigAbilityClimbing CLIMBING = new ConfigAbilityClimbing();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD)
        public ConfigAbilityHealCloud RESTORATION_FIELD = new ConfigAbilityHealCloud();

    }


    @Config.Name("01. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL)
    public boolean CAN_MOUNT = true;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT)
    public boolean CAN_CONTROL_BOATS = false;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED)
    public boolean MOUNT_WHITELIST = false;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST)
    public String[] MOUNT_BLACKLIST = {
            //@formatter:off
			"quark:seat",
			"sit:entity_sit"
			//@formatter:on
    };

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_EFFECTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EFFECTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EFFECTS)
    public String[] EFFECTS_TO_ADD = {};

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_RESISTANCES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RESISTANCES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RESISTANCES)
    public String[] EFFECTS_TO_REMOVE = {};

    @Config.Name("07. " + ConstantsConfigLang.CONFIG_DAMAGE_TYPES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DAMAGE_TYPES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DAMAGE_TYPES)
    public String[] DAMAGE_TYPES_TO_IGNORE = {};

    @Config.Name("90. " + ConstantsConfigLang.CONFIG_ATTRIBUTES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ATTRIBUTES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ATTRIBUTES)
    public String[] ATTRIBUTES = {
            //@formatter:off
            "Name:generic.maxHealth, Amount:-0.6, Operation:2",
            "Name:generic.movementSpeed, Amount:-0.25, Operation:2",
            "Name:generic.attackDamage, Amount:-0.75, Operation:2",
            "Name:generic.armor, Amount:-0.5, Operation:2",
            "Name:generic.armorToughness, Amount:-0.25, Operation:2",
            "Name:generic.reachDistance, Amount:-0.35, Operation:2",
            "Name:forge.swimSpeed, Amount:-0.25, Operation:2",
            "Name:xat.jump, Amount:-0.25, Operation:2",
            "Name:xat.stepheight, Amount:-0.35, Operation:0",
            "Name:xat.flyspeed, Amount:-0.4, Operation:2"
            //@formatter:on
    };

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC)
    public RaceMagicConfig MAGIC = new RaceMagicConfig(500);

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_SIZE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_SIZE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_SIZE)
    public RaceSizeConfig SIZE = new RaceSizeConfig(25, 25);

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS)
    public ElementsConfig ELEMENTS = new ElementsConfig();

    public class ElementsConfig {

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_AIR_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_AIR_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_AIR)
        public ConfigAirFairy AIR = new ConfigAirFairy();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_DARK_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_DARK_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_DARK)
        public ConfigDarkFairy DARK = new ConfigDarkFairy();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH)
        public ConfigEarthFairy EARTH = new ConfigEarthFairy();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE)
        public ConfigFireFairy FIRE = new ConfigFireFairy();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_ICE)
        public ConfigIceFairy ICE = new ConfigIceFairy();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT)
        public ConfigLightFairy LIGHT = new ConfigLightFairy();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING)
        public ConfigLightningFairy LIGHTNING = new ConfigLightningFairy();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_POISON_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_POISON_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_POISON)
        public ConfigPoisonFairy POISON = new ConfigPoisonFairy();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_VOID_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_VOID_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_VOID)
        public ConfigVoidFairy VOID = new ConfigVoidFairy();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_WATER_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_WATER_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_WATER)
        public ConfigWaterFairy WATER = new ConfigWaterFairy();
    }

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

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_EQUIPMENT_WEIGHT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_EQUIPMENT_WEIGHT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_EQUIPMENT_WEIGHT)
    public ConfigRaceEquipmentWeight EQUIPMENT_WEIGHT = new ConfigRaceEquipmentWeight();

}
