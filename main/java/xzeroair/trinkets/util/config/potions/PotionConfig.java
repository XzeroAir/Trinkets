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
	@Config.Comment("Item Catalyst to brew this potion")
	@Name("Potion Catalyst")
	@LangKey(PREFIX + ".catalyst")
	public String catalyst = "xat:glowing_ingot";

	@Config.RequiresMcRestart
	@Config.Comment("How long this potion should last in ticks\\n20 ticks per second")
	@Name("Potion Effect Duration")
	@LangKey(PREFIX + ".duration")
	public int Duration = 1200;

}
