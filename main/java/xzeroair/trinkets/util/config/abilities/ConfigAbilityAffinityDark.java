package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityAffinityDark {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK;

    public ConfigAbilityAffinityDark() {
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_LEECH_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_LEECH_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_LEECH)
    public boolean TRUE_LEECH = false;

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_LEECH_AMOUNT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_LEECH_AMOUNT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_LEECH_AMOUNT)
    @Config.RangeInt(min = 0)
    public float LEECH_AMOUNT = 2f;

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_WITHER_CHANCE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_WITHER_CHANCE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_WITHER_CHANCE)
    @Config.RangeInt(min = 0)
    public int CHANCE = 5;

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_WITHER_DURATION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_WITHER_DURATION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_WITHER_DURATION)
    @Config.RangeInt(min = 0)
    public int DURATION = 40;

}
