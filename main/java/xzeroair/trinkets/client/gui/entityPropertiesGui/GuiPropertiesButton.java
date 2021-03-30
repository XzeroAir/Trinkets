package xzeroair.trinkets.client.gui.entityPropertiesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.util.TrinketsConfig;

public class GuiPropertiesButton extends GuiButton {

	public GuiPropertiesButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		final boolean flag = mc.player.getRecipeBook().isGuiOpen();
		final boolean pressed = flag ? false : super.mousePressed(mc, mouseX, mouseY);
		if (pressed) {
			if (id == 1) {
				mc.player.openGui(Trinkets.instance, 1, mc.player.world, 0, 0, 0);
			}
		}
		return pressed;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (visible && !mc.player.getRecipeBook().isGuiOpen()) {
			int k = this.getHoverState(hovered);
			if (id == 2) {
				EntityProperties properties = Capabilities.getEntityRace(mc.player);
				displayString = "" + properties.showTraits();
			}
			if (id == 4) {
				if (TrinketsConfig.CLIENT.MPBar.mana_horizontal) {
					displayString = "H";
				} else {
					displayString = "V";
				}
			}
			if (id == 999) {
			}
			//			final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
			//			if (id == 69) {
			super.drawButton(mc, mouseX, mouseY, partialTicks);
			//				final FontRenderer fontrenderer = mc.fontRenderer;
			//				hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
			//						&& (mouseY < (y + height));
			//				final int k = this.getHoverState(hovered);
			//				GlStateManager.pushMatrix();
			//
			//				ConfigGuiButtonShared config = TrinketsConfig.CLIENT.Hud.button.texture;
			//				buttonTex = new ResourceLocation(TrinketsConfig.CLIENT.Hud.button.texture.image);
			//				ColorHelper color = new ColorHelper(TrinketsConfig.CLIENT.Hud.button.texture.color);
			//				int X = config.x;
			//				int Y = config.y;
			//				int width = config.width;
			//				int height = config.height;
			//				int texWidth = config.texWidth;
			//				int texHeight = config.texHeight;
			//
			//				if (k == 1) {
			//					mc.getTextureManager().bindTexture(buttonTex);
			//					DrawingHelper.Draw(x, y, 0, X, Y, texWidth, texHeight, width, height, config.texSizeWidth, config.texSizeHeight, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
			//				} else {
			//					mc.getTextureManager().bindTexture(buttonTex);
			//					DrawingHelper.Draw(x, y, 0, X, Y + texHeight, texWidth, texHeight, width, height, config.texSizeWidth, config.texSizeHeight, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
			//					this.drawCenteredString(
			//							fontrenderer, I18n.format(displayString), x + 5, y + height,
			//							0xffffff
			//					);
			//				}
			//				GlStateManager.popMatrix();
			//			}
			//			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
}
