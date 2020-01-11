package xzeroair.trinkets.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleGreed extends Particle {

	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);

	public ParticleGreed(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float colorRed, float colorGreen, float colorBlue) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

		particleRed = colorRed;
		particleGreen = colorGreen;
		particleBlue = colorBlue;
		particleAlpha = 1.0F;
		particleMaxAge = 16;
		particleAge = 0;
	}
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{

		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		GlStateManager.color(particleRed, particleGreen, particleBlue, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("xat:textures/particle/greed.png"));


		final int i = (int)(((particleAge + partialTicks) * 15.0F) / particleMaxAge);

		if (i <= 15)
		{
			final float f = (i % 16) / 16.0F;
			final float f1 = f + 0.0625f;
			final float f2 = i / 16 / 16.0F;
			final float f3 = f2 + 0.0625f;
			//float f4 effects size/scale
			final float f4 = 0.25f;//2.0F * this.size;
			final float f5 = (float)((prevPosX + ((posX - prevPosX) * partialTicks)) - interpPosX);
			final float f6 = (float)((prevPosY + ((posY - prevPosY) * partialTicks)) - interpPosY);
			final float f7 = (float)((prevPosZ + ((posZ - prevPosZ) * partialTicks)) - interpPosZ);
			final int lmv = 0;
			final int lmv2 = 240;
			buffer.begin(7, VERTEX_FORMAT);

			buffer.pos(
					f5 - (rotationX * f4) - (rotationXY * f4),
					f6 - (rotationZ * f4),
					f7 - (rotationYZ * f4) - (rotationXZ * f4))
			.tex(f1, f3)
			.color(particleRed, particleGreen, particleBlue, 1.0F)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(f5 - (rotationX * f4)) + (rotationXY * f4),
					f6 + (rotationZ * f4),
					(f7 - (rotationYZ * f4)) + (rotationXZ * f4))
			.tex(f1, f2)
			.color(particleRed, particleGreen, particleBlue, 1.0F)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					f5 + (rotationX * f4) + (rotationXY * f4),
					f6 + (rotationZ * f4),
					f7 + (rotationYZ * f4) + (rotationXZ * f4))
			.tex(f, f2)
			.color(particleRed, particleGreen, particleBlue, 1.0F)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(f5 + (rotationX * f4)) - (rotationXY * f4),
					f6 - (rotationZ * f4),
					(f7 + (rotationYZ * f4)) - (rotationXZ * f4))
			.tex(f, f3)
			.color(particleRed, particleGreen, particleBlue, 1.0F)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.enableDepth();
			GlStateManager.popMatrix();
		}
	}
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge)
		{
			setExpired();
		}
	}

	@Override
	public int getFXLayer()
	{
		return 3;
	}

}
