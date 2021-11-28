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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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

	public void renderManaGui(RenderGameOverlayEvent event, int x, int y, int tick, float mana, float maxMana, float cost) {
		//		final EntityPlayerSP player = mc.player;
		final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		//		if (TrinketsConfig.CLIENT.MPBar.mana_horizontal) {
		// Actual Size of the Bar
		final int barWidth = TrinketsConfig.CLIENT.MPBar.width;
		final int barHeight = TrinketsConfig.CLIENT.MPBar.height;

		// Texture Section Size
		final int texWidth = barWidth;
		final int texHeight = barHeight;

		// UV Wrapped Tex Size
		final int texUVWidth = barWidth;
		final int texUVHeight = barHeight * 2;

		if (mana > maxMana) {
			mana = maxMana;
		}
		if (cost > mana) {
			cost = mana;
		}

		//		final int m = (int) (((mana) * 100) / maxMana);//(int) ((mana * 100) / maxMana); // This turns into a value between 0 and 100
		//		final float percent = (m / (100 * 1F)); // this turns into a decimal between 0 and 1
		//		final int barMana = m;//this.percentValue(barWidth, percent); // Initial Bar Width is 104
		//		final int texMana = this.percentValue(texWidth, percent);
		final float test = ((((mana - cost) * 100) / maxMana) * 0.01F);
		final int currentManaWithCost = (int) (barWidth * test);//(int) ((cost * 100) / maxMana);
		//		final float percentCost = costM / (100 * 1F);

		//		final int barCost = MathHelper.clamp(this.percentValue(barMana, percentCost), 0, barMana);
		//		final int texCost = MathHelper.clamp(this.percentValue(texMana, percentCost), 0, texMana);
		//		final int currentManaWithCost = costM;//(int) (barCost * percentCost);
		//		System.out.println(currentManaWithCost);
		//		if (cost > 0) {
		//			final int testCost = (int) ((barWidth * percent) * percentCost);//(int) MathHelper.clamp(barMana * percent, 0, barMana);
		//			System.out.println(cost + " | " + percentCost + " | " + barCost + " | " + barMana + " | " + percent + " | " + costM);
		//		}

		//		final int d = (barMana <= (barWidth - 2) ? +2 : 0);
		//		final int xPos = (x + d) + barMana;

		mc.getTextureManager().bindTexture(manaBar);
		GlStateManager.translate(x, y, 0);
		//		System.out.println(scale);
		try {
			final int hs = mc.displayHeight;
			final int ws = mc.displayWidth;
			final int hsf = event.getResolution().getScaledHeight();
			final int wsf = event.getResolution().getScaledWidth();
			final int sF = event.getResolution().getScaleFactor();
			final float scaleH = ((hsf * 1F) / hs);
			final float scaleW = ((wsf * 1F) / ws);
			GlStateManager.scale(scaleW * sF, scaleH * sF, 0);
			//						GlStateManager.scale(0.5, 0.5, 0.5);
		} catch (final Exception e) {
		}
		if (!TrinketsConfig.CLIENT.MPBar.mana_horizontal) {
			GlStateManager.rotate(-90, 0, 0, 1);
		}
		GlStateManager.translate(-x, -y, 0);
		GlStateManager.enableBlend();
		// Bar Background
		drawModalRectWithCustomSizedTexture(x, y, 0, 0, barWidth, barHeight, texUVWidth, texUVHeight);
		if (mana != 0) {
			drawModalRectWithCustomSizedTexture(x, y, 0, texHeight, currentManaWithCost, barHeight, texUVWidth, texUVHeight);
		}
		// Bar Foreground - Cost
		//		if (barCost != 0) {
		//			//			this.drawImage(
		//			//					xPos, 0,
		//			//					barCost, barHeight,
		//			//					texUVWidth, texUVHeight,
		//			//					0, texHeight * 2,
		//			//					texCost, texHeight,
		//			//					true, false, true, false
		//			//			);
		//			//			drawModalRectWithCustomSizedTexture(xPos - barCost, y, 0, texHeight * 2, barCost, barHeight, texCost, texUVHeight);
		//		}
		if (!TrinketsConfig.CLIENT.MPBar.hide_text) {
			GlStateManager.pushMatrix();
			// TEXT is 7 high, 5 wide in pixels
			//			y -= 11; // barHeight / 2
			//			y += (barHeight / 4) + 3;
			mana = Math.round(mana * 100) / 100;
			maxMana = Math.round(MathHelper.clamp(maxMana, 0, maxMana) * 100) / 100;
			final String txt = (int) mana + "/" + (int) maxMana;
			//			x -= txt.length() * 6;
			x += (barWidth / 2) - ((txt.length() * 6) / 2);
			x += 1;
			y += (barHeight / 2) - 4;
			x = fontRenderer.drawStringWithShadow(txt, x, y, 0xffffffff);
			GlStateManager.disableAlpha();
			GlStateManager.popMatrix();
			//			mc.getTextureManager().bindTexture(manaBar);
			//			y -= (barHeight / 4) + 3;
			x -= (barWidth / 2) - ((txt.length() * 6) / 2);
			x -= 1;
			y -= (barHeight / 2) - 4;
		}
		GlStateManager.disableBlend();

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
		final float f = 1.0F / tileWidth;
		final float f1 = 1.0F / tileHeight;
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		final int xF = flipH ? x - width : x;
		final int xF2 = flipH ? x : x + width;
		final int yF = flipV ? y : y - height;
		final int yF2 = flipV ? y + height : y;
		final double uF = flipU ? u + uWidth : u;
		final double uF2 = flipU ? u : u + uWidth;
		final double vF = flipUV ? v + vHeight : v;
		final double vF2 = flipUV ? v : v + vHeight;

		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(xF, yF2, 0.0D).tex(uF * f, (vF2) * f1).endVertex();
		bufferbuilder.pos(xF2, yF2, 0.0D).tex((uF2) * f, (vF2) * f1).endVertex();
		bufferbuilder.pos(xF2, yF, 0.0D).tex((uF2) * f, vF * f1).endVertex();
		bufferbuilder.pos(xF, yF, 0.0D).tex(uF * f, vF * f1).endVertex();
		tessellator.draw();
	}
}
