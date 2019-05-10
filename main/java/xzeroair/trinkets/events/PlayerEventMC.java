package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.CapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class PlayerEventMC {

	@SubscribeEvent
	public void startTracking(PlayerEvent.StartTracking event) {
		if(event.getEntityPlayer() != null) {
			final EntityPlayer player = event.getEntityPlayer();
			final boolean client = player.world.isRemote;
			if((event.getTarget() != null) && (event.getTarget() instanceof EntityLivingBase) && event.getTarget().hasCapability(SizeCapPro.sizeCapability, null)) {
				final EntityLivingBase entity = (EntityLivingBase) event.getTarget();
				final ISizeCap cap = entity.getCapability(SizeCapPro.sizeCapability, null);
				if(entity instanceof EntityPlayer) {
					if(!client) {
						NetworkHandler.INSTANCE.sendTo(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), entity.getEyeHeight(), entity.getEntityId()), (EntityPlayerMP)player);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {
		if(event.getEntityPlayer().hasCapability(SizeCapPro.sizeCapability, null)) {
			final ISizeCap oldCap = event.getOriginal().getCapability(SizeCapPro.sizeCapability, null);
			final ISizeCap newCap = event.getEntityPlayer().getCapability(SizeCapPro.sizeCapability, null);
			if(event.isWasDeath()) {
				if((event.getOriginal().world.getGameRules().getBoolean("keepInventory") == true)) {
					if(TrinketHelper.baubleCheck(event.getOriginal(), ModItems.fairy_ring)) {
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
