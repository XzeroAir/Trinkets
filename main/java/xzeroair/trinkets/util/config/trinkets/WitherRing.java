package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

public class WitherRing {

	@Config.Comment("If an Enemy is has Wither, Should you be able to leech health")
	@Name("01. Health Leech")
	public boolean leech = true;

	@Config.Comment("How much damage should you leech per hit in half hearts")
	@Name("02. Health Leech amount")
	public float leech_amount = 2f;

	@Name("03. Can Wither")
	public boolean wither = true;

	@Config.Comment("1 in X chance to Wither an Enemy on Attack for 2 seconds")
	@Name("04. Wither Chance")
	public int wither_chance = 5;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "ring";

}
