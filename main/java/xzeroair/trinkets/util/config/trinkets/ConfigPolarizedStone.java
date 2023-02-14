package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

//@formatter:off
public class ConfigPolarizedStone {

	private final String name = ModItems.Polarized;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("Should the Polarized Stone Instantly pickup Items, or Pull them toward you. Default True")
	@Name("01. Instant Pickup")
	@LangKey(PREFIX + ".collection.instant")
	public boolean instant_pickup = true;

	@Config.Comment("Should the Polarized Stone Effect Nearby Exp")
	@Name("02. Collect nearby exp")
	@LangKey(PREFIX + ".collection.xp")
	public boolean collectXP = true;

	@Config.Comment("Should the Polarized Stone Repel Incoming projectiles. Default True")
	@Name("03. Repel Projectiles")
	@LangKey(PREFIX + ".repel")
	public boolean repell = true;

	@Config.Comment("Should The Polarized Stone Cost Mana to Repel Projectiles. Default True")
	@Name("04. Repel costs Mana")
	@LangKey(PREFIX + ".repel.exhaust")
	public boolean exhaustion = true;

	@Config.Comment("Mana Cost Per tickrate.")
	@Name("05. Mana Cost")
	@RangeDouble(min = 0)
	@LangKey(PREFIX + ".repel.exhaust.amount")
	public float exhaust_rate = 10F;

	@Config.Comment("How often in Ticks Mana is reduced while Repel is Active")
	@Name("06. Mana Cost Rate")
	@RangeInt(min = 0)
	@LangKey(PREFIX + ".repel.exhaust.speed")
	public int exhaust_ticks = 20;

	@Config.Comment("If Instant Pickup is Disabled, Polarized Stone's Push and pull speed. Default 0.1, 0.1 MIN, 1.0 MAX")
	@Name("07. Polarized Stone Push and Pull Speed")
	@RangeDouble(min = 0.1, max = 1)
	@LangKey(PREFIX + ".collection.speed")
	public double Polarized_Stone_Speed = 0.1;

	@Name("09. Repelled Entities")
	@LangKey(PREFIX + ".repel.entity.whitelist")
	public String[] repelledEntities = new String[] {
			"minecraft:arrow",
			"minecraft:fireball"
	};

	@Config.Comment("The Distance from the player to repel entities")
	@Name("10. Polarized Repel Range")
	@RangeDouble(min = 1, max = 8)
	@LangKey(PREFIX + ".repel.range")
	public double repelRange = 2D;

	@Config.Comment("Should the Polarized Stone Instantly pickup XP, or Pull them toward you. Default True")
	@Name("11. Instant XP Pickup")
	@LangKey(PREFIX + ".collection.instant.xp")
	public boolean instant_xp = true;

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

	@Config.Comment({"For Mor Information on Attributes", "https://minecraft.gamepedia.com/Attribute"})
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:0, Operation:0",
			"Name:generic.knockbackResistance; Amount:0; Operation:0",
			"Name:generic.movementSpeed, Amount:0, Operation:0",
			"Name:generic.attackDamage, Amount:0, Operation:0",
			"Name:generic.attackSpeed, Amount:0, Operation:0",
			"Name:generic.armor, Amount:0, Operation:0",
			"Name:generic.armorToughness, Amount:0, Operation:0",
			"Name:generic.luck, Amount:0, Operation:0",
			"Name:forge.swimSpeed, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
			"Name:xat.entityMagic.affinity, Amount:0, Operation:0",
			"Name:xat.jump, Amount:0, Operation:0",
			"Name:xat.stepheight, Amount:0, Operation:0"
	};

	@Config.Comment("WARNING! SETTING THESE VALUES TOO HIGH WILL CAUSE YOU TO LAG. Try to Keep within a range of 4-16")
	@Name("Pickup Range")
	@LangKey(PREFIX + ".collection.range")
	public Pickup_Range PR = new Pickup_Range();
	public class Pickup_Range {
		@Config.Comment("How Far Vertically(Up, Down) in Blocks the Polarized Stone collects Items and XP. Default 6, MIN 0, MAX 32")
		@Name("Vertical Distance")
		@RangeInt(min = 0, max = 32)
		@LangKey(PREFIX + ".collection.range.vertical")
		public int VD = 6;

		@Config.Comment("How Far Horizontally(N, E, S, W) in Blocks the Polarized Stone collects Items and XP. Default 12, MIN 0, MAX 32")
		@Name("Horizontal Distance")
		@RangeInt(min = 0, max = 32)
		@LangKey(PREFIX + ".collection.range.horizontal")
		public int HD = 12;
	}
}
