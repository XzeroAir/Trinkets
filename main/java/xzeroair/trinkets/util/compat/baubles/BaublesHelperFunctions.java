package xzeroair.trinkets.util.compat.baubles;

import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketOpenBaublesInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import xzeroair.trinkets.client.gui.TrinketGui;
import xzeroair.trinkets.client.gui.TrinketGuiButton;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class BaublesHelperFunctions {

    public static ResourceLocation getBaublesResourceLocation() {
        return GuiPlayerExpanded.background;
    }

    public static void guiScreenEventHelper(GuiScreenEvent.InitGuiEvent.Post event) {
        if ((event.getGui() instanceof GuiInventory) || (event.getGui() instanceof GuiPlayerExpanded)) {
            final GuiContainer gui = (GuiContainer) event.getGui();
            final int ID = TrinketsConfig.CLIENT.GUI.BUTTON.ID;
            final int X = TrinketsConfig.CLIENT.GUI.BUTTON.X;
            final int Y = TrinketsConfig.CLIENT.GUI.BUTTON.Y;
            final int Width = TrinketsConfig.CLIENT.GUI.BUTTON.BUTTON_WIDTH;
            final int Height = TrinketsConfig.CLIENT.GUI.BUTTON.BUTTON_HEIGHT;
            event.getButtonList().add(new TrinketGuiButton(ID, gui, X, Y, Width, Height, I18n.format("gui.xat.button.open")));
        }
    }

    public static void mousePressedHelper(GuiContainer gui, int ID) {
        if (gui instanceof GuiPlayerExpanded) {
            NetworkHandler.sendToServer(new OpenTrinketGui());
        } else {
            if ((gui instanceof TrinketGui) && (ID == Reference.BAUBLES_GUI_BUTTON_ID)) {
                PacketHandler.INSTANCE.sendToServer(new PacketOpenBaublesInventory());
            } else {
                if (ID == TrinketsConfig.CLIENT.GUI.BUTTON.ID) {
                    ((TrinketGui) gui).displayNormalInventory();
                    NetworkHandler.sendToServer(new OpenTrinketGui(Reference.GUI_TRINKETS_EXIT_BUTTON));
                }
            }
        }
    }
}
