<<<<<<< Updated upstream
package xzeroair.trinkets.traits.abilities.interfaces;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public interface IAttackAbility extends IAbilityInterface {

	default void targetedByEnemy(EntityLivingBase enemy) {

	}

	/**
	 * @param source - Damage Source attacking the Entity with the Ability
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return boolean - Return False to Cancel the Attack Entirely
	 */
	default boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
		return cancel;
	}

	/**
	 * @param source - Damage Source attacking the Entity with the Ability
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return float - Return the modified damage from the source, returning 0
	 *         cancels the event
	 *
	 *         Armor and Enchantments have not been applied yet
	 */
	default float hurt(EntityLivingBase attacked, DamageSource source, float dmg) {
		return dmg;
	}

	/**
	 * @param source - Damage Source attacking the Entity with the Ability
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return float - Return the modified damage from the source, returning 0
	 *         cancels the event
	 *
	 *         Armor and Enchantments have been applied at this point
	 */
	default float damaged(EntityLivingBase attacked, DamageSource source, float dmg) {
		return dmg;
	}

	default boolean died(EntityLivingBase attacked, DamageSource source, boolean cancel) {
		return cancel;
	}

	/**
	 * @param source - DamageSource used to attack the entity
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return boolean - Return false to Cancel the attack
	 */
	default boolean attackEntity(EntityLivingBase target, DamageSource source, float dmg, boolean cancel) {
		return cancel;
	}

	/**
	 * @param target - the Entity the Ability Holder is attacking
	 * @param source - DamageSource the Ability Holder is attacking with
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return float - Return the modified damage, return dmg if nothing was
	 *         modified, returning 0 cancels the event
	 *
	 *         Enchantments have not been applied yet
	 */
	default float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		return dmg;
	}

	/**
	 * @param target - the Entity the Ability Holder is attacking
	 * @param source - DamageSource the Ability Holder is attacking with
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return float - Return the modified damage, return dmg if nothing was
	 *         modified, returning 0 cancels the event
	 *
	 *         Armor and Enchantments have been applied at this point
	 */
	default float damageEntity(EntityLivingBase target, DamageSource source, float dmg) {
		return dmg;
	}

	default void killedEntity(EntityLivingBase target, DamageSource source) {
	}

	default int killedEntityExpDrop(EntityLivingBase target, int originalExp, int droppedExp) {
		return droppedExp;
	}

	default void killedEntityItemDrops(EntityLivingBase target, DamageSource source, int lootingLevel, List<EntityItem> drops) {

	}

	default boolean isIndirectDamage(DamageSource source) {
		return (source instanceof EntityDamageSourceIndirect) || source.isExplosion() || source.isMagicDamage() || source.isProjectile();
	}

}
=======
package xzeroair.trinkets.traits.abilities.interfaces;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public interface IAttackAbility extends IAbilityInterface {

	default void targetedByEnemy(EntityLivingBase enemy) {

	}

	/**
	 * @param source - Damage Source attacking the Entity with the Ability
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return boolean - Return False to Cancel the Attack Entirely
	 */
	default boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
		return cancel;
	}

	/**
	 * @param source - Damage Source attacking the Entity with the Ability
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return float - Return the modified damage from the source, returning 0
	 *         cancels the event
	 *
	 *         Armor and Enchantments have not been applied yet
	 */
	default float hurt(EntityLivingBase attacked, DamageSource source, float dmg) {
		return dmg;
	}

	/**
	 * @param source - Damage Source attacking the Entity with the Ability
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return float - Return the modified damage from the source, returning 0
	 *         cancels the event
	 *
	 *         Armor and Enchantments have been applied at this point
	 */
	default float damaged(EntityLivingBase attacked, DamageSource source, float dmg) {
		return dmg;
	}

	default boolean died(EntityLivingBase attacked, DamageSource source, boolean cancel) {
		return cancel;
	}

	/**
	 * @param source - DamageSource used to attack the entity
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return boolean - Return false to Cancel the attack
	 */
	default boolean attackEntity(EntityLivingBase target, DamageSource source, float dmg, boolean cancel) {
		return cancel;
	}

	/**
	 * @param target - the Entity the Ability Holder is attacking
	 * @param source - DamageSource the Ability Holder is attacking with
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return float - Return the modified damage, return dmg if nothing was
	 *         modified, returning 0 cancels the event
	 *
	 *         Enchantments have not been applied yet
	 */
	default float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		return dmg;
	}

	/**
	 * @param target - the Entity the Ability Holder is attacking
	 * @param source - DamageSource the Ability Holder is attacking with
	 * @param dmg    - The Amount of Damage done by the DamageSource
	 * @return float - Return the modified damage, return dmg if nothing was
	 *         modified, returning 0 cancels the event
	 *
	 *         Armor and Enchantments have been applied at this point
	 */
	default float damageEntity(EntityLivingBase target, DamageSource source, float dmg) {
		return dmg;
	}

	default void killedEntity(EntityLivingBase target, DamageSource source) {
	}

	default int killedEntityExpDrop(EntityLivingBase target, int originalExp, int droppedExp) {
		return droppedExp;
	}

	default void killedEntityItemDrops(EntityLivingBase target, DamageSource source, int lootingLevel, List<EntityItem> drops) {

	}

	default boolean isIndirectDamage(DamageSource source) {
		return (source instanceof EntityDamageSourceIndirect) || source.isExplosion() || source.isMagicDamage() || source.isProjectile();
	}

}
>>>>>>> Stashed changes
