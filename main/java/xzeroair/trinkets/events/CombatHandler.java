package xzeroair.trinkets.events;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IBowAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IHealAbility;

public class CombatHandler extends EventBaseHandler {

	@SubscribeEvent
	public void arrowLooseEvent(ArrowLooseEvent event) {
		//only runs when item is actually used - Useless Event for Bows
		// Apparently it Works now? maybe another mod was Breaking it? either that or I fixed whatever trash code of mine that was breaking it
		final EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		//					int charge = 72000 - event.getDuration();
		if (prop != null) {
			try {
				prop.getRaceProperties().bowUsed(event.getBow(), event.getCharge());
			} catch (final Exception e) {
				e.printStackTrace();
			}
			// Handle Entity Abilities
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IBowAbility)) {
						((IBowAbility) handler).looseArrow(event.getEntityLiving(), event.getWorld(), event.getBow(), event.getCharge());
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void arrowNockEvent(ArrowNockEvent event) {
		final EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		final World world = event.getWorld();
		final ItemStack stack = event.getBow();
		final EnumHand hand = event.getHand();
		final boolean hasAmmo = event.hasAmmo();
		final ActionResult<ItemStack> eventAction = event.getAction();
		ActionResult<ItemStack> action = eventAction;
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IBowAbility)) {
						final ActionResult<ItemStack> abilityAction = ((IBowAbility) handler).knockArrow(event.getEntityLiving(), world, stack, hand, action, hasAmmo);
						if (abilityAction != null) {
							action = abilityAction;
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
			final ActionResult<ItemStack> raceResult = prop.getRaceProperties().bowNocked(world, stack, hand, action, hasAmmo);
			if (raceResult != null) {
				action = raceResult;
			}
			if ((eventAction != action) && (action != null)) {
				event.setAction(action);
			}
		}
	}

	@SubscribeEvent
	public void ArrowImpactEvent(ProjectileImpactEvent.Arrow event) {
		if (event.getArrow().shootingEntity instanceof EntityLivingBase) {
			final EntityProperties prop = Capabilities.getEntityRace(event.getArrow().shootingEntity);
			if (prop != null) {
				// Handle Entity Abilities
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IBowAbility)) {
							((IBowAbility) handler).arrowImpact((EntityLivingBase) event.getArrow().shootingEntity, event.getArrow(), event.getRayTraceResult());
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
		final EntityProperties prop = Capabilities.getEntityRace(event.getTarget());
		if (prop != null) {
			prop.getRaceProperties().targetedByEnemy(event.getEntityLiving());
		}
	}

	//	public static void printDamageSource(Entity entity, DamageSource source, float dmg) {
	//
	//		if (source.getImmediateSource() != null) {
	//			//			final Entity trueAttacker = source.getTrueSource();
	//			String attacker = "None";
	//			if (source.getTrueSource() != null) {
	//				attacker = source.getTrueSource().getName();
	//			}
	//			System.out.println(entity.getName() + " is being attacked by");
	//			System.out.println("Immediate Source: " + source.getImmediateSource().getName() + " | True Source: " + attacker + " | Type: " + source.damageType + " | damage: " + dmg);
	//		}
	//	}

	// First Attempt at Attack
	@SubscribeEvent
	public void attackEvent(LivingAttackEvent event) {
		final DamageSource source = event.getSource();
		if (source.canHarmInCreative()) {
			return;
		}
		final EntityLivingBase attacked = event.getEntityLiving();
		EntityLivingBase attacker = null;
		if (source.getTrueSource() instanceof EntityLivingBase) {
			attacker = (EntityLivingBase) source.getTrueSource();
		}
		final float damage = event.getAmount();

		//		  System.out.println("Attacker: "+event.getSource().getImmediateSource().getName() +" Type: " + event.getSource().damageType + " | Damage:" + event.getAmount());
		//				if(attacked.getIsInvulnerable()) {
		//				}

		// Handle Attacker
		boolean cancel = false;
		if (attacker != null) {
			final EntityProperties prop = Capabilities.getEntityRace(attacker);
			if (prop != null) {
				// Handle Entity Abilities
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					if (cancel) {
						break;
					}
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IAttackAbility)) {
							if (((IAttackAbility) handler).attackEntity(attacked, source, damage, cancel)) {
								cancel = true;
							}
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}
			}
		}

