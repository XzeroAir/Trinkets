package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityAffinityWater {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER;

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_BUBBLES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_BUBBLES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_BUBBLES)
    @Config.RangeInt(min = 0, max = 10)
    public int BUBBLES = 1;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_MINING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_MINING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_MINING)
    public boolean UNDERWATER_MINING = true;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_VANILLA_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_VANILLA_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER_VANILLA)
    public boolean VANILLA = false;

}
