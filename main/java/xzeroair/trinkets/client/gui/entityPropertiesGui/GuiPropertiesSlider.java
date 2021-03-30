package xzeroair.trinkets.client.gui.entityPropertiesGui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import xzeroair.trinkets.util.helpers.ColorHelper;

public class GuiPropertiesSlider extends GuiButton {

	public float sliderValue = 1.0F;

	public float sliderMaxValue = 1.0F;

	public float sliderMinValue = 1.0F;

	public boolean dragging = false;

	GuiEntityProperties gui;

	public String label;

	GuiTextField field;

	public ColorHelper color;

	public GuiPropertiesSlider(GuiEntityProperties gui, int id, int x, int y, int width, int height, String label, float startingValue, float maxValue, float minValue) {

		super(id, x, y, width, height, label);
		this.gui = gui;

		field = gui.colorField;
		color = gui.color;

		this.label = label;

		sliderValue = startingValue;

		sliderMaxValue = maxValue;

		sliderMinValue = minValue;

	}

	@Override
	protected int getHoverState(boolean par1) {

		return 0;

	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

		if (visible)

		{

			FontRenderer fontrenderer = mc.fontRenderer;

			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			hovered = (mouseX >= x) && (mouseY >= y) && (mouseX < (x + width)) && (mouseY < (y + height));

			int k = this.getHoverState(hovered);

			GlStateManager.enableBlend();

			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

			GlStateManager.blendFunc(770, 771);

			this.drawTexturedModalRect(x, y, 0, 46 + (k * 20), width / 2, height);

			this.drawTexturedModalRect(x + (width / 2), y, 200 - (width / 2), 46 + (k * 20), width / 2, height);

			this.mouseDragged(mc, mouseX, mouseY);

			int l = 14737632;

			if (packedFGColour != 0)

			{

				l = packedFGColour;

			}

			else if (!enabled)

			{

				l = 10526880;

			}

			else if (hovered)

			{

				l = 16777120;

			}

			this.drawCenteredString(fontrenderer, displayString, x + (width / 2), y + ((height - 8) / 2), l);

		}

	}

	@Override
	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {

		if (enabled && visible && (packedFGColour == 0)) {

			if (dragging) {

				sliderValue = (float) (par2 - (x + 4)) / (width - 8);

				if (sliderValue < 0.0F) {

					sliderValue = 0.0F;

				}

				if (sliderValue > 1.0F) {

					sliderValue = 1.0F;

				}

				if (id == 5) {
					color.setRed(sliderValue);
					gui.properties.setTraitColor(color.getHex());
				}
				if (id == 6) {
					color.setGreen(sliderValue);
					gui.properties.setTraitColor(color.getHex());
				}
				if (id == 7) {
					color.setBlue(sliderValue);
					gui.properties.setTraitColor(color.getHex());
				}
				if (id == 8) {
					color.setAlpha(sliderValue);
					gui.properties.setTraitOpacity(color.getAlpha());
				}
				if (field != null) {
					field.setTextColor(color.getDecimal());
					field.setText(color.getHex());
				}
			}

			displayString = label + ": " + (int) ((sliderValue * sliderMaxValue) * 255);

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
