package xzeroair.trinkets.client.gui.entityPropertiesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiPropertiesButton.ButtonFunctionWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class GuiPropertiesSlider extends GuiButton {

    protected static ResourceLocation TEXTURE = BUTTON_TEXTURES;
    protected float sliderValue = 1.0F;
    protected float sliderMaxValue = 1.0F;
    protected float sliderMinValue = 1.0F;
    protected boolean dragging = false;
    //    private GuiEntityProperties gui;
    //	public String label;
//    private GuiTextField field;

    private final BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed;
    private final BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre;
    private final BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost;
    private final Consumer<GuiPropertiesSlider> pressed;

    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, @Nullable BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, @Nullable BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, @Nullable BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost, @Nullable ResourceLocation buttonTex) {
//        super(buttonId, x, y, width, height, buttonText + ": " + (int) ((startingValue * maxValue) * 255));
        super(buttonId, x, y, width, height, buttonText);
        this.sliderValue = startingValue;
        this.sliderMaxValue = maxValue;
        this.sliderMinValue = minValue;
        this.pressed = null;
        this.whenPressed = whenPressed;
        this.renderPre = renderPre;
        this.renderPost = renderPost;
        if (buttonTex != null) {
            TEXTURE = buttonTex;
        }
    }

    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, @Nullable Consumer<GuiPropertiesSlider> whenPressed) {
//        super(buttonId, x, y, width, height, buttonText + ": " + (int) ((startingValue * maxValue) * 255));
        super(buttonId, x, y, width, height, buttonText);
        this.sliderValue = startingValue;
        this.sliderMaxValue = maxValue;
        this.sliderMinValue = minValue;
        this.whenPressed = null;
        this.renderPre = null;
        this.renderPost = null;
        this.pressed = whenPressed;
    }

//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, ResourceLocation buttonTex) {
//        this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, renderPre, null, buttonTex);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, ResourceLocation buttonTex) {
//        this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, null, null, buttonTex);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, ResourceLocation buttonTex) {
//        this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, null, null, null, buttonTex);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost) {
//        this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, renderPre, renderPost, null);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre) {
//        this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, renderPre, null, null);
//    }

    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed) {
        this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, whenPressed, null, null, null);
    }

//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, String buttonText, float startingValue, float maxValue, float minValue) {
//        this(buttonId, x, y, width, height, buttonText, startingValue, maxValue, minValue, null, null, null, null);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, ResourceLocation buttonTex) {
//        this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, renderPre, null, buttonTex);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, ResourceLocation buttonTex) {
//        this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, null, null, buttonTex);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, ResourceLocation buttonTex) {
//        this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, null, null, null, buttonTex);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPost) {
//        this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, renderPre, renderPost, null);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> renderPre) {
//        this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, renderPre, null, null);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue, BiConsumer<GuiPropertiesSlider, ButtonFunctionWrapper> whenPressed) {
//        this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, whenPressed, null, null, null);
//    }
//
//    public GuiPropertiesSlider(int buttonId, int x, int y, int width, int height, float startingValue, float maxValue, float minValue) {
//        this(buttonId, x, y, width, height, "", startingValue, maxValue, minValue, null, null, null, null);
//    }

    public float getSliderValue() {
        return this.sliderValue;
    }

    @Override
    protected int getHoverState(boolean par1) {
        return 0;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            if (this.renderPre != null) {
                this.renderPre.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, partialTicks));
            }
            final FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < (this.x + this.width)) && (mouseY < (this.y + this.height));
            final int k = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + (k * 20), this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + (this.width / 2), this.y, 200 - (this.width / 2), 46 + (k * 20), this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int l = 14737632;
            if (this.packedFGColour != 0) {
                l = this.packedFGColour;
            } else if (!this.enabled) {
                l = 10526880;
            } else if (this.hovered) {
                l = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.x + (this.width / 2), this.y + ((this.height - 8) / 2), l);
            if (this.renderPost != null) {
                this.renderPost.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, partialTicks));
            }
        }

    }

    @Override
    protected void mouseDragged(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled && this.visible && (this.packedFGColour == 0)) {
            if (this.dragging) {
                this.sliderValue = (float) (mouseX - (this.x + 4)) / (this.width - 8);
                if (this.sliderValue < 0.0F) {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F) {
                    this.sliderValue = 1.0F;
                }

                if (this.pressed != null) {
                    this.pressed.accept(this);
                }
                if (this.whenPressed != null) {
                    this.whenPressed.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, 0));
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            // Button Part
            this.drawTexturedModalRect(this.x + (int) (this.sliderValue * (this.width - 8)), this.y, 0, 66, 4, 20);

            // Button slider part
            this.drawTexturedModalRect(this.x + (int) (this.sliderValue * (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(@Nonnull Minecraft par1Minecraft, int par2, int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.sliderValue = (float) (par2 - (this.x + 4)) / (this.width - 8);
            if (this.sliderValue < 0.0F) {
                this.sliderValue = 0.0F;
            }
            if (this.sliderValue > 1.0F) {
                this.sliderValue = 1.0F;
            }
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    public GuiPropertiesSlider setSliderValue(float sliderValue) {
        this.sliderValue = sliderValue;
        return this;
    }


    public float getSliderMaxValue() {
        return this.sliderMaxValue;
    }

    public float getSliderMinValue() {
        return this.sliderMinValue;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    @Override
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
    }
}
