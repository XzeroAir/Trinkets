package xzeroair.trinkets.util.config.debug;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigDebug {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_DEBUG;

    public ConfigDebug() {

    }

    @Config.Name("Show Armor Materials")
    @Config.Comment("")
    @Config.LangKey(LANG_PREFIX + ".materials")
    public boolean debugArmorMaterials = false;

    @Config.Name("Show Item Slot IDs")
    @Config.Comment("")
    @Config.LangKey(LANG_PREFIX + ".slots")
    public boolean showID = false;

    @Config.Name("Show OreDict Entries")
    @Config.Comment("")
    @Config.LangKey(LANG_PREFIX + ".oredict")
    public boolean showOreDictEntries = false;

    @Config.Name("Show Movement Speed")
    @Config.Comment("")
    @Config.LangKey(LANG_PREFIX + ".speed")
    public boolean showMovementSpeed = false;

    @Config.Name("Show Damage Types")
    @Config.Comment("")
    @Config.LangKey(LANG_PREFIX + ".damagetype")
    public boolean showDamageTypes = false;

}
