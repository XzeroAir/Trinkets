package xzeroair.trinkets.client.events;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.gui.TrinketGui;
import xzeroair.trinkets.client.gui.TrinketGuiButton;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityPropertiesButton;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.baubles.BaublesHelperFunctions;
import xzeroair.trinkets.util.config.gui.ConfigRacePropertiesGui;

@SideOnly(Side.CLIENT)
public class GuiScreenEvents {

    @SubscribeEvent
    public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        final int ID = TrinketsConfig.CLIENT.GUI.BUTTON.ID;
        final int X = TrinketsConfig.CLIENT.GUI.BUTTON.X;
        final int Y = TrinketsConfig.CLIENT.GUI.BUTTON.Y;
        final int Xoffset = TrinketsConfig.CLIENT.GUI.BUTTON.OFFSET_X;
        final int Yoffset = TrinketsConfig.CLIENT.GUI.BUTTON.OFFSET_Y;
        final int Width = TrinketsConfig.CLIENT.GUI.BUTTON.BUTTON_WIDTH;
        final int Height = TrinketsConfig.CLIENT.GUI.BUTTON.BUTTON_HEIGHT;
        if (TrinketsConfig.getClientStore().TRINKET_CONTAINER_ENABLED) {
            if ((event.getGui() instanceof TrinketGui)) {
                final GuiContainer gui = (GuiContainer) event.getGui();
                event.getButtonList().add(new TrinketGuiButton(ID, gui, X + Xoffset, Y + Yoffset, Width, Height, I18n.format("gui.xat.button.close")));
                if ((Loader.isModLoaded("baubles"))) {
                    event.getButtonList().add(new TrinketGuiButton(55, gui, 64, 9, 10, 10, I18n.format("button.baubles")));
                }
            } else {
                if ((Loader.isModLoaded("baubles"))) {
                    BaublesHelperFunctions.guiScreenEventHelper(event);
                } else {
                    if (event.getGui() instanceof GuiInventory) {
                        final GuiContainer gui = (GuiContainer) event.getGui();
                        event.getButtonList().add(new TrinketGuiButton(ID, gui, X, Y, Width, Height, I18n.format("gui.xat.button.open")));
                    }
                }
            }
        }
        if (TrinketsConfig.CLIENT.raceProperties.ENABLED) {
            if (event.getGui() instanceof GuiInventory) {
                final ConfigRacePropertiesGui config = TrinketsConfig.CLIENT.raceProperties;
                final GuiContainer gui = (GuiContainer) event.getGui();
                event.getButtonList().add(new GuiEntityPropertiesButton(config.button.ID, gui, config.button.X, config.button.Y, config.button.BUTTON_WIDTH, config.button.BUTTON_HEIGHT, I18n.format("gui.xat.button.open")));
            }
        }
    }

}
