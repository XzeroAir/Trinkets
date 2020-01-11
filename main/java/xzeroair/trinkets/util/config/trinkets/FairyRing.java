package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

public class FairyRing {

	@Config.Comment("Health Modifications. Set to False to Disable. Default True")
	@Name("01. Health")
	public boolean health = true;

	@Config.Comment("Health Modification Amount When Transformed. Negative Values mean you have Less Health")
	@Name("02. Health Amount")
	@Config.RangeDouble(min = -18D, max = 40D)
	public double health_amount = -12D;

	@Config.Comment("Damage Modifications. Set to False to Disable. Default True")
	@Name("03. Damage")
	public boolean damage = true;

	@Config.Comment("Damage Modification Amount When Transformed. Negative Values mean you do Less Damage")
	@Name("04. Damage Amount")
	@Config.RangeDouble(min = -1D, max = 40D)
	public double damage_amount = -0.75D;

	@Config.Comment("Armor Modifications. Set to true to Enable. Default False")
	@Name("05. Armor")
	public boolean armor = false;

	@Config.Comment("Armor Modification Amount When Transformed. Negative Values mean you have Less Armor")
	@Name("06. Armor Amount")
	@Config.RangeDouble(min = -20D, max = 40D)
	public double armor_amount = -10D;

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

	@Config.RequiresMcRestart
	@Config.Comment("Creative Flight when wearing the Fairies Ring. Set to False to Disable. Default True")
	@Name("11. Creative Flight")
	public boolean creative_flight = true;

	@Config.RequiresMcRestart
	@Config.Comment("Change the flight speed from the Vanilla Default of 0.05")
	@Name("12. Change Flight Speed")
	public boolean creative_flight_speed = false;

	@Config.RequiresMcRestart
	@Config.Comment("How Fast the player moves when in Creative Flight. Vanilla Default 0.05. Default 0.02")
	@Name("13. Creative Flight Speed")
	@Config.RangeDouble(min = 0.01, max = 1)
	public double flight_speed = 0.02;

	@Config.Comment("Fairy's Ring Climbing Ability. Set to False to Disable. Default True")
	@Name("14. Climbing")
	public boolean climbing = true;

	@Name("15. Climable Blocks")
	public String[] allowedBlocks = new String[] {
			"minecraft:dirt",
			"minecraft:grass",
			"minecraft:sand",
			"minecraft:cobblestone",
			"minecraft:mossy_cobblestone",
			"minecraft:oak_stairs",
			"minecraft:stone_stairs",
			"minecraft:brick_stairs",
			"minecraft:stone_brick_stairs",
			"minecraft:nether_brick_stairs",
			"minecraft:sandstone_stairs",
			"minecraft:spruce_stairs",
			"minecraft:birch_stairs",
			"minecraft:jungle_stairs",
			"minecraft:arcacia_stairs",
			"minecraft:quartz_stairs",
			"minecraft:dark_oak_stairs",
			"minecraft:red_sandstone_stairs",
			"minecraft:purpur_stairs",
			"minecraft:stone_slab",
			"minecraft:wooden_slab",
			"minecraft:stone_slab2",
			"minecraft:purpur_slab"
	};

	@Config.Comment("Jump Height Adjustment when wearing Race Rings. Set to False to Disable. Default True")
	@Name("16. Jump Height")
	public boolean step_height = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "ring";

}
