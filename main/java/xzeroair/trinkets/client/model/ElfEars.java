package xzeroair.trinkets.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.util.Reference;

public class ElfEars extends ModelBase {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/ears.png");

	public ElfEars() {
		textureWidth = 64;
		textureHeight = 64;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		float fscale = 0.30F;
		GlStateManager.scale(fscale, fscale, fscale);
		double x = 0.0;
		double y = -1.5;
		double height = 1;
		double width = 1;
		double z = -0.4;
		float u = 16;
		float v = 0;
		int uWidth = 16;
		int vHeight = 16;
		float tileWidth = 64;
		float tileHeight = 32;
		//		GlStateManager.rotate(-10, 1, 0, 0);
		//		GlStateManager.rotate(25, 0, 0, 1);
		double xR = 0.7;
		double xL = -xR;
		this.DrawRightEar(x + xR, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
		this.DrawRightEar(x + xR, y, z + 0.0001, u, v + 16, uWidth, vHeight, width, height, tileWidth, tileHeight);
		//		GlStateManager.rotate(-50, 0, 0, 1);
		this.DrawLeftEar(x + xL, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
		this.DrawLeftEar(x + xL, y, z + 0.0001, u, v + 16, uWidth, vHeight, width, height, tileWidth, tileHeight);
		GlStateManager.popMatrix();
	}

	public void DrawRightEar(double x, double y, double z, float u, float v, int uWidth, int vHeight, double width, double height, float tileWidth, float tileHeight) {
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		double offset = width;
		double offset2 = width;
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, z).tex(u * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + offset2, y + height, z + offset).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + offset2, y, z + offset).tex((u + uWidth) * f, v * f1).endVertex();
		bufferbuilder.pos(x, y, z).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}

	public void DrawLeftEar(double x, double y, double z, float u, float v, int uWidth, int vHeight, double width, double height, float tileWidth, float tileHeight) {
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		double offset = width;
		double offset2 = -width;
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, z).tex(u * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + offset2, y + height, z + offset).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + offset2, y, z + offset).tex((u + uWidth) * f, v * f1).endVertex();
		bufferbuilder.pos(x, y, z).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}
}
