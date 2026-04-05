package xzeroair.trinkets.util.config.abilities.external.survival;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilitySurvivalThirstAbsorption {


    public ConfigAbilitySurvivalThirstAbsorption() {
        this(1);
    }

    public ConfigAbilitySurvivalThirstAbsorption(int amount) {
        this.AMOUNT = amount;
        this.FREQUENCY = 20;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_AMOUNT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_AMOUNT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION_AMOUNT)
    public int AMOUNT;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_FREQUENCY)
    @Config.Comment(ConstantsConfigLang.CONFIG_FREQUENCY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_FREQUENCY)
    @Config.RangeInt(min = 1)
    public int FREQUENCY;

}
