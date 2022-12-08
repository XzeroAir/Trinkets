package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ITickableInventoryAbility extends IContainerAbility {

	/**
	 * Triggers during an Inventory Item Update Tick
	 *
	 * @param stack
	 * @param world
	 * @param entity
	 * @param itemSlot
	 * @param inHand
	 */
	default void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean inHand) {

	}

}
