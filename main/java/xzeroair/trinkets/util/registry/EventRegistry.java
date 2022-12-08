package xzeroair.trinkets.util.registry;

import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.events.ChangeViewRenderEvent;
import xzeroair.trinkets.client.events.EventHandlerClient;
import xzeroair.trinkets.client.events.GuiScreenEvents;
import xzeroair.trinkets.client.events.PlayerCameraSetupEvents;
import xzeroair.trinkets.client.events.RenderEntitiesEvent;
import xzeroair.trinkets.client.events.ScreenOverlayEvents;
import xzeroair.trinkets.events.BaubleEventHandler;
import xzeroair.trinkets.events.BlockBreakEvents;
import xzeroair.trinkets.events.CombatHandler;
import xzeroair.trinkets.events.EnderQueenHandler;
import xzeroair.trinkets.events.EventHandler;
import xzeroair.trinkets.events.EventHandlerServer;
import xzeroair.trinkets.events.MovementHandler;
import xzeroair.trinkets.events.OnWorldJoinHandler;
import xzeroair.trinkets.events.PlayerEventMC;
import xzeroair.trinkets.events.TrinketEventHandler;
import xzeroair.trinkets.init.ModEntities;
import xzeroair.trinkets.init.ModSounds;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.elenaidodge.ElenaiDodgeCompat;
import xzeroair.trinkets.util.compat.enhancedvisuals.EnhancedVisualsRenderEvent;
import xzeroair.trinkets.util.compat.firstaid.FirstAidDamageEvent;

public class EventRegistry {

	public static void preInit() {
		ModEntities.registerEntities();
	}

	public static void init() {
		ModSounds.init();
		MinecraftForge.EVENT_BUS.register(new EventHandlerServer());

		MinecraftForge.EVENT_BUS.register(new OnWorldJoinHandler());

		MinecraftForge.EVENT_BUS.register(new PlayerEventMC());

		MinecraftForge.EVENT_BUS.register(new EventHandler());

		MinecraftForge.EVENT_BUS.register(new EnderQueenHandler());

		MinecraftForge.EVENT_BUS.register(new CombatHandler());

		MinecraftForge.EVENT_BUS.register(new MovementHandler());

		MinecraftForge.EVENT_BUS.register(new BlockBreakEvents());

		if (Trinkets.Baubles && !TrinketsConfig.compat.xatItemsInTrinketGuiOnly) {
			MinecraftForge.EVENT_BUS.register(new BaubleEventHandler());
		}
		MinecraftForge.EVENT_BUS.register(new TrinketEventHandler());
	}

	public static void postInit() {

	}

	public static void clientPreInit() {
		OBJLoader.INSTANCE.addDomain(Reference.MODID);
		ModEntities.registerEntityRenders();
	}

	public static void serverInit() {
		MinecraftForge.EVENT_BUS.register(EventHandlerServer.instance);
	}

	public static void clientInit() {
		MinecraftForge.EVENT_BUS.register(ScreenOverlayEvents.instance);
		MinecraftForge.EVENT_BUS.register(new GuiScreenEvents());
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
		MinecraftForge.EVENT_BUS.register(new RenderEntitiesEvent());
		MinecraftForge.EVENT_BUS.register(new ChangeViewRenderEvent());
		MinecraftForge.EVENT_BUS.register(new PlayerCameraSetupEvents());

		if (Loader.isModLoaded("enhancedvisuals")) {
			try {
				MinecraftForge.EVENT_BUS.register(EnhancedVisualsRenderEvent.instance);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void clientPostInit() {
		registerCommands();
	}

	public static void registerCommands() {

	}

	public static void modCompatPreInit() {
		// if(TrinketsConfig.compat.projecte && Loader.isModLoaded("projecte")) {
		// ProjectEMC.EMC();
		// }
	}

	public static void modCompatInit() {
		if (Loader.isModLoaded("firstaid")) {
			try {
				MinecraftForge.EVENT_BUS.register(new FirstAidDamageEvent());
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void modCompatPostInit() {
		if (Trinkets.ElenaiDodge1 && TrinketsConfig.compat.elenaiDodge) {
			try {
				MinecraftForge.EVENT_BUS.register(new ElenaiDodgeCompat());
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
