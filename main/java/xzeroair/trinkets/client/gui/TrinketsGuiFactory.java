package xzeroair.trinkets.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.Reference;

public class TrinketsGuiFactory extends DefaultGuiFactory {

	public TrinketsGuiFactory() {
		super(Reference.MODID, GuiConfig.getAbridgedConfigPath(Trinkets.config.toString()));
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parent) {
		return new GuiConfig(parent, getConfigElements(), modid, false, false, title);
	}

	private static List<IConfigElement> getConfigElements() {
		final List<IConfigElement> list = new ArrayList<>();

		list.addAll(new ConfigElement(Trinkets.config
				.getCategory(Configuration.CATEGORY_GENERAL))
				.getChildElements());
		list.addAll(new ConfigElement(Trinkets.config
				.getCategory(Configuration.CATEGORY_CLIENT))
				.getChildElements());

		return list;
	}
}
