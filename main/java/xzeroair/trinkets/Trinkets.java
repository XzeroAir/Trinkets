package xzeroair.trinkets;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.CapabilitiesHandler;
import xzeroair.trinkets.init.ModBlocks;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.proxy.CommonProxy;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ConfigHelper;
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

	public static final CreativeTabs trinketstab = new CreativeTabs("trinketstab") {
		@Override
		public ItemStack createIcon() {
			if (Loader.isModLoaded("baubles")) {
				return new ItemStack(ModItems.baubles.BaubleGlowRing);
			} else {
				return new ItemStack(ModItems.trinkets.TrinketGlowRing);
			}
		}
	};

	public static Configuration config;

	@Instance(value = Reference.MODID)
	public static Trinkets instance;

	public static File directory;

	public static final Logger log = LogManager.getLogger(Reference.MODID.toUpperCase());

	public static final int GUI = 0;

	private static boolean gotVIPs = false;

	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;

	public static boolean Baubles = false;
	public static boolean ArtemisLib = false;
	public static boolean ToughAsNails = false;
	public static boolean SimpleDifficulty = false;
	public static boolean FirstAid = false;
	public static boolean ElenaiDodge1 = false;
	public static boolean ElenaiDodge2 = false;
	public static boolean EnhancedVisuals = false;
	public static boolean IceAndFire = false;
	public static boolean FireResistanceTiers = false;
	public static boolean BetterDiving = false;
	public static boolean SoManyEnchantments = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		directory = event.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), Reference.configPath + ".cfg"));
		TrinketsConfig.readConfig();

		//		ConfigHelper.TrinketConfigStorage.init();
		if (TrinketsConfig.SERVER.misc.retrieveVIP && (gotVIPs != true)) {
			// TODO Remove this from the Loading Phase and do a Server Side Check, send info to the Client?
			log.info("Trinkets and Baubles: Generating VIP List");
			try {
				long startTime = System.nanoTime();
				VIPHandler.popVIPList();
				long endTime = System.nanoTime() - startTime;
				log.info("Trinkets and Baubles: Finished Gathering VIP List, Took " + (endTime / 1000000L) + "ms");
			} catch (final Exception e) {
				e.printStackTrace();
			}
			gotVIPs = true;
		}
		Baubles = Loader.isModLoaded("baubles");
		ArtemisLib = Loader.isModLoaded("artemislib");
		ToughAsNails = Loader.isModLoaded("toughasnails");
		SimpleDifficulty = Loader.isModLoaded("simpledifficulty");
		FirstAid = Loader.isModLoaded("firstaid");
		ElenaiDodge1 = Loader.isModLoaded("elenaidodge");
		ElenaiDodge2 = Loader.isModLoaded("elenaidodge2");
		EnhancedVisuals = Loader.isModLoaded("enhancedvisuals");
		IceAndFire = Loader.isModLoaded("iceandfire");
		FireResistanceTiers = Loader.isModLoaded("fireresistancetiers");
		BetterDiving = Loader.isModLoaded("better_diving");
		SoManyEnchantments = Loader.isModLoaded("somanyenchantments");

		Element.registerElements();
		log.info("Setting Up Races");
		EntityRace.registerRaces();
		log.info("Setting Up Blocks");
		ModBlocks.registerBlocks();
		log.info("Setting Up Items");
		ModItems.registerItems();
		//Capabilities
		log.info("Setting Up Capabilities");
		Capabilities.init();

		//Network
		log.info("Setting Up Networking");
		NetworkHandler.INSTANCE.init();

		log.info("Pre-init");
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

		//TODO Add Reskillable Support?
		//		if (Loader.isModLoaded("reskillable")) {
		//			codersafterdark.reskillable.api.ReskillableRegistries.UNLOCKABLES.register(
		//					new ReskillableTrait()
		//			);
		//		}
		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);
		ConfigHelper.TrinketConfigStorage.init();
		MinecraftForge.EVENT_BUS.register(TrinketsConfigEvent.instance);

		if (config.hasChanged()) {
			config.save();
		}
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		//		event.registerServerCommand(new CommandMain());
	}

	//	static {
	//		FluidRegistry.enableUniversalBucket();
	//	}
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
