package xzeroair.trinkets.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.client.model.Tiara;
import xzeroair.trinkets.client.model.Wings;
import xzeroair.trinkets.client.model.bowHat;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.client.particles.ParticleHeartBeat;
import xzeroair.trinkets.client.renderer.RenderLayerHandler;

public class ClientProxy extends CommonProxy {

	private static Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		//		OBJLoader.INSTANCE.addDomain(Reference.MODID);
		//		ModelLoader.setCustomModelResourceLocation(ModItems.ender_tiara, 0, new ModelResourceLocation(Reference.MODID + ":" + "ender_tiara", "inventory"));
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		ModKeyBindings.init();
		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.handlers.EventHandlerClient());

		RenderLayerHandler.initRender();
		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.util.eventhandlers.RenderHandler());

		MinecraftForge.EVENT_BUS.register(new xzeroair.trinkets.util.eventhandlers.CameraHandler());

	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	public void spawnParticle(EnumParticleTypes Particle, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int i, float r, float g, float b) {

		Minecraft.getMinecraft().effectRenderer
		.addEffect(new ParticleGreed(mc.world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, r, g, b));
		Minecraft.getMinecraft().effectRenderer
		.addEffect(new ParticleHeartBeat(mc.world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed));
	}

	private static final xzeroair.trinkets.client.model.Wings wings = new Wings(0F);
	private static final xzeroair.trinkets.client.model.bowHat bowHat = new bowHat(1.0F);
	private static final xzeroair.trinkets.client.model.Tiara tiara = new Tiara(0F);

	@Override
	public ModelBiped getModel(String string) {
		switch (string) {
		case "wings":
			return wings;
		case "tiara":
			return tiara;
		case "bowhat":
			return bowHat;
		default:
			return null;

		}
	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {

		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));

	}

	@Override
	public void registerEntityRenderers() {
		//		registerEntityRenderer(EntityCamera.class, RenderCamera.class);
	}

	private static <E extends Entity> void registerEntityRenderer(Class<E> entityClass, Class<? extends Render<E>> renderClass) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, new EntityRenderFactory<>(renderClass));
	}

	private static class EntityRenderFactory<E extends Entity> implements IRenderFactory<E> {
		private Class<? extends Render<E>> renderClass;

		private EntityRenderFactory(Class<? extends Render<E>> renderClass) {
			this.renderClass = renderClass;
		}

		@Override
		public Render<E> createRenderFor(RenderManager manager) {
			Render<E> renderer = null;

			try {
				renderer = renderClass.getConstructor(RenderManager.class).newInstance(manager);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return renderer;
		}
	}

	@Override
	public IThreadListener getThreadListener(final MessageContext context) {
		if (context.side.isClient()) {
			return Minecraft.getMinecraft();
		} else {
			return context.getServerHandler().player.mcServer;
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
