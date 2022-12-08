package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IHeldAbility extends IAbilityInterface {

	default void heldMainHand(ItemStack stack, EntityLivingBase entity) {

	}

	default void heldOffhand(ItemStack stack, EntityLivingBase entity) {

	}
}
