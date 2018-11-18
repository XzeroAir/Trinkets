package xzeroair.trinkets.proxy;

import java.util.concurrent.Callable;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.compatibilities.ItemCap.DefaultItemCapability;
import xzeroair.trinkets.compatibilities.ItemCap.ItemCap;
import xzeroair.trinkets.compatibilities.ItemCap.ItemStorage;
import xzeroair.trinkets.compatibilities.sizeCap.CapStorage;
import xzeroair.trinkets.compatibilities.sizeCap.DeCap;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.compat.OreDictionaryCompat;

@EventBusSubscriber
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {

		NetworkHandler.init();
		//		ModEntities.init();
		CapabilityManager.INSTANCE.register(ICap.class, new CapStorage(), new Factory());
		CapabilityManager.INSTANCE.register(ItemCap.class, new ItemStorage(), new ItemFactory());

	}

	public void init(FMLInitializationEvent e) {
		OreDictionaryCompat.registerOres();

		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.util.eventhandlers.CompatabilitiesHandler());
		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.util.eventhandlers.OnWorldJoinHandler());
		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.handlers.EventHandler());
		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.handlers.PlayerEventMC());
		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.util.eventhandlers.CombatHandler());
		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.util.eventhandlers.MovementHandler());

		if((Loader.isModLoaded("morph"))) {
			MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.util.compat.morph.MorphEventHandler());
		}

		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.handlers.EventHandlerServer());
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public void spawnParticle(EnumParticleTypes Particle, double xCoord, double yCoord, double zCoord, double xSpeed,
			double ySpeed, double zSpeed, int i, float r, float g, float b) {

	}

	public void registerItemRenderer(Item item, int meta, String id) {

	}
	@SideOnly(Side.CLIENT)
	public ModelBiped getModel(String string) {
		return null;
	}

	public void registerEntityRenderers() {

	}

	public IThreadListener getThreadListener(final MessageContext context) {
		if(context.side.isServer()) {
			return context.getServerHandler().player.mcServer;
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

	private static class Factory implements Callable<ICap> {

		@Override
		public ICap call() throws Exception {
			return new DeCap();
		}
	}
	private static class ItemFactory implements Callable<ItemCap> {

		@Override
		public ItemCap call() throws Exception {
			return new DefaultItemCapability();
		}
	}
}
