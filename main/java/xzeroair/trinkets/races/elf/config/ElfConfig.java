package xzeroair.trinkets.races.elf.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

public class ElfConfig {
	private final String name = "elf";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Config.Comment("")
	@Name("01. Charge Shot")
	@LangKey(PREFIX + ".chargeshot.enabled")
	public boolean charge_shot = true;

	@Config.Comment("how much Mana should this ability initially cost")
	@Name("02. Charge Shot Cost")
	@LangKey(PREFIX + ".chargeshot.cost")
	public float CS_Cost = 20f;

	@Config.Comment("If a fully charged shot should explode on impact causing splash damage")
	@Name("03. Full Charge Explodes")
	@LangKey(PREFIX + ".chargeshot.explode")
	public boolean explode = true;

	@Config.Comment("")
	@Name("04. Max Charge Time")
	@LangKey(PREFIX + ".chargeshot.max.time")
	@RangeInt(min = 12, max = 72000)
	public int ChargeTime = 120;

	// THis isn't setup to do anything, it's also not going to be possible to do this, I need a better way to do the bonus damage
	@Config.Comment("")
	@Name("05. Damage Base Multiplier")
	@LangKey(PREFIX + ".chargeshot.base.multiplier")
	public float baseMultiplier = 1F;

	@Config.Comment("")
	@Name("06. Minimum damage Multiplier")
	@LangKey(PREFIX + ".chargeshot.minimum.damage.multiplier")
	public float minimumDamageMultiplier = 10F;

	@Config.Comment("Used for when a mod bow is not working properly with Charged Shot")
	@Name("7. Bow Blacklist")
	@LangKey(PREFIX + ".chargeshot.bow.blacklist")
	public String[] bowBlacklist = new String[] {
			"*:*crossbow*"
	};

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
		private final double 	attackSpeedAmount = 0.3;
		private final int		attackSpeedOperation = 1;
		private final boolean 	damage = true;
		private final double 	damageAmount = -0.25;
		private final int		damageOperation = 1;
		private final boolean 	health = false;
		private final double 	healthAmount = 0;
		private final int		healthOperation = 0;
		private final boolean 	knockback = false;
		private final double 	knockbackAmount = 0;
		private final int		knockbackOperation = 0;
		private final boolean 	speed = true;
		private final double 	speedAmount = 0.1;
		private final int		speedOperation = 1;
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
				jump, 			jumpAmount, 		jumpOperation,
				stepHeight,		stepHeightAmount, 	stepHeightOperation
				);
}
