package xzeroair.trinkets.client.entityRenders;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import xzeroair.trinkets.entity.AlphaWolf;

public class RenderAlphaWolf extends RenderWolf {//RenderLiving<AlphaWolf> {;
	private static final ResourceLocation WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf.png");
	private static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
	private static final ResourceLocation ANRGY_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

	public RenderAlphaWolf(RenderManager manager) {
		super(manager);
	}

	@Override
	public float prepareScale(EntityWolf entity, float partialTicks) {
		//		final float height = entity.height;
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		this.preRenderCallback(entity, partialTicks);

		float h = entity.height;
		float w = entity.width;
		float scale = h / 0.85F;
		GlStateManager.scale(scale, scale, scale);
		float f = 0.0625F;
		GlStateManager.translate(0.0F, -1.501F, 0.0F);
		return f;
	}

	@Override
	protected void renderModel(EntityWolf entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
	}

	@Override
	protected float handleRotationFloat(EntityWolf entity, float partialTicks) {
		return super.handleRotationFloat(entity, partialTicks);
	}

	@Override
	public void doRender(EntityWolf entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		super.doRenderShadowAndFire(entity, x, y, z, yaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWolf entity) {
		if (entity.isTamed()) {
			return ANRGY_WOLF_TEXTURES;
		} else {
			return entity.isAngry() ? ANRGY_WOLF_TEXTURES : WOLF_TEXTURES;
		}
		//		return super.getEntityTexture(entity);
	}

	public static final Factory FACTORY = new Factory();

	public static class Factory implements IRenderFactory<AlphaWolf> {

		@Override
		public Render<? super AlphaWolf> createRenderFor(RenderManager manager) {
			return new RenderAlphaWolf(manager);
		}

	}
}
