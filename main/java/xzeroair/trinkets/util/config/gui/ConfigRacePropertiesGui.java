package xzeroair.trinkets.util.config.gui;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigRacePropertiesGui {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_RACES_GUI;

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_GUI_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_GUI_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_GUI)
    public boolean ENABLED = true;

    @Config.Name(ConstantsConfigLang.CONFIG_BUTTON_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BUTTON_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BUTTON)
    public TrinketButton button = new TrinketButton();

    public class TrinketButton {

        @Config.Name(ConstantsConfigLang.CONFIG_BUTTON_ID_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_BUTTON_ID_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_BUTTON_ID)
        public int ID = 69;

        @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_X_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_X_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_X)
        public int X = 28; // 0.01


        @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_Y_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_Y_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_Y)
        public int Y = 66; // 0.39

        @Config.Name(ConstantsConfigLang.CONFIG_HEIGHT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_HEIGHT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_HEIGHT)
        public int BUTTON_WIDTH = 10;

        @Config.Name(ConstantsConfigLang.CONFIG_WIDTH_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_WIDTH_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_WIDTH)
        public int BUTTON_HEIGHT = 10;

        private int x = 0;
        private int y = 0;
        private int width = 10;
        private int height = 10;
        private int texWidth = 32;
        private int texHeight = 32;
        private int texSizeWidth = 32;
        private int texSizeHeight = 64;
        private int color = 16777215;
        public ConfigGuiButtonShared texture = new ConfigGuiButtonShared(x, y, width, height, texWidth, texHeight, texSizeWidth, texSizeHeight, color);
    }
}
