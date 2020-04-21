package xzeroair.trinkets.util.eventhandlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.RaceProperties;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.SizeDataPacket;
import xzeroair.trinkets.network.configsync.BlocklistSyncPacket;
import xzeroair.trinkets.network.configsync.PacketConfigSync;
import xzeroair.trinkets.util.TrinketsConfig;

public class OnWorldJoinHandler {

	private boolean check, check2, check3 = false;

	@SubscribeEvent
	public void attachAttributes(EntityEvent.EntityConstructing event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			final EntityLivingBase entity = (EntityLivingBase) event.getEntity();
			final AbstractAttributeMap map = entity.getAttributeMap();

			map.registerAttribute(RaceAttribute.ENTITY_RACE);
		}
	}

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
								false,
								TrinketsConfig.SERVER.FAIRY_RING.creative_flight,
								TrinketsConfig.SERVER.DRAGON_EYE.oreFinder,
								TrinketsConfig.SERVER.GUI.guiEnabled,
								TrinketsConfig.SERVER.FAIRY_RING.creative_flight_speed,
								TrinketsConfig.SERVER.FAIRY_RING.flight_speed,
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
				String combinedArray = "";
				for (int i = configArray.length - 1; i >= 0; --i) {
					combinedArray = configArray[i] + ", " + combinedArray;
				}
				if (!combinedArray.isEmpty()) {
					final int hd = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.DR.C001_HD;
					final int vd = TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.DR.C00_VD;
					NetworkHandler.INSTANCE.sendTo(new BlocklistSyncPacket(player, false, combinedArray, 0, hd, vd), (EntityPlayerMP) player);
				}
			}
			// end config Sync

		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {
		if ((event.player.world != null) && !event.player.world.isRemote) {
			final EntityPlayer player = event.player;
			RaceProperties cap = Capabilities.getEntityRace(player);
			if (cap != null) {
				if ((player.getRidingEntity() instanceof EntityMinecart) && (cap.getTrans() == true)) {
					player.dismountRidingEntity();
				}
			}
		}
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {

	}

	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
		if (event.player != null) {
			final EntityPlayer player = event.player;
			final boolean client = player.world.isRemote;
			if (event.player.hasCapability(Capabilities.ENTITY_RACE, null)) {
				RaceProperties cap = Capabilities.getEntityRace(player);
				if (!client) {
					NetworkHandler.INSTANCE.sendTo(new SizeDataPacket(player, cap), (EntityPlayerMP) player);
				}
			}
		}
	}

}
