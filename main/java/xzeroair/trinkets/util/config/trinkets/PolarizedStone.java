package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.Attribs;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class PolarizedStone {

	private final String name = "polarized_stone";
	private final String PREFIX = Reference.MODID + ".config." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("Should the Polarized Stone Instantly pickup Items, or Pull them toward you. Default True")
	@Name("01. Instant Pickup")
	@LangKey(PREFIX + ".collection.instant")
	public boolean instant_pickup = true;

	@Config.Comment("Should the Polarized Stone Effect Nearby Exp")
	@Name("02. Collect nearby exp")
	@LangKey(PREFIX + ".collection.xp")
	public boolean collectXP = true;

	@Config.Comment("Should the Polarized Stone Repel Incoming projectiles. Default True")
	@Name("03. Repel Projectiles")
	@LangKey(PREFIX + ".repel")
	public boolean repell = true;

	@Config.Comment("Should The Polarized Stone Cost Hunger to Repel Projectiles. Default True")
	@Name("04. Repel costs Hunger")
	@LangKey(PREFIX + ".repel.exhaust")
	public boolean exhaustion = true;

	@Config.Comment("How many Chunks of Hunger per tickrate.")
	@Name("05. Hunger Exhaustion Cost")
	@RangeDouble(min = 0)
	@LangKey(PREFIX + ".repel.exhaust.amount")
	public float exhaust_rate = 1F;

	@Config.Comment("How often in Ticks hunger is reduced while Repel is Active")
	@Name("06. Hunger Exhaustion Rate")
	@RangeInt(min = 0)
	@LangKey(PREFIX + ".repel.exhaust.speed")
	public float exhaust_ticks = 20;

	@Config.Comment("If Instant Pickup is Disabled, Polarized Stone's Push and pull speed. Default 0.1, 0.1 MIN, 1.0 MAX")
	@Name("07. Polarized Stone Push and Pull Speed")
	@RangeDouble(min = 0.1, max = 1)
	@LangKey(PREFIX + ".collection.speed")
	public double Polarized_Stone_Speed = 0.1;

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
		public BaubleCompat baubles = new BaubleCompat("trinket");
	}

	private final boolean 	armor = false;
	private final double 	armorAmount = 0;
	private final int		armorOperation = 0;
	private final boolean 	attackSpeed = false;
	private final double 	attackSpeedAmount = 0;
	private final int		attackSpeedOperation = 0;
	private final boolean 	damage = false;
	private final double 	damageAmount = 0;
	private final int		damageOperation = 0;
	private final boolean 	health = false;
	private final double 	healthAmount = 0;
	private final int		healthOperation = 0;
	private final boolean 	knockback = false;
	private final double 	knockbackAmount = 0;
	private final int		knockbackOperation = 0;
	private final boolean 	speed = false;
	private final double 	speedAmount = 0;
	private final int		speedOperation = 0;
	private final boolean 	swimSpeed = false;
	private final double 	swimSpeedAmount = 0;
	private final int		swimSpeedOperation = 0;
	private final boolean 	toughness = false;
	private final double 	toughnessAmount = 0;
	private final int		toughnessOperation = 0;
	private final boolean	luck = false;
	private final double	luckAmount = 0;
	private final int		luckOperation = 0;
	private final boolean	reach = false;
	private final double	reachAmount = 0;
	private final int		reachOperation = 0;


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

	@Config.Comment("WARNING! SETTING THESE VALUES TOO HIGH WILL CAUSE YOU TO LAG. Try to Keep within a range of 4-16")
	@Name("Pickup Range")
	@LangKey(PREFIX + ".collection.range")
	public Pickup_Range PR = new Pickup_Range();
	public class Pickup_Range {
		@Config.Comment("How Far Vertically(Up, Down) in Blocks the Polarized Stone collects Items and XP. Default 6, MIN 0, MAX 32")
		@Name("Vertical Distance")
		@RangeInt(min = 0, max = 32)
		@LangKey(PREFIX + ".collection.range.vertical")
		public int VD = 6;

		@Config.Comment("How Far Horizontally(N, E, S, W) in Blocks the Polarized Stone collects Items and XP. Default 12, MIN 0, MAX 32")
		@Name("Horizontal Distance")
		@RangeInt(min = 0, max = 32)
		@LangKey(PREFIX + ".collection.range.horizontal")
		public int HD = 12;
	}
}
