<<<<<<< Updated upstream
package xzeroair.trinkets.util.config.mana;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class EntityManaConfig {

	private final String PREFIX = Reference.MODID + ".config.magic";

	@Config.Comment("should certain Trinkets and Race abilities require Mana?")
	@Name("01. Mana Enabled")
	@LangKey(PREFIX + ".mana.enabled")
	public boolean mana_enabled = true;

	@Config.Comment("how often in ticks mana regens")
	@Name("02. Mana Regen Rate")
	@LangKey(PREFIX + ".mana.regen.ticks")
	public int mana_update_ticks = 20;

	@Config.Comment("after using an ability, how long before starting to regen mana again?")
	@Name("03. Mana Regen Timeout")
	@LangKey(PREFIX + ".mana.regen.timeout")
	public int mana_regen_timeout = 60;

	@Config.Comment("Should Max Mana Be Capped")
	@Name("04. Mana Cap")
	@LangKey(PREFIX + ".mana.cap")
	public boolean mana_cap = false;

	@Config.Comment("Total Max Mana before affinity bonus")
	@Name("05. Max Mana")
	@LangKey(PREFIX + ".mana.cap.max")
	public float mana_max = 1000f;

	@Config.Comment("How much Essence does the Moon Rose have")
	@Name("06. Moon Rose Essence")
	@LangKey(PREFIX + ".moon_rose.essence")
	public int essence_amount = 10;

	@Config.Comment("How long in ticks before the next essence can be absorbed")
	@Name("07. Moon Rose Essence Cooldown")
	@LangKey(PREFIX + ".moon_rose.cooldown")
	public int essence_cooldown = 300;

	@Config.Comment("Allow Max Mana cap to effect Magic Affinity")
	@Name("08. Cap Affinity")
	@LangKey(PREFIX + ".mana.cap.affinity")
	public boolean cap_affinity = false;

	@Config.Comment("Mana Crystals explode when smashed against stone producing a Mana Reagent")
	@Name("09. Mana Crystals Explode")
	@LangKey(PREFIX + ".mana_crystal.explodes")
	public boolean crystalExplodes = false;

	@Config.Comment("Do Mana Reagents Hurt when you consume them")
	@Name("10. Mana Reagent Harmful")
	@LangKey(PREFIX + ".mana_reagent.harmful")
	public boolean reagentHarmful = true;

	@Config.Comment("modid:item_name;meta;amount")
	@Name("97. MP Recovery Items")
	@LangKey(PREFIX + ".mana.recovery.list")
	public String[] recovery = new String[] {
			"xat:dwarf_stout;0;100%",
			"xat:elf_sap;0;100%",
			"xat:faelis_food;0;100%",
			"xat:fairy_dew;0;100%",
			"xat:goblin_soup;0;100%",
			"xat:titan_spirit;0;100%",
			"xat:dragon_gem;0;100%",
			"xat:mana_crystal;0;100%",
			"xat:mana_reagent;0;100%",
			"xat:mana_candy;0;50",
			"minecraft:golden_apple;0;20",
			"minecraft:golden_apple;1;50",
			"simpledifficulty:juice;0;10%",
			"simpledifficulty:juice;5;50%",
			"botania:manacookie;0;10%",
	};

}
=======
package xzeroair.trinkets.util.config.mana;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class EntityManaConfig {

	private final String PREFIX = Reference.MODID + ".config.magic";

	@Config.Comment("should certain Trinkets and Race abilities require Mana?")
	@Name("01. Mana Enabled")
	@LangKey(PREFIX + ".mana.enabled")
	public boolean mana_enabled = true;

	@Config.Comment("how often in ticks mana regens")
	@Name("02. Mana Regen Rate")
	@LangKey(PREFIX + ".mana.regen.ticks")
	public int mana_update_ticks = 20;

	@Config.Comment("after using an ability, how long before starting to regen mana again?")
	@Name("03. Mana Regen Timeout")
	@LangKey(PREFIX + ".mana.regen.timeout")
	public int mana_regen_timeout = 60;

	@Config.Comment("Should Max Mana Be Capped")
	@Name("04. Mana Cap")
	@LangKey(PREFIX + ".mana.cap")
	public boolean mana_cap = false;

	@Config.Comment("Total Max Mana before affinity bonus")
	@Name("05. Max Mana")
	@LangKey(PREFIX + ".mana.cap.max")
	public float mana_max = 1000f;

	@Config.Comment("How much Essence does the Moon Rose have")
	@Name("06. Moon Rose Essence")
	@LangKey(PREFIX + ".moon_rose.essence")
	public int essence_amount = 10;

	@Config.Comment("How long in ticks before the next essence can be absorbed")
	@Name("07. Moon Rose Essence Cooldown")
	@LangKey(PREFIX + ".moon_rose.cooldown")
	public int essence_cooldown = 300;

	@Config.Comment("Allow Max Mana cap to effect Magic Affinity")
	@Name("08. Cap Affinity")
	@LangKey(PREFIX + ".mana.cap.affinity")
	public boolean cap_affinity = false;

	@Config.Comment("Mana Crystals explode when smashed against stone producing a Mana Reagent")
	@Name("09. Mana Crystals Explode")
	@LangKey(PREFIX + ".mana_crystal.explodes")
	public boolean crystalExplodes = false;

	@Config.Comment("Do Mana Reagents Hurt when you consume them")
	@Name("10. Mana Reagent Harmful")
	@LangKey(PREFIX + ".mana_reagent.harmful")
	public boolean reagentHarmful = true;

	@Config.Comment("modid:item_name;meta;amount")
	@Name("97. MP Recovery Items")
	@LangKey(PREFIX + ".mana.recovery.list")
	public String[] recovery = new String[] {
			"xat:dwarf_stout;0;100%",
			"xat:elf_sap;0;100%",
			"xat:faelis_food;0;100%",
			"xat:fairy_dew;0;100%",
			"xat:goblin_soup;0;100%",
			"xat:titan_spirit;0;100%",
			"xat:dragon_gem;0;100%",
			"xat:mana_crystal;0;100%",
			"xat:mana_reagent;0;100%",
			"xat:mana_candy;0;50",
			"minecraft:golden_apple;0;20",
			"minecraft:golden_apple;1;50",
			"simpledifficulty:juice;0;10%",
			"simpledifficulty:juice;5;50%",
			"botania:manacookie;0;10%",
	};

}
>>>>>>> Stashed changes
