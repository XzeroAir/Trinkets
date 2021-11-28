package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;

public class ConfigTeddyBear {

	private final String name = ModItems.TeddyBear;
	private final String PREFIX = Reference.MODID + ".config." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("give buffs when sleeping")
	@Name("01. Sleep Bonus")
	@LangKey(PREFIX + ".sleepbonus")
	public boolean sleep_bonus = true;

	@Config.Comment("Which Buffs to give")
	@Name("02. Buffs")
	@LangKey(PREFIX + ".sleepbonus.buff")
	public String[] buffs = new String[] {
			"minecraft:regeneration:300:0",
			"minecraft:luck:600:0",
			"minecraft:health_boost:3600:1"
	};

	@Config.Comment("If this value is greater then 0, instead of giving every buff in the list, it gives x random buffs")
	@Name("03. Random Buffs")
	@LangKey(PREFIX + ".sleepbonus.buff.random")
	public int randomBuff = 0;

	@Config.Comment("Which effects to prevent")
	@Name("10. Immunities")
	@LangKey(PREFIX + ".immunities")
	public String[] immunities = new String[] {
			"lycanitesmobs:fear",
			"lycanitesmobs:insomnia"
	};

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

		public LycanitesCompat lycanites = new LycanitesCompat();

		public class LycanitesCompat {

			public boolean preventFear = true;

		}

		public ParasitesCompat parasites = new ParasitesCompat();

		public class ParasitesCompat {

			public boolean preventFear = true;

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

	private final boolean armor = false;
	private final double armorAmount = 0;
	private final int armorOperation = 0;
	private final boolean attackSpeed = false;
	private final double attackSpeedAmount = 0;
	private final int attackSpeedOperation = 0;
	private final boolean damage = false;
	private final double damageAmount = 0;
	private final int damageOperation = 0;
	private final boolean health = false;
	private final double healthAmount = 0;
	private final int healthOperation = 0;
	private final boolean knockback = false;
	private final double knockbackAmount = 0;
	private final int knockbackOperation = 0;
	private final boolean speed = false;
	private final double speedAmount = 0;
	private final int speedOperation = 0;
	private final boolean swimSpeed = false;
	private final double swimSpeedAmount = 0;
	private final int swimSpeedOperation = 0;
	private final boolean toughness = false;
	private final double toughnessAmount = 0;
	private final int toughnessOperation = 0;
	private final boolean luck = false;
	private final double luckAmount = 0;
	private final int luckOperation = 0;
	private final boolean reach = false;
	private final double reachAmount = 0;
	private final int reachOperation = 0;
	private final boolean jump = false;
	private final double jumpAmount = 0;
	private final int jumpOperation = 0;
	private final boolean stepHeight = false;
	private final double stepHeightAmount = 0;
	private final int stepHeightOperation = 0;

	@Config.Comment({ "For Mor Information on Attributes", "https://minecraft.gamepedia.com/Attribute" })
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public ConfigAttribs Attributes = new ConfigAttribs(
			armor, armorAmount, armorOperation,
			attackSpeed, attackSpeedAmount, attackSpeedOperation,
			damage, damageAmount, damageOperation,
			health, healthAmount, healthOperation,
			knockback, knockbackAmount, knockbackOperation,
			speed, speedAmount, speedOperation,
			swimSpeed, swimSpeedAmount, swimSpeedOperation,
			toughness, toughnessAmount, toughnessOperation,
			luck, luckAmount, luckOperation,
			reach, reachAmount, reachOperation,
			jump, jumpAmount, jumpOperation,
			stepHeight, stepHeightAmount, stepHeightOperation
	);

}
