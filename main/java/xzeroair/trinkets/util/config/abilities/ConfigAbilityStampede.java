package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityStampede {

    public ConfigAbilityStampede() {
        this(10F, 20F, 40, 3.0D, 6.0D);
    }

    public ConfigAbilityStampede(float damage, float cost, int charge, double minVelocity, double maxVelocity) {
        this.ATTACK_DAMAGE = damage;
        this.ATTACK_COST = cost;
        this.CHARGE_TIME = charge;
        this.VELOCITY_MIN = minVelocity;
        this.VELOCITY_MAX = maxVelocity;
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

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_VELOCITY_MIN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_VELOCITY_MIN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_VELOCITY_MIN)
    @Config.RangeDouble(min = 1.0D)
    public double VELOCITY_MIN;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_VELOCITY_MAX_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_VELOCITY_MAX_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_VELOCITY_MAX)
    @Config.RangeDouble(min = 2.0D)
    public double VELOCITY_MAX;


}
