package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface ISleepAbility extends IAbilityHandler {

	void onWakeUp(EntityLivingBase entity, boolean wakeImmediately, boolean updatedWorld, boolean setSpawn);

}
