package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.Entity;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IMovementAbility extends IAbilityHandler {

	public default boolean left(Entity entity, int state) {
		return true;
	}

	public default boolean right(Entity entity, int state) {
		return true;
	}

	public default boolean forward(Entity entity, int state) {
		return true;
	}

	public default boolean back(Entity entity, int state) {
		return true;
	}

	public default boolean jump(Entity entity, int state) {
		return true;
	}

	public default boolean sneak(Entity entity, int state) {
		return true;
	}

}
