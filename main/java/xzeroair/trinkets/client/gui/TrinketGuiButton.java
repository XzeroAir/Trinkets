package xzeroair.trinkets.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.util.ConstantsResourceLocations;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.baubles.BaublesHelperFunctions;
import xzeroair.trinkets.util.config.gui.ConfigGuiButtonShared;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import javax.annotation.Nonnull;

public class TrinketGuiButton extends GuiButton {

    protected final ResourceLocation BUTTON_TEXTURE;

    private final GuiContainer parentGui;

    public TrinketGuiButton(int buttonId, @Nonnull GuiContainer parentGui, int x, int y, int width, int height, String buttonText) {
        super(buttonId, x, parentGui.getGuiTop() + y, width, height, buttonText);
        this.parentGui = parentGui;
        this.BUTTON_TEXTURE = new ResourceLocation(ConstantsResourceLocations.TrinketsGui);
    }

    @Override
    public boolean mousePressed(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        final boolean flag = mc.player.getRecipeBook().isGuiOpen();
        final boolean pressed = !flag && super.mousePressed(mc, mouseX - this.parentGui.getGuiLeft(), mouseY);
        if (pressed) {
            if (this.parentGui instanceof GuiInventory) {
                NetworkHandler.sendToServer(new OpenTrinketGui());
            } else {
                if (Trinkets.MOD_COMPAT.Baubles) {
                    BaublesHelperFunctions.mousePressedHelper(this.parentGui, this.id);
                } else {
                    ((TrinketGui) this.parentGui).displayNormalInventory();
                    NetworkHandler.sendToServer(new OpenTrinketGui(Reference.GUI_TRINKETS_EXIT_BUTTON));
                }
            }
        }
        return pressed;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible && !mc.player.getRecipeBook().isGuiOpen()) {
            final int x = this.x + this.parentGui.getGuiLeft();

            final int ID = TrinketsConfig.CLIENT.GUI.BUTTON.ID;
            if (this.id == ID) {
                final FontRenderer fontrenderer = mc.fontRenderer;
                this.hovered = (mouseX >= x) && (mouseY >= this.y) && (mouseX < (x + this.width)) && (mouseY < (this.y + this.height));
                final int k = this.getHoverState(this.hovered);

                GlStateManager.pushMatrix();

                final ConfigGuiButtonShared open = TrinketsConfig.CLIENT.GUI.BUTTON.open;
                final ConfigGuiButtonShared close = TrinketsConfig.CLIENT.GUI.BUTTON.close;
                final float[] rgbOpen = ColorHelper.getRGBColor(open.COLOR);
                final float[] rgbClose = ColorHelper.getRGBColor(close.COLOR);
                mc.getTextureManager().bindTexture(BUTTON_TEXTURE);
                if (k == 1) {
                    if (this.parentGui instanceof TrinketGui) {
                        DrawingHelper.Draw(x, this.y, 0, close.X, close.Y, close.TEXTURE_WIDTH, close.TEXTURE_HEIGHT, close.BUTTON_WIDTH, close.BUTTON_HEIGHT, close.TEXTURE_ATLAS_WIDTH, close.TEXTURE_ATLAS_HEIGHT, rgbClose[0], rgbClose[1], rgbClose[2], 1F);
                    } else {
                        DrawingHelper.Draw(x, this.y, 0, open.X, open.Y, open.TEXTURE_WIDTH, open.TEXTURE_HEIGHT, open.BUTTON_WIDTH, open.BUTTON_HEIGHT, open.TEXTURE_ATLAS_WIDTH, open.TEXTURE_ATLAS_HEIGHT, rgbOpen[0], rgbOpen[1], rgbOpen[2], 1F);
                    }
                } else {
                    if (this.parentGui instanceof TrinketGui) {
                        DrawingHelper.Draw(x, this.y, 0, open.X, open.Y, open.TEXTURE_WIDTH, open.TEXTURE_HEIGHT, open.BUTTON_WIDTH, open.BUTTON_HEIGHT, open.TEXTURE_ATLAS_WIDTH, open.TEXTURE_ATLAS_HEIGHT, rgbOpen[0], rgbOpen[1], rgbOpen[2], 1F);
                    } else {
                        DrawingHelper.Draw(x, this.y, 0, close.X, close.Y, close.TEXTURE_WIDTH, close.TEXTURE_HEIGHT, close.BUTTON_WIDTH, close.BUTTON_HEIGHT, close.TEXTURE_ATLAS_WIDTH, close.TEXTURE_ATLAS_HEIGHT, rgbClose[0], rgbClose[1], rgbClose[2], 1F);
                    }
                    this.drawCenteredString(fontrenderer, I18n.format(this.displayString), x + 5, this.y + this.height, 0xffffff);
                }
                GlStateManager.popMatrix();
            }
            if (Trinkets.MOD_COMPAT.Baubles && this.id == Reference.BAUBLES_GUI_BUTTON_ID) {
                ResourceLocation tex = BaublesHelperFunctions.getBaublesResourceLocation();
                if (tex != null) {
                    final FontRenderer fontrenderer = mc.fontRenderer;
                    mc.getTextureManager().bindTexture(tex);
                    this.hovered = (mouseX >= x) && (mouseY >= this.y) && (mouseX < (x + this.width)) && (mouseY < (this.y + this.height));
                    final int k = this.getHoverState(this.hovered);
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0, 0, 200);
                    if (k == 1) {
                        this.drawTexturedModalRect(x, this.y, 200, 48, 10, 10);
                    } else {
                        this.drawTexturedModalRect(x, this.y, 210, 48, 10, 10);
                        this.drawCenteredString(fontrenderer, I18n.format(this.displayString), x + 5, this.y + this.height, 0xffffff);
                    }
                    GlStateManager.popMatrix();
                }
            }
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}
