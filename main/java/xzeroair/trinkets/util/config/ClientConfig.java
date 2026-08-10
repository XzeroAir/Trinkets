package xzeroair.trinkets.util.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.debug.ConfigDebug;
import xzeroair.trinkets.util.config.gui.ConfigClientTrinketsContainer;
import xzeroair.trinkets.util.config.gui.ConfigRacePropertiesGui;
import xzeroair.trinkets.util.config.mana.ConfigManaBarHud;

public class ClientConfig {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_CLIENT;

    @Config.Name(ConstantsConfigLang.CONFIG_DEBUG_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DEBUG_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DEBUG)
    public ConfigDebug debug = new ConfigDebug();

    @Config.Name(ConstantsConfigLang.CONFIG_CONTAINER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_CONTAINER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_CONTAINER)
    public ConfigClientTrinketsContainer GUI = new ConfigClientTrinketsContainer();

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD)
    public ConfigManaBarHud MANA_BAR_HUD = new ConfigManaBarHud();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_GUI_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_GUI_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_GUI)
    public ConfigRacePropertiesGui raceProperties = new ConfigRacePropertiesGui();

    @Config.Name(ConstantsConfigLang.CONFIG_CAMERA_ADJUSTMENTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_CAMERA_ADJUSTMENTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_CAMERA_ADJUSTMENTS)
    public boolean CAMERA_HEIGHT = true;


    @Config.Name(ConstantsConfigLang.CONFIG_RENDERING_MAIN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RENDERING_MAIN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RENDERING_MAIN)
    public boolean RENDERING = true;

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS)
    public ClientConfigItems ITEMS = new ClientConfigItems();

    public class ClientConfigItems {

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_ELEMENTS_TOOLTIP_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_ELEMENTS_TOOLTIP_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_ELEMENTS_TOOLTIP)
        public boolean RENDER_ELEMENTS = true;

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_SHIELD_OF_HONOR_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_SHIELD_OF_HONOR_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_SHIELD_OF_HONOR)
        public ClientConfigDamageShield DAMAGE_SHIELD = new ClientConfigDamageShield();

        public class ClientConfigDamageShield {

            @Config.Name(ConstantsConfigLang.CONFIG_RENDERING_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_RENDERING_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_RENDERING)
            public boolean RENDER = true;

        }

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_ENDER_CROWN_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_ENDER_CROWN_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_ENDER_CROWN)
        public ClientConfigEnderCrown ENDER_CROWN = new ClientConfigEnderCrown();

        public class ClientConfigEnderCrown {

            @Config.Name(ConstantsConfigLang.CONFIG_RENDERING_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_RENDERING_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_RENDERING)
            public boolean RENDER = true;

            @Config.Name(ConstantsConfigLang.CONFIG_RENDERING_HELMET_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_RENDERING_HELMET_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_RENDERING_HELMET)
            public boolean RENDER_HELMET = true;

        }

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_SEA_STONE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_SEA_STONE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_SEA_STONE)
        public ClientConfigSeaStone SEA_STONE = new ClientConfigSeaStone();

        public class ClientConfigSeaStone {

            @Config.Name(ConstantsConfigLang.CONFIG_RENDERING_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_RENDERING_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_RENDERING)
            public boolean RENDER = true;

        }

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_FAELIS_CLAWS_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_FAELIS_CLAWS_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_FAELIS_CLAWS)
        public ClientConfigFaelisClaw FAELIS_CLAW = new ClientConfigFaelisClaw();

        public class ClientConfigFaelisClaw {

            @Config.Name(ConstantsConfigLang.CONFIG_RENDERING_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_RENDERING_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_RENDERING)
            public boolean RENDER = true;

        }
    }
}
