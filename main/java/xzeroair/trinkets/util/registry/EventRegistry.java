package xzeroair.trinkets.util.registry;

import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.client.Overlays.OverlayRenderer;
import xzeroair.trinkets.client.entityRender.ChangeViewRenderEvent;
import xzeroair.trinkets.client.events.CameraHandler;
import xzeroair.trinkets.client.events.EventHandlerClient;
import xzeroair.trinkets.client.events.GuiEventHandler;
import xzeroair.trinkets.client.events.RenderHandler;
import xzeroair.trinkets.events.BaubleEventHandler;
import xzeroair.trinkets.events.EventHandler;
import xzeroair.trinkets.events.EventHandlerServer;
import xzeroair.trinkets.events.PlayerEventMC;
import xzeroair.trinkets.events.TrinketEventHandler;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.compat.enhancedvisuals.EnhancedVisualsRenderEvent;
import xzeroair.trinkets.util.compat.firstaid.FirstAidDamageEvent;
import xzeroair.trinkets.util.compat.morph.MorphEventHandler;
import xzeroair.trinkets.util.eventhandlers.CombatHandler;
import xzeroair.trinkets.util.eventhandlers.EnderQueenHandler;
import xzeroair.trinkets.util.eventhandlers.LootHandler;
import xzeroair.trinkets.util.eventhandlers.MovementHandler;
import xzeroair.trinkets.util.eventhandlers.OnWorldJoinHandler;

public class EventRegistry {

	public static void preInit() {

	}

	public static void init() {

		MinecraftForge.EVENT_BUS.register(new EventHandlerServer());

		MinecraftForge.EVENT_BUS.register(new OnWorldJoinHandler());

		MinecraftForge.EVENT_BUS.register(new PlayerEventMC());

		MinecraftForge.EVENT_BUS.register(new EventHandler());

		MinecraftForge.EVENT_BUS.register(new EnderQueenHandler());

		MinecraftForge.EVENT_BUS.register(new CombatHandler());

		MinecraftForge.EVENT_BUS.register(new MovementHandler());

		MinecraftForge.EVENT_BUS.register(new LootHandler());

		if (Loader.isModLoaded("baubles")) {
			MinecraftForge.EVENT_BUS.register(new BaubleEventHandler());
		}
		MinecraftForge.EVENT_BUS.register(new TrinketEventHandler());
	}

	public static void postInit() {

	}

	public static void clientPreInit() {//akka
	}

	public static void serverInit() {
		MinecraftForge.EVENT_BUS.register(EventHandlerServer.instance);
	}

	public static void clientInit() {
		OBJLoader.INSTANCE.addDomain(Reference.MODID);
		MinecraftForge.EVENT_BUS.register(OverlayRenderer.instance);
		MinecraftForge.EVENT_BUS.register(new GuiEventHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
		MinecraftForge.EVENT_BUS.register(new RenderHandler());
		MinecraftForge.EVENT_BUS.register(new ChangeViewRenderEvent());
		MinecraftForge.EVENT_BUS.register(new CameraHandler());

		if (Loader.isModLoaded("enhancedvisuals")) {
			MinecraftForge.EVENT_BUS.register(EnhancedVisualsRenderEvent.instance);
		}
	}

	public static void clientPostInit() {

	}

	public static void modCompatPreInit() {
		if (Loader.isModLoaded("morph")) {
			MinecraftForge.EVENT_BUS.register(new MorphEventHandler());
		}
		// if(TrinketsConfig.compat.projecte && Loader.isModLoaded("projecte")) {
		// ProjectEMC.EMC();
		// }
	}

	public static void modCompatInit() {
		if (Loader.isModLoaded("firstaid")) {
			MinecraftForge.EVENT_BUS.register(new FirstAidDamageEvent());
		}
	}
}
