package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

//@formatter:off
public class ConfigPoisonStone {

	private final String name = ModItems.Poison;
	private final String PREFIX = Reference.MODID + ".config." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("If an Enemy is Poisoned, Should you be able to do Extra damage")
	@Name("01. Bonus Damage")
	@LangKey(PREFIX + ".damagebonus")
	public boolean bonus_damage = true;

	@Config.Comment("How much damage should you do when the enemy is poisoned. Default Damage x this")
	@Name("02. Bonus Damage amount")
	@LangKey(PREFIX + ".damagebonus.amount")
	public float bonus_damage_amount = 2f;

	@Config.Comment("Can the Poison Stone Poison Enemies?")
	@Name("03. Can Poison")
	@LangKey(PREFIX + ".poison")
	public boolean poison = true;

	@Config.Comment("1 in X chance to Poison an Enemy on Attack for 2 seconds, Settings this value to 0 always poisons")
	@Name("04. Poison Chance")
	@LangKey(PREFIX + ".poison.chance")
	public int poison_chance = 5;

	@Config.Comment("")
	@Name("05. Poison Duration")
	@LangKey(PREFIX + ".poison.duration")
	public int poison_duration = 40;

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
