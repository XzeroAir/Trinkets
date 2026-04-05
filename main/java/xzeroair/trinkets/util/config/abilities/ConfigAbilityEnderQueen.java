package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityEnderQueen {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN;

    public ConfigAbilityEnderQueen() {
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_CHEST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_CHEST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_CHEST)
    public boolean ENDER_CHEST = true;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_WATER_HURTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_WATER_HURTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_WATER_HURTS)
    public boolean WATER_HURTS = false;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_DAMAGED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_DAMAGED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_DAMAGED)
    public int IGNORE_CHANCE = 10;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_SPAWN_CHANCE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_SPAWN_CHANCE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_SPAWN_CHANCE)
    public int SPAWN_CHANCE = 10;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_TELEPORT_ON_HURT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_TELEPORT_ON_HURT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_TELEPORT_ON_HURT)
    public int TELEPORT_CHANCE = 1;

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_FOLLOW_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_FOLLOW_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_FOLLOW)
    public boolean ENDERMAN_FOLLOW = true;

    @Config.Name("07. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_RETALIATION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_RETALIATION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_RETALIATION)
    public boolean ENDERMAN_RETALIATE = false;

    @Config.Name("08. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_DROP_EXP_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_DROP_EXP_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_DROP_EXP)
    public boolean ENDERMAN_DROP_EXP = false;

    @Config.Name("09. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_DROP_ITEMS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_DROP_ITEMS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_DROP_ITEMS)
    public boolean ENDERMAN_DROP_ITEMS = false;

    @Config.Name("10. " + ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_BLOCK_TELEPORT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_BLOCK_TELEPORT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN_BLOCK_TELEPORT)
    public boolean BLOCK_TELEPORTATION = false;

}
