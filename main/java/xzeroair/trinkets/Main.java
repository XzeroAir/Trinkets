package xzeroair.trinkets;

import java.io.File;

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
import xzeroair.trinkets.proxy.CommonProxy;
import xzeroair.trinkets.util.Reference;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, acceptedMinecraftVersions = Reference.acceptedMinecraftVersions, updateJSON = Reference.updateJSON)
public class Main {

	public static final CreativeTabs trinketstab = new TrinketsTab("trinketstab");

	public static Configuration config;

	@Instance
	public static Main instance;

	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {

		proxy.preInit(event);

		File directory = event.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "Trinkets_And_Baubles.cfg"));

	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {

		proxy.init(event);

	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);

		if (config.hasChanged()) {
			config.save();
		}

	}
}
