package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

//@formatter:off
public class ConfigDragonsEye {

	private final String name = ModItems.DragonsEye;
	private final String PREFIX = Reference.MODID + ".config." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.RequiresWorldRestart
	@Config.Comment("Dragon's Eye Ore Detection. Set to False to Disable. Default True :Requires Relogin to world")
	@Name("01. Dragon's Eye Ore Detection")
	@LangKey(PREFIX + ".orefinder")
	public boolean oreFinder = true;

	@Name("Block Settings")
	@LangKey(PREFIX + ".orefinder.blocks")
	public Blocks BLOCKS = new Blocks();

	public class Blocks {
		@Config.Comment("This Doesn't Do anything Atm")
		@Name("Generate Block List")
		private boolean generate = false;

		@Config.Comment("Find the Closest Block to the player. False to Compile a list of all nearby")
		@Config.Name("Find Closest")
		@LangKey(PREFIX + ".orefinder.blocks.closest")
		public boolean closest = true;

		@Config.RequiresMcRestart
		@Config.Comment({
				"Accepts:",
				"oreDictionary Names, example: oreCoal",
				"Block Ids, example: minecraft:chest",
				"Block Id and Meta, example: minecraft:stone[1]"
		})
		@Name("Ore Blocks The Dragon's Eye can See")
		@LangKey(PREFIX + ".orefinder.blocks.list")
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
		@LangKey(PREFIX + ".orefinder.range")
		public Detection_Range DR = new Detection_Range();

		public class Detection_Range {

			@Config.RequiresMcRestart
			@Config.Comment("How Far Vertically(Up, Down) in Blocks the Dragon's Eye effect triggers. Default 6, MIN 0, MAX 32")
			@Name("Vertical Distance")
			@LangKey(PREFIX + ".orefinder.range.vertical")
			@RangeInt(min = 0, max = 32)
			public int C00_VD = 6;

			@Config.RequiresMcRestart
			@Config.Comment("How Far Horizontally(N, E, S, W) in Blocks the Dragon's Eye effect triggers. Default 12, MIN 0, MAX 32")
			@Name("Horizontal Distance")
			@LangKey(PREFIX + ".orefinder.range.horizontal")
			@RangeInt(min = 0, max = 32)
			public int C001_HD = 12;
		}
	}

	@Name("02. Prevent Blindess")
	@LangKey(PREFIX + ".blindness")
	public boolean prevent_blindness = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	@LangKey(registry + ".enabled")
	public boolean enabled = true;

	@Name("Compatability Settings")
	@LangKey(Reference.MODID + ".config.compatability")
	public Compatability compat = new Compatability();

	public class Compatability {

		@Name("Tough as Nails Compatability")
		@LangKey(Reference.MODID + ".config.toughasnails")
		public TANCompat tan = new TANCompat();

		public class TANCompat {

			@Config.Comment("If Tough as Nails is Installed, Should the player be immune to Heat")
			@Name("00. Immune to Heat")
			@LangKey(PREFIX + ".toughasnails.immunity.heat")
			public boolean immuneToHeat = true;

		}

		@Name("Ice and Fire Compatability")
		@LangKey(Reference.MODID + ".config.iceandfire")
		public IAFCompat iaf = new IAFCompat();

		public class IAFCompat {

			@Config.Comment("If Ice and Fire is Installed, Should the Dragon's Eye Require a Specific Stage of Skull for the recipe. Set to 0 for any stage")
			@Name("00. Skull Stage")
			@LangKey(PREFIX + ".iceandfire.recipe.stage")
			public int stage = 5;
		}

		@Name("Baubles Compatability")
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
		@LangKey(Reference.MODID + ".config.baubles")
		public BaubleCompat baubles = new BaubleCompat("head");
	}

	private final boolean armor = false;
	private final double armorAmount = 0;
	private final int armorOperation = 0;
	private final boolean attackSpeed = false;
	private final double attackSpeedAmount = 0;
	private final int attackSpeedOperation = 0;
	private final boolean damage = false;
	private final double damageAmount = 0;
	private final int damageOperation = 0;
	private final boolean health = false;
	private final double healthAmount = 0;
	private final int healthOperation = 0;
	private final boolean knockback = false;
	private final double knockbackAmount = 0;
	private final int knockbackOperation = 0;
	private final boolean speed = false;
	private final double speedAmount = 0;
	private final int speedOperation = 0;
	private final boolean swimSpeed = false;
	private final double swimSpeedAmount = 0;
	private final int swimSpeedOperation = 0;
	private final boolean toughness = false;
	private final double toughnessAmount = 0;
	private final int toughnessOperation = 0;
	private final boolean luck = false;
	private final double luckAmount = 0;
	private final int luckOperation = 0;
	private final boolean reach = false;
	private final double reachAmount = 0;
	private final int reachOperation = 0;
	private final boolean	jump = false;
	private final double	jumpAmount = 0;
	private final int		jumpOperation = 0;
	private final boolean	stepHeight = false;
	private final double	stepHeightAmount = 0;
	private final int		stepHeightOperation = 0;


	@Config.Comment({"For Mor Information on Attributes", "https://minecraft.gamepedia.com/Attribute"})
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public ConfigAttribs Attributes = new ConfigAttribs(
			armor, 			armorAmount, 		armorOperation,
			attackSpeed, 	attackSpeedAmount, 	attackSpeedOperation,
			damage, 		damageAmount, 		damageOperation,
			health, 		healthAmount, 		healthOperation,
			knockback, 		knockbackAmount, 	knockbackOperation,
			speed, 			speedAmount, 		speedOperation,
			swimSpeed, 		swimSpeedAmount, 	swimSpeedOperation,
			toughness, 		toughnessAmount, 	toughnessOperation,
			luck,			luckAmount,			luckOperation,
			reach,			reachAmount,		reachOperation,
			jump, 			jumpAmount, 		jumpOperation,
			stepHeight,		stepHeightAmount, 	stepHeightOperation
			);

}
