package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

//@formatter:off
public class ConfigDamageShield {

	private final String name = ModItems.DamageShield;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Name("01. ignore damage System")
	@LangKey(PREFIX + ".ignore.damage")
	public boolean damage_ignore = true;

	@Config.Comment("How many hits required before you ignore the next hit. Hits only count if the damage is at least 1 whole heart")
	@Name("02. Requred Hits")
	@LangKey(PREFIX + ".ignore.damage.hits")
	public int hits = 3;

	@Config.Comment("Do you Take less damage from Explosions. IE Creeper Explosions")
	@Name("03. Explosion Resistance")
	@LangKey(PREFIX + ".explosion.resist")
	public boolean explosion_resist = true;

	@Config.Comment("1 Means you Take Full Damage, 0.25 Means you take 1/4 damage")
	@Name("04. Explosion Reistance Amount")
	@Config.RangeDouble(min = 0, max = 1f)
	@LangKey(PREFIX + ".explosion.resist.amount")
	public float explosion_amount = 0.25f;

	@Config.Comment("The Resistance level given when worn")
	@Name("05. Resistance Level")
	@Config.RangeInt(min = 0)
	@LangKey(PREFIX + ".resistance.level")
	public int resistance_level = 0;

	@Config.Comment("Does Resistance stack with Resistance from other sources")
	@Name("06. Resistance stacks")
	@LangKey(PREFIX + ".resistance.stacks")
	public boolean resistance_stacks = true;

	@Config.Comment("Max level Resistance can stack")
	@Name("07. Max Stackable")
	@Config.RangeInt(min = 0)
	@LangKey(PREFIX + ".resistance.stacks.max")
	public int resistance_stacks_max = 3;

	@Config.Comment("Option to change what 'Resistance' is")
	@Name("08. Resistance potion")
	@LangKey(PREFIX + ".resistance")
	public String potionEffect = "minecraft:resistance";

	@Config.Comment("Only Disable this if You're the Epic Pro Gamer")
	@Name("69. Epic Pro Gamer")
	@LangKey(PREFIX + ".epic.gamer")
	public boolean special = true;

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

		@Name("First Aid Compatability")
		@LangKey(Reference.MODID + ".config.firstaid")
		public FirstAidCompat firstaid = new FirstAidCompat();
		public class FirstAidCompat {

			@Config.Comment("If First Aid is Installed. This ONLY Triggers if the Next headshot was supposed to kill you")
			@Name("01. Chance to Ignore Headshots")
			@LangKey(PREFIX + ".firstaid.ignore.headshots")
			public boolean chance_ignore = true;

			@Config.Comment("If First Aid is Installed. 1 in How many Chance to Trigger Ignore Headshot")
			@Name("02. Headshots Ignore Chance")
			@LangKey(PREFIX + ".firstaid.ignore.headshots.chance")
			public int chance_headshots = 100;

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
		public BaubleCompat baubles = new BaubleCompat("body");
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

}
