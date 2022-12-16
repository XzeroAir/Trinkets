<<<<<<< Updated upstream
package xzeroair.trinkets.traits.abilities.interfaces;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAbilityInterface {

	ResourceLocation getRegistryName();

	String getDisplayName();

	String getTranslationKey();

	String getUUID();

	@SideOnly(Side.CLIENT)
	void getDescription(List<String> tooltips);

	/**
	 * called when the ability is added to an entity
	 */
	default void onAbilityAdded(EntityLivingBase entity) {

	}

	/**
	 * Called when the ability is removed from the entity
	 */
	default void onAbilityRemoved(EntityLivingBase entity) {

	}

	default boolean shouldRemove() {
		return false;
	}

	default IAbilityInterface scheduleRemoval() {
		return this;
	}

	default void loadStorage(NBTTagCompound nbt) {
	}

	default void saveStorage(NBTTagCompound nbt) {
	}

}
=======
package xzeroair.trinkets.traits.abilities.interfaces;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAbilityInterface {

	ResourceLocation getRegistryName();

	String getDisplayName();

	String getTranslationKey();

	String getUUID();

	@SideOnly(Side.CLIENT)
	void getDescription(List<String> tooltips);

	/**
	 * called when the ability is added to an entity
	 */
	default void onAbilityAdded(EntityLivingBase entity) {

	}

	/**
	 * Called when the ability is removed from the entity
	 */
	default void onAbilityRemoved(EntityLivingBase entity) {

	}

	default boolean shouldRemove() {
		return false;
	}

	default IAbilityInterface scheduleRemoval() {
		return this;
	}

	default void loadStorage(NBTTagCompound nbt) {
	}

	default void saveStorage(NBTTagCompound nbt) {
	}

}
>>>>>>> Stashed changes
