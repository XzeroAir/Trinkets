package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;

public class MovementHandler {

	@SubscribeEvent
	public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
		if (event.getEntityPlayer() != null) {
			EntityProperties cap = Capabilities.getEntityRace(event.getEntityPlayer());
			if ((cap != null) && !cap.isNormalHeight()) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event) {
		if ((event.getEntityLiving() != null)) {
			if (event.getEntityLiving().world.isRemote) {
				EntityLivingBase entity = event.getEntityLiving();
				final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
				if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
					entity.motionY += (attribute.getAttributeValue() - 0.42F);
					if (entity.isSprinting()) {
						entity.motionX *= attribute.getAttributeValue() / 0.42F;
						entity.motionZ *= attribute.getAttributeValue() / 0.42F;
					}
				}
				//				entity.motionY = 0.368129F;
				EntityProperties prop = Capabilities.getEntityRace(entity);
				if (prop != null) {
					prop.getRaceProperties().jump();
				}
			}
		}
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		if ((event.getEntityLiving() != null)) {
			EntityLivingBase entity = event.getEntityLiving();
			final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
			if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
				final float base = event.getDistance();
				final float distance = (float) (3F * (attribute.getAttributeValue() / 0.42F));//(3F * 2F);
				event.setDistance(base - distance);
			}
			EntityProperties prop = Capabilities.getEntityRace(entity);
			if (prop != null) {
				prop.getRaceProperties().fall(event);
			}
		}
	}
}
