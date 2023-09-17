package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;

public interface ILightningStrikeAbility extends IAbilityInterface {

	default boolean onStruckByLightning(EntityLivingBase entity, boolean cancel) {
		return cancel;
	}

}
