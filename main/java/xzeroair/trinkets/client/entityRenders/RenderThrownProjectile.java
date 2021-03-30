package xzeroair.trinkets.client.entityRenders;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.entity.MovingThrownProjectile;
import xzeroair.trinkets.util.Reference;

public class RenderThrownProjectile extends Render<MovingThrownProjectile> {

	private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MODID, "textures/particle/dragon_breath.png");

	protected RenderThrownProjectile(RenderManager renderManager) {
		super(renderManager);
		shadowSize = 0.15F;
		shadowOpaque = 0.75F;
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(MovingThrownProjectile entity, double x, double y, double z, float entityYaw, float partialTicks) {
	}

	@SubscribeEvent
	public void renderWorldLast(RenderWorldLastEvent event) {
		EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;
		for (Entity e : player.world.getLoadedEntityList()) {
			if (e instanceof MovingThrownProjectile) {
				Minecraft.getMinecraft().getRenderManager().renderEntityStatic(e, event.getPartialTicks(), false);
			}
		}

	}

	@Override
	protected ResourceLocation getEntityTexture(MovingThrownProjectile entity) {
		return TEXTURES;
	}

	public static final Factory FACTORY = new Factory();

	public static class Factory implements IRenderFactory<MovingThrownProjectile> {

		@Override
		public Render<? super MovingThrownProjectile> createRenderFor(RenderManager manager) {
			return new RenderThrownProjectile(manager);
		}

	}

}
