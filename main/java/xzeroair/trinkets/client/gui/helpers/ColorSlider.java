package xzeroair.trinkets.client.gui.helpers;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiPropertiesButton;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiPropertiesSlider;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class ColorSlider {

    private final GuiEntityProperties parent;
    private final GuiTextField colorField;
    private final GuiPropertiesSlider r;
    private final GuiPropertiesSlider g;
    private final GuiPropertiesSlider b;
    private final GuiPropertiesButton reset;
    private final int defaultColor;
    private final float defaultColorR;
    private final float defaultColorG;
    private final float defaultColorB;
    private int color;
    private int id;
    private int x;
    private int y;
    private int height;
    private int width;
    private float colorR;
    private float colorG;
    private float colorB;

    public ColorSlider(GuiEntityProperties parent, int id, int x, int y, int width, int height, int color, int defaultColor, FontRenderer fontRenderer) {
        this(parent, id, x, y, width, height, color, defaultColor, fontRenderer, null);
    }

    public ColorSlider(GuiEntityProperties parent, int id, int x, int y, int width, int height, int color, int defaultColor, FontRenderer fontRenderer, Consumer<Integer> colorChanged) {
        this.parent = parent;
        this.color = color;
        this.defaultColor = defaultColor;
        final float[] defRGB = ColorHelper.getRGBColor(defaultColor);
        this.defaultColorR = defRGB[0];
        this.defaultColorG = defRGB[1];
        this.defaultColorB = defRGB[2];
        final float[] colorRGB = ColorHelper.getRGBColor(color);
        this.colorR = colorRGB[0];
        this.colorG = colorRGB[1];
        this.colorB = colorRGB[2];
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorField = new GuiTextField(id++, fontRenderer, x + 1, y, width, height);
        this.colorField.setText(ColorHelper.getHexFromRGB(colorRGB[0], colorRGB[1], colorRGB[2]));
        this.colorField.setMaxStringLength(7);
        this.r = new GuiPropertiesSlider(id++, x, y + (height) + 1, width + 22, height, "R: " + Math.round(colorRGB[0] * 255), colorRGB[0], 1F, 0F, (sliderR) -> {
            int newColor = ColorHelper.getDecimalFromRGB(sliderR.getSliderValue(), this.colorG, this.colorB);
            this.color = newColor;
            this.colorR = sliderR.getSliderValue();
            sliderR.displayString = "R" + ": " + (int) ((sliderR.getSliderValue() * sliderR.getSliderMaxValue()) * 255);
            if (colorChanged != null) {
                colorChanged.accept(newColor);
            }
            this.getColorField().setText(ColorHelper.convertDecimalColorToHexadecimal(newColor));
        });
        this.g = new GuiPropertiesSlider(id++, x, y + (height * 2) + 1, width + 22, height, "G: " + Math.round(colorRGB[1] * 255), colorRGB[1], 1F, 0F, (sliderG) -> {
            int newColor = ColorHelper.getDecimalFromRGB(this.colorR, sliderG.getSliderValue(), this.colorB);
            this.color = newColor;
            this.colorG = sliderG.getSliderValue();
            sliderG.displayString = "G" + ": " + (int) ((sliderG.getSliderValue() * sliderG.getSliderMaxValue()) * 255);
            if (colorChanged != null) {
                colorChanged.accept(newColor);
            }
            this.getColorField().setText(ColorHelper.convertDecimalColorToHexadecimal(newColor));
        });
        this.b = new GuiPropertiesSlider(id++, x, y + (height * 3) + 1, width + 22, height, "B: " + Math.round(colorRGB[2] * 255), colorRGB[2], 1F, 0F, (sliderB) -> {
            int newColor = ColorHelper.getDecimalFromRGB(this.colorR, this.colorG, sliderB.getSliderValue());
            this.color = newColor;
            this.colorB = sliderB.getSliderValue();
            sliderB.displayString = "B" + ": " + (int) ((sliderB.getSliderValue() * sliderB.getSliderMaxValue()) * 255);
            if (colorChanged != null) {
                colorChanged.accept(newColor);
            }
            this.getColorField().setText(ColorHelper.convertDecimalColorToHexadecimal(newColor));
        });
        this.reset = new GuiPropertiesButton(id++, x + width + 2, y, 20, height, "R", (button, pressed) -> {
            this.getColorField().setText(ColorHelper.convertDecimalColorToHexadecimal(this.defaultColor));
            this.getR().setSliderValue(this.defaultColorR);
            this.getR().displayString = "R: " + ((int) (this.defaultColorR * 255));
            this.getG().setSliderValue(this.defaultColorG);
            this.getG().displayString = "G: " + ((int) (this.defaultColorG * 255));
            this.getB().setSliderValue(this.defaultColorB);
            this.getB().displayString = "B: " + ((int) (this.defaultColorB * 255));
            this.color = this.defaultColor;
            this.colorR = this.defaultColorR;
            this.colorG = this.defaultColorG;
            this.colorB = this.defaultColorB;
            if (colorChanged != null) {
                colorChanged.accept(this.defaultColor);
            }
        });
        this.id = id;
    }

    public void init(@Nonnull List<GuiButton> buttonList) {
        buttonList.add(this.r);
        buttonList.add(this.g);
        buttonList.add(this.b);
        buttonList.add(this.reset);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.getColorField().mouseClicked(mouseX, mouseY, mouseButton);
    }

    public boolean keyTyped(char character, int keyCode) {
        if (this.getColorField().isFocused()) {
            this.getColorField().textboxKeyTyped(character, keyCode);
            final String text = this.getColorField().getText().toLowerCase().replaceAll("[^#0-9a-f]", "");
            int newColor = ColorHelper.getColorFromString(text);
            this.getColorField().setText(text);
            final float[] rgb = ColorHelper.getRGBColor(newColor);
            this.getR().setSliderValue(rgb[0]);
            this.getR().displayString = "R: " + ((int) (rgb[0] * 255));
            this.getG().setSliderValue(rgb[1]);
            this.getG().displayString = "G: " + ((int) (rgb[1] * 255));
            this.getB().setSliderValue(rgb[2]);
            this.getB().displayString = "B: " + ((int) (rgb[2] * 255));
            this.color = newColor;
            this.colorR = this.getR().getSliderValue();
            this.colorG = this.getG().getSliderValue();
            this.colorB = this.getB().getSliderValue();
            return true;
        }
        return false;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.getColorField().drawTextBox();
        DrawingHelper.Draw(this.x + this.width + 3, this.y + 1, 0, 0, 0, 0, 0, 18, 18, 0, 0, ((this.getR().getSliderValue())), ((this.getG().getSliderValue())), ((this.getB().getSliderValue())), 1F);
        DrawingHelper.Draw(this.x + this.width + 3, this.y + 1, 0, 0, 0, 0, 0, 4, 4, 0, 0, this.defaultColorR, this.defaultColorG, this.defaultColorB, 1F);
    }

    public GuiPropertiesSlider getR() {
        return this.r;
    }

    public GuiPropertiesSlider getG() {
        return this.g;
    }

    public GuiPropertiesSlider getB() {
        return this.b;
    }

    public GuiTextField getColorField() {
        return this.colorField;
    }

    public int getNextX() {
        return this.x + (this.width) + 22;
    }

    public int getNextY() {
        return this.y + (this.height * 4) + 1;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getNextId() {
        return this.id;
    }

    public int getColor() {
        return this.color;
    }

    public int getDefaultColor() {
        return this.defaultColor;
    }

    public float getDefaultColorR() {
        return this.defaultColorR;
    }

    public float getDefaultColorG() {
        return this.defaultColorG;
    }

    public float getDefaultColorB() {
        return this.defaultColorB;
    }
}
