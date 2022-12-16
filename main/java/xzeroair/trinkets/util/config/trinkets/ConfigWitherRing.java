<<<<<<< Updated upstream
package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

//@formatter:off
public class ConfigWitherRing {

	private final String name = "wither_ring";
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("If an Enemy is has Wither, Should you be able to leech health")
	@Name("01. Health Leech")
	@LangKey(PREFIX + ".leech")
	public boolean leech = true;

	@Config.Comment("How much damage should you leech per hit in half hearts")
	@Name("02. Health Leech amount")
	@LangKey(PREFIX + ".leech.amount")
	public float leech_amount = 2f;

	@Name("03. Can Wither")
	@LangKey(PREFIX + ".wither")
	public boolean wither = true;

	@Config.Comment("1 in X chance to Wither an Enemy on Attack for 2 seconds")
	@Name("04. Wither Chance")
	@LangKey(PREFIX + ".wither.chance")
	public int wither_chance = 5;

	@Config.Comment("")
	@Name("05. Wither Duration")
	@LangKey(PREFIX + ".wither.duration")
	public int wither_duration = 40;

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
		public BaubleCompat baubles = new BaubleCompat("ring");
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
=======
package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

//@formatter:off
public class ConfigWitherRing {

	private final String name = "wither_ring";
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("If an Enemy is has Wither, Should you be able to leech health")
	@Name("01. Health Leech")
	@LangKey(PREFIX + ".leech")
	public boolean leech = true;

	@Config.Comment("How much damage should you leech per hit in half hearts")
	@Name("02. Health Leech amount")
	@LangKey(PREFIX + ".leech.amount")
	public float leech_amount = 2f;

	@Name("03. Can Wither")
	@LangKey(PREFIX + ".wither")
	public boolean wither = true;

	@Config.Comment("1 in X chance to Wither an Enemy on Attack for 2 seconds")
	@Name("04. Wither Chance")
	@LangKey(PREFIX + ".wither.chance")
	public int wither_chance = 5;

	@Config.Comment("")
	@Name("05. Wither Duration")
	@LangKey(PREFIX + ".wither.duration")
	public int wither_duration = 40;

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
		public BaubleCompat baubles = new BaubleCompat("ring");
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
>>>>>>> Stashed changes
