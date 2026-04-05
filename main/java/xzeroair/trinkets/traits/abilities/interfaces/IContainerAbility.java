package xzeroair.trinkets.traits.abilities.interfaces;

import javax.annotation.Nullable;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public interface IContainerAbility extends IAbilityInterface {

	/**
	 * This is a Player Only Ability, Triggers during the player tick event
	 *
	 * @param inventoryContainer - the players inventory container
	 */
	default void inventoryContainer(Container inventoryContainer) {

	}

	/**
	 * Triggers when a player has a container open
	 *
	 * @param openContainer
	 */
	default void openContainer(@Nullable Container openContainer) {

	}

	/**
	 * Triggers during the Player tick event
	 *
	 * @param inventory
	 */
	default void playerInventory(InventoryPlayer inventory) {

	}

}
