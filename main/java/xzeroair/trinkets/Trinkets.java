package xzeroair.trinkets;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
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
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

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

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		directory = event.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), Reference.configPath + ".cfg"));
		TrinketsConfig.readConfig();

		//Capabilities
		Capabilities.init();

		//Network
		NetworkHandler.init();

		if (gotVIPs != true) {
			System.out.println("Trinkets and Baubles: Generating VIP List");
			VIPHandler.popVIPList();
			gotVIPs = true;
		}

		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);

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
