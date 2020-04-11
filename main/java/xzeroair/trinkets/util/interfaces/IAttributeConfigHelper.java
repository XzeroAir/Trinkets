package xzeroair.trinkets.util.interfaces;

public interface IAttributeConfigHelper {

	boolean ArmorAttributeEnabled();
	int ArmorAttributeOperation();
	double ArmorAttributeAmount();

	boolean AttackSpeedAttributeEnabled();
	int AttackSpeedAttributeOperation();
	double AttackSpeedAttributeAmount();

	boolean DamageAttributeEnabled();
	int DamageAttributeOperation();
	double DamageAttributeAmount();

	boolean HealthAttributeEnabled();
	int HealthAttributeOperation();
	double HealthAttributeAmount();

	boolean KnockbackAttributeEnabled();
	int KnockbackAttributeOperation();
	double KnockbackAttributeAmount();

	boolean MovementSpeedAttributeEnabled();
	int MovementSpeedAttributeOperation();
	double MovementSpeedAttributeAmount();

	boolean SwimSpeedAttributeEnabled();
	int SwimSpeedAttributeOperation();
	double SwimSpeedAttributeAmount();

	boolean ArmorToughnessAttributeEnabled();
	int ArmorToughnessAttributeOperation();
	double ArmorToughnessAttributeAmount();

	boolean LuckAttributeEnabled();
	int LuckAttributeOperation();
	double LuckAttributeAmount();

	boolean ReachAttributeEnabled();
	int ReachAttributeOperation();
	double ReachAttributeAmount();

}
