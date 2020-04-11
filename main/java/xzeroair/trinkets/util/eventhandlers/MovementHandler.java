package xzeroair.trinkets.util.eventhandlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.items.effects.EffectsTitanRing;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.EntityRaceHelper;

public class MovementHandler {

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event) {
		if (event.getEntityLiving().world.isRemote && (event.getEntityLiving() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if ((EntityRaceHelper.getRace(player).contentEquals("fairy_dew")) && !(TrinketHelper.AccessoryCheck(player, EffectsFairyRing.incompatible))) {
				if ((TrinketsConfig.SERVER.FAIRY_RING.step_height != false)) {
					player.motionY *= 0.5f;
				}
			}
			if ((EntityRaceHelper.getRace(player).contentEquals("titan_spirit")) && !(TrinketHelper.AccessoryCheck(player, EffectsTitanRing.incompatible))) {
				if (TrinketsConfig.SERVER.TITAN_RING.step_height != false) {
					player.motionY *= 1.5f;
				}
			}
		}
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		if ((event.getEntityLiving() != null) && event.getEntityLiving().world.isRemote) {
			final EntityLivingBase player = event.getEntityLiving();
			if ((EntityRaceHelper.getRace(player).contentEquals("titan_spirit")) && !(TrinketHelper.AccessoryCheck(player, EffectsTitanRing.incompatible))) {
				final float base = event.getDistance();
				final float distance = (3F * 2F);
				event.setDistance(base - distance);
			}
		}
	}
}
