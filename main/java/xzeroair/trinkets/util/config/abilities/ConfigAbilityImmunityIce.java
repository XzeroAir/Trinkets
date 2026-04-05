package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityImmunityIce {

    public ConfigAbilityImmunityIce() {
        this(3600);
    }

    public ConfigAbilityImmunityIce(int duration) {
        this(duration, true);
    }

    public ConfigAbilityImmunityIce(boolean enabled) {
        this(3600, enabled);
    }

    public ConfigAbilityImmunityIce(int duration, boolean enabled) {
        this.DURATION = duration;
        this.ENABLED = enabled;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_POTIONS_DURATION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_DURATION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_DURATION)
    public int DURATION;

}
