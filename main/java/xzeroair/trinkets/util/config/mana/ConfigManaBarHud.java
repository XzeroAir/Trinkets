package xzeroair.trinkets.util.config.mana;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigManaBarHud {

    private final String LANG_PREFIX_MAGIC_HUD = ConstantsConfigLang.CONFIG_MAGIC_HUD;

    public ConfigManaBarHud() {

    }

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_ENABLED)
    public boolean shown = true;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_ENABLED_ALWAYS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_ENABLED_ALWAYS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_ENABLED_ALWAYS)
    public boolean always_shown = false;

    @Config.Name(ConstantsConfigLang.CONFIG_HORIZONTAL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_HORIZONTAL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_HORIZONTAL)
    public boolean mana_horizontal = true;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_TEXT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_TEXT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_TEXT)
    public boolean SHOW_TEXT = true;

    @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_X_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_X_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_X)
    public double translatedX = 0.19;

    @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_Y_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_Y_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_Y)
    public double translatedY = 0.94;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_MODE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_MODE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_MODE)
    public boolean usePixelPosition = false;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_X_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_X_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_X)
    public int xPixels = 0;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_Y_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_Y_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_Y)
    public int yPixels = 0;

    @Config.Name(ConstantsConfigLang.CONFIG_TEXTURE_WIDTH_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_TEXTURE_WIDTH_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_TEXTURE_WIDTH)
    public int width = 106;

    @Config.Name(ConstantsConfigLang.CONFIG_TEXTURE_HEIGHT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_TEXTURE_HEIGHT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_TEXTURE_HEIGHT)
    public int height = 16;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_PRE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_PRE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_PRE)
    public boolean rendLocPre = true;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_TEXTURE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_TEXTURE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_TEXTURE)
    @Config.RangeInt(min = 0, max = 6)
    public int rendTexID = 0;

}
