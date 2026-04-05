package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityLightningBolt {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_LIGHTNING_BOLT;

    public ConfigAbilityLightningBolt() {
        this(40F, 300F, 120);
    }

    public ConfigAbilityLightningBolt(float damage, float maxCost, int charge) {
        this.ATTACK_DAMAGE = damage;
        this.ATTACK_COST = maxCost;
        this.CHARGE_TIME = charge;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_DAMAGE_MAX_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DAMAGE_MAX_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DAMAGE_MAX)
    public float ATTACK_DAMAGE;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_MAGIC_COST_MAX_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COST_MAX_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_COST_MAX)
    public float ATTACK_COST;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_CHARGE_TIME_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_CHARGE_TIME_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_CHARGE_TIME)
    @Config.RangeInt(min = 20)
    public int CHARGE_TIME;

}
