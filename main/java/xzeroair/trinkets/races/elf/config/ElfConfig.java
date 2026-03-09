package xzeroair.trinkets.races.elf.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.damage.DamageTypesConfig;
import xzeroair.trinkets.util.config.race.RaceMagicConfig;
import xzeroair.trinkets.util.config.race.RaceSizeConfig;

//@formatter:off
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

	@Config.Comment("What potion effects is the player immune to")
	@Name("Potion Resistances")
	@LangKey(Reference.MODID + ".config.races" + ".resistances")
	public String[] resistances = {};

	@Config.Comment("What DamageTypes is the player immune to")
	@Name("Damage Immunity")
	@LangKey(Reference.MODID + ".config.races" + ".immunities")
	public DamageTypesConfig dmgType = new DamageTypesConfig();

	@Config.Comment("What effects to add to the player")
	@Name("Potion Effects")
	@LangKey(Reference.MODID + ".config.races" + ".effects")
	public String[] potEffects = {};

	@Config.Name("Magic")
	@Config.LangKey(Reference.MODID + ".config.magic")
	public final RaceMagicConfig magic = new RaceMagicConfig(200);

	@Name("Size")
	@LangKey(Reference.MODID + ".config.race.size")
	public final RaceSizeConfig size = new RaceSizeConfig(100, 100);

	@Config.Comment({ "For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute" })
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:0, Operation:0",
			"Name:generic.knockbackResistance; Amount:0; Operation:0",
			"Name:generic.movementSpeed, Amount:0.1, Operation:1",
			"Name:generic.attackDamage, Amount:0, Operation:0",
			"Name:generic.attackSpeed, Amount:0.3, Operation:1",
			"Name:generic.armor, Amount:0, Operation:0",
			"Name:generic.armorToughness, Amount:0, Operation:0",
			"Name:generic.luck, Amount:0, Operation:0",
			"Name:generic.reachDistance, Amount:0, Operation:0",
			"Name:forge.swimSpeed, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
			"Name:xat.entityMagic.affinity, Amount:0, Operation:0",
			"Name:xat.jump, Amount:0, Operation:0",
			"Name:xat.stepheight, Amount:0, Operation:0"
	};
}
