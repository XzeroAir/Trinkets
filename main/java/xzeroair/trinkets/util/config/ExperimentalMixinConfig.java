package xzeroair.trinkets.util.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ExperimentalMixinConfig {
    @Config.Name(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_DIMENSIONS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_DIMENSIONS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_DIMENSIONS)
    @Config.RequiresMcRestart
    public boolean RACE_SCALED_PLAYER_DIMENSIONS = true;

    @Config.Name(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS)
    @Config.RequiresMcRestart
    public boolean RACE_SCALED_PLAYER_SHADOWS = true;

}
