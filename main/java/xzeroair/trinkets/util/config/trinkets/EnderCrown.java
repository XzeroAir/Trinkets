package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

public class EnderCrown {

	@Config.RequiresWorldRestart
	@Name("01. Endermen Follow")
	public boolean Follow = true;

	@Config.Comment("If while wearing this you should have a chance of Ignoring Damage")
	@Name("02. Chance to Ignore Damage")
	public boolean dmgChance = true;

	@Config.Comment("If while wearing this you should have a chance of Summoning an Enderman to protect you")
	@Name("03. Chance for Enderman")
	public boolean spawnChance = true;

	@Config.Comment("1 in 'num' chance to ignore damage and summon an Enderman to protect you")
	@Name("04. Chance")
	public int chance = 50;

	@Config.Comment("If while wearing this should the player take damage while wet")
	@Name("05. Water Hurts")
	public boolean water_hurts = false;

	@Name("06. Enderman Retaliate")
	public boolean attackBack = false;

	@Name("07. Enderman drop exp")
	public boolean expDrop = false;

	@Name("08. Enderman drop Items")
	public boolean itemDrop = false;

	@Config.Comment("If Tough as Nails is Installed, Should the player be immune to Cold")
	@Name("09. Immune to Cold")
	public boolean immuneToCold = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "head";

}
