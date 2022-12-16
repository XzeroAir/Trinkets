package xzeroair.trinkets.races.dwarf.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

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

		@Config.Comment({"For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute"})
		@Name("Attributes")
		@LangKey(Reference.MODID + ".config.attributes")
		public String[] attributes = {
				"Name:generic.maxHealth, Amount:-0.3, Operation:1",
				"Name:generic.knockbackResistance; Amount:0.2; Operation:1",
				"Name:generic.movementSpeed, Amount:-0.25, Operation:1",
				"Name:generic.attackDamage, Amount:0.25, Operation:1",
				"Name:generic.attackSpeed, Amount:-0.25, Operation:2",
				"Name:generic.armor, Amount:0, Operation:0",
				"Name:generic.armorToughness, Amount:0.25, Operation:2",
				"Name:generic.luck, Amount:0, Operation:0",
				"Name:generic.reachDistance, Amount:0, Operation:0",
				"Name:forge.swimSpeed, Amount:0, Operation:0",
				"Name:xat.entityMagic.regen, Amount:0, Operation:0",
				"Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
				"Name:xat.entityMagic.affinity, Amount:0, Operation:0",
				"Name:xat.jump, Amount:0, Operation:0",
				"Name:xat.stepheight, Amount:0, Operation:0"
		};
}
