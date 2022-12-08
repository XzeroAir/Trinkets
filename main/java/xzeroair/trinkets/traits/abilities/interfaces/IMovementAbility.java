package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.Entity;

public interface IMovementAbility extends IAbilityInterface {

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