		// Handle Attacked
		if (!cancel) {
			final EntityProperties prop = Capabilities.getEntityRace(attacked);
			if (prop != null) {
				// Handle Entity Abilities
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					if (cancel) {
						break;
					}
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IAttackAbility)) {
							if (((IAttackAbility) handler).attacked(attacked, source, damage, cancel)) {
								cancel = true;
							}
							//							System.out.println(ability.getName() + " | Attacked " + cancel);
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}
				// Handle Entity Race
				final boolean raceCancel = prop.getRaceProperties().isAttacked(source, damage);
				if ((raceCancel == false) && event.isCancelable() && !event.isCanceled()) {
					event.setCanceled(true);
				}
			}
		}
		if (cancel && !this.isEventCanceled(event)) {
			this.cancelEvent(event);
		}
	}

	//	@SubscribeEvent
	//	public void CriticalHitEvent(CriticalHitEvent event) {
	//		EntityLivingBase attacker = event.getEntityLiving();
	//		// Handle Attacker
	//		if (attacker != null) {
	//			EntityProperties prop = Capabilities.getEntityRace(attacker);
	//			// Handle Entity Abilities
	//			boolean cancel = false;
	//			for (IAbilityInterface ability : prop.getAbilities().getAbilitiesList()) {
	//				try {
	//					IAbilityHandler handler = prop.getAbilities().getAbilityHandler(ability);
	//					if ((handler != null) && (handler instanceof IAttackAbility)) {
	//						float newModifier = ((IAttackAbility) handler).critical(attacker, event.getOldDamageModifier(), event.getDamageModifier(), event.isVanillaCritical());
	//					}
	//				} catch (Exception e) {
	//					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
	//					e.printStackTrace();
	//				}
	//			}
	//			if (cancel && event.isCancelable() && !event.isCanceled()) {
	//				event.setCanceled(true);
	//			}
	//		}
	//	}

	@SubscribeEvent
	public void knockbackEvent(LivingKnockBackEvent event) {

	}

	// Started Attack, Calculating Damage
	//Cancelable
	@SubscribeEvent
	public void HurtEvent(LivingHurtEvent event) {
		final DamageSource source = event.getSource();
		float damage = event.getAmount();
		if (source.canHarmInCreative() || (damage == 0)) {
			return;
		}
		final EntityLivingBase attacked = event.getEntityLiving();

		EntityLivingBase attacker = null;
		if (source.getTrueSource() instanceof EntityLivingBase) {
			attacker = (EntityLivingBase) source.getTrueSource();
		}

		//		if (attacker == attacked) {
		//			Trinkets.log.info("Why are you hitting yourself, why are you hitting yourself");
		//		}
		//		System.out.println("Source Damage Type: " + source.damageType + " | Damage: " + damage);
		// Handle Attacker
		if (attacker != null) {
			final EntityProperties prop = Capabilities.getEntityRace(attacker);
			if (prop != null) {
				// Handle Entity Abilities
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IAttackAbility)) {
							damage = ((IAttackAbility) handler).hurtEntity(attacked, source, damage);
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}
				// Handle Entity Race
				damage = prop.getRaceProperties().hurtEntity(attacked, source, damage);
			}
		}

		// Handle Attacked
		if (damage > 0) {
			final EntityProperties prop = Capabilities.getEntityRace(attacked);
			if (prop != null) {
				// Handle Entity Abilities
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IAttackAbility)) {
							damage = ((IAttackAbility) handler).hurt(attacked, source, damage);
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}

				// Handle Entity Race
				damage = prop.getRaceProperties().isHurt(source, damage);
			}
		}
		if ((event.getAmount() != damage)) {
			if (damage > 0) {
				event.setAmount(damage);
			} else if (!this.isEventCanceled(event)) {
				this.cancelEvent(event);
			}
		}
	}

	// Applying Damage after Armor And other Calculations
	//Cancelable
	@SubscribeEvent
	public void applyDamageEvent(LivingDamageEvent event) {
		final DamageSource source = event.getSource();
		if (source.canHarmInCreative()) {
			return;
		}
		final EntityLivingBase attacked = event.getEntityLiving();
		EntityLivingBase attacker = null;
		if (source.getTrueSource() instanceof EntityLivingBase) {
			attacker = (EntityLivingBase) source.getTrueSource();
		}
		float damage = event.getAmount();

		// Handle Attacker
		if (attacker != null) {
			final EntityProperties prop = Capabilities.getEntityRace(attacker);
			if (prop != null) {
				// Handle Entity Abilities
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IAttackAbility)) {
							damage = ((IAttackAbility) handler).damageEntity(attacked, source, damage);
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}

				// Handle Entity Race
				damage = prop.getRaceProperties().damagedEntity(attacked, source, damage);
			}
		}

		// Handle Attacked
		if (damage > 0) {
			final EntityProperties prop = Capabilities.getEntityRace(attacked);
			if (prop != null) {
				// Handle Entity Abilities
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IAttackAbility)) {
							damage = ((IAttackAbility) handler).damaged(attacked, source, damage);
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}

				// Handle Entity Race
				damage = prop.getRaceProperties().isDamaged(source, damage);
			}
		}
		if ((event.getAmount() != damage)) {
			if (damage > 0) {
				event.setAmount(damage);
			} else if (!this.isEventCanceled(event)) {
				this.cancelEvent(event);
			}
		}
	}

	@SubscribeEvent
	public void onDeathEvent(LivingDeathEvent event) {
		final DamageSource source = event.getSource();
		final EntityLivingBase attacked = event.getEntityLiving();
		if ((attacked instanceof EntityPlayer) && attacked.world.isRemote) {
			return;
		}
		EntityLivingBase attacker = null;
		if (source.getTrueSource() instanceof EntityLivingBase) {
			attacker = (EntityLivingBase) source.getTrueSource();
		}
		boolean cancel = false;

		// Handle Attacker
		if (attacker != null) {
			final EntityProperties prop = Capabilities.getEntityRace(attacker);
			if (prop != null) {
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					if (cancel) {
						break;
					}
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IAttackAbility)) {
							if (((IAttackAbility) handler).killedEntity(attacked, source, cancel)) {
								cancel = true;
							}
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}
			}
		}

		final EntityProperties prop = Capabilities.getEntityRace(attacked);
		if ((prop != null) && !cancel) {
			// Handle Entity Abilities
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				if (cancel) {
					break;
				}
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IAttackAbility)) {
						if (((IAttackAbility) handler).died(attacked, source, cancel)) {
							cancel = true;
						}
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
		if (cancel && !this.isEventCanceled(event)) {
			this.cancelEvent(event);
		}
	}

	@SubscribeEvent
	public void experienceDropEvent(LivingExperienceDropEvent event) {
		final EntityLivingBase attacked = event.getEntityLiving();
		final EntityLivingBase attacker = event.getAttackingPlayer();
		final int originalExp = event.getOriginalExperience();
		final int originalDropped = event.getDroppedExperience();
		int droppedExp = originalDropped;
		if (attacker != null) {
			final EntityProperties prop = Capabilities.getEntityRace(attacker);
			if (prop != null) {
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IAttackAbility)) {
							droppedExp = ((IAttackAbility) handler).killedEntityExpDrop(attacked, originalExp, droppedExp);
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}
			}
		}
		if (originalDropped != droppedExp) {
			if (droppedExp > 0) {
				event.setDroppedExperience(droppedExp);
			} else if (!this.isEventCanceled(event)) {
				this.cancelEvent(event);
			}
		}

	}

	// Happens when an Entity Dies
	@SubscribeEvent
	public void ItemDropEvent(LivingDropsEvent event) {
		final EntityLivingBase attacked = event.getEntityLiving();
		final DamageSource source = event.getSource();
		final Entity attacker = source.getTrueSource();
		final int lootingLevel = event.getLootingLevel();
		final List<EntityItem> drops = event.getDrops();

		if (attacker instanceof EntityLivingBase) {
			final EntityProperties prop = Capabilities.getEntityRace(attacker);
			if (prop != null) {
				for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
					try {
						final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
						if ((handler != null) && (handler instanceof IAttackAbility)) {
							((IAttackAbility) handler).killedEntityItemDrops(attacked, source, lootingLevel, drops);
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}
			}
		}
	}

	//	// Happens after the Player Dies
	@SubscribeEvent
	public void onPlayerDropEvent(PlayerDropsEvent event) {
	}

	@SubscribeEvent
	public void healEvent(LivingHealEvent event) {
		final float amount = event.getAmount();
		final EntityLivingBase entity = event.getEntityLiving();
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		float finalAmount = amount;
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				try {
					final IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IHealAbility)) {
						finalAmount = ((IHealAbility) handler).onHeal(entity, finalAmount);
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
		if (amount != finalAmount) {
			if (finalAmount > 0) {
				event.setAmount(finalAmount);
			} else if (!this.isEventCanceled(event)) {
				this.cancelEvent(event);
			}
		}
	}
	//
	//	@SubscribeEvent
	//	public void playerAttackedEntity(AttackEntityEvent event) {
	//
	//	}

}
