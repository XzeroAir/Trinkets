package xzeroair.trinkets.events;

import java.util.Map;

import net.minecraft.entity.Entity;
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
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.configsync.PacketConfigSync;
import xzeroair.trinkets.util.TrinketsConfig;

public class OnWorldJoinHandler {

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
	 * Fired on Logical Server and Fired After EntityJoinedWorldEvent
	 */
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		final EntityPlayer player = event.player;
		if ((player instanceof EntityPlayerMP)) {
			final EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
			// config Sync
			Trinkets.log.info("Syncing Config to " + playerMP.getName());
			final Map<String, String> configMap = TrinketsConfig.writeConfigMap();
			NetworkHandler.sendTo(new PacketConfigSync(configMap), playerMP);

			Capabilities.getEntityProperties(
					playerMP, EntityProperties::onLogin
			);

			Capabilities.getMagicStats(
					playerMP,
					cap -> {
						cap.sendManaToPlayer(playerMP);
					}
			);
		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {
		final EntityPlayer player = event.player;
		if ((player instanceof EntityPlayerMP)) {
			final EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
			Capabilities.getEntityProperties(
					playerMP, EntityProperties::onLogoff
			);
		}
	}

	/**
	 * Send Capability Data To the player from Server, Because Client is Incorrect
	 */
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		final Entity entity = event.getEntity();
		if (!(entity instanceof EntityPlayer)) {
			Capabilities.getEntityProperties(entity, prop -> prop.setLogin(true));
		}
	}

	/**
	 * Runs Server Side Only, Runs After EntityjoinWorld
	 */
	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		if ((event.player != null) && (event.player.getEntityWorld() != null)) {
			final EntityPlayer player = event.player;
			Capabilities.getEntityProperties(player, cap -> {
				cap.sendInformationToPlayer(player);
			});
			Capabilities.getMagicStats(player, cap -> {
				cap.sendManaToPlayer(player);
			});
		}
	}

}
