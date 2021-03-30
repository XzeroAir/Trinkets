package xzeroair.trinkets.races.goblin.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

public class GoblinConfig {

	private final String name = "goblin";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Config.Comment("")
	@Name("01. Resistances")
	@LangKey(PREFIX + ".natural_resistance")
	public boolean natural_resistance = true;
	@Config.Comment("")
	@Name("02. Goblin Rider")
	@LangKey(PREFIX + ".rider")
	public boolean rider = true;

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
	private final boolean 	attackSpeed = false;
	private final double 	attackSpeedAmount = 0;
	private final int		attackSpeedOperation = 0;
	private final boolean 	damage = true;
	private final double 	damageAmount = -0.5;
	private final int		damageOperation = 1;
	private final boolean 	health = true;
	private final double 	healthAmount = -0.4;
	private final int		healthOperation = 2;
	private final boolean 	knockback = false;
	private final double 	knockbackAmount = 0;
	private final int		knockbackOperation = 0;
	private final boolean 	speed = true;
	private final double 	speedAmount = 0.2;
	private final int		speedOperation = 1;
	private final boolean 	swimSpeed = true;
	private final double 	swimSpeedAmount = 0.1;
	private final int		swimSpeedOperation = 1;
	private final boolean 	toughness = false;
	private final double 	toughnessAmount = 0;
	private final int		toughnessOperation = 0;
	private final boolean	luck = true;
	private final double	luckAmount = 1;
	private final int		luckOperation = 0;
	private final boolean	reach = false;
	private final double	reachAmount = 0;
	private final int		reachOperation = 0;
	private final boolean	jump = false;
	private final double	jumpAmount = 0;
	private final int		jumpOperation = 2;
	private final boolean	stepHeight = false;
	private final double	stepHeightAmount = 0;
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
