package xzeroair.trinkets.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.compat.OreDictionaryCompat;
import xzeroair.trinkets.util.helpers.EventRegistry;

@EventBusSubscriber
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		//Other Mod Compatibility
		EventRegistry.modCompatInit();
		//Capabilities
		Capabilities.init();
		//Network
		NetworkHandler.init();
		//Register Mod Stuff

		//Event Handlers
		EventRegistry.preInit();

	}

	public void init(FMLInitializationEvent e) {
		OreDictionaryCompat.registerOres();
		EventRegistry.init();
	}
	public void postInit(FMLPostInitializationEvent e) {
		EventRegistry.postInit();
	}

	public void spawnParticle(EnumParticleTypes Particle, double xCoord, double yCoord, double zCoord, double xSpeed,
			double ySpeed, double zSpeed, int i, float r, float g, float b) {

	}

	public void registerItemRenderer(Item item, int meta, String id) {
	}

	public IThreadListener getThreadListener(final MessageContext context) {
		if(context.side.isServer()) {
			return context.getServerHandler().player.server;
		} else {
			throw new WrongSideException("Tried to get the IThreadListener from a client-side MessageContext on the dedicated server");
		}
	}

	public EntityPlayer getPlayer(final MessageContext context) {
		if (context.side.isServer()) {
			return context.getServerHandler().player;
		} else {
			throw new WrongSideException("Tried to get the player from a client-side MessageContext on the dedicated server");
		}
	}

	class WrongSideException extends RuntimeException {
		public WrongSideException(final String message) {
			super(message);
		}

		public WrongSideException(final String message, final Throwable cause) {
			super(message, cause);
		}
	}
}
