package xzeroair.trinkets.util.config.trinkets.shared;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class BaubleCompat {

	private static final String PREFIX = Reference.MODID + ".config.baubles";

	public BaubleCompat(String string) {
		bauble_type = string;
	}

	@Config.Comment({
		"If the mod Baubles is installed what bauble slot should it use",
		"Available Types:",
		"Trinket, Any, All",
		"Amulet, Necklace, Pendant",
		"Ring, Rings",
		"Belt",
		"Head, Hat",
		"Body, Chest",
		"Charm"
	})
	@Name("Bauble Type")
	@Config.RequiresWorldRestart
	@LangKey(PREFIX + ".type")
	public String bauble_type = "trinket";
}
