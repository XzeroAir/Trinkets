package xzeroair.trinkets.util.config.trinkets.shared;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.interfaces.IAttributeConfigHelper;

public class Attribs implements IAttributeConfigHelper {

	public Attribs(
			boolean armor, double armorAmount, int armorOperation,
			boolean attackSpeed, double AttackSpeedAmount, int AttackSpeedOperation,
			boolean damage, double damageAmount, int damageOperation,
			boolean health, double healthAmount, int healthOperation,
			boolean knockback, double knockbackAmount, int knockbackOperation,
			boolean speed, double speedAmount, int speedOperation,
			boolean swimSpeed, double swimSpeedAmount, int swimSpeedOperation,
			boolean toughness, double toughnessAmount, int toughnessOperation,
			boolean luck, double luckAmount, int luckOperation,
			boolean reach, double reachAmount, int reachOperation
			) {

		this.armor = armor;
		armor_amount = armorAmount;
		armor_operation = armorOperation;
		attackspeed = attackSpeed;
		attackspeed_amount = AttackSpeedAmount;
		attackspeed_operation = AttackSpeedOperation;
		this.damage = damage;
		damage_amount = damageAmount;
		damage_operation = damageOperation;
		this.health = health;
		health_amount = healthAmount;
		health_operation = healthOperation;
		this.knockback = knockback;
		knockback_amount = knockbackAmount;
		knockback_operation = knockbackOperation;
		this.speed = speed;
		speed_amount = speedAmount;
		speed_operation = speedOperation;
		swim_speed = swimSpeed;
		swim_speed_amount = swimSpeedAmount;
		swim_speed_operation = swimSpeedOperation;
		this.toughness = toughness;
		toughness_amount = toughnessAmount;
		toughness_operation = toughnessOperation;
		this.luck = luck;
		luck_amount = luckAmount;
		luck_operation = luckOperation;
		this.reach = reach;
		reach_amount = reachAmount;
		reach_operation = reachOperation;

	}

	private static final String PREFIX = Reference.MODID + ".config.attributes";
	private static final String armorKey = PREFIX + ".armor";
	private static final String armortoughnessKey = PREFIX + ".armortoughness";
	private static final String attackspeedKey = PREFIX + ".attackspeed";
	private static final String damageKey = PREFIX + ".damage";
	private static final String healthKey = PREFIX + ".health";
	private static final String knockbackKey = PREFIX + ".knockback";
	private static final String luckKey = PREFIX + ".luck";
	private static final String reachKey = PREFIX + ".reach";
	private static final String movementspeedKey = PREFIX + ".movementspeed";
	private static final String swimspeedKey = PREFIX + ".swimspeed";
	private static final String amountKey = ".amount";
	private static final String operationKey = ".operation";

	@Config.Comment("Enable Attribute Modifier")
	@Name("Armor")
	@LangKey(armorKey)
	public boolean armor = false;

	@Config.Comment("Modification Amount. Negative Values mean you have Less Armor")
	@Name("Armor Amount")
	@LangKey(armorKey + amountKey)
	public double armor_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Armor Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(armorKey + operationKey)
	public int armor_operation = 0;

	@Config.Comment("Enable Attribute Modifier")
	@Name("Attack Speed")
	@LangKey(attackspeedKey)
	public boolean attackspeed = false;

	@Config.Comment("Modification Amount. Negative Values mean you move slower")
	@Name("Attack Speed Amount")
	@LangKey(attackspeedKey + amountKey)
	public double attackspeed_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Attack Speed Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(attackspeedKey + operationKey)
	public int attackspeed_operation = 0;

	@Config.Comment("Enable Attribute Modifier")
	@Name("Damage")
	@LangKey(damageKey)
	public boolean damage = false;

	@Config.Comment("Modification Amount. Negative Values mean you do Less Damage")
	@Name("Damage Amount")
	@LangKey(damageKey + amountKey)
	public double damage_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Damage Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(damageKey + operationKey)
	public int damage_operation = 0;

	@Config.Comment("Enable Attribute Modifier")
	@Name("Health")
	@LangKey(healthKey)
	public boolean health = false;

	@Config.Comment("Modification Amount. Negative Values mean you have Less Health")
	@Name("Health Amount")
	@LangKey(healthKey + amountKey)
	public double health_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Health Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(healthKey + operationKey)
	public int health_operation = 0;

	@Config.Comment("Enable Attribute Modifier")
	@Name("Knockback Resistance")
	@LangKey(knockbackKey)
	public boolean knockback = false;

	@Config.Comment("Modification Amount.")
	@Name("Knockback Resistance Amount")
	@LangKey(knockbackKey + amountKey)
	public double knockback_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Knockback Resistance Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(knockbackKey + operationKey)
	public int knockback_operation = 0;

	@Config.Comment("Enable Attribute Modifier")
	@Name("Movement Speed")
	@LangKey(movementspeedKey)
	public boolean speed = false;

