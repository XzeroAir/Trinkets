package xzeroair.trinkets.util.config.gui;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigGuiButtonShared {

    public ConfigGuiButtonShared(int x, int y, int width, int height, int texWidth, int texHeight, int texSizeWidth, int texSizeHeight, int color) {
        this.X = x;
        this.Y = y;
        this.BUTTON_WIDTH = width;
        this.BUTTON_HEIGHT = height;
        this.TEXTURE_WIDTH = texWidth;
        this.TEXTURE_HEIGHT = texHeight;
        this.TEXTURE_ATLAS_WIDTH = texSizeWidth;
        this.TEXTURE_ATLAS_HEIGHT = texSizeHeight;
        this.COLOR = color;
    }

    @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_X_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_X_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_X)
    public int X = 0;
    @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_Y_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_Y_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_Y)
    public int Y = 0;

    @Config.Name(ConstantsConfigLang.CONFIG_WIDTH_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_WIDTH_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_WIDTH)
    public int BUTTON_WIDTH = 16;
    @Config.Name(ConstantsConfigLang.CONFIG_HEIGHT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_HEIGHT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_HEIGHT)
    public int BUTTON_HEIGHT = 16;

    @Config.Name(ConstantsConfigLang.CONFIG_TEXTURE_WIDTH_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_TEXTURE_WIDTH_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_TEXTURE_WIDTH)
    public int TEXTURE_WIDTH = 16;
    @Config.Name(ConstantsConfigLang.CONFIG_TEXTURE_HEIGHT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_TEXTURE_HEIGHT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_TEXTURE_HEIGHT)
    public int TEXTURE_HEIGHT = 16;
    @Config.Name(ConstantsConfigLang.CONFIG_TEXTURE_ATLAS_WIDTH_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_TEXTURE_ATLAS_WIDTH_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_TEXTURE_ATLAS_WIDTH)
    public int TEXTURE_ATLAS_WIDTH = 16;
    @Config.Name(ConstantsConfigLang.CONFIG_TEXTURE_ATLAS_HEIGHT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_TEXTURE_ATLAS_HEIGHT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_TEXTURE_ATLAS_HEIGHT)
    public int TEXTURE_ATLAS_HEIGHT = 16;

    @Config.Name(ConstantsConfigLang.CONFIG_COLOR_DECIMAL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COLOR_DECIMAL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COLOR_DECIMAL)
    public int COLOR = 16777215;
}
