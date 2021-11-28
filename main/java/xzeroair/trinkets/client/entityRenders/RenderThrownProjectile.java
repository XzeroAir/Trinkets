package xzeroair.trinkets.client.entityRenders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import xzeroair.trinkets.entity.MovingThrownProjectile;
import xzeroair.trinkets.util.Reference;

public class RenderThrownProjectile<T extends Entity> extends Render<T> {

	private static final ResourceLocation DRAGON_FIREBALL_TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_fireball.png");
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
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		//		final Tessellator tessellator = Tessellator.getInstance();
		//		final BufferBuilder buffer = tessellator.getBuffer();
		//		final int particleAge = entity.ticksExisted;
		//		final int particleMaxAge = 32;
		//		float particleScale = 1F;
		//		final float fT = (particleAge + partialTicks) / particleMaxAge;
		//		final float flameScale = particleScale;
		//		final int i = (int) (((particleAge + partialTicks) * 30.0F) / particleMaxAge);
		//		particleScale = flameScale * (1.0F - (fT * fT * 0.5F));
		//		//		particleScale = flameScale * (1.0F - (i * i * 0.5F));
		//		if (i <= (particleMaxAge - 1)) {
		//			GlStateManager.pushMatrix();
		//			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/particle/dragon_breath.png"));
		//			//			float f = particleTextureIndexX % 16.0F;
		//			//			float f1 = f + 0.0624375F;
		//			//			float f2 = particleTextureIndexY / 16.0F;
		//			//			float f3 = f2 + 0.0624375F;
		//			final boolean flipU = false;
		//			final boolean flipUV = false;
		//
		//			final int width = 16;
		//			final int height = 16;
		//			final float tileWidth = 1F / 256;
		//			final float tileHeight = 1F / 256;
		//			final int x1 = i < 16 ? i * 16 : (i + 1) * 16;
		//			final int y1 = i < 16 ? 16 : 0;
		//
		//			final float uF = flipU ? x1 + width : x1;
		//			final float uF2 = flipU ? x1 : x1 + width;
		//			final float vF = flipUV ? y1 + height : y1;
		//			final float vF2 = flipUV ? y1 : y1 + height;
		//
		//			final int offset = particleMaxAge;
		//			final float f4 = particleScale;
		//
		////			final int b = this.getBrightnessForRender(partialTicks);
		////			final int j = (b >> 16) & 65535;
		////			final int k = b & 65535;
		//			final float f5 = (float) ((entity.prevPosX + ((entity.posX - entity.prevPosX) * partialTicks)) - x);
		//			final float f6 = (float) ((entity.prevPosY + ((entity.posY - entity.prevPosY) * partialTicks)) - y);
		//			final float f7 = (float) ((entity.prevPosZ + ((entity.posZ - entity.prevPosZ) * partialTicks)) - z);
		//			final Vec3d[] avec3d = new Vec3d[] {
		//					new Vec3d((-rotationX * f4) - (rotationXY * f4), -rotationZ * f4, (-rotationYZ * f4) - (rotationXZ * f4)),
		//					new Vec3d((-rotationX * f4) + (rotationXY * f4), rotationZ * f4, (-rotationYZ * f4) + (rotationXZ * f4)),
		//					new Vec3d((rotationX * f4) + (rotationXY * f4), rotationZ * f4, (rotationYZ * f4) + (rotationXZ * f4)),
		//					new Vec3d((rotationX * f4) - (rotationXY * f4), -rotationZ * f4, (rotationYZ * f4) - (rotationXZ * f4))
		//			};
		//
		//			if (particleAngle != 0.0F) {
		//				final float f8 = particleAngle + ((particleAngle - prevParticleAngle) * partialTicks);
		//				final float f9 = MathHelper.cos(f8 * 0.5F);
		//				final float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
		//				final float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
		//				final float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
		//				final Vec3d vec3d = new Vec3d(f10, f11, f12);
		//
		//				for (int l = 0; l < 4; ++l) {
		//					avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale(2.0F * f9));
		//				}
		//			}
		//			buffer.pos(f5 + avec3d[0].x, f6 + avec3d[0].y, f7 + avec3d[0].z).tex(uF * tileWidth, vF2 * tileHeight).color(particleRed, particleGreen, particleBlue, particleAlpha).endVertex();//.lightmap(j, k).endVertex();
		//			buffer.pos(f5 + avec3d[1].x, f6 + avec3d[1].y, f7 + avec3d[1].z).tex(uF2 * tileWidth, vF2 * tileHeight).color(particleRed, particleGreen, particleBlue, particleAlpha).endVertex();//.lightmap(j, k).endVertex();
		//			buffer.pos(f5 + avec3d[2].x, f6 + avec3d[2].y, f7 + avec3d[2].z).tex(uF2 * tileWidth, vF * tileHeight).color(particleRed, particleGreen, particleBlue, particleAlpha).endVertex();//.lightmap(j, k).endVertex();
		//			buffer.pos(f5 + avec3d[3].x, f6 + avec3d[3].y, f7 + avec3d[3].z).tex(uF * tileWidth, vF * tileHeight).color(particleRed, particleGreen, particleBlue, particleAlpha).endVertex();//.lightmap(j, k).endVertex();
		//			GlStateManager.popMatrix();
		//		}
		//		GlStateManager.pushMatrix();
		//		this.bindEntityTexture(entity);
		//		GlStateManager.translate((float) x, (float) y, (float) z);
		//		GlStateManager.enableRescaleNormal();
		//		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		//		final Tessellator tessellator = Tessellator.getInstance();
		//		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		//		final float f = 1.0F;
		//		final float f1 = 0.5F;
		//		final float f2 = 0.25F;
		//		GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		//		GlStateManager.rotate((renderManager.options.thirdPersonView == 2 ? -1 : 1) * -renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		//
		//		if (renderOutlines) {
		//			GlStateManager.enableColorMaterial();
		//			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		//		}
		//
		//		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		//		bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		//		bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		//		bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		//		bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		//		tessellator.draw();
		//
		//		if (renderOutlines) {
		//			GlStateManager.disableOutlineMode();
		//			GlStateManager.disableColorMaterial();
		//		}
		//
		//		GlStateManager.disableRescaleNormal();
		//		GlStateManager.popMatrix();
		//		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless
	 * you call Render.bindEntityTexture.
	 */
	@Override
	public ResourceLocation getEntityTexture(T entity) {
		//		return null;
		//		return DRAGON_FIREBALL_TEXTURE;
		return null;//DRAGON_FIREBALL_TEXTURE;
	}

	//	/**
	//	 * Renders the desired {@code T} type Entity.
	//	 */
	//	@Override
	//	public void doRender(MovingThrownProjectile entity, double x, double y, double z, float entityYaw, float partialTicks) {
	//	}
	//
	//	@SubscribeEvent
	//	public void renderWorldLast(RenderWorldLastEvent event) {
	//		EntityPlayer player = net.minecraft.client.Minecraft.getMinecraft().player;
	//		for (Entity e : player.world.getLoadedEntityList()) {
	//			if (e instanceof MovingThrownProjectile) {
	//
	//				Minecraft.getMinecraft().getRenderManager().renderEntityStatic(e, event.getPartialTicks(), false);
	//			}
	//		}
	//
	//	}
	//
	//	@Override
	//	protected ResourceLocation getEntityTexture(MovingThrownProjectile entity) {
	//		return TEXTURES;
	//	}

	public static final Factory FACTORY = new Factory();

	public static class Factory implements IRenderFactory<MovingThrownProjectile> {

		@Override
		public Render<? super MovingThrownProjectile> createRenderFor(RenderManager manager) {
			return new RenderThrownProjectile<>(manager);
		}

	}

}
