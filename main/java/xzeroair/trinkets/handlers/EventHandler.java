package xzeroair.trinkets.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.SizeHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class EventHandler {

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		boolean client = player.world.isRemote;

		if (player.hasCapability(CapPro.sizeCapability, null)) {
			ICap cap = player.getCapability(CapPro.sizeCapability, null);

			if(TrinketHelper.baubleCheck(player, ModItems.small_ring)) {
				if(cap.getTrans() == true) {
					SizeHelper.fairyRing(player, cap);
				}
			}

			if(TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
				if(cap.getTrans() == true) {
					SizeHelper.dwarfRing(player, cap);
				}
			}

			if(cap.getTrans() == false) {
				if((player.stepHeight != 0.6f) && ((TrinketsConfig.SERVER.C01_Step_Height != false))){
					player.stepHeight = 0.6f;
				}
			}
			SizeHelper.eyeHeightHandler(player, cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
			if(cap.getSize() != cap.getTarget()) {
				SizeHelper.sizeHandler(player, cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
			}
		}
	}

	@SubscribeEvent
	public void makeNoise(PlaySoundAtEntityEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			boolean client = player.world.isRemote;
			if(TrinketHelper.baubleCheck(player, ModItems.small_ring) || TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
				if((event.getSound() == SoundEvents.BLOCK_STONE_STEP) || (event.getSound() == SoundEvents.BLOCK_GRASS_STEP) || (event.getSound() == SoundEvents.BLOCK_CLOTH_STEP) || (event.getSound() == SoundEvents.BLOCK_WOOD_STEP)
						|| (event.getSound() == SoundEvents.BLOCK_GRAVEL_STEP) || (event.getSound() == SoundEvents.BLOCK_SNOW_STEP) || (event.getSound() == SoundEvents.BLOCK_GLASS_STEP) || (event.getSound() == SoundEvents.BLOCK_METAL_STEP)
						|| (event.getSound() == SoundEvents.BLOCK_ANVIL_STEP) || (event.getSound() == SoundEvents.BLOCK_LADDER_STEP) || (event.getSound() == SoundEvents.BLOCK_SLIME_STEP)) {
					if(!client) {
						if(!event.getEntity().isSprinting()) {
							event.setVolume(0.0F);
						} else {
							event.setVolume(0.1F);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event){
//		if((event.getEntityLiving() instanceof EntityLiving) && (!(event.getEntityLiving() instanceof EntityPlayer)) && event.getEntity().hasCapability(CapPro.sizeCapability, null)) {
//			EntityLiving entity = (EntityLiving) event.getEntityLiving();
//			ICap cap = entity.getCapability(CapPro.sizeCapability, null);
//			if(cap.getTrans() == true) {
//				if(((entity.ticksExisted%400)==0)) {
////					cap.setTarget(100);
////					cap.setTrans(false);
////					if(entity.world.isRemote) {
//						NetworkHandler.INSTANCE.sendToServer(new MobCapDataMessage(cap.getSize(), false, 100, entity.width, entity.height, cap.getDefaultWidth(), cap.getDefaultHeight(), entity.getEntityId()));
//
//						NetworkHandler.INSTANCE.sendToAll(new MobCapDataMessage(cap.getSize(), false, 100, entity.width, entity.height, cap.getDefaultWidth(), cap.getDefaultHeight(), entity.getEntityId()));
////					}
//				}
//				SizeHelper.mobSize(entity, cap);
//			} else {
//				if(!(event.getEntityLiving() instanceof EntityPlayer)) {
////					System.out.println(cap.getSize());
//					if(entity.world.isRemote) {
////					System.out.println(cap.getDefaultHeight() + "   default");
////					System.out.println(cap.getHeight() + "   not default");
//					}
//				}
//			}
////			System.out.println(entity.width);
////			System.out.println(entity.height);
//			if(cap.getSize() != cap.getTarget()) {
//				SizeHelper.mobSize(entity, cap);
//				SizeHelper.sizeHandler(event.getEntity(), cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
//			}
//		}
	}

}
