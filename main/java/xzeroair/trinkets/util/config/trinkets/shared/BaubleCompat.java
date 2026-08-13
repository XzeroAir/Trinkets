package xzeroair.trinkets.util.config.trinkets.shared;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class BaubleCompat {


    public BaubleCompat(String string) {
        this(string, false);
    }

    public BaubleCompat(String string, boolean equip_multiple) {
        this.bauble_type = string;
        this.equip_multiple = equip_multiple;
    }

    @Config.Comment({
            //@formatter:off
			"If the mod Baubles is installed what bauble slot should it use",
			"Available Types:",
			"Trinket, Any, All",
			"Amulet, Necklace, Pendant",
			"Ring, Rings",
			"Belt",
			"Head, Hat",
			"Body, Chest",
			"Charm"
			//@formatter:on
    })
    @Name("Bauble Type")
    @Config.RequiresWorldRestart
    @LangKey(ConstantsConfigLang.CONFIG_BAUBLES + ".type")
    public String bauble_type;

    @Name("Equip Multiple")
    @LangKey(ConstantsConfigLang.CONFIG_BAUBLES + ".multiple")
    public boolean equip_multiple;
}
