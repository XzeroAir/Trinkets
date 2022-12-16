<<<<<<< Updated upstream
package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.trinkets.shared.BaubleCompat;

public class ConfigArcingOrb {

	private final String name = ModItems.ArcingOrb;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Name("Bolt Attack")
	@Config.LangKey(PREFIX + ".bold.enabled")
	public boolean attackAbility = true;

	@Config.Name("Bolt Attack Damage")
	@Config.LangKey(PREFIX + ".bolt.damage")
	public float attackDmg = 40F;

	@Config.Name("Bolt Attack Cost")
	@Config.LangKey(PREFIX + ".bolt.cost")
	public float attackCost = 300F;

	@Config.Name("Dodge")
	@Config.LangKey(PREFIX + ".dodge.enabled")
	public boolean dodgeAbility = true;

	@Config.Name("Dodge Cost")
	@Config.LangKey(PREFIX + ".dodge.cost")
	public float dodgeCost = 30F;

	@Config.Name("Dodge Stuns")
	@Config.LangKey(PREFIX + "dodge.stuns.enabled")
	public boolean dodgeStuns = true;

	@Config.Name("Stun Radius")
	@Config.LangKey(PREFIX + "dodge.stuns.radius")
	public double stunDistance = 2;

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
		public BaubleCompat baubles = new BaubleCompat("belt");
	}

	@Config.Comment({ "For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute" })
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:0, Operation:0",
			"Name:generic.knockbackResistance; Amount:0; Operation:0",
			"Name:generic.movementSpeed, Amount:0.25, Operation:1",
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

public class ConfigArcingOrb {

	private final String name = ModItems.ArcingOrb;
	private final String PREFIX = Reference.MODID + ".config.server." + name;
	private final String registry = Reference.MODID + ".config.registry";

	@Config.Name("Bolt Attack")
	@Config.LangKey(PREFIX + ".bold.enabled")
	public boolean attackAbility = true;

	@Config.Name("Bolt Attack Damage")
	@Config.LangKey(PREFIX + ".bolt.damage")
	public float attackDmg = 40F;

	@Config.Name("Bolt Attack Cost")
	@Config.LangKey(PREFIX + ".bolt.cost")
	public float attackCost = 300F;

	@Config.Name("Dodge")
	@Config.LangKey(PREFIX + ".dodge.enabled")
	public boolean dodgeAbility = true;

	@Config.Name("Dodge Cost")
	@Config.LangKey(PREFIX + ".dodge.cost")
	public float dodgeCost = 30F;

	@Config.Name("Dodge Stuns")
	@Config.LangKey(PREFIX + "dodge.stuns.enabled")
	public boolean dodgeStuns = true;

	@Config.Name("Stun Radius")
	@Config.LangKey(PREFIX + "dodge.stuns.radius")
	public double stunDistance = 2;

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
		public BaubleCompat baubles = new BaubleCompat("belt");
	}

	@Config.Comment({ "For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute" })
	@Name("Attributes")
	@LangKey(Reference.MODID + ".config.attributes")
	public String[] attributes = {
			"Name:generic.maxHealth, Amount:0, Operation:0",
			"Name:generic.knockbackResistance; Amount:0; Operation:0",
			"Name:generic.movementSpeed, Amount:0.25, Operation:1",
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
