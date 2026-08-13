package xzeroair.trinkets.util.config.potions;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class PotionConfig {

    public PotionConfig(String itemID, int duration) {
        catalyst = itemID;
        Duration = duration;
    }

    @Config.RequiresMcRestart
    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_CATALYST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_CATALYST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_CATALYST)
    public String catalyst;

    @Config.RequiresMcRestart
    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_DURATION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_DURATION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_DURATION)
    public int Duration;

}
