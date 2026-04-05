package xzeroair.trinkets.util.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.blocks.ConfigBlocksMain;
import xzeroair.trinkets.util.config.elements.TrinketElementsConfig;
import xzeroair.trinkets.util.config.mana.EntityManaConfig;
import xzeroair.trinkets.util.config.potions.ConfigPotionMain;
import xzeroair.trinkets.util.config.race.ConfigRaces;
import xzeroair.trinkets.util.config.trinkets.TrinketItemsConfig;

public class ServerConfig {

    @Config.Name(ConstantsConfigLang.CONFIG_CONTAINER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_CONTAINER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_CONTAINER)
    public ConfigTrinketContainer GUI = new ConfigTrinketContainer();

    public class ConfigTrinketContainer {

        @Config.RequiresMcRestart
        @Config.RequiresWorldRestart
        @Config.Name(ConstantsConfigLang.CONFIG_CONTAINER_ENABLED_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_CONTAINER_ENABLED_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_CONTAINER_ENABLED)
        public boolean ENABLED = true;

        @Config.RequiresMcRestart
        @Config.RequiresWorldRestart
        @Config.Name(ConstantsConfigLang.CONFIG_CONTAINER_SLOTS_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_CONTAINER_SLOTS_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_CONTAINER_SLOTS)
        @Config.RangeInt(min = 1, max = 32)
        public int SLOTS = 8;

        @Config.Name(ConstantsConfigLang.CONFIG_CONTAINER_TRINKETS_ONLY_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_CONTAINER_TRINKETS_ONLY_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_CONTAINER_TRINKETS_ONLY)
        public boolean TRINKETS_CONTAINER_ALLOW_BAUBLES = false;

    }

    @Config.Name(ConstantsConfigLang.CONFIG_FOOD_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_FOOD_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_FOOD)
    public Foods FOOD = new Foods();

    public class Foods {

        @Config.RequiresMcRestart
        @Config.Name(ConstantsConfigLang.CONFIG_FOOD_REGISTRY_ENABLED_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_FOOD_REGISTRY_ENABLED_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_FOOD_REGISTRY_ENABLED)
        public boolean ENABLED = true;

        @Config.Name(ConstantsConfigLang.CONFIG_FOOD_TRANSFORMATION_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_FOOD_TRANSFORMATION_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_FOOD_TRANSFORMATION)
        public boolean EFFECTS = true;

        @Config.Name(ConstantsConfigLang.CONFIG_FOOD_TRANSFORMATION_KEEP_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_FOOD_TRANSFORMATION_KEEP_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_FOOD_TRANSFORMATION_KEEP)
        public boolean KEEP_EFFECTS = true;
    }

    @Config.Name(ConstantsConfigLang.CONFIG_BLOCKS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLOCKS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLOCKS)
    public ConfigBlocksMain BLOCKS = new ConfigBlocksMain();

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS)
    public ConfigPotionMain POTIONS = new ConfigPotionMain();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS)
    public TrinketItemsConfig ITEMS = new TrinketItemsConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC)
    public EntityManaConfig MAGIC = new EntityManaConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES)
    public ConfigRaces RACES = new ConfigRaces();

    @Config.Ignore
    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES)
    public ConfigDefaultAbilityStorage ABILITIES = new ConfigDefaultAbilityStorage();

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS)
    public TrinketElementsConfig ELEMENTS = new TrinketElementsConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_MISC_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MISC_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MISC)
    public MiscConfigs MISC = new MiscConfigs();

    public class MiscConfigs {
        @Config.RequiresWorldRestart
        @Config.Comment("Does Depth Strider Stack with Swim Speed Attributes?")
        @Config.Name("Depth Strider Stacks")
        @Config.LangKey(ConstantsConfigLang.CONFIG_MISC + ".depth")
        public boolean DEPTH_STACKS = false;

        @Config.RequiresWorldRestart
        @Config.Comment("Does Underwater Strider from Rins So many Enchantments Stack with Swim Speed Attributes?")
        @Config.Name("Underwater Strider Stacks")
        @Config.LangKey(ConstantsConfigLang.CONFIG_MISC + ".sme.underwaterstrider")
        public boolean STRIDER_STACKS = false;

        @Config.RequiresWorldRestart
        @Config.Comment("If enabled, the player will be unable to move when transforming from one race to another")
        @Config.Name("Prevent Movement while transforming")
        @Config.LangKey(ConstantsConfigLang.CONFIG_MISC + ".movement")
        public boolean MOVEMENT = false;

        @Config.Comment("Vanilla MC doesn't handle interaction with increased reach properly, this fixes it")
        @Config.Name("Reach Interaction Fix")
        @Config.LangKey(ConstantsConfigLang.CONFIG_MISC + ".interaction.fix")
        public boolean REACH = true;

        @Config.Comment("The VIP list may be unaccessible under some circumstance, use this to shorten game launching time.")
        @Config.Name("VIP")
        @Config.LangKey(ConstantsConfigLang.CONFIG_MISC + ".vip")
        public boolean VIPS = true;

        @Config.Comment("Hidden feature, add anything into this list to disable it.")
        @Config.Name("Blessings")
        @Config.LangKey(ConstantsConfigLang.CONFIG_MISC + ".blessings")
        public String[] Blessings = {};
    }

}
