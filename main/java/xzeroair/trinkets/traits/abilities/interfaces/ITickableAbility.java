package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;

public interface ITickableAbility extends IAbilityInterface {

    default void tickAbilityPre(EntityLivingBase entity) {
    }

	void tickAbility(EntityLivingBase entity);

}
