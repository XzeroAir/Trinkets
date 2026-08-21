package xzeroair.trinkets.util;

public class ConstantsConfigLang {

    public static final ConstantsConfigLang INSTANCE = new ConstantsConfigLang();

    public ConstantsConfigLang() {
    }

    public static final String CONFIG = Reference.MODID + ".config";

    public static final String CONFIG_COMPAT = CONFIG + ".compat";
    public static final String CONFIG_COMPAT_NAME = "Compatibility";
    public static final String CONFIG_COMPAT_COMMENT = "";
    public static final String CONFIG_COMPAT_SETTINGS = CONFIG_COMPAT + ".settings";
    public static final String CONFIG_COMPAT_SETTINGS_NAME = "Compatibility Settings";
    public static final String CONFIG_COMPAT_SETTINGS_COMMENT = "";

    public static final String CONFIG_EXTERNAL = CONFIG + ".external";
    public static final String CONFIG_EXTERNAL_NAME = "External";
    public static final String CONFIG_EXTERNAL_COMMENT = "";

    public static final String CONFIG_CLIENT = CONFIG + ".client";
    public static final String CONFIG_CLIENT_NAME = "Client Configuration";
    public static final String CONFIG_CLIENT_COMMENT = "";
    public static final String CONFIG_CLIENT_SETTINGS = CONFIG_CLIENT + ".settings";
    public static final String CONFIG_CLIENT_SETTINGS_NAME = "Client Settings";
    public static final String CONFIG_CLIENT_SETTINGS_COMMENT = "";

    public static final String CONFIG_SERVER = CONFIG + ".server";
    public static final String CONFIG_SERVER_NAME = "Server Configuration";
    public static final String CONFIG_SERVER_COMMENT = "";
    public static final String CONFIG_SERVER_SETTINGS = CONFIG_SERVER + ".settings";
    public static final String CONFIG_SERVER_SETTINGS_NAME = "Server Settings";
    public static final String CONFIG_SERVER_SETTINGS_COMMENT = "";

    public static final String CONFIG_EXPERIMENTAL_MIXINS = CONFIG + ".experimental_mixins";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_NAME = "Experimental Mixins";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_COMMENT = "Optional Mixin-based behavior changes. All options require a game restart.";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_VANILLA_PLAYER_SIZE_UPDATES = CONFIG_EXPERIMENTAL_MIXINS + ".vanilla_player_size_updates";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_VANILLA_PLAYER_SIZE_UPDATES_NAME = "Vanilla Player Size Updates";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_VANILLA_PLAYER_SIZE_UPDATES_COMMENT = "Let vanilla update player dimensions using Trinkets' race-adjusted width and height, including collision and bounding boxes. Requires restart.";

    public static final String CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS = CONFIG_EXPERIMENTAL_MIXINS + ".race_scaled_player_shadows";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS_NAME = "Race-Scaled Player Shadows";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS_COMMENT = "Scale player shadows to the current race-adjusted width. Requires restart.";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_CUSTOM_GLIDE_BODY_POSE = CONFIG_EXPERIMENTAL_MIXINS + ".custom_glide_body_pose";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_CUSTOM_GLIDE_BODY_POSE_NAME = "Custom Glide Body Pose";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_CUSTOM_GLIDE_BODY_POSE_COMMENT = "Use vanilla's Elytra body pose while Trinkets custom gliding. Requires restart.";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_FREEZE_LIMBS_WHILE_GLIDING = CONFIG_EXPERIMENTAL_MIXINS + ".freeze_limbs_while_gliding";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_FREEZE_LIMBS_WHILE_GLIDING_NAME = "Freeze Limbs While Gliding";
    public static final String CONFIG_EXPERIMENTAL_MIXINS_FREEZE_LIMBS_WHILE_GLIDING_COMMENT = "Stop arm and leg walking animations while Trinkets custom gliding. Requires Custom Glide Body Pose. Requires restart.";

    public static final String REGISTRY = CONFIG + ".registry";
    public static final String REGISTRY_ENABLED = REGISTRY + ".enabled";
    public static final String REGISTRY_ENABLED_NAME = "Enabled";
    public static final String REGISTRY_ENABLED_COMMENT = "Should this be enabled";

    public static final String CONFIG_BUTTON = CONFIG + ".button";
    public static final String CONFIG_BUTTON_NAME = "Button";
    public static final String CONFIG_BUTTON_COMMENT = "Button Configuration";
    public static final String CONFIG_BUTTON_ENABLED = CONFIG_BUTTON + ".enabled";
    public static final String CONFIG_BUTTON_ENABLED_NAME = "Button Enabled";
    public static final String CONFIG_BUTTON_ENABLED_COMMENT = "";
    public static final String CONFIG_BUTTON_ID = CONFIG_BUTTON + ".id";
    public static final String CONFIG_BUTTON_ID_NAME = "Button ID";
    public static final String CONFIG_BUTTON_ID_COMMENT = "Button ID, used for potential gui conflicts, though doubtful";

    public static final String CONFIG_WHITELIST = CONFIG + ".whitelist";
    public static final String CONFIG_WHITELIST_NAME = "Whitelist";
    public static final String CONFIG_WHITELIST_COMMENT = "";
    public static final String CONFIG_WHITELIST_INVERTED = CONFIG_WHITELIST + ".inverted";
    public static final String CONFIG_WHITELIST_INVERTED_NAME = "Invert Whitelist";
    public static final String CONFIG_WHITELIST_INVERTED_COMMENT = "";
    public static final String CONFIG_BLACKLIST = CONFIG + ".blacklist";
    public static final String CONFIG_BLACKLIST_NAME = "Blacklist";
    public static final String CONFIG_BLACKLIST_COMMENT = "";
    public static final String CONFIG_BLACKLIST_INVERTED = CONFIG_BLACKLIST + ".inverted";
    public static final String CONFIG_BLACKLIST_INVERTED_NAME = "Invert Blacklist";
    public static final String CONFIG_BLACKLIST_INVERTED_COMMENT = "";
    public static final String CONFIG_COOLDOWN = CONFIG + ".cooldown";
    public static final String CONFIG_COOLDOWN_NAME = "Cooldown";
    public static final String CONFIG_COOLDOWN_COMMENT = "";
    public static final String CONFIG_AMOUNT = CONFIG + ".amount";
    public static final String CONFIG_AMOUNT_NAME = "Amount";
    public static final String CONFIG_AMOUNT_COMMENT = "";

    public static final String CONFIG_MULTIPLIER = CONFIG + ".multi";
    public static final String CONFIG_MULTIPLIER_NAME = "Multiplier";
    public static final String CONFIG_MULTIPLIER_COMMENT = "Multiplier Value";

    public static final String CONFIG_VELOCITY = CONFIG + ".velocity";
    public static final String CONFIG_VELOCITY_NAME = "Velocity";
    public static final String CONFIG_VELOCITY_COMMENT = "Velocity Configuration";
    public static final String CONFIG_VELOCITY_MIN = CONFIG_VELOCITY + ".min";
    public static final String CONFIG_VELOCITY_MIN_NAME = "Minimum Velocity";
    public static final String CONFIG_VELOCITY_MIN_COMMENT = "Minimum Velocity";
    public static final String CONFIG_VELOCITY_MAX = CONFIG_VELOCITY + ".max";
    public static final String CONFIG_VELOCITY_MAX_NAME = "Maximum Velocity";
    public static final String CONFIG_VELOCITY_MAX_COMMENT = "Maximum Velocity";

    public static final String CONFIG_CHARGE_TIME = CONFIG + ".charge";
    public static final String CONFIG_CHARGE_TIME_NAME = "Charge Time";
    public static final String CONFIG_CHARGE_TIME_COMMENT = "Time required for a full charge";
    public static final String CONFIG_DAMAGE = CONFIG + ".damage";
    public static final String CONFIG_DAMAGE_NAME = "Damage";
    public static final String CONFIG_DAMAGE_COMMENT = "Damage Dealt";
    public static final String CONFIG_DAMAGE_MIN = CONFIG_DAMAGE + ".min";
    public static final String CONFIG_DAMAGE_MIN_NAME = "Damage Dealt Min";
    public static final String CONFIG_DAMAGE_MIN_COMMENT = "Minimum Damage Dealt";
    public static final String CONFIG_DAMAGE_MAX = CONFIG_DAMAGE + ".max";
    public static final String CONFIG_DAMAGE_MAX_NAME = "Damage Dealt Max";
    public static final String CONFIG_DAMAGE_MAX_COMMENT = "Maximum Damage Dealt";
    public static final String CONFIG_DAMAGE_TAKEN = CONFIG_DAMAGE + ".taken";
    public static final String CONFIG_DAMAGE_TAKEN_NAME = "Damage Received";
    public static final String CONFIG_DAMAGE_TAKEN_COMMENT = "Damage Received";
    public static final String CONFIG_DAMAGE_TAKEN_MIN = CONFIG_DAMAGE_TAKEN + ".min";
    public static final String CONFIG_DAMAGE_TAKEN_MIN_NAME = "Damage Received Min";
    public static final String CONFIG_DAMAGE_TAKEN_MIN_COMMENT = "Minimum Damage Received";
    public static final String CONFIG_DAMAGE_TAKEN_MAX = CONFIG_DAMAGE_TAKEN + ".max";
    public static final String CONFIG_DAMAGE_TAKEN_MAX_NAME = "Damage Received Max";
    public static final String CONFIG_DAMAGE_TAKEN_MAX_COMMENT = "Maximum Damage Received";

    public static final String CONFIG_FREQUENCY = CONFIG + ".frequency";
    public static final String CONFIG_FREQUENCY_NAME = "Frequency";
    public static final String CONFIG_FREQUENCY_COMMENT = "How often in ticks to run code";
    public static final String CONFIG_FORCE = CONFIG + ".force";
    public static final String CONFIG_FORCE_NAME = "Force";
    public static final String CONFIG_FORCE_COMMENT = "";
    public static final String CONFIG_WEIGHT = CONFIG + ".weight";
    public static final String CONFIG_WEIGHT_NAME = "Weight";
    public static final String CONFIG_WEIGHT_COMMENT = "";
    public static final String CONFIG_WIDTH = CONFIG + ".width";
    public static final String CONFIG_WIDTH_NAME = "Width";
    public static final String CONFIG_WIDTH_COMMENT = "";
    public static final String CONFIG_HEIGHT = CONFIG + ".height";
    public static final String CONFIG_HEIGHT_NAME = "Height";
    public static final String CONFIG_HEIGHT_COMMENT = "";
    public static final String CONFIG_HORIZONTAL = CONFIG + ".horizontal";
    public static final String CONFIG_HORIZONTAL_NAME = "Horizontal";
    public static final String CONFIG_HORIZONTAL_COMMENT = "";
    public static final String CONFIG_VERTICAL = CONFIG + ".vertical";
    public static final String CONFIG_VERTICAL_NAME = "Vertical";
    public static final String CONFIG_VERTICAL_COMMENT = "";
    public static final String CONFIG_DEPTH = CONFIG + ".depth";
    public static final String CONFIG_DEPTH_NAME = "Depth";
    public static final String CONFIG_DEPTH_COMMENT = "";
    public static final String CONFIG_RANGE = CONFIG + ".range";
    public static final String CONFIG_RANGE_NAME = "Range";
    public static final String CONFIG_RANGE_COMMENT = "WARNING! SETTING THESE VALUES TOO HIGH WILL CAUSE YOU TO LAG. Try to Keep within a range of 4-16";
    public static final String CONFIG_RANGE_HORIZONTAL = CONFIG_RANGE + ".horizontal";
    public static final String CONFIG_RANGE_HORIZONTAL_NAME = "Horizontal Distance";
    public static final String CONFIG_RANGE_HORIZONTAL_COMMENT = "How Far Horizontally(N, E, S, W) in Blocks";
    public static final String CONFIG_RANGE_VERTICAL = CONFIG_RANGE + ".vertical";
    public static final String CONFIG_RANGE_VERTICAL_NAME = "Vertical Distance";
    public static final String CONFIG_RANGE_VERTICAL_COMMENT = "How Far Vertically(Up, Down) in Blocks";
    public static final String CONFIG_CAMERA_ADJUSTMENTS = CONFIG + ".camera.adjustments";
    public static final String CONFIG_CAMERA_ADJUSTMENTS_NAME = "Camera POV Adjustments";
    public static final String CONFIG_CAMERA_ADJUSTMENTS_COMMENT = "";
    public static final String CONFIG_VOLUME_CONTROL = CONFIG + ".volume";
    public static final String CONFIG_VOLUME_CONTROL_NAME = "Volume";
    public static final String CONFIG_VOLUME_CONTROL_COMMENT = "";
    public static final String CONFIG_VOLUME_CONTROL_PITCH = CONFIG_VOLUME_CONTROL + ".pitch";
    public static final String CONFIG_VOLUME_CONTROL_PITCH_NAME = "Pitch";
    public static final String CONFIG_VOLUME_CONTROL_PITCH_COMMENT = "";
    public static final String CONFIG_RENDERING = CONFIG + ".render";
    public static final String CONFIG_RENDERING_NAME = "Rendering";
    public static final String CONFIG_RENDERING_COMMENT = "";
    public static final String CONFIG_RENDERING_MAIN = CONFIG_RENDERING + ".main";
    public static final String CONFIG_RENDERING_MAIN_NAME = "Rendering Main";
    public static final String CONFIG_RENDERING_MAIN_COMMENT = "Main Rendering Controller";
    public static final String CONFIG_RENDERING_HELMET = CONFIG_RENDERING + ".helmet";
    public static final String CONFIG_RENDERING_HELMET_NAME = "Render Helmet";
    public static final String CONFIG_RENDERING_HELMET_COMMENT = "";

