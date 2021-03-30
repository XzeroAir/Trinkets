package xzeroair.trinkets.util.config.potions;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class PotionConfig {

	private final String potion = ".potion";
	private final String PREFIX = Reference.MODID + ".config" + potion;

	public PotionConfig(String itemID, int duration) {
		catalyst = itemID;
		Duration = duration;
	}

	@Config.RequiresMcRestart
	@Name("Potion Catalyst")
	@LangKey(PREFIX + ".catalyst")
	public String catalyst = "xat:glowing_ingot";

	@Config.RequiresMcRestart
	@Name("Potion Effect Duration")
	@LangKey(PREFIX + ".duration")
	public int Duration = 1200;

}
