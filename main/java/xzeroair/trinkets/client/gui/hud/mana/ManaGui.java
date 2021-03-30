package xzeroair.trinkets.client.gui.hud.mana;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class ManaGui extends Gui {

	public static ResourceLocation background = null;

	private static ResourceLocation manaH = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/mana_horizontal.png");
	private static ResourceLocation manaHAlt = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/mana_horizontal_alt.png");
	private static ResourceLocation manaHAlt2 = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/mana_horizontal_alt2.png");
	private static ResourceLocation manaV = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/mana_vertical.png");
	private static ResourceLocation manaVAlt = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/mana_vertical_alt.png");

	private final Minecraft mc;
	private final Random rand = new Random();
	private float oldMouseX;
	private float oldMouseY;

	public ManaGui(Minecraft mc) {
		this.mc = mc;
	}

	//hmmmm
	public void renderManaGui(int x, int y, int tick, float mana, float maxMana, float cost) {
		final EntityPlayerSP player = mc.player;
		final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		if (TrinketsConfig.CLIENT.MPBar.mana_horizontal) {
			int xOffset = 0;
			int yOffset = 5;
			int barWidth = 100;
			int barHeight = 5;
			int barCutoffWidth = 100;
			int barCutoffHeight = 5;
			int texWidth = 100;
			int texHeight = 15;
			this.drawImageHorizontal(x, y, 0, 0, barCutoffWidth, barCutoffHeight, barWidth, barHeight, texWidth, texHeight);
			int m = (int) (((int) (mana * barWidth) / maxMana) * 1);
			int cutoff = barWidth - m;
			this.drawImageHorizontal(x, y, 0, 5, m, barCutoffHeight, m, barHeight, texWidth, texHeight);

			GlStateManager.pushMatrix();
			int costM = (int) (((int) (cost * barWidth) / maxMana));
			int clampedCost = MathHelper.clamp(costM, costM, m);
			this.drawImage(x + (m), y, 0, 10, cutoff - barCutoffWidth, barCutoffHeight, clampedCost, 5, texWidth, texHeight);
			GlStateManager.popMatrix();

			if (!TrinketsConfig.CLIENT.MPBar.hide_text) {
				GlStateManager.pushMatrix();
				y -= 12;
				x = fontRenderer.drawStringWithShadow("Mana: ", x, y, 0xffffffff);
				mana = Math.round(mana * 100) / 100;
				maxMana = Math.round(MathHelper.clamp(maxMana, 0, maxMana) * 100) / 100;
				x = fontRenderer.drawStringWithShadow((int) mana + "/" + (int) maxMana, x, y, 0xffffffff);
				y += 22;
				x -= 50;
				int percentage = Math.round(((mana * 100) / maxMana) * 100) / 100;
				x = fontRenderer.drawStringWithShadow(percentage + "%", x, (y), 0xffffffff);
				GlStateManager.popMatrix();
			}
		} else {
			int xOffset = 5;
			int yOffset = 0;
			int barWidth = 5;
			int barHeight = 100;
			int barCutoffWidth = 5;
			int barCutoffHeight = 100;
			int texWidth = 15;
			int texHeight = 100;
			this.drawImageVertical(x, y, 0, 0, barCutoffWidth, barCutoffHeight, barWidth, barHeight, texWidth, texHeight);
			int m = (int) (((int) (mana * barHeight) / maxMana) * 1);
			int cutoff = barHeight - m;
			this.drawImageVertical(x, y, 5, cutoff, barCutoffWidth, m, barWidth, m, texWidth, texHeight);

			GlStateManager.pushMatrix();
			int costM = (int) (((int) (cost * barHeight) / maxMana));
			int clampedCost = MathHelper.clamp(costM, costM, m);
			int costOffset = barHeight - m;
			this.drawImage(x + 5, y - m, 10, barHeight + costOffset, 5, clampedCost, barWidth, clampedCost, texWidth, texHeight);
			GlStateManager.popMatrix();

			if (!TrinketsConfig.CLIENT.MPBar.hide_text) {
				GlStateManager.pushMatrix();
				y += 4;
				x -= 18;
				mana = Math.round(mana * 100) / 100;
				maxMana = Math.round(MathHelper.clamp(maxMana, 0, maxMana) * 100) / 100;
				x = fontRenderer.drawStringWithShadow((int) mana + "/" + (int) maxMana, x, y, 0xffffffff);
				y += 10;
				x -= 33;
				String percentage = (Math.round(((mana * 100) / maxMana) * 100) / 100) + "";
				//				x -= 6;
				x = fontRenderer.drawStringWithShadow(percentage + "%", x, (y), 0xffffffff);
				GlStateManager.popMatrix();
			}
		}
	}

	/**
	 * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used
	 * anywhere in vanilla code.
	 *
	 * @param u          Texture U (or x) coordinate, in pixels
	 * @param v          Texture V (or y) coordinate, in pixels
	 * @param uWidth     Width of the rendered part of the texture, in pixels. Parts
	 *                   of the texture outside of it will wrap around
	 * @param vHeight    Height of the rendered part of the texture, in pixels.
	 *                   Parts of the texture outside of it will wrap around
	 * @param tileWidth  total width of the texture
	 * @param tileHeight total height of the texture
	 */
	private void drawImage(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
		boolean flipH = true; // Needs flipped for Horizontal
		boolean flipV = true;
		boolean flipU = false;
		boolean flipUV = false;
		GlStateManager.color(1f, 1F, 1F, 1f);
		//		GlStateManager.color(1f, 0f, 0f, 1F);
		//		GlStateManager.enableAlpha();
		//		GlStateManager.disableTexture2D();
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		int xF = flipH ? x - width : x;
		int xF2 = flipH ? x : x + width;
		int yF = flipV ? y : y - height;
		int yF2 = flipV ? y + height : y;
		double uF = flipU ? u + uWidth : u;
		double uF2 = flipU ? u : u + uWidth;
		double vF = flipUV ? v + vHeight : v;
		double vF2 = flipUV ? v : v + vHeight;

		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(xF, yF2, 0.0D).tex(uF * f, (vF2) * f1).endVertex();
		bufferbuilder.pos(xF2, yF2, 0.0D).tex((uF2) * f, (vF2) * f1).endVertex();
		bufferbuilder.pos(xF2, yF, 0.0D).tex((uF2) * f, vF * f1).endVertex();
		bufferbuilder.pos(xF, yF, 0.0D).tex(uF * f, vF * f1).endVertex();
		//		bufferbuilder.pos(x, y, 0.0D).tex(u * f, (v + vHeight) * f1).endVertex();
		//		bufferbuilder.pos(x + width, y, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
		//		bufferbuilder.pos(x + width, y - height, 0.0D).tex((u + uWidth) * f, v * f1).endVertex();
		//		bufferbuilder.pos(x, y - height, 0.0D).tex(u * f, v * f1).endVertex();
		tessellator.draw();
		//		GlStateManager.disableBlend();
		//		GlStateManager.disableAlpha();
		//		GlStateManager.enableTexture2D();
	}

	private void drawImageReversed(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
		GlStateManager.pushMatrix();
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x - width, y + height, 0.0D).tex(u * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x, y + height, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x, y, 0.0D).tex((u + uWidth) * f, v * f1).endVertex();
		bufferbuilder.pos(x - width, y, 0.0D).tex(u * f, v * f1).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
	}

	private void drawImageHorizontal(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
		mc.getTextureManager().bindTexture(manaH);
		//		mc.getTextureManager().bindTexture(manaHAlt);
		GlStateManager.pushMatrix();
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, 0.0D).tex(u * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + width, y + height, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + width, y, 0.0D).tex((u + uWidth) * f, v * f1).endVertex();
		bufferbuilder.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
	}

	private void drawImageVertical(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
		mc.getTextureManager().bindTexture(manaV);
		GlStateManager.pushMatrix();
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y, 0.0D).tex(u * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + width, y, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
		bufferbuilder.pos(x + width, y - height, 0.0D).tex((u + uWidth) * f, v * f1).endVertex();
		bufferbuilder.pos(x, y - height, 0.0D).tex(u * f, v * f1).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
	}
}
