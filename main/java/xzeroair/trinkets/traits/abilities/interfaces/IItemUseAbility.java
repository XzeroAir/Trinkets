package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IItemUseAbility extends IAbilityHandler {

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
