package xzeroair.trinkets.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.Greater_inertia_stone;
import xzeroair.trinkets.items.Inertia_null_stone;
import xzeroair.trinkets.items.sea_stone;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.helpers.SizeHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class EventHandler {

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		final EntityPlayer player = event.player;

		if (player.hasCapability(SizeCapPro.sizeCapability, null)) {
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);

			if((event.phase == Phase.END)) {
				sea_stone.playerTick(player);
				Inertia_null_stone.playerTick(player);
				Greater_inertia_stone.playerTick(player);
			}

			if(TrinketHelper.baubleCheck(player, ModItems.fairy_ring)) {
				if(cap.getTrans() == true) {
					//					if(!(Loader.isModLoaded("artemislib"))) {
					SizeHelper.fairyRing(player, cap);
					//					}
				}
			}

			if(TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
				if(cap.getTrans() == true) {
					if(!(Loader.isModLoaded("artemislib"))) {
						SizeHelper.dwarfRing(player, cap);
					}
				}
			}

			if(cap.getTrans() == false) {
				if((player.stepHeight != 0.6f) && ((TrinketsConfig.SERVER.C01_Step_Height != false))){
					player.stepHeight = 0.6f;
				}
				if(cap.getSize() == cap.getTarget()) {
					if((Loader.isModLoaded("artemislib"))) {
						SizeAttribute.removeModifier(player);
					}
				}
			}
			SizeHelper.eyeHeightHandler(player, cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
			if(cap.getSize() != cap.getTarget()) {
				SizeHelper.sizeHandler(cap.getTrans(), cap.getTarget(), 100, cap.getSize(), cap);
			}
		}
	}

	@SubscribeEvent
	public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
		if(event.getEntityPlayer() != null) {
			if(TrinketHelper.baubleCheck(event.getEntityPlayer(), ModItems.fairy_ring) || TrinketHelper.baubleCheck(event.getEntityPlayer(), ModItems.dwarf_ring)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void makeNoise(PlaySoundAtEntityEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final boolean client = player.world.isRemote;
			if(TrinketHelper.baubleCheck(player, ModItems.fairy_ring) || TrinketHelper.baubleCheck(player, ModItems.dwarf_ring)) {
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
}