    public static final String CONFIG_LOCATION = CONFIG + ".location";
    public static final String CONFIG_LOCATION_NAME = "Location";
    public static final String CONFIG_LOCATION_COMMENT = "";
    public static final String CONFIG_LOCATION_X = CONFIG_LOCATION + ".x";
    public static final String CONFIG_LOCATION_X_NAME = "Location X";
    public static final String CONFIG_LOCATION_X_COMMENT = "";
    public static final String CONFIG_LOCATION_Y = CONFIG_LOCATION + ".y";
    public static final String CONFIG_LOCATION_Y_NAME = "Location Y";
    public static final String CONFIG_LOCATION_Y_COMMENT = "";
    public static final String CONFIG_LOCATION_Z = CONFIG_LOCATION + ".z";
    public static final String CONFIG_LOCATION_Z_NAME = "Location Z";
    public static final String CONFIG_LOCATION_Z_COMMENT = "";

    public static final String CONFIG_OFFSET_X = CONFIG + ".offset.x";
    public static final String CONFIG_OFFSET_X_NAME = "Offset X";
    public static final String CONFIG_OFFSET_X_COMMENT = "";
    public static final String CONFIG_OFFSET_Y = CONFIG + ".offset.y";
    public static final String CONFIG_OFFSET_Y_NAME = "Offset Y";
    public static final String CONFIG_OFFSET_Y_COMMENT = "";
    public static final String CONFIG_OFFSET_Z = CONFIG + ".offset.z";
    public static final String CONFIG_OFFSET_Z_NAME = "Offset Z";
    public static final String CONFIG_OFFSET_Z_COMMENT = "";

    public static final String CONFIG_TEXTURE = CONFIG + ".texture";
    public static final String CONFIG_TEXTURE_NAME = "Texture";
    public static final String CONFIG_TEXTURE_COMMENT = "";
    public static final String CONFIG_TEXTURE_ATLAS = CONFIG_TEXTURE + ".atlas";
    public static final String CONFIG_TEXTURE_ATLAS_NAME = "Atlas Size";
    public static final String CONFIG_TEXTURE_ATLAS_COMMENT = "";
    public static final String CONFIG_TEXTURE_ATLAS_WIDTH = CONFIG_TEXTURE_ATLAS + ".width";
    public static final String CONFIG_TEXTURE_ATLAS_WIDTH_NAME = "Atlas Width";
    public static final String CONFIG_TEXTURE_ATLAS_WIDTH_COMMENT = "";
    public static final String CONFIG_TEXTURE_ATLAS_HEIGHT = CONFIG_TEXTURE_ATLAS + ".height";
    public static final String CONFIG_TEXTURE_ATLAS_HEIGHT_NAME = "Atlas Height";
    public static final String CONFIG_TEXTURE_ATLAS_HEIGHT_COMMENT = "";
    public static final String CONFIG_TEXTURE_WIDTH = CONFIG_TEXTURE + ".width";
    public static final String CONFIG_TEXTURE_WIDTH_NAME = "Texture Width";
    public static final String CONFIG_TEXTURE_WIDTH_COMMENT = "";
    public static final String CONFIG_TEXTURE_HEIGHT = CONFIG_TEXTURE + ".height";
    public static final String CONFIG_TEXTURE_HEIGHT_NAME = "Texture Height";
    public static final String CONFIG_TEXTURE_HEIGHT_COMMENT = "";


    /**
     * xat.config.container
     */
    public static final String CONFIG_CONTAINER = CONFIG + ".container";
    public static final String CONFIG_CONTAINER_NAME = "Trinkets Container";
    public static final String CONFIG_CONTAINER_COMMENT = "";
    public static final String CONFIG_CONTAINER_ENABLED = CONFIG_CONTAINER + ".enabled";
    public static final String CONFIG_CONTAINER_ENABLED_NAME = "Enable T&B Container";
    public static final String CONFIG_CONTAINER_ENABLED_COMMENT = "";
    public static final String CONFIG_CONTAINER_SLOTS = CONFIG_CONTAINER + ".slots";
    public static final String CONFIG_CONTAINER_SLOTS_NAME = "T&B Container Slots";
    public static final String CONFIG_CONTAINER_SLOTS_COMMENT = "How many slots should the Trinkets container have";
    public static final String CONFIG_CONTAINER_TRINKETS_ONLY = CONFIG_CONTAINER + ".limited";
    public static final String CONFIG_CONTAINER_TRINKETS_ONLY_NAME = "Accessories Use T&B Container Only";
    public static final String CONFIG_CONTAINER_TRINKETS_ONLY_COMMENT = "Should Trinkets & Baubles Items only work in the T&B Container";
    public static final String CONFIG_CONTAINER_POTIONS = CONFIG_CONTAINER + ".potions";
    public static final String CONFIG_CONTAINER_POTIONS_NAME = "Show Potion Icons";
    public static final String CONFIG_CONTAINER_POTIONS_COMMENT = "Show Active Potion Effects while in the Trinkets Gui";
    public static final String CONFIG_COLOR_DECIMAL = CONFIG + ".color";
    public static final String CONFIG_COLOR_DECIMAL_NAME = "Decimal Color";
    public static final String CONFIG_COLOR_DECIMAL_COMMENT = "Decimal value for a color";
    public static final String CONFIG_COLOR_HEX = CONFIG_COLOR_DECIMAL + ".hex";
    public static final String CONFIG_COLOR_HEX_NAME = "Hex Color";
    public static final String CONFIG_COLOR_HEX_COMMENT = "Hex Color Followed by Alpha float value";

    /**
     * xat.config.elements
     */
    public static final String CONFIG_ELEMENTS = CONFIG + ".elements";
    public static final String CONFIG_ELEMENTS_NAME = "Elements";
    public static final String CONFIG_ELEMENTS_COMMENT = "Configuration for Elemental Specific Changes";

    public static final String CONFIG_ELEMENTS_VOID = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.VOID;
    public static final String CONFIG_ELEMENTS_VOID_NAME = "Void";
    public static final String CONFIG_ELEMENTS_VOID_COMMENT = "";
    public static final String CONFIG_ELEMENTS_VOID_DAMAGE = CONFIG_ELEMENTS_VOID + ".damage";
    public static final String CONFIG_ELEMENTS_VOID_DAMAGE_NAME = "Void";
    public static final String CONFIG_ELEMENTS_VOID_DAMAGE_COMMENT = "";

    public static final String CONFIG_ELEMENTS_POISON = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.POISON;
    public static final String CONFIG_ELEMENTS_POISON_NAME = "Poison";
    public static final String CONFIG_ELEMENTS_POISON_COMMENT = "";
    public static final String CONFIG_ELEMENTS_POISON_DAMAGE = CONFIG_ELEMENTS_POISON + ".damage";
    public static final String CONFIG_ELEMENTS_POISON_DAMAGE_NAME = "Poison";
    public static final String CONFIG_ELEMENTS_POISON_DAMAGE_COMMENT = "";

    public static final String CONFIG_ELEMENTS_LIGHT = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.LIGHT;
    public static final String CONFIG_ELEMENTS_LIGHT_NAME = "Light";
    public static final String CONFIG_ELEMENTS_LIGHT_COMMENT = "";
    public static final String CONFIG_ELEMENTS_LIGHT_DAMAGE = CONFIG_ELEMENTS_LIGHT + ".damage";
    public static final String CONFIG_ELEMENTS_LIGHT_DAMAGE_NAME = "Light";
    public static final String CONFIG_ELEMENTS_LIGHT_DAMAGE_COMMENT = "";

    public static final String CONFIG_ELEMENTS_DARK = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.DARK;
    public static final String CONFIG_ELEMENTS_DARK_NAME = "Dark";
    public static final String CONFIG_ELEMENTS_DARK_COMMENT = "";
    public static final String CONFIG_ELEMENTS_DARK_DAMAGE = CONFIG_ELEMENTS_DARK + ".damage";
    public static final String CONFIG_ELEMENTS_DARK_DAMAGE_NAME = "Dark";
    public static final String CONFIG_ELEMENTS_DARK_DAMAGE_COMMENT = "";

    public static final String CONFIG_ELEMENTS_EARTH = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.EARTH;
    public static final String CONFIG_ELEMENTS_EARTH_NAME = "Earth";
    public static final String CONFIG_ELEMENTS_EARTH_COMMENT = "";
    public static final String CONFIG_ELEMENTS_EARTH_DAMAGE = CONFIG_ELEMENTS_EARTH + ".damage";
    public static final String CONFIG_ELEMENTS_EARTH_DAMAGE_NAME = "Earth";
    public static final String CONFIG_ELEMENTS_EARTH_DAMAGE_COMMENT = "";

    public static final String CONFIG_ELEMENTS_WATER = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.WATER;
    public static final String CONFIG_ELEMENTS_WATER_NAME = "Water";
    public static final String CONFIG_ELEMENTS_WATER_COMMENT = "";
    public static final String CONFIG_ELEMENTS_WATER_DAMAGE = CONFIG_ELEMENTS_WATER + ".damage";
    public static final String CONFIG_ELEMENTS_WATER_DAMAGE_NAME = "Water";
    public static final String CONFIG_ELEMENTS_WATER_DAMAGE_COMMENT = "";

    public static final String CONFIG_ELEMENTS_AIR = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.AIR;
    public static final String CONFIG_ELEMENTS_AIR_NAME = "Air";
    public static final String CONFIG_ELEMENTS_AIR_COMMENT = "";
    public static final String CONFIG_ELEMENTS_AIR_DAMAGE = CONFIG_ELEMENTS_AIR + ".damage";
    public static final String CONFIG_ELEMENTS_AIR_DAMAGE_NAME = "Air";
    public static final String CONFIG_ELEMENTS_AIR_DAMAGE_COMMENT = "";

    public static final String CONFIG_ELEMENTS_FIRE = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.FIRE;
    public static final String CONFIG_ELEMENTS_FIRE_NAME = "Fire";
    public static final String CONFIG_ELEMENTS_FIRE_COMMENT = "";
    public static final String CONFIG_ELEMENTS_FIRE_DAMAGE = CONFIG_ELEMENTS_FIRE + ".damage";
    public static final String CONFIG_ELEMENTS_FIRE_DAMAGE_NAME = "Fire";
    public static final String CONFIG_ELEMENTS_FIRE_DAMAGE_COMMENT = "";

    public static final String CONFIG_ELEMENTS_ICE = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.ICE;
    public static final String CONFIG_ELEMENTS_ICE_NAME = "Ice";
    public static final String CONFIG_ELEMENTS_ICE_COMMENT = "";
    public static final String CONFIG_ELEMENTS_ICE_DAMAGE = CONFIG_ELEMENTS_ICE + ".damage";
    public static final String CONFIG_ELEMENTS_ICE_DAMAGE_NAME = "Ice";
    public static final String CONFIG_ELEMENTS_ICE_DAMAGE_COMMENT = "";

    public static final String CONFIG_ELEMENTS_LIGHTNING = CONFIG_ELEMENTS + "." + TrinketsRegistryNames.ModElements.LIGHTNING;
    public static final String CONFIG_ELEMENTS_LIGHTNING_NAME = "Lightning";
    public static final String CONFIG_ELEMENTS_LIGHTNING_COMMENT = "";
    public static final String CONFIG_ELEMENTS_LIGHTNING_DAMAGE = CONFIG_ELEMENTS_LIGHTNING + ".damage";
    public static final String CONFIG_ELEMENTS_LIGHTNING_DAMAGE_NAME = "Lightning";
    public static final String CONFIG_ELEMENTS_LIGHTNING_DAMAGE_COMMENT = "";

    /**
     * xat.config.abilities
     */
    public static final String CONFIG_ABILITIES = CONFIG + ".abilities";
    public static final String CONFIG_ABILITIES_NAME = "Abilities";
    public static final String CONFIG_ABILITIES_COMMENT = "";

    public static final String CONFIG_ABILITIES_SKILLED_ARCHER = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SKILLED_ARCHER;
    public static final String CONFIG_ABILITIES_SKILLED_ARCHER_NAME = "Skilled Archer";
    public static final String CONFIG_ABILITIES_SKILLED_ARCHER_COMMENT = "";

    public static final String CONFIG_ABILITIES_SKILLED_MINER = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SKILLED_MINER;
    public static final String CONFIG_ABILITIES_SKILLED_MINER_NAME = "Skilled Miner";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_FORTUNE = CONFIG_ABILITIES_SKILLED_MINER + ".fortune";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_NAME = "Natural Fortune";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_STACKS = CONFIG_ABILITIES_SKILLED_MINER_FORTUNE + ".stacks";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_STACKS_NAME = "Fortune Stacks";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_STACKS_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_WHITELIST = CONFIG_ABILITIES_SKILLED_MINER_FORTUNE + ".whitelist";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_WHITELIST_NAME = "Fortune Whitelist";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_WHITELIST_COMMENT = "Blocks that Natural Fortune works on";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_REDUCED_REQUIREMENT = CONFIG_ABILITIES_SKILLED_MINER + ".pick";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_REDUCED_REQUIREMENT_NAME = "Reduced Requirement";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_REDUCED_REQUIREMENT_COMMENT = "Should the Dwarves lower the mining level requirement for pickaxes. IE. an Iron Pickaxe will be able to break Obsidian";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_STATIC_MINING = CONFIG_ABILITIES_SKILLED_MINER + ".speed";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_STATIC_MINING_NAME = "Static Speed";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_STATIC_MINING_COMMENT = "Mining Speed is static at (Block Hardness * 4), Not Including other Modifiers";

    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS = CONFIG_ABILITIES_SKILLED_MINER + ".xp";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_NAME = "Mining XP";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MIN = CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS + ".min";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MIN_NAME = "Min Bonus";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MIN_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MAX = CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS + ".max";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MAX_NAME = "Max Bonus";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MAX_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_WHITELIST = CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS + ".whitelist";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_WHITELIST_NAME = "Mining XP Whitelist";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_WHITELIST_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN = CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS + ".alt.min";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_NAME = "Minimal XP Gain";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_COMMENT = "Should Some Blocks give at least 1 XP";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_WHITELIST = CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN + ".whitelist";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_WHITELIST_NAME = "Minimal XP Gain Whitelist";
    public static final String CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_WHITELIST_COMMENT = "Blocks in this List will always give 1 xp when broken";

