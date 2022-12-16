package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;

public class AbilitySturdy extends Ability implements IAttackAbility, IJumpAbility {

	float fallMultiplier = 1F;

	public AbilitySturdy() {
		super(Abilities.nullKinetic);
	}

	public AbilitySturdy setFallMultiplier(float fallMultiplier) {
		this.fallMultiplier = fallMultiplier;
		return this;
	}

	public float getFallMultiplier() {
		return fallMultiplier;
	}

	@Override
	public boolean fall(EntityLivingBase entity, float distance, float multiplier, boolean cancel) {
		if ((fallMultiplier <= 0))
			return true;
		return cancel;
	}

	@Override
	public float fallDamageMultiplier(EntityLivingBase entity, float multiplier) {
		if ((fallMultiplier != 1F)) {
			multiplier = fallMultiplier;
		}
		return multiplier;
	}

	@Override
	public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
		if ((fallMultiplier <= 0)) {
			if (source.equals(DamageSource.FALL) || source.equals(DamageSource.FALLING_BLOCK) || source.equals(DamageSource.FLY_INTO_WALL))
				return true;
		}
		return cancel;
	}

}
