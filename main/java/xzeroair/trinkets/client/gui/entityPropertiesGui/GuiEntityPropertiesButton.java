package xzeroair.trinkets.client.gui.entityPropertiesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.ConstantsResourceLocations;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.gui.ConfigGuiButtonShared;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import javax.annotation.Nonnull;

public class GuiEntityPropertiesButton extends GuiButton {

    private final ResourceLocation TEXTURE_BUTTON;

    private final GuiContainer parentGui;

    public GuiEntityPropertiesButton(int buttonId, GuiContainer parentGui, int x, int y, int width, int height, String buttonText) {
        super(buttonId, x, parentGui.getGuiTop() + y, width, height, buttonText);
        this.parentGui = parentGui;
        this.TEXTURE_BUTTON = new ResourceLocation(ConstantsResourceLocations.GUI_ENTITY_BUTTON);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        final boolean flag = mc.player.getRecipeBook().isGuiOpen();
        final boolean pressed = !flag && super.mousePressed(mc, mouseX - this.parentGui.getGuiLeft(), mouseY);
        if (pressed) {
            mc.player.openGui(Trinkets.instance, Reference.GUI_ENTITY, mc.player.world, 0, 0, 0);
        }
        return pressed;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible && !mc.player.getRecipeBook().isGuiOpen()) {
            final int x = this.x + this.parentGui.getGuiLeft();

            final int ID = TrinketsConfig.CLIENT.raceProperties.button.ID;
            if (this.id == ID) {
                final FontRenderer fontrenderer = mc.fontRenderer;
                this.hovered = (mouseX >= x) && (mouseY >= this.y) && (mouseX < (x + this.width)) && (mouseY < (this.y + this.height));
                final int k = this.getHoverState(this.hovered);
                GlStateManager.pushMatrix();

                final ConfigGuiButtonShared TEXTURE_CONFIG = TrinketsConfig.CLIENT.raceProperties.button.texture;
                final int X = TEXTURE_CONFIG.X;
                final int Y = TEXTURE_CONFIG.Y;
                final int width = TEXTURE_CONFIG.BUTTON_WIDTH;
                final int height = TEXTURE_CONFIG.BUTTON_HEIGHT;
                final int texWidth = TEXTURE_CONFIG.TEXTURE_WIDTH;
                final int texHeight = TEXTURE_CONFIG.TEXTURE_HEIGHT;
                final float[] rgb = ColorHelper.getRGBColor(TrinketsConfig.CLIENT.raceProperties.button.texture.COLOR);
                if (k == 1) {
                    mc.getTextureManager().bindTexture(this.TEXTURE_BUTTON);
                    DrawingHelper.Draw(x, this.y, 0, X, Y, texWidth, texHeight, width, height, TEXTURE_CONFIG.TEXTURE_ATLAS_WIDTH, TEXTURE_CONFIG.TEXTURE_ATLAS_HEIGHT, rgb[0], rgb[1], rgb[2], 1F);
                } else {
                    mc.getTextureManager().bindTexture(this.TEXTURE_BUTTON);
                    DrawingHelper.Draw(x, this.y, 0, X, Y + texHeight, texWidth, texHeight, width, height, TEXTURE_CONFIG.TEXTURE_ATLAS_WIDTH, TEXTURE_CONFIG.TEXTURE_ATLAS_HEIGHT, rgb[0], rgb[1], rgb[2], 1F);
                    this.drawCenteredString(fontrenderer, I18n.format(this.displayString), x + 5, this.y + height, 0xffffff);
                }
                GlStateManager.popMatrix();
            }
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}
