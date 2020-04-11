package xzeroair.trinkets.events;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandlerServer {

	public static EventHandlerServer instance = new EventHandlerServer();

	private boolean check, check2, check3 = false;

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {

	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {

	}

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) {

	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {

	}

}
