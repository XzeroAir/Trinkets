package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.Attribs;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class FairyRing {

	private final String name = "fairy_ring";
	private final String PREFIX = Reference.MODID + ".config." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.RequiresMcRestart
	@Config.Comment("Creative Flight when wearing the Fairies Ring. Set to False to Disable. Default True")
	@Name("01. Creative Flight")
	@LangKey(PREFIX + ".flight")
	public boolean creative_flight = true;

	@Config.RequiresMcRestart
	@Config.Comment("Change the flight speed from the Vanilla Default of 0.05")
	@Name("02. Change Flight Speed")
	@LangKey(PREFIX + ".flight.speed")
	public boolean creative_flight_speed = false;

	@Config.RequiresMcRestart
	@Config.Comment("How Fast the player moves when in Creative Flight. Vanilla Default 0.05. Default 0.02")
	@Name("03. Creative Flight Speed")
	@Config.RangeDouble(min = 0.01, max = 1)
	@LangKey(PREFIX + ".flight.speed.amount")
	public double flight_speed = 0.02;

	@Config.Comment("Jump Height Adjustment when wearing Race Rings. Set to False to Disable. Default True")
	@Name("04. Jump Height")
	@LangKey(PREFIX + ".jumpheight")
	public boolean step_height = true;

	@Config.Comment("Fairy's Ring Climbing Ability. Set to False to Disable. Default True")
	@Name("05. Climbing")
	@LangKey(PREFIX + ".climbing")
	public boolean climbing = true;

	@Name("06. Climable Blocks")
	@LangKey(PREFIX + ".climbing.blocks.list")
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
		private TANCompat tan = new TANCompat();
		public class TANCompat {

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
		public BaubleCompat baubles = new BaubleCompat("ring");
	}

	private final boolean 	armor = true;
	private final double 	armorAmount = -0.5D;
	private final int		armorOperation = 2;
	private final boolean 	attackSpeed = false;
	private final double 	attackSpeedAmount = 0;
	private final int		attackSpeedOperation = 0;
	private final boolean 	damage = true;
	private final double 	damageAmount = -0.5D;
	private final int		damageOperation = 2;
	private final boolean 	health = true;
	private final double 	healthAmount = -0.5D;
	private final int		healthOperation = 2;
	private final boolean 	knockback = false;
	private final double 	knockbackAmount = 0;
	private final int		knockbackOperation = 0;
	private final boolean 	speed = true;
	private final double 	speedAmount = -0.25D;
	private final int		speedOperation = 2;
	private final boolean 	swimSpeed = true;
	private final double 	swimSpeedAmount = -0.5D;
	private final int		swimSpeedOperation = 2;
	private final boolean 	toughness = true;
	private final double 	toughnessAmount = -0.25D;
	private final int		toughnessOperation = 2;
	private final boolean	luck = false;
	private final double	luckAmount = 0;
	private final int		luckOperation = 0;
	private final boolean	reach = true;
	private final double	reachAmount = -0.35D;
	private final int		reachOperation = 2;


	@Config.Comment({"For Mor Information on Attributes", "https://minecraft.gamepedia.com/Attribute"})
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public Attribs Attributes = new Attribs(
			armor, 			armorAmount, 		armorOperation,
			attackSpeed, 	attackSpeedAmount, 	attackSpeedOperation,
			damage, 		damageAmount, 		damageOperation,
			health, 		healthAmount, 		healthOperation,
			knockback, 		knockbackAmount, 	knockbackOperation,
			speed, 			speedAmount, 		speedOperation,
			swimSpeed, 		swimSpeedAmount, 	swimSpeedOperation,
			toughness, 		toughnessAmount, 	toughnessOperation,
			luck,			luckAmount,			luckOperation,
			reach,			reachAmount,		reachOperation
			);

}
