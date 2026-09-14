package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityImmunityVoid {

    public ConfigAbilityImmunityVoid() {
        this(true);
    }

    public ConfigAbilityImmunityVoid(boolean enabled) {
        this.ENABLED = enabled;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED;
}
