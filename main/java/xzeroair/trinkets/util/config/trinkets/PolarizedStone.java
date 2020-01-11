package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;

public class PolarizedStone {

	@Config.Comment("Should the Polarized Stone Instantly pickup Items, or Pull them toward you. Default True")
	@Name("01. Instant Pickup")
	public boolean instant_pickup = true;

	@Config.Comment("Should the Polarized Stone Effect Nearby Exp")
	@Name("02. Collect nearby exp")
	public boolean collectXP = true;

	@Config.Comment("Should the Polarized Stone Repell Incoming projectiles. Default True")
	@Name("03. Repell Projectiles")
	public boolean repell = true;

	@Config.Comment("Should The Polarized Stone Cost Hunger to Repell Projectiles. Default True")
	@Name("04. Repell costs Hunger")
	public boolean exhaustion = true;

	@Config.Comment("How many Chunks of Hunger per tickrate.")
	@Name("05. Hunger Exhaustion Cost")
	@RangeDouble(min = 0)
	public float exhaust_rate = 1F;

	@Config.Comment("How often in Ticks hunger is reduced while Repell is Active")
	@Name("06. Hunger Exhaustion Rate")
	@RangeInt(min = 0)
	public float exhaust_ticks = 20;

	@Config.Comment("If Instant Pickup is Disabled, Polarized Stone's Push and pull speed. Default 0.1, 0.1 MIN, 1.0 MAX")
	@Name("07. Polarized Stone Push and Pull Speed")
	@RangeDouble(min = 0.1, max = 1)
	public double Polarized_Stone_Speed = 0.1;

	@Config.Comment("WARNING! SETTING THESE VALUES TOO HIGH WILL CAUSE YOU TO LAG. Try to Keep within a range of 4-16")
	@Name("Pickup Range")
	public Pickup_Range PR = new Pickup_Range();
	public class Pickup_Range {
		@Config.Comment("How Far Vertically(Up, Down) in Blocks the Polarized Stone collects Items and XP. Default 6, MIN 0, MAX 32")
		@Name("Vertical Distance")
		@RangeInt(min = 0, max = 32)
		public int VD = 6;

		@Config.Comment("How Far Horizontally(N, E, S, W) in Blocks the Polarized Stone collects Items and XP. Default 12, MIN 0, MAX 32")
		@Name("Horizontal Distance")
		@RangeInt(min = 0, max = 32)
		public int HD = 12;
	}

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "trinket";

}
