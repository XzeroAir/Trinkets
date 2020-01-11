package xzeroair.trinkets.client.events;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.client.gui.TrinketGui;
import xzeroair.trinkets.client.gui.TrinketGuiButton;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compatibility.BaublesHelperFunctions;

public class GuiEventHandler {

	@SubscribeEvent
	public void GuiAction(GuiScreenEvent.ActionPerformedEvent event) {
	}

	@SubscribeEvent
	public void GuiInit(GuiScreenEvent.InitGuiEvent event) {
	}

	@SubscribeEvent
	public void GuiPreInit(GuiScreenEvent.InitGuiEvent.Pre event) {
	}

	@SubscribeEvent
	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if(TrinketsConfig.SERVER.GUI.guiEnabled) {
			final int ID = TrinketsConfig.CLIENT.GUI.button.ID;
			final int guiTexSize = TrinketsConfig.CLIENT.GUI.guiTexSize;
			final int X = TrinketsConfig.CLIENT.GUI.button.X;
			final int Y = TrinketsConfig.CLIENT.GUI.button.Y;
			final int Xoffset = TrinketsConfig.CLIENT.GUI.button.Xoffset;
			final int Yoffset = TrinketsConfig.CLIENT.GUI.button.Yoffset;
			final int Width = TrinketsConfig.CLIENT.GUI.button.Width;
			final int Height = TrinketsConfig.CLIENT.GUI.button.Height;
			if ((event.getGui() instanceof TrinketGui)) {
				final GuiContainer gui = (GuiContainer) event.getGui();
				event.getButtonList().add(new TrinketGuiButton(ID, gui, X+Xoffset, Y+Yoffset, Width, Height, I18n.format("gui.xat.button.close")));
				//			event.getButtonList().add(new TrinketGuiButton(9999, gui, 0, -20, 16, 16, I18n.format("button.test")));
				if((Loader.isModLoaded("baubles"))) {
					event.getButtonList().add(new TrinketGuiButton(55, gui, 64, 9, 10, 10, I18n.format("button.baubles")));
				}
			} else {
				if((Loader.isModLoaded("baubles"))) {
					BaublesHelperFunctions.guiScreenEventHelper(event);
				} else {
					if(event.getGui() instanceof GuiInventory) {
						final GuiContainer gui = (GuiContainer) event.getGui();
						event.getButtonList().add(new TrinketGuiButton(ID, gui, X, Y, Width, Height, I18n.format("gui.xat.button.open")));
					}
				}
			}
		}
	}

}
