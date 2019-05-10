package xzeroair.trinkets.util.eventhandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.Greater_inertia_stone;
import xzeroair.trinkets.items.fairy_ring;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class MovementHandler {

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event){
		if(event.getEntityLiving().world.isRemote && (event.getEntityLiving() instanceof EntityPlayer)){
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			fairy_ring.playerJump(player);
			Greater_inertia_stone.playerJump(player);
		}
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if((TrinketHelper.baubleCheck(player, ModItems.inertia_null_stone) || TrinketHelper.baubleCheck(player, ModItems.greater_inertia_stone))){
				event.setDamageMultiplier(0);
			}
			if(TrinketHelper.baubleCheck(player, ModItems.weightless_stone)) {
				event.setDamageMultiplier((float) player.motionY);
			}
		}
	}
}
