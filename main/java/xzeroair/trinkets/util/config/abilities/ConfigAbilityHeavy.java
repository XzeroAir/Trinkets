package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityHeavy {

    public ConfigAbilityHeavy() {
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_HEAVY_TRAMPLE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_HEAVY_TRAMPLE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_HEAVY_TRAMPLE)
    public boolean TRAMPLE = true;

}