    public static final String CONFIG_ABILITIES_NIGHT_VISION = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.NIGHT_VISION;
    public static final String CONFIG_ABILITIES_NIGHT_VISION_NAME = "Night Vision";
    public static final String CONFIG_ABILITIES_NIGHT_VISION_COMMENT = "";

    public static final String CONFIG_ABILITIES_CREATIVE_FLIGHT = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.CREATIVE_FLIGHT;
    public static final String CONFIG_ABILITIES_CREATIVE_FLIGHT_NAME = "Creative Flight";
    public static final String CONFIG_ABILITIES_CREATIVE_FLIGHT_COMMENT = "";

    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.ELYTRA_FLIGHT;
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_NAME = "Elytra Flight";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_COMMENT = "Allows Elytra-style gliding without wearing an Elytra";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_ENABLED = CONFIG_ABILITIES_ELYTRA_FLIGHT + ".lift_enabled";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_ENABLED_NAME = "Enable Lift";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_ENABLED_COMMENT = "Allows pressing jump while gliding for a burst of altitude at the cost of Mana. Disable to mimic vanilla Elytra gliding.";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_COLLISION_DAMAGE = CONFIG_ABILITIES_ELYTRA_FLIGHT + ".collision_damage";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_COLLISION_DAMAGE_NAME = "Enable Collision Damage";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_COLLISION_DAMAGE_COMMENT = "Applies vanilla-style wall collision damage while custom Elytra flight is active. Disable when another mod handles collision damage.";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_COST = CONFIG_ABILITIES_ELYTRA_FLIGHT + ".lift_cost";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_COST_NAME = "Lift Mana Cost";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_COST_COMMENT = "Mana cost per lift pulse";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_STRENGTH = CONFIG_ABILITIES_ELYTRA_FLIGHT + ".lift_strength";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_STRENGTH_NAME = "Lift Strength";
    public static final String CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_STRENGTH_COMMENT = "Upward velocity added by each lift pulse during Elytra-style gliding";

    public static final String CONFIG_ABILITIES_GREEDY_EYES = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.GREEDY_EYES;
    public static final String CONFIG_ABILITIES_GREEDY_EYES_NAME = "Greedy Eyes";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_COMMENT = "";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_BLOCKS = CONFIG_ABILITIES_GREEDY_EYES + ".blocks";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_NAME = "Treasure";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_COMMENT = "List of Items and Entities that Count as Treasure";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_CLOSEST = CONFIG_ABILITIES_GREEDY_EYES_BLOCKS + ".closest";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_CLOSEST_NAME = "Find Closest";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_CLOSEST_COMMENT = "Find the Closest Block to the player. False to Compile a list of all nearby";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_CLIENT_PARTICLES = CONFIG_ABILITIES_GREEDY_EYES + ".client.particles";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_CLIENT_PARTICLES_NAME = "Particle Count";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_CLIENT_PARTICLES_COMMENT = "The Maximum amount of Particles to generate at one time";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_CLIENT_GROWL = CONFIG_ABILITIES_GREEDY_EYES + ".client.growl";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_CLIENT_GROWL_NAME = "Dragon's Growl";
    public static final String CONFIG_ABILITIES_GREEDY_EYES_CLIENT_GROWL_COMMENT = "The Maximum amount of Particles to generate at one time";

    public static final String CONFIG_ABILITIES_CLIMBING = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.CLIMBING;
    public static final String CONFIG_ABILITIES_CLIMBING_NAME = "Climbing";
    public static final String CONFIG_ABILITIES_CLIMBING_COMMENT = "";
    public static final String CONFIG_ABILITIES_CLIMBING_BLOCKS = CONFIG_ABILITIES_CLIMBING + ".blocks";
    public static final String CONFIG_ABILITIES_CLIMBING_BLOCKS_NAME = "Climbable Blocks";
    public static final String CONFIG_ABILITIES_CLIMBING_BLOCKS_COMMENT = "List of Blocks that are able to be climbed";

    public static final String CONFIG_ABILITIES_RESTORATION_FIELD = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.RESTORATION_FIELD;
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_NAME = "Mending Bloom";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COMMENT = "Casts Mending Bloom at the targeted location";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_RADIUS = CONFIG_ABILITIES_RESTORATION_FIELD + ".radius";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_RADIUS_NAME = "Radius";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_RADIUS_COMMENT = "Horizontal radius of Mending Bloom in blocks";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_VERTICAL_RADIUS = CONFIG_ABILITIES_RESTORATION_FIELD + ".vertical_radius";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_VERTICAL_RADIUS_NAME = "Vertical Radius";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_VERTICAL_RADIUS_COMMENT = "Vertical radius above and below the Mending Bloom center";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_WAIT_TIME = CONFIG_ABILITIES_RESTORATION_FIELD + ".wait_time";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_WAIT_TIME_NAME = "Wait Time";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_WAIT_TIME_COMMENT = "Delay in ticks before Mending Bloom starts pulsing";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_PULSE_INTERVAL = CONFIG_ABILITIES_RESTORATION_FIELD + ".pulse_interval";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_PULSE_INTERVAL_NAME = "Pulse Interval";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_PULSE_INTERVAL_COMMENT = "How often Mending Bloom checks entities and blocks, in ticks";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_REAPPLICATION_DELAY = CONFIG_ABILITIES_RESTORATION_FIELD + ".reapplication_delay";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_REAPPLICATION_DELAY_NAME = "Reapplication Delay";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_REAPPLICATION_DELAY_COMMENT = "Minimum ticks before Mending Bloom can affect the same entity, item, or block again";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_REPAIR_AMOUNT = CONFIG_ABILITIES_RESTORATION_FIELD + ".repair_amount";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_REPAIR_AMOUNT_NAME = "Repair Amount";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_REPAIR_AMOUNT_COMMENT = "Durability repaired from dropped damaged item entities when affected by a Mending Bloom pulse. Set to 0 to disable repair";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_GROWTH_ATTEMPTS = CONFIG_ABILITIES_RESTORATION_FIELD + ".growth_attempts";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_GROWTH_ATTEMPTS_NAME = "Growth Attempts Per Pulse";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_GROWTH_ATTEMPTS_COMMENT = "Maximum nearby blocks Mending Bloom may grow each pulse. Set to 0 to disable growth";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_CAST_RANGE = CONFIG_ABILITIES_RESTORATION_FIELD + ".cast_range";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_CAST_RANGE_NAME = "Cast Range";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_CAST_RANGE_COMMENT = "Maximum targeted cast range in blocks. If no direct hit is found at the range limit, Mending Bloom tries to snap down to nearby ground instead of floating in midair";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_SECOND = CONFIG_ABILITIES_RESTORATION_FIELD + ".cost_per_second";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_SECOND_NAME = "Mana Cost Per Second";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_SECOND_COMMENT = "Mana cost for each second Mending Bloom remains active";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_POTION_EFFECT = CONFIG_ABILITIES_RESTORATION_FIELD + ".cost_per_potion_effect";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_POTION_EFFECT_NAME = "Mana Cost Per Potion Effect";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_POTION_EFFECT_COMMENT = "Additional mana cost for each configured potion effect applied by Mending Bloom";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_EFFECT_LEVEL = CONFIG_ABILITIES_RESTORATION_FIELD + ".cost_per_effect_level";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_EFFECT_LEVEL_NAME = "Mana Cost Per Effect Level";
    public static final String CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_EFFECT_LEVEL_COMMENT = "Additional mana cost per potion amplifier step above level 1 applied by Mending Bloom";

    public static final String CONFIG_ABILITIES_WELL_RESTED = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.WELL_RESTED;
    public static final String CONFIG_ABILITIES_WELL_RESTED_NAME = "Well Rested";
    public static final String CONFIG_ABILITIES_WELL_RESTED_COMMENT = "";
    public static final String CONFIG_ABILITIES_WELL_RESTED_BUFFS = CONFIG_ABILITIES_WELL_RESTED + ".buffs";
    public static final String CONFIG_ABILITIES_WELL_RESTED_BUFFS_NAME = "Sleep Effects";
    public static final String CONFIG_ABILITIES_WELL_RESTED_BUFFS_COMMENT = "Effects given after a good nights rest";
    public static final String CONFIG_ABILITIES_WELL_RESTED_BUFFS_RANDOM = CONFIG_ABILITIES_WELL_RESTED_BUFFS + ".random";
    public static final String CONFIG_ABILITIES_WELL_RESTED_BUFFS_RANDOM_NAME = "Random Effects";
    public static final String CONFIG_ABILITIES_WELL_RESTED_BUFFS_RANDOM_COMMENT = "If this value is greater then 0, instead of giving every effect in the list, it gives x random effects";

    public static final String CONFIG_ABILITIES_WEIGHTLESS = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.WEIGHTLESS;
    public static final String CONFIG_ABILITIES_WEIGHTLESS_NAME = "Weightless";
    public static final String CONFIG_ABILITIES_WEIGHTLESS_COMMENT = "";

    public static final String CONFIG_ABILITIES_NULL_KINETIC = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.NULLIFY_KINETIC;
    public static final String CONFIG_ABILITIES_NULL_KINETIC_NAME = "Null Kinetic Energy";
    public static final String CONFIG_ABILITIES_NULL_KINETIC_COMMENT = "";
    public static final String CONFIG_ABILITIES_REDUCE_KINETIC = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.REDUCE_KINETIC;
    public static final String CONFIG_ABILITIES_REDUCE_KINETIC_NAME = "Reduce Kinetic Energy";
    public static final String CONFIG_ABILITIES_REDUCE_KINETIC_COMMENT = "";

    public static final String CONFIG_ABILITIES_SAFE_GUARD = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SAFE_GUARD;
    public static final String CONFIG_ABILITIES_SAFE_GUARD_NAME = "Safe Guard";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_COMMENT = "";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_EXPLOSION = CONFIG_ABILITIES_SAFE_GUARD + ".explosion";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_EXPLOSION_NAME = "Explosion Resistance";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_EXPLOSION_COMMENT = "Default multiplier to reduce explosion damage, set to 1 to disable";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST = CONFIG_ABILITIES_SAFE_GUARD + ".resist";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_NAME = "Resistance";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_COMMENT = "";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_LEVEL = CONFIG_ABILITIES_SAFE_GUARD_RESIST + ".amp";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_LEVEL_NAME = "Default Amplifier";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_LEVEL_COMMENT = "Default level for Resistance";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS = CONFIG_ABILITIES_SAFE_GUARD_RESIST + ".stacks";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_NAME = "Effect Stacks";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_COMMENT = "Does the Effect stack with other sources";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_MAX = CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS + ".max";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_MAX_NAME = "Max Stacks";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_MAX_COMMENT = "Maximum level the effect can stack to";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_HIT = CONFIG_ABILITIES_SAFE_GUARD + ".hit";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_HIT_NAME = "Required Hits";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_HIT_COMMENT = "How many hits required before you ignore the next hit. Hits only count if the damage is at least 1 whole heart";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_HIT_MIN = CONFIG_ABILITIES_SAFE_GUARD_HIT + ".min";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_HIT_MIN_NAME = "Damage Min";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_HIT_MIN_COMMENT = "The minimum damage to count towards triggering a Block";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_HIT_TRIGGER = CONFIG_ABILITIES_SAFE_GUARD_HIT + ".trigger";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_HIT_TRIGGER_NAME = "Damage Trigger";
    public static final String CONFIG_ABILITIES_SAFE_GUARD_HIT_TRIGGER_COMMENT = "The minimum damage to trigger a block";

    public static final String CONFIG_ABILITIES_ENDER_QUEEN = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.ENDER_QUEEN;
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_NAME = "Ender Queen";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_COMMENT = "";

