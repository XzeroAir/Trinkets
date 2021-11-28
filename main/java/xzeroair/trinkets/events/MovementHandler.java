package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;

public class MovementHandler {

	// TODO Crashed because Unresolved Compilation Problem from prop.isNormalHeight(). Possible Random Crash?
	@SubscribeEvent
	public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
		if (event.getEntityPlayer() != null) {
			final EntityProperties cap = Capabilities.getEntityRace(event.getEntityPlayer());
			if ((cap != null) && !cap.isNormalHeight()) {
				if (event.isCancelable() && !event.isCanceled()) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		if (entity.world.isRemote) {
			final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
			if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
				entity.motionY += (attribute.getAttributeValue() - 0.42F);
				if (entity.isSprinting()) {
					entity.motionX *= attribute.getAttributeValue() / 0.42F;
					entity.motionZ *= attribute.getAttributeValue() / 0.42F;
				}
			}
		}
		//				entity.motionY = 0.368129F;
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			prop.getRaceProperties().jump();
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				if (ability instanceof IJumpAbility) {
					final IJumpAbility jump = (IJumpAbility) ability;
					try {
						jump.jump(entity);
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		final EntityLivingBase entity = event.getEntityLiving();
		final float baseDistance = event.getDistance();
		final float baseMultiplier = event.getDamageMultiplier();

		// ATTRIBUTE
		final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
		if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
			final float distance = (float) (3F * (attribute.getAttributeValue() / 0.42F));//(3F * 2F);
			event.setDistance(baseDistance - distance);
		}
		// END ATTRIBUTE

		float fallDistance = event.getDistance();
		float damageMultiplier = event.getDamageMultiplier();
		boolean cancel = false;
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		if (prop != null) {
			for (final IAbilityInterface ability : prop.getAbilityHandler().getAbilitiesList()) {
				if (ability instanceof IJumpAbility) {
					final IJumpAbility fall = (IJumpAbility) ability;
					try {
						final float abilityDistance = fall.fallDistance(entity, fallDistance);
						if (fallDistance != abilityDistance) {
							fallDistance = abilityDistance;
						}
						final float abilityModifier = fall.fallDamageMultiplier(entity, damageMultiplier);
						if (damageMultiplier != abilityModifier) {
							damageMultiplier = abilityModifier;
						}
						final boolean abilityCancel = fall.fall(entity, fallDistance, damageMultiplier, cancel);
						if (cancel != abilityCancel) {
							cancel = abilityCancel;
						}
					} catch (final Exception e) {
						Trinkets.log.error("Trinkets had an Error with Ability:" + ability.getName());
						e.printStackTrace();
					}
				}
			}
			// TODO ADD RACE FALL HERE

			if (fallDistance != baseDistance) {
				event.setDistance(MathHelper.clamp(fallDistance, 0, fallDistance));
			}
			if (damageMultiplier != baseMultiplier) {
				event.setDamageMultiplier(MathHelper.clamp(damageMultiplier, 0, damageMultiplier));
			}
			if (cancel && event.isCancelable() && !event.isCanceled()) {
				event.setCanceled(true);
			}
		}
	}
}
