<<<<<<< Updated upstream
package xzeroair.trinkets.client.gui.entityPropertiesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.gui.ConfigGuiButtonShared;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class GuiEntityPropertiesButton extends GuiButton {

	public static ResourceLocation buttonTex = null;

	private final GuiContainer parentGui;

	public GuiEntityPropertiesButton(int buttonId, GuiContainer parentGui, int x, int y, int width, int height,
			String buttonText) {
		super(buttonId, x, parentGui.getGuiTop() + y, width, height, buttonText);
		this.parentGui = parentGui;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		final boolean flag = mc.player.getRecipeBook().isGuiOpen();
		final boolean pressed = flag ? false : super.mousePressed(mc, mouseX - parentGui.getGuiLeft(), mouseY);
		if (pressed) {
			//			mc.player.openGui(Trinkets.instance, 1, mc.player.world, 0, 0, 0);
			mc.player.openGui(Trinkets.instance, 2, mc.player.world, 0, 0, 0);
		}
		return pressed;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (visible && !mc.player.getRecipeBook().isGuiOpen()) {
			final int x = this.x + parentGui.getGuiLeft();

			final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
			if (id == 69) {
				final FontRenderer fontrenderer = mc.fontRenderer;
				hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
						&& (mouseY < (y + height));
				final int k = this.getHoverState(hovered);
				GlStateManager.pushMatrix();

				final ConfigGuiButtonShared config = TrinketsConfig.CLIENT.raceProperties.button.texture;
				buttonTex = new ResourceLocation(TrinketsConfig.CLIENT.raceProperties.button.texture.image);
				final int X = config.x;
				final int Y = config.y;
				final int width = config.width;
				final int height = config.height;
				final int texWidth = config.texWidth;
				final int texHeight = config.texHeight;
				final float[] rgb = ColorHelper.getRGBColor(TrinketsConfig.CLIENT.raceProperties.button.texture.color);
				if (k == 1) {
					mc.getTextureManager().bindTexture(buttonTex);
					DrawingHelper.Draw(
							x, y, 0,
							X, Y,
							texWidth, texHeight,
							width, height,
							config.texSizeWidth, config.texSizeHeight,
							rgb[0],
							rgb[1],
							rgb[2],
							1F
					);
				} else {
					mc.getTextureManager().bindTexture(buttonTex);
					DrawingHelper.Draw(
							x, y, 0,
							X, Y + texHeight,
							texWidth, texHeight,
							width, height,
							config.texSizeWidth, config.texSizeHeight,
							rgb[0],
							rgb[1],
							rgb[2],
							1F
					);
					this.drawCenteredString(
							fontrenderer, I18n.format(displayString), x + 5, y + height,
							0xffffff
					);
				}
				GlStateManager.popMatrix();
			}
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
}
=======
package xzeroair.trinkets.client.gui.entityPropertiesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.gui.ConfigGuiButtonShared;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class GuiEntityPropertiesButton extends GuiButton {

	public static ResourceLocation buttonTex = null;

	private final GuiContainer parentGui;

	public GuiEntityPropertiesButton(int buttonId, GuiContainer parentGui, int x, int y, int width, int height,
			String buttonText) {
		super(buttonId, x, parentGui.getGuiTop() + y, width, height, buttonText);
		this.parentGui = parentGui;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		final boolean flag = mc.player.getRecipeBook().isGuiOpen();
		final boolean pressed = flag ? false : super.mousePressed(mc, mouseX - parentGui.getGuiLeft(), mouseY);
		if (pressed) {
			//			mc.player.openGui(Trinkets.instance, 1, mc.player.world, 0, 0, 0);
			mc.player.openGui(Trinkets.instance, 2, mc.player.world, 0, 0, 0);
		}
		return pressed;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (visible && !mc.player.getRecipeBook().isGuiOpen()) {
			final int x = this.x + parentGui.getGuiLeft();

			final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
			if (id == 69) {
				final FontRenderer fontrenderer = mc.fontRenderer;
				hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
						&& (mouseY < (y + height));
				final int k = this.getHoverState(hovered);
				GlStateManager.pushMatrix();

				final ConfigGuiButtonShared config = TrinketsConfig.CLIENT.raceProperties.button.texture;
				buttonTex = new ResourceLocation(TrinketsConfig.CLIENT.raceProperties.button.texture.image);
				final int X = config.x;
				final int Y = config.y;
				final int width = config.width;
				final int height = config.height;
				final int texWidth = config.texWidth;
				final int texHeight = config.texHeight;
				final float[] rgb = ColorHelper.getRGBColor(TrinketsConfig.CLIENT.raceProperties.button.texture.color);
				if (k == 1) {
					mc.getTextureManager().bindTexture(buttonTex);
					DrawingHelper.Draw(
							x, y, 0,
							X, Y,
							texWidth, texHeight,
							width, height,
							config.texSizeWidth, config.texSizeHeight,
							rgb[0],
							rgb[1],
							rgb[2],
							1F
					);
				} else {
					mc.getTextureManager().bindTexture(buttonTex);
					DrawingHelper.Draw(
							x, y, 0,
							X, Y + texHeight,
							texWidth, texHeight,
							width, height,
							config.texSizeWidth, config.texSizeHeight,
							rgb[0],
							rgb[1],
							rgb[2],
							1F
					);
					this.drawCenteredString(
							fontrenderer, I18n.format(displayString), x + 5, y + height,
							0xffffff
					);
				}
				GlStateManager.popMatrix();
			}
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
}
>>>>>>> Stashed changes