    public static final String CONFIG_ABILITIES_ENDER_QUEEN_CHEST = CONFIG_ABILITIES_ENDER_QUEEN + ".chest";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_CHEST_NAME = "Remote Ender Chest";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_CHEST_COMMENT = "Able to access the Ender Chest from Anywhere";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_WATER_HURTS = CONFIG_ABILITIES_ENDER_QUEEN + ".waterhurts";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_WATER_HURTS_NAME = "Water Hurts";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_WATER_HURTS_COMMENT = "If while wearing this should the player take damage while wet";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_DAMAGED = CONFIG_ABILITIES_ENDER_QUEEN + ".damaged";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_DAMAGED_NAME = "Ignore Damage";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_DAMAGED_COMMENT = "1 in 'num' chance to ignore damage entirely";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_SPAWN_CHANCE = CONFIG_ABILITIES_ENDER_QUEEN_DAMAGED + ".spawn";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_SPAWN_CHANCE_NAME = "Spawn Enderman";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_SPAWN_CHANCE_COMMENT = "1 in 'num' chance to summon an Enderman to protect you when taking damage";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_TELEPORT_ON_HURT = CONFIG_ABILITIES_ENDER_QUEEN_DAMAGED + ".teleport";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_TELEPORT_ON_HURT_NAME = "Teleport on Hurt";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_TELEPORT_ON_HURT_COMMENT = "1 in 'num' chance to teleport when taking indirect damage";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_FOLLOW = CONFIG_ABILITIES_ENDER_QUEEN + ".followers";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_FOLLOW_NAME = "Follower Enderman";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_FOLLOW_COMMENT = "Nearby Endermen Follow you";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_RETALIATION = CONFIG_ABILITIES_ENDER_QUEEN + ".retaliate";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_RETALIATION_NAME = "Enderman Retaliate";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_RETALIATION_COMMENT = "When Attacking an Enderman should it fight back";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_DROP_EXP = CONFIG_ABILITIES_ENDER_QUEEN + ".drop.exp";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_DROP_EXP_NAME = "Block Exp Drops";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_DROP_EXP_COMMENT = "When Killing an Enderman Should it drop Experience";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_DROP_ITEMS = CONFIG_ABILITIES_ENDER_QUEEN + ".drop.items";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_DROP_ITEMS_NAME = "Block Item Drops";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_DROP_ITEMS_COMMENT = "When Killing an Enderman Should it drop Items";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_BLOCK_TELEPORT = CONFIG_ABILITIES_ENDER_QUEEN + ".block.teleport";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_BLOCK_TELEPORT_NAME = "Block Enderman Teleportation";
    public static final String CONFIG_ABILITIES_ENDER_QUEEN_BLOCK_TELEPORT_COMMENT = "Allow Enderman to teleport";

    public static final String CONFIG_ABILITIES_VICIOUS_STRIKE = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.VICIOUS_STRIKE;
    public static final String CONFIG_ABILITIES_VICIOUS_STRIKE_NAME = "Vicious Strike";
    public static final String CONFIG_ABILITIES_VICIOUS_STRIKE_COMMENT = "";
    public static final String CONFIG_ABILITIES_VICIOUS_STRIKE_CHANCE = CONFIG_ABILITIES_VICIOUS_STRIKE + ".chance";
    public static final String CONFIG_ABILITIES_VICIOUS_STRIKE_CHANCE_NAME = "Bleed Chance";
    public static final String CONFIG_ABILITIES_VICIOUS_STRIKE_CHANCE_COMMENT = "";

    public static final String CONFIG_ABILITIES_MAGNETIC = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.MAGNETIC;
    public static final String CONFIG_ABILITIES_MAGNETIC_NAME = "Magnetic";
    public static final String CONFIG_ABILITIES_MAGNETIC_COMMENT = "";

    public static final String CONFIG_ABILITIES_MAGNETIC_XP = CONFIG_ABILITIES_MAGNETIC + ".xp";
    public static final String CONFIG_ABILITIES_MAGNETIC_XP_NAME = "Collect Exp";
    public static final String CONFIG_ABILITIES_MAGNETIC_XP_COMMENT = "Should the ability effect exp orbs";
    public static final String CONFIG_ABILITIES_MAGNETIC_INSTANT = CONFIG_ABILITIES_MAGNETIC + ".instant";
    public static final String CONFIG_ABILITIES_MAGNETIC_INSTANT_NAME = "Instant Pickup";
    public static final String CONFIG_ABILITIES_MAGNETIC_INSTANT_COMMENT = "Should the You Instantly pickup Items, or Pull them toward you.";
    public static final String CONFIG_ABILITIES_MAGNETIC_INSTANT_XP = CONFIG_ABILITIES_MAGNETIC_INSTANT + ".xp";
    public static final String CONFIG_ABILITIES_MAGNETIC_INSTANT_XP_NAME = "Instant XP";
    public static final String CONFIG_ABILITIES_MAGNETIC_INSTANT_XP_COMMENT = "Should you instantly pickup Exp orbs";

    public static final String CONFIG_ABILITIES_REPEL = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.REPEL;
    public static final String CONFIG_ABILITIES_REPEL_NAME = "Repel";
    public static final String CONFIG_ABILITIES_REPEL_COMMENT = "";

    public static final String CONFIG_ABILITIES_DODGE = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.DODGING;
    public static final String CONFIG_ABILITIES_DODGE_NAME = "Dodging";
    public static final String CONFIG_ABILITIES_DODGE_COMMENT = "";
    public static final String CONFIG_ABILITIES_DODGE_KEYBIND_MOVEMENT = CONFIG_ABILITIES_DODGE + ".keybind_movement";
    public static final String CONFIG_ABILITIES_DODGE_KEYBIND_MOVEMENT_NAME = "Keybind + Movement";
    public static final String CONFIG_ABILITIES_DODGE_KEYBIND_MOVEMENT_COMMENT = "Use the configured Dodge key plus a movement key instead of double-tapping movement to Dodge";
    public static final String CONFIG_ABILITIES_DODGE_STUN = CONFIG_ABILITIES_DODGE + ".stuns";
    public static final String CONFIG_ABILITIES_DODGE_STUN_NAME = "Dodge Stuns";
    public static final String CONFIG_ABILITIES_DODGE_STUN_COMMENT = "Should triggering a dodge stun nearby entities";
    public static final String CONFIG_ABILITIES_DODGE_STUN_RANGE = CONFIG_ABILITIES_DODGE_STUN + ".radius";
    public static final String CONFIG_ABILITIES_DODGE_STUN_RANGE_NAME = "Stun Radius";
    public static final String CONFIG_ABILITIES_DODGE_STUN_RANGE_COMMENT = "The Radius outward from the player to stun entities";

    public static final String CONFIG_ABILITIES_BREATH = CONFIG_ABILITIES + ".breath";
    public static final String CONFIG_ABILITIES_BREATH_NAME = "Breath";
    public static final String CONFIG_ABILITIES_BREATH_COMMENT = "";
    public static final String CONFIG_ABILITIES_BREATH_EFFECTS = CONFIG_ABILITIES_BREATH + ".effects";
    public static final String CONFIG_ABILITIES_BREATH_EFFECTS_NAME = "Breath Effects";
    public static final String CONFIG_ABILITIES_BREATH_EFFECTS_COMMENT = "Effects applied to entities hit by the breath";
    public static final String CONFIG_ABILITIES_BREATH_TERRAIN = CONFIG_ABILITIES_BREATH + ".terrain";
    public static final String CONFIG_ABILITIES_BREATH_TERRAIN_NAME = "Terrain Effects";
    public static final String CONFIG_ABILITIES_BREATH_TERRAIN_COMMENT = "Should the Breath interact with the terrain";

    public static final String CONFIG_ABILITIES_BREATH_LIGHTNING = CONFIG_ABILITIES_BREATH + "." + TrinketsRegistryNames.ModAbilities.BREATH_LIGHTNING;
    public static final String CONFIG_ABILITIES_BREATH_LIGHTNING_NAME = "Lightning Breath";
    public static final String CONFIG_ABILITIES_BREATH_LIGHTNING_COMMENT = "";
    public static final String CONFIG_ABILITIES_BREATH_FIRE = CONFIG_ABILITIES_BREATH + "." + TrinketsRegistryNames.ModAbilities.BREATH_FIRE;
    public static final String CONFIG_ABILITIES_BREATH_FIRE_NAME = "Fire Breath";
    public static final String CONFIG_ABILITIES_BREATH_FIRE_COMMENT = "";
    public static final String CONFIG_ABILITIES_BREATH_ICE = CONFIG_ABILITIES_BREATH + "." + TrinketsRegistryNames.ModAbilities.BREATH_ICE;
    public static final String CONFIG_ABILITIES_BREATH_ICE_NAME = "Ice Breath";
    public static final String CONFIG_ABILITIES_BREATH_ICE_COMMENT = "";
    public static final String CONFIG_ABILITIES_BREATH_DRAGON = CONFIG_ABILITIES_BREATH + "." + TrinketsRegistryNames.ModAbilities.BREATH_DRAGON;
    public static final String CONFIG_ABILITIES_BREATH_DRAGON_NAME = "Dragon Breath";
    public static final String CONFIG_ABILITIES_BREATH_DRAGON_COMMENT = "";

    public static final String CONFIG_ABILITIES_IMMUNITY_FIRE = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.IMMUNITY_FIRE;
    public static final String CONFIG_ABILITIES_IMMUNITY_FIRE_NAME = "Fire Immunity";
    public static final String CONFIG_ABILITIES_IMMUNITY_FIRE_COMMENT = "";
    public static final String CONFIG_ABILITIES_IMMUNITY_ICE = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.IMMUNITY_ICE;
    public static final String CONFIG_ABILITIES_IMMUNITY_ICE_NAME = "Ice Immunity";
    public static final String CONFIG_ABILITIES_IMMUNITY_ICE_COMMENT = "";
    public static final String CONFIG_ABILITIES_IMMUNITY_LIGHTNING = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.IMMUNITY_LIGHTNING;
    public static final String CONFIG_ABILITIES_IMMUNITY_LIGHTNING_NAME = "Lightning Immunity";
    public static final String CONFIG_ABILITIES_IMMUNITY_LIGHTNING_COMMENT = "";
    public static final String CONFIG_ABILITIES_IMMUNITY_POISON = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.IMMUNITY_POISON;
    public static final String CONFIG_ABILITIES_IMMUNITY_POISON_NAME = "Poison Immunity";
    public static final String CONFIG_ABILITIES_IMMUNITY_POISON_COMMENT = "";
    public static final String CONFIG_ABILITIES_IMMUNITY_DARK = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.IMMUNITY_DARK;
    public static final String CONFIG_ABILITIES_IMMUNITY_DARK_NAME = "Dark Immunity";
    public static final String CONFIG_ABILITIES_IMMUNITY_DARK_COMMENT = "";
    public static final String CONFIG_ABILITIES_IMMUNITY_WATER = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.IMMUNITY_WATER;
    public static final String CONFIG_ABILITIES_IMMUNITY_WATER_NAME = "Water Immunity";
    public static final String CONFIG_ABILITIES_IMMUNITY_WATER_COMMENT = "";

    public static final String CONFIG_ABILITIES_AFFINITY_WATER = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.AFFINITY_WATER;
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_NAME = "Water Affinity";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_COMMENT = "";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_VANILLA = CONFIG_ABILITIES_AFFINITY_WATER + ".vanilla";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_VANILLA_NAME = "Use Vanilla Effect";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_VANILLA_COMMENT = "Use Water Breathing instead of Modifying the players Oxygen";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_BUBBLES = CONFIG_ABILITIES_AFFINITY_WATER + ".bubbles";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_BUBBLES_NAME = "Bubbles";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_BUBBLES_COMMENT = "How many oxygen bubbles should the player have while under water. Setting to 0 Disables Infinite Oxygen entirely";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_MINING = CONFIG_ABILITIES_AFFINITY_WATER + ".mining";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_MINING_NAME = "Underwater Mining";
    public static final String CONFIG_ABILITIES_AFFINITY_WATER_MINING_COMMENT = "Adjust mining speed while under water";

    public static final String CONFIG_ABILITIES_AFFINITY_POISON = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.AFFINITY_POISON;
    public static final String CONFIG_ABILITIES_AFFINITY_POISON_NAME = "Poison Affinity";
    public static final String CONFIG_ABILITIES_AFFINITY_POISON_COMMENT = "";
    public static final String CONFIG_ABILITIES_AFFINITY_POISON_CHANCE = CONFIG_ABILITIES_AFFINITY_POISON + ".chance";
    public static final String CONFIG_ABILITIES_AFFINITY_POISON_CHANCE_NAME = "Poison Chance";
    public static final String CONFIG_ABILITIES_AFFINITY_POISON_CHANCE_COMMENT = "1 in X chance to Poison a target on Attack";
    public static final String CONFIG_ABILITIES_AFFINITY_POISON_MULTI = CONFIG_ABILITIES_AFFINITY_POISON + ".multi";
    public static final String CONFIG_ABILITIES_AFFINITY_POISON_MULTI_NAME = "Poison Damage Multiplier";
    public static final String CONFIG_ABILITIES_AFFINITY_POISON_MULTI_COMMENT = "How much to multiply damage when attacking a poisoned enemy";

    public static final String CONFIG_ABILITIES_AFFINITY_DARK = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.AFFINITY_DARK;
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_NAME = "Dark Affinity";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_COMMENT = "";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_HEAL = CONFIG_ABILITIES_AFFINITY_DARK + ".heal";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_HEAL_NAME = "Dark Heal";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_HEAL_COMMENT = "Heal from taking dark based attacks";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_HEAL_MULTI = CONFIG_ABILITIES_AFFINITY_DARK_HEAL + ".multi";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_HEAL_MULTI_NAME = "Heal percentage";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_HEAL_MULTI_COMMENT = "How much damage to convert into health";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_LEECH = CONFIG_ABILITIES_AFFINITY_DARK + ".leech";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_LEECH_NAME = "True Leeching";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_LEECH_COMMENT = "Directly heal for all the damage you deal on withered enemies";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_LEECH_AMOUNT = CONFIG_ABILITIES_AFFINITY_DARK_LEECH + ".amount";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_LEECH_AMOUNT_NAME = "Leech amount";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_LEECH_AMOUNT_COMMENT = "How much health to leech when attacking a withered enemy if True Leech is disabled";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_WITHER_CHANCE = CONFIG_ABILITIES_AFFINITY_DARK + ".chance";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_WITHER_CHANCE_NAME = "Wither Chance";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_WITHER_CHANCE_COMMENT = "1 in X chance to Wither an Enemy on Attack";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_WITHER_DURATION = CONFIG_ABILITIES_AFFINITY_DARK + ".duration";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_WITHER_DURATION_NAME = "Wither Duration";
    public static final String CONFIG_ABILITIES_AFFINITY_DARK_WITHER_DURATION_COMMENT = "How long should wither be applied in ticks (20 ticks / second)";

