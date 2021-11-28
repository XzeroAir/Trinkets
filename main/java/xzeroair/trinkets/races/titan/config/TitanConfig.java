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

	@Config.Comment("what blocks can not be AoE Mined")
	@Name("06. AoE Mining Blacklist")
	@LangKey(PREFIX + ".mining.extended.blacklist")
	public String[] miningAoEBlacklist = new String[] {
			"dynamictrees:*"
	};

	@Config.Comment("If the player can Mount Entities at all")
	@Name("07. Able to Mount Entities")
	@LangKey(PREFIX + ".mount.enabled")
	public boolean canMount = true;

	@Config.Comment("Should the Mount Blacklist to a Whitelist")
	@Name("08. Toggle Whitelist")
	@LangKey(PREFIX + ".mount.whitelist")
	public boolean whitelist = false;

	@Config.Comment("Entities that can not be mounted, or can only be mounted")
	@Name("09. Mount Blacklist")
	@LangKey(PREFIX + ".mount.blacklist")
	public String[] mountBlacklist = new String[] {
			"quark:seat",
			"sit:entity_sit",
			"minecraft:minecart"
	};

	@Name("Compatability Settings")
	@LangKey(Reference.MODID + ".config.compatability")
	private final Compatability compat = new Compatability();

	public class Compatability {

		@Name("Tough as Nails Compatability")
		@LangKey(Reference.MODID + ".config.toughasnails")
		private final TANCompat tan = new TANCompat();

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
	private final int		healthOperation = 1;
	private final boolean 	knockback = true;
	private final double 	knockbackAmount = 1D;
	private final int		knockbackOperation = 0;
	private final boolean 	speed = true;
	private final double 	speedAmount = 0D;
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
	private final boolean	reach = true;
	private final double	reachAmount = 1D;
	private final int		reachOperation = 1;
	private final boolean	jump = true;
	private final double	jumpAmount = 0.75;
	private final int		jumpOperation = 1;
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
