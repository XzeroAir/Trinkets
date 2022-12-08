package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class ConfigTeddyBear {

	private final String name = ModItems.TeddyBear;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
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
	@LangKey("xat.config.immunities.potions")
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

	@Config.Comment({ "For Mor Information on Attributes", "https://minecraft.gamepedia.com/Attribute" })
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
