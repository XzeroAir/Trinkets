package xzeroair.trinkets.util.compat.firstaid;

import java.util.Map;
import java.util.Map.Entry;

import ichttt.mods.firstaid.api.event.FirstAidLivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.events.EventBaseHandler;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;

public class FirstAidDamageEvent extends EventBaseHandler {

	/**
	 * This is Triggered on the
	 * net.minecraftforge.event.entity.living.LivingHurtEvent and Cancels the Normal
	 * Event
	 *
	 */
	@SubscribeEvent
	public void DamageEvent(FirstAidLivingDamageEvent event) {
		Capabilities.getEntityProperties(event.getEntityLiving(), prop -> {
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IFirstAidAbility)) {
						boolean cancel = ((IFirstAidAbility) ability).firstAidHit(event.getEntityLiving(), event.getSource(), event.getUndistributedDamage(), event.getBeforeDamage(), event.getAfterDamage());
						if (cancel) {
							this.cancelEvent(event);
							break;
						}
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
		});
	}

}
