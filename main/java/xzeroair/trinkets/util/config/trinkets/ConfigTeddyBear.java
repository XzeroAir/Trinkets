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

	protected final boolean armor = false;
	protected final double armorAmount = 0;
	protected final int armorOperation = 0;
	protected final boolean attackSpeed = false;
	protected final double attackSpeedAmount = 0;
	protected final int attackSpeedOperation = 0;
	protected final boolean damage = false;
	protected final double damageAmount = 0;
	protected final int damageOperation = 0;
	protected final boolean health = false;
	protected final double healthAmount = 0;
	protected final int healthOperation = 0;
	protected final boolean knockback = false;
	protected final double knockbackAmount = 0;
	protected final int knockbackOperation = 0;
	protected final boolean speed = false;
	protected final double speedAmount = 0;
	protected final int speedOperation = 0;
	protected final boolean swimSpeed = false;
	protected final double swimSpeedAmount = 0;
	protected final int swimSpeedOperation = 0;
	protected final boolean toughness = false;
	protected final double toughnessAmount = 0;
	protected final int toughnessOperation = 0;
	protected final boolean luck = false;
	protected final double luckAmount = 0;
	protected final int luckOperation = 0;
	protected final boolean reach = false;
	protected final double reachAmount = 0;
	protected final int reachOperation = 0;
	protected final boolean jump = false;
	protected final double jumpAmount = 0;
	protected final int jumpOperation = 0;
	protected final boolean stepHeight = false;
	protected final double stepHeightAmount = 0;
	protected final int stepHeightOperation = 0;

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
