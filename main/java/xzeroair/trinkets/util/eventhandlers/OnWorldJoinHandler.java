package xzeroair.trinkets.util.eventhandlers;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.entity.ai.EnderAiEdit;
import xzeroair.trinkets.entity.ai.EnderQueensKnightAI;
import xzeroair.trinkets.network.CapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;

public class OnWorldJoinHandler {

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		//		if((event.player.world != null) && !event.player.world.isRemote) {
		//			EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
		//			ICap cap = playerMP.getCapability(CapPro.sizeCapability, null);
		//			NetworkHandler.INSTANCE.sendTo(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), playerMP.getEyeHeight(), playerMP.getEntityId()), playerMP);
		//		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {
		if((event.player.world != null) && !event.player.world.isRemote) {
			EntityPlayer player = (EntityPlayer) event.player;
			ICap cap = player.getCapability(CapPro.sizeCapability, null);
			if((player.getRidingEntity() instanceof EntityMinecart) && (cap.getTrans() == true)) {
				player.dismountRidingEntity();
			}
		}
	}

	@SubscribeEvent
	public void entityConstructing(EntityConstructing event) {

	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		//		if(event.player instanceof EntityPlayer) {
		//			EntityPlayer player = (EntityPlayer) event.player;
		//			if(!player.world.isRemote) {
		//				EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
		//				ICap cap = playerMP.getCapability(CapPro.sizeCapability, null);
		//				NetworkHandler.INSTANCE.sendToAll(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), playerMP.getEyeHeight(), playerMP.getEntityId()));

		//				NetworkHandler.INSTANCE.sendTo(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), playerMP.getEyeHeight(), playerMP.getEntityId()), playerMP);
		//			}
		//		}
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if((event.getEntity() instanceof EntityPlayer) && event.getEntity().hasCapability(CapPro.sizeCapability, null)) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if(!player.world.isRemote) {
				EntityPlayerMP playerMP = (EntityPlayerMP) event.getEntity();
				ICap cap = playerMP.getCapability(CapPro.sizeCapability, null);
				NetworkHandler.INSTANCE.sendTo(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), playerMP.getEyeHeight(), playerMP.getEntityId()), playerMP);
			}
		}

		//Add Tiara AI to Enderman
		if (event.getEntity() instanceof EntityEnderman) {
			EntityEnderman ender = (EntityEnderman) event.getEntity();

			for (Object a : ender.targetTasks.taskEntries.toArray())
			{
				EntityAIBase ai = ((EntityAITaskEntry) a).action;
				if(ai.toString().startsWith("net.minecraft.entity.monster.EntityEnderman$AIFindPlayer")){
					ender.targetTasks.removeTask(ai);
				}
			}
			ender.targetTasks.addTask(1, new EnderAiEdit(ender));
			ender.targetTasks.addTask(2, new EnderQueensKnightAI(ender));
		}
	}

	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {

	}

}
