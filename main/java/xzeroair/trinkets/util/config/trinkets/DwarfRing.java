package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

public class DwarfRing {

	@Config.Comment("Health Modifications. Set to False to Disable. Default True")
	@Name("01. Health")
	public boolean health = true;

	@Config.Comment("Health Modification Amount When Transformed. Negative Values mean you have Less Health")
	@Name("02. Health Amount")
	@Config.RangeDouble(min = -18D, max = 40D)
	public double health_amount = -6D;

	@Config.Comment("Damage Modifications. Set to False to Disable. Default True")
	@Name("03. Damage")
	public boolean damage = true;

	@Config.Comment("Damage Modification Amount When Transformed. Negative Values mean you do Less Damage")
	@Name("04. Damage Amount")
	@Config.RangeDouble(min = -1D, max = 40D)
	public double damage_amount = -0.5D;

	@Config.Comment("Armor Modifications. Set to true to Enable. Default False")
	@Name("05. Armor")
	public boolean armor = false;

	@Config.Comment("Armor Modification Amount When Transformed. Negative Values mean you have Less Armor")
	@Name("06. Armor Amount")
	@Config.RangeDouble(min = -20D, max = 40D)
	public double armor_amount = -5D;

	@Config.Comment("Toughness Modifications. Set to true to Enable. Default False")
	@Name("07. Toughness")
	public boolean toughness = false;

	@Config.Comment("Toughness Modification Amount When Transformed. Negative Values mean you have Less Armor Toughness")
	@Name("08. Toughness Amount")
	@Config.RangeDouble(min = -40D, max = 40D)
	public double toughness_amount = 0D;

	@Config.Comment("Speed Modifications. Set to true to Enable. Default False")
	@Name("09. Speed")
	public boolean speed = false;

	@Config.Comment("Speed Modification Amount When Transformed. Negative Values mean you move slower")
	@Name("10. Speed Amount")
	@Config.RangeDouble(min = -0.1D, max = 10D)
	public double speed_amount = 0D;

	@Config.Comment("Dwarves Ring Special Fortune Like Effect. Set to False to Disable. Default True")
	@Name("11. Fortune Effect")
	public boolean fortune = true;

	@Config.Comment("Should the Dwarves Ring Fortune Effect stack with the Fortune Enchantment?. Set to False to Disable. Default True")
	@Name("12. Fortune Stacking")
	public boolean fortune_mix = true;

	@Config.Comment("Should the Dwarves Ring lower the mining level requirement for pickaxes. IE. an Iron Pickaxe will be able to break Obsidian. Set to False to Disable. Default True")
	@Name("13. Skilled Mining Ability")
	public boolean skilled_miner = true;

	@Config.Comment("While Wearing this Item, Mining Speed is static at (Block Hardness * 4), Not Including other Modifiers.  Set to False to Disable. Default True")
	@Name("14. Static Mining")
	public boolean static_mining = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "ring";

	@Name("Block Settings")
	public Blocks BLOCKS = new Blocks();
	public class Blocks {
		@Config.Comment("Should Blocks give Bonus XP")
		@Name("01. Bonus XP")
		public boolean bonus_exp = true;

		@Name("02. Ore Blocks that Fortune Effect works on")
		public String[] Blocks = new String[] {
				"minecraft:coal_ore",
				"minecraft:lapis_ore",
				"minecraft:diamond_ore",
				"minecraft:redstone_ore",
				"minecraft:emerald_ore",
				"minecraft:quartz_ore"
		};

		@Name("03. Bonus XP Max")
		public int bonus_exp_max = 2;

		@Name("04. Bonus XP Min")
		public int bonus_exp_min = 0;

		@Config.Comment("Blocks in this List will give Bonus XP Randomly between Bonus_XP_Min and Bonus_XP_Max")
		@Name("05. Blocks that give Bonus XP")
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
		public boolean minXpBlocks = true;

		@Config.Comment("Blocks in this List will always give 1 xp when broken")
		@Name("07. Blocks that give a Minimum XP")
		public String[] MinBlocks = new String[] { "minecraft:stone", "minecraft:end_stone" };
	}

}
