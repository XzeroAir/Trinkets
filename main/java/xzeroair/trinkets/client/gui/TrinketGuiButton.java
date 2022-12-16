package xzeroair.trinkets.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.baubles.BaublesHelperFunctions;
import xzeroair.trinkets.util.config.gui.ConfigGuiButtonShared;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class TrinketGuiButton extends GuiButton {

	public static ResourceLocation openButtonTex = null;
	public static ResourceLocation closeButtonTex = null;

	private final GuiContainer parentGui;
	private final ColorHelper color;

	public TrinketGuiButton(int buttonId, GuiContainer parentGui, int x, int y, int width, int height,
			String buttonText) {
		super(buttonId, x, parentGui.getGuiTop() + y, width, height, buttonText);
		this.parentGui = parentGui;
		color = new ColorHelper();
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		final boolean flag = mc.getMinecraft().player.getRecipeBook().isGuiOpen();
		final boolean pressed = flag ? false : super.mousePressed(mc, mouseX - parentGui.getGuiLeft(), mouseY);
		final boolean baublesLoaded = Loader.isModLoaded("baubles");
		if (pressed) {
			if (parentGui instanceof GuiInventory) {
				NetworkHandler.INSTANCE.sendToServer(new OpenTrinketGui());
			} else {
				if (baublesLoaded) {
					BaublesHelperFunctions.mousePressedHelper(parentGui, id);
				} else {
					if (id == 9999) {

					} else {
						((TrinketGui) parentGui).displayNormalInventory();
						NetworkHandler.sendToServer(new OpenTrinketGui(99));
					}
				}
			}
		}
		return pressed;
	}

	@SuppressWarnings("static-access")
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (visible && !mc.getMinecraft().player.getRecipeBook().isGuiOpen()) {
			final int x = this.x + parentGui.getGuiLeft();

			final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
			if (id == ID) {
				final FontRenderer fontrenderer = mc.fontRenderer;
				hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
						&& (mouseY < (y + height));
				final int k = this.getHoverState(hovered);

				GlStateManager.pushMatrix();

				openButtonTex = new ResourceLocation(TrinketsConfig.CLIENT.GUI.button.open.image);
				closeButtonTex = new ResourceLocation(TrinketsConfig.CLIENT.GUI.button.close.image);

				final ConfigGuiButtonShared open = TrinketsConfig.CLIENT.GUI.button.open;
				final ConfigGuiButtonShared close = TrinketsConfig.CLIENT.GUI.button.close;
				final float[] rgbOpen = ColorHelper.getRGBColor(open.color);
				final float[] rgbClose = ColorHelper.getRGBColor(close.color);
				if (k == 1) {
					if (parentGui instanceof TrinketGui) {
						mc.getTextureManager().bindTexture(closeButtonTex);
						DrawingHelper.Draw(
								x, y, 0,
								close.x, close.y,
								close.texWidth, close.texHeight,
								close.width, close.height,
								close.texSizeWidth, close.texSizeHeight,
								rgbClose[0], rgbClose[1], rgbClose[2], 1F
						);
					} else {
						mc.getTextureManager().bindTexture(openButtonTex);
						DrawingHelper.Draw(
								x, y, 0,
								open.x, open.y,
								open.texWidth, open.texHeight,
								open.width, open.height,
								open.texSizeWidth, open.texSizeHeight,
								rgbOpen[0], rgbOpen[1], rgbOpen[2], 1F
						);
					}
				} else {
					if (parentGui instanceof TrinketGui) {
						mc.getTextureManager().bindTexture(openButtonTex);
						DrawingHelper.Draw(
								x, y, 0,
								open.x, open.y,
								open.texWidth, open.texHeight,
								open.width, open.height,
								open.texSizeWidth, open.texSizeHeight,
								rgbOpen[0], rgbOpen[1], rgbOpen[2], 1F
						);
					} else {
						mc.getTextureManager().bindTexture(closeButtonTex);
						DrawingHelper.Draw(
								x, y, 0,
								close.x, close.y,
								close.texWidth, close.texHeight,
								close.width, close.height,
								close.texSizeWidth, close.texSizeHeight,
								rgbClose[0], rgbClose[1], rgbClose[2], 1F
						);
					}
					this.drawCenteredString(
							fontrenderer, I18n.format(displayString), x + 5, y + height,
							0xffffff
					);
				}
				GlStateManager.popMatrix();
			}
			if ((Loader.isModLoaded("baubles"))) {
				if (id == 55) {
					final FontRenderer fontrenderer = mc.fontRenderer;
					mc.getTextureManager().bindTexture(BaublesHelperFunctions.getBaublesResourceLocation());
					hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
							&& (mouseY < (y + height));
					final int k = this.getHoverState(hovered);
					GlStateManager.pushMatrix();
					GlStateManager.translate(0, 0, 200);
					if (k == 1) {
						this.drawTexturedModalRect(x, y, 200, 48, 10, 10);
					} else {
						this.drawTexturedModalRect(x, y, 210, 48, 10, 10);
						this.drawCenteredString(
								fontrenderer, I18n.format(displayString), x + 5,
								y + height, 0xffffff
						);
					}
					GlStateManager.popMatrix();
				}
			}
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
}
