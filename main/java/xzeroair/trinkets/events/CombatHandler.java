package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;

public class CombatHandler {

	@SubscribeEvent
	public void arrowLooseEvent(ArrowLooseEvent event) {
		//only runs when item is actually used - Useless Event for Bows
	}

	@SubscribeEvent
	public void arrowNockEvent(ArrowNockEvent event) {
		EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		if (prop != null) {
			prop.getRaceProperties().bowNocked(event);
		}
	}

	@SubscribeEvent
	public void ArrowImpactEvent(ProjectileImpactEvent.Arrow event) {
	}

	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
		if (event.getTarget() != null) {
			EntityProperties prop = Capabilities.getEntityRace(event.getTarget());
			if (prop != null) {
				prop.getRaceProperties().targetedByEnemy(event.getEntityLiving());
			}
		}
	}

	@SubscribeEvent
	public void experienceDropEvent(LivingExperienceDropEvent event) {
	}

	@SubscribeEvent
	public void ItemDropEvent(LivingDropsEvent event) {
	}

	@SubscribeEvent
	public void knockbackEvent(LivingKnockBackEvent event) {

	}

	@SubscribeEvent
	public void healEvent(LivingHealEvent event) {

	}

	@SubscribeEvent
	public void deathEvent(LivingDamageEvent event) {
	}

	@SubscribeEvent
	public void HurtEvent(LivingHurtEvent event) {
		if (event.getEntityLiving() != null) {
			EntityLivingBase e = event.getEntityLiving();
			if (event.getSource() != null) {
				if ((event.getSource().getTrueSource() instanceof EntityLivingBase) && event.getSource().getTrueSource().hasCapability(Capabilities.ENTITY_RACE, null)) {
					EntityProperties attacker = Capabilities.getEntityRace((EntityLivingBase) event.getSource().getTrueSource());
					if (attacker != null) {
						float dmg = attacker.getRaceProperties().hurtEntity(e, event.getSource(), event.getAmount());
						if (dmg > 0F) {
							event.setAmount(dmg);
						}
					}
				}
			}
			EntityProperties prop = Capabilities.getEntityRace(e);
			if (prop != null) {
				float dmg = prop.getRaceProperties().isHurt(event.getSource(), event.getAmount());
				if (dmg > 0F) {
					event.setAmount(dmg);
				}
			}
		}
	}
}
