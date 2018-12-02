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

		this.particleRed = colorRed;
		this.particleGreen = colorGreen;
		this.particleBlue = colorBlue;
		this.particleAlpha = 1.0F;
		this.particleMaxAge = 16;
		this.particleAge = 0;
	}
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{

		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		GlStateManager.color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("xat:textures/particle/greed.png"));

		int i = (int)((((float)this.particleAge + partialTicks) * 15.0F) / (float)this.particleMaxAge);

		if (i <= 15)
		{
			float f = (float)(i % 16) / 16.0F;
			float f1 = f + 0.0625f;
			float f2 = (float)(i / 16) / 16.0F;
			float f3 = f2 + 0.0625f;
			float f4 = 0.25f;//2.0F * this.size;
			float f5 = (float)((this.prevPosX + ((this.posX - this.prevPosX) * (double)partialTicks)) - interpPosX);
			float f6 = (float)((this.prevPosY + ((this.posY - this.prevPosY) * (double)partialTicks)) - interpPosY);
			float f7 = (float)((this.prevPosZ + ((this.posZ - this.prevPosZ) * (double)partialTicks)) - interpPosZ);
			int lmv = 0;
			int lmv2 = 240;
			buffer.begin(7, VERTEX_FORMAT);

			buffer.pos(
					(double)(f5 - (rotationX * f4) - (rotationXY * f4)),
					(double)(f6 - (rotationZ * f4)),
					(double)(f7 - (rotationYZ * f4) - (rotationXZ * f4)))
			.tex((double)f1, (double)f3)
			.color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(double)((f5 - (rotationX * f4)) + (rotationXY * f4)),
					(double)(f6 + (rotationZ * f4)),
					(double)((f7 - (rotationYZ * f4)) + (rotationXZ * f4)))
			.tex((double)f1, (double)f2)
			.color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(double)(f5 + (rotationX * f4) + (rotationXY * f4)),
					(double)(f6 + (rotationZ * f4)),
					(double)(f7 + (rotationYZ * f4) + (rotationXZ * f4)))
			.tex((double)f, (double)f2)
			.color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(double)((f5 + (rotationX * f4)) - (rotationXY * f4)),
					(double)(f6 - (rotationZ * f4)),
					(double)((f7 + (rotationYZ * f4)) - (rotationXZ * f4)))
			.tex((double)f, (double)f3)
			.color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
			.lightmap(lmv, lmv2)
			.normal(0.0F, 1.0F, 0.0F).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.enableDepth();
			GlStateManager.popMatrix();
		}
	}
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setExpired();
		}
	}

	@Override
	public int getFXLayer()
	{
		return 3;
	}
}
