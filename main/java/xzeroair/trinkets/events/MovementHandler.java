package xzeroair.trinkets.events;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.util.TrinketsConfig;

public class MovementHandler extends EventBaseHandler {

	@SubscribeEvent
	public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
		final EntityPlayer player = event.getEntityPlayer();
		Capabilities.getEntityProperties(player, prop -> {
			if (!prop.isNormalSize()) {
				event.setCanceled(true);
			}
		});
	}

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		// TODO Add Config to disable attribute
		final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
		if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
			entity.motionY += (attribute.getAttributeValue() - 0.42F);
			if (entity.isSprinting()) {
				entity.motionX *= attribute.getAttributeValue() / 0.42F;
				entity.motionZ *= attribute.getAttributeValue() / 0.42F;
			}
		}
		//				entity.motionY = 0.368129F;
		Capabilities.getEntityProperties(entity, prop -> {
			if (TrinketsConfig.SERVER.misc.movement) {
				if (prop.getRaceHandler().isTransforming()) {
					entity.motionY = 0;
					return;
				}
			}
			prop.getRaceHandler().jump();
			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IJumpAbility)) {
						((IJumpAbility) ability).jump(entity);
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
		});
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final float baseDistance = event.getDistance();
		final float baseMultiplier = event.getDamageMultiplier();

		// ATTRIBUTE
		final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
		if ((attribute != null)) {
			final double value = attribute.getAttributeValue();
			final float multi = (float) (value / 0.42F);
			final float distance = (3F * (multi - 1));//(3F * 2F);
			event.setDistance(baseDistance - distance);
		}
		// END ATTRIBUTE

		boolean cancel = Capabilities.getEntityProperties(entity, false, (prop, bool) -> {
			float fallDistance = event.getDistance();
			float damageMultiplier = event.getDamageMultiplier();

			Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
			for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
				String key = entry.getKey();
				AbilityHolder value = entry.getValue();
				try {
					IAbilityInterface ability = value.getAbility();
					if ((ability instanceof IJumpAbility)) {
						final IJumpAbility fall = (IJumpAbility) ability;
						final float abilityDistance = fall.fallDistance(entity, fallDistance);
						if (fallDistance != abilityDistance) {
							fallDistance = abilityDistance;
						}
						final float abilityModifier = fall.fallDamageMultiplier(entity, damageMultiplier);
						if (damageMultiplier != abilityModifier) {
							damageMultiplier = abilityModifier;
						}
						final boolean abilityCancel = fall.fall(entity, fallDistance, damageMultiplier, bool);
						if (bool != abilityCancel) {
							bool = abilityCancel;
						}
					}
				} catch (Exception e) {
					Trinkets.log.error("Trinkets had an Error with Ability:" + key);
					e.printStackTrace();
				}
			}
			if (fallDistance != baseDistance) {
				event.setDistance(MathHelper.clamp(fallDistance, 0, fallDistance));
			}
			if (damageMultiplier != baseMultiplier) {
				event.setDamageMultiplier(MathHelper.clamp(damageMultiplier, 0, damageMultiplier));
			}

			prop.getRaceHandler().fall(event);

			return event.isCanceled() || (event.getDistance() <= 0) || (event.getDamageMultiplier() <= 0) || bool;
		});
		if (cancel) {
			this.cancelEvent(event);
		}
	}
}
