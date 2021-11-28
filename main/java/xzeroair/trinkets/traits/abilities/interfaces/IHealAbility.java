package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IHealAbility extends IAbilityHandler {

	float onHeal(EntityLivingBase entity, float healAmount);

}
