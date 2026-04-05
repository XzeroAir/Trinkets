package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityImmunityPoison {

    public ConfigAbilityImmunityPoison() {
        this(3600);
    }

    public ConfigAbilityImmunityPoison(int duration) {
        this(duration, true);
    }

    public ConfigAbilityImmunityPoison(boolean enabled) {
        this(3600, enabled);
    }

    public ConfigAbilityImmunityPoison(int duration, boolean enabled) {
        this.DURATION = duration;
        this.ENABLED = enabled;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED;


    @Config.Ignore
    @Config.RequiresMcRestart
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_DURATION_COMMENT)
    @Config.Name("01. " + ConstantsConfigLang.CONFIG_POTIONS_DURATION_NAME)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_DURATION)
    public int DURATION;

}
