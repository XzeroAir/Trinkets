package xzeroair.trinkets.races.faelis.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityClimbing;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityNightVision;
import xzeroair.trinkets.util.config.compat.ConfigSurvivalCompat;
import xzeroair.trinkets.util.config.race.RaceMagicConfig;
import xzeroair.trinkets.util.config.race.RaceSizeConfig;

public class FaelisConfig {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_RACES_FAELIS;

    public FaelisConfig() {
    }

    /// Default Abilities
    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigAbilities ABILITIES = new ConfigAbilities();

    public class ConfigAbilities {

        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING_COMMENT)
        @Name(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING_NAME)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING)
        public ConfigAbilityClimbing CLIMBING = new ConfigAbilityClimbing();

        @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NIGHT_VISION_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_NIGHT_VISION_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_NIGHT_VISION)
        public ConfigAbilityNightVision NIGHT_VISION = new ConfigAbilityNightVision(false);

    }

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_BARE_HAND_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_BARE_HAND_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_BARE_HAND)
    public boolean BAREHAND_COMBAT = true;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_BARE_HAND_BONUS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_BARE_HAND_BONUS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_BARE_HAND_BONUS)
    public double BAREHAND_COMBAT_BONUS = 3.25;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_BARE_HAND_LIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_BARE_HAND_LIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_BARE_HAND_LIST)
    public String[] BARE_HANDS = {"minecraft:dummy_item;*;0.01"};

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_BUFF_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_BUFF_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_BUFF)
    public boolean MILK_BONUS = true;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_DURATION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_DURATION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_DURATION)
    public int MILK_BONUS_DURATION = 600;

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_LIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_LIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_LIST)
    public String[] MILK = {
            //@formatter:off
            "minecraft:milk_bucket"
            //@formatter:on
    };

    @Config.Name("07. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_BUFF_LIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_BUFF_LIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_MILK_BUFF_LIST)
    public String[] MILK_BUFFS = {
            //@formatter:off
            "minecraft:speed:3600:0",
            "minecraft:strength:3600:0",
            "minecraft:jump_boost:3600:0"
            //@formatter:on
    };

    @Config.Name("08. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_HEAVY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_HEAVY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_HEAVY)
    public boolean HEAVY_ARMOR_PENALTY = true;

    @Config.Name("09. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_HEAVY_INVIGORATED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_HEAVY_INVIGORATED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_HEAVY_INVIGORATED)
    public boolean MILK_INVIGORATED = true;

    @Config.Name("10. " + ConstantsConfigLang.CONFIG_RACES_FAELIS_HEAVY_LIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_HEAVY_LIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS_HEAVY_LIST)
    public String[] HEAVY_ARMOR = {
            //@formatter:off
            "minecraft:chainmail_helmet;*;0.01",
            "minecraft:chainmail_chestplate;*;0.09",
            "minecraft:chainmail_leggings;*;0.075",
            "minecraft:chainmail_boots;*;0.025",
            "Iron;head;0.025",
            "Iron;chest;0.15",
            "Iron;legs;0.075",
            "Iron;feet;0.05",
            "minecraft:golden_helmet;*;0.04",
            "minecraft:golden_chestplate;*;0.2",
            "minecraft:golden_leggings;*;0.1",
            "minecraft:golden_boots;*;0.06",
            "Diamond;0.075",
            //@formatter:on
    };

    @Config.Name("11. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL)
    public boolean CAN_MOUNT = true;

    @Config.Name("12. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BOAT)
    public boolean CAN_CONTROL_BOATS = true;

    @Config.Name("13. " + ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLACKLIST_INVERTED)
    public boolean MOUNT_WHITELIST = false;

    @Config.Name("14. " + ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MOUNT_CONTROL_BLACKLIST)
    public String[] MOUNT_BLACKLIST = {};

    @Config.Name("15. " + ConstantsConfigLang.CONFIG_EFFECTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EFFECTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EFFECTS)
    public String[] EFFECTS_TO_ADD = {};

    @Config.Name("16. " + ConstantsConfigLang.CONFIG_RESISTANCES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RESISTANCES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RESISTANCES)
    public String[] EFFECTS_TO_REMOVE = {};

    @Config.Name("17. " + ConstantsConfigLang.CONFIG_DAMAGE_TYPES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DAMAGE_TYPES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DAMAGE_TYPES)
    public String[] DAMAGE_TYPES_TO_IGNORE = {};

    @Config.Name("90. " + ConstantsConfigLang.CONFIG_ATTRIBUTES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ATTRIBUTES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ATTRIBUTES)
    public String[] ATTRIBUTES = {
            //@formatter:off
            "Name:generic.maxHealth, Amount:-0.25, Operation:1",
            "Name:generic.movementSpeed, Amount:0.15, Operation:1",
            "Name:generic.attackDamage, Amount:-0.25, Operation:1",
            "Name:generic.attackSpeed, Amount:0.15, Operation:2",
            "Name:generic.armorToughness, Amount:-0.15, Operation:2",
            "Name:generic.luck, Amount:1, Operation:0",
            "Name:generic.reachDistance, Amount:-0.1, Operation:1",
            "Name:forge.swimSpeed, Amount:0.3, Operation:1",
            "Name:xat.jump, Amount:0.6, Operation:1",
            "Name:xat.stepheight, Amount:0.6, Operation:0"
            //@formatter:on
    };

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC)
    public RaceMagicConfig MAGIC = new RaceMagicConfig(125);

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_SIZE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_SIZE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_SIZE)
    public RaceSizeConfig SIZE = new RaceSizeConfig(85, 85);

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
