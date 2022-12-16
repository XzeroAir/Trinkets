<<<<<<< Updated upstream
package xzeroair.trinkets.client.particles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.RayTraceHelper.Beam;

@SideOnly(Side.CLIENT)
public class ParticleSmell extends Particle {

	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);

	private Vec3d start;
	private Vec3d end;
	private boolean depth = false;
	private Beam beam;
	List<Vec3d> points = new ArrayList<>();
	Vec3d firstSegment, secondSegment, thirdSegment;

	private boolean first = false;

	public ParticleSmell(World world, double x, double y, double z, float r, float g, float b, float a) {
		super(world, x, y, z);
		particleRed = r;
		particleGreen = g;
		particleBlue = b;
		particleAlpha = a;
		depth = false;
		particleMaxAge = 16;
		particleAge = 0;
	}

	public ParticleSmell(World world, BlockPos pos, float r, float g, float b, float a) {
		super(world, pos.getX(), pos.getY(), pos.getZ());
		particleRed = r;
		particleGreen = g;
		particleBlue = b;
		particleAlpha = a;
		depth = false;
		particleMaxAge = 16;
		particleAge = 0;
	}

	public ParticleSmell(World world, Vec3d start, Vec3d end, int color, float a, boolean bool) {
		super(world, start.x, start.y, start.z);
		final int r = (color & 16711680) >> 16;
		final int g = (color & 65280) >> 8;
		final int b = (color & 255) >> 0;
		particleRed = r / 255.0F;
		particleGreen = g / 255.0F;
		particleBlue = b / 255.0F;
		particleAlpha = a;
		depth = bool;
		particleMaxAge = 1;
		particleAge = 0;
		this.start = start;
		this.end = end;
	}

	public ParticleSmell(World world, Vec3d start, Vec3d end, Beam beam, int color, float a, boolean bool) {
		super(world, start.x, start.y, start.z);
		final int r = (color & 16711680) >> 16;
		final int g = (color & 65280) >> 8;
		final int b = (color & 255) >> 0;
		particleRed = r / 255.0F;
		particleGreen = g / 255.0F;
		particleBlue = b / 255.0F;
		particleAlpha = a;
		depth = bool;
		final int rf = TrinketsConfig.CLIENT.items.DRAGON_EYE.Render_Cooldown;
		//		particleMaxAge = rf;
		particleMaxAge = 16;
		particleAge = 0;
		this.start = start;
		this.end = end;
		this.beam = beam;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

		//		float r = 1f;//particleRed;
		//		float g = 0f;//particleGreen;
		//		float b = 0f;//particleBlue;
		//		float a = 1F;//particleAlpha;
		float r = particleRed;
		float g = particleGreen;
		float b = particleBlue;
		float a = particleAlpha;//MathHelper.clamp(particleAlpha, 0.11F, Float.MAX_VALUE);

		if (depth) {
			//			GlStateManager.enableBlend();
		}
		//		if (!depth) {
		//		}

		final int i = (int) (((particleAge + partialTicks) * 15.0F) / particleMaxAge);

		if (i <= 15) {
			GlStateManager.pushMatrix();
			final float f = (i % 16) / 16.0F;
			final float f1 = f + 0.0625f;
			final float f2 = i / 16 / 16.0F;
			final float f3 = f2 + 0.0625f;
			//float f4 effects size/scale
			final float f4 = 0.25f;//2.0F * this.size;
			final float f5 = (float) ((prevPosX + ((posX - prevPosX) * 0.5)) - interpPosX);
			final float f6 = (float) ((prevPosY + ((posY - prevPosY) * 0.5)) - interpPosY);
			final float f7 = (float) ((prevPosZ + ((posZ - prevPosZ) * 0.5)) - interpPosZ);
			final float f8 = (float) ((end.x + ((end.x - end.x) * 0.5)) - interpPosX);
			final float f9 = (float) ((end.y + ((end.y - end.y) * 0.5)) - interpPosY);
			final float f10 = (float) ((end.z + ((end.z - end.z) * 0.5)) - interpPosZ);
			final int lmv = 0;
			final int lmv2 = 240;
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			if (!depth) {
				GlStateManager.disableDepth();
			}
			buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			GlStateManager.glLineWidth(6f);
			buffer.pos(f5, f6, f7)
					.tex(0, 0)
					.lightmap(lmv, lmv2)
					.color(r, g, b, a)
					.endVertex();
			buffer.pos(f8, f9, f10)
					.tex(0, 0)
					.lightmap(lmv, lmv2)
					.color(r, g, b, a)
					.endVertex();
			Tessellator.getInstance().draw();
			if (!depth) {
				GlStateManager.enableDepth();
			}
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void onUpdate() {
		if ((posX != prevPosX) || (posY != prevPosY) || (posZ != prevPosZ)) {
			this.setExpired();
		}
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

=======
package xzeroair.trinkets.client.particles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.RayTraceHelper.Beam;

@SideOnly(Side.CLIENT)
public class ParticleSmell extends Particle {

	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);

	private Vec3d start;
	private Vec3d end;
	private boolean depth = false;
	private Beam beam;
	List<Vec3d> points = new ArrayList<>();
	Vec3d firstSegment, secondSegment, thirdSegment;

	private boolean first = false;

	public ParticleSmell(World world, double x, double y, double z, float r, float g, float b, float a) {
		super(world, x, y, z);
		particleRed = r;
		particleGreen = g;
		particleBlue = b;
		particleAlpha = a;
		depth = false;
		particleMaxAge = 16;
		particleAge = 0;
	}

	public ParticleSmell(World world, BlockPos pos, float r, float g, float b, float a) {
		super(world, pos.getX(), pos.getY(), pos.getZ());
		particleRed = r;
		particleGreen = g;
		particleBlue = b;
		particleAlpha = a;
		depth = false;
		particleMaxAge = 16;
		particleAge = 0;
	}

	public ParticleSmell(World world, Vec3d start, Vec3d end, int color, float a, boolean bool) {
		super(world, start.x, start.y, start.z);
		final int r = (color & 16711680) >> 16;
		final int g = (color & 65280) >> 8;
		final int b = (color & 255) >> 0;
		particleRed = r / 255.0F;
		particleGreen = g / 255.0F;
		particleBlue = b / 255.0F;
		particleAlpha = a;
		depth = bool;
		particleMaxAge = 1;
		particleAge = 0;
		this.start = start;
		this.end = end;
	}

	public ParticleSmell(World world, Vec3d start, Vec3d end, Beam beam, int color, float a, boolean bool) {
		super(world, start.x, start.y, start.z);
		final int r = (color & 16711680) >> 16;
		final int g = (color & 65280) >> 8;
		final int b = (color & 255) >> 0;
		particleRed = r / 255.0F;
		particleGreen = g / 255.0F;
		particleBlue = b / 255.0F;
		particleAlpha = a;
		depth = bool;
		final int rf = TrinketsConfig.CLIENT.items.DRAGON_EYE.Render_Cooldown;
		//		particleMaxAge = rf;
		particleMaxAge = 16;
		particleAge = 0;
		this.start = start;
		this.end = end;
		this.beam = beam;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

		//		float r = 1f;//particleRed;
		//		float g = 0f;//particleGreen;
		//		float b = 0f;//particleBlue;
		//		float a = 1F;//particleAlpha;
		float r = particleRed;
		float g = particleGreen;
		float b = particleBlue;
		float a = particleAlpha;//MathHelper.clamp(particleAlpha, 0.11F, Float.MAX_VALUE);

		if (depth) {
			//			GlStateManager.enableBlend();
		}
		//		if (!depth) {
		//		}

		final int i = (int) (((particleAge + partialTicks) * 15.0F) / particleMaxAge);

		if (i <= 15) {
			GlStateManager.pushMatrix();
			final float f = (i % 16) / 16.0F;
			final float f1 = f + 0.0625f;
			final float f2 = i / 16 / 16.0F;
			final float f3 = f2 + 0.0625f;
			//float f4 effects size/scale
			final float f4 = 0.25f;//2.0F * this.size;
			final float f5 = (float) ((prevPosX + ((posX - prevPosX) * 0.5)) - interpPosX);
			final float f6 = (float) ((prevPosY + ((posY - prevPosY) * 0.5)) - interpPosY);
			final float f7 = (float) ((prevPosZ + ((posZ - prevPosZ) * 0.5)) - interpPosZ);
			final float f8 = (float) ((end.x + ((end.x - end.x) * 0.5)) - interpPosX);
			final float f9 = (float) ((end.y + ((end.y - end.y) * 0.5)) - interpPosY);
			final float f10 = (float) ((end.z + ((end.z - end.z) * 0.5)) - interpPosZ);
			final int lmv = 0;
			final int lmv2 = 240;
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			if (!depth) {
				GlStateManager.disableDepth();
			}
			buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
			GlStateManager.glLineWidth(6f);
			buffer.pos(f5, f6, f7)
					.tex(0, 0)
					.lightmap(lmv, lmv2)
					.color(r, g, b, a)
					.endVertex();
			buffer.pos(f8, f9, f10)
					.tex(0, 0)
					.lightmap(lmv, lmv2)
					.color(r, g, b, a)
					.endVertex();
			Tessellator.getInstance().draw();
			if (!depth) {
				GlStateManager.enableDepth();
			}
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void onUpdate() {
		if ((posX != prevPosX) || (posY != prevPosY) || (posZ != prevPosZ)) {
			this.setExpired();
		}
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

>>>>>>> Stashed changes
}