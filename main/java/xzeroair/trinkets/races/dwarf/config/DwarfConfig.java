package xzeroair.trinkets.races.dwarf.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

//@formatter:off
public class DwarfConfig {

	private final String name = "dwarf";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

		@Config.Comment("Dwarves Special Fortune Like Effect. Set to False to Disable. Default True")
		@Name("01. Fortune Effect")
		@LangKey(PREFIX + ".fortune")
		public boolean fortune = true;

		@Config.Comment("Should the Dwarves Fortune Effect stack with the Fortune Enchantment?. Set to False to Disable. Default True")
		@Name("02. Fortune Stacking")
		@LangKey(PREFIX + ".fortune.stacking")
		public boolean fortune_mix = true;

		@Config.Comment("Should the Dwarves lower the mining level requirement for pickaxes. IE. an Iron Pickaxe will be able to break Obsidian. Set to False to Disable. Default True")
		@Name("03. Skilled Mining Ability")
		@LangKey(PREFIX + ".skilledminer")
		public boolean skilled_miner = true;

		@Config.Comment("Mining Speed is static at (Block Hardness * 4), Not Including other Modifiers.  Set to False to Disable. Default True")
		@Name("04. Static Mining")
		@LangKey(PREFIX + ".static_mining")
		public boolean static_mining = true;

		@Name("Block Settings")
		@LangKey(PREFIX + ".blocks")
		public Blocks BLOCKS = new Blocks();
		public class Blocks {

			@Name("01. Ore Blocks that Fortune Effect works on")
			@LangKey(PREFIX + ".fortune.blocks.list")
			public String[] Blocks = new String[] {
					"minecraft:coal_ore",
					"minecraft:lapis_ore",
					"minecraft:diamond_ore",
					"minecraft:redstone_ore",
					"minecraft:lit_redstone_ore",
					"minecraft:emerald_ore",
					"minecraft:quartz_ore"
			};

			@Config.Comment("Should Blocks give Bonus XP")
			@Name("02. Bonus XP")
			@LangKey(PREFIX + ".blocks.xp")
			public boolean bonus_exp = true;

			@Name("03. Bonus XP Max")
			@LangKey(PREFIX + ".blocks.xp.max")
			public int bonus_exp_max = 2;

			@Name("04. Bonus XP Min")
			@LangKey(PREFIX + ".blocks.xp.min")
			public int bonus_exp_min = 0;

			@Config.Comment("Blocks in this List will give Bonus XP Randomly between Bonus_XP_Min and Bonus_XP_Max")
			@Name("05. Blocks that give Bonus XP")
			@LangKey(PREFIX + ".blocks.xp.list")
			public String[] xPBlocks = new String[] {
					"minecraft:coal_ore",
					"minecraft:iron_ore",
					"minecraft:gold_ore",
					"minecraft:lapis_ore",
					"minecraft:redstone_ore",
					"minecraft:diamond_ore",
					"minecraft:emerald_ore",
					"minecraft:quartz_ore"
			};

			@Config.Comment("Should Blocks give at least 1 XP")
			@Name("06. Minimum XP Blocks")
			@LangKey(PREFIX + ".blocks.minimum.xp")
			public boolean minXpBlocks = true;

			@Config.Comment("Blocks in this List will always give 1 xp when broken")
			@Name("07. Blocks that give a Minimum XP")
			@LangKey(PREFIX + ".blocks.minimum.xp.list")
			public String[] MinBlocks = new String[] { "minecraft:stone", "minecraft:end_stone" };
		}

		@Name("Compatability Settings")
		@LangKey(Reference.MODID + ".config.compatability")
		private Compatability compat = new Compatability();
		public class Compatability {

			@Name("Tough as Nails Compatability")
			@LangKey(Reference.MODID + ".config.toughasnails")
			private TANCompat tan = new TANCompat();
			public class TANCompat {

			}

		}

		private final boolean 	armor = false;
		private final double 	armorAmount = 0;
		private final int		armorOperation = 1;
		private final boolean 	attackSpeed = true;
		private final double 	attackSpeedAmount = -0.25D;
		private final int		attackSpeedOperation = 2;
		private final boolean 	damage = true;
		private final double 	damageAmount = 0.25D;
		private final int		damageOperation = 1;
		private final boolean 	health = true;
		private final double 	healthAmount = -0.3D;
		private final int		healthOperation = 1;
		private final boolean 	knockback = true;
		private final double 	knockbackAmount = 0.2;
		private final int		knockbackOperation = 1;
		private final boolean 	speed = true;
		private final double 	speedAmount = -0.25D;
		private final int		speedOperation = 1;
		private final boolean 	swimSpeed = false;
		private final double 	swimSpeedAmount = 0;
		private final int		swimSpeedOperation = 1;
		private final boolean 	toughness = true;
		private final double 	toughnessAmount = 0.25D;
		private final int		toughnessOperation = 2;
		private final boolean	luck = false;
		private final double	luckAmount = 0;
		private final int		luckOperation = 1;
		private final boolean	reach = false;
		private final double	reachAmount = 0;
		private final int		reachOperation = 1;
		private final boolean	jump = false;
		private final double	jumpAmount = 0;
		private final int		jumpOperation = 1;
		private final boolean	stepHeight = false;
		private final double	stepHeightAmount = 0;
		private final int		stepHeightOperation = 1;


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
