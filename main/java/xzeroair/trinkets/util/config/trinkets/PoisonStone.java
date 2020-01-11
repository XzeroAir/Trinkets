package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

public class PoisonStone {

	@Config.Comment("If an Enemy is Poisoned, Should you be able to do Extra damage")
	@Name("01. Bonus Damage")
	public boolean damage = true;

	@Config.Comment("How much damage should you do when the enemy is poisoned. Default Damage x this")
	@Name("02. Bonus Damage amount")
	public float damage_amount = 2f;

	@Name("03. Can Poison")
	public boolean poison = true;

	@Config.Comment("1 in X chance to Poison an Enemy on Attack for 2 seconds")
	@Name("04. Poison Chance")
	public int poison_chance = 5;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "trinket";

}
