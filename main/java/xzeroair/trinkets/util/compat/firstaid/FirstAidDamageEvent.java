package xzeroair.trinkets.util.compat.firstaid;

import ichttt.mods.firstaid.api.event.FirstAidLivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.traits.abilities.IAbilityHandler;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;

public class FirstAidDamageEvent {

	/**
	 * This is Triggered on the
	 * net.minecraftforge.event.entity.living.LivingHurtEvent and Cancels the Normal
	 * Event
	 *
	 */
	@SubscribeEvent
	public void DamageEvent(FirstAidLivingDamageEvent event) {
		EntityProperties prop = Capabilities.getEntityRace(event.getEntityLiving());
		// Handle Entity Abilities
		boolean cancel = false;
		if (prop != null) {
			for (IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				if (cancel) {
					break;
				}
				try {
					IAbilityHandler handler = prop.getAbilityHandler().getAbilityInstance(ability);
					if ((handler != null) && (handler instanceof IFirstAidAbility)) {
						cancel = ((IFirstAidAbility) handler).firstAidHit(event.getEntityLiving(), event.getSource(), event.getUndistributedDamage(), cancel, event.getBeforeDamage(), event.getAfterDamage());
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with FirstAid Ability:" + ability.getName());
					e.printStackTrace();
				}
			}
		}
		if (cancel && event.isCancelable() && !event.isCanceled()) {
			event.setCanceled(true);
		}
		//		if (event.getEntityPlayer() instanceof EntityPlayer) {
		//			if (event.getSource().getTrueSource() instanceof EntityLiving) {
		//				final EntityPlayer player = event.getEntityPlayer();
		//				final AbstractPlayerDamageModel aidCap = player.getCapability(CapabilityExtendedHealthSystem.INSTANCE, null);
		//				if (TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDamageShield)) {
		//					String string = "Ouch!";
		//					//TODO Fix VIP
		//					VipStatus vip = Capabilities.getVipStatus(player);
		//					if (vip != null) {
		//						String quote = vip.getRandomQuote();
		//						if (!quote.isEmpty()) {
		//							string = quote;
		//						}
		//					}
		//					TextComponentString message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GOLD + string);
		//					final ItemStack stack = TrinketHelper.getAccessory(player, ModItems.trinkets.TrinketDamageShield);
		//					TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		//					if ((aidCap != null) && (iCap != null)) {
		//						if (TrinketsConfig.SERVER.Items.DAMAGE_SHIELD.damage_ignore) {
		//							if ((event.getAfterDamage().HEAD.currentHealth < 1) || (event.getAfterDamage().BODY.currentHealth < 1)) {
		//								if (iCap.StoredExp() == 0) {
		//									event.setCanceled(true);
		//								}
		//								if (event.isCanceled()) {
		//									if (TrinketsConfig.SERVER.Items.DAMAGE_SHIELD.special) {
		//										player.sendStatusMessage(message, false);
		//									}
		//									aidCap.scheduleResync();
		//								}
		//							}
		//						}
		//					}
		//				}
		//			}
		//		}
	}

}
