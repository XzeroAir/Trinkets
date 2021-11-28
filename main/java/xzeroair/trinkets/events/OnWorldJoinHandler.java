package xzeroair.trinkets.events;

import java.util.Map;

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
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.MagicAttributes;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.configsync.PacketConfigSync;
import xzeroair.trinkets.util.TrinketsConfig;

public class OnWorldJoinHandler {

	private boolean check, check2;
	private final boolean check3 = false;

	@SubscribeEvent
	public void attachAttributes(EntityEvent.EntityConstructing event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			final EntityLivingBase entity = (EntityLivingBase) event.getEntity();
			final AbstractAttributeMap map = entity.getAttributeMap();

			map.registerAttribute(RaceAttribute.ENTITY_RACE);
			map.registerAttribute(JumpAttribute.Jump);
			map.registerAttribute(JumpAttribute.stepHeight);
			map.registerAttribute(MagicAttributes.regen);
		}
	}

	/*
	 * Fired on Logical Server Fired After EntityJoinedWorldEvent
	 */
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		final EntityPlayer player = event.player;
		if ((event.player.world != null) && (player instanceof EntityPlayerMP)) {
			final EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
			final boolean client = playerMP.world.isRemote;
			// config Sync
			if (!client) {
				Trinkets.log.info("Syncing Config to " + playerMP.getName());
				final Map<String, String> configMap = TrinketsConfig.writeConfigMap();
				NetworkHandler.sendTo(new PacketConfigSync(configMap), playerMP);

				//				final VipStatus status = Capabilities.getVipStatus(player);
				//				if (status != null) {
				//					NetworkHandler.sendTo(new VipStatusPacket(player, status), (EntityPlayerMP) player);
				//				}
				final EntityProperties cap = Capabilities.getEntityRace(player);
				if ((cap != null)) {
					cap.sendInformationToPlayer(player);
				}
				final MagicStats magic = Capabilities.getMagicStats(player);
				if (magic != null) {
					magic.sendManaToPlayer(player);
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
	 */
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		//		if ((event.getEntity() instanceof EntityPlayer) && !event.getEntity().isDead && (event.getEntity().world != null)) {
		//			System.out.println("Running Join World");
		//			final EntityPlayer player = (EntityPlayer) event.getEntity();
		//			final boolean client = player.world.isRemote;
		//			final EntityProperties cap = Capabilities.getEntityRace(player);
		//			if ((cap != null) && !client) {
		//				cap.sendInformationToPlayer(player);
		//			}
		//			final MagicStats magic = Capabilities.getMagicStats(player);
		//			if (magic != null) {
		//				magic.sendManaToPlayer(player);
		//			}
		//		}
	}

	/**
	 * Runs Server Side Only, Runs After EntityjoinWorld
	 */
	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		if ((event.player != null) && (event.player.getEntityWorld() != null)) {
			final EntityPlayer player = event.player;
			final boolean client = player.world.isRemote;
			final EntityProperties cap = Capabilities.getEntityRace(player);
			if ((cap != null) && !client) {
				cap.sendInformationToPlayer(player);
			}
			final MagicStats magic = Capabilities.getMagicStats(player);
			if (magic != null) {
				magic.sendManaToPlayer(player);
			}
		}
	}

}
