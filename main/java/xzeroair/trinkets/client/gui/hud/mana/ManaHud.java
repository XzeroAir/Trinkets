package xzeroair.trinkets.client.gui.hud.mana;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import xzeroair.trinkets.client.gui.ITrinketGuiInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ManaHud extends GuiScreen implements ITrinketGuiInterface {

    public static ResourceLocation background = null;

    private float oldMouseX;
    private float oldMouseY;

    public ManaHud() {
        this.allowUserInput = true;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void initGui() {
        //		buttonList.clear();
        super.initGui();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        this.mc.player.closeScreen();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;//super.doesGuiPauseGame();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.oldMouseX = mouseX;
        this.oldMouseY = mouseY;
        if (TrinketsConfig.CLIENT.MANA_BAR_HUD.usePixelPosition) {
            TrinketsConfig.CLIENT.MANA_BAR_HUD.xPixels = mouseX;
            TrinketsConfig.CLIENT.MANA_BAR_HUD.yPixels = mouseY;
        } else {
            TrinketsConfig.CLIENT.MANA_BAR_HUD.translatedX = ((mouseX * 100D) / this.width) * 0.01D;
            TrinketsConfig.CLIENT.MANA_BAR_HUD.translatedY = ((mouseY * 100D) / this.height) * 0.01D;
        }
        //		this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        //		drawHoveringText(text, mouseX, y);
        //		this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
        ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        super.keyTyped(par1, par2);
    }

    public void displayNormalInventory() {
        final GuiInventory gui = new GuiInventory(this.mc.player);
        this.mc.displayGuiScreen(gui);
    }
}