    public static final String CONFIG_ABILITIES_LIGHTNING_BOLT = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.LIGHTNING_BOLT;
    public static final String CONFIG_ABILITIES_LIGHTNING_BOLT_NAME = "Lightning Bolt";
    public static final String CONFIG_ABILITIES_LIGHTNING_BOLT_COMMENT = "";

    public static final String CONFIG_ABILITIES_FROST_WALKER = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.FROST_WALKER;
    public static final String CONFIG_ABILITIES_FROST_WALKER_NAME = "Frost Walker";
    public static final String CONFIG_ABILITIES_FROST_WALKER_COMMENT = "";

    public static final String CONFIG_ABILITIES_LARGE_HANDS = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.LARGE_HANDS;
    public static final String CONFIG_ABILITIES_LARGE_HANDS_NAME = "Large Hards";
    public static final String CONFIG_ABILITIES_LARGE_HANDS_COMMENT = "";
    public static final String CONFIG_ABILITIES_LARGE_HANDS_MINING = CONFIG_ABILITIES_LARGE_HANDS + ".mining";
    public static final String CONFIG_ABILITIES_LARGE_HANDS_MINING_NAME = "AoE Mining";
    public static final String CONFIG_ABILITIES_LARGE_HANDS_MINING_COMMENT = "If Enabled the player will be able to mine a 3x3 area";
    public static final String CONFIG_ABILITIES_LARGE_HANDS_MINING_BLACKLIST = CONFIG_ABILITIES_LARGE_HANDS_MINING + ".blacklist";
    public static final String CONFIG_ABILITIES_LARGE_HANDS_MINING_BLACKLIST_NAME = "AoE Mining Blacklist";
    public static final String CONFIG_ABILITIES_LARGE_HANDS_MINING_BLACKLIST_COMMENT = "List of blocks to ignore when mining that conflict or cause issues.";

    public static final String CONFIG_ABILITIES_HEAVY = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.HEAVY;
    public static final String CONFIG_ABILITIES_HEAVY_NAME = "Heavy";
    public static final String CONFIG_ABILITIES_HEAVY_COMMENT = "If the player should be too heavy to stay afloat in fluids";
    public static final String CONFIG_ABILITIES_HEAVY_TRAMPLE = CONFIG_ABILITIES_HEAVY + ".trample";
    public static final String CONFIG_ABILITIES_HEAVY_TRAMPLE_NAME = "Trample Farmland";
    public static final String CONFIG_ABILITIES_HEAVY_TRAMPLE_COMMENT = "If Enabled the player will trample farmland";
    public static final String CONFIG_ABILITIES_LEVELING = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.LEVELING;
    public static final String CONFIG_ABILITIES_LEVELING_NAME = "Leveling";
    public static final String CONFIG_ABILITIES_LEVELING_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_RIDER = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SKILLED_RIDER;
    public static final String CONFIG_ABILITIES_SKILLED_RIDER_NAME = "Skilled Rider";
    public static final String CONFIG_ABILITIES_SKILLED_RIDER_COMMENT = "";
    public static final String CONFIG_ABILITIES_WOLF_RIDER = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.WOLF_RIDER;
    public static final String CONFIG_ABILITIES_WOLF_RIDER_NAME = "Wolf Rider";
    public static final String CONFIG_ABILITIES_WOLF_RIDER_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_SWIMMER = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SKILLED_SWIMMER;
    public static final String CONFIG_ABILITIES_SKILLED_SWIMMER_NAME = "Skilled Swimmer";
    public static final String CONFIG_ABILITIES_SKILLED_SWIMMER_COMMENT = "";
    public static final String CONFIG_ABILITIES_SKILLED_SWIMMER_OLD_TWEAKS = CONFIG_ABILITIES_SKILLED_SWIMMER + ".old";
    public static final String CONFIG_ABILITIES_SKILLED_SWIMMER_OLD_TWEAKS_NAME = "Use old Tweaks";
    public static final String CONFIG_ABILITIES_SKILLED_SWIMMER_OLD_TWEAKS_COMMENT = "Use old version of Swimming Tweaks. Not recommended";

    public static final String CONFIG_ABILITIES_STAMPEDE = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.STAMPEDE;
    public static final String CONFIG_ABILITIES_STAMPEDE_NAME = "Stampede";
    public static final String CONFIG_ABILITIES_STAMPEDE_COMMENT = "";

    /// External

    /// SURVIVAL
    public static final String CONFIG_ABILITIES_SURVIVAL_IMMUNITY_HEAT = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SURVIVAL_HEAT_IMMUNITY;
    public static final String CONFIG_ABILITIES_SURVIVAL_IMMUNITY_HEAT_NAME = "Immune to Heat";
    public static final String CONFIG_ABILITIES_SURVIVAL_IMMUNITY_HEAT_COMMENT = "Should the player be immune to Heat";
    public static final String CONFIG_ABILITIES_SURVIVAL_IMMUNITY_COLD = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SURVIVAL_COLD_IMMUNITY;
    public static final String CONFIG_ABILITIES_SURVIVAL_IMMUNITY_COLD_NAME = "Immune to Cold";
    public static final String CONFIG_ABILITIES_SURVIVAL_IMMUNITY_COLD_COMMENT = "Should the player be immune to Cold";
    public static final String CONFIG_ABILITIES_SURVIVAL_THIRST = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SURVIVAL_THIRST_IMMUNITY;
    public static final String CONFIG_ABILITIES_SURVIVAL_THIRST_NAME = "Immune to Thirst Poisoning";
    public static final String CONFIG_ABILITIES_SURVIVAL_THIRST_COMMENT = "Should the player be immune to Thirst Poisoning";
    public static final String CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SURVIVAL_THIRST_ABSORPTION;
    public static final String CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_NAME = "Water Absorption";
    public static final String CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_COMMENT = "Should the player Absorb water to refill their thirst";
    public static final String CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_AMOUNT = CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION;
    public static final String CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_AMOUNT_NAME = "Absorption Amount";
    public static final String CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_AMOUNT_COMMENT = "How much thirst to absorb while in water";
    public static final String CONFIG_ABILITIES_SURVIVAL_PARASITES = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.SURVIVAL_PARASITES_IMMUNITY;
    public static final String CONFIG_ABILITIES_SURVIVAL_PARASITES_NAME = "Immune to Parasites";
    public static final String CONFIG_ABILITIES_SURVIVAL_PARASITES_COMMENT = "Should the player be immune to Parasites from drinking tainted water";

    /// FIRST AID
    public static final String CONFIG_ABILITIES_FIRST_AID_HARD_HEAD = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.FIRST_AID_HARD_HEAD;
    public static final String CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_NAME = "Hard Head";
    public static final String CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_COMMENT = "";
    public static final String CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_CHANCE = CONFIG_ABILITIES_FIRST_AID_HARD_HEAD + ".chance";
    public static final String CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_CHANCE_NAME = "Headshots Ignore Chance";
    public static final String CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_CHANCE_COMMENT = "If First Aid is Installed. 1 in How many Chance to Trigger Ignore Headshot";

    /// ENHANCED VISUALS
    public static final String CONFIG_ABILITIES_ENHANCED_VISUALS_ENDER_EYES = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.ENHANCED_VISUALS_STATIC;
    public static final String CONFIG_ABILITIES_ENHANCED_VISUALS_ENDER_EYES_NAME = "Ender Eyes";
    public static final String CONFIG_ABILITIES_ENHANCED_VISUALS_ENDER_EYES_COMMENT = "Removed the Enhanced Visuals Static Effect";
    public static final String CONFIG_ABILITIES_ENHANCED_VISUALS_CLEAR_VISION = CONFIG_ABILITIES + "." + TrinketsRegistryNames.ModAbilities.ENHANCED_VISUALS_BLUR;
    public static final String CONFIG_ABILITIES_ENHANCED_VISUALS_CLEAR_VISION_NAME = "Clear Vision";
    public static final String CONFIG_ABILITIES_ENHANCED_VISUALS_CLEAR_VISION_COMMENT = "Removed the Enhanved Visuals Blur Effect";

    /**
     * xat.config.races
     */
    public static final String CONFIG_RACES = CONFIG + ".races";
    public static final String CONFIG_RACES_NAME = "Race Configuration";
    public static final String CONFIG_RACES_COMMENT = "";
    public static final String CONFIG_RACES_GUI = CONFIG_RACES + ".gui";
    public static final String CONFIG_RACES_GUI_NAME = "Race Informational GUI";
    public static final String CONFIG_RACES_GUI_COMMENT = "";
    public static final String CONFIG_RACES_MENU = CONFIG_RACES + ".menu";
    public static final String CONFIG_RACES_MENU_NAME = "Race Selection Menu";
    public static final String CONFIG_RACES_MENU_COMMENT = "Prmopts the player to choose a Race when joining a world for the first time.";
    public static final String CONFIG_RACES_MENU_BLACKLIST = CONFIG_RACES_MENU + ".blacklist";
    public static final String CONFIG_RACES_MENU_BLACKLIST_NAME = "Blacklisted Races";
    public static final String CONFIG_RACES_MENU_BLACKLIST_COMMENT = "These are races which are blacklisted from the Race Selection Menu if Enabled";

    public static final String CONFIG_RACES_SIZE = CONFIG_RACES + ".size";
    public static final String CONFIG_RACES_SIZE_NAME = "Size";
    public static final String CONFIG_RACES_SIZE_COMMENT = "";
    public static final String CONFIG_RACES_MOUNT_CONTROL = CONFIG_RACES + ".mount";
    public static final String CONFIG_RACES_MOUNT_CONTROL_NAME = "Mount Entities";
    public static final String CONFIG_RACES_MOUNT_CONTROL_COMMENT = "Is this race able to mount other entities. Horses, Boats, etc.";
    public static final String CONFIG_RACES_MOUNT_CONTROL_BOAT = CONFIG_RACES_MOUNT_CONTROL + ".boat";
    public static final String CONFIG_RACES_MOUNT_CONTROL_BOAT_NAME = "Control Boats";
    public static final String CONFIG_RACES_MOUNT_CONTROL_BOAT_COMMENT = "Is this race able to control boats. Note this will make it impossible to mount a boat without another entity as the driver";
    public static final String CONFIG_RACES_MOUNT_CONTROL_BLACKLIST = CONFIG_RACES_MOUNT_CONTROL + ".blacklist";
    public static final String CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_NAME = "Mount Blacklist";
    public static final String CONFIG_RACES_MOUNT_CONTROL_BLACKLIST_COMMENT = "If the race can mount entities, which ones are excluded";

    public static final String CONFIG_RACES_HUMAN = CONFIG_RACES + "." + TrinketsRegistryNames.ModRaces.HUMAN;
    public static final String CONFIG_RACES_HUMAN_NAME = "Human";
    public static final String CONFIG_RACES_HUMAN_COMMENT = "";
    public static final String CONFIG_RACES_ELF = CONFIG_RACES + "." + TrinketsRegistryNames.ModRaces.ELF;
    public static final String CONFIG_RACES_ELF_NAME = "Elf";
    public static final String CONFIG_RACES_ELF_COMMENT = "";
    public static final String CONFIG_RACES_DWARF = CONFIG_RACES + "." + TrinketsRegistryNames.ModRaces.DWARF;
    public static final String CONFIG_RACES_DWARF_NAME = "Dwarf";
    public static final String CONFIG_RACES_DWARF_COMMENT = "";
    public static final String CONFIG_RACES_FAIRY = CONFIG_RACES + "." + TrinketsRegistryNames.ModRaces.FAIRY;
    public static final String CONFIG_RACES_FAIRY_NAME = "Fairy";
    public static final String CONFIG_RACES_FAIRY_COMMENT = "";

    public static final String CONFIG_RACES_GOBLIN = CONFIG_RACES + "." + TrinketsRegistryNames.ModRaces.GOBLIN;
    public static final String CONFIG_RACES_GOBLIN_NAME = "Goblin";
    public static final String CONFIG_RACES_GOBLIN_COMMENT = "";
    public static final String CONFIG_RACES_GOBLIN_RESISTANCE = CONFIG_RACES_GOBLIN + ".resistance";
    public static final String CONFIG_RACES_GOBLIN_RESISTANCE_NAME = "Natural Resistance";
    public static final String CONFIG_RACES_GOBLIN_RESISTANCE_COMMENT = "Should Goblins be naturally resistant to explosives and Fire Damage";
    public static final String CONFIG_RACES_GOBLIN_CREEPERS_FRIENDLY = CONFIG_RACES_GOBLIN + ".creeper.friendly";
    public static final String CONFIG_RACES_GOBLIN_CREEPERS_FRIENDLY_NAME = "Friendly Creepers";
    public static final String CONFIG_RACES_GOBLIN_CREEPERS_FRIENDLY_COMMENT = "Should Creepers Ignore you as a Goblin";
    public static final String CONFIG_RACES_GOBLIN_CREEPERS_EXPLODE = CONFIG_RACES_GOBLIN + ".creeper.explode";
    public static final String CONFIG_RACES_GOBLIN_CREEPERS_EXPLODE_NAME = "Creepers Instantly Explode";
    public static final String CONFIG_RACES_GOBLIN_CREEPERS_EXPLODE_COMMENT = "Should Creepers Explode instantly when hit as a Goblin";


    public static final String CONFIG_RACES_FAELIS = CONFIG_RACES + "." + TrinketsRegistryNames.ModRaces.FAELIS;
    public static final String CONFIG_RACES_FAELIS_NAME = "Faelis";
    public static final String CONFIG_RACES_FAELIS_COMMENT = "";

