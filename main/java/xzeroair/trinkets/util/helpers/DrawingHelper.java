package xzeroair.trinkets.util.helpers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DrawingHelper {

	@SideOnly(Side.CLIENT)
	public static void Draw(double x, double y, double z, float u, float v, int uWidth, int vHeight, double width, double height, float tileWidth, float tileHeight) {
		final float f = 1.0F / tileWidth;
		final float f1 = 1.0F / tileHeight;
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, z).tex(u * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + width, y + height, z).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + width, y, z).tex((u + uWidth) * f, v * f1).endVertex();
		bufferbuilder.pos(x, y, z).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}

	@SideOnly(Side.CLIENT)
	public static void Draw(double x, double y, double z, float u, float v, int uWidth, int vHeight, double width, double height, float tileWidth, float tileHeight, float r, float g, float b, float a) {
		Draw(null, x, y, z, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight, r, g, b, a);
	}

	@SideOnly(Side.CLIENT)
	public static void Draw(ResourceLocation texture, double x, double y, double z, float u, float v, int uWidth, int vHeight, double width, double height, float tileWidth, float tileHeight, float r, float g, float b, float a) {
		if (texture != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		}
		if (a < 1) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
			//			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		}
		//		GlStateManager.enableAlpha();
		final float f = 1.0F / tileWidth;
		final float f1 = 1.0F / tileHeight;
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos(x, y + height, z).tex(u * f, (v + vHeight) * f1).color(r, g, b, a).endVertex();
		bufferbuilder.pos(x + width, y + height, z).tex((u + uWidth) * f, (v + vHeight) * f1).color(r, g, b, a).endVertex();
		bufferbuilder.pos(x + width, y, z).tex((u + uWidth) * f, v * f1).color(r, g, b, a).endVertex();
		bufferbuilder.pos(x, y, z).tex(u * f, v * f1).color(r, g, b, a).endVertex();
		tessellator.draw();
		//		GlStateManager.disableAlpha();
		if (a < 1) {
			GlStateManager.disableBlend();
		}
		GlStateManager.color(1, 1, 1);
	}

	@SideOnly(Side.CLIENT)
	public static void startRender() {
		GlStateManager.pushMatrix();
		//		GlStateManager.disableTexture2D();
		//		GlStateManager.enableAlpha();
		final int op1 = 770;
		final int op2 = 771;//32772;

		//		GlStateManager.tryBlendFuncSeparate(768, 769, op1, op2);
		GlStateManager.blendFunc(op1, op2);
		GlStateManager.enableBlend();
	}

	@SideOnly(Side.CLIENT)
	public static void endRender() {
		//		GlStateManager.enableTexture2D();
		//		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		//			GlStateManager.disableLighting();
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	public static void drawEntityOnScreen(int posX, int posY, double scale, boolean flip, float rotation, double mouseX, double mouseY, EntityLivingBase ent) {
		GlStateManager.pushMatrix();
		GlStateManager.enableColorMaterial();
		GlStateManager.translate(posX, posY, 50.0F);
		GlStateManager.scale((-scale), scale, scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		final float f = ent.renderYawOffset;
		final float f1 = ent.rotationYaw;
		final float f2 = ent.rotationPitch;
		final float f3 = ent.prevRotationYawHead;
		final float f4 = ent.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		final float YawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
		final float yaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
		ent.renderYawOffset = flip ? -YawOffset : YawOffset;
		ent.rotationYaw = flip ? -yaw : yaw;
		ent.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		if (flip) {
			GlStateManager.rotate(rotation, 0, 1, 0);
		}
		final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableAlpha();
		GlStateManager.popMatrix();
	}
}
