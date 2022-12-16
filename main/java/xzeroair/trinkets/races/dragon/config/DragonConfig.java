package xzeroair.trinkets.races.dragon.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;

public class DragonConfig {
	private final String name = "dragon";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Config.RequiresWorldRestart
	@Config.Comment("Creative Flight. Set to False to Disable. Default True")
	@Name("00. Creative Flight")
	@LangKey(PREFIX + ".flight")
	public boolean creative_flight = true;

	@Config.Comment("How much damage per second the dragon breath does")
	@Name("01. Dragon Breath Damage")
	@LangKey(PREFIX + ".breath.damage")
	public float breath_damage = 1F;

	@Config.Comment("The Mana Cost per tick when using dragons breath")
	@Name("02. Dragon Breath Cost")
	@LangKey(PREFIX + ".breath.cost")
	public float breath_cost = 10F;

	@Config.RequiresWorldRestart
	@Config.Comment("Change the flight speed from the Vanilla Default of 0.05")
	@Name("03. Change Flight Speed")
	@LangKey(PREFIX + ".flight.speed")
	public boolean creative_flight_speed = true;

	@Config.RequiresWorldRestart
	@Config.Comment("How Fast the player moves when in Creative Flight. Vanilla Default 0.05. Default 0.02")
	@Name("04. Creative Flight Speed")
	@Config.RangeDouble(min = 0.01, max = 1)
	@LangKey(PREFIX + ".flight.speed.amount")
	public double flight_speed = 0.02;

	@Config.Comment("Mana Cost per second while flying")
	@Name("05. Flight Cost")
	@LangKey(PREFIX + ".flight.cost")
	public float flight_cost = 5F;

	@Name("Compatability Settings")
	@LangKey(Reference.MODID + ".config.compatability")
	public Compatability compat = new Compatability();

	public class Compatability {

		@Name("Tough as Nails Compatability")
		@LangKey(Reference.MODID + ".config.toughasnails")
		public TANCompat tan = new TANCompat();

		public class TANCompat {
			@Config.Comment("If Tough as Nails is Installed, Should the player be immune to Heat")
			@Name("00. Immune to Heat")
			@LangKey(Reference.MODID + ".config." + ModItems.DragonsEye + ".toughasnails.immunity.heat")
			public boolean immuneToHeat = true;
		}

	}

	@Config.Comment({ "For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute" })
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:0.25, Operation:1",
			"Name:generic.knockbackResistance; Amount:0; Operation:0",
			"Name:generic.movementSpeed, Amount:0, Operation:0",
			"Name:generic.attackDamage, Amount:0.5, Operation:1",
			"Name:generic.attackSpeed, Amount:0, Operation:0",
			"Name:generic.armor, Amount:0, Operation:0",
			"Name:generic.armorToughness, Amount:0.5, Operation:1",
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
