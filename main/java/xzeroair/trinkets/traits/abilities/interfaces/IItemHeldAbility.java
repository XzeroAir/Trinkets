package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IItemHeldAbility extends IAbilityHandler {

	default void heldMainHand(ItemStack stack) {

	}

	default void heldOffhand(ItemStack stack) {

	}

	default void head(ItemStack head) {

	}

	default void chest(ItemStack chest) {

	}

	default void legs(ItemStack legs) {

	}

	default void feet(ItemStack feet) {

	}

	default void playerInventory(InventoryPlayer inventory) {

	}

}
