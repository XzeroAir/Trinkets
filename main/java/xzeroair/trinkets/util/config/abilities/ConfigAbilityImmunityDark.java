package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityImmunityDark {

    public ConfigAbilityImmunityDark() {
        this(3600);
    }

    public ConfigAbilityImmunityDark(int duration) {
        this(duration, true);
    }

    public ConfigAbilityImmunityDark(boolean enabled) {
        this(3600, enabled);
    }

    public ConfigAbilityImmunityDark(int duration, boolean enabled) {
        this.DURATION = duration;
        this.ENABLED = enabled;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED;

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_HEAL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_HEAL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_HEAL)
    public boolean HEAL_FROM_DARK = true;

    @Config.Name(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_HEAL_MULTI_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_HEAL_MULTI_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK_HEAL_MULTI)
    @Config.RangeDouble(min = 0)
    public float HEAL_FROM_DARK_MULTI = 1f;

    @Config.Ignore
    @Config.RequiresMcRestart
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_DURATION_COMMENT)
    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_DURATION_NAME)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_DURATION)
    public int DURATION;

}
