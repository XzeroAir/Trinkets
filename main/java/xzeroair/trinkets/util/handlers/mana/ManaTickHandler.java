package xzeroair.trinkets.util.handlers.mana;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ManaTickHandler {

	public static ManaTickHandler instance = new ManaTickHandler();

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			return;
		}
		final World world = event.world;
		WorldMana.get(world).tick(world);
	}

}
