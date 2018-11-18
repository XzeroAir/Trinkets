package xzeroair.trinkets.client.particles;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class ParticleHeartBeat extends Particle {
	
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
	//private final float scale;
	
	
	public ParticleHeartBeat(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		
		

		this.particleRed = 1.0f;//((float)(Math.random() * 0.20000000298023224D) + 0.8F) * f4;
		this.particleGreen = 0.1f;//((float)(Math.random() * 0.20000000298023224D) + 1F) * f4;
		this.particleBlue = 0.2F;//((float)(Math.random() * 0.20000000298023224D) + 0.8F) * f4;
		this.particleAlpha = 1.0F;
		this.particleMaxAge = 16;
		this.particleAge = 0;
	//	this.particleScale = 0.4F;
	        
	//  this.setParticleTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(greed.toString()));
	}
	@Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
		
			GL11.glPushMatrix();
			GL11.glDepthFunc(GL11.GL_ALWAYS);
			GL11.glColor4d(this.particleRed, this.particleGreen, this.particleBlue, 1.0F);
			GlStateManager.disableLighting();
			RenderHelper.disableStandardItemLighting();
			//Below points to "Minecraft/textures/particle/ not MODID/textures/particle currently not sure how to change it, if you can
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("xat:textures/particle/heartbeat.png"));

			
		int i = (int)(((float)this.particleAge + partialTicks) * 15.0F / (float)this.particleMaxAge);

		if (i <= 15)
		{
			float f = (float)(i % 16) / 16.0F;
			float f1 = f + 0.0625f;
			float f2 = (float)(i / 16) / 16.0F;
			float f3 = f2 + 0.0625f;
			//float f4 effects size/scale
			float f4 = 0.3f;//2.0F * this.size;
			float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
			float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
			float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
			int lmv = 0;
			int lmv2 = 240;
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			RenderHelper.disableStandardItemLighting();
			buffer.begin(7, VERTEX_FORMAT);
			
			
			buffer.pos(
					(double)(f5 - rotationX * f4 - rotationXY * f4),
					(double)(f6 - rotationZ * f4),
					(double)(f7 - rotationYZ * f4 - rotationXZ * f4))
					.tex((double)f1, (double)f3)
					.color(this.particleRed, this.particleGreen, this.particleBlue, 0.5f)
					.lightmap(lmv, lmv2)
					.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(double)(f5 - rotationX * f4 + rotationXY * f4), 
					(double)(f6 + rotationZ * f4), 
					(double)(f7 - rotationYZ * f4 + rotationXZ * f4))
					.tex((double)f1, (double)f2)
					.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(lmv, lmv2)
					.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(double)(f5 + rotationX * f4 + rotationXY * f4), 
					(double)(f6 + rotationZ * f4), 
					(double)(f7 + rotationYZ * f4 + rotationXZ * f4))
					.tex((double)f, (double)f2)
					.color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
					.lightmap(lmv, lmv2)
					.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos(
					(double)(f5 + rotationX * f4 - rotationXY * f4), 
					(double)(f6 - rotationZ * f4), 
					(double)(f7 + rotationYZ * f4 - rotationXZ * f4))
					.tex((double)f, (double)f3)
					.color(this.particleRed, this.particleGreen, this.particleBlue, 0.5f)
					.lightmap(lmv, lmv2)
					.normal(0.0F, 1.0F, 0.0F).endVertex();
			Tessellator.getInstance().draw();
			GlStateManager.enableLighting();
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glPopMatrix();
			GlStateManager.enableLighting();
	
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

	public int getFXLayer()
	{
		return 3;
	}

}
