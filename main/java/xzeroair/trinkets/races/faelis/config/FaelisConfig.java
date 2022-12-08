package xzeroair.trinkets.races.faelis.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class FaelisConfig {
	private final String name = "faelis";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Name("01. Claw Bonus")
	@LangKey(PREFIX + ".claw.bonus")
	public boolean claw_bonus = true;
	@Name("02. Bonus amount")
	@LangKey(PREFIX + ".claw.bonus.amount")
	public double bonus = 3.25;
	@Config.Comment("")
	@Name("03. Armor Penalties")
	@LangKey(PREFIX + ".armor.penalties")
	public boolean penalties = true;
	@Config.Comment("The Amount of Weight per armor piece equipped")
	@Name("04. Penaltiy amount")
	@LangKey(PREFIX + ".armor.penalties.amount")
	public double penalty_amount = 0.0250;

	@Config.Comment("Should Faelis get a buff when drinking milk")
	@Name("05. Milk Buff")
	@LangKey(PREFIX + ".milk.buff")
	public boolean Invigorated = true;

	@Config.Comment("How long the buff should last")
	@Name("06. Buff Duration")
	@LangKey(PREFIX + ".milk.buff.duration")
	public int Invigorated_Duration = 600;

	@Config.Comment("Should the Faelis be Immune to the armor weight penalty while the Buff is Active")
	@Name("07. Buff Immunity")
	@LangKey(PREFIX + ".milk.buff.immunity")
	public boolean InvigoratedPenalty = true;

	@Config.Comment("List of Items that count as Milk")
	@Name("08. Milk")
	@LangKey(PREFIX + ".milk.list")
	public String[] milk = new String[] {
			"minecraft:milk_bucket"
	};
	@Config.Comment("List of Items that count as Heavy Armor")
	@Name("09. Heavy Armor")
	@LangKey(PREFIX + ".armor.heavy.list")
	public String[] heavyArmor = new String[] {
			"minecraft:chainmail_helmet;*;0.01",
			"minecraft:chainmail_chestplate;*;0.09",
			"minecraft:chainmail_leggings;*;0.075",
			"minecraft:chainmail_boots;*;0.025",
			"materialIron;head;0.025",
			"materialIron;chest;0.15",
			"materialIron;legs;0.075",
			"materialIron;feet;0.05",
			"minecraft:golden_helmet;*;0.04",
			"minecraft:golden_chestplate;*;0.2",
			"minecraft:golden_leggings;*;0.1",
			"minecraft:golden_boots;*;0.06",
			"materialDiamond;0.075",
	};

	@Config.Comment("Buffs when drinking milk")
	@Name("10. Milk Buffs")
	@LangKey(PREFIX + ".milk.buff.list")
	public String[] buffs = new String[] {
			"minecraft:speed:3600:0",
			"minecraft:strength:3600:0",
			"minecraft:jump_boost:3600:0"
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

	@Config.Comment({ "For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute" })
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:-0.25, Operation:1",
			"Name:generic.knockbackResistance; Amount:0; Operation:0",
			"Name:generic.movementSpeed, Amount:0.15, Operation:1",
			"Name:generic.attackDamage, Amount:-0.25, Operation:1",
			"Name:generic.attackSpeed, Amount:0.15, Operation:2",
			"Name:generic.armor, Amount:0, Operation:0",
			"Name:generic.armorToughness, Amount:-0.15, Operation:2",
			"Name:generic.luck, Amount:1, Operation:0",
			"Name:generic.reachDistance, Amount:-0.1, Operation:1",
			"Name:forge.swimSpeed, Amount:0.3, Operation:1",
			"Name:xat.entityMagic.regen, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
			"Name:xat.entityMagic.affinity, Amount:0, Operation:0",
			"Name:xat.jump, Amount:0.6, Operation:1",
			"Name:xat.stepheight, Amount:0.6, Operation:0"
	};
}
