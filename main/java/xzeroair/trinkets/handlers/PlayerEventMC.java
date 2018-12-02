package xzeroair.trinkets.handlers;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.CapDataMessage;
import xzeroair.trinkets.network.MobCapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PacketBaubleSync;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class PlayerEventMC {

	@SubscribeEvent
	public void startTracking(PlayerEvent.StartTracking event) {
		if(event.getEntityPlayer() != null) {
			EntityPlayer player = event.getEntityPlayer();
			boolean client = player.world.isRemote;
			if((event.getTarget() != null) && (event.getTarget() instanceof EntityLivingBase) && event.getTarget().hasCapability(CapPro.sizeCapability, null)) {
				EntityLivingBase entity = (EntityLivingBase) event.getTarget();
				ICap cap = entity.getCapability(CapPro.sizeCapability, null);
				if(entity instanceof EntityPlayer) {
					IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer)entity);
					for(int i = 0; i < baubles.getSlots(); i++) {
						if(!baubles.getStackInSlot(i).isEmpty()) {
							if((baubles.getStackInSlot(i).getItem() == ModItems.small_ring) || (baubles.getStackInSlot(i).getItem() == ModItems.dwarf_ring)) {
								if(!client) {
									NetworkHandler.INSTANCE.sendTo(new PacketBaubleSync((EntityPlayer)entity, i), (EntityPlayerMP)player);
								}
							}
						}
					}
					if(!client) {
						NetworkHandler.INSTANCE.sendTo(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), entity.getEyeHeight(), entity.getEntityId()), (EntityPlayerMP)player);
					}
				} else {
					if(!client) {
						NetworkHandler.INSTANCE.sendTo(new MobCapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), entity.width, entity.height, cap.getDefaultWidth(), cap.getDefaultHeight(), entity.getEntityId()), (EntityPlayerMP)player);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {
		if(event.getEntityPlayer().hasCapability(CapPro.sizeCapability, null)) {
			final ICap oldCap = event.getOriginal().getCapability(CapPro.sizeCapability, null);
			final ICap newCap = event.getEntityPlayer().getCapability(CapPro.sizeCapability, null);
			if(event.isWasDeath()) {
				if((event.getOriginal().world.getGameRules().getBoolean("keepInventory") == true)) {
					if(TrinketHelper.baubleCheck(event.getOriginal(), ModItems.small_ring)) {
						if ((newCap != null) && (oldCap != null)) {
							newCap.setTrans(oldCap.getTrans());
							newCap.setTarget(oldCap.getTarget());
							newCap.setSize(oldCap.getSize());
							newCap.setWidth(oldCap.getWidth());
							newCap.setHeight(oldCap.getHeight());
							newCap.setDefaultWidth(oldCap.getDefaultWidth());
							newCap.setDefaultHeight(oldCap.getDefaultHeight());
							event.getEntityPlayer().eyeHeight = event.getOriginal().eyeHeight;
						}
					}
				}
			}
		}
	}

}
