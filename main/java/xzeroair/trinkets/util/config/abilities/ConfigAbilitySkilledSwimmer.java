package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilitySkilledSwimmer {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_SWIMMER;

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_SWIMMER_OLD_TWEAKS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_SWIMMER_OLD_TWEAKS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_SWIMMER_OLD_TWEAKS)
    public boolean OLD_TWEAKS = false;

}
