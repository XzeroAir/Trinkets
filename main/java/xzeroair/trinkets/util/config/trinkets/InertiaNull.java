package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

public class InertiaNull {

	@Config.Comment("Armor Modifications. Set to true to Enable. Default False")
	@Name("01. Armor")
	public boolean armor = false;

	@Config.Comment("Armor Modification Amount. Negative Values mean you have Less Armor")
	@Name("02. Armor Amount")
	@Config.RangeDouble(min = -20D, max = 40D)
	public double armor_amount = 0D;

	@Config.Comment("Toughness Modifications. Set to true to Enable. Default False")
	@Name("03. Toughness")
	public boolean toughness = false;

	@Config.Comment("Toughness Modification Amount. Negative Values mean you have Less Armor Toughness")
	@Name("04. Toughness Amount")
	@Config.RangeDouble(min = -40D, max = 40D)
	public double toughness_amount = 0D;

	@Config.Comment("Knockback Modifications. Set to true to Enable. Default False")
	@Name("05. Knockback")
	public boolean knockback = true;

	@Config.Comment("Knockback Modification Amount. Negative Values mean you have Less Knockback Resistance")
	@Name("06. Knockback Amount")
	@Config.RangeDouble(min = -1D, max = 1D)
	public double knockback_amount = 1D;

	@Config.Comment("Should you Take Fall Damage?. Set to true to Enable. Default False")
	@Name("07. Fall Damage")
	public boolean fall_damage = true;

	@Name("08. Fall Damage Amount")
	@Config.RangeDouble(min = 0, max = 10)
	public float falldamage_amount = 0f;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "trinket";

}
