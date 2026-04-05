package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.compat.ConfigFireResistanceTiersCompat;

public class ConfigAbilityImmunityFire {

    public ConfigAbilityImmunityFire() {
        this(3600);
    }

    public ConfigAbilityImmunityFire(int duration) {
        this(duration, true);
    }

    public ConfigAbilityImmunityFire(boolean enabled) {
        this(3600, enabled);
    }

    public ConfigAbilityImmunityFire(int duration, boolean enabled) {
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

    @Config.Name(ConstantsConfigLang.CONFIG_COMPAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COMPAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COMPAT)
    public Compatibility COMPAT = new Compatibility();

    public class Compatibility {

        @Config.Name(ConstantsConfigLang.CONFIG_FIRE_TIERS_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_FIRE_TIERS_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_FIRE_TIERS)
        public ConfigFireResistanceTiersCompat TIERS = new ConfigFireResistanceTiersCompat();
    }
}
