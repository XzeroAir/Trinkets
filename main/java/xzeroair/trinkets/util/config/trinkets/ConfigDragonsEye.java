package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

//@formatter:off
public class ConfigDragonsEye {

	private final String name = ModItems.DragonsEye;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
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

		@Config.RequiresWorldRestart
		@Config.Comment("Find the Closest Block to the player. False to Compile a list of all nearby")
		@Config.Name("Find Closest")
		@LangKey(PREFIX + ".orefinder.blocks.closest")
		public boolean closest = true;

		@Config.RequiresWorldRestart
		@Config.Comment({
				"Accepts:",
				"oreDictionary Names, example: oreCoal",
				"Block Ids, example: minecraft:chest",
				"Block Id and Meta, example: minecraft:stone[1]"
		})
		@Name("Ore Blocks The Dragon's Eye can See")
		@LangKey(PREFIX + ".orefinder.blocks.list")
		public String[] Blocks = new String[] {
				"oreCoal;4605510",
				"oreIron;16764057",
				"oreGold;16766720",
				"oreLapis;2515356",
				"oreRedstone;11546150",
				"oreDiamond;59135",
				"oreEmerald;65357",
				"oreQuartz;15461355",
				"minecraft:chest;*;16766720",
				"minecraft:chest_minecart;16766720"
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
			@Config.Comment("If Tough as Nails is Installed, Should the player be immune to Cold")
			@Name("01. Immune to Cold")
			@LangKey(PREFIX + ".toughasnails.immunity.cold")
			public boolean immuneToCold = true;

		}

		@Name("Ice and Fire Compatability")
		@LangKey(Reference.MODID + ".config.iceandfire")
		public IAFCompat iaf = new IAFCompat();

		public class IAFCompat {

			@Config.Comment("If Ice and Fire is Installed, Should the Dragon's Eye Require a Specific Stage of Skull for the recipe. Set to 0 for any stage")
			@Name("00. Skull Stage")
			@LangKey(PREFIX + ".iceandfire.recipe.stage")
			public int stage = 5;

			@Config.Comment("Should there be an Ice Variant of the Dragons eye")
			@Name("01. Ice Variant")
			@LangKey(PREFIX + ".iceandfire.ice.variant")
			public boolean ICE_VARIANT = true;

			@Config.Comment("Should the Ice Variant have Frost Walker")
			@Name("02. Frost Walker")
			@LangKey(PREFIX + ".iceandfire.ice.frostwalker")
			public boolean FROST_WALKER = true;

			@Config.Comment("Should there be a Lightning Variant of the Dragons eye")
			@Name("03. Lightning Variant")
			@LangKey(PREFIX + ".iceandfire.lightning.variant")
			public boolean LIGHTNING_VARIANT = true;

			@Config.Comment("Should the Lightning Variant have Paralysis Immunity")
			@Name("04. Paralysis Immunity")
			@LangKey(PREFIX + ".iceandfire.lightning.paralysis")
			public boolean PARALYSIS_IMMUNITY = true;
		}

		@Name("Fire Resistance Tiers")
		@LangKey(Reference.MODID + ".config.fire_resistance_tiers")
		public FireResistanceTiers FRTiers = new FireResistanceTiers();

		public class FireResistanceTiers {

			public int amplifier = 0;
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

	@Config.Comment({"For Mor Information on Attributes", "https://minecraft.gamepedia.com/Attribute"})
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:0, Operation:0",
			"Name:generic.knockbackResistance; Amount:0; Operation:0",
			"Name:generic.movementSpeed, Amount:0, Operation:0",
			"Name:generic.attackDamage, Amount:0, Operation:0",
			"Name:generic.attackSpeed, Amount:0, Operation:0",
			"Name:generic.armor, Amount:0, Operation:0",
			"Name:generic.armorToughness, Amount:0, Operation:0",
			"Name:generic.luck, Amount:0, Operation:0",
			"Name:forge.swimSpeed, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
			"Name:xat.entityMagic.affinity, Amount:0, Operation:0",
			"Name:xat.jump, Amount:0, Operation:0",
			"Name:xat.stepheight, Amount:0, Operation:0"
	};

}
