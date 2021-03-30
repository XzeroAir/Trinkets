package xzeroair.trinkets.util.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DrawingHelper {

	@SideOnly(Side.CLIENT)
	public static void Draw(double x, double y, double z, float u, float v, int uWidth, int vHeight, double width, double height, float tileWidth, float tileHeight) {
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, z).tex(u * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + width, y + height, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + width, y, z).tex((u + uWidth) * f, v * f1).endVertex();
		bufferbuilder.pos(x, y, z).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}

	@SideOnly(Side.CLIENT)
	public static void Draw(double x, double y, double z, float u, float v, int uWidth, int vHeight, double width, double height, float tileWidth, float tileHeight, float r, float g, float b, float a) {
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos(x, y + height, z).tex(u * f, (v + vHeight) * f1).color(r, g, b, a).endVertex();
		bufferbuilder.pos(x + width, y + height, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).color(r, g, b, a).endVertex();
		bufferbuilder.pos(x + width, y, z).tex((u + uWidth) * f, v * f1).color(r, g, b, a).endVertex();
		bufferbuilder.pos(x, y, z).tex(u * f, v * f1).color(r, g, b, a).endVertex();
		tessellator.draw();
	}

	@SideOnly(Side.CLIENT)
	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
		drawEntityOnScreen(posX, posY, scale, false, 0, mouseX, mouseY, ent);
	}

	@SideOnly(Side.CLIENT)
	public static void startRender() {
		GlStateManager.pushMatrix();
		//		GlStateManager.disableTexture2D();
		//		GlStateManager.enableAlpha();
		int op1 = 770;
		int op2 = 771;//32772;

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
	public static void drawEntityOnScreen(int posX, int posY, int scale, boolean flip, float rotation, float mouseX, float mouseY, EntityLivingBase ent) {
		GlStateManager.pushMatrix();
		GlStateManager.enableColorMaterial();
		GlStateManager.translate(posX, posY, 50.0F);
		GlStateManager.scale((-scale), scale, scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		float YawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
		float yaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
		ent.renderYawOffset = flip ? -YawOffset : YawOffset;
		ent.rotationYaw = flip ? -yaw : yaw;
		ent.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		if (flip) {
			GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
		}
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
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
