package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.enums.ActivationMethod;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityLargeHands {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_LARGE_HANDS;

    public ConfigAbilityLargeHands() {
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_LARGE_HANDS_MINING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LARGE_HANDS_MINING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LARGE_HANDS_MINING)
    public ActivationMethod MINING_EXTENDED = ActivationMethod.STAND;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_LARGE_HANDS_MINING_BLACKLIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LARGE_HANDS_MINING_BLACKLIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LARGE_HANDS_MINING_BLACKLIST)
    public String[] MINING_EXTENDED_BLACKLIST = {"dynamictrees:*"};

}
