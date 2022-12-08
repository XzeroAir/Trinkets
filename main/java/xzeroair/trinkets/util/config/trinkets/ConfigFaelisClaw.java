package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class ConfigFaelisClaw {

	private final String name = ModItems.FaelisClaws;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Name("01. Bleeding")
	@LangKey(PREFIX + ".bleeding")
	public boolean bleed = true;

	@Name("02. Bleed Duration")
	@LangKey(PREFIX + ".bleeding.duration")
	public int bleedDuration = 300;

	@Name("03. Bleed chance")
	@LangKey(PREFIX + ".bleeding.chance")
	public int chance = 4;

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

			public boolean useLycaniteBleed = true;

		}

		public DefiledLandsCompat defiledlands = new DefiledLandsCompat();

		public class DefiledLandsCompat {

			public boolean useDefiledLandsBleed = true;
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
		public BaubleCompat baubles = new BaubleCompat("Trinket");
	}

	//@formatter:off

	@Config.Comment({"For Mor Information on Attributes", "https://minecraft.gamepedia.com/Attribute"})
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:0, Operation:0",
			"Name:generic.knockbackResistance; Amount:0; Operation:0",
			"Name:generic.movementSpeed, Amount:0, Operation:0",
			"Name:generic.attackDamage, Amount:0.25, Operation:1",
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
