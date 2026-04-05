package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilitySafeGuard {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD;

    public ConfigAbilitySafeGuard() {
    }

    @Config.Name(ConstantsConfigLang.CONFIG_CLIENT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_CLIENT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_CLIENT)
    public ClientConfig CLIENT = new ClientConfig();

    public class ClientConfig {

        @Config.Name(ConstantsConfigLang.CONFIG_VOLUME_CONTROL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_VOLUME_CONTROL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_VOLUME_CONTROL)
        @Config.RangeDouble(min = 0, max = 1)
        public float VOLUME = 0.2F;

        @Config.Name(ConstantsConfigLang.CONFIG_VOLUME_CONTROL_PITCH_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_VOLUME_CONTROL_PITCH_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_VOLUME_CONTROL_PITCH)
        @Config.RangeDouble(min = 0, max = 1)
        public float PITCH = 1F;

    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_HIT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_HIT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_HIT)
    public int MAX_HITS = 3;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_HIT_MIN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_HIT_MIN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_HIT_MIN)
    @Config.RangeDouble(min = 0)
    public float MIN_DAMAGE_TO_COUNT = 1F;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_HIT_TRIGGER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_HIT_TRIGGER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_HIT_TRIGGER)
    @Config.RangeDouble(min = 0)
    public float MIN_DAMAGE_TO_TRIGGER = 1F;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_EXPLOSION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_EXPLOSION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_EXPLOSION)
    @Config.RangeDouble(min = 0, max = 1f)
    public float EXPLOSION_REDUCED_AMOUNT = 0.25f;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST)
    public String DEFAULT_EFFECT = "minecraft:resistance";

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_LEVEL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_LEVEL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_LEVEL)
    @Config.RangeInt(min = 0)
    public int DEFAULT_EFFECT_LEVEL = 0;

    @Config.Name("07. " + ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS)
    public boolean EFFECT_STACKS = true;

    @Config.Name("08. " + ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_MAX_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_MAX_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD_RESIST_STACKS_MAX)
    @Config.RangeInt(min = 0)
    public int EFFECT_STACKS_LIMIT = 3;

}
