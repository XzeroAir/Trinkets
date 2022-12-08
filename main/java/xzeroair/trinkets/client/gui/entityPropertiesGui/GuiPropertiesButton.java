package xzeroair.trinkets.client.gui.entityPropertiesGui;

import java.util.function.BiConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiPropertiesButton extends GuiButton {

	protected static ResourceLocation TEXTURE = BUTTON_TEXTURES;
	private BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, renderPre, renderPost;

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, String buttonText,
			BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed,
			BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPre,
			BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPost,
			ResourceLocation buttonTex) {
		super(buttonId, x, y, width, height, buttonText);
		this.whenPressed = whenPressed;
		this.renderPre = renderPre;
		this.renderPost = renderPost;
		if (buttonTex != null) {
			TEXTURE = buttonTex;
		}
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, String buttonText, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPre, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, buttonText, whenPressed, renderPre, null, buttonTex);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, String buttonText, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, buttonText, whenPressed, null, null, buttonTex);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, String buttonText, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, buttonText, null, null, null, buttonTex);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, String buttonText, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPre, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPost) {
		this(buttonId, x, y, width, height, buttonText, whenPressed, renderPre, renderPost, null);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, String buttonText, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPre) {
		this(buttonId, x, y, width, height, buttonText, whenPressed, renderPre, null, null);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, String buttonText, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed) {
		this(buttonId, x, y, width, height, buttonText, whenPressed, null, null, null);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, String buttonText) {
		this(buttonId, x, y, width, height, buttonText, null, null, null, null);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPre, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, "", whenPressed, renderPre, null, buttonTex);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, "", whenPressed, null, null, buttonTex);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, "", null, null, null, buttonTex);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPre, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPost) {
		this(buttonId, x, y, width, height, "", whenPressed, renderPre, renderPost, null);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPre) {
		this(buttonId, x, y, width, height, "", whenPressed, renderPre, null, null);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed) {
		this(buttonId, x, y, width, height, "", whenPressed, null, null, null);
	}

	public GuiPropertiesButton(int buttonId, int x, int y, int width, int height) {
		this(buttonId, x, y, width, height, "", null, null, null, null);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		final boolean flag = mc.player.getRecipeBook().isGuiOpen();
		final boolean pressed = flag ? false : super.mousePressed(mc, mouseX, mouseY);
		if (pressed) {
			if (whenPressed != null) {
				whenPressed.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, 0));
			}
		}
		return pressed;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (visible) {
			if (renderPre != null) {
				renderPre.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, partialTicks));
			}
			final FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(TEXTURE);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width)) && (mouseY < (y + height));
			final int i = this.getHoverState(hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(x, y, 0, 46 + (i * 20), width / 2, height);
			this.drawTexturedModalRect(x + (width / 2), y, 200 - (width / 2), 46 + (i * 20), width / 2, height);
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;

			if (packedFGColour != 0) {
				j = packedFGColour;
			} else if (!enabled) {
				j = 10526880;
			} else if (hovered) {
				j = 16777120;
			}
			this.drawCenteredString(fontrenderer, displayString, x + (width / 2), y + ((height - 8) / 2), j);
			if (renderPost != null) {
				renderPost.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, partialTicks));
			}
		}
	}

	public static class ButtonFunctionWrapper {
		public Minecraft mc;
		public int mouseX;
		public int mouseY;
		public float partialTicks;

		public ButtonFunctionWrapper(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			this.mc = mc;
			this.mouseX = mouseX;
			this.mouseY = mouseY;
			this.partialTicks = partialTicks;
		}
	}

	//	@Override
	//	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
	//		if (visible && !mc.player.getRecipeBook().isGuiOpen()) {
	//			//			final int k = this.getHoverState(hovered);
	//			if (render != null) {
	//				render.accept(this);
	//			}
	//			if (id == 4) {
	//				final EntityProperties properties = Capabilities.getEntityRace(mc.player);
	//				displayString = "" + properties.showTraits();
	//			}
	//			if (id == 3) {
	//				if (TrinketsConfig.CLIENT.MPBar.mana_horizontal) {
	//					displayString = "H";
	//				} else {
	//					displayString = "V";
	//				}
	//			}
	//			if (id == 19) {
	//				if (TrinketsConfig.CLIENT.MPBar.always_shown) {
	//					displayString = "A";
	//				} else {
	//					displayString = "^";
	//				}
	//			}
	//			if (id == 999) {
	//			}
	//			//			final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
	//			//			if (id == 69) {
	//			super.drawButton(mc, mouseX, mouseY, partialTicks);
	//			//				final FontRenderer fontrenderer = mc.fontRenderer;
	//			//				hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width))
	//			//						&& (mouseY < (y + height));
	//			//				final int k = this.getHoverState(hovered);
	//			//				GlStateManager.pushMatrix();
	//			//
	//			//				ConfigGuiButtonShared config = TrinketsConfig.CLIENT.Hud.button.texture;
	//			//				buttonTex = new ResourceLocation(TrinketsConfig.CLIENT.Hud.button.texture.image);
	//			//				ColorHelper color = new ColorHelper(TrinketsConfig.CLIENT.Hud.button.texture.color);
	//			//				int X = config.x;
	//			//				int Y = config.y;
	//			//				int width = config.width;
	//			//				int height = config.height;
	//			//				int texWidth = config.texWidth;
	//			//				int texHeight = config.texHeight;
	//			//
	//			//				if (k == 1) {
	//			//					mc.getTextureManager().bindTexture(buttonTex);
	//			//					DrawingHelper.Draw(x, y, 0, X, Y, texWidth, texHeight, width, height, config.texSizeWidth, config.texSizeHeight, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	//			//				} else {
	//			//					mc.getTextureManager().bindTexture(buttonTex);
	//			//					DrawingHelper.Draw(x, y, 0, X, Y + texHeight, texWidth, texHeight, width, height, config.texSizeWidth, config.texSizeHeight, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	//			//					this.drawCenteredString(
	//			//							fontrenderer, I18n.format(displayString), x + 5, y + height,
	//			//							0xffffff
	//			//					);
	//			//				}
	//			//				GlStateManager.popMatrix();
	//			//			}
	//			//			this.mouseDragged(mc, mouseX, mouseY);
	//		}
	//	}

	public int mousedOver() {
		return this.getHoverState(hovered);
	}
}
