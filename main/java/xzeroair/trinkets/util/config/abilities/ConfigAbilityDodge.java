package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityDodge {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_DODGE;

    public ConfigAbilityDodge() {
        this(30F);
    }

    public ConfigAbilityDodge(float cost) {
        this.COST = cost;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_MAGIC_COST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_COST)
    public float COST;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_DODGE_STUN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_DODGE_STUN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_DODGE_STUN)
    public boolean STUNS = true;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_ABILITIES_DODGE_STUN_RANGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_DODGE_STUN_RANGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_DODGE_STUN_RANGE)
    @Config.RangeDouble(min = 1)
    public double STUN_RADIUS = 2;

}
