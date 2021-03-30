package xzeroair.trinkets.client.events;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.client.gui.TrinketGui;
import xzeroair.trinkets.client.gui.TrinketGuiButton;
import xzeroair.trinkets.client.gui.entityPropertiesGui.GuiEntityPropertiesButton;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.baubles.BaublesHelperFunctions;
import xzeroair.trinkets.util.config.gui.TrinketsPropertiesConfig;

public class GuiScreenEvents {

	@SubscribeEvent
	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
		final int X = TrinketsConfig.CLIENT.GUI.button.X;
		final int Y = TrinketsConfig.CLIENT.GUI.button.Y;
		final int Xoffset = TrinketsConfig.CLIENT.GUI.button.Xoffset;
		final int Yoffset = TrinketsConfig.CLIENT.GUI.button.Yoffset;
		final int Width = TrinketsConfig.CLIENT.GUI.button.bWidth;
		final int Height = TrinketsConfig.CLIENT.GUI.button.bHeight;
		if (TrinketsConfig.SERVER.GUI.guiEnabled) {
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
		if (TrinketsConfig.CLIENT.raceProperties.shown) {
			if (event.getGui() instanceof GuiInventory) {
				TrinketsPropertiesConfig config = TrinketsConfig.CLIENT.raceProperties;
				final GuiContainer gui = (GuiContainer) event.getGui();
				event.getButtonList().add(new GuiEntityPropertiesButton(config.button.ID, gui, config.button.X, config.button.Y, config.button.bWidth, config.button.bHeight, I18n.format("gui.xat.button.open")));
			} else {
				//				if (event.getGui() instanceof ManaHud) {
				//					final GuiContainer gui = (GuiContainer) event.getGui();
				//					event.getButtonList().add(new ManaHudButton(69, gui, X - 40, X - 40, 16, 16, I18n.format("gui.xat.button.close")));
				//				}
			}
		}
	}

}
