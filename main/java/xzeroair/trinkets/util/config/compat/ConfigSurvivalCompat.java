package xzeroair.trinkets.util.config.compat;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigSurvivalCompat {

    public static final String LANG_PREFIX = ConstantsConfigLang.CONFIG_SURVIVAL;

    public ConfigSurvivalCompat() {
        this(false, false, false, false);
    }

    public ConfigSurvivalCompat(boolean heat, boolean cold, boolean thirst, boolean parasites) {
        this.immuneToHeat = heat;
        this.immuneToCold = cold;
        this.immuneToThirst = thirst;
        this.immuneToParasites = parasites;
    }

    @Config.Name("00. " + ConstantsConfigLang.CONFIG_SURVIVAL_HEAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_HEAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL_HEAT)
    public boolean immuneToHeat;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_SURVIVAL_COLD_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_COLD_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL_COLD)
    public boolean immuneToCold;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_SURVIVAL_THIRST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_THIRST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL_THIRST)
    public boolean immuneToThirst;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_SURVIVAL_PARASITES_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_SURVIVAL_PARASITES_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_SURVIVAL_PARASITES)
    public boolean immuneToParasites;

    public ConfigSurvivalCompat setImmuneToHeat() {
        return this.setImmuneToHeat(true);
    }

    public ConfigSurvivalCompat setImmuneToHeat(boolean immuneToHeat) {
        this.immuneToHeat = immuneToHeat;
        return this;
    }

    public ConfigSurvivalCompat setImmuneToCold() {
        return this.setImmuneToCold(true);
    }

    public ConfigSurvivalCompat setImmuneToCold(boolean immuneToCold) {
        this.immuneToCold = immuneToCold;
        return this;
    }

    public ConfigSurvivalCompat setImmuneToParasites() {
        return setImmuneToParasites(true);
    }

    public ConfigSurvivalCompat setImmuneToParasites(boolean immuneToParasites) {
        this.immuneToParasites = immuneToParasites;
        return this;
    }

    public ConfigSurvivalCompat setImmuneToThirst() {
        return setImmuneToThirst(true);
    }

    public ConfigSurvivalCompat setImmuneToThirst(boolean immuneToThirst) {
        this.immuneToThirst = immuneToThirst;
        return this;
    }
}