    public static final String CONFIG_RACES_FAELIS_MILK = CONFIG_RACES_FAELIS + ".milk";
    public static final String CONFIG_RACES_FAELIS_MILK_NAME = "?";
    public static final String CONFIG_RACES_FAELIS_MILK_COMMENT = "";
    public static final String CONFIG_RACES_FAELIS_MILK_BUFF = CONFIG_RACES_FAELIS_MILK + ".buff";
    public static final String CONFIG_RACES_FAELIS_MILK_BUFF_NAME = "Milk Bonus";
    public static final String CONFIG_RACES_FAELIS_MILK_BUFF_COMMENT = "Should the Faelis gain a bonus when drinking milk";
    public static final String CONFIG_RACES_FAELIS_MILK_BUFF_LIST = CONFIG_RACES_FAELIS_MILK_BUFF + ".list";
    public static final String CONFIG_RACES_FAELIS_MILK_BUFF_LIST_NAME = "Milk Effects";
    public static final String CONFIG_RACES_FAELIS_MILK_BUFF_LIST_COMMENT = "List of effects given when drinking milk";
    public static final String CONFIG_RACES_FAELIS_MILK_DURATION = CONFIG_RACES_FAELIS_MILK_BUFF_LIST + ".duration";
    public static final String CONFIG_RACES_FAELIS_MILK_DURATION_NAME = "Milk Bonus Duration";
    public static final String CONFIG_RACES_FAELIS_MILK_DURATION_COMMENT = "How long should milk buffs last";
    public static final String CONFIG_RACES_FAELIS_MILK_LIST = CONFIG_RACES_FAELIS_MILK + ".list";
    public static final String CONFIG_RACES_FAELIS_MILK_LIST_NAME = "Milk";
    public static final String CONFIG_RACES_FAELIS_MILK_LIST_COMMENT = "List of Items that count as Milk for the Milk bonus";

    public static final String CONFIG_RACES_FAELIS_HEAVY = CONFIG_RACES_FAELIS + ".heavy";
    public static final String CONFIG_RACES_FAELIS_HEAVY_NAME = "Heavy";
    public static final String CONFIG_RACES_FAELIS_HEAVY_COMMENT = "Should armor give a movement penalty when using equipment";
    public static final String CONFIG_RACES_FAELIS_HEAVY_LIST = CONFIG_RACES_FAELIS_HEAVY + ".list";
    public static final String CONFIG_RACES_FAELIS_HEAVY_LIST_NAME = "Heavy Equipment";
    public static final String CONFIG_RACES_FAELIS_HEAVY_LIST_COMMENT = "List of all Items and equipment that are considered heavy and their values";
    public static final String CONFIG_RACES_FAELIS_HEAVY_INVIGORATED = CONFIG_RACES_FAELIS_HEAVY + ".invigorated";
    public static final String CONFIG_RACES_FAELIS_HEAVY_INVIGORATED_NAME = "Milk removes Heavy Penalty";
    public static final String CONFIG_RACES_FAELIS_HEAVY_INVIGORATED_COMMENT = "Should Drinking milk temporarily remove the heavy armor penalty";

    public static final String CONFIG_RACES_FAELIS_BARE_HAND = CONFIG_RACES_FAELIS + ".barehand";
    public static final String CONFIG_RACES_FAELIS_BARE_HAND_NAME = "Bare Handed Combat";
    public static final String CONFIG_RACES_FAELIS_BARE_HAND_COMMENT = "";
    public static final String CONFIG_RACES_FAELIS_BARE_HAND_BONUS = CONFIG_RACES_FAELIS_BARE_HAND + ".bonus";
    public static final String CONFIG_RACES_FAELIS_BARE_HAND_BONUS_NAME = "Bare Handed Bonus";
    public static final String CONFIG_RACES_FAELIS_BARE_HAND_BONUS_COMMENT = "";
    public static final String CONFIG_RACES_FAELIS_BARE_HAND_LIST = CONFIG_RACES_FAELIS_BARE_HAND + ".list";
    public static final String CONFIG_RACES_FAELIS_BARE_HAND_LIST_NAME = "Bare handed equipment";
    public static final String CONFIG_RACES_FAELIS_BARE_HAND_LIST_COMMENT = "";

    public static final String CONFIG_RACES_TITAN = CONFIG_RACES + "." + TrinketsRegistryNames.ModRaces.TITAN;
    public static final String CONFIG_RACES_TITAN_NAME = "Titan";
    public static final String CONFIG_RACES_TITAN_COMMENT = "";
    public static final String CONFIG_RACES_DRAGON = CONFIG_RACES + "." + TrinketsRegistryNames.ModRaces.DRAGON;
    public static final String CONFIG_RACES_DRAGON_NAME = "Dragon";
    public static final String CONFIG_RACES_DRAGON_COMMENT = "";
    public static final String CONFIG_RACES_TAURUS = CONFIG_RACES + "." + TrinketsRegistryNames.ModRaces.TAURUS;
    public static final String CONFIG_RACES_TAURUS_NAME = "Taurus";
    public static final String CONFIG_RACES_TAURUS_COMMENT = "";

    public static final String CONFIG_DAMAGE_TYPES = CONFIG + ".damagetypes";
    public static final String CONFIG_DAMAGE_TYPES_NAME = "Damage Types";
    public static final String CONFIG_DAMAGE_TYPES_COMMENT = "Immune to all Damage Types. Syntax Example\nonHurt:type;isFire;isTrue:minecraft:blaze;5.0;0.1\nonHurt:*;isFire;*;5.0;0.1";

    public static final String CONFIG_EFFECTS = CONFIG + ".effects";
    public static final String CONFIG_EFFECTS_NAME = "Potion Effects";
    public static final String CONFIG_EFFECTS_COMMENT = "";

    public static final String CONFIG_RESISTANCES = CONFIG + ".resistances";
    public static final String CONFIG_RESISTANCES_NAME = "Potion Resistances";
    public static final String CONFIG_RESISTANCES_COMMENT = "What potion effects is the player immune to";

    public static final String CONFIG_ATTRIBUTES = CONFIG + ".attributes";
    public static final String CONFIG_ATTRIBUTES_NAME = "Attributes";
    public static final String CONFIG_ATTRIBUTES_COMMENT = "For More Information on Attributes, https://minecraft.gamepedia.com/Attribute";

    /**
     * xat.config.magic
     */
    public static final String CONFIG_MAGIC = CONFIG + ".magic";
    public static final String CONFIG_MAGIC_NAME = "Magic";
    public static final String CONFIG_MAGIC_COMMENT = "";
    public static final String CONFIG_MAGIC_ENABLED = CONFIG_MAGIC + ".enabled";
    public static final String CONFIG_MAGIC_ENABLED_NAME = "Magic System Enabled";
    public static final String CONFIG_MAGIC_ENABLED_COMMENT = "If Abilities from Items and Races cost Mana";
    public static final String CONFIG_MAGIC_HUD = CONFIG_MAGIC + ".hud";
    public static final String CONFIG_MAGIC_HUD_NAME = "Mana Hud";
    public static final String CONFIG_MAGIC_HUD_COMMENT = "Mana Bar Hud Configuration";
    public static final String CONFIG_MAGIC_HUD_ENABLED = CONFIG_MAGIC_HUD + ".enabled";
    public static final String CONFIG_MAGIC_HUD_ENABLED_NAME = "Show Mana Bar";
    public static final String CONFIG_MAGIC_HUD_ENABLED_COMMENT = "Disable to hide the Mana Bar";
    public static final String CONFIG_MAGIC_HUD_ENABLED_ALWAYS = CONFIG_MAGIC_HUD_ENABLED + ".always";
    public static final String CONFIG_MAGIC_HUD_ENABLED_ALWAYS_NAME = "Show Always";
    public static final String CONFIG_MAGIC_HUD_ENABLED_ALWAYS_COMMENT = "Show the Mana Bar even when full";
    public static final String CONFIG_MAGIC_HUD_TEXT = CONFIG_MAGIC_HUD + ".text";
    public static final String CONFIG_MAGIC_HUD_TEXT_NAME = "Show Values";
    public static final String CONFIG_MAGIC_HUD_TEXT_COMMENT = "Should the Mana Bar show values";
    public static final String CONFIG_MAGIC_HUD_PRE = CONFIG_MAGIC_HUD + ".pre";
    public static final String CONFIG_MAGIC_HUD_PRE_NAME = "Render in Pre-Event";
    public static final String CONFIG_MAGIC_HUD_PRE_COMMENT = "Should the Mana bar be rendered in the Pre-Render Event. This makes the Mana Bar render behind the Hotbar";
    public static final String CONFIG_MAGIC_HUD_TEXTURE = CONFIG_MAGIC_HUD + ".texture";
    public static final String CONFIG_MAGIC_HUD_TEXTURE_NAME = "Mana Bar Texture";
    public static final String CONFIG_MAGIC_HUD_TEXTURE_COMMENT = "Alternate Textures for the Mana Bar";
    public static final String CONFIG_MAGIC_HUD_POSITION_MODE = CONFIG_MAGIC_HUD + ".position.mode";
    public static final String CONFIG_MAGIC_HUD_POSITION_MODE_NAME = "Use Pixel Position";
    public static final String CONFIG_MAGIC_HUD_POSITION_MODE_COMMENT = "Use fixed pixel X/Y coordinates instead of percentage-based screen position";
    public static final String CONFIG_MAGIC_HUD_POSITION_PIXELS_X = CONFIG_MAGIC_HUD + ".position.x";
    public static final String CONFIG_MAGIC_HUD_POSITION_PIXELS_X_NAME = "X Position (Pixels)";
    public static final String CONFIG_MAGIC_HUD_POSITION_PIXELS_X_COMMENT = "Absolute X position in scaled-screen pixels";
    public static final String CONFIG_MAGIC_HUD_POSITION_PIXELS_Y = CONFIG_MAGIC_HUD + ".position.y";
    public static final String CONFIG_MAGIC_HUD_POSITION_PIXELS_Y_NAME = "Y Position (Pixels)";
    public static final String CONFIG_MAGIC_HUD_POSITION_PIXELS_Y_COMMENT = "Absolute Y position in scaled-screen pixels";
    public static final String CONFIG_MAGIC_COST = CONFIG_MAGIC + ".cost";
    public static final String CONFIG_MAGIC_COST_NAME = "Mana Cost";
    public static final String CONFIG_MAGIC_COST_COMMENT = "";
    public static final String CONFIG_MAGIC_COST_MIN = CONFIG_MAGIC_COST + ".min";
    public static final String CONFIG_MAGIC_COST_MIN_NAME = "Mana Cost Min";
    public static final String CONFIG_MAGIC_COST_MIN_COMMENT = "";
    public static final String CONFIG_MAGIC_COST_MAX = CONFIG_MAGIC_COST + ".max";
    public static final String CONFIG_MAGIC_COST_MAX_NAME = "Mana Cost Max";
    public static final String CONFIG_MAGIC_COST_MAX_COMMENT = "";
    public static final String CONFIG_MAGIC_AFFINITY = CONFIG_MAGIC + ".affinity";
    public static final String CONFIG_MAGIC_AFFINITY_NAME = "Magic Affinity";
    public static final String CONFIG_MAGIC_AFFINITY_COMMENT = "";
    public static final String CONFIG_MAGIC_BONUS = CONFIG_MAGIC + ".bonus";
    public static final String CONFIG_MAGIC_BONUS_NAME = "Default Bonus";
    public static final String CONFIG_MAGIC_BONUS_COMMENT = "Default Mana given per point of consumption from sources such as the Mana Crystal, or Meditating near a Moon Rose";
    public static final String CONFIG_MAGIC_BONUS_MAX = CONFIG_MAGIC_BONUS + ".cap";
    public static final String CONFIG_MAGIC_BONUS_MAX_NAME = "Max Bonuses";
    public static final String CONFIG_MAGIC_BONUS_MAX_COMMENT = "Maximum amount of times one can eat a Mana Crystal or Meditate near a moon rose for Bonus Mana";

    public static final String CONFIG_MAGIC_MAX = CONFIG_MAGIC + ".cap";
    public static final String CONFIG_MAGIC_MAX_NAME = "Cap Mana";
    public static final String CONFIG_MAGIC_MAX_COMMENT = "Cap the player Maximum Mana";
    public static final String CONFIG_MAGIC_MAX_AMOUNT = CONFIG_MAGIC_MAX + ".amount";
    public static final String CONFIG_MAGIC_MAX_AMOUNT_NAME = "Max Mana";
    public static final String CONFIG_MAGIC_MAX_AMOUNT_COMMENT = "Maximum mana the player can have before affinity bonuses are applied";
    public static final String CONFIG_MAGIC_MAX_AFFINITY = CONFIG_MAGIC_MAX + ".affinity";
    public static final String CONFIG_MAGIC_MAX_AFFINITY_NAME = "Cap Affinity";
    public static final String CONFIG_MAGIC_MAX_AFFINITY_COMMENT = "Cap Mana Affinity, bonuses are normally applied on top of Maximum Mana";

    public static final String CONFIG_MAGIC_REGEN = CONFIG_MAGIC + ".regen";
    public static final String CONFIG_MAGIC_REGEN_NAME = "Mana Regeneration";
    public static final String CONFIG_MAGIC_REGEN_COMMENT = "";
    public static final String CONFIG_MAGIC_REGEN_FREQUENCY = CONFIG_MAGIC_REGEN + ".frequency";
    public static final String CONFIG_MAGIC_REGEN_FREQUENCY_NAME = "Default Regeneration Frequency";
    public static final String CONFIG_MAGIC_REGEN_FREQUENCY_COMMENT = "How long in ticks to recover Mana";
    public static final String CONFIG_MAGIC_REGEN_TIMEOUT = CONFIG_MAGIC_REGEN + ".timeout";
    public static final String CONFIG_MAGIC_REGEN_TIMEOUT_NAME = "Default Regeneration Timeout";
    public static final String CONFIG_MAGIC_REGEN_TIMEOUT_COMMENT = "How long in ticks to wait before allowing regeneration after using Mana";

