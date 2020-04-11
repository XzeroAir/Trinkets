package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.Attribs;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class EnderCrown {

	private final String name = "ender_tiara";
	private final String PREFIX = Reference.MODID + ".config." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("If while wearing this should the player take damage while wet")
	@Name("01. Water Hurts")
	@LangKey(PREFIX + ".waterhurts")
	public boolean water_hurts = false;

	@Config.Comment("If while wearing this you should have a chance of Ignoring Damage")
	@Name("02. Chance to Ignore Damage")
	@LangKey(PREFIX + ".ignoredamage")
	public boolean dmgChance = true;

	@Config.Comment("If while wearing this you should have a chance of Summoning an Enderman to protect you")
	@Name("03. Chance for Enderman")
	@LangKey(PREFIX + ".ignoredamage.spawn")
	public boolean spawnChance = true;

	@Config.Comment("1 in 'num' chance to ignore damage and summon an Enderman to protect you")
	@Name("04. Chance")
	@LangKey(PREFIX + ".ignoredamage.spawn.chance")
	public int chance = 50;

	@Config.RequiresWorldRestart
	@Name("05. Endermen Follow")
	@LangKey(PREFIX + ".endermen.follow")
	public boolean Follow = true;

	@Name("06. Enderman Retaliate")
	@LangKey(PREFIX + ".endermen.retaliate")
	public boolean attackBack = false;

	@Name("07. Enderman drop exp")
	@LangKey(PREFIX + ".endermen.exp")
	public boolean expDrop = false;

	@Name("08. Enderman drop Items")
	@LangKey(PREFIX + ".endermen.items")
	public boolean itemDrop = false;

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
		public TANCompat tan = new TANCompat();
		public class TANCompat {

			@Config.Comment("If Tough as Nails is Installed, Should the player be immune to Cold")
			@Name("00. Immune to Cold")
			@LangKey(PREFIX + ".toughasnails.immunity.cold")
			public boolean immuneToCold = true;

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
		public BaubleCompat baubles = new BaubleCompat("head");
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

}
