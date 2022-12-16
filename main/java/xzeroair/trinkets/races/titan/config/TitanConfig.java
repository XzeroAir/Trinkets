<<<<<<< Updated upstream
package xzeroair.trinkets.races.titan.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class TitanConfig {

	private final String name = "titan";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Config.Comment("Jump Height Adjustment when wearing Race Rings. Set to False to Disable. Default True")
	@Name("01. Jump Height")
	@LangKey(PREFIX + ".jumpheight")
	public boolean step_height = true;

	@Config.Comment("If the player should be too heavy to stay afloat")
	@Name("02. Heavy Player")
	@LangKey(PREFIX + ".heavy")
	public boolean sink = true;

	@Config.Comment("If Enabled the player will trample farmland")
	@Name("03. Trample Farmland")
	@LangKey(PREFIX + ".trample")
	public boolean trample = true;

	@Config.Comment("If Enabled the player will be able to mine a 3x3 area")
	@Name("04. AoE Mining")
	@LangKey(PREFIX + ".mining.extended")
	public boolean miningExtend = true;

	@Config.Comment("If Enabled the player will be required to sneak to AoE Mine")
	@Name("05. Invert AoE Mining")
	@LangKey(PREFIX + ".mining.extended.inverted")
	public boolean miningExtendInverted = false;

	@Config.Comment("what blocks can not be AoE Mined")
	@Name("06. AoE Mining Blacklist")
	@LangKey(PREFIX + ".mining.extended.blacklist")
	public String[] miningAoEBlacklist = new String[] {
			"dynamictrees:*"
	};

	@Config.Comment("If the player can Mount Entities at all")
	@Name("07. Able to Mount Entities")
	@LangKey(PREFIX + ".mount.enabled")
	public boolean canMount = true;

	@Config.Comment("Should the Mount Blacklist to a Whitelist")
	@Name("08. Toggle Whitelist")
	@LangKey(PREFIX + ".mount.whitelist")
	public boolean whitelist = false;

	@Config.Comment("Entities that can not be mounted, or can only be mounted")
	@Name("09. Mount Blacklist")
	@LangKey(PREFIX + ".mount.blacklist")
	public String[] mountBlacklist = new String[] {
			"quark:seat",
			"sit:entity_sit",
			"minecraft:minecart"
	};

	@Name("Compatability Settings")
	@LangKey(Reference.MODID + ".config.compatability")
	private final Compatability compat = new Compatability();

	public class Compatability {

		@Name("Tough as Nails Compatability")
		@LangKey(Reference.MODID + ".config.toughasnails")
		private final TANCompat tan = new TANCompat();

		public class TANCompat {

		}

	}

	@Config.Comment({ "For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute" })
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:1, Operation:1",
			"Name:generic.knockbackResistance; Amount:1; Operation:0",
			"Name:generic.movementSpeed, Amount:0, Operation:0",
			"Name:generic.attackDamage, Amount:0.5, Operation:2",
			"Name:generic.attackSpeed, Amount:-0.5, Operation:2",
			"Name:generic.armor, Amount:0, Operation:0",
			"Name:generic.armorToughness, Amount:0, Operation:0",
			"Name:generic.luck, Amount:0, Operation:0",
			"Name:generic.reachDistance, Amount:1, Operation:1",
			"Name:forge.swimSpeed, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
			"Name:xat.entityMagic.affinity, Amount:0, Operation:0",
			"Name:xat.jump, Amount:0.75, Operation:1",
			"Name:xat.stepheight, Amount:1.4, Operation:0"
	};
}
=======
package xzeroair.trinkets.races.titan.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class TitanConfig {

	private final String name = "titan";
	private final String PREFIX = Reference.MODID + ".config.races." + name;

	@Config.Comment("Jump Height Adjustment when wearing Race Rings. Set to False to Disable. Default True")
	@Name("01. Jump Height")
	@LangKey(PREFIX + ".jumpheight")
	public boolean step_height = true;

	@Config.Comment("If the player should be too heavy to stay afloat")
	@Name("02. Heavy Player")
	@LangKey(PREFIX + ".heavy")
	public boolean sink = true;

	@Config.Comment("If Enabled the player will trample farmland")
	@Name("03. Trample Farmland")
	@LangKey(PREFIX + ".trample")
	public boolean trample = true;

	@Config.Comment("If Enabled the player will be able to mine a 3x3 area")
	@Name("04. AoE Mining")
	@LangKey(PREFIX + ".mining.extended")
	public boolean miningExtend = true;

	@Config.Comment("If Enabled the player will be required to sneak to AoE Mine")
	@Name("05. Invert AoE Mining")
	@LangKey(PREFIX + ".mining.extended.inverted")
	public boolean miningExtendInverted = false;

	@Config.Comment("what blocks can not be AoE Mined")
	@Name("06. AoE Mining Blacklist")
	@LangKey(PREFIX + ".mining.extended.blacklist")
	public String[] miningAoEBlacklist = new String[] {
			"dynamictrees:*"
	};

	@Config.Comment("If the player can Mount Entities at all")
	@Name("07. Able to Mount Entities")
	@LangKey(PREFIX + ".mount.enabled")
	public boolean canMount = true;

	@Config.Comment("Should the Mount Blacklist to a Whitelist")
	@Name("08. Toggle Whitelist")
	@LangKey(PREFIX + ".mount.whitelist")
	public boolean whitelist = false;

	@Config.Comment("Entities that can not be mounted, or can only be mounted")
	@Name("09. Mount Blacklist")
	@LangKey(PREFIX + ".mount.blacklist")
	public String[] mountBlacklist = new String[] {
			"quark:seat",
			"sit:entity_sit",
			"minecraft:minecart"
	};

	@Name("Compatability Settings")
	@LangKey(Reference.MODID + ".config.compatability")
	private final Compatability compat = new Compatability();

	public class Compatability {

		@Name("Tough as Nails Compatability")
		@LangKey(Reference.MODID + ".config.toughasnails")
		private final TANCompat tan = new TANCompat();

		public class TANCompat {

		}

	}

	@Config.Comment({ "For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute" })
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:1, Operation:1",
			"Name:generic.knockbackResistance; Amount:1; Operation:0",
			"Name:generic.movementSpeed, Amount:0, Operation:0",
			"Name:generic.attackDamage, Amount:0.5, Operation:2",
			"Name:generic.attackSpeed, Amount:-0.5, Operation:2",
			"Name:generic.armor, Amount:0, Operation:0",
			"Name:generic.armorToughness, Amount:0, Operation:0",
			"Name:generic.luck, Amount:0, Operation:0",
			"Name:generic.reachDistance, Amount:1, Operation:1",
			"Name:forge.swimSpeed, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen, Amount:0, Operation:0",
			"Name:xat.entityMagic.regen.cooldown, Amount:0, Operation:0",
			"Name:xat.entityMagic.affinity, Amount:0, Operation:0",
			"Name:xat.jump, Amount:0.75, Operation:1",
			"Name:xat.stepheight, Amount:1.4, Operation:0"
	};
}
>>>>>>> Stashed changes
