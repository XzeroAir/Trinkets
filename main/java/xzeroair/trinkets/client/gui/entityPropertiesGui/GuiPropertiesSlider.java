<<<<<<< Updated upstream
package xzeroair.trinkets.client.gui.entityPropertiesGui;

import java.util.function.BiConsumer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiPropertiesButton.ButtonFunctionWrapper;
import xzeroair.trinkets.util.helpers.ColorHelper;

public class GuiPropertiesSlider extends GuiButton {

	protected static ResourceLocation TEXTURE = BUTTON_TEXTURES;
	public float sliderValue = 1.0F;
	public float sliderMaxValue = 1.0F;
	public float sliderMinValue = 1.0F;
	public boolean dragging = false;
	GuiEntityProperties gui;
	//	public String label;
	GuiTextField field;
	public ColorHelper color;

	private BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, renderPre, renderPost;

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue,
			BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed,
			BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre,
			BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost,
			ResourceLocation buttonTex) {
		super(buttonId, x, y, width, height, buttonText + ": " + (int) ((startingValue * maxValue) * 255));
		sliderValue = startingValue;
		sliderMaxValue = maxValue;
		sliderMinValue = minValue;
		this.whenPressed = whenPressed;
		this.renderPre = renderPre;
		this.renderPost = renderPost;
		if (buttonTex != null) {
			TEXTURE = buttonTex;
		}
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, renderPre, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, null, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, null, null, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, renderPre, renderPost, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, renderPre, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, null, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, null, null, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, renderPre, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, null, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, null, null, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, renderPre, renderPost, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, renderPre, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, null, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, null, null, null, null);
	}

	public float getSliderValue() {
		return sliderValue;
	}

	@Override
	protected int getHoverState(boolean par1) {
		return 0;
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
			final int k = this.getHoverState(hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			this.drawTexturedModalRect(x, y, 0, 46 + (k * 20), width / 2, height);
			this.drawTexturedModalRect(x + (width / 2), y, 200 - (width / 2), 46 + (k * 20), width / 2, height);
			this.mouseDragged(mc, mouseX, mouseY);
			int l = 14737632;
			if (packedFGColour != 0) {
				l = packedFGColour;
			} else if (!enabled) {
				l = 10526880;
			} else if (hovered) {
				l = 16777120;
			}
			this.drawCenteredString(fontrenderer, displayString, x + (width / 2), y + ((height - 8) / 2), l);
			if (renderPost != null) {
				renderPost.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, partialTicks));
			}
		}

	}

	@Override
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
		if (enabled && visible && (packedFGColour == 0)) {
			if (dragging) {
				sliderValue = (float) (mouseX - (x + 4)) / (width - 8);
				if (sliderValue < 0.0F) {
					sliderValue = 0.0F;
				}

				if (sliderValue > 1.0F) {
					sliderValue = 1.0F;
				}

				if (whenPressed != null) {
					whenPressed.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, 0));
				}
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			// Button Part
			this.drawTexturedModalRect(x + (int) (sliderValue * (width - 8)), y, 0, 66, 4, 20);

			// Button slider part
			this.drawTexturedModalRect(x + (int) (sliderValue * (width - 8)) + 4, y, 196, 66, 4, 20);
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		if (super.mousePressed(par1Minecraft, par2, par3)) {
			sliderValue = (float) (par2 - (x + 4)) / (width - 8);
			if (sliderValue < 0.0F) {
				sliderValue = 0.0F;
			}
			if (sliderValue > 1.0F) {
				sliderValue = 1.0F;
			}
			dragging = true;
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void mouseReleased(int par1, int par2) {
		dragging = false;
	}
}
=======
package xzeroair.trinkets.client.gui.entityPropertiesGui;

import java.util.function.BiConsumer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiPropertiesButton.ButtonFunctionWrapper;
import xzeroair.trinkets.util.helpers.ColorHelper;

public class GuiPropertiesSlider extends GuiButton {

	protected static ResourceLocation TEXTURE = BUTTON_TEXTURES;
	public float sliderValue = 1.0F;
	public float sliderMaxValue = 1.0F;
	public float sliderMinValue = 1.0F;
	public boolean dragging = false;
	GuiEntityProperties gui;
	//	public String label;
	GuiTextField field;
	public ColorHelper color;

	private BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, renderPre, renderPost;

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue,
			BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed,
			BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre,
			BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost,
			ResourceLocation buttonTex) {
		super(buttonId, x, y, width, height, buttonText + ": " + (int) ((startingValue * maxValue) * 255));
		sliderValue = startingValue;
		sliderMaxValue = maxValue;
		sliderMinValue = minValue;
		this.whenPressed = whenPressed;
		this.renderPre = renderPre;
		this.renderPost = renderPost;
		if (buttonTex != null) {
			TEXTURE = buttonTex;
		}
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, renderPre, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, null, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, null, null, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, renderPre, renderPost, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, renderPre, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, null, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue) {
		this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, null, null, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, renderPre, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, null, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, ResourceLocation buttonTex) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, null, null, null, buttonTex);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, renderPre, renderPost, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, renderPre, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, null, null, null);
	}

	public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue) {
		this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, null, null, null, null);
	}

	public float getSliderValue() {
		return sliderValue;
	}

	@Override
	protected int getHoverState(boolean par1) {
		return 0;
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
			final int k = this.getHoverState(hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			this.drawTexturedModalRect(x, y, 0, 46 + (k * 20), width / 2, height);
			this.drawTexturedModalRect(x + (width / 2), y, 200 - (width / 2), 46 + (k * 20), width / 2, height);
			this.mouseDragged(mc, mouseX, mouseY);
			int l = 14737632;
			if (packedFGColour != 0) {
				l = packedFGColour;
			} else if (!enabled) {
				l = 10526880;
			} else if (hovered) {
				l = 16777120;
			}
			this.drawCenteredString(fontrenderer, displayString, x + (width / 2), y + ((height - 8) / 2), l);
			if (renderPost != null) {
				renderPost.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, partialTicks));
			}
		}

	}

	@Override
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
		if (enabled && visible && (packedFGColour == 0)) {
			if (dragging) {
				sliderValue = (float) (mouseX - (x + 4)) / (width - 8);
				if (sliderValue < 0.0F) {
					sliderValue = 0.0F;
				}

				if (sliderValue > 1.0F) {
					sliderValue = 1.0F;
				}

				if (whenPressed != null) {
					whenPressed.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, 0));
				}
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			// Button Part
			this.drawTexturedModalRect(x + (int) (sliderValue * (width - 8)), y, 0, 66, 4, 20);

			// Button slider part
			this.drawTexturedModalRect(x + (int) (sliderValue * (width - 8)) + 4, y, 196, 66, 4, 20);
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		if (super.mousePressed(par1Minecraft, par2, par3)) {
			sliderValue = (float) (par2 - (x + 4)) / (width - 8);
			if (sliderValue < 0.0F) {
				sliderValue = 0.0F;
			}
			if (sliderValue > 1.0F) {
				sliderValue = 1.0F;
			}
			dragging = true;
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void mouseReleased(int par1, int par2) {
		dragging = false;
	}
}
>>>>>>> Stashed changes
