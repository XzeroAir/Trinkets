package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

public class DamageShield {

	@Name("01. ignore damage System")
	public boolean damage_ignore = true;

	@Config.Comment("How many hits required before you ignore the next hit. Hits only count if the damage is at least 1 whole heart")
	@Name("02. Requred Hits")
	public int hits = 3;

	@Config.Comment("If First Aid is Installed. This ONLY Triggers if the Next headshot was supposed to kill you")
	@Name("03. Chance to Ignore Headshots")
	public boolean chance_ignore = true;

	@Config.Comment("If First Aid is Installed. 1 in How many Chance to Trigger Ignore Headshot")
	@Name("04. Headshots Ignore Chance")
	public int chance_headshots = 100;

	@Config.Comment("Do you Take less damage from Explosions. IE Creeper Explosions")
	@Name("05. Explosion Resistance")
	public boolean explosion_resist = true;

	@Config.Comment("1 Means you Take Full Damage, 0.25 Means you take 1/4 damage")
	@Name("06. Explosion Reistance Amount")
	@Config.RangeDouble(min = 0, max = 1f)
	public float explosion_amount = 0.25f;

	@Config.Comment("Only Disable this if You're the Epic Pro Gamer")
	@Name("07. Epic Pro Gamer")
	public boolean special = true;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "body";

}
