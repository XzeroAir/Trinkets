package xzeroair.trinkets.util.config.race;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.ConfigDefaultReusedConstants;

public class ConfigRaceEquipmentWeight {

    public ConfigRaceEquipmentWeight() {
        this(false, ConfigDefaultReusedConstants.EMPTY_STRING_ARRAY);
    }

    public ConfigRaceEquipmentWeight(boolean enabled, String[] equipment) {
        this.ENABLED = enabled;
        this.EQUIPMENT = equipment;
    }

    @Config.Name("00. " + ConstantsConfigLang.CONFIG_RACES_EQUIPMENT_WEIGHT_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_EQUIPMENT_WEIGHT_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_EQUIPMENT_WEIGHT_ENABLED)
    public boolean ENABLED;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_RACES_EQUIPMENT_WEIGHT_LIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_EQUIPMENT_WEIGHT_LIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_EQUIPMENT_WEIGHT_LIST)
    public String[] EQUIPMENT;
}
