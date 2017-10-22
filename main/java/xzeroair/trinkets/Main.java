package xzeroair.trinkets;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xzeroair.trinkets.client.TextureStitcher;
import xzeroair.trinkets.emc.emcmapping;
import xzeroair.trinkets.particles.ParticleGreed;
import xzeroair.trinkets.proxy.CommonProxy;
import xzeroair.trinkets.util.Reference;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class Main {
	
	public static final CreativeTabs trinketstab = new TrinketsTab("trinketstab");
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;

	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(new TextureStitcher());
		
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {

	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		
		emcmapping.postInit();
		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.util.handlers.EventHandler());
		
	}
}
