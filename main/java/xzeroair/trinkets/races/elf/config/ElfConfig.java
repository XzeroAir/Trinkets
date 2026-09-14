package xzeroair.trinkets.races.elf.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.races.elf.config.elements.*;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilitySkilledArcher;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.config.race.RaceMagicConfig;
import xzeroair.trinkets.util.config.race.RaceSizeConfig;
import xzeroair.trinkets.util.config.race.ConfigRaceEquipmentWeight;

public class ElfConfig {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_RACES_ELF;

    public ElfConfig() {
    }

    /// Default Abilities
    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigAbilities ABILITIES = new ConfigAbilities();

    public class ConfigAbilities {

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_ARCHER_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_ARCHER_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_ARCHER)
        public ConfigAbilitySkilledArcher SKILLED_ARCHER = new ConfigAbilitySkilledArcher();

    }

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL)
    public boolean CAN_MOUNT = true;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT)
    public boolean CAN_CONTROL_BOATS = true;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED)
    public boolean MOUNT_WHITELIST = false;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST)
    public String[] MOUNT_BLACKLIST = {};

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
			"Name:generic.movementSpeed, Amount:0.1, Operation:1",
			"Name:generic.attackSpeed, Amount:0.3, Operation:1"
			//@formatter:on
    };

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC)
    public RaceMagicConfig MAGIC = new RaceMagicConfig(200);

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_SIZE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_SIZE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_SIZE)
    public RaceSizeConfig SIZE = new RaceSizeConfig(100, 100);

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS)
    public ElementsConfig ELEMENTS = new ElementsConfig();

    public class ElementsConfig {

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_AIR_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_AIR_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_AIR)
        public ConfigAirElf AIR = new ConfigAirElf();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_DARK_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_DARK_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_DARK)
        public ConfigDarkElf DARK = new ConfigDarkElf();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH)
        public ConfigEarthElf EARTH = new ConfigEarthElf();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE)
        public ConfigFireElf FIRE = new ConfigFireElf();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_ICE)
        public ConfigIceElf ICE = new ConfigIceElf();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT)
        public ConfigLightElf LIGHT = new ConfigLightElf();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING)
        public ConfigLightningElf LIGHTNING = new ConfigLightningElf();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_POISON_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_POISON_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_POISON)
        public ConfigPoisonElf POISON = new ConfigPoisonElf();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_VOID_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_VOID_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_VOID)
        public ConfigVoidElf VOID = new ConfigVoidElf();

        @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_WATER_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_WATER_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_WATER)
        public ConfigWaterElf WATER = new ConfigWaterElf();
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
