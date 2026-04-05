package xzeroair.trinkets.util.config.abilities.external.firstaid;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityHardHead {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_FIRST_AID_HARD_HEAD;

    public ConfigAbilityHardHead() {
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_CHANCE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_CHANCE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_FIRST_AID_HARD_HEAD_CHANCE)
    public int CHANCE = 100;

}
