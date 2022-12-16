<<<<<<< Updated upstream
package xzeroair.trinkets.util.interfaces;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import xzeroair.trinkets.api.ITrinketInterface;

public interface IAccessoryInterface extends ITrinketInterface {

	/**
	 * @param itemstack
	 * @param player
	 */

	default void eventClientTick(ItemStack stack, EntityPlayer player) {
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
	default void eventPlayerLogin(ItemStack stack, EntityPlayer player) {
	}

	/**
	 * This Triggers when the Player disconnects
	 */
	default void eventPlayerLogout(ItemStack stack, EntityPlayer player) {
	}

	default void eventPlayerChangedDimension(ItemStack stack, EntityPlayer player, int fromDimension, int toDimension) {
	}

	/**
	 * This Triggers when the Entity jumps
	 */
	default void eventLivingJump(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the Entity is About to hit the ground
	 */
	default void eventLivingFall(ItemStack stack, EntityLivingBase entity, LivingFallEvent event) {
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Combat~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * This Triggers when an Entity tries targeting an Entity
	 *
	 * @param stack
	 * @param target   - The Entity being targeted
	 * @param attacker - The Entity Targeting
	 */
	default void eventSetAttackTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase targeter) {
	}

	/**
	 * This Triggers when the Entity is About to be Hurt
	 *
	 * @param stack
	 * @param entity - The Entity being Attacked - the same as
	 *               {@link LivingHurtEvent#getEntityLiving()}
	 * @param event
	 */
	default void eventLivingHurtAttacked(@Nonnull ItemStack stack, @Nonnull EntityLivingBase attacked, @Nonnull LivingHurtEvent event) {
	}

	/**
	 * This Triggers when an Entity is about to Hurt another Entity
	 *
	 * @param stack
	 * @param entity - The Entity Attacking - the same as
	 *               {@link LivingHurtEvent#getSource()}.{@link DamageSource#getTrueSource()}
	 * @param event
	 */
	default void eventLivingHurtAttacker(@Nonnull ItemStack stack, @Nonnull EntityLivingBase attacker, @Nonnull LivingHurtEvent event) {
	}

	/**
	 * This Triggers when Entity is about to be Damaged
	 *
	 * @param stack
	 * @param entity - The Entity Attacking - the same as
	 *               {@link LivingHurtEvent#getSource()}.{@link DamageSource#getTrueSource()}
	 * @param event
	 */
	default void eventLivingDamageAttacked(ItemStack stack, EntityLivingBase attacked, LivingDamageEvent event) {
	}

	/**
	 * This Triggers when an Entity is about to Damage another Entity
	 *
	 * @param stack
	 * @param entity - The Entity Attacking - the same as
	 *               {@link LivingHurtEvent#getSource()}.{@link DamageSource#getTrueSource()}
	 * @param event
	 */
	default void eventLivingDamageAttacker(ItemStack stack, EntityLivingBase attacker, LivingDamageEvent event) {
	}

	/**
	 * This Triggers when a Player kills an Entity
	 */
	default void eventLivingExperienceDrops(ItemStack stack, EntityPlayer player, LivingExperienceDropEvent event) {
	}

	/**
	 * This Triggers when an Entity kills another Entity
	 *
	 * @param stack
	 * @param attacker - The Entity Attacking - the same as
	 *                 {@link LivingDropsEvent#getSource()}.{@link DamageSource#getTrueSource()}
	 * @param event
	 */
	default void eventLivingDrops(ItemStack stack, EntityLivingBase attacker, LivingDropsEvent event) {
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~End Combat~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * This Triggers when the Player is attempting to break a block
	 */
	default void eventBreakSpeed(ItemStack stack, EntityPlayer player, BreakSpeed event) {
	}

	/**
	 * This Triggers when the Player is about to break a block
	 */
	default void eventBlockBreak(ItemStack stack, EntityPlayer player, BreakEvent event) {
	}

	/**
	 * This Triggers after a player breaks a block
	 */
	default void eventBlockDrops(ItemStack stack, EntityLivingBase player, HarvestDropsEvent event) {
	}

	/**
	 * This Triggers when a potion effect is about to be applied to an entity
	 */
	default void eventPotionApplicable(ItemStack stack, EntityLivingBase entity, PotionApplicableEvent event) {
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~End Events~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	default void onEntityArmorTick(World world, EntityLivingBase entity, ItemStack stack) {

	}

	/**
	 * This Triggers when the Entity Equips the ItemStack
	 */
	default void onAccessoryEquipped(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the Entity Unequips the ItemStack
	 */
	default void onAccessoryUnequipped(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the Entity Attempts to Equip an ItemStack
	 */
	default boolean canEquipAccessory(ItemStack stack, EntityLivingBase entity) {
		return true;
	}

	/**
	 * This Triggers when the Entity Attempts to Unequip an ItemStack
	 */
	default boolean canUnequipAccessory(ItemStack stack, EntityLivingBase entity) {
		return true;
	}

	/**
	 * This Renders things on the Trinkets Render Layer
	 *
	 * @param scale
	 * @param isSlim - True if using Slim Model
	 */
	default void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
	}

	/**
	 * This is used internally to determine if Trinket Items should be registered
	 */
	default boolean ItemEnabled() {
		return true;
	}

}
=======
package xzeroair.trinkets.util.interfaces;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import xzeroair.trinkets.api.ITrinketInterface;

public interface IAccessoryInterface extends ITrinketInterface {

	/**
	 * @param itemstack
	 * @param player
	 */

	default void eventClientTick(ItemStack stack, EntityPlayer player) {
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
	default void eventPlayerLogin(ItemStack stack, EntityPlayer player) {
	}

	/**
	 * This Triggers when the Player disconnects
	 */
	default void eventPlayerLogout(ItemStack stack, EntityPlayer player) {
	}

	default void eventPlayerChangedDimension(ItemStack stack, EntityPlayer player, int fromDimension, int toDimension) {
	}

	/**
	 * This Triggers when the Entity jumps
	 */
	default void eventLivingJump(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the Entity is About to hit the ground
	 */
	default void eventLivingFall(ItemStack stack, EntityLivingBase entity, LivingFallEvent event) {
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Combat~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * This Triggers when an Entity tries targeting an Entity
	 *
	 * @param stack
	 * @param target   - The Entity being targeted
	 * @param attacker - The Entity Targeting
	 */
	default void eventSetAttackTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase targeter) {
	}

	/**
	 * This Triggers when the Entity is About to be Hurt
	 *
	 * @param stack
	 * @param entity - The Entity being Attacked - the same as
	 *               {@link LivingHurtEvent#getEntityLiving()}
	 * @param event
	 */
	default void eventLivingHurtAttacked(@Nonnull ItemStack stack, @Nonnull EntityLivingBase attacked, @Nonnull LivingHurtEvent event) {
	}

	/**
	 * This Triggers when an Entity is about to Hurt another Entity
	 *
	 * @param stack
	 * @param entity - The Entity Attacking - the same as
	 *               {@link LivingHurtEvent#getSource()}.{@link DamageSource#getTrueSource()}
	 * @param event
	 */
	default void eventLivingHurtAttacker(@Nonnull ItemStack stack, @Nonnull EntityLivingBase attacker, @Nonnull LivingHurtEvent event) {
	}

	/**
	 * This Triggers when Entity is about to be Damaged
	 *
	 * @param stack
	 * @param entity - The Entity Attacking - the same as
	 *               {@link LivingHurtEvent#getSource()}.{@link DamageSource#getTrueSource()}
	 * @param event
	 */
	default void eventLivingDamageAttacked(ItemStack stack, EntityLivingBase attacked, LivingDamageEvent event) {
	}

	/**
	 * This Triggers when an Entity is about to Damage another Entity
	 *
	 * @param stack
	 * @param entity - The Entity Attacking - the same as
	 *               {@link LivingHurtEvent#getSource()}.{@link DamageSource#getTrueSource()}
	 * @param event
	 */
	default void eventLivingDamageAttacker(ItemStack stack, EntityLivingBase attacker, LivingDamageEvent event) {
	}

	/**
	 * This Triggers when a Player kills an Entity
	 */
	default void eventLivingExperienceDrops(ItemStack stack, EntityPlayer player, LivingExperienceDropEvent event) {
	}

	/**
	 * This Triggers when an Entity kills another Entity
	 *
	 * @param stack
	 * @param attacker - The Entity Attacking - the same as
	 *                 {@link LivingDropsEvent#getSource()}.{@link DamageSource#getTrueSource()}
	 * @param event
	 */
	default void eventLivingDrops(ItemStack stack, EntityLivingBase attacker, LivingDropsEvent event) {
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~End Combat~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * This Triggers when the Player is attempting to break a block
	 */
	default void eventBreakSpeed(ItemStack stack, EntityPlayer player, BreakSpeed event) {
	}

	/**
	 * This Triggers when the Player is about to break a block
	 */
	default void eventBlockBreak(ItemStack stack, EntityPlayer player, BreakEvent event) {
	}

	/**
	 * This Triggers after a player breaks a block
	 */
	default void eventBlockDrops(ItemStack stack, EntityLivingBase player, HarvestDropsEvent event) {
	}

	/**
	 * This Triggers when a potion effect is about to be applied to an entity
	 */
	default void eventPotionApplicable(ItemStack stack, EntityLivingBase entity, PotionApplicableEvent event) {
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~End Events~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	default void onEntityArmorTick(World world, EntityLivingBase entity, ItemStack stack) {

	}

	/**
	 * This Triggers when the Entity Equips the ItemStack
	 */
	default void onAccessoryEquipped(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the Entity Unequips the ItemStack
	 */
	default void onAccessoryUnequipped(ItemStack stack, EntityLivingBase entity) {
	}

	/**
	 * This Triggers when the Entity Attempts to Equip an ItemStack
	 */
	default boolean canEquipAccessory(ItemStack stack, EntityLivingBase entity) {
		return true;
	}

	/**
	 * This Triggers when the Entity Attempts to Unequip an ItemStack
	 */
	default boolean canUnequipAccessory(ItemStack stack, EntityLivingBase entity) {
		return true;
	}

	/**
	 * This Renders things on the Trinkets Render Layer
	 *
	 * @param scale
	 * @param isSlim - True if using Slim Model
	 */
	default void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
	}

	/**
	 * This is used internally to determine if Trinket Items should be registered
	 */
	default boolean ItemEnabled() {
		return true;
	}

}
>>>>>>> Stashed changes
