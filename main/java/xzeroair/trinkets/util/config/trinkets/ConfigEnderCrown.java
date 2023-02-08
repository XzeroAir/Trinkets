package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

//@formatter:off
public class ConfigEnderCrown {

	private final String name = ModItems.EnderTiara;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("If while wearing this should the player take damage while wet")
	@Name("01. Water Hurts")
	@LangKey(PREFIX + ".waterhurts")
	public boolean water_hurts = false;

	@Config.Comment("If while wearing this you should have a chance of Ignoring Damage")
	@Name("02. Chance to Ignore Damage")
	@LangKey(PREFIX + ".ignoredamage")
	public boolean dmgChance = true;

	@Config.Comment("If while wearing this you should have a chance of Summoning an Enderman to protect you")
	@Name("03. Chance for Enderman")
	@LangKey(PREFIX + ".ignoredamage.spawn")
	public boolean spawnChance = true;

	@Config.Comment("1 in 'num' chance to ignore damage and summon an Enderman to protect you")
	@Name("04. Chance")
	@LangKey(PREFIX + ".ignoredamage.spawn.chance")
	public int chance = 50;

	@Config.Comment("Nearby Endermen Follow you if enabled")
	@Name("05. Endermen Follow")
	@LangKey(PREFIX + ".endermen.follow")
	public boolean Follow = true;

	@Config.Comment("When Attacking an Enderman should it fight back?")
	@Name("06. Enderman Retaliate")
	@LangKey(PREFIX + ".endermen.retaliate")
	public boolean attackBack = false;

	@Config.Comment("When Killing an Enderman Should it drop Experience?")
	@Name("07. Enderman drop exp")
	@LangKey(PREFIX + ".endermen.exp")
	public boolean expDrop = false;

	@Config.Comment("When Killing an Enderman Should it drop Items?")
	@Name("08. Enderman drop Items")
	@LangKey(PREFIX + ".endermen.items")
	public boolean itemDrop = false;

	@Config.Comment("Allow Enderman to teleport")
	@Name("09. teleport")
	@LangKey(PREFIX + ".endermen.teleport")
	public boolean teleport = false;

	@Config.Comment("Teleport when damaged by indirect attacks")
	@Name("10. teleport on hurt")
	@LangKey(PREFIX + ".ignoredamage.teleport")
	public boolean teleportOnHurt= true;

	@Config.Comment("1 in 'num' chance to teleport when taking indirect damage")
	@Name("11. Teleport Chance")
	@LangKey(PREFIX + ".ignoredamage.teleport.chance")
	public int teleportChance = 1;

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
		public TANCompat tan = new TANCompat();
		public class TANCompat {

			@Config.Comment("If Tough as Nails is Installed, Should the player be immune to Cold")
			@Name("00. Immune to Cold")
			@LangKey(PREFIX + ".toughasnails.immunity.cold")
			public boolean immuneToCold = true;

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
		public BaubleCompat baubles = new BaubleCompat("head");
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
