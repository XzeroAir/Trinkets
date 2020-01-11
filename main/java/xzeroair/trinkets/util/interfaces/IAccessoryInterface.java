package xzeroair.trinkets.util.interfaces;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public interface IAccessoryInterface {

	/**
	 * @param itemstack
	 * @param player
	 */

	/**
	 * This Triggers at the End of every Player Tick
	 * */
	default void eventPlayerTick(ItemStack stack, EntityPlayer player) {}

	/**
	 * This Triggers at the End of every LivingUpdate Tick
	 * */
	default void eventLivingUpdateTick(ItemStack stack, EntityLivingBase entity) {}

	/**
	 * This Triggers when the Player enters the world
	 * */
	default void eventEntityJoinWorld(ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player disconnects
	 * */
	default void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player jumps
	 * */
	default void eventLivingJump(ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player is About to hit the ground
	 * */
	default void eventLivingFall(LivingFallEvent event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player is About to be Hurt
	 * */
	default void eventPlayerHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when an Entity is about to be Hurt by a Player
	 * */
	default void eventLivingHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player is attempting to break a block
	 * */
	default void eventBreakSpeed(BreakSpeed event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player is about to break a block
	 * */
	default void eventBlockBreak(BreakEvent event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers after a player breaks a block
	 * */
	default void eventBlockDrops(HarvestDropsEvent event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when something tries to target the Player
	 * */
	default void eventSetAttackTarget(LivingSetAttackTargetEvent event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player kills an Entity
	 * */
	default void eventLivingExperienceDrops(LivingExperienceDropEvent event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player kills an Entity
	 * */
	default void eventLivingDrops(LivingDropsEvent event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player is about to be Damaged by an Entity
	 * */
	default void eventLivingDamageAttacked(LivingDamageEvent event, ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player is about to Damage an Entity
	 * */
	default void eventLivingDamageAttacker(LivingDamageEvent event, ItemStack stack, EntityLivingBase player) {}

	default String getIsTrinketOrBauble(ItemStack stack, EntityLivingBase player) {
		return "Else";
	}

	/**
	 * This Returns the Current Slot ID of the ItemStack
	 * */
	default int getEquippedSlot(ItemStack stack, EntityLivingBase player) {
		return -1;
	}

	/**
	 * This Triggers when the Player Equips the ItemStack
	 * */
	default void playerEquipped(ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player Unequips the ItemStack
	 * */
	default void playerUnequipped(ItemStack stack, EntityLivingBase player) {}

	/**
	 * This Triggers when the Player Attempts to Equip an ItemStack
	 * */
	default boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	/**
	 * This Triggers when the Player Attempts to Unequip an ItemStack
	 * */
	default boolean playerCanUnequip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	default boolean ItemEnabled() {
		return true;
	}

	default void playerRender(ItemStack stack, EntityLivingBase player, float partialTicks, boolean isBauble) {}

	default boolean ability() {
		return false;
	}

	default void setAbility(boolean transformed) {}

	default boolean altAbility() {
		return false;
	}

	default void setAltAbility(boolean transformed) {}

	default int oreTarget() {
		return -1;
	}

	default void setOreTarget(int type) {}

	default int hitCount() {
		return 0;
	}

	default void setHitCount(int hits) {}

	default int storedExp() {
		return 0;
	}

	default void setStoredExp(int exp) {}

	default int wornSlot() {
		return -1;
	}

	default void setWornSlot(int slot) {}

	default	NBTTagCompound saveNBT() {
		return null;
	}

	default void loadNBT(NBTTagCompound compound) {}

}
