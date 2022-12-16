<<<<<<< Updated upstream
package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

//@formatter:off
public class ConfigPoisonStone {

	private final String name = ModItems.Poison;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("If an Enemy is Poisoned, Should you be able to do Extra damage")
	@Name("01. Bonus Damage")
	@LangKey(PREFIX + ".damagebonus")
	public boolean bonus_damage = true;

	@Config.Comment("How much damage should you do when the enemy is poisoned. Default Damage x this")
	@Name("02. Bonus Damage amount")
	@LangKey(PREFIX + ".damagebonus.amount")
	public float bonus_damage_amount = 2f;

	@Config.Comment("Can the Poison Stone Poison Enemies?")
	@Name("03. Can Poison")
	@LangKey(PREFIX + ".poison")
	public boolean poison = true;

	@Config.Comment("1 in X chance to Poison an Enemy on Attack for 2 seconds, Settings this value to 0 always poisons")
	@Name("04. Poison Chance")
	@LangKey(PREFIX + ".poison.chance")
	public int poison_chance = 5;

	@Config.Comment("How Long should the poison effect last in ticks")
	@Name("05. Poison Duration")
	@LangKey(PREFIX + ".poison.duration")
	public int poison_duration = 40;

	@Config.Comment("Which effects to prevent")
	@Name("81. Immunities")
	@LangKey("xat.config.immunities.potions")
	public String[] immunities = new String[] {
			"minecraft:poison",
			"minecraft:hunger",
			"lycanitesmobs:plague"
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

}
=======
package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

//@formatter:off
public class ConfigPoisonStone {

	private final String name = ModItems.Poison;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Comment("If an Enemy is Poisoned, Should you be able to do Extra damage")
	@Name("01. Bonus Damage")
	@LangKey(PREFIX + ".damagebonus")
	public boolean bonus_damage = true;

	@Config.Comment("How much damage should you do when the enemy is poisoned. Default Damage x this")
	@Name("02. Bonus Damage amount")
	@LangKey(PREFIX + ".damagebonus.amount")
	public float bonus_damage_amount = 2f;

	@Config.Comment("Can the Poison Stone Poison Enemies?")
	@Name("03. Can Poison")
	@LangKey(PREFIX + ".poison")
	public boolean poison = true;

	@Config.Comment("1 in X chance to Poison an Enemy on Attack for 2 seconds, Settings this value to 0 always poisons")
	@Name("04. Poison Chance")
	@LangKey(PREFIX + ".poison.chance")
	public int poison_chance = 5;

	@Config.Comment("How Long should the poison effect last in ticks")
	@Name("05. Poison Duration")
	@LangKey(PREFIX + ".poison.duration")
	public int poison_duration = 40;

	@Config.Comment("Which effects to prevent")
	@Name("81. Immunities")
	@LangKey("xat.config.immunities.potions")
	public String[] immunities = new String[] {
			"minecraft:poison",
			"minecraft:hunger",
			"lycanitesmobs:plague"
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

}
>>>>>>> Stashed changes
