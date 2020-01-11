package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

public class SeaStone {

	@Config.Comment("Sea Stone's Ability to float in water. Set to False to Disable. Default True")
	@Name("01. Sea Stone's Swimming Tweaks")
	public boolean C06_Sea_Stone_Swim_Tweaks = true;

	@Name("02. Infinite Water Breathing")
	public boolean underwater_breathing = true;

	@Config.Comment("Should the player always have full bubbles, or stop at 1")
	@Name("03. Full Bubbles")
	public boolean always_full = true;

	@Name("04. Swim Speed")
	public double speed = 4D;

	@Config.Comment("If Tough as Nails is installed should the Stone of the Sea Prevent thirst Poisoning")
	@Name("05. Prevent TAN Thirst Poisoning")
	public boolean prevent_thirst = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "amulet";

}
