package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;

public interface ITickableAbility extends IAbilityInterface {

	void tickAbility(EntityLivingBase entity);

}
