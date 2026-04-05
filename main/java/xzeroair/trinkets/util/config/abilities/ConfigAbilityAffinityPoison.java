package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityAffinityPoison {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_POISON;

    public ConfigAbilityAffinityPoison() {

    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_POISON_MULTI_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_POISON_MULTI_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_POISON_MULTI)
    @Config.RangeDouble(min = 1)
    public float DAMAGE_MULTI = 2f;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_POISON_CHANCE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_POISON_CHANCE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_POISON_CHANCE)
    public int CHANCE = 5;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_POTIONS_DURATION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_DURATION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_DURATION)
    public int DURATION = 40;


}
