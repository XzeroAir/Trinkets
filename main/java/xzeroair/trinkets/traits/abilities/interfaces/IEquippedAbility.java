package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IEquippedAbility extends IAbilityInterface {

	default void head(ItemStack head, EntityLivingBase entity) {

	}

	default void chest(ItemStack chest, EntityLivingBase entity) {

	}

	default void legs(ItemStack legs, EntityLivingBase entity) {

	}

	default void feet(ItemStack feet, EntityLivingBase entity) {

	}

}
