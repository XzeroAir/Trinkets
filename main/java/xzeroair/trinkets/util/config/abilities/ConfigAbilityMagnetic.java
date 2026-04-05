package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityMagnetic {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC;

    public ConfigAbilityMagnetic() {
        this(0F, 20);
    }

    public ConfigAbilityMagnetic(float cost, int ticks) {
        this.COST = cost;
        this.FREQUENCY = ticks;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC_INSTANT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC_INSTANT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC_INSTANT)
    public boolean PICKUP_INSTANT = true;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC_INSTANT_XP_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC_INSTANT_XP_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC_INSTANT_XP)
    public boolean PICKUP_INSTANT_XP = true;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC_XP_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC_XP_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC_XP)
    public boolean PICKUP_XP = true;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_MAGIC_COST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_COST)
    @Config.RangeDouble(min = 0)
    public float COST;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_FREQUENCY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_FREQUENCY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_FREQUENCY)
    @Config.RangeInt(min = 1)
    public int FREQUENCY;

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_FORCE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_FORCE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_FORCE)
    @Config.RangeDouble(min = 0.1, max = 1)
    public double FORCE = 0.1;

    @Config.Ignore
    @Config.Name("07. " + ConstantsConfigLang.CONFIG_WHITELIST_INVERTED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_WHITELIST_INVERTED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_WHITELIST_INVERTED)
    public boolean WHITELIST_INVERTED = true;

    @Config.Ignore
    @Config.Name("08. " + ConstantsConfigLang.CONFIG_WHITELIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_WHITELIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_WHITELIST)
    public String[] WHITELIST = new String[]{
            //@formatter:off
            //@formatter:on
    };

    @Config.Name(ConstantsConfigLang.CONFIG_RANGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RANGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RANGE)
    public DetectionRange RANGE = new DetectionRange();

    public class DetectionRange {

        @Config.Comment(ConstantsConfigLang.CONFIG_RANGE_VERTICAL_COMMENT)
        @Config.Name(ConstantsConfigLang.CONFIG_RANGE_VERTICAL_NAME)
        @Config.LangKey(ConstantsConfigLang.CONFIG_RANGE_VERTICAL)
        @Config.RangeInt(min = 0, max = 32)
        public int RANGE_VERTICAL = 6;

        @Config.Comment(ConstantsConfigLang.CONFIG_RANGE_HORIZONTAL_COMMENT)
        @Config.Name(ConstantsConfigLang.CONFIG_RANGE_HORIZONTAL_NAME)
        @Config.LangKey(ConstantsConfigLang.CONFIG_RANGE_HORIZONTAL)
        @Config.RangeInt(min = 0, max = 32)
        public int RANGE_HORIZONTAL = 12;
    }
}
