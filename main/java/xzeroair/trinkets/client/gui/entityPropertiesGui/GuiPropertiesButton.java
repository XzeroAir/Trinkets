package xzeroair.trinkets.client.gui.entityPropertiesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class GuiPropertiesButton extends GuiButton {

    protected static ResourceLocation TEXTURE = BUTTON_TEXTURES;
    @Nullable
    private final BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed;
    @Nullable
    private final BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPre;
    @Nullable
    private final BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPost;

    public GuiPropertiesButton(int buttonId, int x, int y, int width, int height, String buttonText, @Nullable BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> whenPressed, @Nullable BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPre, @Nullable BiConsumer<GuiPropertiesButton, ButtonFunctionWrapper> renderPost, @Nullable ResourceLocation buttonTex) {
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
    public boolean mousePressed(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        final boolean flag = mc.player.getRecipeBook().isGuiOpen();
        final boolean pressed = !flag && super.mousePressed(mc, mouseX, mouseY);
        if (pressed) {
            if (this.whenPressed != null) {
                this.whenPressed.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, 0));
            }
        }
        return pressed;
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
            final int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + (i * 20), this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + (this.width / 2), this.y, 200 - (this.width / 2), 46 + (i * 20), this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (this.packedFGColour != 0) {
                j = this.packedFGColour;
            } else if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.x + (this.width / 2), this.y + ((this.height - 8) / 2), j);
            if (this.renderPost != null) {
                this.renderPost.accept(this, new ButtonFunctionWrapper(mc, mouseX, mouseY, partialTicks));
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

    public int mousedOver() {
        return this.getHoverState(this.hovered);
    }
}
