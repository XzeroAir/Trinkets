package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IJumpAbility extends IAbilityHandler {

	default void jump(EntityLivingBase entity) {
	}

	default float fallDistance(EntityLivingBase entity, float distance) {
		return distance;
	}

	default float fallDamageMultiplier(EntityLivingBase entity, float multiplier) {
		return multiplier;
	}

	default boolean fall(EntityLivingBase entity, float distance, float multiplier, boolean cancel) {
		return cancel;
	}

}
