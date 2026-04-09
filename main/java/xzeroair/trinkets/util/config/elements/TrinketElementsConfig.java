package xzeroair.trinkets.util.config.elements;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class TrinketElementsConfig {

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_AIR_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_AIR_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_AIR_DAMAGE)
    public String[] Air_Damage = {};

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_DARK_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_DARK_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_DARK_DAMAGE)
    public String[] Dark_Damage = {"wither"};

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_EARTH_DAMAGE)
    public String[] Earth_Damage = {};

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_FIRE_DAMAGE)
    public String[] Fire_Damage = {"inFire", "onFire", "lava", "hotFloor", "dragon_fire"};

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_ICE_DAMAGE)
    public String[] Ice_Damage = {"dragon_ice", "ooze", "cold_fire"};

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHT_DAMAGE)
    public String[] Light_Damage = {};

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_LIGHTNING_DAMAGE)
    public String[] Lightning_Damage = {"lightningBolt", "locks.shock", "dragon_lightning"};

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_POISON_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_POISON_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_POISON_DAMAGE)
    public String[] Poison_Damage = {"poison", "xat.poison"};

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_VOID_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_VOID_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_VOID_DAMAGE)
    public String[] Void_Damage = {};

    @Config.Name(ConstantsConfigLang.CONFIG_ELEMENTS_WATER_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELEMENTS_WATER_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELEMENTS_WATER_DAMAGE)
    public String[] Water_Damage = {};

}
