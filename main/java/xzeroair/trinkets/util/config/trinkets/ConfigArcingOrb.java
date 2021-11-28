package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

//@formatter:off
public class ConfigArcingOrb {
	private final String name = ModItems.ArcingOrb;
	private final String PREFIX = Reference.MODID + ".config." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Name("Bolt Attack")
	@Config.LangKey(PREFIX + ".bold.enabled")
	public boolean attackAbility = true;

	@Config.Name("Bolt Attack Damage")
	@Config.LangKey(PREFIX + ".bolt.damage")
	public float attackDmg = 40F;

	@Config.Name("Bolt Attack Cost")
	@Config.LangKey(PREFIX + ".bolt.cost")
	public float attackCost = 300F;

	@Config.Name("Dodge")
	@Config.LangKey(PREFIX + ".dodge.enabled")
	public boolean dodgeAbility = true;

	@Config.Name("Dodge Cost")
	@Config.LangKey(PREFIX + ".dodge.cost")
	public float dodgeCost = 30F;

	@Config.Name("Dodge Stuns")
	@Config.LangKey(PREFIX + "dodge.stuns.enabled")
	public boolean dodgeStuns = true;

	@Config.Name("Stun Radius")
	@Config.LangKey(PREFIX + "dodge.stuns.radius")
	public double stunDistance = 2;

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
		public BaubleCompat baubles = new BaubleCompat("belt");
	}

	protected final boolean armor = false;
	protected final double 	armorAmount = 0;
	protected final int		armorOperation = 0;
	protected final boolean attackSpeed = false;
	protected final double 	attackSpeedAmount = 0;
	protected final int		attackSpeedOperation = 0;
	protected final boolean damage = false;
	protected final double 	damageAmount = 0;
	protected final int		damageOperation = 0;
	protected final boolean health = false;
	protected final double 	healthAmount = 0;
	protected final int		healthOperation = 0;
	protected final boolean knockback = false;
	protected final double 	knockbackAmount = 0;
	protected final int		knockbackOperation = 0;
	protected final boolean speed = true;
	protected final double 	speedAmount = 0.25;
	protected final int		speedOperation = 1;
	protected final boolean swimSpeed = false;
	protected final double 	swimSpeedAmount = 0;
	protected final int		swimSpeedOperation = 0;
	protected final boolean toughness = false;
	protected final double 	toughnessAmount = 0;
	protected final int		toughnessOperation = 0;
	protected final boolean	luck = false;
	protected final double	luckAmount = 0;
	protected final int		luckOperation = 0;
	protected final boolean	reach = false;
	protected final double	reachAmount = 0;
	protected final int		reachOperation = 0;
	private final boolean	jump = false;
	private final double	jumpAmount = 0;
	private final int		jumpOperation = 0;
	private final boolean	stepHeight = false;
	private final double	stepHeightAmount = 0;
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
