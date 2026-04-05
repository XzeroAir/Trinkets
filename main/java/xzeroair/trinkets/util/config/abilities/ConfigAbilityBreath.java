package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityBreath {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_BREATH;

    public ConfigAbilityBreath() {
        this(1F, 10F);
    }

    public ConfigAbilityBreath(String... effects) {
        this(1F, 10F, effects);
    }

    public ConfigAbilityBreath(float breath_damage, float breath_cost, String... effects) {
        this.DAMAGE = breath_damage;
        this.COST = breath_cost;
        if (effects == null) {
            effects = new String[0];
        }
        this.EFFECTS = effects;
        this.FREQUENCY = 3;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_BREATH_TERRAIN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_TERRAIN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_TERRAIN)
    public boolean TERRAIN = true;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DAMAGE)
    public float DAMAGE;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_MAGIC_COST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_COST)
    public float COST;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_FREQUENCY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_FREQUENCY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_FREQUENCY)
    @Config.RangeInt(min = 3)
    public int FREQUENCY;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_ABILITIES_BREATH_EFFECTS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_EFFECTS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_EFFECTS)
    public String[] EFFECTS;

}
