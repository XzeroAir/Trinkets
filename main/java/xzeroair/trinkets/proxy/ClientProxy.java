package xzeroair.trinkets.proxy;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.gui.TrinketGui;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.client.renderLayers.TrinketsRenderLayer;
import xzeroair.trinkets.util.registry.EventRegistry;

public class ClientProxy extends CommonProxy {

	private static Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		ModKeyBindings.init();
		super.preInit(e);
		EventRegistry.clientPreInit();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		final Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		RenderPlayer render;
		render = skinMap.get("default");
		render.addLayer(new TrinketsRenderLayer());

		render = skinMap.get("slim");
		render.addLayer(new TrinketsRenderLayer());
		super.init(e);
		EventRegistry.clientInit();
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		EventRegistry.clientPostInit();
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
	public void registerEntityRenderers() {
		//		registerEntityRenderer(EntityCamera.class, RenderCamera.class);
	}

	private static <E extends Entity> void registerEntityRenderer(Class<E> entityClass, Class<? extends Render<E>> renderClass) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, new EntityRenderFactory<>(renderClass));
	}

	private static class EntityRenderFactory<E extends Entity> implements IRenderFactory<E> {
		private final Class<? extends Render<E>> renderClass;

		private EntityRenderFactory(Class<? extends Render<E>> renderClass) {
			this.renderClass = renderClass;
		}

		@Override
		public Render<E> createRenderFor(RenderManager manager) {
			Render<E> renderer = null;

			try {
				renderer = this.renderClass.getConstructor(RenderManager.class).newInstance(manager);
			} catch (final Exception e) {
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
			return context.getServerHandler().player.getServerWorld();
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

	@Override
	@Nullable
	public EntityLivingBase getEntityLivingBase(MessageContext context, int entityID)
	{
		final EntityPlayer player = context.side.isClient() ? Minecraft.getMinecraft().player : context.getServerHandler().player;
		final Entity entity = player.world.getEntityByID(entityID);
		return entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (world instanceof WorldClient) {
			switch (ID) {
			case Trinkets.GUI: return new TrinketGui(player);
			}
		}
		return null;
	}
}
