package xzeroair.trinkets.client.particles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleLightning extends Particle {

	final List<Vec3d> points = new ArrayList<>();
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
	Vec3d firstSegment, secondSegment, thirdSegment;

	public ParticleLightning(World worldIn, double x, double y, double z, double xD, double yD, double zD, float r, float g, float b) {
		super(worldIn, x, y, z, 0, 0, 0);

		particleRed = r;
		particleGreen = g;
		particleBlue = b;
		particleAlpha = 0.3F;
		particleMaxAge = 16;
		particleAge = 0;
	}
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
		//Interpolating everything back to 0,0,0. These are transforms you can find at RenderEntity class
		final double d0 = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partialTicks);
		final double d1 = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partialTicks);
		final double d2 = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partialTicks);
		final float f5 = (float)((prevPosX + ((posX - prevPosX) * partialTicks)) - interpPosX);
		final float f6 = (float)((prevPosY + ((posY - prevPosY) * partialTicks)) - interpPosY);
		final float f7 = (float)((prevPosZ + ((posZ - prevPosZ) * partialTicks)) - interpPosZ);

		//		final Vec3d origin = new Vec3d(d0, d1+0.2, d2);
		//		final Vec3d origin = new Vec3d(posX, posY+0.2, posZ);
		final Vec3d origin = new Vec3d(f5, f6+0.2, f7);
		final int i = (int)(((particleAge + partialTicks) * 15.0F) / particleMaxAge);

		if (i <= 15)
		{
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("xat:textures/particle/greed.png"));
			GlStateManager.pushMatrix();
			GlStateManager.color(particleRed, particleGreen, particleBlue, particleAlpha);
			GlStateManager.translate(0, 0.33, 0.0);
			GlStateManager.scale(1, 1, 1);
			//			GlStateManager.disableDepth();
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			final float f = (i % 16) / 16.0F;
			final float f1 = f + 0.0625f;
			final float f2 = i / 16 / 16.0F;
			final float f3 = f2 + 0.0625f;
			//float f4 effects size/scale
			float f4 = 0.25f;//2.0F * this.size;
			//			final float f5 = (float)((prevPosX + ((posX - prevPosX) * partialTicks)) - interpPosX);
			//			final float f6 = (float)((prevPosY + ((posY - prevPosY) * partialTicks)) - interpPosY);
			//			final float f7 = (float)((prevPosZ + ((posZ - prevPosZ) * partialTicks)) - interpPosZ);
			final int lmv = 240;
			final int lmv2 = 240;
			buffer.begin(7, VERTEX_FORMAT);

			buffer.pos(
					f5 - (rotationX * f4) - (rotationXY * f4),
					f6 - (rotationZ * f4),
					f7 - (rotationYZ * f4) - (rotationXZ * f4))
			.tex(f1, f3)
			.color(particleRed, particleGreen, particleBlue, particleAlpha)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(f5 - (rotationX * f4)) + (rotationXY * f4),
					f6 + (rotationZ * f4),
					(f7 - (rotationYZ * f4)) + (rotationXZ * f4))
			.tex(f1, f2)
			.color(particleRed, particleGreen, particleBlue, particleAlpha)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					f5 + (rotationX * f4) + (rotationXY * f4),
					f6 + (rotationZ * f4),
					f7 + (rotationYZ * f4) + (rotationXZ * f4))
			.tex(f, f2)
			.color(particleRed, particleGreen, particleBlue, particleAlpha)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(f5 + (rotationX * f4)) - (rotationXY * f4),
					f6 - (rotationZ * f4),
					(f7 + (rotationYZ * f4)) - (rotationXZ * f4))
			.tex(f, f3)
			.color(particleRed, particleGreen, particleBlue, particleAlpha)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			//			GlStateManager.enableDepth();
			GlStateManager.popMatrix();
			//		}
			//			if((i % 1) == 0) {
			for(int p = 0; p < particleMaxAge;p++) {
				if(p < 1) {
					points.add(origin);
					//					points.add(new Vec3d(f5, f6, f7));
				} else {
					final int test = ((rand.nextInt(3) + 1)+1)-3;
					final int test2 = rand.nextInt(4);
					final int test3 = ((rand.nextInt(3) + 1)+1)-3;
					final float t1 = (float) test/10;//new Float("0." + String.valueOf(test));
					final float t2 = new Float("0." + String.valueOf(test2));
					final float t3 = (float) test3/10;//new Float("0." + String.valueOf(test3));
					final Vec3d lP = points.get(p-1);
					final Vec3d next = new Vec3d(lP.x+(t3), lP.y+(t1), lP.z+(t2));
					points.add(next);
				}
			}

			GlStateManager.pushMatrix();

			//Apply 0-our transforms to set everything back to 0,0,0
			//			Tessellator.getInstance().getBuffer().setTranslation(-(d0), -(d1), -(d2));
			//			Tessellator.getInstance().getBuffer().setTranslation(-f5, -f6, -f7);
			GlStateManager.rotate(90, 0, 1, 0);
			final boolean check = true;
			final float r = check? 0F : 1F;
			final float g = check? 0.7F : 0F;
			final float b = 1F;
			final float a = particleAlpha;
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			//			buffer.begin(GL11.GL_QUADS, VERTEX_FORMAT);
			buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			GlStateManager.glLineWidth(6f);
			for(int p = 0;p < (points.size()-1);p++) {
				final Vec3d lp = p==0?points.get(p):points.get(p-1);
				final Vec3d cp = points.get(p);
				//			final float f5 = (float)((prevPosX + ((posX - prevPosX) * partialTicks)) - interpPosX);
				//			final float f6 = (float)((prevPosY + ((posY - prevPosY) * partialTicks)) - interpPosY);
				//			final float f7 = (float)((prevPosZ + ((posZ - prevPosZ) * partialTicks)) - interpPosZ);
				final float t1 = (float)((lp.x + ((cp.x - lp.x) * partialTicks)));
				final float t2 = (float)((lp.y + ((cp.y - lp.y) * partialTicks)));
				final float t3 = (float)((lp.z + ((cp.z - lp.z) * partialTicks)));
				//				final float t1 = (float)((cp.x + ((lp.x - cp.x) * partialTicks)));
				//				final float t2 = (float)((cp.y + ((lp.y - cp.y) * partialTicks)));
				//				final float t3 = (float)((cp.z + ((lp.z - cp.z) * partialTicks)));
				//				Tessellator.getInstance().getBuffer().setTranslation(-t1, -t2, -t3);
				f4 = 1;//0.25f;//2.0F * this.size;
				buffer.pos(cp.x, cp.y, cp.z)
				//				buffer.pos(
				//						cp.x - (rotationX * f4) - (rotationXY * f4),
				//						cp.y - (rotationZ * f4),
				//						cp.z - (rotationYZ * f4) - (rotationXZ * f4))
				//				buffer.pos(
				//						(cp.x - (rotationX * f4)) + (rotationXY * f4),
				//						cp.y + (rotationZ * f4),
				//						(cp.z - (rotationYZ * f4)) + (rotationXZ * f4))
				//				buffer.pos(
				//						cp.x + (rotationX * f4) + (rotationXY * f4),
				//						cp.y + (rotationZ * f4),
				//						cp.z + (rotationYZ * f4) + (rotationXZ * f4))
				//				buffer.pos(
				//						(cp.x + (rotationX * f4)) - (rotationXY * f4),
				//						cp.y - (rotationZ * f4),
				//						(cp.z + (rotationYZ * f4)) - (rotationXZ * f4))
				.tex(0, 0)
				.lightmap(lmv, lmv2)
				.color(r, g, b, particleAlpha)
				//				.normal(0.0F, 1.0F, 0.0F)
				.endVertex();
				//				buffer.pos(cp.x - rotationX, cp.y - rotationZ, cp.z - rotationYZ).tex(0, 0).lightmap(240, 240).color(r, g, b, 0.3f).endVertex();
				//				buffer.pos(cp.x, cp.y, cp.z).tex(0, 0).lightmap(240, 240).color(r, g, b, 0.3f).endVertex();
				//				buffer.pos(t1, t2, t3).tex(0, 0).lightmap(240, 240).color(r, g, b, 0.3f).endVertex();
				//				buffer.pos((t1 + (rotationX)) + rotationXY, t2 + rotationZ, (t3 - rotationYZ) + rotationXZ).tex(0, 0).lightmap(240, 240).color(r, g, b, 0.3f).endVertex();
			}
			Tessellator.getInstance().draw();
			//			Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			//When you are done rendering all your boxes reset the offsets. We do not want everything that renders next to still be at 0,0,0 :)

			GlStateManager.popMatrix();
			points.clear();
			//			}
		}
	}
	@Override
	public void onUpdate() {
		//		if(particleAge == 0) {
		//			for(int p = 0; p < particleMaxAge;p++) {
		//				if(p < 1) {
		//					points.add(new Vec3d(posX, posY, posZ));
		//				} else {
		//					final int test = ((rand.nextInt(3) + 1)+1)-3;
		//					final int test2 = rand.nextInt(4);
		//					final float t1 = (float) test/10;//new Float("0." + String.valueOf(test));
		//					final float t2 = new Float("0." + String.valueOf(test2));
		//					final Vec3d lP = points.get(p-1);
		//					points.add(new Vec3d(lP.x, lP.y+(t1), lP.z+(t2)));
		//				}
		//			}
		//		}
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge)
		{
			setExpired();
			//			points.clear();
		}
		move(0,0,0);
	}

	@Override
	public int getFXLayer()
	{
		return 3;
	}

}