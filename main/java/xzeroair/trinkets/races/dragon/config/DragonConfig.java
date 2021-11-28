package xzeroair.trinkets.races.dragon.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

public class DragonConfig {
	private final String name = "dragon";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Config.RequiresWorldRestart
	@Config.Comment("Creative Flight. Set to False to Disable. Default True")
	@Name("00. Creative Flight")
	@LangKey(PREFIX + ".flight")
	public boolean creative_flight = true;

	@Config.Comment("How much damage per second the dragon breath does")
	@Name("01. Dragon Breath Damage")
	@LangKey(PREFIX + ".breath.damage")
	public float breath_damage = 1F;

	@Config.Comment("The Mana Cost per tick when using dragons breath")
	@Name("02. Dragon Breath Cost")
	@LangKey(PREFIX + ".breath.cost")
	public float breath_cost = 10F;

	@Config.RequiresWorldRestart
	@Config.Comment("Change the flight speed from the Vanilla Default of 0.05")
	@Name("03. Change Flight Speed")
	@LangKey(PREFIX + ".flight.speed")
	public boolean creative_flight_speed = true;

	@Config.RequiresWorldRestart
	@Config.Comment("How Fast the player moves when in Creative Flight. Vanilla Default 0.05. Default 0.02")
	@Name("04. Creative Flight Speed")
	@Config.RangeDouble(min = 0.01, max = 1)
	@LangKey(PREFIX + ".flight.speed.amount")
	public double flight_speed = 0.02;

	@Config.Comment("Mana Cost per second while flying")
	@Name("05. Flight Cost")
	@LangKey(PREFIX + ".flight.cost")
	public float flight_cost = 5F;

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
			@LangKey(Reference.MODID + ".config." + ModItems.DragonsEye + ".toughasnails.immunity.heat")
			public boolean immuneToHeat = true;
		}

	}

	//@formatter:off
	private final boolean 	armor = false;
	private final double 	armorAmount = 0;
	private final int		armorOperation = 2;
	private final boolean 	attackSpeed = false;
	private final double 	attackSpeedAmount = 0;
	private final int		attackSpeedOperation = 2;
	private final boolean 	damage = true;
	private final double 	damageAmount = 0.5;
	private final int		damageOperation = 1;
	private final boolean 	health = true;
	private final double 	healthAmount = 0.25;
	private final int		healthOperation = 1;
	private final boolean 	knockback = false;
	private final double 	knockbackAmount = 0;
	private final int		knockbackOperation = 1;
	private final boolean 	speed = false;
	private final double 	speedAmount = 0;
	private final int		speedOperation = 1;
	private final boolean 	swimSpeed = false;
	private final double 	swimSpeedAmount = 0;
	private final int		swimSpeedOperation = 1;
	private final boolean 	toughness = true;
	private final double 	toughnessAmount = 0.5;
	private final int		toughnessOperation = 1;
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
			jump, 			jumpAmount, 			jumpOperation,
			stepHeight,		stepHeightAmount, 	stepHeightOperation
			);
}
