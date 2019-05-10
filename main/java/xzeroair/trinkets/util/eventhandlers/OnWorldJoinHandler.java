package xzeroair.trinkets.util.eventhandlers;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.entity.ai.EnderAiEdit;
import xzeroair.trinkets.entity.ai.EnderQueensKnightAI;
import xzeroair.trinkets.items.Greater_inertia_stone;
import xzeroair.trinkets.items.Inertia_null_stone;
import xzeroair.trinkets.items.dragons_eye;
import xzeroair.trinkets.items.fairy_ring;
import xzeroair.trinkets.items.sea_stone;
import xzeroair.trinkets.network.CapDataMessage;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.PacketConfigSync;
import xzeroair.trinkets.util.TrinketsConfig;

public class OnWorldJoinHandler {

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		if((event.player.world != null)) {
			final EntityPlayer player = event.player;
			final boolean client = player.world.isRemote;
			//			if(!client && (player.getCapability(SizeCapPro.sizeCapability, null) != null)) {
			if(!client) {
				//				final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
				NetworkHandler.INSTANCE.sendTo(new PacketConfigSync(player, TrinketsConfig.SERVER.C04_DE_Chests), (EntityPlayerMP) player);
				//				NetworkHandler.INSTANCE.sendTo(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), player.getEyeHeight(), player.getEntityId()), (EntityPlayerMP) player);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {
		if((event.player.world != null) && !event.player.world.isRemote) {
			final EntityPlayer player = event.player;
			if((player.getCapability(SizeCapPro.sizeCapability, null) != null)) {
				final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
				if((player.getRidingEntity() instanceof EntityMinecart) && (cap.getTrans() == true)) {
					player.dismountRidingEntity();
				}
			}
			dragons_eye.onPlayerLogout(player);
			fairy_ring.onPlayerLogout(player);
			sea_stone.playerLogout(player);
			Inertia_null_stone.playerLogout(player);
			Greater_inertia_stone.playerLogout(player);
		}
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if((event.getEntity() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final boolean client = player.world.isRemote;
			if(!client && (player.getCapability(SizeCapPro.sizeCapability, null) != null)) {
				final EntityPlayerMP playerMP = (EntityPlayerMP) event.getEntity();
				final ISizeCap cap = playerMP.getCapability(SizeCapPro.sizeCapability, null);
				NetworkHandler.INSTANCE.sendTo(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), playerMP.getEyeHeight(), playerMP.getEntityId()), playerMP);
			}
		}

		//Add Tiara AI to Enderman
		if (event.getEntity() instanceof EntityEnderman) {
			final EntityEnderman ender = (EntityEnderman) event.getEntity();

			for (final Object a : ender.targetTasks.taskEntries.toArray())
			{
				final EntityAIBase ai = ((EntityAITaskEntry) a).action;
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
		if((event.player != null)) {
			final EntityPlayer player = event.player;
			final boolean client = player.world.isRemote;
			if(!client && (player.getCapability(SizeCapPro.sizeCapability, null) != null)) {
				final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
				NetworkHandler.INSTANCE.sendTo(new CapDataMessage(cap.getSize(), cap.getTrans(), cap.getTarget(), cap.getWidth(), cap.getHeight(), cap.getDefaultWidth(), cap.getDefaultHeight(), player.getEyeHeight(), player.getEntityId()), (EntityPlayerMP) player);
			}
		}
	}

}
