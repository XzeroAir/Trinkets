package xzeroair.trinkets.util.config.race;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class RaceSizeConfig {

    public RaceSizeConfig() {
        this(100, 100);
    }

    public RaceSizeConfig(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Config.Name("00. " + ConstantsConfigLang.CONFIG_HEIGHT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_HEIGHT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_HEIGHT)
    @Config.RangeInt(min = 20)
    public int height;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_WIDTH_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_WIDTH_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_WIDTH)
    @Config.RangeInt(min = 20)
    public int width;

}
