package xzeroair.trinkets.client.particles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleLightning extends Particle {

	List<Vec3d> points = new ArrayList<>();
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
	protected double prevPosX2;
	protected double prevPosY2;
	protected double prevPosZ2;
	protected double posX2;
	protected double posY2;
	protected double posZ2;
	private boolean depth = false;

	public ParticleLightning(World world, double x, double y, double z, int color, float a, boolean bool, float scale) {
		super(world, x, y, z);
		final int r = (color & 16711680) >> 16;
		final int g = (color & 65280) >> 8;
		final int b = (color & 255) >> 0;
		particleRed = r / 255.0F;
		particleGreen = g / 255.0F;
		particleBlue = b / 255.0F;
		particleAlpha = a;
		depth = bool;
		particleMaxAge = 16;
		particleAge = 0;
		particleScale = scale;
	}

	public ParticleLightning(World world, double x, double y, double z, double x2, double y2, double z2, int color, float a, boolean bool, float scale) {
		super(world, x, y, z);
		posX2 = x2;
		posY2 = y2;
		posZ2 = z2;
		prevPosX2 = x2;
		prevPosY2 = y2;
		prevPosZ2 = z2;
		final int r = (color & 16711680) >> 16;
		final int g = (color & 65280) >> 8;
		final int b = (color & 255) >> 0;
		particleRed = r / 255.0F;
		particleGreen = g / 255.0F;
		particleBlue = b / 255.0F;
		particleAlpha = a;
		depth = bool;
		particleMaxAge = 16;
		particleAge = 0;
		particleScale = scale;
	}

	public ParticleLightning(World world, double x, double y, double z, double x2, double y2, double z2, double xSpeed, double ySpeed, double zSpeed, int color, float a, boolean bool, float scale) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		posX2 = x2;
		posY2 = y2;
		posZ2 = z2;
		prevPosX2 = x2;
		prevPosY2 = y2;
		prevPosZ2 = z2;
		final int r = (color & 16711680) >> 16;
		final int g = (color & 65280) >> 8;
		final int b = (color & 255) >> 0;
		particleRed = r / 255.0F;
		particleGreen = g / 255.0F;
		particleBlue = b / 255.0F;
		particleAlpha = a;
		depth = bool;
		particleMaxAge = 16;
		particleAge = 0;
		particleScale = scale;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

		final float x = (float) ((prevPosX + ((posX - prevPosX) * partialTicks)) - interpPosX);
		final float y = (float) ((prevPosY + ((posY - prevPosY) * partialTicks)) - interpPosY);
		final float z = (float) ((prevPosZ + ((posZ - prevPosZ) * partialTicks)) - interpPosZ);

		final Vec3d origin = new Vec3d(x + 0.0, y + 0.2, z + 0.0);
		final int i = (int) (((particleAge + partialTicks) * 15.0F) / particleMaxAge);

		if (i <= 15) {
			final int lmv = 240;
			final int lmv2 = 240;
			for (double done = 0; done <= 15.0D; done += 1D) {
				final double alpha = done / 15.0D;
				final double x1 = interpolate(posX, posX2, alpha);
				final double y1 = interpolate(posY, posY2, alpha);
				final double z1 = interpolate(posZ, posZ2, alpha);
				final Vec3d test = new Vec3d(x1, y1, z1);
				if (done < 1) {
					//					points.add(test);
				} else if (done == 1) {
					points.add(test);
				} else {
					if (done < (15.0D)) {
						points.add(new LightningVertex(test).getVec());
					} else {
						points.add(test);
						points.add(new Vec3d(posX2, posY2, posZ2));
					}
				}
			}
			GlStateManager.pushMatrix();
			final float r = particleRed;
			final float g = particleGreen;
			final float b = particleBlue;
			final float a = particleAlpha;
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
			buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR);
			GlStateManager.glLineWidth(particleScale);
			for (int p = 0; p < (points.size() - 1); p++) {
				final int clamped = MathHelper.clamp(p, 0, (points.size() - 1));
				final Vec3d lp = i == 0 ? points.get(clamped) : points.get(clamped);
				final float t1 = (float) ((lp.x + ((points.get(clamped).x - lp.x) * partialTicks)) - interpPosX);
				final float t2 = (float) ((lp.y + ((points.get(clamped).y - lp.y) * partialTicks)) - interpPosY);
				final float t3 = (float) ((lp.z + ((points.get(clamped).z - lp.z) * partialTicks)) - interpPosZ);
				buffer.pos(t1, t2, t3)
						.tex(0, 0)
						.color(r, g, b, a)
						//						.lightmap(lmv, lmv2)
						.endVertex();
			}
			Tessellator.getInstance().draw();

			buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR);
			GlStateManager.glLineWidth(particleScale * 4F);
			for (int p = 0; p < (points.size() - 1); p++) {
				final int clamped = MathHelper.clamp(p, 0, (points.size() - 1));
				final Vec3d lp = i == 0 ? points.get(clamped) : points.get(clamped);
				final float t1 = (float) ((lp.x + ((points.get(clamped).x - lp.x) * partialTicks)) - interpPosX);
				final float t2 = (float) ((lp.y + ((points.get(clamped).y - lp.y) * partialTicks)) - interpPosY);
				final float t3 = (float) ((lp.z + ((points.get(clamped).z - lp.z) * partialTicks)) - interpPosZ);
				buffer.pos(t1, t2, t3)
						.tex(0, 0)
						.color(r, g, b, a * 0.5F)
						//						.lightmap(lmv, lmv2)
						.endVertex();
			}
			Tessellator.getInstance().draw();
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.glLineWidth(2F);
			GlStateManager.popMatrix();
			points.clear();
		}
	}

	private static double interpolate(double start, double end, double alpha) {
		return start + ((end - start) * alpha);
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge) {
			this.setExpired();
		}
	}

	@Override
	public int getFXLayer() {
		return 3;
	}

}