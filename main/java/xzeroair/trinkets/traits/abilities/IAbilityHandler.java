package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IAbilityHandler {

	//	KeyHandler getKeybindHandler();

	//	NBTTagCompound getStorage();

	/*
	 * Call this Method to Add the Ability to the entity provided;
	 */
	default void addAbility(EntityLivingBase entity) {

	}

	/*
	 * Call this Method to Remove the Ability from the entity provided
	 */
	default void removeAbility(EntityLivingBase entity) {

	}

	default boolean shouldRemove() {
		return false;
	}

	default void scheduleRemoval() {

	}

	default void loadStorage(NBTTagCompound nbt) {
	}

	default void saveStorage(NBTTagCompound nbt) {
	}
}
