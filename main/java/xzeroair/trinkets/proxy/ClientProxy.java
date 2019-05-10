package xzeroair.trinkets.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.client.events.CameraHandler;
import xzeroair.trinkets.client.events.EventHandlerClient;
import xzeroair.trinkets.client.events.RenderHandler;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.client.particles.ParticleGreed;

public class ClientProxy extends CommonProxy {

	private static Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		ModKeyBindings.init();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
		MinecraftForge.EVENT_BUS.register(new RenderHandler());
		MinecraftForge.EVENT_BUS.register(new CameraHandler());

	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	public void spawnParticle(EnumParticleTypes Particle, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int i, float r, float g, float b) {

		Minecraft.getMinecraft().effectRenderer
		.addEffect(new ParticleGreed(mc.world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, r, g, b));
	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
	}

	@Override
	public IThreadListener getThreadListener(final MessageContext context) {
		if (context.side.isClient()) {
			return Minecraft.getMinecraft();
		} else {
			return context.getServerHandler().player.server;
		}
	}

	@Override
	public EntityPlayer getPlayer(final MessageContext context) {
		if (context.side.isClient()) {
			return Minecraft.getMinecraft().player;
		} else {
			return context.getServerHandler().player;
		}
	}
}
