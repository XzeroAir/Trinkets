package xzeroair.trinkets.races.dragon.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.*;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsFireParticles;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.config.race.RaceMagicConfig;
import xzeroair.trinkets.util.config.race.RaceSizeConfig;
import xzeroair.trinkets.util.config.race.ConfigRaceEquipmentWeight;

public class DragonConfig {

    private final static String LANG_PREFIX = ConstantsConfigLang.CONFIG_RACES_DRAGON;

    public DragonConfig() {
    }

    /// Default Abilities
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

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_CREATIVE_FLIGHT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_CREATIVE_FLIGHT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_CREATIVE_FLIGHT)
        public ConfigAbilityFlight FLIGHT = new ConfigAbilityFlight(true, 5F);

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT)
        public ConfigAbilityElytraFlight ELYTRA_FLIGHT = new ConfigAbilityElytraFlight(true, true, 0F, 10F, 0.42D);

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_DRAGON_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_DRAGON_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_DRAGON)
        public ConfigAbilityBreath DRAGON_BREATH = new ConfigAbilityBreath();

        @Config.Name(ConstantsConfigLang.CONFIG_EXTERNAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_EXTERNAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_EXTERNAL)
        public ConfigExternal EXTERNAL = new ConfigExternal();

        public class ConfigExternal {

            @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_GRITLESS_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_GRITLESS_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_GRITLESS)
            public ConfigAbilityEnhancedVisualsFireParticles CLEAR_FIRE_PARTICLES = new ConfigAbilityEnhancedVisualsFireParticles();

        }

    }

    /// Elemental Configuration

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS)
    public ElementConfig ELEMENTS = new ElementConfig();

    public class ElementConfig {

        /// Fire
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE)
        public ConfigFireDragon FIRE = new ConfigFireDragon();

        /// Ice
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_ICE)
        public ConfigIceDragon ICE = new ConfigIceDragon();

        /// Lightning
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING)
        public ConfigLightningDragon LIGHTNING = new ConfigLightningDragon();

        /// Air
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_AIR_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_AIR_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_AIR)
        public ConfigAirDragon AIR = new ConfigAirDragon();

        /// Dark
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_DARK_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_DARK_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_DARK)
        public ConfigDarkDragon DARK = new ConfigDarkDragon();

        /// Earth
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH)
        public ConfigEarthDragon EARTH = new ConfigEarthDragon();

        /// Light
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT)
        public ConfigLightDragon LIGHT = new ConfigLightDragon();

        /// Poison
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_POISON_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_POISON_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_POISON)
        public ConfigPoisonDragon POISON = new ConfigPoisonDragon();

        /// Void
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_VOID_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_VOID_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_VOID)
        public ConfigVoidDragon VOID = new ConfigVoidDragon();

        /// Water
        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_WATER_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_WATER_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_WATER)
        public ConfigWaterDragon WATER = new ConfigWaterDragon();

    }

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL)
    public boolean CAN_MOUNT = true;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT)
    public boolean CAN_CONTROL_BOATS = true;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED)
    public boolean MOUNT_WHITELIST = false;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST)
    public String[] MOUNT_BLACKLIST = {};

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_EFFECTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EFFECTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EFFECTS)
    public String[] EFFECTS_TO_ADD = {};

    @Config.Name("07. " + ConstantsConfigLang.CONFIG_RESISTANCES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RESISTANCES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RESISTANCES)
    public String[] EFFECTS_TO_REMOVE = {};

    @Config.Name("08. " + ConstantsConfigLang.CONFIG_DAMAGE_TYPES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DAMAGE_TYPES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DAMAGE_TYPES)
    public String[] DAMAGE_TYPES_TO_IGNORE = {};


    @Config.Name("90. " + ConstantsConfigLang.CONFIG_ATTRIBUTES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ATTRIBUTES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ATTRIBUTES)
    public String[] ATTRIBUTES = {
            //@formatter:off
            "Name:generic.maxHealth, Amount:0.25, Operation:1",
            "Name:generic.attackDamage, Amount:0.5, Operation:1",
            "Name:generic.armorToughness, Amount:0.5, Operation:1",
            "Name:xat.flyspeed, Amount:-0.4, Operation:2"
            //@formatter:on
    };

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC)
    public RaceMagicConfig MAGIC = new RaceMagicConfig(400);

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_SIZE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_SIZE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_SIZE)
    public RaceSizeConfig SIZE = new RaceSizeConfig(120, 120);

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
