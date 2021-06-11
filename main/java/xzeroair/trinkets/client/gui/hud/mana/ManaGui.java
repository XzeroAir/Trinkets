package xzeroair.trinkets.client.gui.hud.mana;

import java.util.Random;

import net.minecraft.client.Minecraft;
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
	private static ResourceLocation manaBar = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/mana_bar.png");

	private final Minecraft mc;
	private final Random rand = new Random();
	private float oldMouseX;
	private float oldMouseY;

	public ManaGui(Minecraft mc) {
		this.mc = mc;
	}

	public void renderManaGui(int x, int y, int tick, float mana, float maxMana, float cost) {
		//		final EntityPlayerSP player = mc.player;
		final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		if (TrinketsConfig.CLIENT.MPBar.mana_horizontal) {
			// Actual Size of the Bar
			int barWidth = 104;
			int barHeight = 14;

			// Texture Section Size
			int texWidth = 104;
			int texHeight = 14;

			// UV Wrapped Tex Size
			int texUVWidth = 104;
			int texUVHeight = 42;

			int m = (int) ((mana * 100) / maxMana);//this.percentValue((mana / maxMana), 1F);
			float percent = (m / (100 * 1F));
			int barMana = this.percentValue(barWidth, percent);
			int texMana = this.percentValue(texWidth, percent);

			GlStateManager.pushMatrix();
			mc.getTextureManager().bindTexture(manaBar);
			// Bar Background
			this.drawImage(
					x, y,
					barWidth, barHeight,
					texUVWidth, texUVHeight,
					0, 0,
					texWidth, texHeight,
					false, false, false, false
			);

			// Bar Foreground - Mana
			this.drawImage(
					x, y,
					barMana, barHeight,
					texUVWidth, texUVHeight,
					0, texHeight,
					texMana, texHeight,
					false, false, false, false
			);

			int costM = (int) ((cost * 100) / maxMana);
			float percentCost = costM / (100 * 1F);

			int barCost = MathHelper.clamp(this.percentValue(barMana, percentCost), 0, barMana);
			int texCost = MathHelper.clamp(this.percentValue(texMana, percentCost), 0, texMana);
			int d = (barMana <= (barWidth - 2) ? +2 : 0);
			int xPos = (x + d) + barMana;

			// Bar Foreground - Cost
			this.drawImage(
					xPos, y,
					barCost, barHeight,
					texUVWidth, texUVHeight,
					0, texHeight * 2,
					texCost, texHeight,
					true, false, true, false
			);
			GlStateManager.popMatrix();
			if (!TrinketsConfig.CLIENT.MPBar.hide_text) {
				GlStateManager.pushMatrix();
				y -= 11;
				x += 30;
				mana = Math.round(mana * 100) / 100;
				maxMana = Math.round(MathHelper.clamp(maxMana, 0, maxMana) * 100) / 100;
				x = fontRenderer.drawStringWithShadow((int) mana + "/" + (int) maxMana, x, y, 0xffffffff);
				//				x = fontRenderer.drawStringWithShadow(percentage + "%", x, (y), 0xffffffff);
				GlStateManager.popMatrix();
			}
		} else {
			int barWidth = 104;
			int barHeight = 14;

			// Texture Section Size
			int texWidth = 104;
			int texHeight = 14;

			// UV Wrapped Tex Size
			int texUVWidth = 104;
			int texUVHeight = 42;

			int m = (int) ((mana * 100) / maxMana);//this.percentValue((mana / maxMana), 1F);
			float percent = (m / (100 * 1F));
			int barMana = this.percentValue(barWidth, percent);
			int texMana = this.percentValue(texWidth, percent);

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, 0);
			GlStateManager.rotate(-90, 0, 0, 1);
			GlStateManager.translate(-x, -y, 0);
			mc.getTextureManager().bindTexture(manaBar);
			int xPos = x;
			int yPos = y + (barHeight / 2);
			// Bar Background
			this.drawImage(
					xPos, yPos,
					barWidth, barHeight,
					texUVWidth, texUVHeight,
					0, 0,
					texWidth, texHeight,
					false, false, false, false
			);

			// Bar Foreground - Mana
			this.drawImage(
					xPos, yPos,
					barMana, barHeight,
					texUVWidth, texUVHeight,
					0, texHeight,
					texMana, texHeight,
					false, false, false, false
			);

			int costM = (int) ((cost * 100) / maxMana);
			float percentCost = costM / (100 * 1F);

			int barCost = MathHelper.clamp(this.percentValue(barMana, percentCost), 0, barMana);
			int texCost = MathHelper.clamp(this.percentValue(texMana, percentCost), 0, texMana);
			int d = (barMana <= (barWidth - 2) ? +2 : 0);
			xPos = (xPos + d) + barMana;

			// Bar Foreground - Cost
			this.drawImage(
					xPos, yPos,
					barCost, barHeight,
					texUVWidth, texUVHeight,
					0, texHeight * 2,
					texCost, texHeight,
					true, false, true, false
			);
			GlStateManager.popMatrix();
			if (!TrinketsConfig.CLIENT.MPBar.hide_text) {
				GlStateManager.pushMatrix();
				x -= 22;
				y += 2;
				mana = Math.round(mana * 100) / 100;
				maxMana = Math.round(MathHelper.clamp(maxMana, 0, maxMana) * 100) / 100;
				x = fontRenderer.drawStringWithShadow((int) mana + "", x, y, 0xffffffff);
				x = fontRenderer.drawStringWithShadow("/", x, y, 0xffffffff);
				x = fontRenderer.drawStringWithShadow((int) maxMana + "", x, y, 0xffffffff);
				y += 10;
				x -= 33;
				String percentage = (Math.round(((mana * 100) / maxMana) * 100) / 100) + "";
				x = fontRenderer.drawStringWithShadow(percentage + "%", x, (y), 0xffffffff);
				GlStateManager.popMatrix();
			}
		}
	}

	private int percentValue(int value, float percent) {
		return (int) (value * percent);
	}

	/**
	 * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used
	 * anywhere in vanilla code.
	 *
	 * @param x          Location X
	 * @param y          Location Y
	 * @param width      Rendered Image Width
	 * @param height     Rendered Image Height
	 * @param u          Texture U (or x) coordinate, in pixels
	 * @param v          Texture V (or y) coordinate, in pixels
	 * @param uWidth     Width of the rendered part of the texture, in pixels. Parts
	 *                   of the texture outside of it will wrap around
	 * @param vHeight    Height of the rendered part of the texture, in pixels.
	 *                   Parts of the texture outside of it will wrap around
	 * @param tileWidth  total width of the texture
	 * @param tileHeight total height of the texture
	 */
	private void drawImage(int x, int y, int width, int height, float tileWidth, float tileHeight, float u, float v, int uWidth, int vHeight) {
		this.drawImage(x, y, width, height, tileWidth, tileHeight, u, v, uWidth, vHeight, true, true, false, false);
	}

	private void drawImage(int x, int y, int width, int height, float tileWidth, float tileHeight, float u, float v, int uWidth, int vHeight, boolean flipH, boolean flipV, boolean flipU, boolean flipUV) {
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
		tessellator.draw();
	}
}
