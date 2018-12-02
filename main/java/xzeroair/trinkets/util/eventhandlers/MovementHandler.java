package xzeroair.trinkets.util.eventhandlers;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.handlers.BounceHandler;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class MovementHandler {

	@SubscribeEvent
	public void livingJump(LivingJumpEvent event){
		if(event.getEntityLiving().world.isRemote && (event.getEntityLiving() instanceof EntityPlayer)){
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if((TrinketHelper.baubleCheck(player, ModItems.small_ring)) && ((TrinketsConfig.SERVER.C01_Step_Height != false))){
				player.motionY = player.motionY*0.5f;
			}
		}
	}

	@SubscribeEvent
	public void livingFall(LivingFallEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			boolean isClient = player.world.isRemote;
			if(TrinketHelper.baubleCheck(player, ModItems.small_ring)) {
				if((MathHelper.cos(event.getDistance()) < 0.6f) && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
					//					if(player.motionY < 0.01f) {
					//					event.setDamageMultiplier(MathHelper.cos(event.getDistance()));
					//					}
				}
			}

			//The Section of Code below was barrowed from Tinkers Construct Slime Boots...

			if(TrinketHelper.baubleCheck(player, ModItems.rubber_stone)) {
				if(!player.isSneaking() && (event.getDistance() > 3)) {
					if(!(TrinketHelper.baubleCheck(player, ModItems.inertia_stone) || TrinketHelper.baubleCheck(player, ModItems.great_inertia_stone))){
						event.setDamageMultiplier(0.2f);
					}
					player.fallDistance = 0;
					if(isClient) {
						player.motionY *= -0.9;
						player.isAirBorne = true;
						player.onGround = false;
						double f = 0.91d + 0.04d;
						player.motionX /= f;
						player.motionZ /= f;
					}
					else {
						event.setCanceled(false);
					}
					player.playSound(SoundEvents.ENTITY_PLAYER_BREATH, 1f, 1f);
					BounceHandler.addBounceHandler(player, player.motionY);
				}
				else if(!isClient && player.isSneaking()) {
					if(!(TrinketHelper.baubleCheck(player, ModItems.inertia_stone) || TrinketHelper.baubleCheck(player, ModItems.great_inertia_stone))){
						event.setDamageMultiplier(0.2f);
					}
				}
			}
			if((TrinketHelper.baubleCheck(player, ModItems.inertia_stone) || TrinketHelper.baubleCheck(player, ModItems.great_inertia_stone))){
				event.setDamageMultiplier(0);
			}
		}
	}
}
