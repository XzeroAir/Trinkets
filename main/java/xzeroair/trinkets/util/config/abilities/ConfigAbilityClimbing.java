package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.ConfigDefaultReusedConstants;

public class ConfigAbilityClimbing {

    public ConfigAbilityClimbing() {
        this(true);
    }

    public ConfigAbilityClimbing(boolean canClimb) {
        this(canClimb, true);
    }

    public ConfigAbilityClimbing(boolean canClimb, boolean whitelist) {
        this(canClimb, whitelist, ConfigDefaultReusedConstants.DEFAULT_CLIMB_LIST);
    }

    public ConfigAbilityClimbing(boolean canClimb, boolean whitelist, String[] list) {
        this.ENABLED = canClimb;
        this.USE_WHITELIST = whitelist;
        this.BLOCKS = list;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_WHITELIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_WHITELIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_WHITELIST)
    public boolean USE_WHITELIST;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING_BLOCKS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING_BLOCKS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING_BLOCKS)
    public String[] BLOCKS;
}
