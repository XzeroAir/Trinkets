package xzeroair.trinkets;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilitiesHandler;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.proxy.CommonProxy;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.util.RaceRegistry;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.TrinketsConfigEvent;
import xzeroair.trinkets.vip.VIPHandler;

// @formatter:off
@Mod(
		modid = Reference.MODID,
		name = Reference.NAME,
		version = Reference.VERSION,
		//		guiFactory = Reference.GUIFACTORY,
		dependencies = Reference.DEPENDENCIES,
		acceptedMinecraftVersions = Reference.acceptedMinecraftVersions,
		updateJSON = Reference.updateJSON
		//		certificateFingerprint = Reference.FINGERPRINT
		)
// @formatter:on
public class Trinkets {

	public static final CreativeTabs trinketstab = new TrinketsTab("trinketstab");

	public static Configuration config;

	@Instance(value = Reference.MODID)
	public static Trinkets instance;

	public static File directory;

	public static final Logger log = LogManager.getLogger(Reference.MODID.toUpperCase());

	public static final int GUI = 0;

	private static boolean gotVIPs = false;

	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;

	public static final RaceRegistry<ResourceLocation, EntityRace> RaceRegistry = new RaceRegistry<>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		directory = event.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), Reference.configPath + ".cfg"));
		TrinketsConfig.readConfig();

		if (gotVIPs != true) {
			log.info("Trinkets and Baubles: Generating VIP List");
			//Needs to be on Client only
			//			if (Minecraft.getMinecraft().getSession() != null) {
			//				Session session = Minecraft.getMinecraft().getSession();
			//				if (session.getProfile() != null) {
			//					GameProfile profile = session.getProfile();
			VIPHandler.popVIPList();
			//					System.out.println(session.getUsername() + " with ID:" + session.getPlayerID());
			//					System.out.println(profile.getName() + " with ID:" + profile.getId());
			//				}
			//			}
			gotVIPs = true;
		}

		EntityRace.registerRaces();
		//Capabilities
		Capabilities.init();

		//Network
		NetworkHandler.init();

		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

		//TODO Add Reskillable Support?
		//		if (Loader.isModLoaded("reskillable")) {
		//			codersafterdark.reskillable.api.ReskillableRegistries.UNLOCKABLES.register(
		//                new ReskillableTrait();
		//        )
		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);

		MinecraftForge.EVENT_BUS.register(TrinketsConfigEvent.instance);

		if (config.hasChanged()) {
			config.save();
		}
	}

	//	@EventHandler
	//	public void fingerprintViolated(FMLFingerprintViolationEvent event) {
	//
	//		System.err.println("\n\n\nInvalid signature for "+Reference.NAME
	//				+ "\nSomeone might have messed with the JAR file of the mod"
	//				+"\nMake sure to only download from the official source"
	//				+"\nhttps://www.curseforge.com/minecraft/mc-mods/trinkets-and-baubles"
	//				+ "\n\n");
	//
	//	}
}
