package xzeroair.trinkets.events;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.*;

public class CombatHandler extends EventBaseHandler {

	@SubscribeEvent
	public void arrowLooseEvent(ArrowLooseEvent event) {
		//only runs when item is actually used - Useless Event for Bows
		// Apparently it Works now? maybe another mod was Breaking it? either that or I fixed whatever trash code of mine that was breaking it
		Capabilities.getEntityProperties(
				event.getEntityLiving(), prop -> {
					//					int charge = 72000 - event.getDuration();
					try {
						prop.getRaceHandler().bowUsed(event);
					} catch (final Exception e) {
						e.printStackTrace();
					}
					// Handle Entity Abilities

					if (!event.isCanceled()) {
						Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
						for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
							String key = entry.getKey();
							AbilityHolder value = entry.getValue();
							if (event.isCanceled()) {
								break;
							}
							try {
								IAbilityInterface ability = value.getAbility();
								if ((ability instanceof IBowAbility)) {
									((IBowAbility) ability).looseArrow(event);
								}
							} catch (Exception e) {
								Trinkets.log.error("Trinkets had an Error with Ability:" + key);
								e.printStackTrace();
							}
						}
					}
				}
		);
	}

	@SubscribeEvent
	public void arrowNockEvent(ArrowNockEvent event) {
		Capabilities.getEntityProperties(event.getEntityLiving(), prop -> {
			try {
				prop.getRaceHandler().bowNocked(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IBowAbility)) {
						((IBowAbility) ability).knockArrow(event);
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
		});
	}

	@SubscribeEvent
	public void ArrowImpactEvent(ProjectileImpactEvent.Arrow event) {
		if (event.getArrow().shootingEntity instanceof EntityLivingBase) {
			Capabilities.getEntityProperties(
					event.getArrow().shootingEntity, prop -> {
						// Handle Entity Abilities
						Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
						for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
							String key = entry.getKey();
							AbilityHolder value = entry.getValue();
							if (event.isCanceled()) {
								break;
							}
							try {
								IAbilityInterface ability = value.getAbility();
								if ((ability instanceof IBowAbility)) {
									((IBowAbility) ability).arrowImpact(event);
								}
							} catch (Exception e) {
								Trinkets.log.error("Trinkets had an Error with Ability:" + key);
								e.printStackTrace();
							}
						}
					}
			);
		}
	}

	@SubscribeEvent
	public void onStruckByLightningEvent(EntityStruckByLightningEvent event) {
		if (!(event.getEntity() instanceof EntityLivingBase)) {
			return;
		}
		final EntityLivingBase entity = (EntityLivingBase) event.getEntity();
		if (!entity.isEntityAlive()) {
			return;
		}
		Capabilities.getEntityProperties(entity, prop -> {
			boolean cancel = false;
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder holder = entry.getValue();
				try {
					final IAbilityInterface handler = holder.getAbility();
					if (handler instanceof ILightningStrikeAbility) {
						cancel = ((ILightningStrikeAbility) handler).onStruckByLightning(entity, cancel);
					}
				} catch (final Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			if (cancel) {
				event.setResult(Event.Result.DENY);
			}
		});
	}

	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
		EntityLivingBase enemy = event.getEntityLiving();
		Capabilities.getEntityProperties(event.getTarget(), prop -> {
			prop.getRaceHandler().targetedByEnemy(enemy);
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				if (enemy instanceof EntityLiving) {
					if (((EntityLiving) enemy).getAttackTarget() == null) {
						break;
					}
				}
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IAttackAbility)) {
						((IAttackAbility) ability).targetedByEnemy(enemy);
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
		});
	}

	// First Attempt at Attack
	@SubscribeEvent
	public void attackEvent(LivingAttackEvent event) {
		final DamageSource source = event.getSource();
		if (source.canHarmInCreative())
			return;
		final float damage = event.getAmount();
		final EntityLivingBase attacked = event.getEntityLiving();
		if (source.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
			// Handle Attacker
			Capabilities.getEntityProperties(
					attacker, prop -> {
						boolean isCanceled = !prop.getRaceHandler().attackedEntity(attacked, source, damage);
						if (!isCanceled) {
							Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
							for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
								String key = entry.getKey();
								AbilityHolder value = entry.getValue();
								try {
									IAbilityInterface ability = value.getAbility();
									if ((ability instanceof IAttackAbility)) {
										isCanceled = ((IAttackAbility) ability).attackEntity(attacked, source, damage, isCanceled);
										if (isCanceled) {
											break;
										}
									}
								} catch (Exception e) {
									Trinkets.log.error("Trinkets had an Error with Ability:" + key);
									e.printStackTrace();
								}
							}
						}
						if (isCanceled) {
							this.cancelEvent(event);
						}
					}
			);
		}

		if (!event.isCanceled()) {
			// Handle Attacked
			Capabilities.getEntityProperties(attacked, prop -> {
				// Handle Entity Abilities
				boolean isCanceled = !prop.getRaceHandler().isAttacked(source, damage);
				if (!isCanceled) {
					Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
					for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
						String key = entry.getKey();
						AbilityHolder value = entry.getValue();
						try {
							IAbilityInterface ability = value.getAbility();
							if ((ability instanceof IAttackAbility)) {
								isCanceled = ((IAttackAbility) ability).attacked(attacked, source, damage, isCanceled);
								if (isCanceled) {
									break;
								}
							}
						} catch (Exception e) {
							Trinkets.log.error("Trinkets had an Error with Ability:" + key);
							e.printStackTrace();
						}
					}
				}
				if (isCanceled) {
					this.cancelEvent(event);
				}
			});
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

	//	@SubscribeEvent
	//	public void knockbackEvent(LivingKnockBackEvent event) {
	//	}

	// Started Attack, Calculating Damage
	//Cancelable
	@SubscribeEvent
	public void HurtEvent(LivingHurtEvent event) {
		final DamageSource source = event.getSource();
		float damage = event.getAmount();
		if (source.canHarmInCreative() || (damage == 0))
			return;
		final EntityLivingBase attacked = event.getEntityLiving();

		if (source.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
			// Handle Attacker
			damage = Capabilities.getEntityProperties(
					attacker, damage, (prop, dmg) -> {
						dmg = prop.getRaceHandler().hurtEntity(attacked, source, dmg);
						if (dmg > 0) {
							Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
							for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
								String key = entry.getKey();
								AbilityHolder value = entry.getValue();
								try {
									IAbilityInterface ability = value.getAbility();
									if ((ability instanceof IAttackAbility)) {
										dmg = ((IAttackAbility) ability).hurtEntity(attacked, source, dmg);
										if (dmg <= 0) {
											break;
										}
									}
								} catch (Exception e) {
									Trinkets.log.error("Trinkets had an Error with Ability:" + key);
									e.printStackTrace();
								}
							}
						}
						return dmg;
					}
			);
		}

		// Handle Attacked
		if (damage > 0) {
			damage = Capabilities.getEntityProperties(
					attacked, damage, (prop, dmg) -> {
						dmg = prop.getRaceHandler().isHurt(source, dmg);
						if (dmg > 0) {
							Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
							for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
								String key = entry.getKey();
								AbilityHolder value = entry.getValue();
								try {
									IAbilityInterface ability = value.getAbility();
									if ((ability instanceof IAttackAbility)) {
										dmg = ((IAttackAbility) ability).hurt(attacked, source, dmg);
										if (dmg <= 0) {
											break;
										}
									}
								} catch (Exception e) {
									Trinkets.log.error("Trinkets had an Error with Ability:" + key);
									e.printStackTrace();
								}
							}
						}
						return dmg;
					}
			);
		}
		if (damage <= 0) {
			this.cancelEvent(event);
		} else {
			event.setAmount(damage);
		}
	}

	// Applying Damage after Armor And other Calculations
	//Cancelable
	@SubscribeEvent
	public void applyDamageEvent(LivingDamageEvent event) {
		final DamageSource source = event.getSource();
		float damage = event.getAmount();
		if (source.canHarmInCreative() || (damage == 0))
			return;
		final EntityLivingBase attacked = event.getEntityLiving();

		if (source.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
			// Handle Attacker
			damage = Capabilities.getEntityProperties(
					attacker, damage, (prop, dmg) -> {
						dmg = prop.getRaceHandler().damagedEntity(attacked, source, dmg);
						if (dmg > 0) {
							Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
							for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
								String key = entry.getKey();
								AbilityHolder value = entry.getValue();
								try {
									IAbilityInterface ability = value.getAbility();
									if ((ability instanceof IAttackAbility)) {
										dmg = ((IAttackAbility) ability).damageEntity(attacked, source, dmg);
										if (dmg <= 0) {
											break;
										}
									}
								} catch (Exception e) {
									Trinkets.log.error("Trinkets had an Error with Ability:" + key);
									e.printStackTrace();
								}
							}
						}
						return dmg;
					}
			);
		}

		// Handle Attacked
		if (damage > 0) {
			damage = Capabilities.getEntityProperties(
					attacked, damage, (prop, dmg) -> {
						dmg = prop.getRaceHandler().isDamaged(source, dmg);
						if (dmg > 0) {
							Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
							for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
								String key = entry.getKey();
								AbilityHolder value = entry.getValue();
								try {
									IAbilityInterface ability = value.getAbility();
									if ((ability instanceof IAttackAbility)) {
										dmg = ((IAttackAbility) ability).damaged(attacked, source, dmg);
										if (dmg <= 0) {
											break;
										}
									}
								} catch (Exception e) {
									Trinkets.log.error("Trinkets had an Error with Ability:" + key);
									e.printStackTrace();
								}
							}
						}
						return dmg;
					}
			);
		}
		if (damage <= 0) {
			this.cancelEvent(event);
		} else {
			event.setAmount(damage);
		}
	}

	@SubscribeEvent
	public void onDeathEvent(LivingDeathEvent event) {
		final DamageSource source = event.getSource();
		final EntityLivingBase attacked = event.getEntityLiving();
		if ((attacked instanceof EntityPlayer) && attacked.world.isRemote)
			return;
		if (source.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
			// Handle Attacker
			Capabilities.getEntityProperties(
					attacker, prop -> {
						//						prop.getRaceHandler().
						Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
						for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
							String key = entry.getKey();
							AbilityHolder value = entry.getValue();
							try {
								IAbilityInterface ability = value.getAbility();
								if ((ability instanceof IAttackAbility)) {
									((IAttackAbility) ability).killedEntity(attacked, source);
								}
							} catch (Exception e) {
								Trinkets.log.error("Trinkets had an Error with Ability:" + key);
								e.printStackTrace();
							}
						}
					}
			);
		}

		Capabilities.getEntityProperties(
				attacked, prop -> {
					//						prop.getRaceHandler().
					boolean notDead = false;
					Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
					for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
						String key = entry.getKey();
						AbilityHolder value = entry.getValue();
						try {
							IAbilityInterface ability = value.getAbility();
							if ((ability instanceof IAttackAbility)) {
								notDead = ((IAttackAbility) ability).died(attacked, source, notDead);
								if (notDead) {
									break;
								}
							}
						} catch (Exception e) {
							Trinkets.log.error("Trinkets had an Error with Ability:" + key);
							e.printStackTrace();
						}
					}
					if (notDead) {
						this.cancelEvent(event);
					}
				}
		);
	}

	@SubscribeEvent
	public void experienceDropEvent(LivingExperienceDropEvent event) {
		final EntityLivingBase attacked = event.getEntityLiving();
		try {
			NBTTagCompound nbt = attacked.getEntityData();
			if (nbt != null) {
				if (nbt.hasKey("xat:summoned")) {
					event.setDroppedExperience(0);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		final EntityLivingBase attacker = event.getAttackingPlayer();
		Capabilities.getEntityProperties(attacker, prop -> {
			final int originalExp = event.getOriginalExperience();
			final int originalDropped = event.getDroppedExperience();
			int droppedExp = originalDropped;
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IAttackAbility)) {
						droppedExp = ((IAttackAbility) ability).killedEntityExpDrop(attacked, originalExp, droppedExp);
						if (droppedExp <= 0) {
							break;
						}
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			if (originalDropped != droppedExp) {
				if (droppedExp > 0) {
					event.setDroppedExperience(droppedExp);
				} else if (!this.isEventCanceled(event)) {
					this.cancelEvent(event);
				}
			}
		});
	}

	// Happens when an Entity Dies
	@SubscribeEvent
	public void ItemDropEvent(LivingDropsEvent event) {
		final EntityLivingBase attacked = event.getEntityLiving();
		try {
			NBTTagCompound nbt = attacked.getEntityData();
			if (nbt != null) {
				if (nbt.hasKey("xat:summoned")) {
					if (!event.getDrops().isEmpty()) {
						event.getDrops().clear();
					}
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		final DamageSource source = event.getSource();
		if (source.getTrueSource() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) source.getTrueSource();
			Capabilities.getEntityProperties(attacker, prop -> {
				Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
				for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
					String key = entry.getKey();
					AbilityHolder value = entry.getValue();
					try {
						IAbilityInterface ability = value.getAbility();
						if ((ability instanceof IAttackAbility)) {
							((IAttackAbility) ability).killedEntityItemDrops(attacked, source, event.getLootingLevel(), event.getDrops());
						}
					} catch (Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + key);
						e.printStackTrace();
					}
				}
			});
		}
	}

	//	// Happens after the Player Dies
	//	@SubscribeEvent(priority = EventPriority.LOW)
	//	public void onPlayerDropEvent(PlayerDropsEvent event) {
	//
	//	}

	@SubscribeEvent
	public void healEvent(LivingHealEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		Capabilities.getEntityProperties(entity, prop -> {
			final float amount = event.getAmount();
			float finalAmount = amount;
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IHealAbility)) {
						finalAmount = ((IHealAbility) ability).onHeal(entity, finalAmount);
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			finalAmount = prop.getRaceHandler().onHeal(finalAmount);
			if (amount != finalAmount) {
				if (finalAmount > 0) {
					event.setAmount(finalAmount);
				} else if (!this.isEventCanceled(event)) {
					this.cancelEvent(event);
				}
			}
		});
	}
	//
	//	@SubscribeEvent
	//	public void playerAttackedEntity(AttackEntityEvent event) {
	//
	//	}

}
