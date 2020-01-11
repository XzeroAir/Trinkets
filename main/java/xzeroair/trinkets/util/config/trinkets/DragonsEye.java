package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;

public class DragonsEye {

	@Config.RequiresWorldRestart
	@Config.Comment("Dragon's Eye Ore Detection. Set to False to Disable. Default True :Requires Relogin to world")
	@Name("01. Dragon's Eye Ore Detection")
	public boolean oreFinder = true;

	@Name("Block Settings")
	public Blocks BLOCKS = new Blocks();
	public class Blocks {
		@Config.Comment("This Doesn't Do anything Atm")
		@Name("Generate Block List")
		public boolean generate = false;

		@Config.RequiresMcRestart
		@Config.Comment({
			"Accepts:",
			"oreDictionary Names, example: oreCoal",
			"Block Ids, example: minecraft:chest",
			"Block Id and Meta, example: minecraft:stone[1]"
		})
		@Name("Ore Blocks The Dragon's Eye can See")
		public String[] Blocks = new String[] {
				"oreCoal",
				"oreIron",
				"oreGold",
				"oreLapis",
				"oreRedstone",
				"oreDiamond",
				"oreEmerald",
				"oreQuartz",
				"minecraft:chest"
		};

		@Config.Comment("WARNING! SETTING THESE VALUES TOO HIGH WILL CAUSE YOU TO LAG. Try to Keep within a range of 4-16")
		@Name("Detection Range")
		public Detection_Range DR = new Detection_Range();
		public class Detection_Range {

			@Config.RequiresMcRestart
			@Config.Comment("How Far Vertically(Up, Down) in Blocks the Dragon's Eye effect triggers. Default 6, MIN 0, MAX 32")
			@Name("Vertical Distance")
			@RangeInt(min = 0, max = 32)
			public int C00_VD = 6;

			@Config.RequiresMcRestart
			@Config.Comment("How Far Horizontally(N, E, S, W) in Blocks the Dragon's Eye effect triggers. Default 12, MIN 0, MAX 32")
			@Name("Horizontal Distance")
			@RangeInt(min = 0, max = 32)
			public int C001_HD = 12;
		}
	}

	@Name("02. Prevent Blindess")
	public boolean prevent_blindness = true;

	@Config.Comment("If Tough as Nails is Installed, Should the player be immune to Heat")
	@Name("03. Immune to Heat")
	public boolean immuneToHeat = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "head";

}
