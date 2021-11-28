package xzeroair.trinkets.attributes;

import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;
import xzeroair.trinkets.util.interfaces.IAttributeConfigHelper;

public class AttributeConfigWrapper {

	protected IAttributeConfigHelper config;

	public AttributeConfigWrapper() {
		config = new ConfigAttribs(false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0);
	}

	public AttributeConfigWrapper(IAttributeConfigHelper config) {
		if (config != null) {
			this.config = config;
		} else {
			this.config = new ConfigAttribs(false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0, false, 0, 0);
		}
	}

	public boolean armorEnabled() {
		return config.ArmorAttributeEnabled();
	}

	public final double getArmor() {
		return config.ArmorAttributeAmount();
	}

	public final int getArmorOperation() {
		return config.ArmorAttributeOperation();
	}

	public boolean armorToughnessEnabled() {
		return config.ArmorToughnessAttributeEnabled();
	}

	public final double getArmorToughness() {
		return config.ArmorToughnessAttributeAmount();
	}

	public final int getArmorToughnessOperation() {
		return config.ArmorToughnessAttributeOperation();
	}

	public boolean attackSpeedEnabled() {
		return config.AttackSpeedAttributeEnabled();
	}

	public final double getAttackSpeed() {
		return config.AttackSpeedAttributeAmount();
	}

	public final int getAttackSpeedOperation() {
		return config.AttackSpeedAttributeOperation();
	}

	public boolean attackDamageEnabled() {
		return config.DamageAttributeEnabled();
	}

	public final double getAttackDamage() {
		return config.DamageAttributeAmount();
	}

	public final int getAttackDamageOperation() {
		return config.DamageAttributeOperation();
	}

	public boolean healthEnabled() {
		return config.HealthAttributeEnabled();
	}

	public final double getHealth() {
		return config.HealthAttributeAmount();
	}

	public final int getHealthOperation() {
		return config.HealthAttributeOperation();
	}

	public boolean jumpEnabled() {
		return config.JumpAttributeEnabled();
	}

	public final double getJump() {
		return config.JumpAttributeAmount();
	}

	public final int getJumpOperation() {
		return config.JumpAttributeOperation();
	}

	public boolean knockbackEnabled() {
		return config.KnockbackAttributeEnabled();
	}

	public final double getKnockback() {
		return config.KnockbackAttributeAmount();
	}

	public final int getKnockbackOperation() {
		return config.KnockbackAttributeOperation();
	}

	public boolean luckEnabled() {
		return config.LuckAttributeEnabled();
	}

	public final double getLuck() {
		return config.LuckAttributeAmount();
	}

	public final int getLuckOperation() {
		return config.LuckAttributeOperation();
	}

	public boolean movementSpeedEnabled() {
		return config.MovementSpeedAttributeEnabled();
	}

	public final double getMovementSpeed() {
		return config.MovementSpeedAttributeAmount();
	}

	public final int getMovementSpeedOperation() {
		return config.MovementSpeedAttributeOperation();
	}

	public boolean reachEnabled() {
		return config.ReachAttributeEnabled();
	}

	public final double getReach() {
		return config.ReachAttributeAmount();
	}

	public final int getReachOperation() {
		return config.ReachAttributeOperation();
	}

	public boolean stepHeightEnabled() {
		return config.StepHeightAttributeEnabled();
	}

	public double getStepHeight() {
		return config.StepHeightAttributeAmount();
	}

	public int getStepHeightOperation() {
		return config.StepHeightAttributeOperation();
	}

	public boolean swimSpeedEnabled() {
		return config.SwimSpeedAttributeEnabled();
	}

	public final double getSwimSpeed() {
		return config.SwimSpeedAttributeAmount();
	}

	public final int getSwimSpeedOperation() {
		return config.SwimSpeedAttributeOperation();
	}

}
