package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

public class GreaterInertia {

	@Config.Comment("Damage Modifications. Set to False to Disable. Default True")
	@Name("01. Damage")
	public boolean damage = false;

	@Config.Comment("Damage Modification Amount. Negative Values mean you do Less Damage")
	@Name("02. Damage Amount")
	@Config.RangeDouble(min = -1D, max = 40D)
	public double damage_amount = 0D;

	@Config.Comment("Armor Modifications. Set to true to Enable. Default False")
	@Name("03. Armor")
	public boolean armor = false;

	@Config.Comment("Armor Modification Amount. Negative Values mean you have Less Armor")
	@Name("04. Armor Amount")
	@Config.RangeDouble(min = -20D, max = 40D)
	public double armor_amount = 0D;

	@Config.Comment("Toughness Modifications. Set to true to Enable. Default False")
	@Name("05. Toughness")
	public boolean toughness = false;

	@Config.Comment("Toughness Modification Amount. Negative Values mean you have Less Armor Toughness")
	@Name("06. Toughness Amount")
	@Config.RangeDouble(min = -40D, max = 40D)
	public double toughness_amount = 0D;

	@Config.Comment("Knockback Modifications. Set to true to Enable. Default False")
	@Name("07. Knockback")
	public boolean knockback = true;

	@Config.Comment("Knockback Modification Amount. Negative Values mean you have Less Knockback Resistance")
	@Name("08. Knockback Amount")
	@Config.RangeDouble(min = -1D, max = 1D)
	public double knockback_amount = 0.5D;

	@Config.Comment("Speed Modifications. Set to true to Enable. Default False")
	@Name("09. Speed")
	public boolean speed = true;

	@Config.Comment("Speed Modification Amount. Negative Values mean you move slower")
	@Name("10. Speed Amount")
	@Config.RangeDouble(min = -0.1D, max = 10D)
	public double speed_amount = 0.3D;

	@Config.Comment("Jump Modifications. Set to true to Enable. Default False")
	@Name("11. Jump Height")
	public boolean jump = true;

	@Config.Comment("Jump Height Modification Amount.")
	@Name("12. Jump Height Amount")
	@Config.RangeDouble(min = 1, max = 10)
	public float jumpheight = 2f;

	@Config.Comment("Should you Take Fall Damage?. Set to true to Enable. Default False")
	@Name("13. Fall Damage")
	public boolean fall_damage = true;

	@Name("14. Fall Damage Amount")
	@Config.RangeDouble(min = 0, max = 10)
	public float falldamage_amount = 0.25f;

	@Config.RequiresMcRestart
	@Config.Comment("Should this Item Be Registered")
	@Name("98. Item Enabled")
	public boolean enabled = true;

	@Config.Comment("If the mod Baubles is installed what bauble slot should it use")
	@Name("99. Bauble Type")
	public String bauble_type = "trinket";

}
