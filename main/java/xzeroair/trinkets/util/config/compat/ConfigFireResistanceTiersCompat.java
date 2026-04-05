package xzeroair.trinkets.util.config.compat;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigFireResistanceTiersCompat {

    public ConfigFireResistanceTiersCompat() {
        this(0);
    }

    public ConfigFireResistanceTiersCompat(int amplifier) {
        this.amplifier = amplifier;
    }

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_AMPLIFIER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_AMPLIFIER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_AMPLIFIER)
    public int amplifier;

}
