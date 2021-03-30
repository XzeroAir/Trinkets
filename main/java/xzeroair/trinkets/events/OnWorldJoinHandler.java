package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.configsync.BlocklistSyncPacket;
import xzeroair.trinkets.network.configsync.PacketConfigSync;
import xzeroair.trinkets.network.vip.VipStatusPacket;
import xzeroair.trinkets.util.TrinketsConfig;

public class OnWorldJoinHandler {

	private boolean check, check2, check3 = false;

	@SubscribeEvent
	public void attachAttributes(EntityEvent.EntityConstructing event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			final EntityLivingBase entity = (EntityLivingBase) event.getEntity();
			final AbstractAttributeMap map = entity.getAttributeMap();

			map.registerAttribute(RaceAttribute.ENTITY_RACE);
			map.registerAttribute(JumpAttribute.Jump);
			map.registerAttribute(JumpAttribute.stepHeight);
		}
	}

	/*
	 * Fired on Logical Server Fired After EntityJoinedWorldEvent
	 */
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		if ((event.player.world != null)) {
			final EntityPlayer player = event.player;
			final boolean client = player.world.isRemote;

			// config Sync
			if (!client) {
				NetworkHandler.INSTANCE.sendTo(
						new PacketConfigSync(
								player,
								TrinketsConfig.SERVER.races.dragon.creative_flight,
								TrinketsConfig.SERVER.races.fairy.creative_flight,
								TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder,
								TrinketsConfig.SERVER.GUI.guiEnabled,
								TrinketsConfig.SERVER.races.fairy.creative_flight_speed,
								TrinketsConfig.SERVER.races.fairy.flight_speed,
								TrinketsConfig.SERVER.GUI.guiEnabled,
								TrinketsConfig.SERVER.GUI.guiSlotsRows,
								TrinketsConfig.SERVER.GUI.guiSlotsRowLength,
								TrinketsConfig.compat.artemislib,
								TrinketsConfig.compat.baubles,
								TrinketsConfig.compat.enhancedvisuals,
								TrinketsConfig.compat.morph,
								TrinketsConfig.compat.toughasnails,
								TrinketsConfig.compat.betterdiving
						), (EntityPlayerMP) player
				);

				final String[] configArray = TrinketsConfig.getBlockListArray(false);
				//				CallHelper.combineStringArray(configArray);
				String combinedArray = "";
				for (int i = configArray.length - 1; i >= 0; --i) {
					combinedArray = configArray[i] + ", " + combinedArray;
				}
				if (!combinedArray.isEmpty()) {
					final int hd = TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C001_HD;
					final int vd = TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C00_VD;
					NetworkHandler.INSTANCE.sendTo(new BlocklistSyncPacket(player, false, combinedArray, 0, hd, vd), (EntityPlayerMP) player);
				}
				VipStatus status = Capabilities.getVipStatus(player);
				if (status != null) {
					status.onUpdate();
					NetworkHandler.INSTANCE.sendTo(new VipStatusPacket(player, status), (EntityPlayerMP) player);
				}

				EntityProperties cap = Capabilities.getEntityRace(player);
				if (cap != null) {
					cap.getMagic().syncToManaHud();
				}
			}
			// end config Sync
		}
		check = true;
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {
		//		if ((event.player.world != null) && !event.player.world.isRemote) {
		//			final EntityPlayer player = event.player;
		//			RaceProperties cap = Capabilities.getEntityRace(player);
		//			if (cap != null) {
		//				if ((player.getRidingEntity() instanceof EntityMinecart) && (cap.isTransformed() == true)) {
		//					player.dismountRidingEntity();
		//				}
		//			}
		//		}
		check = false;
	}

	/**
	 * Send Capability Data To the player from Server, Because Client is Incorrect
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if ((event.getEntity() instanceof EntityPlayer) && !event.getEntity().isDead && (event.getEntity().world != null)) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final boolean client = player.world.isRemote;
			EntityProperties cap = Capabilities.getEntityRace(player);
			if ((cap != null) && !client) {
				cap.sendInformationToPlayer(player);
			}
		}
	}

	/**
	 * Runs Server Side Only, Runs After EntityjoinWorld
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		if ((event.player != null) && (event.player.getEntityWorld() != null)) {
			final EntityPlayer player = event.player;
			final boolean client = player.world.isRemote;
			EntityProperties cap = Capabilities.getEntityRace(player);
			if ((cap != null) && !client) {
				cap.sendInformationToPlayer(player);
			}
		}
	}

}