    public static final String CONFIG_MAGIC_ITEMS = CONFIG_MAGIC + ".items";
    public static final String CONFIG_MAGIC_ITEMS_NAME = "Magic Items";
    public static final String CONFIG_MAGIC_ITEMS_COMMENT = "";

    public static final String CONFIG_MAGIC_ITEMS_RECOVERY = CONFIG_MAGIC_ITEMS + ".recovery";
    public static final String CONFIG_MAGIC_ITEMS_RECOVERY_NAME = "Recovery Items";
    public static final String CONFIG_MAGIC_ITEMS_RECOVERY_COMMENT = "List of Items that recover mana.\\nmodid:item_name;meta;amount";

    public static final String CONFIG_MAGIC_ITEMS_CRYSTAL = CONFIG_MAGIC_ITEMS + ".crystal";
    public static final String CONFIG_MAGIC_ITEMS_CRYSTAL_NAME = "Mana Crystal";
    public static final String CONFIG_MAGIC_ITEMS_CRYSTAL_COMMENT = "";
    public static final String CONFIG_MAGIC_ITEMS_CRYSTAL_EXPLODE = CONFIG_MAGIC_ITEMS_CRYSTAL + ".explode";
    public static final String CONFIG_MAGIC_ITEMS_CRYSTAL_EXPLODE_NAME = "Explosive Crystals";
    public static final String CONFIG_MAGIC_ITEMS_CRYSTAL_EXPLODE_COMMENT = "Should Mana Crystals explode when smashed";
    public static final String CONFIG_MAGIC_ITEMS_REAGENT = CONFIG_MAGIC_ITEMS + ".reagent";
    public static final String CONFIG_MAGIC_ITEMS_REAGENT_NAME = "Mana Reagent";
    public static final String CONFIG_MAGIC_ITEMS_REAGENT_COMMENT = "";
    public static final String CONFIG_MAGIC_ITEMS_REAGENT_HARMFUL = CONFIG_MAGIC_ITEMS_REAGENT + ".harmful";
    public static final String CONFIG_MAGIC_ITEMS_REAGENT_HARMFUL_NAME = "Harmful consumption";
    public static final String CONFIG_MAGIC_ITEMS_REAGENT_HARMFUL_COMMENT = "Should the Mana Reagent cause negative effects when consumed";

    /**
     * xat.config.blocks
     */
    public static final String CONFIG_BLOCKS = CONFIG + ".blocks";
    public static final String CONFIG_BLOCKS_NAME = "Blocks";
    public static final String CONFIG_BLOCKS_COMMENT = "";

    public static final String CONFIG_BLOCKS_MOON_ROSE = CONFIG_BLOCKS + "." + TrinketsRegistryNames.ModBlocks.MOON_ROSE;
    public static final String CONFIG_BLOCKS_MOON_ROSE_NAME = "Moon Rose";
    public static final String CONFIG_BLOCKS_MOON_ROSE_COMMENT = "";
    public static final String CONFIG_BLOCKS_MOON_ROSE_ESSENCE = CONFIG_BLOCKS_MOON_ROSE + ".essence";
    public static final String CONFIG_BLOCKS_MOON_ROSE_ESSENCE_NAME = "Magic Essence";
    public static final String CONFIG_BLOCKS_MOON_ROSE_ESSENCE_COMMENT = "How much Magical Essence does the Moon Rose Have";
    public static final String CONFIG_BLOCKS_MOON_ROSE_ESSENCE_COOLDOWN = CONFIG_BLOCKS_MOON_ROSE_ESSENCE + ".cooldown";
    public static final String CONFIG_BLOCKS_MOON_ROSE_ESSENCE_COOLDOWN_NAME = "Absorption Cooldown";
    public static final String CONFIG_BLOCKS_MOON_ROSE_ESSENCE_COOLDOWN_COMMENT = "Time required in ticks to meditate near a Moon Rose to Absorb Magic Essence";
    public static final String CONFIG_BLOCKS_TEDDY_BEAR = CONFIG_BLOCKS + "." + TrinketsRegistryNames.ModBlocks.TEDDY_BEAR;
    public static final String CONFIG_BLOCKS_TEDDY_BEAR_NAME = "Teddy Bear";
    public static final String CONFIG_BLOCKS_TEDDY_BEAR_COMMENT = "";

    /**
     * xat.config.items
     */
    public static final String CONFIG_ITEMS = CONFIG + ".items";
    public static final String CONFIG_ITEMS_NAME = "Items";
    public static final String CONFIG_ITEMS_COMMENT = "";

    public static final String CONFIG_ITEMS_ELEMENTS_TOOLTIP = CONFIG_ITEMS + ".tooltip.elements";
    public static final String CONFIG_ITEMS_ELEMENTS_TOOLTIP_NAME = "Element Tooltips";
    public static final String CONFIG_ITEMS_ELEMENTS_TOOLTIP_COMMENT = "";

    public static final String CONFIG_ITEMS_ARCING_ORB = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.ARCING_ORB;
    public static final String CONFIG_ITEMS_ARCING_ORB_COMMENT = "";
    public static final String CONFIG_ITEMS_ARCING_ORB_NAME = "Arcing Orb";
    public static final String CONFIG_ITEMS_DRAGONS_EYE = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.DRAGONS_EYE;
    public static final String CONFIG_ITEMS_DRAGONS_EYE_COMMENT = "";
    public static final String CONFIG_ITEMS_DRAGONS_EYE_NAME = "Dragons Eye";
    public static final String CONFIG_ITEMS_ENDER_CROWN = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.ENDER_TIARA;
    public static final String CONFIG_ITEMS_ENDER_CROWN_COMMENT = "";
    public static final String CONFIG_ITEMS_ENDER_CROWN_NAME = "Ender Queens Crown";
    public static final String CONFIG_ITEMS_FAELIS_CLAWS = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.FAELIS_CLAWS;
    public static final String CONFIG_ITEMS_FAELIS_CLAWS_COMMENT = "";
    public static final String CONFIG_ITEMS_FAELIS_CLAWS_NAME = "Faelis Claws";
    public static final String CONFIG_ITEMS_GLOW_RING = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.GLOW_RING;
    public static final String CONFIG_ITEMS_GLOW_RING_COMMENT = "";
    public static final String CONFIG_ITEMS_GLOW_RING_NAME = "Ring of Enchanted Eyes";
    public static final String CONFIG_ITEMS_INERTIA_STONE = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.GREATER_INERTIA_STONE;
    public static final String CONFIG_ITEMS_INERTIA_STONE_COMMENT = "";
    public static final String CONFIG_ITEMS_INERTIA_STONE_NAME = "Stone of Greater Inertia";
    public static final String CONFIG_ITEMS_NULL_STONE = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.INERTIA_NULL_STONE;
    public static final String CONFIG_ITEMS_NULL_STONE_COMMENT = "";
    public static final String CONFIG_ITEMS_NULL_STONE_NAME = "Stone of Inertia Null";
    public static final String CONFIG_ITEMS_POISON_STONE = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.POISON_STONE;
    public static final String CONFIG_ITEMS_POISON_STONE_COMMENT = "";
    public static final String CONFIG_ITEMS_POISON_STONE_NAME = "Poison Stone";
    public static final String CONFIG_ITEMS_POLARIZED_STONE = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.POLARIZED_STONE;
    public static final String CONFIG_ITEMS_POLARIZED_STONE_COMMENT = "";
    public static final String CONFIG_ITEMS_POLARIZED_STONE_NAME = "Polarized Stone";
    public static final String CONFIG_ITEMS_SEA_STONE = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.SEA_STONE;
    public static final String CONFIG_ITEMS_SEA_STONE_COMMENT = "";
    public static final String CONFIG_ITEMS_SEA_STONE_NAME = "Stone of The Sea";
    public static final String CONFIG_ITEMS_SHIELD_OF_HONOR = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.HONOR_SHIELD;
    public static final String CONFIG_ITEMS_SHIELD_OF_HONOR_COMMENT = "";
    public static final String CONFIG_ITEMS_SHIELD_OF_HONOR_NAME = "Shield of Honor";
    public static final String CONFIG_ITEMS_TEDDY_BEAR = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.TEDDY_BEAR;
    public static final String CONFIG_ITEMS_TEDDY_BEAR_COMMENT = "";
    public static final String CONFIG_ITEMS_TEDDY_BEAR_NAME = "Teddy Bear";
    public static final String CONFIG_ITEMS_WEIGHTLESS_STONE = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.WEIGHTLESS_STONE;
    public static final String CONFIG_ITEMS_WEIGHTLESS_STONE_COMMENT = "";
    public static final String CONFIG_ITEMS_WEIGHTLESS_STONE_NAME = "Stone of Weightlessness";
    public static final String CONFIG_ITEMS_WITHER_RING = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.WITHER_RING;
    public static final String CONFIG_ITEMS_WITHER_RING_COMMENT = "";
    public static final String CONFIG_ITEMS_WITHER_RING_NAME = "Wither Ring";

    public static final String CONFIG_ITEMS_TRANSFORMATION = CONFIG_ITEMS + ".transformation";
    public static final String CONFIG_ITEMS_TRANSFORMATION_NAME = "Transformation Items";
    public static final String CONFIG_ITEMS_TRANSFORMATION_COMMENT = "";

    public static final String CONFIG_ITEMS_RING_DRAGON = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_DRAGON;
    public static final String CONFIG_ITEMS_RING_DRAGON_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_DRAGON_NAME = "Dragon Ring";
    public static final String CONFIG_ITEMS_RING_DWARF = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_DWARF;
    public static final String CONFIG_ITEMS_RING_DWARF_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_DWARF_NAME = "Dwarf Ring";
    public static final String CONFIG_ITEMS_RING_ELF = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_ELF;
    public static final String CONFIG_ITEMS_RING_ELF_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_ELF_NAME = "Elf Ring";
    public static final String CONFIG_ITEMS_RING_FAELIS = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_FAELIS;
    public static final String CONFIG_ITEMS_RING_FAELIS_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_FAELIS_NAME = "Faelis Ring";
    public static final String CONFIG_ITEMS_RING_FAIRY = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_FAIRY;
    public static final String CONFIG_ITEMS_RING_FAIRY_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_FAIRY_NAME = "Fairy Ring";
    public static final String CONFIG_ITEMS_RING_GOBLIN = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_GOBLIN;
    public static final String CONFIG_ITEMS_RING_GOBLIN_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_GOBLIN_NAME = "Goblin Ring";
    public static final String CONFIG_ITEMS_RING_SLIME = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_SLIME;
    public static final String CONFIG_ITEMS_RING_SLIME_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_SLIME_NAME = "Slime Ring";
    public static final String CONFIG_ITEMS_RING_SUCCUBUS = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_SUCCUBUS;
    public static final String CONFIG_ITEMS_RING_SUCCUBUS_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_SUCCUBUS_NAME = "Succubus Ring";
    public static final String CONFIG_ITEMS_RING_TAURUS = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_TAURUS;
    public static final String CONFIG_ITEMS_RING_TAURUS_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_TAURUS_NAME = "Taurus Ring";
    public static final String CONFIG_ITEMS_RING_TITAN = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.RING_TITAN;
    public static final String CONFIG_ITEMS_RING_TITAN_COMMENT = "";
    public static final String CONFIG_ITEMS_RING_TITAN_NAME = "Titan Ring";

    public static final String CONFIG_ITEMS_COSMETIC = CONFIG_ITEMS + "." + TrinketsRegistryNames.ModItems.COSMETIC;
    public static final String CONFIG_ITEMS_COSMETIC_NAME = "Cosmetic Item";
    public static final String CONFIG_ITEMS_COSMETIC_COMMENT = "";
    /**
     * xat.config.items.registry
     */
    public static final String CONFIG_ITEMS_REGISTRY = CONFIG_ITEMS + ".registry";

    /**
     * xat.config.food
     */
    public static final String CONFIG_FOOD = CONFIG_ITEMS + ".food";
    public static final String CONFIG_FOOD_NAME = "Magical Foods";
    public static final String CONFIG_FOOD_COMMENT = "Configuration for Food Items";
    public static final String CONFIG_FOOD_TRANSFORMATION = CONFIG_FOOD + ".transformation";
    public static final String CONFIG_FOOD_TRANSFORMATION_NAME = "Food Transformations";
    public static final String CONFIG_FOOD_TRANSFORMATION_COMMENT = "Should Food Items provide race transformations when eaten";
    public static final String CONFIG_FOOD_TRANSFORMATION_KEEP = CONFIG_FOOD_TRANSFORMATION + ".keep";
    public static final String CONFIG_FOOD_TRANSFORMATION_KEEP_NAME = "Keep Transformations";
    public static final String CONFIG_FOOD_TRANSFORMATION_KEEP_COMMENT = "Should the Transformation effect persist through Death";

    /**
     * xat.config.food.registry
     */
    public static final String CONFIG_FOOD_REGISTRY = CONFIG_FOOD + ".registry";
    public static final String CONFIG_FOOD_REGISTRY_NAME = "Magical Food Registry";
    public static final String CONFIG_FOOD_REGISTRY_COMMENT = "Magical Food Registry Configuration";