	@Config.Comment("Modification Amount. Negative Values mean you move slower")
	@Name("Movement Speed Amount")
	@LangKey(movementspeedKey + amountKey)
	public double speed_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Movement Speed Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(movementspeedKey + operationKey)
	public int speed_operation = 0;

	@Config.Comment("Enable Attribute Modifier")
	@Name("Swim Speed")
	@LangKey(swimspeedKey)
	public boolean swim_speed = false;

	@Config.Comment("Modification Amount. Negative Values mean you move slower in water")
	@Name("Swim Speed Amount")
	@LangKey(swimspeedKey + amountKey)
	public double swim_speed_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Swim Speed Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(swimspeedKey + operationKey)
	public int swim_speed_operation = 0;

	@Config.Comment("Enable Attribute Modifier")
	@Name("Armor Toughness")
	@LangKey(armortoughnessKey)
	public boolean toughness = false;

	@Config.Comment("Toughness Modification Amount When Transformed. Negative Values mean you have Less Armor Toughness")
	@Name("Armor Toughness Amount")
	@LangKey(armortoughnessKey + amountKey)
	public double toughness_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Armor Toughness Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(armortoughnessKey + operationKey)
	public int toughness_operation = 0;

	@Config.Comment("Enable Attribute Modifier")
	@Name("Luck")
	@LangKey(luckKey)
	public boolean luck = false;

	@Config.Comment("Toughness Modification Amount When Transformed. Negative Values mean you have Less Armor Toughness")
	@Name("Luck Amount")
	@LangKey(luckKey + amountKey)
	public double luck_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Luck Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(luckKey + operationKey)
	public int luck_operation = 0;

	@Config.Comment("Enable Attribute Modifier")
	@Name("Reach")
	@LangKey(reachKey)
	public boolean reach = false;

	@Config.Comment("Toughness Modification Amount When Transformed. Negative Values mean you have Less Armor Toughness")
	@Name("Reach Amount")
	@LangKey(reachKey + amountKey)
	public double reach_amount = 0D;

	@Config.Comment("Determines the Math used for the Attribute Modifier")
	@Name("Reach Operation")
	@Config.RangeInt(min = 0, max = 2)
	@LangKey(reachKey + operationKey)
	public int reach_operation = 0;

	@Override
	public boolean ArmorAttributeEnabled() {
		return armor;
	}

	@Override
	public int ArmorAttributeOperation() {
		return armor_operation;
	}

	@Override
	public double ArmorAttributeAmount() {
		return armor_amount;
	}

	@Override
	public boolean AttackSpeedAttributeEnabled() {
		return attackspeed;
	}

	@Override
	public int AttackSpeedAttributeOperation() {
		return attackspeed_operation;
	}

	@Override
	public double AttackSpeedAttributeAmount() {
		return attackspeed_amount;
	}

	@Override
	public boolean DamageAttributeEnabled() {
		return damage;
	}

	@Override
	public int DamageAttributeOperation() {
		return damage_operation;
	}

	@Override
	public double DamageAttributeAmount() {
		return damage_amount;
	}

	@Override
	public boolean HealthAttributeEnabled() {
		return health;
	}

	@Override
	public int HealthAttributeOperation() {
		return health_operation;
	}

	@Override
	public double HealthAttributeAmount() {
		return health_amount;
	}

	@Override
	public boolean KnockbackAttributeEnabled() {
		return knockback;
	}

	@Override
	public int KnockbackAttributeOperation() {
		return knockback_operation;
	}

	@Override
	public double KnockbackAttributeAmount() {
		return knockback_amount;
	}

	@Override
	public boolean MovementSpeedAttributeEnabled() {
		return speed;
	}

	@Override
	public int MovementSpeedAttributeOperation() {
		return speed_operation;
	}

	@Override
	public double MovementSpeedAttributeAmount() {
		return speed_amount;
	}

	@Override
	public boolean SwimSpeedAttributeEnabled() {
		return swim_speed;
	}

	@Override
	public int SwimSpeedAttributeOperation() {
		return swim_speed_operation;
	}

	@Override
	public double SwimSpeedAttributeAmount() {
		return swim_speed_amount;
	}

	@Override
	public boolean ArmorToughnessAttributeEnabled() {
		return toughness;
	}

	@Override
	public int ArmorToughnessAttributeOperation() {
		return toughness_operation;
	}

	@Override
	public double ArmorToughnessAttributeAmount() {
		return toughness_amount;
	}

	@Override
	public boolean LuckAttributeEnabled() {
		return luck;
	}

	@Override
	public int LuckAttributeOperation() {
		return luck_operation;
	}

	@Override
	public double LuckAttributeAmount() {
		return luck_amount;
	}

	@Override
	public boolean ReachAttributeEnabled() {
		return reach;
	}

	@Override
	public int ReachAttributeOperation() {
		return reach_operation;
	}

	@Override
	public double ReachAttributeAmount() {
		return reach_amount;
	}

}
