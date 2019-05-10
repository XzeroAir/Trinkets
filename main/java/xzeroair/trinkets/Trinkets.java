package xzeroair.trinkets;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xzeroair.trinkets.proxy.CommonProxy;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

@Mod(
		modid = Reference.MODID,
		name = Reference.NAME,
		version = Reference.VERSION,
		dependencies = Reference.DEPENDENCIES,
		acceptedMinecraftVersions = Reference.acceptedMinecraftVersions,
		updateJSON = Reference.updateJSON
		)

public class Trinkets {

	public static final CreativeTabs trinketstab = new TrinketsTab("trinketstab");

	public static Configuration config;

	@Instance(value=Reference.MODID)
	public static Trinkets instance;

	//	public File directory;

	public static final Logger log = LogManager.getLogger(Reference.MODID.toUpperCase());
	public static final int GUI = 0;

	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		final File directory = event.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "Trinkets_And_Baubles.cfg"));
		TrinketsConfig.readConfig();

		proxy.preInit(event);

		config.save();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);

		if (config.hasChanged()) {
			config.save();
		}

	}
}
