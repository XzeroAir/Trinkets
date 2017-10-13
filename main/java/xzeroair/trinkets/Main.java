package xzeroair.trinkets;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.glowing_ingot;
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
		
	//	ModItems.init();
	//	ModItems.register();
	//	proxy.init();
		
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		
		
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		
	}
}
