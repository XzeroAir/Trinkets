package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityHealCloud {

    public ConfigAbilityHealCloud() {
        this.ENABLED = false;
        this.EFFECTS = new String[]{"minecraft:instant_health:60:0"};
        this.RADIUS = 3.0F;
        this.VERTICAL_RADIUS = 2.0F;
        this.DURATION = 120;
        this.WAIT_TIME = 10;
        this.PULSE_INTERVAL = 20;
        this.REAPPLICATION_DELAY = 20;
        this.ITEM_REPAIR_AMOUNT = 1;
        this.GROWTH_ATTEMPTS_PER_PULSE = 4;
        this.CAST_RANGE = 15.0D;
        this.COST_PER_SECOND = 20F;
        this.COST_PER_POTION_EFFECT = 5F;
        this.COST_PER_EFFECT_LEVEL = 5F;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_RADIUS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_RADIUS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_RADIUS)
    @Config.RangeDouble(min = 0)
    public float RADIUS;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_VERTICAL_RADIUS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_VERTICAL_RADIUS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_VERTICAL_RADIUS)
    @Config.RangeDouble(min = 0)
    public float VERTICAL_RADIUS;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_POTIONS_DURATION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_DURATION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_DURATION)
    @Config.RangeInt(min = 1)
    public int DURATION;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_WAIT_TIME_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_WAIT_TIME_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_WAIT_TIME)
    @Config.RangeInt(min = 0)
    public int WAIT_TIME;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_PULSE_INTERVAL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_PULSE_INTERVAL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_PULSE_INTERVAL)
    @Config.RangeInt(min = 1)
    public int PULSE_INTERVAL;

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_REAPPLICATION_DELAY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_REAPPLICATION_DELAY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_REAPPLICATION_DELAY)
    @Config.RangeInt(min = 0)
    public int REAPPLICATION_DELAY;

    @Config.Name("07. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_REPAIR_AMOUNT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_REPAIR_AMOUNT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_REPAIR_AMOUNT)
    @Config.RangeInt(min = 0)
    public int ITEM_REPAIR_AMOUNT;

    @Config.Name("08. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_GROWTH_ATTEMPTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_GROWTH_ATTEMPTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_GROWTH_ATTEMPTS)
    @Config.RangeInt(min = 0)
    public int GROWTH_ATTEMPTS_PER_PULSE;

    @Config.Name("09. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_CAST_RANGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_CAST_RANGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_CAST_RANGE)
    @Config.RangeDouble(min = 1)
    public double CAST_RANGE;

    @Config.Name("10. " + ConstantsConfigLang.CONFIG_EFFECTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_EFFECTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_EFFECTS)
    public String[] EFFECTS;

    @Config.Name("11. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_SECOND_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_SECOND_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_SECOND)
    @Config.RangeDouble(min = 0)
    public float COST_PER_SECOND;

    @Config.Name("12. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_POTION_EFFECT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_POTION_EFFECT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_POTION_EFFECT)
    @Config.RangeDouble(min = 0)
    public float COST_PER_POTION_EFFECT;

    @Config.Name("13. " + ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_EFFECT_LEVEL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_EFFECT_LEVEL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD_COST_PER_EFFECT_LEVEL)
    @Config.RangeDouble(min = 0)
    public float COST_PER_EFFECT_LEVEL;
}
