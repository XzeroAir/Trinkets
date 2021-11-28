package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IPotionAbility extends IAbilityHandler {

	// Can't be given the potion even with commands if denied
	boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel);

}
