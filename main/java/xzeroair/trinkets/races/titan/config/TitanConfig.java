package xzeroair.trinkets.races.titan.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

public class TitanConfig {

	private final String name = "titan";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Config.Comment("Jump Height Adjustment when wearing Race Rings. Set to False to Disable. Default True")
	@Name("01. Jump Height")
	@LangKey(PREFIX + ".jumpheight")
	public boolean step_height = true;

	@Config.Comment("If the player should be too heavy to stay afloat")
	@Name("02. Heavy Player")
	@LangKey(PREFIX + ".heavy")
	public boolean sink = true;

	@Config.Comment("If Enabled the player will trample farmland")
	@Name("03. Trample Farmland")
	@LangKey(PREFIX + ".trample")
	public boolean trample = true;

	@Config.Comment("If Enabled the player will be able to mine a 3x3 area")
	@Name("04. AoE Mining")
	@LangKey(PREFIX + ".mining.extended")
	public boolean miningExtend = true;

	@Config.Comment("If Enabled the player will be required to sneak to AoE Mine")
	@Name("05. Invert AoE Mining")
	@LangKey(PREFIX + ".mining.extended.inverted")
	public boolean miningExtendInverted = false;

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
	private final double 	attackSpeedAmount = -0.5D;
	private final int		attackSpeedOperation = 2;
	private final boolean 	damage = true;
	private final double 	damageAmount = 0.5D;
	private final int		damageOperation = 2;
	private final boolean 	health = true;
	private final double 	healthAmount = 1D;
	private final int		healthOperation = 2;
	private final boolean 	knockback = true;
	private final double 	knockbackAmount = 0.3D;
	private final int		knockbackOperation = 0;
	private final boolean 	speed = true;
	private final double 	speedAmount = -0.25D;
	private final int		speedOperation = 2;
	private final boolean 	swimSpeed = false;
	private final double 	swimSpeedAmount = 0;
	private final int		swimSpeedOperation = 0;
	private final boolean 	toughness = false;
	private final double 	toughnessAmount = 0;
	private final int		toughnessOperation = 0;
	private final boolean	luck = false;
	private final double	luckAmount = 0;
	private final int		luckOperation = 0;
	private final boolean	reach = true;
	private final double	reachAmount = 0.6D;
	private final int		reachOperation = 2;
	private final boolean	jump = true;
	private final double	jumpAmount = 0.75;
	private final int		jumpOperation = 2;
	private final boolean	stepHeight = true;
	private final double	stepHeightAmount = 1.4;
	private final int		stepHeightOperation = 0;


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
