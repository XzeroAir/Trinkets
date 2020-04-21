package xzeroair.trinkets.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.trinketcontainer.OpenDefaultInventory;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compatibility.BaublesHelperFunctions;
import xzeroair.trinkets.util.helpers.ColorHelper;

public class TrinketGuiButton extends GuiButton {

	public static ResourceLocation openButtonTex = null;
	public static ResourceLocation closeButtonTex = null;

	private final GuiContainer parentGui;

	public TrinketGuiButton(int buttonId, GuiContainer parentGui, int x, int y, int width, int height,
			String buttonText) {
		super(buttonId, x, parentGui.getGuiTop() + y, width, height, buttonText);
		this.parentGui = parentGui;
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

			if (id == 9999) {
				mc.getTextureManager().bindTexture(new ResourceLocation("xat:textures/items/weightless_stone.png"));
				this.TrinketDrawButton(x, y, 16, 16, 16, 16, 0.0625f, 1, 1, 1, 1);
			}

			final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
			if (id == ID) {
				final FontRenderer fontrenderer = mc.fontRenderer;
				hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
						&& (mouseY < (y + height));
				final int k = this.getHoverState(hovered);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				GlStateManager.pushMatrix();

				final int tXpos = x;
				final int tYpos = y;

				final int X = TrinketsConfig.CLIENT.GUI.button.X;
				final int Y = TrinketsConfig.CLIENT.GUI.button.Y;
				final int Xoffset = TrinketsConfig.CLIENT.GUI.button.Xoffset;
				final int Yoffset = TrinketsConfig.CLIENT.GUI.button.Yoffset;
				final int Width = TrinketsConfig.CLIENT.GUI.button.Width;
				final int Height = TrinketsConfig.CLIENT.GUI.button.Height;

				final int obSize = TrinketsConfig.CLIENT.GUI.button.Texture.openButtonTexSize;
				final int obX = TrinketsConfig.CLIENT.GUI.button.Texture.openButtonTexX;
				final int obY = TrinketsConfig.CLIENT.GUI.button.Texture.openButtonTexY;
				final float openScale = TrinketsConfig.CLIENT.GUI.button.Texture.openButtonScale;

				final int cbSize = TrinketsConfig.CLIENT.GUI.button.Texture.closeButtonTexSize;
				final int cbX = TrinketsConfig.CLIENT.GUI.button.Texture.closeButtonTexX;
				final int cbY = TrinketsConfig.CLIENT.GUI.button.Texture.closeButtonTexY;
				final float closeScale = TrinketsConfig.CLIENT.GUI.button.Texture.closeButtonScale;

				openButtonTex = new ResourceLocation(TrinketsConfig.CLIENT.GUI.button.Texture.openButtonTex);
				closeButtonTex = new ResourceLocation(TrinketsConfig.CLIENT.GUI.button.Texture.closeButtonTex);
				final boolean flag = TrinketsConfig.CLIENT.GUI.button.Texture.openButtonTex
						.equalsIgnoreCase(Reference.RESOURCE_PREFIX + "textures/gui/gui_inventory.png");
				final boolean flag2 = TrinketsConfig.CLIENT.GUI.button.Texture.closeButtonTex
						.equalsIgnoreCase(Reference.RESOURCE_PREFIX + "textures/gui/gui_inventory.png");
				final float[] obColor = ColorHelper
						.getHexColor(TrinketsConfig.CLIENT.GUI.button.Texture.openButtonColor);

				final float or = obColor[0];
				final float og = obColor[1];
				final float ob = obColor[2];
				final float oa = obColor[3];

				final float[] cbColor = ColorHelper
						.getHexColor(TrinketsConfig.CLIENT.GUI.button.Texture.closeButtonColor);
				final float cr = cbColor[0];
				final float cg = cbColor[1];
				final float cb = cbColor[2];
				final float ca = cbColor[3];

				if (k == 1) {
					if (parentGui instanceof TrinketGui) {
						mc.getTextureManager().bindTexture(closeButtonTex);
						if (flag2) {
							this.TrinketDrawButton(tXpos, tYpos, cbX, cbY, cbSize, cbSize, closeScale, cr, cg, cb, ca);
						} else {
							this.TrinketDrawButton(tXpos, tYpos, cbX, cbY, cbSize, cbSize, closeScale, cr, cg, cb, ca);
						}
					} else {
						mc.getTextureManager().bindTexture(openButtonTex);
						if (flag) {
							this.TrinketDrawButton(tXpos, tYpos, obX, obY, obSize, obSize, openScale, or, og, ob, oa);
						} else {
							this.TrinketDrawButton(tXpos, tYpos, obX, obY, obSize, obSize, openScale, or, og, ob, oa);
						}
					}
				} else {
					if (parentGui instanceof TrinketGui) {
						mc.getTextureManager().bindTexture(openButtonTex);
						if (flag) {
							this.TrinketDrawButton(tXpos, tYpos, obX, obY, obSize, obSize, openScale, or, og, ob, oa);
						} else {
							this.TrinketDrawButton(tXpos, tYpos, obX, obY, obSize, obSize, openScale, or, og, ob, oa);
						}
					} else {
						mc.getTextureManager().bindTexture(closeButtonTex);
						if (flag2) {
							this.TrinketDrawButton(tXpos, tYpos, cbX, cbY, cbSize, cbSize, closeScale, cr, cg, cb, ca);
						} else {
							this.TrinketDrawButton(tXpos, tYpos, cbX, cbY, cbSize, cbSize, closeScale, cr, cg, cb, ca);
						}
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
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
							&& (mouseY < (y + height));
					final int k = this.getHoverState(hovered);
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

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

	private void TrinketDrawButton(int x, int y, int texX, int texY, int width, int height, float scale, float r,
			float g, float b, float a) {
		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b, a);
		final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		// Bottom Left
		buffer.pos(x + 0, y + height, zLevel).tex((texX + 0) * scale, (texY + height) * scale).endVertex();
		// bottom right
		buffer.pos(x + width, y + height, zLevel).tex((texX + width) * scale, (texY + height) * scale).endVertex();
		// top right
		buffer.pos(x + width, y + 0, zLevel).tex((texX + width) * scale, (texY + 0) * scale).endVertex();
		// top left
		buffer.pos(x + 0, y + 0, zLevel).tex((texX + 0) * scale, (texY + 0) * scale).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
	}

}
