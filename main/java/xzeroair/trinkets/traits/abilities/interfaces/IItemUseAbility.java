<<<<<<< Updated upstream
package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IItemUseAbility extends IAbilityInterface {

	default int onItemStartUse(EntityLivingBase entity, ItemStack stack, int duration) {
		return duration;
	}

	default int onItemUseTick(EntityLivingBase entity, ItemStack stack, int duration) {
		return duration;
	}

	default void onItemUseStop(EntityLivingBase entity, ItemStack stack, int duration) {

	}

	default void onItemUseFinish(EntityLivingBase entity, ItemStack stack, int duration) {

	}

}
=======
package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IItemUseAbility extends IAbilityInterface {

	default int onItemStartUse(EntityLivingBase entity, ItemStack stack, int duration) {
		return duration;
	}

	default int onItemUseTick(EntityLivingBase entity, ItemStack stack, int duration) {
		return duration;
	}

	default void onItemUseStop(EntityLivingBase entity, ItemStack stack, int duration) {

	}

	default void onItemUseFinish(EntityLivingBase entity, ItemStack stack, int duration) {

	}

}
>>>>>>> Stashed changes
