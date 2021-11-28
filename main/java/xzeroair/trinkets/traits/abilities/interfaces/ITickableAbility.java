package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface ITickableAbility extends IAbilityHandler {

	void tickAbility(EntityLivingBase entity);

}
