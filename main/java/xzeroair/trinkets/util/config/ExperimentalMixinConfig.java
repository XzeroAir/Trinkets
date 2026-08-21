package xzeroair.trinkets.util.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ExperimentalMixinConfig {
    @Config.Name(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_VANILLA_PLAYER_SIZE_UPDATES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_VANILLA_PLAYER_SIZE_UPDATES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_VANILLA_PLAYER_SIZE_UPDATES)
    @Config.RequiresMcRestart
    public boolean VANILLA_PLAYER_SIZE_UPDATES = false;


    @Config.Name(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS)
    @Config.RequiresMcRestart
    public boolean RACE_SCALED_PLAYER_SHADOWS = false;

}
