package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.enums.ActivationMethod;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityGreedyEyes {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES;
    private final String LANG_BLOCKS = ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_BLOCKS;

    public ConfigAbilityGreedyEyes() {
        this(0F);
    }

    public ConfigAbilityGreedyEyes(float cost) {
        this.COST = cost;
    }

    @Config.Name(ConstantsConfigLang.CONFIG_CLIENT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_CLIENT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_CLIENT)
    public ClientConfig CLIENT = new ClientConfig();

    public class ClientConfig {

        @Config.Name("00. " + ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_CLIENT_PARTICLES_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_CLIENT_PARTICLES_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_CLIENT_PARTICLES)
        @Config.RangeInt(min = 1)
        public int PARTICLES = 255;

        @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_CLIENT_GROWL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_CLIENT_GROWL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_CLIENT_GROWL)
        public ActivationMethod GROWL_ACTIVATION = ActivationMethod.SNEAK;

        @Config.Name("02. " + ConstantsConfigLang.CONFIG_VOLUME_CONTROL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_VOLUME_CONTROL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_VOLUME_CONTROL)
        @Config.RangeInt(min = 0, max = 300)
        public int VOLUME = 100;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_MAGIC_COST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_COST)
    public float COST;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_CLOSEST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_CLOSEST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_CLOSEST)
    public boolean CLOSEST = true;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_FREQUENCY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_FREQUENCY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_FREQUENCY)
    @Config.RangeInt(min = 20, max = 360)
    public int FREQUENCY = 79;

    @Config.RequiresWorldRestart
    @Config.Name("04. " + ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_BLOCKS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES_BLOCKS)
    public String[] BLOCKS = new String[]{
            //@formatter:off
            "oreCoal;#464646",
            "oreIron;#FFCC99",
            "oreGold;#FFD700",
            "oreLapis;#26619C",
            "oreRedstone;#B02E26",
            "oreDiamond;#00E6FF",
            "oreEmerald;#00FF4D",
            "oreQuartz;#EBEBEB",
            "minecraft:chest;*;#FFD700",
            "minecraft:chest_minecart;#FFD700"
            //@formatter:on
    };

    @Config.Name(ConstantsConfigLang.CONFIG_RANGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RANGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RANGE)
    public DetectionRange RANGE = new DetectionRange();

    public class DetectionRange {

        @Config.RequiresWorldRestart
        @Config.Name(ConstantsConfigLang.CONFIG_RANGE_VERTICAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_RANGE_VERTICAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_RANGE_VERTICAL)
        @Config.RangeInt(min = 0, max = 32)
        public int RANGE_VERTICAL = 6;

        @Config.RequiresWorldRestart
        @Config.Name(ConstantsConfigLang.CONFIG_RANGE_HORIZONTAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_RANGE_HORIZONTAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_RANGE_HORIZONTAL)
        @Config.RangeInt(min = 0, max = 32)
        public int RANGE_HORIZONTAL = 12;
    }
}
