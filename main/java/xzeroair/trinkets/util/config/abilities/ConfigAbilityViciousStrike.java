package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityViciousStrike {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_VICIOUS_STRIKE;

    public ConfigAbilityViciousStrike() {
        this(true, 300, 4);
    }

    public ConfigAbilityViciousStrike(boolean enabled, int durationTicks, int chance) {
        this.ENABLED = enabled;
        this.DURATION = durationTicks;
        this.CHANCE = chance;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_VICIOUS_STRIKE_CHANCE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_VICIOUS_STRIKE_CHANCE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_VICIOUS_STRIKE_CHANCE)
    @Config.RangeInt(min = 0)
    public int CHANCE;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_POTIONS_DURATION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_DURATION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_DURATION)
    public int DURATION;

    @Config.Name(ConstantsConfigLang.CONFIG_COMPAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COMPAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COMPAT)
    public Compatibility COMPAT = new Compatibility();

    public class Compatibility {

        @Config.Name(ConstantsConfigLang.CONFIG_DEFILED_LANDS_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_DEFILED_LANDS_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_DEFILED_LANDS)
        public DefiledLandsCompat DEFILED_LANDS = new DefiledLandsCompat();

        public class DefiledLandsCompat {

            @Config.Name(ConstantsConfigLang.CONFIG_DEFILED_LANDS_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_DEFILED_LANDS_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_DEFILED_LANDS)
            public boolean BLEED = true;
        }
    }
}
