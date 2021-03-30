package xzeroair.trinkets.util.interfaces;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public interface IAccessoryInterface {

	/**
	 * @param itemstack
	 * @param player
	 */

	default void eventClientTick(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers at the End of every Player Tick
	 */
	default void eventPlayerTick(ItemStack stack, EntityPlayer player) {
	}

	/**
	 * This Triggers at the End of every LivingUpdate Tick
	 */
	default void eventLivingUpdateTick(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the entity enters the world
	 */
	default void eventEntityJoinWorld(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the Player disconnects
	 */
	default void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Entity jumps
	 */
	default void eventLivingJump(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the Entity is About to hit the ground
	 */
	default void eventLivingFall(LivingFallEvent event, ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the Player is About to be Hurt
	 */
	default void eventPlayerHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when an Entity is about to be Hurt by a Player
	 */
	default void eventLivingHurt(LivingHurtEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Player is attempting to break a block
	 */
	default void eventBreakSpeed(BreakSpeed event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Player is about to break a block
	 */
	default void eventBlockBreak(BreakEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers after a player breaks a block
	 */
	default void eventBlockDrops(HarvestDropsEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when something tries to target the Player
	 */
	default void eventSetAttackTarget(LivingSetAttackTargetEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Player kills an Entity
	 */
	default void eventLivingExperienceDrops(LivingExperienceDropEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Player kills an Entity
	 */
	default void eventLivingDrops(LivingDropsEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Player is about to be Damaged by an Entity
	 */
	default void eventLivingDamageAttacked(LivingDamageEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Player is about to Damage an Entity
	 */
	default void eventLivingDamageAttacker(LivingDamageEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when a potion effect is about to be applied to a player
	 */
	default void eventPotionApplicable(PotionApplicableEvent event, ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Player Equips the ItemStack
	 */
	default void playerEquipped(ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Player Unequips the ItemStack
	 */
	default void playerUnequipped(ItemStack stack, EntityLivingBase player) {
	}

	/**
	 * This Triggers when the Player Attempts to Equip an ItemStack
	 */
	default boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	/**
	 * This Triggers when the Player Attempts to Unequip an ItemStack
	 */
	default boolean playerCanUnequip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	/**
	 * This Renders things on the Trinkets Render Layer
	 *
	 * @param scale
	 */
	default void playerRender(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, float partialTicks, float scale, boolean isTrinket) {
	}

	/**
	 * This is used internally to determine if Trinket Items should be registered
	 */
	default boolean ItemEnabled() {
		return true;
	}

}
