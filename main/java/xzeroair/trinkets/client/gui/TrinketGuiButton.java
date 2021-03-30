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
import xzeroair.trinkets.network.trinketcontainer.OpenDefaultInventory;
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
	private ColorHelper color;

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
						NetworkHandler.INSTANCE.sendToServer(new OpenDefaultInventory());
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

			//			if (id == 9999) {
			//				mc.getTextureManager().bindTexture(new ResourceLocation("xat:textures/items/weightless_stone.png"));
			//				this.TrinketDrawButton(x, y, 16, 16, 16, 16, 0.0625f, 1, 1, 1, 1);
			//			}

			final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
			if (id == ID) {
				final FontRenderer fontrenderer = mc.fontRenderer;
				hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
						&& (mouseY < (y + height));
				final int k = this.getHoverState(hovered);
				//				GlStateManager.enableBlend();
				//				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				//				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				GlStateManager.pushMatrix();

				openButtonTex = new ResourceLocation(TrinketsConfig.CLIENT.GUI.button.open.image);
				closeButtonTex = new ResourceLocation(TrinketsConfig.CLIENT.GUI.button.close.image);

				ConfigGuiButtonShared open = TrinketsConfig.CLIENT.GUI.button.open;
				ConfigGuiButtonShared close = TrinketsConfig.CLIENT.GUI.button.close;
				ColorHelper openColor = color.setColor(open.color);
				ColorHelper closeColor = color.setColor(close.color);
				if (k == 1) {
					if (parentGui instanceof TrinketGui) {
						mc.getTextureManager().bindTexture(closeButtonTex);
						DrawingHelper.Draw(x, y, 0, close.x, close.y, close.texWidth, close.texHeight, close.width, close.height, close.texSizeWidth, close.texSizeHeight, closeColor.getRed(), closeColor.getGreen(), closeColor.getBlue(), 1F);
					} else {
						mc.getTextureManager().bindTexture(openButtonTex);
						DrawingHelper.Draw(x, y, 0, open.x, open.y, open.texWidth, open.texHeight, open.width, open.height, open.texSizeWidth, open.texSizeHeight, openColor.getRed(), openColor.getGreen(), openColor.getBlue(), 1F);
					}
				} else {
					if (parentGui instanceof TrinketGui) {
						mc.getTextureManager().bindTexture(openButtonTex);
						DrawingHelper.Draw(x, y, 0, open.x, open.y, open.texWidth, open.texHeight, open.width, open.height, open.texSizeWidth, open.texSizeHeight, openColor.getRed(), openColor.getGreen(), openColor.getBlue(), 1F);
					} else {
						mc.getTextureManager().bindTexture(closeButtonTex);
						DrawingHelper.Draw(x, y, 0, close.x, close.y, close.texWidth, close.texHeight, close.width, close.height, close.texSizeWidth, close.texSizeHeight, closeColor.getRed(), closeColor.getGreen(), closeColor.getBlue(), 1F);
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
					//					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
							&& (mouseY < (y + height));
					final int k = this.getHoverState(hovered);
					//					GlStateManager.enableBlend();
					//					GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
					//					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

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
