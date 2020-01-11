package xzeroair.trinkets.util.eventhandlers;

import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CombatHandler {

	@SubscribeEvent
	public void TargetEvent(LivingSetAttackTargetEvent event) {
	}

	@SubscribeEvent
	public void experienceDropEvent(LivingExperienceDropEvent event) {
	}

	@SubscribeEvent
	public void ItemDropEvent(LivingDropsEvent event) {
	}

	@SubscribeEvent
	public void deathEvent(LivingDamageEvent event) {
	}

	@SubscribeEvent
	public void HurtEvent(LivingHurtEvent event) {
	}
}
