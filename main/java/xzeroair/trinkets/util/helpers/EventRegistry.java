package xzeroair.trinkets.util.helpers;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.CapabilitiesHandler;
import xzeroair.trinkets.events.EventHandler;
import xzeroair.trinkets.events.EventHandlerServer;
import xzeroair.trinkets.events.PlayerEventMC;
import xzeroair.trinkets.util.compat.ProjectEMC;
import xzeroair.trinkets.util.compat.morph.MorphEventHandler;
import xzeroair.trinkets.util.eventhandlers.CombatHandler;
import xzeroair.trinkets.util.eventhandlers.LootHandler;
import xzeroair.trinkets.util.eventhandlers.MovementHandler;
import xzeroair.trinkets.util.eventhandlers.OnWorldJoinHandler;

public class EventRegistry {

	public static void preInit() {

	}

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		MinecraftForge.EVENT_BUS.register(new OnWorldJoinHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerEventMC());
		MinecraftForge.EVENT_BUS.register(new CombatHandler());
		MinecraftForge.EVENT_BUS.register(new MovementHandler());

		MinecraftForge.EVENT_BUS.register(new LootHandler());

		MinecraftForge.EVENT_BUS.register(new EventHandlerServer());

	}

	public static void postInit() {

	}

	public static void clientPreInit() {
	}

	public static void clientInit() {
	}

	public static void clientPostInit() {

	}

	public static void modCompatInit() {
		if((Loader.isModLoaded("morph"))) {
			MinecraftForge.EVENT_BUS.register(new MorphEventHandler());
		}
		if(Loader.isModLoaded("projecte")) {
			ProjectEMC.EMC();
		}
	}
}
