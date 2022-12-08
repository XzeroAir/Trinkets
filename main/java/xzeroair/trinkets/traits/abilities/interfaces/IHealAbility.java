package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;

public interface IHealAbility extends IAbilityInterface {

	float onHeal(EntityLivingBase entity, float healAmount);

}
