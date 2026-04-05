package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityWellRested {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_WELL_RESTED;

    public ConfigAbilityWellRested() {

    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_WELL_RESTED_BUFFS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_WELL_RESTED_BUFFS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_WELL_RESTED_BUFFS)
    public String[] SLEEP_BONUSES = new String[]{
            //@formatter:off
            "minecraft:regeneration:300:0",
            "minecraft:luck:600:0",
            "minecraft:health_boost:3600:1"
            //@formatter:on
    };

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_WELL_RESTED_BUFFS_RANDOM_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_WELL_RESTED_BUFFS_RANDOM_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_WELL_RESTED_BUFFS_RANDOM)
    public int SLEEP_BONUSES_RANDOM = 0;
}
