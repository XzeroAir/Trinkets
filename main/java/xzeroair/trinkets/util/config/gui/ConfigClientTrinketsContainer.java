package xzeroair.trinkets.util.config.gui;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigClientTrinketsContainer {

    public ConfigClientTrinketsContainer() {

    }

    @Config.Name(ConstantsConfigLang.CONFIG_TEXTURE_ATLAS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_TEXTURE_ATLAS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_TEXTURE_ATLAS)
    public int ATLAS_SIZE = 256;

    @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_X_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_X_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_X)
    public int X = -14;

    @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_Y_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_Y_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_Y)
    public int Y = 7;

    @Config.Name(ConstantsConfigLang.CONFIG_CONTAINER_POTIONS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_CONTAINER_POTIONS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_CONTAINER_POTIONS)
    public boolean ICONS_POTIONS = true;

    @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_Z_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_Z_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_Z)
    @Config.SlidingOption
    @Config.RangeInt(min = 0, max = 2)
    public int Z = 1;

//    @Config.Name(ConstantsConfigLang.CONFIG_COLOR_HEX_NAME)
//    @Config.Comment(ConstantsConfigLang.CONFIG_COLOR_HEX_COMMENT)
//    @Config.LangKey(ConstantsConfigLang.CONFIG_COLOR_HEX)
//    public String GUI_COLOR = "#FFFFFF";

    @Config.Name(ConstantsConfigLang.CONFIG_BUTTON_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BUTTON_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BUTTON)
    public TrinketButton BUTTON = new TrinketButton();

    public class TrinketButton {

        @Config.Name(ConstantsConfigLang.CONFIG_BUTTON_ID_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_BUTTON_ID_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_BUTTON_ID)
        public int ID = 67;

        @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_X_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_X_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_X)
        public int X = -24;

        @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_Y_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_Y_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_Y)
        public int Y = -24;

        @Config.Name(ConstantsConfigLang.CONFIG_OFFSET_X_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_OFFSET_X_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_OFFSET_X)
        public int OFFSET_X = 0;

        @Config.Name(ConstantsConfigLang.CONFIG_OFFSET_Y_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_OFFSET_Y_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_OFFSET_Y)
        public int OFFSET_Y = 0;

        @Config.Name(ConstantsConfigLang.CONFIG_HEIGHT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_HEIGHT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_HEIGHT)
        public int BUTTON_HEIGHT = 10;

        @Config.Name(ConstantsConfigLang.CONFIG_WIDTH_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_WIDTH_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_WIDTH)
        public int BUTTON_WIDTH = 10;

        private int x = 208;
        private int y = 0;
        private int width = 10;
        private int height = 10;
        private int texWidth = 16;
        private int texHeight = 16;
        private int texSizeWidth = 256;
        private int texSizeHeight = 256;
        private int color = 16777215;

        public ConfigGuiButtonShared open = new ConfigGuiButtonShared(x + texWidth, y, width, height, texWidth, texHeight, texSizeWidth, texSizeHeight, color);
        public ConfigGuiButtonShared close = new ConfigGuiButtonShared(x, y, width, height, texWidth, texHeight, texSizeWidth, texSizeHeight, color);

    }

}