    public static final String CONFIG_FOOD_REGISTRY_ENABLED = CONFIG_FOOD_REGISTRY + ".enabled";
    public static final String CONFIG_FOOD_REGISTRY_ENABLED_NAME = "Enable Magical Foods";
    public static final String CONFIG_FOOD_REGISTRY_ENABLED_COMMENT = "Should this mod add Magical Foods?. Set to False to Disable. Default True";

    /**
     * xat.config.potions
     */
    public static final String CONFIG_POTIONS = CONFIG + ".potions";
    public static final String CONFIG_POTIONS_NAME = "Potion Configuration";
    public static final String CONFIG_POTIONS_COMMENT = "Configuration for potions";
    public static final String CONFIG_POTIONS_DURATION = CONFIG_POTIONS + ".duration";
    public static final String CONFIG_POTIONS_DURATION_NAME = "Duration";
    public static final String CONFIG_POTIONS_DURATION_COMMENT = "How long this effect should last in ticks\\n20 ticks per second";
    public static final String CONFIG_POTIONS_TICKS = CONFIG_POTIONS + ".ticks";
    public static final String CONFIG_POTIONS_TICKS_NAME = "Ticks";
    public static final String CONFIG_POTIONS_TICKS_COMMENT = "";
    public static final String CONFIG_POTIONS_CATALYST = CONFIG_POTIONS + ".catalyst";
    public static final String CONFIG_POTIONS_CATALYST_NAME = "Catalyst";
    public static final String CONFIG_POTIONS_CATALYST_COMMENT = "The Item used as a Catalyst to brew this Potion";
    public static final String CONFIG_POTIONS_AMPLIFIER = CONFIG_POTIONS + ".amplifier";
    public static final String CONFIG_POTIONS_AMPLIFIER_NAME = "Amplifier";
    public static final String CONFIG_POTIONS_AMPLIFIER_COMMENT = "";
    public static final String CONFIG_POTIONS_SURVIVAL_WATER = CONFIG_POTIONS + ".water";
    public static final String CONFIG_POTIONS_SURVIVAL_WATER_NAME = "Potions give water";
    public static final String CONFIG_POTIONS_SURVIVAL_WATER_COMMENT = "Should Potions give Water if a survival mod is installed.";

    public static final String CONFIG_POTIONS_RESISTANCE = CONFIG_POTIONS + ".resistance";
    public static final String CONFIG_POTIONS_RESISTANCE_ICE = CONFIG_POTIONS_RESISTANCE + ".ice";
    public static final String CONFIG_POTIONS_RESISTANCE_ICE_NAME = "Ice Resistance Potion";
    public static final String CONFIG_POTIONS_RESISTANCE_ICE_COMMENT = "";
    public static final String CONFIG_POTIONS_RESISTANCE_LIGHTNING = CONFIG_POTIONS_RESISTANCE + ".lightning";
    public static final String CONFIG_POTIONS_RESISTANCE_LIGHTNING_NAME = "Lightning Resistance Potion";
    public static final String CONFIG_POTIONS_RESISTANCE_LIGHTNING_COMMENT = "";

    public static final String CONFIG_POTIONS_RACE = CONFIG_POTIONS + ".race";
    public static final String CONFIG_POTIONS_RACE_DRAGON = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.DRAGON;
    public static final String CONFIG_POTIONS_RACE_DRAGON_NAME = "Potion of Dragons";
    public static final String CONFIG_POTIONS_RACE_DRAGON_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_DRAGON_FIRE = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.DRAGON + "." + TrinketsRegistryNames.ModElements.FIRE;
    public static final String CONFIG_POTIONS_RACE_DRAGON_FIRE_NAME = "Potion of Fire Dragons";
    public static final String CONFIG_POTIONS_RACE_DRAGON_FIRE_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_DRAGON_ICE = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.DRAGON + "." + TrinketsRegistryNames.ModElements.ICE;
    public static final String CONFIG_POTIONS_RACE_DRAGON_ICE_NAME = "Potion of Ice Dragons";
    public static final String CONFIG_POTIONS_RACE_DRAGON_ICE_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_DRAGON_LIGHTNING = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.DRAGON + "." + TrinketsRegistryNames.ModElements.LIGHTNING;
    public static final String CONFIG_POTIONS_RACE_DRAGON_LIGHTNING_NAME = "Potion of Lightning Dragons";
    public static final String CONFIG_POTIONS_RACE_DRAGON_LIGHTNING_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_DWARF = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.DWARF;
    public static final String CONFIG_POTIONS_RACE_DWARF_NAME = "Potion of Dwarves";
    public static final String CONFIG_POTIONS_RACE_DWARF_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_ELF = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.ELF;
    public static final String CONFIG_POTIONS_RACE_ELF_NAME = "Potion of Elves";
    public static final String CONFIG_POTIONS_RACE_ELF_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_FAELIS = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.FAELIS;
    public static final String CONFIG_POTIONS_RACE_FAELIS_NAME = "Potion of Faelis";
    public static final String CONFIG_POTIONS_RACE_FAELIS_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_FAIRY = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.FAIRY;
    public static final String CONFIG_POTIONS_RACE_FAIRY_NAME = "Potion of Fairies";
    public static final String CONFIG_POTIONS_RACE_FAIRY_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_GOBLIN = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.GOBLIN;
    public static final String CONFIG_POTIONS_RACE_GOBLIN_NAME = "Potion of Goblins";
    public static final String CONFIG_POTIONS_RACE_GOBLIN_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_HUMAN = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.HUMAN;
    public static final String CONFIG_POTIONS_RACE_HUMAN_NAME = "Potion of Normie";
    public static final String CONFIG_POTIONS_RACE_HUMAN_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_TAURUS = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.TAURUS;
    public static final String CONFIG_POTIONS_RACE_TAURUS_NAME = "Potion of Taurus";
    public static final String CONFIG_POTIONS_RACE_TAURUS_COMMENT = "";
    public static final String CONFIG_POTIONS_RACE_TITAN = CONFIG_POTIONS_RACE + "." + TrinketsRegistryNames.ModRaces.TITAN;
    public static final String CONFIG_POTIONS_RACE_TITAN_NAME = "Potion of Titans";
    public static final String CONFIG_POTIONS_RACE_TITAN_COMMENT = "";
    /**
     * xat.config.potions.registry
     */
    public static final String CONFIG_POTIONS_REGISTRY = CONFIG_POTIONS + ".registry";
    public static final String CONFIG_POTIONS_REGISTRY_NAME = "Potions Registry";
    public static final String CONFIG_POTIONS_REGISTRY_COMMENT = "Potion Registry Configuration, Disable to unregister T&B Potions";

    /**
     * xat.config.misc
     */
    public static final String CONFIG_MISC = CONFIG + ".misc";
    public static final String CONFIG_MISC_NAME = "Misc Settings";
    public static final String CONFIG_MISC_COMMENT = "";

    public static final String CONFIG_FIRE_TIERS = CONFIG + ".firetiers";
    public static final String CONFIG_FIRE_TIERS_NAME = "Fire Resistance Tiers";
    public static final String CONFIG_FIRE_TIERS_COMMENT = "";
    public static final String CONFIG_ARTEMIS_LIB = CONFIG + ".artemislib";
    public static final String CONFIG_ARTEMIS_LIB_NAME = "Artemis Lib";
    public static final String CONFIG_ARTEMIS_LIB_COMMENT = "";
    public static final String CONFIG_FIRST_AID = CONFIG + ".firstaid";
    public static final String CONFIG_FIRST_AID_NAME = "First Aid";
    public static final String CONFIG_FIRST_AID_COMMENT = "";
    public static final String CONFIG_ELENAI_DODGE = CONFIG + ".elenaidodge";
    public static final String CONFIG_ELENAI_DODGE_NAME = "Elenai Dodge";
    public static final String CONFIG_ELENAI_DODGE_COMMENT = "";
    public static final String CONFIG_TOUGH_AS_NAILS = CONFIG + ".toughasnails";
    public static final String CONFIG_TOUGH_AS_NAILS_NAME = "Tough as Nails";
    public static final String CONFIG_TOUGH_AS_NAILS_COMMENT = "";
    public static final String CONFIG_SIMPLE_DIIFFICULTY = CONFIG + ".simpledifficulty";
    public static final String CONFIG_SIMPLE_DIIFFICULTY_NAME = "Simple Difficulty";
    public static final String CONFIG_SIMPLE_DIIFFICULTY_COMMENT = "";
    public static final String CONFIG_ENHANCED_VISUALS = CONFIG + ".enhancedvisuals";
    public static final String CONFIG_ENHANCED_VISUALS_NAME = "Enhanced Visuals";
    public static final String CONFIG_ENHANCED_VISUALS_COMMENT = "";
    public static final String CONFIG_BETTER_DIVING = CONFIG + ".betterdiving";
    public static final String CONFIG_BETTER_DIVING_NAME = "Better Diving";
    public static final String CONFIG_BETTER_DIVING_COMMENT = "";
    public static final String CONFIG_MO_BENDS = CONFIG + ".mobends";
    public static final String CONFIG_MO_BENDS_NAME = "Mo Bends";
    public static final String CONFIG_MO_BENDS_COMMENT = "";
    public static final String CONFIG_IDO_SWIMMING = CONFIG_COMPAT + ".ido_swimming";
    public static final String CONFIG_IDO_SWIMMING_NAME = "Ido Swimming Compatibility";
    public static final String CONFIG_IDO_SWIMMING_COMMENT = "Let Ido control temporary player dimensions while it handles swimming. Prevents Trinkets swimming size changes from competing with Ido.";
    public static final String CONFIG_TROPICRAFT_SWIMMING = CONFIG_COMPAT + ".tropicraft_swimming";
    public static final String CONFIG_TROPICRAFT_SWIMMING_NAME = "Tropicraft Swimming Compatibility";
    public static final String CONFIG_TROPICRAFT_SWIMMING_COMMENT = "Let Tropicraft control temporary player dimensions while it handles swimming. Prevents Trinkets swimming size changes from competing with Tropicraft.";
    /**
     * xat.config.debug
     */
    public static final String CONFIG_DEBUG = CONFIG + ".debug";
    public static final String CONFIG_DEBUG_NAME = "Debug";
    public static final String CONFIG_DEBUG_COMMENT = "";

    public static final String CONFIG_BAUBLES = CONFIG + ".baubles";
    public static final String CONFIG_BAUBLES_NAME = "Baubles";
    public static final String CONFIG_BAUBLES_COMMENT = "If the mod Baubles is installed what bauble slot should it use\\n,Available Types:\\n,Trinket, Any, All\\n,Amulet, Necklace, Pendant\\n,Ring, Rings\\n,Belt\\n,Head, Hat\\n,Body, Chest\\n,Charm";

    public static final String CONFIG_ICE_AND_FIRE = CONFIG_COMPAT + ".iceandfire";
    public static final String CONFIG_ICE_AND_FIRE_NAME = "Ice and Fire";
    public static final String CONFIG_ICE_AND_FIRE_COMMENT = "Configuration for Ice and Fire";

    public static final String CONFIG_ICE_AND_FIRE_STAGE = CONFIG_ICE_AND_FIRE + ".recipe.stage";
    public static final String CONFIG_ICE_AND_FIRE_STAGE_NAME = "Skull Stage";
    public static final String CONFIG_ICE_AND_FIRE_STAGE_COMMENT = "If Ice and Fire is Installed, Should the Dragon's Eye Require a Specific Stage of Skull for the recipe. Set to 0 for any stage";

    public static final String CONFIG_DEFILED_LANDS = CONFIG_COMPAT + ".defiledlands";
    public static final String CONFIG_DEFILED_LANDS_NAME = "Defiled Lands";
    public static final String CONFIG_DEFILED_LANDS_COMMENT = "";

    public static final String CONFIG_LYCANITES_MOBS = CONFIG_COMPAT + ".lycanitesmobs";
    public static final String CONFIG_LYCANITES_MOBS_NAME = "Lycanites Mobs";
    public static final String CONFIG_LYCANITES_MOBS_COMMENT = "";

    public static final String CONFIG_SURVIVAL = CONFIG_COMPAT + ".survival";
    public static final String CONFIG_SURVIVAL_NAME = "Survival Mods";
    public static final String CONFIG_SURVIVAL_COMMENT = "";
    public static final String CONFIG_SURVIVAL_HEAT = CONFIG_SURVIVAL + ".heat";
    public static final String CONFIG_SURVIVAL_HEAT_NAME = "Heat";
    public static final String CONFIG_SURVIVAL_HEAT_COMMENT = "";
    public static final String CONFIG_SURVIVAL_COLD = CONFIG_SURVIVAL + ".cold";
    public static final String CONFIG_SURVIVAL_COLD_NAME = "Cold";
    public static final String CONFIG_SURVIVAL_COLD_COMMENT = "";
    public static final String CONFIG_SURVIVAL_THIRST = CONFIG_SURVIVAL + ".thirst";
    public static final String CONFIG_SURVIVAL_THIRST_NAME = "Thirst";
    public static final String CONFIG_SURVIVAL_THIRST_COMMENT = "";
    public static final String CONFIG_SURVIVAL_PARASITES = CONFIG_SURVIVAL + ".parasites";
    public static final String CONFIG_SURVIVAL_PARASITES_NAME = "Parasites";
    public static final String CONFIG_SURVIVAL_PARASITES_COMMENT = "";
    public static final String CONFIG_SURVIVAL_THIRST_ABSORPTION = CONFIG_SURVIVAL_THIRST + ".absorption";
    public static final String CONFIG_SURVIVAL_THIRST_ABSORPTION_NAME = "Water Absorption";
    public static final String CONFIG_SURVIVAL_THIRST_ABSORPTION_COMMENT = "";

}
