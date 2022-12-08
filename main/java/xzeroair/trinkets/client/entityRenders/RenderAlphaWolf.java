package xzeroair.trinkets.client.entityRenders;

import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import xzeroair.trinkets.entity.AlphaWolf;

public class RenderAlphaWolf extends RenderLiving<AlphaWolf> {
	private static final ResourceLocation WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf.png");
	private static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
	private static final ResourceLocation ANRGY_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

	public RenderAlphaWolf(RenderManager manager) {
		super(manager, new ModelWolf(), 0.5F);
		//        this.addLayer(new LayerWolfCollar(this));
	}

	@Override
	public float prepareScale(AlphaWolf entitylivingbaseIn, float partialTicks) {
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		this.preRenderCallback(entitylivingbaseIn, partialTicks);

		float h = entitylivingbaseIn.height;
		float w = entitylivingbaseIn.width;
		float aH = h / 0.85F;
		GlStateManager.scale(aH, aH, aH);
		float f = 0.0625F;
		GlStateManager.translate(0.0F, -1.501F, 0.0F);
		//		float f = super.prepareScale(entitylivingbaseIn, partialTicks);
		return f;
	}

	@Override
	protected void renderModel(AlphaWolf entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	@Override
	protected float handleRotationFloat(AlphaWolf livingBase, float partialTicks) {
		return livingBase.getTailRotation();
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(AlphaWolf entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.isWolfWet()) {
			float f = entity.getBrightness() * entity.getShadingWhileWet(partialTicks);
			GlStateManager.color(f, f, f);
		}
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless
	 * you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(AlphaWolf entity) {
		if (entity.isTamed()) {
			return ANRGY_WOLF_TEXTURES;
			//			return TAMED_WOLF_TEXTURES;
		} else {
			return entity.isAngry() ? ANRGY_WOLF_TEXTURES : WOLF_TEXTURES;
		}
	}

	public static final Factory FACTORY = new Factory();

	public static class Factory implements IRenderFactory<AlphaWolf> {

		@Override
		public Render<? super AlphaWolf> createRenderFor(RenderManager manager) {
			return new RenderAlphaWolf(manager);
		}

	}
}
