package xzeroair.trinkets.races.faelis.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

// owo uwu

public class FaelisConfig {
	private final String name = "faelis";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Name("01. Claw Bonus")
	@LangKey(PREFIX + ".claw.bonus")
	public boolean claw_bonus = true;
	@Name("02. Bonus amount")
	@LangKey(PREFIX + ".claw.bonus.amount")
	public double bonus = 3.25;
	@Config.Comment("")
	@Name("03. Armor Penalties")
	@LangKey(PREFIX + ".armor.penalties")
	public boolean penalties = true;
	@Config.Comment("The Amount of Weight per armor piece equipped")
	@Name("04. Penaltiy amount")
	@LangKey(PREFIX + ".armor.penalties.amount")
	public double penalty_amount = 0.0250;

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

	//@formatter:off
	private final boolean 	armor = false;
	private final double 	armorAmount = 0;
	private final int		armorOperation = 0;
	private final boolean 	attackSpeed = true;
	private final double 	attackSpeedAmount = 0.15;
	private final int		attackSpeedOperation = 2;
	private final boolean 	damage = true;
	private final double 	damageAmount = -0.25;
	private final int		damageOperation = 2;
	private final boolean 	health = true;
	private final double 	healthAmount = -0.25;
	private final int		healthOperation = 2;
	private final boolean 	knockback = false;
	private final double 	knockbackAmount = 0;
	private final int		knockbackOperation = 0;
	private final boolean 	speed = true;
	private final double 	speedAmount = 0.15;
	private final int		speedOperation = 2;
	private final boolean 	swimSpeed = true;
	private final double 	swimSpeedAmount = 0.3;
	private final int		swimSpeedOperation = 2;
	private final boolean 	toughness = true;
	private final double 	toughnessAmount = -0.15;
	private final int		toughnessOperation = 2;
	private final boolean	luck = true;
	private final double	luckAmount = 2;
	private final int		luckOperation = 0;
	private final boolean	reach = true;
	private final double	reachAmount = -0.1;
	private final int		reachOperation = 2;
	private final boolean	jump = true;
	private final double	jumpAmount = 0.6;
	private final int		jumpOperation = 2;
	private final boolean	stepHeight = false;
	private final double	stepHeightAmount = 1;
	private final int		stepHeightOperation = 2;


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
