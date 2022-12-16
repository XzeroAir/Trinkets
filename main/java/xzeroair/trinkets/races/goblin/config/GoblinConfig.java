package xzeroair.trinkets.races.goblin.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class GoblinConfig {

	private final String name = "goblin";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Config.Comment("")
	@Name("01. Resistances")
	@LangKey(PREFIX + ".natural_resistance")
	public boolean natural_resistance = true;
	@Config.Comment("")
	@Name("02. Goblin Rider")
	@LangKey(PREFIX + ".rider")
	public boolean rider = true;

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
			"Name:generic.maxHealth, Amount:-0.4, Operation:2",
			"Name:generic.knockbackResistance, Amount:0, Operation:0",
			"Name:generic.movementSpeed, Amount:0.2, Operation:1",
			"Name:generic.attackDamage, Amount:0.5, Operation:1",
			"Name:generic.attackSpeed, Amount:0, Operation:0",
			"Name:generic.armor, Amount:0, Operation:0",
			"Name:generic.armorToughness, Amount:0, Operation:0",
			"Name:generic.luck, Amount:1, Operation:0",
			"Name:generic.reachDistance, Amount:0, Operation:0",
			"Name:forge.swimSpeed, Amount:0.1, Operation:1",
			"Name:xat.entityMagic.regen, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
			"Name:xat.entityMagic.affinity, Amount:0, Operation:0",
			"Name:xat.jump, Amount:0, Operation:0",
			"Name:xat.stepheight, Amount:0, Operation:0"
	};
}
