package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityKinetic {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_REDUCE_KINETIC;

    public ConfigAbilityKinetic() {
        this(0F);
    }

    public ConfigAbilityKinetic(float multiplier) {
        this(multiplier, 0F);
    }

    public ConfigAbilityKinetic(float AMOUNT, float COST) {
        this.MULTIPLIER = AMOUNT;
        this.COST = COST;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_MULTIPLIER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MULTIPLIER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MULTIPLIER)
    @Config.RangeDouble(min = 0, max = 10)
    public float MULTIPLIER;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_MAGIC_COST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_COST)
    public float COST;

}
