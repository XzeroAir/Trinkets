package xzeroair.trinkets.client.gui.helpers;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityProperties;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiPropertiesButton;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiPropertiesSlider;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class RotationSlider {

    private final GuiEntityProperties parent;
    private final GuiPropertiesSlider posX;
    private final GuiPropertiesSlider posY;
    private final GuiPropertiesSlider posZ;
    private final GuiPropertiesButton reset;
    private final double defaultXPosition;
    private final double defaultYPosition;
    private final double defaultZPosition;
    private int id;
    private int x;
    private int y;
    private int height;
    private int width;
    private float positionX;
    private float positionY;
    private float positionZ;

    public RotationSlider(GuiEntityProperties parent, int id, int x, int y, int width, int height, float posX, float posY, float posZ, int defaultColor, FontRenderer fontRenderer) {
        this(parent, id, x, y, width, height, posX, posY, posZ, defaultColor, fontRenderer, null);
    }

    public RotationSlider(GuiEntityProperties parent, int id, int x, int y, int width, int height, float posX, float posY, float posZ, int defaultColor, FontRenderer fontRenderer, Consumer<Integer> colorChanged) {
        this.parent = parent;
        this.defaultXPosition = 0;
        this.defaultYPosition = 0;
        this.defaultZPosition = 0;
        this.positionX = posX;
        this.positionY = posY;
        this.positionZ = posZ;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.posX = new GuiPropertiesSlider(id++, x, y + (height) + 1, width + 22, height, "X: " + ((int) posX), posX, 1F, 0F, (sliderR) -> {
            this.positionX = sliderR.getSliderValue();
//            sliderR.displayString = "R" + ": " + (int) ((sliderR.getSliderValue() * sliderR.getSliderMaxValue()) );
//            if (colorChanged != null) {
//                colorChanged.accept(this.color);
//            }
        });
        this.posY = new GuiPropertiesSlider(id++, x, y + (height * 2) + 1, width + 22, height, "G: " + Math.round(posY * 255), posY, 1F, 0F, (sliderG) -> {
            this.positionY = sliderG.getSliderValue();
//            sliderG.displayString = "G" + ": " + (int) ((sliderG.getSliderValue() * sliderG.getSliderMaxValue()) * 255);
//            if (colorChanged != null) {
//                colorChanged.accept(this.color);
//            }
        });
        this.posZ = new GuiPropertiesSlider(id++, x, y + (height * 3) + 1, width + 22, height, "B: " + Math.round(posZ * 255), posZ, 1F, 0F, (sliderB) -> {
            this.positionZ = sliderB.getSliderValue();
//            sliderB.displayString = "B" + ": " + (int) ((sliderB.getSliderValue() * sliderB.getSliderMaxValue()) * 255);
//            if (colorChanged != null) {
//                colorChanged.accept(this.color);
//            }
        });
        this.reset = new GuiPropertiesButton(id++, x + width + 2, y, 20, height, "R", (button, pressed) -> {
//            this.getR().setSliderValue(rgb[0]);
//            this.getR().displayString = "R: " + ((int) (rgb[0] * 255));
//            this.getG().setSliderValue(rgb[1]);
//            this.getG().displayString = "G: " + ((int) (rgb[1] * 255));
//            this.getB().setSliderValue(rgb[2]);
//            this.getB().displayString = "B: " + ((int) (rgb[2] * 255));
//            this.color = defaultColor;
//            if (colorChanged != null) {
//                colorChanged.accept(this.color);
//            }
        });
        this.id = id;
    }

    public void init(@Nonnull List<GuiButton> buttonList) {
        buttonList.add(this.posX);
        buttonList.add(this.posY);
        buttonList.add(this.posZ);
        buttonList.add(this.reset);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public boolean keyTyped(char character, int keyCode) {
        return false;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
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
}