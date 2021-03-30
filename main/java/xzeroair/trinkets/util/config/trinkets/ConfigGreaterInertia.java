package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

//@formatter:off
public class ConfigGreaterInertia {

	private final String name = ModItems.GreaterInertia;
	private final String PREFIX = Reference.MODID + ".config." + name;
	private final String registry = Reference.MODID + ".config.registry";

	//TODO Sort this out
//	@Config.Comment("Jump Modifications. Set to true to Enable. Default False")
//	@Name("Modified Jump Height")
//	@LangKey(PREFIX + ".jumpheight")
//	public boolean jump = true;

//	@Config.Comment("Jump Height Modification Amount.")
//	@Name("Modified Jump Height Amount")
//	@Config.RangeDouble(min = 1, max = 10)
//	@LangKey(PREFIX + ".jumpheight.amount")
//	public float jumpheight = 2f;

	@Config.Comment("Should you Take Fall Damage?. Set to true to Enable. Default False")
	@Name("Modified Fall Damage")
	@LangKey(PREFIX + ".falldamage")
	public boolean fall_damage = true;

	@Name("Modified Fall Damage Amount")
	@Config.RangeDouble(min = 0, max = 10)
	@LangKey(PREFIX + ".falldamage.amount")
	public float falldamage_amount = 0.25f;

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
	private final boolean 	knockback = true;
	private final double 	knockbackAmount = 0.4D;
	private final int		knockbackOperation = 0;
	private final boolean 	speed = true;
	private final double 	speedAmount = 1.5D;
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
	private final boolean	reach = false;
	private final double	reachAmount = 0;
	private final int		reachOperation = 0;
	private final boolean	jump = true;
	private final double	jumpAmount = 2;
	private final int		jumpOperation = 2;
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
