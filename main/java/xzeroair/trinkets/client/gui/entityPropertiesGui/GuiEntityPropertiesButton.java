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
	ColorHelper color;

	public GuiEntityPropertiesButton(int buttonId, GuiContainer parentGui, int x, int y, int width, int height,
			String buttonText) {
		super(buttonId, x, parentGui.getGuiTop() + y, width, height, buttonText);
		this.parentGui = parentGui;
		color = new ColorHelper().setColor(TrinketsConfig.CLIENT.raceProperties.button.texture.color);
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

				ConfigGuiButtonShared config = TrinketsConfig.CLIENT.raceProperties.button.texture;
				buttonTex = new ResourceLocation(TrinketsConfig.CLIENT.raceProperties.button.texture.image);
				int X = config.x;
				int Y = config.y;
				int width = config.width;
				int height = config.height;
				int texWidth = config.texWidth;
				int texHeight = config.texHeight;

				if (k == 1) {
					mc.getTextureManager().bindTexture(buttonTex);
					DrawingHelper.Draw(x, y, 0, X, Y, texWidth, texHeight, width, height, config.texSizeWidth, config.texSizeHeight, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
				} else {
					mc.getTextureManager().bindTexture(buttonTex);
					DrawingHelper.Draw(x, y, 0, X, Y + texHeight, texWidth, texHeight, width, height, config.texSizeWidth, config.texSizeHeight, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
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
