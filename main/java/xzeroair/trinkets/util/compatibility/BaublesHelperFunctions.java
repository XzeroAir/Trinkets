package xzeroair.trinkets.util.compatibility;

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
import xzeroair.trinkets.network.trinketcontainer.OpenDefaultInventory;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.util.TrinketsConfig;

public class BaublesHelperFunctions {

	public static ResourceLocation getBaublesResourceLocation() {
		return GuiPlayerExpanded.background;
	}

	public static void guiScreenEventHelper(GuiScreenEvent.InitGuiEvent.Post event) {
		if ((event.getGui() instanceof GuiInventory) || (event.getGui() instanceof GuiPlayerExpanded)) {
			final GuiContainer gui = (GuiContainer) event.getGui();
			final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
			final int X = TrinketsConfig.CLIENT.GUI.button.X;
			final int Y = TrinketsConfig.CLIENT.GUI.button.Y;
			final int Width = TrinketsConfig.CLIENT.GUI.button.Width;
			final int Height = TrinketsConfig.CLIENT.GUI.button.Height;
			event.getButtonList().add(new TrinketGuiButton(ID, gui, X, Y, Width, Height, I18n.format("gui.xat.button.open")));
		}
	}

	public static void mousePressedHelper(GuiContainer gui, int ID) {
		if (gui instanceof GuiPlayerExpanded) {
			NetworkHandler.INSTANCE.sendToServer(new OpenTrinketGui());
		} else {
			if ((gui instanceof TrinketGui) && (ID == 55)) {
				PacketHandler.INSTANCE.sendToServer(new PacketOpenBaublesInventory());
			} else {
				if (ID == 9999) {

				} else {
					((TrinketGui) gui).displayNormalInventory();
					NetworkHandler.INSTANCE.sendToServer(new OpenDefaultInventory());
				}
			}
		}
	}
}
